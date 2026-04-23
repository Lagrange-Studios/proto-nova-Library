package simulation;

import protonova.protobuf.ActionProto.Action;
import protonova.protobuf.ActionProto.ActionType;
import protonova.protobuf.EntityProto.Direction;
import protonova.protobuf.EntityProto.Entity;
import protonova.protobuf.VectorProto.Vector;
import util.VectorMath;

public class EntitySimulation {
	
	public static final int TPS = 60;
	public static final float accelerationModifer = 0.95f;
	
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
	
	public static Entity simulateVelocityXAxis(Entity entity) {
		
		Vector unitVector = entity.getVelocity().getX() != 0 || entity.getVelocity().getY() != 0?VectorMath.unitVector(entity.getVelocity()): 
			Vector.newBuilder()
			.setX(0)
			.setY(0)
			.build();
		
		Vector position = entity.getPosition().toBuilder()
				.setX((float) (entity.getPosition().getX() + entity.getSpeed()*unitVector.getX()/TPS))
				.build();
		
		return entity.toBuilder()
				.setPosition(position)
				.build();
		
	}
	
	public static Entity simulateVelocityYAxis(Entity entity) {
		
		Vector unitVector = entity.getVelocity().getX() != 0 || entity.getVelocity().getY() != 0?VectorMath.unitVector(entity.getVelocity()): 
			Vector.newBuilder()
			.setX(0)
			.setY(0)
			.build();
		
		Vector position = entity.getPosition().toBuilder()
				.setY((float) (entity.getPosition().getY() + entity.getSpeed()*unitVector.getY()/TPS))
				.build();
		
		return entity.toBuilder()
				.setPosition(position)
				.build();
		
	}
}
