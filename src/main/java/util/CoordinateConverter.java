package util;

import protonova.protobuf.CoordinateProto.Coordinate;
import protonova.protobuf.VectorProto.Vector;

public class CoordinateConverter {

	public static final int CHUNK_SIZE = 10;
	
	public static String convert(String x, String y) {		
		return x + "," + y;
	}
	
	public static String convert(Vector position) {
		return convert(toTileCoordinates(position));
	}
	
	public static String convert(Coordinate coordinate) {
		String x = String.valueOf(coordinate.getX());
		String y = String.valueOf(coordinate.getY());
		
		return x + "," + y;
	}
	
	public static Coordinate toChunkCoordinates(Vector vector) {
		Coordinate coordinate = Coordinate.newBuilder()
				.setX(Math.round(vector.getX()/CHUNK_SIZE))
				.setY(Math.round(vector.getY()/CHUNK_SIZE))
				.build();
		
		return coordinate;
	}

	/** Converts a world or local position to the nearest tile-center coordinate. */
	public static Coordinate toTileCoordinates(Vector vector) {
		if (vector == null) return null;
		return Coordinate.newBuilder()
				.setX(Math.round(vector.getX()))
				.setY(Math.round(vector.getY()))
				.build();
	}

	/** Converts a world position into a position relative to an origin. */
	public static Vector toLocalPosition(Vector worldPosition, Vector origin) {
		if (worldPosition == null || origin == null) return null;
		return Vector.newBuilder()
				.setX(worldPosition.getX() - origin.getX())
				.setY(worldPosition.getY() - origin.getY())
				.build();
	}
	
	public static Vector toVector(Coordinate coordinate) {
		Vector vector = Vector.newBuilder()
				.setX(coordinate.getX())
				.setY(coordinate.getY())
				.build();
				
		return vector;
	}
	
	public static Coordinate toCoordinate(int x, int y) {
		return Coordinate.newBuilder()
				.setX(x)
				.setY(y)
				.build();
	}
	
	public static Coordinate toCoordinate(float x, float y) {
		return toCoordinate(Math.round(x),Math.round(y));
	}
}
