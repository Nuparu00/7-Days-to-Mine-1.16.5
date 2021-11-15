package nuparu.sevendaystomine.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.util.SimplexNoise;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.city.Cars;

import java.util.Random;

public class FeatureCars<T extends IFeatureConfig> extends Feature<T> {

    private static SimplexNoise noise;

    public FeatureCars(Codec<T> p_i231922_1_) {
        super(p_i231922_1_);
    }

    public boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, T config) {
        int ci = (pos.getX() >> 4) << 4;
        int ck = (pos.getZ() >> 4) << 4;
        RegistryKey<World> dimensionType = world.getLevel().dimension();
        if (dimensionType != World.OVERWORLD || generator instanceof FlatChunkGenerator)
            return false;
        if (noise == null || noise.seed != world.getSeed()) {
            noise = new SimplexNoise(world.getSeed());
        }
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                double value = Math.abs(getNoiseValue(pos.getX() + i, pos.getZ() + j, 0));
                if (value < 0.005d) {
                    for (int k = CommonConfig.roadMaxY.get(); k >= CommonConfig.roadMinY.get(); --k) {
                        BlockPos blockPos = new BlockPos(pos.getX() + i, k, pos.getZ() + j);
                        BlockState state = world.getBlockState(blockPos);

                       if(state.getBlock() == ModBlocks.ASPHALT.get()){
                           if ((i > 3 && i < 13) && (j > 3 && j < 13) && random.nextInt(300) == 0) {
                               Cars.placeRandomCar(world,blockPos.above(), Utils.HORIZONTALS[random.nextInt(4)],random);
                           }
                       }
                    }
                }
            }
        }

        return true;

    }

    public static double getNoiseValue(int x, int y, int z) {
        if (noise == null)
            return 0;
        double q1 = noise.getNoise(x, y, z);
        /*
         * double q2 = noise.getNoise(x + 1.3, y + 0.7, 0.0);
         *
         * double r1 = noise.getNoise(x + 1 * q1 + 1.7, y + 1 * q2 + 9.2, 0.0); double
         * r2 = noise.getNoise(x + 1 * q1 + 8.3, y + 1 * q2 + 2.8, 0.0);
         *
         * double value = noise.getNoise(x + 2 * q1, y + 2 * q2, z);
         *
         */
        return q1;
    }

}