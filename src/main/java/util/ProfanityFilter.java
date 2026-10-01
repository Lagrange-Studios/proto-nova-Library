package util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lightweight profanity filter that can be used on both the client and server.
 *
 * <p>Supports normal profanity, punctuation evasion, leetspeak, repeated letters, scrambled
 * letters, and profanity split across multiple messages.
 *
 * <p>Username history is case-sensitive. Message detection is case-insensitive.
 *
 * <p>Example:
 *
 * <pre>
 * ProfanityFilter filter = new ProfanityFilter();
 * String result = filter.filter("PlayerName", "Hello everyone!");
 * </pre>
 */
public class ProfanityFilter {

  private static final int MAX_HISTORY_MESSAGES = 6;
  private static final long HISTORY_TIME_MS = 15_000L;

  private static final int BASE_THRESHOLD = 10;
  private static final int MAX_LENGTH_THRESHOLD_BONUS = 8;

  private static final Pattern WORD_PATTERN = Pattern.compile("[A-Za-z0-9]+");

  private final Map<String, String> blacklist = createBlacklist();
  private final Map<Character, Character> leetMap = createLeetMap();
  private final Set<String> contextWords = createContextWords();

  private final Map<String, PlayerHistory> playerHistory = new HashMap<>();

  /**
   * Filters a chat message for the specified username.
   *
   * @param username player's username; case-sensitive
   * @param message message to filter
   * @return filtered message, or the original message if no profanity is detected with enough
   *     confidence
   */
  public synchronized String filter(String username, String message) {

    if (username == null || username.isEmpty() || message == null || message.isEmpty()) {
      return message;
    }

    PlayerHistory history = playerHistory.computeIfAbsent(username, key -> new PlayerHistory());

    removeExpiredMessages(history);

    AnalysisResult current = analyzeMessage(message);

    AnalysisResult previous = analyzeHistory(history, message);

    int score = current.score + previous.score;
    int threshold = calculateThreshold(message);

    history.messages.addLast(new ChatMessage(message, System.currentTimeMillis()));

    trimHistory(history);

    if (score < threshold) {
      return message;
    }

    return replaceProfanity(message);
  }

  /**
   * Clears the stored message history for one username.
   *
   * <p>Useful when a player disconnects from the server or leaves the game.
   *
   * @param username username whose history should be removed
   */
  public synchronized void removePlayer(String username) {
    if (username != null) {
      playerHistory.remove(username);
    }
  }

  /**
   * Clears all stored player history.
   *
   * <p>Useful when shutting down or resetting the chat system.
   */
  public synchronized void clearHistory() {
    playerHistory.clear();
  }

  /**
   * Analyzes a message for profanity and common filter-evasion techniques.
   *
   * @param message message to analyze
   * @return analysis result containing the calculated suspicion score
   */
  private AnalysisResult analyzeMessage(String message) {

    int score = 0;

    Matcher matcher = WORD_PATTERN.matcher(message);

    while (matcher.find()) {

      String word = matcher.group();
      String normalized = normalize(word);

      if (blacklist.containsKey(normalized)) {
        score += 10;
        continue;
      }

      String compressed = removeRepeatedCharacters(normalized);

      if (!compressed.equals(normalized) && blacklist.containsKey(compressed)) {
        score += 7;
      }

      String leet = normalizeLeetspeak(normalized);

      if (!leet.equals(normalized) && blacklist.containsKey(leet)) {
        score += 8;
      }

      if (normalized.length() >= 5 && isScrambledProfanity(normalized)) {
        score += 6;
      }
    }

    if (containsSeparatedProfanity(message)) {
      score += 7;
    }

    int matches = countProfanityMatches(message);

    if (matches > 1) {
      score += (matches - 1) * 4;
    }

    if (containsInsultContext(message)) {
      score += 2;
    }

    if (containsContextWord(message)) {
      score -= 2;
    }

    if (hasExcessivePunctuation(message)) {
      score++;
    }

    return new AnalysisResult(Math.max(score, 0));
  }

  /**
   * Checks the player's recent messages for profanity split across multiple messages.
   *
   * @param history player's recent message history
   * @param currentMessage current message
   * @return additional suspicion score from message history
   */
  private AnalysisResult analyzeHistory(PlayerHistory history, String currentMessage) {

    if (history.messages.isEmpty()) {
      return new AnalysisResult(0);
    }

    StringBuilder combined = new StringBuilder();

    for (ChatMessage message : history.messages) {
      combined.append(removeDetectionSeparators(normalize(message.text)));
    }

    combined.append(removeDetectionSeparators(normalize(currentMessage)));

    String combinedText = combined.toString();

    for (String profanity : blacklist.keySet()) {

      if (profanity.length() >= 3 && combinedText.contains(profanity)) {

        return new AnalysisResult(6);
      }
    }

    return new AnalysisResult(0);
  }

