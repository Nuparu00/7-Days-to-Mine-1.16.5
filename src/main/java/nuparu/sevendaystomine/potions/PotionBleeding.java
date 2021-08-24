package nuparu.sevendaystomine.potions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.world.Difficulty;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.util.DamageSources;
import nuparu.sevendaystomine.util.EnumModParticleType;
import nuparu.sevendaystomine.util.MathUtils;

public class PotionBleeding extends PotionBase {

	public PotionBleeding(EffectType type, int color) {
		super(type, color);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int p_76394_2_) {
		if (entity.level.random.nextInt(12) == 0) {
			entity.hurt(DamageSources.bleeding, 1);
		}
		if (entity.level.isClientSide() && entity.level.getDifficulty() != Difficulty.PEACEFUL) {
			if (entity.level.random.nextInt(5) == 0) {

				for (int i = 0; i < (int) Math
						.round(MathUtils.getDoubleInRange(1, 5) * SevenDaysToMine.proxy.getParticleLevel()); i++) {
					double x = entity.getX() + MathUtils.getDoubleInRange(-1, 1) * entity.getBbWidth();
					double y = entity.getY() + MathUtils.getDoubleInRange(0, 1) * entity.getBbHeight();
					double z = entity.getZ() + MathUtils.getDoubleInRange(-1, 1) * entity.getBbWidth();
					SevenDaysToMine.proxy.addParticle(entity.level, EnumModParticleType.BLOOD, x, y, z,
							MathUtils.getDoubleInRange(-1d, 1d) / 7d, MathUtils.getDoubleInRange(-0.5d, 1d) / 7d,
							MathUtils.getDoubleInRange(-1d, 1d) / 7d);
				}
			}
		}

	}

	@Override
	public boolean isDurationEffectTick(int p_76397_1_, int p_76397_2_) {
		return true;
	}
}
