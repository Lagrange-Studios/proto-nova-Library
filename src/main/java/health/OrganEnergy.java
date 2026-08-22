package health;

import protonova.protobuf.OrgansProto.CardiovascularSystem;
import protonova.protobuf.OrgansProto.OrganStatus;
import protonova.protobuf.OrgansProto.OrganType;

public final class OrganEnergy {

	public static final float DEFAULT_HEART_NUTRITION_USE = 0.01f;
	public static final float DEFAULT_LUNG_NUTRITION_USE = 0.005f;
	public static final float DEFAULT_LIVER_NUTRITION_USE = 0.01f;
	public static final float DEFAULT_BRAIN_NUTRITION_USE = 0.03f;
	public static final float DEFAULT_STOMACH_NUTRITION_USE = 0.005f;

	private OrganEnergy() {
	}

	public static OrganStatus biological(float nutritionUsePerSecond) {
		return OrganStatus.newBuilder()
				.setType(OrganType.ORGAN_TYPE_BIOLOGICAL)
				.setNutritionUsePerSecond(positive(nutritionUsePerSecond))
				.build();
	}

	public static OrganStatus cybernetic(float powerUsePerSecond) {
		return OrganStatus.newBuilder()
				.setType(OrganType.ORGAN_TYPE_CYBERNETIC)
				.setPowerUsePerSecond(positive(powerUsePerSecond))
				.build();
	}

	public static boolean isBiological(OrganStatus status) {
		return status.getType() == OrganType.ORGAN_TYPE_BIOLOGICAL;
	}

	public static boolean isCybernetic(OrganStatus status) {
		return status.getType() == OrganType.ORGAN_TYPE_CYBERNETIC;
	}

	public static float nutritionUsePerSecond(OrganStatus status, float defaultValue) {
		if (!isBiological(status)) {
			return 0;
		}
		return status.hasNutritionUsePerSecond()
				? positive(status.getNutritionUsePerSecond())
				: positive(defaultValue);
	}

	public static float powerUsePerSecond(OrganStatus status) {
		return isCybernetic(status) ? positive(status.getPowerUsePerSecond()) : 0;
	}

	public static float maximumNutrition(CardiovascularSystem cardiovascularSystem) {
		return cardiovascularSystem.hasMaxNutrition()
				? positive(cardiovascularSystem.getMaxNutrition())
				: positive(cardiovascularSystem.getNutrition());
	}

	public static CardiovascularSystem withNutrition(
			CardiovascularSystem cardiovascularSystem,
			float nutrition,
			float maximumNutrition) {
		float safeMaximum = positive(maximumNutrition);
		float safeNutrition = Math.min(positive(nutrition), safeMaximum);
		return cardiovascularSystem.toBuilder()
				.setNutrition(safeNutrition)
				.setMaxNutrition(safeMaximum)
				.build();
	}

	public static CardiovascularSystem addNutrition(
			CardiovascularSystem cardiovascularSystem,
			float nutrition) {
		float addedNutrition = positive(nutrition);
		float currentNutrition = positive(cardiovascularSystem.getNutrition());
		float maximumNutrition = cardiovascularSystem.hasMaxNutrition()
				? positive(cardiovascularSystem.getMaxNutrition())
				: currentNutrition + addedNutrition;
		return withNutrition(
				cardiovascularSystem,
				currentNutrition + addedNutrition,
				maximumNutrition);
	}

	public static CardiovascularSystem consumeNutrition(
			CardiovascularSystem cardiovascularSystem,
			float nutrition) {
		float currentNutrition = positive(cardiovascularSystem.getNutrition());
		return withNutrition(
				cardiovascularSystem,
				Math.max(0, currentNutrition - positive(nutrition)),
				maximumNutrition(cardiovascularSystem));
	}

	private static float positive(float value) {
		return Float.isFinite(value) ? Math.max(0, value) : 0;
	}
}
