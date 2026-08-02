package health;

public final class ChemicalUnits {

	public static final float STANDARD_DOSE = 10.0f;

	public static final float DEFAULT_STOMACH_CAPACITY = 50.0f;

	public static final float DEFAULT_INJECTION_RESERVE = 50.0f;

	private ChemicalUnits() {
	}

	public static float doses(float units) {
		return Math.max(0, units) / STANDARD_DOSE;
	}

	public static float units(float doses) {
		return Math.max(0, doses) * STANDARD_DOSE;
	}
}
