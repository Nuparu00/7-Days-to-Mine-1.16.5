package nuparu.sevendaystomine.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.block.BlockCar;
import nuparu.sevendaystomine.block.BlockGarbage;
import nuparu.sevendaystomine.block.BlockPaper;
import nuparu.sevendaystomine.block.BlockSandLayer;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModLootTables;
import nuparu.sevendaystomine.tileentity.TileEntityGarbage;
import nuparu.sevendaystomine.util.SimplexNoise;
import nuparu.sevendaystomine.util.Utils;

import java.util.Objects;
import java.util.Random;

public class FeatureRoads<T extends IFeatureConfig> extends Feature<T> {

    private static SimplexNoise noise;

    public FeatureRoads(Codec<T> p_i231922_1_) {
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

                        if (Utils.isSolid(world, blockPos, state) && state.getMaterial() != Material.WOOD) {
                            for (int l = 1; l < 4; l++) {
                                BlockState state2 = world.getBlockState(blockPos.above(l));
                                if (state2.getMaterial() == Material.WOOD ||
                                state2.getMaterial() == Material.LEAVES)
                                    break;
                                world.setBlock(blockPos.above(l), Blocks.AIR.defaultBlockState(),0);
                            }
                            BlockState state3 = world.getBlockState(blockPos.above());
                            if (state3.getMaterial().isReplaceable()) {
                                placeAsphalt(world,blockPos,random);
                            }
                            break;
                        }

                        else if (k == CommonConfig.roadMinY.get() && state.getBlock() == Blocks.AIR) {
                            placeAsphalt(world,blockPos.above(),random);
                            if (value < 0.001d && random.nextInt(10) == 0) {
                                for (int k2 = k; k2 > 0; --k2) {
                                    BlockPos pos2 = new BlockPos(pos.getX() + i, k2, pos.getZ() + j);
                                    BlockState state2 = world.getBlockState(pos2);
                                    if (!state2.getMaterial().isReplaceable()) {
                                        break;
                                    }
                                    world.setBlock(pos2, Blocks.COBBLESTONE.defaultBlockState(),0);
                                }

                            }
                            break;
                        }
                    }
                }
            }
        }

        return true;

    }

    public void placeAsphalt(ISeedReader world, BlockPos pos, Random rand){
        world.setBlock(pos,
                ModBlocks.ASPHALT.get().defaultBlockState(), 0);
        BlockState stateToReplace = world.getBlockState(pos.above());
        Biome biome = world.getBiome(pos);
        RegistryKey<Biome> biomeKey = RegistryKey.create(ForgeRegistries.Keys.BIOMES,
                Objects.requireNonNull(biome.getRegistryName(),
                        "Non existing biome detected!"));

        if(stateToReplace.getBlock() instanceof BlockCar) return;
        if (rand.nextInt(400) == 0) {
            world.setBlock(pos.above(), ModBlocks.GARBAGE.get().defaultBlockState()
                    .setValue(BlockGarbage.FACING, Utils.HORIZONTALS[rand.nextInt(4)]),0);
            TileEntity te = world.getBlockEntity(pos.above());
            if(te != null && te instanceof TileEntityGarbage){
                TileEntityGarbage garbage = (TileEntityGarbage)te;
                garbage.setLootTable(ModLootTables.GARBAGE, rand.nextLong());
            }
        }
        else if (rand.nextInt(80) == 0 ) {
            world.setBlock(pos.above(), ModBlocks.PAPER.get().defaultBlockState().setValue(BlockPaper.FACING, Utils.HORIZONTALS[rand.nextInt(4)]),0);
        }

        else if (rand.nextInt(8192) == 0 ) {
            world.setBlock(pos.above(), ModBlocks.SKELETON_TORSO.get().defaultBlockState().setValue(BlockPaper.FACING, Utils.HORIZONTALS[rand.nextInt(4)]),0);
        }

        else if (rand.nextInt(8192) == 0 ) {
            world.setBlock(pos.above(), ModBlocks.SKELETON.get().defaultBlockState().setValue(BlockPaper.FACING, Utils.HORIZONTALS[rand.nextInt(4)]),0);
        }
        else if (CommonConfig.sandRoadCover.get() && rand.nextInt(2) == 0 && BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.SANDY)){
            BlockState topFiller = biome.getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial();
            if(topFiller.getBlock() == Blocks.SAND){
                world.setBlock(pos.above(), ModBlocks.SAND_LAYER.get().defaultBlockState().setValue(BlockSandLayer.LAYERS, 1+ rand.nextInt(2)),0);
            }
            else if(topFiller.getBlock() == Blocks.RED_SAND){
                world.setBlock(pos.above(), ModBlocks.RED_SAND_LAYER.get().defaultBlockState().setValue(BlockSandLayer.LAYERS, 1+ rand.nextInt(2)),0);
            }
        }
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