package nuparu.sevendaystomine.world.gen.city.street.limit;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.world.gen.city.street.Street;

import java.util.Random;

public class CityLimitSign extends LimitSign{

    int height = 7;
    int signHeight = 3;

    @Override
    public void generate(WorldGenRegion world, BlockPos pos, Street street, Direction direction, int forward, int side, int offset, boolean end) {
        BlockState base = world.getBlockState(pos.below());
        if (base.getBlock() instanceof SlabBlock) {
            world.setBlock(pos.below(), street.city.type.pavementBlock == null ?  Blocks.STONE_BRICKS.defaultBlockState() : street.city.type.pavementBlock,0);
        }
        for (int i = 0; i < height; i++) {
            world.setBlock(pos.above(i), getRandomWall(street.city.rand),0);
        }

        BlockPos polePos2 = pos.relative(direction.getClockWise(),street.city.type.getRoadWidth()+1);
        BlockState base2 = world.getBlockState(polePos2.below());
        if (base2.getBlock() instanceof SlabBlock) {
            world.setBlock(polePos2.below(), street.city.type.pavementBlock == null ?  Blocks.STONE_BRICKS.defaultBlockState() : street.city.type.pavementBlock,0);
        }
        for (int i = 0; i < height; i++) {
            world.setBlock(polePos2.above(i), getRandomWall(street.city.rand),0);
        }

        for (int i = 0; i < street.city.type.getRoadWidth()+2; i++) {
            world.setBlock(pos.above(height).relative(direction.getClockWise(), i), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(),0);
        }

        for (int i = 1; i < street.city.type.getRoadWidth()+1; i++) {
            for (int j = 0; j < signHeight; j++) {
                world.setBlock(pos.above(height-signHeight + j).relative(direction.getClockWise(), i), Blocks.GREEN_TERRACOTTA.defaultBlockState(),0);
            }
        }
/*
        BlockPos signPos = pos.up(4).offset(facing.rotateY(), 4).offset(facing);
        world.setBlockState(signPos,
                ModBlocks.BIG_SIGN_MASTER.getDefaultState().withProperty(BlockWallSign.FACING, facing));*/
    }

    public BlockState getRandomWall(Random rand) {
        return rand.nextInt(8) == 0
                ? Blocks.MOSSY_COBBLESTONE_WALL.defaultBlockState()
                : Blocks.COBBLESTONE_WALL.defaultBlockState();
    }
}
