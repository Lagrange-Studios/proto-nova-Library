package health;

import protonova.protobuf.EntityProto.Entity;

public class Health {
	
	public enum HealthState {
	    HEALTHY(0), 
	    MINOR_INJURIES(15), 
	    INJURED(40),
		SEVERLY_INJURED(70),
		MORTALLY_WOUNDED(90),
		CRITICAL(100),
		NEAR_DEATH(150),
		DEAD(200);

	    // At what point the Health State starts
	    private final double percent;

	    HealthState(int percent) {
	        this.percent = percent;
	    }

	    public double getPercent() {
	        return this.percent;
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
}
