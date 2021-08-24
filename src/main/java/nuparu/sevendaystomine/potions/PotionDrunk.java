package nuparu.sevendaystomine.potions;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.EffectType;
import nuparu.sevendaystomine.SevenDaysToMine;

public class PotionDrunk extends PotionBase {

	public PotionDrunk(EffectType type, int color) {
		super(type, color);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
				"9c442288-bf44-11e7-abc4-cec278b6b50a", -0.1D, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.MAX_HEALTH, "9c442f44-bf44-11e7-abc4-cec278b6b50a",
				-0.1D, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}
}
