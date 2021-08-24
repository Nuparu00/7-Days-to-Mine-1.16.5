package nuparu.sevendaystomine.world.gen.city.street.traffic;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.block.BlockHorizontalBase;
import nuparu.sevendaystomine.block.BlockWallStreetSign;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntityStreetSign;
import nuparu.sevendaystomine.world.gen.city.street.Street;

import java.util.Random;

public class CityTrafficLight extends TrafficLight {

    int height = 5;

    @Override
    public void generate(WorldGenRegion world, BlockPos pos, Street street, Direction direction, int forward, int side, int offset, boolean end) {
        if(!street.canBranch) {
            direction = direction.getOpposite();
        }
        BlockState base = world.getBlockState(pos.below());
        if (base.getBlock() instanceof SlabBlock) {
            world.setBlock(pos.below(), street.city.type.pavementBlock == null ?  Blocks.STONE_BRICKS.defaultBlockState() : street.city.type.pavementBlock,0);
        }
        for (int i = 0; i < height; i++) {
            world.setBlock(pos.above(i), getRandomWall(street.city.rand),0);
        }

        int dir = end ? -1 : 1;

        for (int i = 1; i < 4; i++) {
            BlockPos pos2 = pos.above(height).relative(direction.getClockWise(), i * dir);
            BlockState state = getRandomWall(street.city.rand);
            world.setBlock(pos2, state,0);
            world.setBlock(pos2.relative(direction, 1 * dir),
                    ModBlocks.TRAFFIC_LIGHT.get().defaultBlockState().setValue(BlockHorizontalBase.FACING, direction.getOpposite()),0);
        }

        world.setBlock(pos.above(height), Blocks.CHISELED_STONE_BRICKS.defaultBlockState(),0);


        world.setBlock(pos.above(2).relative(direction.getClockWise(), 1 * dir), ModBlocks.TRAFFIC_LIGHT_PEDESTRIAN
                .get().defaultBlockState().setValue(BlockHorizontalBase.FACING, direction.getCounterClockWise()),0);
        world.setBlock(pos.above(2).relative(direction, -1 * dir), ModBlocks.TRAFFIC_LIGHT_PEDESTRIAN.get().defaultBlockState()
                .setValue(BlockHorizontalBase.FACING, direction),0);

        world.setBlock(pos.above(2).relative(direction, dir),
                ModBlocks.STREET_SIGN_WALL.get().defaultBlockState().setValue(BlockWallStreetSign.FACING, direction.getOpposite()),0);
        tryToSetSignText(street.city.world,pos.above(2).relative(direction, dir),street.name);
        String otherName = "";
        if (street.startCrossing != null && street.startCrossing.getStreets() != null) {
            for (Street st : street.startCrossing.getStreets()) {
                if (st.facing != direction && st.facing != direction.getOpposite()) {
                    otherName = st.name;
                    break;
                }
            }
        }

        world.setBlock(pos.above(2).relative(direction.getClockWise(), -dir), ModBlocks.STREET_SIGN_WALL.get().defaultBlockState()
                .setValue(BlockWallStreetSign.FACING, direction.getClockWise()),0);

        tryToSetSignText(street.city.world,pos.above(2).relative(direction.getClockWise(), -dir),otherName);
    }

    public BlockState getRandomWall(Random rand) {
        return rand.nextInt(8) == 0
                ? Blocks.MOSSY_COBBLESTONE_WALL.defaultBlockState()
                : Blocks.COBBLESTONE_WALL.defaultBlockState();
    }

    public void tryToSetSignText(WorldGenRegion world, BlockPos pos, String text) {
        TileEntity te = world.getBlockEntity(pos);
        if (te == null || !(te instanceof TileEntityStreetSign))
            return;
        TileEntityStreetSign sign = (TileEntityStreetSign) te;
        Iterable<String> result = Splitter.fixedLength(15).split(text);
        String[] parts = Iterables.toArray(result, String.class);
        for (int i = 0; i < 4; i++) {
            if (i + 1 > parts.length) {
                break;
            }
            sign.setMessage(i,new StringTextComponent(parts[i]));
        }
        sign.setColor(DyeColor.WHITE);
    }

}
