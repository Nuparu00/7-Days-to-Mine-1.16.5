package nuparu.sevendaystomine.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.util.MathUtils;

import java.util.Random;

public class FeatureSmallStone<T extends IFeatureConfig> extends Feature<T> {

    public FeatureSmallStone(Codec<T> p_i231922_1_) {
        super(p_i231922_1_);
    }

    public boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, T config) {
        int ci = (pos.getX() >> 4) << 4;
        int ck = (pos.getZ() >> 4) << 4;
        RegistryKey<World> dimensionType = world.getLevel().dimension();
        if (dimensionType != World.OVERWORLD || generator instanceof FlatChunkGenerator)
            return false;
        if (CommonConfig.smallRockGenerationChance.get() <= 0) return false;
        if (random.nextInt(CommonConfig.smallRockGenerationChance.get()) == 0) {

            int x = 0;
            for (int a = 0; a < MathUtils.getIntInRange(random, CommonConfig.smallRockGenerationRateMin.get(), CommonConfig.smallRockGenerationRateMax.get() + 1); a++) {
                int i = ci + random.nextInt(16);
                int k = ck + random.nextInt(16);
                int j = world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, i, k);
                BlockPos pos2 = new BlockPos(i, j, k);

                BlockPos below = pos2.below();
                BlockState belowState = world.getBlockState(below);
                if (!belowState.isFaceSturdy(world, below, Direction.UP)) continue;
                world.setBlock(pos2, getRandomRock(random), 4);
                x++;
            }
            return x > 0;
        }
        return false;

    }

    static Block[] blocks;

    public static BlockState getRandomRock(Random random) {
        if (blocks == null) {
            blocks = new Block[]{ModBlocks.SMALL_ROCK_STONE.get(), ModBlocks.SMALL_ROCK_ANDESITE.get(), ModBlocks.SMALL_ROCK_DIORITE.get(), ModBlocks.SMALL_ROCK_GRANITE.get()};
        }
        return blocks[random.nextInt(blocks.length)].defaultBlockState();

    }
}