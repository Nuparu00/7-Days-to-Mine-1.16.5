package nuparu.sevendaystomine.potions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectType;
import net.minecraft.world.Difficulty;
import nuparu.sevendaystomine.util.DamageSources;

public class PotionFungalInfection extends PotionBase {

    public PotionFungalInfection(EffectType type, int color) {
        super(type, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int p_76394_2_) {
        if (entity.level.random.nextInt(12) == 0) {
            entity.hurt(DamageSources.fungalInfection, 1);
        }
        if (entity.level.isClientSide() && entity.level.getDifficulty() != Difficulty.PEACEFUL) {
            if (entity.level.random.nextInt(2) == 0) {
                entity.level.addParticle(ParticleTypes.WARPED_SPORE, entity.getRandomX(0.5D), entity.getRandomY(), entity.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
            }

        }

    }

    @Override
    public boolean isDurationEffectTick(int p_76397_1_, int p_76397_2_) {
        return true;
    }
}