  /**
   * Replaces profanity in a message with its configured replacement.
   *
   * <p>Replacement only occurs after the complete message has reached the filter's confidence
   * threshold.
   *
   * @param message original message
   * @return message with detected profanity replaced
   */
  private String replaceProfanity(String message) {

    String result = message;

    for (Map.Entry<String, String> entry : blacklist.entrySet()) {

      String profanity = entry.getKey();
      String replacement = entry.getValue();

      Pattern pattern =
          Pattern.compile("(?i)(?<![A-Za-z0-9])" + Pattern.quote(profanity) + "(?![A-Za-z0-9])");

      result = pattern.matcher(result).replaceAll(Matcher.quoteReplacement(replacement));
    }

    return result;
  }

  /**
   * Calculates the required score based on message length.
   *
   * @param message message being evaluated
   * @return score required before the message is modified
   */
  private int calculateThreshold(String message) {

    int wordCount = 0;

    Matcher matcher = WORD_PATTERN.matcher(message);

    while (matcher.find()) {
      wordCount++;
    }

    int bonus = Math.min(wordCount / 10, MAX_LENGTH_THRESHOLD_BONUS);

    return BASE_THRESHOLD + bonus;
  }

  /**
   * Normalizes text for profanity detection without modifying the original message.
   *
   * @param text text to normalize
   * @return lowercase normalized text
   */
  private String normalize(String text) {
    return text.toLowerCase();
  }

  /**
   * Converts common leetspeak characters into their normal equivalents.
   *
   * @param text text containing possible leetspeak
   * @return normalized text
   */
  private String normalizeLeetspeak(String text) {

    StringBuilder result = new StringBuilder();

    for (char character : text.toCharArray()) {

      Character replacement = leetMap.get(character);

      result.append(replacement != null ? replacement : character);
    }

    return result.toString();
  }

  /**
   * Removes repeated consecutive characters.
   *
   * <p>For example, "fuuuck" becomes "fuck".
   *
   * @param text text to compress
   * @return text with repeated characters reduced
   */
  private String removeRepeatedCharacters(String text) {

    if (text.length() < 2) {
      return text;
    }

    StringBuilder result = new StringBuilder();
    char previous = 0;

    for (char character : text.toCharArray()) {

      if (character != previous) {
        result.append(character);
        previous = character;
      }
    }

    return result.toString();
  }

  /**
   * Checks whether a word contains the same letters as a blacklisted word in a different order.
   *
   * @param word word to check
   * @return true if the word appears to be scrambled profanity
   */
  private boolean isScrambledProfanity(String word) {

    String sortedWord = sortCharacters(word);

    for (String profanity : blacklist.keySet()) {

      if (profanity.length() != word.length() || profanity.length() < 5) {
        continue;
      }

      if (sortedWord.equals(sortCharacters(profanity))) {
        return true;
      }
    }

    return false;
  }

  /**
   * Sorts the characters in a string for scrambled-word comparison.
   *
   * @param text text to sort
   * @return sorted characters
   */
  private String sortCharacters(String text) {

    char[] characters = text.toCharArray();

    java.util.Arrays.sort(characters);

    return new String(characters);
  }

