package util;

import protonova.protobuf.EntityProto.Entity;

/** Common map-aware spatial checks for entities. */
public final class EntitySpatial {

	private EntitySpatial() {}

	public static boolean isOnSameMap(Entity first, Entity second) {
		return first != null && second != null && first.getMap() == second.getMap();
	}

	/** Returns infinity when the entities are missing or are on different maps. */
	public static double distance(Entity first, Entity second) {
		if (!isOnSameMap(first, second)) return Double.POSITIVE_INFINITY;
		return VectorMath.distance(first.getPosition(), second.getPosition());
	}

	public static boolean isWithinDistance(Entity first, Entity second, double maximumDistance) {
		return maximumDistance >= 0 && distance(first, second) <= maximumDistance;
	}
}
