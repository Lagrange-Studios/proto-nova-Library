package character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Backward-compatible wire encoding for cosmetic character appearances.
 * The value travels as a namespaced entity tag, so older protocol builds
 * safely preserve or ignore it without granting clients control of gameplay tags.
 */
public final class CharacterAppearanceCodec {
    public static final String PREFIX = "character:v1:";
    public static final String DEFAULT = PREFIX + "default";
    private static final int MAX_ENCODED_LENGTH = 128;

    private CharacterAppearanceCodec() {}

    public static String encode(int skinRgb, int hairStyle, int hairRgb,
            int underpantsRgb, Set<Integer> scars) {
        if (hairStyle < 0 || hairStyle > 3) throw new IllegalArgumentException("Invalid hair style.");
        StringBuilder result = new StringBuilder(PREFIX)
                .append(rgbHex(skinRgb)).append(':')
                .append(hairStyle).append(':')
                .append(rgbHex(hairRgb)).append(':')
                .append(rgbHex(underpantsRgb)).append(':');
        boolean first = true;
        if (scars != null) {
            for (Integer scar : new java.util.TreeSet<>(scars)) {
                if (scar == null || scar < 1 || scar > 10) throw new IllegalArgumentException("Invalid scar style.");
                if (!first) result.append(',');
                result.append(scar);
                first = false;
            }
        }
        if (result.length() > MAX_ENCODED_LENGTH) throw new IllegalArgumentException("Appearance is too large.");
        return result.toString();
    }

    public static Appearance decode(String encoded) {
        if (encoded == null || encoded.length() > MAX_ENCODED_LENGTH || !encoded.startsWith(PREFIX)
                || DEFAULT.equals(encoded)) return null;
        String[] fields = encoded.substring(PREFIX.length()).split(":", -1);
        if (fields.length != 5 || !fields[1].matches("[0-3]")) return null;
        try {
            int skin = parseRgb(fields[0]);
            int hairStyle = Integer.parseInt(fields[1]);
            int hair = parseRgb(fields[2]);
            int underpants = parseRgb(fields[3]);
            LinkedHashSet<Integer> scars = new LinkedHashSet<>();
            if (!fields[4].isEmpty()) {
                String[] values = fields[4].split(",", -1);
                if (values.length > 10) return null;
                for (String value : values) {
                    int scar = Integer.parseInt(value);
                    if (scar < 1 || scar > 10 || !scars.add(scar)) return null;
                }
            }
            return new Appearance(skin, hairStyle, hair, underpants, new ArrayList<>(scars));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static boolean isAppearanceTag(String value) {
        return value != null && value.startsWith(PREFIX);
    }

    public static boolean isValidUpdate(String value) {
        return DEFAULT.equals(value) || decode(value) != null;
    }

    private static String rgbHex(int rgb) {
        return String.format(Locale.ROOT, "%06X", rgb & 0xFFFFFF);
    }

    private static int parseRgb(String value) {
        if (!value.matches("[0-9A-Fa-f]{6}")) throw new NumberFormatException();
        return Integer.parseInt(value, 16);
    }

    public static final class Appearance {
        private final int skinRgb;
        private final int hairStyle;
        private final int hairRgb;
        private final int underpantsRgb;
        private final List<Integer> scars;

        private Appearance(int skinRgb, int hairStyle, int hairRgb, int underpantsRgb, List<Integer> scars) {
            this.skinRgb = skinRgb;
            this.hairStyle = hairStyle;
            this.hairRgb = hairRgb;
            this.underpantsRgb = underpantsRgb;
            this.scars = Collections.unmodifiableList(scars);
        }

        public int getSkinRgb() { return skinRgb; }
        public int getHairStyle() { return hairStyle; }
        public int getHairRgb() { return hairRgb; }
        public int getUnderpantsRgb() { return underpantsRgb; }
        public List<Integer> getScars() { return scars; }
    }
}
