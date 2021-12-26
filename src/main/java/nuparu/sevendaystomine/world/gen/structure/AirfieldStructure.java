package nuparu.sevendaystomine.world.gen.structure;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModStructureFeatures;
import nuparu.sevendaystomine.world.gen.feature.jigsaw.LargeJigsawManager;

import java.util.List;

public class AirfieldStructure extends Structure<NoFeatureConfig> {
    public AirfieldStructure(Codec<NoFeatureConfig> codec) {
        super(codec);
    }
    /**
     * This is how the worldgen code knows what to call when it
     * is time to create the pieces of the structure for generation.
     */
    @Override
    public  IStartFactory<NoFeatureConfig> getStartFactory() {
        return AirfieldStructure.Start::new;
    }


    /**
     *        : WARNING!!! DO NOT FORGET THIS METHOD!!!! :
     * If you do not override step method, your structure WILL crash the biome as it is being parsed!
     *
     * Generation stage for when to generate the structure. there are 10 stages you can pick from!
     * This surface structure stage places the structure before plants and ores are generated.
     */
    @Override
    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }


    /**
     * || ONLY WORKS IN FORGE 34.1.12+ ||
     *
     * This method allows us to have mobs that spawn naturally over time in our structure.
     * No other mobs will spawn in the structure of the same entity classification.
     * The reason you want to match the classifications is so that your structure's mob
     * will contribute to that classification's cap. Otherwise, it may cause a runaway
     * spawning of the mob that will never stop.
     *
     * NOTE: getDefaultSpawnList is for monsters only and getDefaultCreatureSpawnList is
     *       for creatures only. If you want to add entities of another classification,
     *       use the StructureSpawnListGatherEvent to add water_creatures, water_ambient,
     *       ambient, or misc mobs. Use that event to add/remove mobs from structures
     *       that are not your own.
     *
     * NOTE 2: getSpecialEnemies and getSpecialAnimals are the vanilla methods that Forge does
     *         not hook up. Do not use those methods or else the mobs won't spawn. You would
     *         have to manually implement spawning for them. Stick with Forge's Default form
     *         as it is easier to use that.
     */
    private static final List<MobSpawnInfo.Spawners> STRUCTURE_MONSTERS = ImmutableList.of(
    );
    @Override
    public List<MobSpawnInfo.Spawners> getDefaultSpawnList() {
        return STRUCTURE_MONSTERS;
    }

    private static final List<MobSpawnInfo.Spawners> STRUCTURE_CREATURES = ImmutableList.of(
    );
    @Override
    public List<MobSpawnInfo.Spawners> getDefaultCreatureSpawnList() {
        return STRUCTURE_CREATURES;
    }


    /*
     * This is where extra checks can be done to determine if the structure can spawn here.
     * This only needs to be overridden if you're adding additional spawn conditions.
     *
     * Fun fact, if you set your structure separation/spacing to be 0/1, you can use
     * isFeatureChunk to return true only if certain chunk coordinates are passed in
     * which allows you to spawn structures only at certain coordinates in the world.
     *
     * Notice how the biome is also passed in. Though, you are not going to
     * do any biome checking here as you should've added this structure to
     * the biomes you wanted already with the biome load event.
     *
     * Basically, this method is used for determining if the land is at a suitable height,
     * if certain other structures are too close or not, or some other restrictive condition.
     *
     * For example, Pillager Outposts added a check to make sure it cannot spawn within 10 chunk of a Village.
     * (Bedrock Edition seems to not have the same check)
     *
     *
     * Also, please for the love of god, do not do dimension checking here. If you do and
     * another mod's dimension is trying to spawn your structure, the locate
     * command will make minecraft hang forever and break the game.
     *
     * Instead, use the addDimensionalSpacing method in StructureTutorialMain class.
     * If you check for the dimension there and do not add your structure's
     * spacing into the chunk generator, the structure will not spawn in that dimension!
     */
    @Override
    protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeSource, long seed, SharedSeedRandom chunkRandom, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, NoFeatureConfig featureConfig) {
        if(isNear(ModStructureFeatures.CITY.get(),chunkGenerator,seed,chunkRandom,chunkX,chunkZ)) return false;
        if(isNear(ModStructureFeatures.AIRPLANE.get(),chunkGenerator,seed,chunkRandom,chunkX,chunkZ)) return false;
        if(isNear(ModStructureFeatures.OBSERVATORY.get(),chunkGenerator,seed,chunkRandom,chunkX,chunkZ)) return false;

        BlockPos centerOfChunk = new BlockPos(chunkX * 16, 0, chunkZ * 16);

        // Grab height of land. Will stop at first non-air block.
        int landHeight = chunkGenerator.getFirstOccupiedHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Type.WORLD_SURFACE_WG);

        // Grabs column of blocks at given position. In overworld, this column will be made of stone, water, and air.
        // In nether, it will be netherrack, lava, and air. End will only be endstone and air. It depends on what block
        // the chunk generator will place for that dimension.
        IBlockReader columnOfBlocks = chunkGenerator.getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ());

        // Combine the column of blocks with land height and you get the top block itself which you can test.
        BlockState topBlock = columnOfBlocks.getBlockState(centerOfChunk.above(landHeight));

        // Now we test to make sure our structure is not spawning on water or other fluids.
        // You can do height check instead too to make it spawn at high elevations.
        return topBlock.getFluidState().isEmpty(); //landHeight > 100;
    }

    private boolean isNear(Structure structure, ChunkGenerator p_242782_1_, long p_242782_2_, SharedSeedRandom p_242782_4_, int p_242782_5_, int p_242782_6_) {
        StructureSeparationSettings structureseparationsettings = p_242782_1_.getSettings().getConfig(structure);
        if (structureseparationsettings == null) {
            return false;
        } else {
            for(int i = p_242782_5_ - 10; i <= p_242782_5_ + 10; ++i) {
                for(int j = p_242782_6_ - 10; j <= p_242782_6_ + 10; ++j) {
                    ChunkPos chunkpos = structure.getPotentialFeatureChunk(structureseparationsettings, p_242782_2_, p_242782_4_, i, j);
                    if (i == chunkpos.x && j == chunkpos.z) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    /**
     * Handles calling up the structure's pieces class and height that structure will spawn at.
     */
    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void generatePieces(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {

            // Turns the chunk coordinates into actual coordinates we can use
            int x = chunkX * 16;
            int z = chunkZ * 16;

            BlockPos centerPos = new BlockPos(x, 0, z);
          LargeJigsawManager.addPieces(
                    dynamicRegistryManager,
                    new VillageConfig(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                            // The path to the starting Template Pool JSON file to read.
                            //
                            // Note, this is "structure_tutorial:run_down_house/start_pool" which means
                            // the game will automatically look into the following path for the template pool:
                            // "resources/data/structure_tutorial/worldgen/template_pool/run_down_house/start_pool.json"
                            // This is why your pool files must be in "data/<modid>/worldgen/template_pool/<the path to the pool here>"
                            // because the game automatically will check in worldgen/template_pool for the pools.
                            .get(new ResourceLocation(SevenDaysToMine.MODID, "scattered/airfield/airfield_pool")),

                            // How many pieces outward from center can a recursive jigsaw structure spawn.
                            // Our structure is only 1 piece outward and isn't recursive so any value of 1 or more doesn't change anything.
                            // However, I recommend you keep this a decent value like 10 so people can use datapacks to add additional pieces to your structure easily.
                            // But don't make it too large for recursive structures like villages or you'll crash server due to hundreds of pieces attempting to generate!
                            14),
                    AbstractVillagePiece::new,
                    chunkGenerator,
                    templateManagerIn,
                    centerPos, // Position of the structure. Y value is ignored if last parameter is set to true.
                    this.pieces, // The list that will be populated with the jigsaw pieces after this method.
                    this.random,
                    false, // Special boundary adjustments for villages. It's... hard to explain. Keep this false and make your pieces not be partially intersecting.
                    // Either not intersecting or fully contained will make children pieces spawn just fine. It's easier that way.
                    true);  // Place at heightmap (top land). Set this to false for structure to be place at the passed in blockpos's Y value instead.
            // Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.


            this.pieces.forEach(piece -> piece.move(0, 1, 0));
            this.pieces.forEach(piece -> piece.getBoundingBox().y0 -= 0);

            // Since by default, the start piece of a structure spawns with it's corner at centerPos
            // and will randomly rotate around that corner, we will center the piece on centerPos instead.
            // This is so that our structure's start piece is now centered on the water check done in isFeatureChunk.
            // Whatever the offset done to center the start piece, that offset is applied to all other pieces
            // so the entire structure is shifted properly to the new spot.
            Vector3i structureCenter = this.pieces.get(0).getBoundingBox().getCenter();
            int xOffset = centerPos.getX() - structureCenter.getX();
            int zOffset = centerPos.getZ() - structureCenter.getZ();
            for(StructurePiece structurePiece : this.pieces){
                structurePiece.move(xOffset, 0, zOffset);
            }
            this.calculateBoundingBox();
        }

    }
}