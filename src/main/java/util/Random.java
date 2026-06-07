package util;

import java.util.HashSet;

import protonova.protobuf.EntityProto.Entity;
import protonova.protobuf.PlaneProto.Plane;
import protonova.protobuf.TileProto.Tile;

public class Random {

	public static int randomInt(int lowerBound, int upperBound) {
		return (int) (Math.round(Math.random()*(upperBound-lowerBound)) + lowerBound);
	}
	
	public static Tile findRandomTile(Plane plane, HashSet<String> allowedTiles) {
		Tile[] tileArray = plane.getTilesMap().values().toArray(new Tile[0]);
		int count = 0;
		
		while (count < 1000) {
			Tile tile = tileArray[randomInt(0,tileArray.length)];
			if (allowedTiles.contains(tile.getName())) return tile;
			else count++;
		}
		return null;
	}
	
	public static Entity randomizeDirection(Entity entity) {
		entity = entity.toBuilder()
				.setDirectionValue(randomInt(0,3))
				.build();
		return entity;
	}
}
