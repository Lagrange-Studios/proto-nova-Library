package simulation;

import protonova.protobuf.ActionProto.Action;
import protonova.protobuf.ActionProto.ActionType;
import protonova.protobuf.EntityProto.Entity;
import protonova.protobuf.VectorProto.Vector;
import util.VectorMath;

public class EntitySimulation {
	public static Entity simulateMovement(Entity entity, int TPS, Action action) {
		Vector velocity = entity.getVelocity();
		float speed = (float) entity.getSpeed();
		float acceleration = speed/(TPS);
		
		float newX = 0;
		float newY = 0;
		
		
		switch(action.getActionType().getNumber()) {
			case ActionType.MoveUp_VALUE:
				if (velocity.getY() < speed) {
					newY = Math.min(velocity.getY()+acceleration, speed);
				}
				break;
			case ActionType.MoveDown_VALUE:
				if (velocity.getY() > -speed) {
					newY = Math.max(velocity.getY()-acceleration, -speed);
				}
				break;
			case ActionType.MoveRight_VALUE:
				if (velocity.getX() < speed) {
					newX = Math.min(velocity.getX()+acceleration, speed);
				}
				break;
			case ActionType.MoveLeft_VALUE:
				if (velocity.getX() > -speed) {
					newX = Math.max(velocity.getX()-acceleration, -speed);
				}
				break;
		}
		
		// the entity slows if it dosent want to move
		if (newY == 0 && velocity.getY() != 0 ) {
			newY = Math.abs(velocity.getY())>acceleration?velocity.getY()-Math.copySign(acceleration, velocity.getY()):0;
		}
		if (newX == 0 && velocity.getX() != 0 ) {
			newX = Math.abs(velocity.getX())>acceleration?velocity.getX()-Math.copySign(acceleration, velocity.getX()):0;
		}
		
		// apply keys
		velocity = Vector.newBuilder()
				.setX(newX)
				.setY(newY)
				.build();
		
		Vector unitVector = newX != 0 || newY != 0?VectorMath.unitVector(velocity): 
			Vector.newBuilder()
			.setX(0)
			.setY(0)
			.build();
		
		Vector position = Vector.newBuilder()
				.setX(entity.getPosition().getX() + unitVector.getX()/TPS)
				.setY(entity.getPosition().getY() + unitVector.getY()/TPS)
				.build();
		
		return entity.toBuilder()
				.setVelocity(velocity)
				.setPosition(position)
				.build();
	}
}
