package nuparu.sevendaystomine.potions;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.EffectType;

public class PotionSplintedLeg extends PotionBase {

	public PotionSplintedLeg(EffectType type, int color) {
		super(type, color);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "7a5c093e-7a5d-11ea-bc55-0242ac130003", -0.1D,
				AttributeModifier.Operation.MULTIPLY_TOTAL);
	}
}
