package simulation;

import protonova.protobuf.TileProto.Tile;

public final class TileMovement {

	private static final float NORMAL_DIFFICULTY = 1.0f;
	private static final float LEGACY_WATER_DIFFICULTY = 2.0f;

	private TileMovement() {}

	public static float getDifficulty(Tile tile) {
		if (tile == null) return NORMAL_DIFFICULTY;
		float difficulty = tile.getMovementDifficulty();
		if (Float.isFinite(difficulty) && difficulty > 0) return difficulty;
		if ("water".equalsIgnoreCase(tile.getName())) return LEGACY_WATER_DIFFICULTY;
		return NORMAL_DIFFICULTY;
	}

	public static float getSpeedMultiplier(Tile tile) {
		return 1.0f / getDifficulty(tile);
	}
}
