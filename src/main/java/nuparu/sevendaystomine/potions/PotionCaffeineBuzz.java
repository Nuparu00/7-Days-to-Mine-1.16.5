package nuparu.sevendaystomine.potions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectType;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;

public class PotionCaffeineBuzz extends PotionBase {

	public PotionCaffeineBuzz(EffectType type, int color) {
		super(type, color);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
				"9c4420da-bf44-11e7-abc4-cec278b6b50a", 0.05D, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.ATTACK_SPEED,
				"9c4420da-bf44-11e7-abc4-cec278b6b50a", 0.33D, AttributeModifier.Operation.MULTIPLY_TOTAL);

	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		if(entity.level.random.nextInt(3) == 0 && entity instanceof PlayerEntity) {
			IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer((PlayerEntity)entity);
			if(extendedPlayer == null)return;
			extendedPlayer.addStamina(1*amplifier);
		}
	}
}
