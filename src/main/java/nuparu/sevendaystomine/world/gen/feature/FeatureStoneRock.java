package nuparu.sevendaystomine.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.util.MathUtils;

import java.util.Random;

public class FeatureStoneRock<T extends IFeatureConfig> extends Feature<T> {

    public FeatureStoneRock(Codec<T> p_i231922_1_) {
        super(p_i231922_1_);
    }

    public boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, T config) {
        int ci = (pos.getX() >> 4) << 4;
        int ck = (pos.getZ() >> 4) << 4;
        RegistryKey<World> dimensionType = world.getLevel().dimension();
        if (dimensionType != World.OVERWORLD || generator instanceof FlatChunkGenerator)
            return false;
        if(CommonConfig.largeRockGenerationChance.get() <= 0) return false;
        if (random.nextInt(CommonConfig.largeRockGenerationChance.get()) == 0) {

            int x = 0;
            for (int a = 0; a < MathUtils.getIntInRange(random, CommonConfig.largeRockGenerationRateMin.get(), CommonConfig.largeRockGenerationRateMax.get()+1); a++) {
                int i = ci + random.nextInt(16);
                int k = ck + random.nextInt(16);
                int j = world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, i, k);
                BlockPos pos2 = new BlockPos(i, j, k);

                BlockPos below = pos2.below();
                BlockState belowState = world.getBlockState(below);
                if(!belowState.isFaceSturdy(world,below,Direction.UP)) return false;
                world.setBlock(pos2, ModBlocks.LARGE_ROCK.get().defaultBlockState(), 4);
                x++;
            }
            return x > 0;
        }
        return false;

    }
}