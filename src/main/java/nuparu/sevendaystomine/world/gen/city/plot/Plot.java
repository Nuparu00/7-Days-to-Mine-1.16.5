package nuparu.sevendaystomine.world.gen.city.plot;

import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.city.City;
import nuparu.sevendaystomine.world.gen.city.plot.building.Building;
import nuparu.sevendaystomine.world.gen.city.street.Street;

public class Plot {
    public City city;
    public Street street;
    public Building building;
    public int xSize;
    public int zSize;
    public BlockPos start;
    public BlockPos end;
    public boolean mirror;

    public Plot(Street street, int index, Building building, boolean mirror, BlockPos start) {
        this.street = street;
        city = street.city;
        this.building = building;
        BlockPos dimensions = building.getDimensions(street.world.getLevel(), street.facing,mirror);
        xSize = dimensions.getX();
        zSize = dimensions.getZ();
        this.mirror = mirror;

        int dir = street.facing.getAxis() == Direction.Axis.Z ? -1 : 1;
        switch (street.facing) {
            default:
            case EAST:
                this.start = start.relative(mirror ? Direction.SOUTH : Direction.NORTH,
                        (city.type.getRoadWidth() / 2) + city.type.getPavementWidth() + 1);
                break;
            case WEST:
                this.start = start.relative(mirror ? Direction.NORTH : Direction.SOUTH,
                        (city.type.getRoadWidth() / 2) + city.type.getPavementWidth() + 1);
                break;
            case NORTH:
                this.start = start.relative(mirror ? Direction.EAST : Direction.WEST,
                        (city.type.getRoadWidth() / 2) + city.type.getPavementWidth() + 1);
                break;
            case SOUTH:
                this.start = start.relative(mirror ? Direction.WEST : Direction.EAST,
                        (city.type.getRoadWidth() / 2) + city.type.getPavementWidth() + 1);
                break;

        }

        this.start = this.start.relative(street.facing, 5);
        end = start.relative(street.facing, street.facing.getAxis() == Direction.Axis.Z ? zSize : xSize).relative(
                mirror ? street.facing.getClockWise() : street.facing.getCounterClockWise(),
                street.facing.getAxis() == Direction.Axis.X ? zSize : xSize);
        this.end = Utils.getTopGroundBlock(end, street.world, true);

        //this.start = start.relative(mirror ? street.facing.getClockWise() : street.facing.getCounterClockWise(),)
    }

    public void generate() {

        BlockPos pos = start.relative(street.facing, street.facing.getAxis() == Direction.Axis.Z ? zSize : xSize);
        int yStart = start.getY();
        int yEnd = end.getY();


        building.generate(street.world.getLevel(), new BlockPos(pos.getX(), MathUtils.lerp(yStart, yEnd, 0.5f), pos.getZ()),
                street.facing, mirror, street.city.rand);
       /* street.world.setBlockAndUpdate(new BlockPos(start.getX(),128, start.getZ()), Blocks.BEDROCK.defaultBlockState());
        street.world.setBlockAndUpdate(new BlockPos(end.getX(),128, end.getZ()), Blocks.GOLD_BLOCK.defaultBlockState());*/
    }

    public boolean intersects(Plot plot) {
        AxisAlignedBB aabb = new AxisAlignedBB(getMin(), getMax().above(2));
        AxisAlignedBB aabb2 = new AxisAlignedBB(plot.getMin().above(), plot.getMax().above(2));

        return aabb.intersects(aabb2);
    }

    public BlockPos getMin() {
        return new BlockPos(Math.min(start.getX(), end.getX()), 0, Math.min(start.getZ(), end.getZ()));
    }

    public BlockPos getMax() {
        return new BlockPos(Math.max(start.getX(), end.getX()), 0, Math.max(start.getZ(), end.getZ()));
    }
}
