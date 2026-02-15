package collision;

import java.util.ArrayList;

import protonova.protobuf.EntityProto.Entity;
import protonova.protobuf.VectorProto.Vector;
import util.VectorMath;

public class EntityCollision {

	private static int roundPlaces = 1000000000;
	
	private static ArrayList<Vector> getCorners(Entity entity) {
		
		ArrayList<Vector> corners = new ArrayList<Vector>();
		
		float centerX = entity.getPosition().getX();
		float centerY = entity.getPosition().getY();
		
		float sizeX = entity.getSize().getX();
		float sizeY = entity.getSize().getY();
		
		//TODO: take into account entity rotation
		
		
		corners.add(Vector.newBuilder()
				.setX((float) (centerX + (0.5*-sizeX)))
				.setY((float) (centerY + (0.5*-sizeY)))
				.build());
		corners.add(Vector.newBuilder()
				.setX((float) (centerX + (0.5*sizeX)))
				.setY((float) (centerY + (0.5*-sizeY)))
				.build());
		corners.add(Vector.newBuilder()
				.setX((float) (centerX + (0.5*sizeX)))
				.setY((float) (centerY + (0.5*sizeY)))
				.build());
		corners.add(Vector.newBuilder()
				.setX((float) (centerX + (0.5*-sizeX)))
				.setY((float) (centerY + (0.5*sizeY)))
				.build());
		
		
		return corners;
	}
	
	private static ArrayList<Vector> getEdgeVectors(ArrayList<Vector> corners) {
		
		ArrayList<Vector> edgeVectors = new ArrayList<Vector>();
		
		for (int i=0;i<corners.size();i++) {
			Vector corner = corners.get(i);
			Vector corner2 = corners.get(i+1!=corners.size()?i+1:0); // I'm sorry -F
			
			edgeVectors.add(VectorMath.vectorDistance(corner, corner2));
		}
		return edgeVectors;
	}
	
	// Could optimize if we stick with rectangle collision boxes
	private static ArrayList<Vector> getNomralVectors(ArrayList<Vector> edgeVectors) {
		
		ArrayList<Vector> normalVectors = new ArrayList<Vector>();
		
		for (int i=0;i<edgeVectors.size();i++) {
			Vector newNormal = VectorMath.normal(edgeVectors.get(i));
			normalVectors.add(newNormal);
		}
		
		return normalVectors;
	}
	
	private static double projectVectorToAxis(Vector vector, Vector axis) {
		double change = VectorMath.scalar(vector, axis);
		
		// rounded for some reason that might bite me back later idk tho havent slept in like 32 hours and im four monsters deep
		//return Math.round((VectorMath.magnitude(vector)/Math.sin(90*Math.PI/180))*Math.sin((90*Math.PI/180)-change)*roundPlaces)/roundPlaces;
		return ((VectorMath.magnitude(vector)/Math.sin(90*Math.PI/180))*Math.sin((90*Math.PI/180)-change));
				
	}
	
	
	private static double[] findOuterPointsOnAxis(Vector axis, Object[] points) {
		
		double[] minMax = {Double.MAX_VALUE,-Double.MAX_VALUE};
		
		for (int i=0;i<points.length;i++) {
			double projectedPoint = projectVectorToAxis((Vector) points[i],axis);
			
			minMax[0] = Math.min(projectedPoint, minMax[0]);
			minMax[1] = Math.max(projectedPoint, minMax[1]);
		}
		
		return minMax;
	}
	
	private static ArrayList<Vector> removeDoubleAxis(ArrayList<Vector> axis) {
		
		ArrayList<Vector> returningList = new ArrayList<Vector>();
		
		for (int i=0;i<axis.size()-1;i++) {
			for (int u=i+1;u<axis.size();u++) {
				double scalarValue = VectorMath.scalar(axis.get(i), axis.get(u));
				
				if (scalarValue == Math.PI || scalarValue == 0) {
					break;
				}
				else if (u == axis.size()-1) {
					returningList.add(axis.get(i));
				}
			}
		}
		returningList.add(axis.getLast());
		
		return returningList;
	}
	
	// Reference https://www.youtube.com/watch?v=dn0hUgsok9M
	public static boolean checkCollision(Entity entity1, Entity entity2) {
		
		// simple distance check first
		float hypotenuse1 = (float) Math.hypot(entity1.getSize().getX()/2, entity1.getSize().getY()/2);
		float hypotenuse2 = (float) Math.hypot(entity2.getSize().getX()/2, entity2.getSize().getY()/2);
		
		if (hypotenuse1+hypotenuse2 < VectorMath.distance(entity1.getPosition(), entity2.getPosition())) return false;
		
		ArrayList<Vector> corners1 = getCorners(entity1);
		ArrayList<Vector> corners2 = getCorners(entity2);
		
		ArrayList<Vector> axis = getNomralVectors(getEdgeVectors(corners1));
		axis.addAll(getNomralVectors(getEdgeVectors(corners2)));
		
		axis = removeDoubleAxis(axis);
		
		for (int i=0;i<axis.size();i++) {
			double[] points1 = findOuterPointsOnAxis(axis.get(i),corners1.toArray());
			double[] points2 = findOuterPointsOnAxis(axis.get(i),corners2.toArray());
			
			if (points1[1] <= points2[0]) {
				return false;
			}
			else if (points1[0] >= points2[1]) {
				return false;
			}
		}
		
		return true;
	}
}
