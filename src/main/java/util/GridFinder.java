package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import protonova.protobuf.CoordinateProto.Coordinate;
import protonova.protobuf.EntityProto.Entity;
import protonova.protobuf.GridProto.Grid;
import protonova.protobuf.TileProto.Tile;
import protonova.protobuf.VectorProto.Vector;

/**
 * Spatial lookup helpers for movable grids.
 *
 * Grid positions are treated as world-space origins. Coordinates in
 * {@code Grid.tiles} are treated as local offsets from that origin.
 */
public final class GridFinder {

	private GridFinder() {}

	/** Finds the first grid whose tile area contains the entity's center. */
	public static Grid findGridUnder(Entity entity, Iterable<Grid> grids) {
		if (entity == null || grids == null) return null;
		for (Grid grid : grids) {
			if (findTileUnder(entity, grid) != null) return grid;
		}
		return null;
	}

	/** Finds every overlapping grid under the entity's center. */
	public static List<Grid> findGridsUnder(Entity entity, Iterable<Grid> grids) {
		if (entity == null || grids == null) return Collections.emptyList();
		List<Grid> matches = new ArrayList<>();
		for (Grid grid : grids) {
			if (findTileUnder(entity, grid) != null) matches.add(grid);
		}
		return matches;
	}

	/** Finds the local grid tile under the entity's world-space center. */
	public static Tile findTileUnder(Entity entity, Grid grid) {
		if (entity == null || grid == null || entity.getMap() != grid.getMap()) return null;
		return TileFinder.findTileAt(toLocalTileCoordinate(entity.getPosition(), grid), grid.getTilesMap());
	}

	/** Converts a world position to the nearest local tile coordinate on a grid. */
	public static Coordinate toLocalTileCoordinate(Vector worldPosition, Grid grid) {
		if (worldPosition == null || grid == null) return null;
		Vector localPosition = CoordinateConverter.toLocalPosition(worldPosition, grid.getPosition());
		return CoordinateConverter.toTileCoordinates(localPosition);
	}
}
