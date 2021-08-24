package nuparu.sevendaystomine.events;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event;

public class MobBreakEvent extends Event
{
    public final World world;
    public final BlockPos pos;
    public final BlockState state;
    public final LivingEntity entity;
    public MobBreakEvent(World world, BlockPos pos, BlockState state, LivingEntity entity)
    {
        this.pos = pos;
        this.world = world;
        this.state = state;
        this.entity = entity;
    }
}
