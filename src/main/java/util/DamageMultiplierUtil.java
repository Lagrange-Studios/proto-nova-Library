package util;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.FieldDescriptor;

import protonova.protobuf.DamageProto.DamageMultiplier;
import protonova.protobuf.EntityProto.Entity;

public class DamageMultiplierUtil {

	public static DamageMultiplier getTotalDamageMultiplier(Entity entity, ArrayList<Entity> modifiers) {
		
		DamageMultiplier.Builder base = entity.getDamage().getDamageMultiplier().toBuilder();
		
		for (Entity gear : modifiers) {
			base = multiply(base, gear.getDamage().getDamageMultiplier());
		}
		
		return base.build();
	}
	
	private static DamageMultiplier.Builder multiply(DamageMultiplier.Builder damage1, DamageMultiplier damage2) {
		
		for (FieldDescriptor field  : DamageMultiplier.getDescriptor().getFields()) {
			if (field.getJavaType().equals(Descriptors.FieldDescriptor.JavaType.FLOAT)) {
				if (damage1.hasField(field) && damage2.hasField(field)) damage1.setField(field, (float) damage1.getField(field) * (float) damage2.getField(field));
				else if (damage1.hasField(field)) continue; // do nothing cause its currently in the builder already
				else if (damage2.hasField(field)) damage1.setField(field, damage2.getField(field));
				else damage1.setField(field, 1f);
			}
		}
		
		return damage1;
	}
}
