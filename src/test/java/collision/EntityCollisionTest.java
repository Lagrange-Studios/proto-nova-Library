package collision;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import protonova.protobuf.EntityProto.Entity;
import protonova.protobuf.VectorProto.Vector;

public class EntityCollisionTest {
    @Test
    public void detectsOverlapFromEveryDirection() {
        Entity tree = entity(0, 0, 2, 2);
        assertTrue(EntityCollision.checkCollision(entity(0, 1.4f, 1, 1), tree));
        assertTrue(EntityCollision.checkCollision(entity(0, -1.4f, 1, 1), tree));
        assertTrue(EntityCollision.checkCollision(entity(1.4f, 0, 1, 1), tree));
        assertTrue(EntityCollision.checkCollision(entity(-1.4f, 0, 1, 1), tree));
    }

    @Test
    public void allowsTouchingEdgesAndSeparatedBoxes() {
        Entity tree = entity(0, 0, 2, 2);
        assertFalse(EntityCollision.checkCollision(entity(0, 1.5f, 1, 1), tree));
        assertFalse(EntityCollision.checkCollision(entity(1.5f, 0, 1, 1), tree));
        assertFalse(EntityCollision.checkCollision(entity(8, 8, 1, 1), tree));
    }

    @Test
    public void ignoresMissingHitboxDimensions() {
        assertFalse(EntityCollision.checkCollision(entity(0, 0, 0, 1), entity(0, 0, 1, 1)));
        assertFalse(EntityCollision.checkCollision(entity(0, 0, 1, 1), entity(0, 0, 1, 0)));
    }

    @Test
    public void embeddedEntityMayOnlyMoveAwayFromObstacle() {
        Entity tree = entity(0, 0, 2, 2);
        Entity embedded = entity(0, 0.8f, 1, 1);
        assertTrue(EntityCollision.isMovingAway(embedded, entity(0, 0.9f, 1, 1), tree));
        assertFalse(EntityCollision.isMovingAway(embedded, entity(0, 0.7f, 1, 1), tree));
        assertFalse(EntityCollision.isMovingAway(embedded, entity(0.1f, 0.7937f, 1, 1), tree));
    }

    private static Entity entity(float x, float y, float width, float height) {
        return Entity.newBuilder()
                .setPosition(Vector.newBuilder().setX(x).setY(y))
                .setSize(Vector.newBuilder().setX(width).setY(height))
                .build();
    }
}
