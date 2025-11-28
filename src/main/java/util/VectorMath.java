package util;

import protonova.protobuf.VectorProto.Vector;

public class VectorMath {

	/**
	 * Gets the distance between two vectors using distance formula
	 * sqrt((x1-x2)^2 + (y1-y2)^2)
	 */
	public static double distance(Vector position1, Vector position2) {
		
		return Math.sqrt(Math.pow(position1.getX()-position2.getX(), 2) + Math.pow(position1.getY()-position2.getY(), 2));
		
	}
	
	/**
	 * Gets the magnitude from the vector
	 * Also known as the distance from 0,0
	 * sqrt(x^2+y^2)
	 */
	public static double magnitude(Vector position) {
		return Math.sqrt(Math.pow(position.getX(), 2) + Math.pow(position.getY(), 2));
	}
	
	/**
	 * Returns the given vector as a unit vector with a magnitude of 1
	 * x = x / magnitude
	 * y = y / magnitude	
	 */
	public static Vector unitVector(Vector position) {
		
		double magnitude = magnitude(position);
		
		return Vector.newBuilder()
				.setX((float) (position.getX()/magnitude))
				.setY((float) (position.getY()/magnitude))
				.build();
	}
	
	/**
	 * Returns the scalar in radians 0-PI between two vectors also know the the angle of change
	 * arccos((x1*x2+y1*y2)/ (magnitude(v1)*magnitude(v2)))
	 */
	public static double scalar(Vector position1, Vector position2) {
		
		double dotProduct = position1.getX() * position2.getX() + position1.getY() * position2.getY();
		
		return Math.acos(dotProduct / (magnitude(position1) * magnitude(position2)));
	}
	
	/**
	 * Returns the distance of two points as a vector
	 * x = x1-x2
	 * y = y1-y2
	 */	
	public static Vector vectorDistance(Vector position1, Vector position2) {
		return Vector.newBuilder()
				.setX(position1.getX()-position2.getX())
				.setY(position1.getY()-position2.getY())
				.build();
	}
	
	/**
	 * Returns the normal of a vector also known as the same vector but perpendicular
	 * x = -y
	 * y = x
	 */
	public static Vector normal(Vector position) {
		return Vector.newBuilder()
				.setX(-position.getY())
				.setY(position.getX())
				.build();
	}
}