  /**
   * Detects profanity where separators have been inserted between letters.
   *
   * @param message message to inspect
   * @return true if separated profanity is detected
   */
  private boolean containsSeparatedProfanity(String message) {

    String normalized = normalize(message);

    String compact = removeDetectionSeparators(normalized);

    for (String profanity : blacklist.keySet()) {

      if (profanity.length() < 3) {
        continue;
      }

      if (compact.contains(profanity) && !normalized.contains(profanity)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Removes characters commonly used to separate letters for evasion.
   *
   * @param text text to process
   * @return text without detection separators
   */
  private String removeDetectionSeparators(String text) {

    StringBuilder result = new StringBuilder();

    for (char character : text.toCharArray()) {

      if (!Character.isWhitespace(character) && ".,-_/*\\|".indexOf(character) == -1) {

        result.append(character);
      }
    }

    return result.toString();
  }

  /**
   * Counts direct profanity matches in a message.
   *
   * @param message message to inspect
   * @return number of detected profanity words
   */
  private int countProfanityMatches(String message) {

    int count = 0;

    Matcher matcher = WORD_PATTERN.matcher(message);

    while (matcher.find()) {

      String word = normalize(matcher.group());

      if (blacklist.containsKey(word)) {
        count++;
      }
    }

    return count;
  }

  /**
   * Detects whether profanity appears near common insulting language.
   *
   * @param message message to inspect
   * @return true when insulting context is detected
   */
  private boolean containsInsultContext(String message) {

    String normalized = normalize(message);

    return normalized.contains("you ")
        || normalized.contains("your ")
        || normalized.startsWith("you")
        || normalized.endsWith("you");
  }

  /**
   * Checks for words that can provide innocent context around an otherwise ambiguous match.
   *
   * @param message message to inspect
   * @return true when contextual words are present
   */
  private boolean containsContextWord(String message) {

    Matcher matcher = WORD_PATTERN.matcher(normalize(message));

    while (matcher.find()) {

      if (contextWords.contains(matcher.group())) {
        return true;
      }
    }

    return false;
  }

  /**
   * Detects excessive punctuation that may indicate filter evasion.
   *
   * @param message message to inspect
   * @return true if excessive punctuation is present
   */
  private boolean hasExcessivePunctuation(String message) {

    int punctuation = 0;

    for (char character : message.toCharArray()) {

      if (!Character.isLetterOrDigit(character) && !Character.isWhitespace(character)) {
        punctuation++;
      }
    }

    return punctuation >= 6;
  }

  /**
   * Removes expired messages from a player's history.
   *
   * @param history player history to clean
   */
  private void removeExpiredMessages(PlayerHistory history) {

    long currentTime = System.currentTimeMillis();

    while (!history.messages.isEmpty()) {

      ChatMessage message = history.messages.peekFirst();

      if (currentTime - message.timestamp <= HISTORY_TIME_MS) {
        break;
      }

      history.messages.removeFirst();
    }
  }

  /**
   * Ensures a player's history never exceeds the configured size.
   *
   * @param history player history to trim
   */
  private void trimHistory(PlayerHistory history) {

    while (history.messages.size() > MAX_HISTORY_MESSAGES) {

      history.messages.removeFirst();
    }
  }

  /**
   * Creates the profanity blacklist and their replacement words.
   *
   * @return profanity replacement map
   */
  private Map<String, String> createBlacklist() {

    Map<String, String> map = new HashMap<>();

    map.put("fuck", "frick");
    map.put("fucking", "fricking");
    map.put("fucked", "fricked");

    map.put("shit", "crap");
    map.put("shitty", "crappy");

    map.put("damn", "dang");
    map.put("dammit", "dang it");

    map.put("hell", "heck");

    map.put("asshole", "jerk");
    map.put("asses", "butts");
    map.put("ass", "butt");

    map.put("bitch", "jerk");

    return map;
  }

  /**
   * Creates the leetspeak conversion table used during detection.
   *
   * @return leetspeak conversion map
   */
  private Map<Character, Character> createLeetMap() {

    Map<Character, Character> map = new HashMap<>();

    map.put('0', 'o');
    map.put('1', 'i');
    map.put('2', 'z');
    map.put('3', 'e');
    map.put('4', 'a');
    map.put('5', 's');
    map.put('6', 'g');
    map.put('7', 't');
    map.put('8', 'b');
    map.put('9', 'g');

    map.put('@', 'a');
    map.put('$', 's');
    map.put('!', 'i');

    return map;
  }

  /**
   * Creates lightweight contextual words used to reduce false positives.
   *
   * <p>This is not a whitelist. These words only slightly reduce the suspicion score.
   *
   * @return contextual word set
   */
  private Set<String> createContextWords() {

    Set<String> words = new HashSet<>();

    words.add("fish");
    words.add("fishing");
    words.add("bass");
    words.add("class");
    words.add("classic");
    words.add("classes");
    words.add("assistant");
    words.add("assistance");
    words.add("assessment");
    words.add("pass");
    words.add("passing");
    words.add("password");

    return words;
  }

  /** Stores the recent messages for one player. */
  private static class PlayerHistory {

    private final Deque<ChatMessage> messages = new ArrayDeque<>();
  }

  /** Represents one message stored in player history. */
  private record ChatMessage(String text, long timestamp) {}

  /** Stores the result of analyzing a message. */
  private record AnalysisResult(int score) {}
}
