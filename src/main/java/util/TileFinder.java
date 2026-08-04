package util;

import java.util.Map;

import protonova.protobuf.CoordinateProto.Coordinate;
import protonova.protobuf.EntityProto.Entity;
import protonova.protobuf.PlaneProto.Plane;
import protonova.protobuf.TileProto.Tile;
import protonova.protobuf.VectorProto.Vector;

/** Shared tile lookup helpers for world and local coordinate spaces. */
public final class TileFinder {

	private TileFinder() {}

	/** Finds the tile under the center position of an entity on a plane. */
	public static Tile findTileUnder(Entity entity, Plane plane) {
		if (entity == null || plane == null || entity.getMap() != plane.getId()) return null;
		return findTileAt(entity.getPosition(), plane.getTilesMap());
	}

	/** Finds the tile nearest to a floating-point position. */
	public static Tile findTileAt(Vector position, Map<String, Tile> tiles) {
		if (position == null) return null;
		return findTileAt(CoordinateConverter.toTileCoordinates(position), tiles);
	}

	/** Finds a tile using its exact integer coordinate. */
	public static Tile findTileAt(Coordinate coordinate, Map<String, Tile> tiles) {
		if (coordinate == null || tiles == null) return null;
		return tiles.get(CoordinateConverter.convert(coordinate));
	}
}
