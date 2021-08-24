package nuparu.sevendaystomine.world.gen.city.street.lamp;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.world.gen.city.street.Street;

public abstract class StreetLamp {
    public abstract void generate(WorldGenRegion world, BlockPos pos, Street street, Direction direction, int forward, int side, int offset, boolean end);
}
