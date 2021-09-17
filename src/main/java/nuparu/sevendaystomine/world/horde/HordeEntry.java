package nuparu.sevendaystomine.world.horde;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.entity.ZombieBaseEntity;

public class HordeEntry {
    public ResourceLocation res;
    public int weight;

    public HordeEntry(ResourceLocation res, int weight) {
        this.res = res;
        this.weight = weight;
    }

    public ZombieBaseEntity spawn(World world, BlockPos pos) {
        if (res == null) return null;

        EntityType<?> type = ForgeRegistries.ENTITIES.getValue(res);
        type.create(world);
        Entity e = type.create(world);
        if (e == null || !(e instanceof ZombieBaseEntity))
            return null;
        e.setPos(pos.getX(), pos.getY(), pos.getZ());
        if (!world.isClientSide()) {
            world.addFreshEntity(e);
            return (ZombieBaseEntity) e;
        }
        return null;

    }
}