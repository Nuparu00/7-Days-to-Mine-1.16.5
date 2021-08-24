package nuparu.sevendaystomine.potions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.EffectType;
import net.minecraft.world.Difficulty;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.util.DamageSources;

public class PotionMercuryPoisoning extends PotionBase {

	public PotionMercuryPoisoning(EffectType type, int color) {
		super(type, color);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		if (entity.level.getDifficulty() == Difficulty.PEACEFUL)
			return;
		if (entity.level.random.nextInt(10) == 0) {
			entity.hurt(DamageSources.alcoholPoison, 1);

		}
	}
	@Override
	public boolean isDurationEffectTick(int p_76397_1_, int p_76397_2_) {
		return true;
	}
}
