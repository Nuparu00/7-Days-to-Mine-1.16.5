package nuparu.sevendaystomine.potions;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.EffectType;
import nuparu.sevendaystomine.SevenDaysToMine;

public class PotionBrokenLeg extends PotionBase {

	public PotionBrokenLeg(EffectType type, int color) {
		super(type, color);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
				"7a5c093e-7a5d-11ea-bc55-0242ac130003", -0.25D, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}
}
