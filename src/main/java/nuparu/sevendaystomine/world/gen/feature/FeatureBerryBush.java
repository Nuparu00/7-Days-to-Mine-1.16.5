package nuparu.sevendaystomine.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import nuparu.sevendaystomine.block.BlockFruitBush;
import nuparu.sevendaystomine.init.ModBlocks;

import java.util.Random;

public class FeatureBerryBush<T extends BlockClusterFeatureConfig> extends Feature<T> {

    public FeatureBerryBush(Codec<T> p_i231922_1_) {
        super(p_i231922_1_);
    }
    
    public boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, T config) {
        BlockState blockstate = (random.nextBoolean() ? ModBlocks.BLUEBERRY_PLANT.get() : ModBlocks.BANEBERRY_PLANT.get()).defaultBlockState();
        BlockPos blockpos;
        if (config.project) {
            blockpos = world.getHeightmapPos(Heightmap.Type.WORLD_SURFACE_WG, pos);
        } else {
            blockpos = pos;
        }

        int i = 0;
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();


        for(int j = 0; j < config.tries; ++j) {
            blockpos$mutable.setWithOffset(blockpos, random.nextInt(config.xspread + 1) - random.nextInt(config.xspread + 1), random.nextInt(config.yspread + 1) - random.nextInt(config.yspread + 1), random.nextInt(config.zspread + 1) - random.nextInt(config.zspread + 1));
            BlockPos blockpos1 = blockpos$mutable.below();
            BlockState blockstate1 = world.getBlockState(blockpos1);
            if ((world.isEmptyBlock(blockpos$mutable) || config.canReplace && world.getBlockState(blockpos$mutable).getMaterial().isReplaceable()) && blockstate.canSurvive(world, blockpos$mutable) && (config.whitelist.isEmpty() || config.whitelist.contains(blockstate1.getBlock())) && !config.blacklist.contains(blockstate1) && (!config.needWater || world.getFluidState(blockpos1.west()).is(FluidTags.WATER) || world.getFluidState(blockpos1.east()).is(FluidTags.WATER) || world.getFluidState(blockpos1.north()).is(FluidTags.WATER) || world.getFluidState(blockpos1.south()).is(FluidTags.WATER))) {
                blockstate = blockstate.setValue(BlockFruitBush.AGE, random.nextInt(8));
                config.blockPlacer.place(world, blockpos$mutable, blockstate, random);
                ++i;
            }
        }

        return i > 0;

    }

}