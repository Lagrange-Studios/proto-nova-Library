package simulation;

import protonova.protobuf.ActionProto.Action;
import protonova.protobuf.ActionProto.ActionType;
import protonova.protobuf.EntityProto.Direction;
import protonova.protobuf.EntityProto.Entity;
import protonova.protobuf.VectorProto.Vector;
import util.VectorMath;

public class EntitySimulation {
	
	public static final float accelerationModifer = 0.95f;
	private static final float ITEM_SLOWDOWN_PER_SECOND = 10.0f;
	
	/**
	 * Should be called for every key input
	 */
	public static Entity simulateMovement(Entity entity, Action action) {
		Vector velocity = entity.getVelocity();
		float speed = (float) entity.getSpeed();
		float acceleration = speed*accelerationModifer;
		
		float newX = velocity.getX();
		float newY = velocity.getY();
		
		Direction direction = entity.getDirection();
		
		
		switch(action.getActionType().getNumber()) {
			case ActionType.MoveUp_VALUE:
				direction = Direction.Up;
				if (velocity.getY() < speed) {
					newY = Math.min(velocity.getY()+acceleration, speed);
				}
				break;
			case ActionType.MoveDown_VALUE:
				direction = Direction.Down;
				if (velocity.getY() > -speed) {
					newY = Math.max(velocity.getY()-acceleration, -speed);
				}
				break;
			case ActionType.MoveRight_VALUE:
				direction = Direction.Right;
				if (velocity.getX() < speed) {
					newX = Math.min(velocity.getX()+acceleration, speed);
				}
				break;
			case ActionType.MoveLeft_VALUE:
				direction = Direction.Left;
				if (velocity.getX() > -speed) {
					newX = Math.max(velocity.getX()-acceleration, -speed);
				}
				break;
			case ActionType.StopX_VALUE:
				newX = Math.abs(velocity.getX())>acceleration?velocity.getX()-Math.copySign(acceleration, velocity.getX()):0;
				break;
			case ActionType.StopY_VALUE:
				newY = Math.abs(velocity.getY())>acceleration?velocity.getY()-Math.copySign(acceleration, velocity.getY()):0;
				break;
		}
		
		// apply velocity changes
		velocity = Vector.newBuilder()
				.setX(newX)
				.setY(newY)
				.build();
		
		return entity.toBuilder()
				.setVelocity(velocity)
				.setDirection(direction)
				.build();
	}
	
	public static Entity simulateVelocityXAxis(Entity entity, int TPS) {
		float movementPerTick = 0;
		if (TPS <= 0) {
			return entity;
		}
		if (entity.getIsItem()) {
			if (entity.getAnchored()) {
				return entity;
			}
			movementPerTick = entity.getVelocity().getX() / TPS;
		} else if (entity.getVelocity().getX() != 0 || entity.getVelocity().getY() != 0) {
			Vector movementDirection = VectorMath.unitVector(entity.getVelocity());
			movementPerTick = (float) (entity.getSpeed() * movementDirection.getX() / TPS);
		}

		Vector position = entity.getPosition().toBuilder()
				.setX(entity.getPosition().getX() + movementPerTick)
				.build();
		
		return entity.toBuilder()
				.setPosition(position)
				.build();
		
	}
	
	public static Entity simulateVelocityYAxis(Entity entity, int TPS) {
		float movementPerTick = 0;
		if (TPS <= 0) {
			return entity;
		}
		if (entity.getIsItem()) {
			if (entity.getAnchored()) {
				return entity;
			}
			movementPerTick = entity.getVelocity().getY() / TPS;
		} else if (entity.getVelocity().getX() != 0 || entity.getVelocity().getY() != 0) {
			Vector movementDirection = VectorMath.unitVector(entity.getVelocity());
			movementPerTick = (float) (entity.getSpeed() * movementDirection.getY() / TPS);
		}

		Vector position = entity.getPosition().toBuilder()
				.setY(entity.getPosition().getY() + movementPerTick)
				.build();
		
		return entity.toBuilder()
				.setPosition(position)
				.build();
	}

	public static Entity slowItemVelocity(Entity entity, int TPS) {
		if (!entity.getIsItem() || entity.getAnchored() || TPS <= 0) {
			return entity;
		}

		float currentVelocity = (float) VectorMath.magnitude(entity.getVelocity());
		if (currentVelocity <= 0) {
			return entity;
		}

		float remainingVelocity = currentVelocity - ITEM_SLOWDOWN_PER_SECOND / TPS;
		if (remainingVelocity < 0.05f) {
			remainingVelocity = 0;
		}

		Vector newVelocity;
		if (remainingVelocity == 0) {
			newVelocity = Vector.newBuilder().build();
		} else {
			float velocityRatio = remainingVelocity / currentVelocity;
			newVelocity = entity.getVelocity().toBuilder()
					.setX(entity.getVelocity().getX() * velocityRatio)
					.setY(entity.getVelocity().getY() * velocityRatio)
					.build();
		}

		return entity.toBuilder()
				.setVelocity(newVelocity)
				.build();
	}
}
