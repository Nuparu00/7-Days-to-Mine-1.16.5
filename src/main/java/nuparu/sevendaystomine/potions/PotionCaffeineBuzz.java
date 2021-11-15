package nuparu.sevendaystomine.potions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectType;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;

public class PotionCaffeineBuzz extends PotionBase {

	public PotionCaffeineBuzz(EffectType type, int color) {
		super(type, color);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
				"14eded8a-3fc1-11ec-9356-0242ac130003", 0.075D, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.ATTACK_SPEED,
				"1fdd9970-3fc1-11ec-9356-0242ac130003", 0.5D, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.FLYING_SPEED,
				"1fdd9a56-3fc1-11ec-9356-0242ac130003", 0.15D, AttributeModifier.Operation.MULTIPLY_BASE);

	}

	public boolean isDurationEffectTick(int p_76397_1_, int p_76397_2_) {
		return true;
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		if(entity instanceof PlayerEntity) {
			IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer((PlayerEntity)entity);
			if(extendedPlayer == null)return;
			extendedPlayer.addStamina(1*(entity.getEffect(this).getAmplifier()+1));
		}
	}
}
