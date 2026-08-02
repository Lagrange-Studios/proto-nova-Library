package collision;

import protonova.protobuf.EntityProto.Entity;

/** Collision checks for the game's axis-aligned entity hitboxes. */
public final class EntityCollision {
    private EntityCollision() {}

    /**
     * Returns true only when the two hitboxes have positive overlapping area.
     * Merely touching edges is allowed, which prevents entities from sticking
     * after collision resolution places them directly beside one another.
     */
    public static boolean checkCollision(Entity first, Entity second) {
        if (first == null || second == null) return false;

        double firstWidth = first.getSize().getX();
        double firstHeight = first.getSize().getY();
        double secondWidth = second.getSize().getX();
        double secondHeight = second.getSize().getY();
        if (!isPositiveFinite(firstWidth) || !isPositiveFinite(firstHeight)
                || !isPositiveFinite(secondWidth) || !isPositiveFinite(secondHeight)) return false;

        double horizontalDistance = Math.abs(first.getPosition().getX() - second.getPosition().getX());
        double verticalDistance = Math.abs(first.getPosition().getY() - second.getPosition().getY());
        double combinedHalfWidths = (firstWidth + secondWidth) * 0.5;
        double combinedHalfHeights = (firstHeight + secondHeight) * 0.5;

        return horizontalDistance < combinedHalfWidths && verticalDistance < combinedHalfHeights;
    }

    /** Returns true when a movement increases the center distance from an obstacle. */
    public static boolean isMovingAway(Entity before, Entity after, Entity obstacle) {
        if (before == null || after == null || obstacle == null) return false;
        double beforeX = before.getPosition().getX() - obstacle.getPosition().getX();
        double beforeY = before.getPosition().getY() - obstacle.getPosition().getY();
        double afterX = after.getPosition().getX() - obstacle.getPosition().getX();
        double afterY = after.getPosition().getY() - obstacle.getPosition().getY();
        return afterX * afterX + afterY * afterY > beforeX * beforeX + beforeY * beforeY;
    }

    private static boolean isPositiveFinite(double value) {
        return value > 0 && Double.isFinite(value);
    }
}
