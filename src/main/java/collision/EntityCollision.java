package collision;

import java.util.ArrayList;

import protonova.protobuf.EntityProto.Entity;
import protonova.protobuf.VectorProto.Vector;
import util.VectorMath;

public class EntityCollision {

	private static ArrayList<Vector> getCorners(Entity entity) {
		
		ArrayList<Vector> corners = new ArrayList<Vector>();
		
		float centerX = entity.getPosition().getX();
		float centerY = entity.getPosition().getY();
		
		//TODO: take into account entity rotation and size
		for (int x=-1;x<=1;x+=2) {
			for (int y=-1;y<=1;y+=2) {
				corners.add(Vector.newBuilder()
						.setX((float) (centerX + 0.5*x))
						.setY((float) (centerY + 0.5*y))
						.build());
			}
		}
		
		return corners;
	}
	
	private static ArrayList<Vector> getEdgeVectors(ArrayList<Vector> corners) {
		
		ArrayList<Vector> edgeVectors = new ArrayList<Vector>();
		
		for (int i=0;i<corners.size();i++) {
			Vector corner = corners.get(i);
			Vector corner2 = corners.get(i++!=corners.size()?i++:0); // I'm sorry -F
			
			edgeVectors.add(VectorMath.vectorDistance(corner, corner2));
		}
		
		return edgeVectors;
	}
	
	// Could optimize if we stick with rectangle collision boxes
	private static ArrayList<Vector> getUniqueNomralVectors(ArrayList<Vector> edgeVectors) {
		
		ArrayList<Vector> normalVectors = new ArrayList<Vector>();
		
		for (int i=0;i<edgeVectors.size();i++) {
			Vector newNormal = VectorMath.normal(edgeVectors.get(i));
			
			for (int u=0;u<normalVectors.size();u++) {
				double scalarValue = VectorMath.scalar(newNormal, normalVectors.get(u));
				
				if (scalarValue == Math.PI || scalarValue == 0) {
					break;
				}
				else if (u == normalVectors.size()-1) {
					normalVectors.add(newNormal);
				}
			}
		}
		
		return normalVectors;
	}
	
	private static Vector projectVectorToAxis(Vector vector, Vector axis) {
		double change = VectorMath.scalar(vector, VectorMath.unitVector(axis));
		double magnitude = VectorMath.magnitude(vector);
		
		return Vector.newBuilder()
				.setX((float) (magnitude*Math.cos(change)))
				.setY((float) (magnitude*Math.sin(change)))
				.build();
	}
	
	
	private static double[] findOuterPointsOnAxis(Vector axis, Object[] points) {
		
		double[] minMax = {0,0};
		
		for (int i=0;i<points.length;i++) {
			double projectedPoint = VectorMath.magnitude(projectVectorToAxis((Vector) points[i],axis));
			
			minMax[0] = Math.min(projectedPoint, minMax[0]);
			minMax[1] = Math.max(projectedPoint, minMax[1]);
		}
		
		return minMax;
	}
	
	private static ArrayList<Vector> removeDoubleAxis(ArrayList<Vector> axis) {
		
		for (int i=0;i<axis.size();i++) {
			
			if (i != axis.size()-1) {
				for (int u=i++;u<axis.size();u++) {
					if (axis.get(i) == axis.get(u)) {
						axis.remove(u);
					}
				}
			}
		}
		
		return axis;
	}
	
	// Reference https://www.youtube.com/watch?v=dn0hUgsok9M
	public static boolean checkCollision(Entity entity1, Entity entity2) {
		
		ArrayList<Vector> corners1 = getCorners(entity1);
		ArrayList<Vector> corners2 = getCorners(entity2);
		
		ArrayList<Vector> axis = getUniqueNomralVectors(getEdgeVectors(corners1));
		axis.addAll(getUniqueNomralVectors(getEdgeVectors(corners2)));
		
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
