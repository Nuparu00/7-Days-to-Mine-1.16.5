package nuparu.sevendaystomine.potions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectType;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;

public class PotionDysentery extends PotionBase {

	public PotionDysentery(EffectType type, int color) {
		super(type, color);
		this.addAttributeModifier(Attributes.MAX_HEALTH, "9c44164e-bf44-11e7-abc4-cec278b6b50a", -0.1D,
				AttributeModifier.Operation.MULTIPLY_TOTAL);

	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {

		if (entity instanceof PlayerEntity) {

			IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer((PlayerEntity) entity);
			if (entity.level.random.nextInt(10) == 0) {
				extendedPlayer.consumeStamina(5);
			}
		}

	}
	@Override
	public boolean isDurationEffectTick(int p_76397_1_, int p_76397_2_) {
		return true;
	}
}
