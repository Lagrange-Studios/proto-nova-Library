package health;

import protonova.protobuf.EntityProto.Entity;

public class Health {
	
	public enum TraumaState {
	    HEALTHY(0), 
	    MINOR_INJURIES(15),
	    INJURED(40),
	    SEVERELY_INJURED(70),
		MORTALLY_WOUNDED(90),
		CRITICAL(100),
		// Health Percent After Critical Threashold
		NEAR_DEATH(50),
		DEAD(100);

	    // At what point the Trauma State starts
	    private final double trauma;

	    TraumaState(int trauma) {
	        this.trauma = trauma;
	    }

	    public double getTraumaPercent() {
	        return this.trauma;
	    }
	    
	    public double getTraumaPercentAsDecimal() {
	    	//returns decimal
	        return this.trauma / 100;
	    }
	}
	
	public static double getDamage(Entity entity) {
		float brute = entity.getDamage().getBruteDamage();
		float burn = entity.getDamage().getBurnDamage();
		float toxin = entity.getDamage().getToxinDamage();
		float asphyxiation = entity.getDamage().getAsphyxiationDamage();
		float genetic = entity.getDamage().getGeneticDamage();
		float structural = entity.getDamage().getStructuralDamage();
		double totalDamage = brute + burn + toxin + asphyxiation + genetic + structural;
		return totalDamage;
		
	}
	
	public static TraumaState getTraumaStateFromEntity(Entity entity) {
		if (!entity.getAlive()) {
			return TraumaState.DEAD;
		}
		double entityDamage = getDamage(entity);
		
		if (entityDamage >= entity.getCritHealth()) {
			double healthDiff = entity.getMaxHealth() - entity.getCritHealth();
			double damageAfterCrit = entityDamage - entity.getCritHealth();
			if (entityDamage >= entity.getMaxHealth()) {
				return TraumaState.DEAD;
			} else if (healthDiff - TraumaState.NEAR_DEATH.getTraumaPercentAsDecimal() <= damageAfterCrit) {
				return TraumaState.NEAR_DEATH;
			} else {
				return TraumaState.CRITICAL;
			}
		} else {
			if (entityDamage >= entity.getCritHealth() * TraumaState.MORTALLY_WOUNDED.getTraumaPercentAsDecimal()) {
				return TraumaState.MORTALLY_WOUNDED;
			} else if (entityDamage >= entity.getCritHealth() * TraumaState.SEVERELY_INJURED.getTraumaPercentAsDecimal()) {
				return TraumaState.SEVERELY_INJURED;
			} else if (entityDamage >= entity.getCritHealth() * TraumaState.INJURED.getTraumaPercentAsDecimal()) {
				return TraumaState.INJURED;
			} else if (entityDamage >= entity.getCritHealth() * TraumaState.MINOR_INJURIES.getTraumaPercentAsDecimal()) {
				return TraumaState.MINOR_INJURIES;
			} else {
				return TraumaState.HEALTHY;
			}
		}
	}
	
	
}
