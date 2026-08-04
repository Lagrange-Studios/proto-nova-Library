package util;

import protonova.protobuf.AudioProto.Audio;
import protonova.protobuf.AudioProto.AudioType;
import protonova.protobuf.VectorProto.Vector;

public final class AudioBuilder {

	private AudioBuilder() {}

	public static Audio createSoundEffect(String name, Vector position, int map) {
		return createSoundEffect(name, position, map, 1.0f);
	}

	public static Audio createSoundEffect(String name, Vector position, int map, float volume) {
		return base(name, AudioType.SOUND_EFFECT, map, volume).setPosition(position).build();
	}

	public static Audio createSoundEffectAtEntity(String name, int entityId, int map) {
		return createSoundEffectAtEntity(name, entityId, map, 1.0f);
	}

	public static Audio createSoundEffectAtEntity(String name, int entityId, int map, float volume) {
		return base(name, AudioType.SOUND_EFFECT, map, volume).setEntityID(entityId).build();
	}

	public static Audio createLoopingSoundEffectAtEntity(String name, int entityId, int map, float volume) {
		return base(name, AudioType.SOUND_EFFECT, map, volume)
				.setEntityID(entityId).setLooping(true).build();
	}

	public static Audio createAmbientSound(String name, Vector position, int map) {
		return createAmbientSound(name, position, map, 1.0f);
	}

	public static Audio createAmbientSound(String name, Vector position, int map, float volume) {
		return base(name, AudioType.AMBIENT, map, volume).setPosition(position).build();
	}

	public static Audio createLoopingAmbientSound(String name, Vector position, int map, float volume) {
		return base(name, AudioType.AMBIENT, map, volume)
				.setPosition(position).setLooping(true).build();
	}

	public static Audio createMusic(String name, int map) {
		return createMusic(name, map, 1.0f);
	}

	public static Audio createMusic(String name, int map, float volume) {
		return base(name, AudioType.MUSIC, map, volume).build();
	}

	public static Audio createLoopingMusic(String name, int map, float volume) {
		return base(name, AudioType.MUSIC, map, volume).setLooping(true).build();
	}

	public static Audio createVoice(String name, int entityId, int map) {
		return createVoice(name, entityId, map, 1.0f);
	}

	public static Audio createVoice(String name, int entityId, int map, float volume) {
		return base(name, AudioType.VOICE, map, volume).setEntityID(entityId).build();
	}

	public static Audio stopLoop(String name, int entityId, int map) {
		return stopLoop(name, AudioType.SOUND_EFFECT, entityId, map);
	}

	public static Audio stopLoop(String name, AudioType type, int map) {
		return base(name, type, map, 0).setStop(true).build();
	}

	public static Audio stopLoop(String name, AudioType type, int entityId, int map) {
		return base(name, type, map, 0).setEntityID(entityId).setStop(true).build();
	}

	public static float getVolumeAsFloat(Audio audio) {
		return audio == null ? 0 : clampVolume(audio.getVolume() / 100.0f);
	}

	public static String getSoundFilePath(String soundName) {
		return getFilePath(AudioType.SOUND_EFFECT, soundName);
	}

	public static String getFilePath(Audio audio) {
		if (audio == null) throw new IllegalArgumentException("Audio is required");
		return getFilePath(audio.getAudioType(), audio.getName());
	}

	public static String getFilePath(AudioType type, String soundName) {
		String safeName = requireName(soundName);
		String folder;
		switch (type) {
			case AMBIENT: folder = "ambient"; break;
			case MUSIC: folder = "music"; break;
			case VOICE: folder = "voice"; break;
			default: folder = "soundEffects"; break;
		}
		return "assets/audio/" + folder + "/" + safeName + ".wav";
	}

	private static Audio.Builder base(String name, AudioType type, int map, float volume) {
		return Audio.newBuilder()
				.setName(requireName(name))
				.setAudioType(type)
				.setMap(map)
				.setVolume(Math.round(clampVolume(volume) * 100));
	}

	private static String requireName(String name) {
		if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Sound name is required");
		String value = name.trim();
		if (value.contains("/") || value.contains("\\") || value.contains("..")) {
			throw new IllegalArgumentException("Sound name must not contain a path");
		}
		return value;
	}

	private static float clampVolume(float volume) {
		if (!Float.isFinite(volume)) return 0;
		return Math.max(0, Math.min(1, volume));
	}
}
