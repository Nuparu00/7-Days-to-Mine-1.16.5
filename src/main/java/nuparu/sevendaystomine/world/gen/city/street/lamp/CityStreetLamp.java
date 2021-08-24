package nuparu.sevendaystomine.world.gen.city.street.lamp;

import net.minecraft.block.*;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.block.BlockFakeAnvil;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.city.street.Street;

import java.util.Random;

public class CityStreetLamp extends StreetLamp{

    int height = 5;

    @Override
    public void generate(WorldGenRegion world, BlockPos pos, Street street, Direction direction, int forward, int side, int offset, boolean end) {
        BlockState base = world.getBlockState(pos.below());
        if(base.getBlock() instanceof SlabBlock){
            world.setBlock(pos.below(), street.city.type.pavementBlock == null ?  Blocks.STONE_BRICKS.defaultBlockState() : street.city.type.pavementBlock,0);
        }
        for (int i = 0; i < height; i++) {
            world.setBlock(pos.above(i), getRandomWall(street.city.rand),0);
        }
        BlockPos top = pos.above(height);
        world.setBlock(top,
                ModBlocks.FAKE_ANVIL.get().defaultBlockState().setValue(BlockFakeAnvil.FACING, direction.getClockWise()),0);
        world.setBlock(top.above(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(),0);

        Direction facing = end ? direction.getCounterClockWise() : direction.getClockWise();

        world.setBlock(top.above().relative(facing), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(),0);
        world.setBlock(top.relative(facing), Blocks.HOPPER.defaultBlockState().setValue(HopperBlock.FACING,facing),0);
        world.setBlock(top.above().relative(facing,2), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(),0);
        BlockPos lampPos = top.relative(facing,2);
        world.setBlock(lampPos, ModBlocks.REDSTONE_LAMP_BROKEN.get().defaultBlockState(),0);

        for(Direction direction1 : Utils.HORIZONTALS) {
            BlockPos vineStart = lampPos.relative(direction1);
            BlockState toReplace = world.getBlockState(vineStart);
            BlockState vineState = Blocks.VINE.defaultBlockState().setValue(VineBlock.getPropertyForFace(direction1.getOpposite()), true);
            while(street.city.rand.nextBoolean() && toReplace.getBlock() == Blocks.AIR && vineStart.getY() > 0) {
                world.setBlock(vineStart, vineState,0);
                vineStart=vineStart.below();
                toReplace = world.getBlockState(vineStart);
            }
        }
    }

    public BlockState getRandomWall(Random rand) {
        return rand.nextInt(8) == 0
                ? Blocks.MOSSY_COBBLESTONE_WALL.defaultBlockState()
                : Blocks.COBBLESTONE_WALL.defaultBlockState();
    }
}
