package nuparu.sevendaystomine.potions;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.EffectType;
import nuparu.sevendaystomine.SevenDaysToMine;

public class PotionAlcoholBuzz extends PotionBase {

	public PotionAlcoholBuzz(EffectType type, int color) {
		super(type, color);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
				"9c4420da-bf44-11e7-abc4-cec278b6b50a", 0.15D, AttributeModifier.Operation.MULTIPLY_BASE);
		this.addAttributeModifier(Attributes.ATTACK_SPEED,
				"9c4420da-bf44-11e7-abc4-cec278b6b50a", 0.25D, AttributeModifier.Operation.MULTIPLY_BASE);
		this.addAttributeModifier(Attributes.FLYING_SPEED,
				"9c4420da-bf44-11e7-abc4-cec278b6b50a", 0.15D, AttributeModifier.Operation.MULTIPLY_BASE);
	}
}