package nuparu.sevendaystomine.world.gen.city.street.traffic;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.WorldGenRegion;
import nuparu.sevendaystomine.world.gen.city.street.Street;

public abstract class TrafficLight {
    public abstract void generate(WorldGenRegion world, BlockPos pos, Street street, Direction direction, int forward, int side, int offset, boolean end);
}
