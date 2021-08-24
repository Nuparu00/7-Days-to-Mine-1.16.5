package nuparu.sevendaystomine.potions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.world.Difficulty;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.util.DamageSources;

public class PotionThirst extends PotionBase {

	public PotionThirst(EffectType type, int color) {
		super(type, color);

	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		if (entity instanceof PlayerEntity) {

			IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer((PlayerEntity) entity);
			if (entity.level.random.nextInt(10) == 0) {
				extendedPlayer.consumeStamina(2);
			}
		}
		if (entity.level.getDifficulty() == Difficulty.PEACEFUL)
			return;
		if (entity.level.random.nextInt(15) == 0) {
			entity.hurt(DamageSources.thirst, 2);
		}

	}
	@Override
	public boolean isDurationEffectTick(int p_76397_1_, int p_76397_2_) {
		return true;
	}
}
