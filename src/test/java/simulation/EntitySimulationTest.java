package simulation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import protonova.protobuf.EntityProto.Entity;
import protonova.protobuf.VectorProto.Vector;

public class EntitySimulationTest {

	private static final float DIFFERENCE_ALLOWED = 0.0001f;

	@Test
	public void itemVelocityMovesItemsEvenWhenTheirWalkingSpeedIsZero() {
		Entity item = createItem(6, 0, false);

		Entity movedItem = EntitySimulation.simulateVelocityXAxis(item, 20);

		assertEquals(0.3f, movedItem.getPosition().getX(), DIFFERENCE_ALLOWED);
	}

	@Test
	public void droppedItemsSlowDownEveryTick() {
		Entity item = createItem(6, 0, false);

		Entity slowedItem = EntitySimulation.slowItemVelocity(item, 20);

		assertEquals(5.5f, slowedItem.getVelocity().getX(), DIFFERENCE_ALLOWED);
	}

	@Test
	public void anchoredItemsDoNotMove() {
		Entity item = createItem(6, 0, true);

		Entity movedItem = EntitySimulation.simulateVelocityXAxis(item, 20);

		assertEquals(0, movedItem.getPosition().getX(), DIFFERENCE_ALLOWED);
	}

	private Entity createItem(float horizontalVelocity, float verticalVelocity, boolean anchored) {
		return Entity.newBuilder()
				.setIsItem(true)
				.setAnchored(anchored)
				.setPosition(Vector.newBuilder().build())
				.setVelocity(Vector.newBuilder()
						.setX(horizontalVelocity)
						.setY(verticalVelocity))
				.build();
	}
}
