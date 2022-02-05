package nuparu.sevendaystomine.potions;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectType;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.SpawnBloodMessage;
import nuparu.sevendaystomine.util.DamageSources;
import nuparu.sevendaystomine.util.MathUtils;

public class PotionBleeding extends PotionBase {

    public PotionBleeding(EffectType type, int color) {
        super(type, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int p_76394_2_) {
        if (!entity.level.isClientSide() && (entity instanceof MobEntity || entity instanceof PlayerEntity)) {
            if (entity.level.random.nextInt(12) == 0) {
                entity.hurt(DamageSources.bleeding, 1);
                for (int i = 0; i < MathUtils.getIntInRange(entity.level.random, 20, 35); i++) {
                    PacketManager.sendToTrackingEntity(PacketManager.spawnBlood, new SpawnBloodMessage(entity.getX(0.5), entity.getY() + entity.getBbHeight() * MathUtils.getFloatInRange(0.4f, 0.75f), entity.getZ(0.5), MathUtils.getFloatInRange(-0.1f, 0.1f), MathUtils.getFloatInRange(0.1f, 0.22f), MathUtils.getFloatInRange(-0.1f, 0.1f)), () -> entity);
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int p_76397_1_, int p_76397_2_) {
        return true;
    }
}
