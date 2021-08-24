package nuparu.sevendaystomine.world.gen.city.plot.building;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.properties.StructureMode;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModLootTables;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.city.CityType;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Building {
    @Nullable
    public ResourceLocation res;
    public ResourceLocation registryName;
    public int weight;
    public int yOffset = 0;
    public boolean canBeMirrored = true;
    @Nullable
    public BlockState pedestalState;
    public boolean hasPedestal = true;

    public Set<Biome> allowedBiomes;
    public Block[] allowedBlocks;
    public Set<CityType> allowedCityTypes;

    public Building(ResourceLocation res, int weight) {
        this(res, weight, 0);
    }

    public Building(ResourceLocation res, int weight, int yOffset) {
        this(res, weight, yOffset, null);
    }

    public Building(ResourceLocation res, int weight, int yOffset, BlockState pedestalState) {
        this.res = res;
        this.registryName = res;
        this.weight = weight;
        this.yOffset = yOffset;
        this.pedestalState = pedestalState;
        this.allowedBiomes = null;
    }

    public void generate(ServerWorld world, BlockPos pos, Direction facing, boolean mirror, Random rand) {

        MinecraftServer minecraftserver = world.getServer();
        TemplateManager templatemanager = world.getStructureManager();

        Template template = templatemanager.get(res);
        if (template == null) {
            return;
        }

        Rotation rot = Utils.facingToRotation(facing.getCounterClockWise());
        BlockPos size = template.getSize(rot);

        pos = pos.above(yOffset);

        PlacementSettings placementsettings = (new PlacementSettings())
                .setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot).setIgnoreEntities(false)
                .setChunkPos((ChunkPos) null);

        generateTemplate(world, pos, mirror, facing, placementsettings, template, hasPedestal, rand);
    }

    public void generateTemplate(ServerWorld world, BlockPos pos, boolean mirror, Direction facing,
                                 PlacementSettings placementsettings, Template template, boolean pedestal, Random rand) {
        generateTemplate(world, pos, mirror, facing, placementsettings, template, pedestal, rand, true);
    }

    public void generateTemplate(ServerWorld world, BlockPos pos, boolean mirror, Direction facing,
                                 PlacementSettings placeSettings, Template template, boolean pedestal, Random rand, boolean sand) {
        template.placeInWorld(world, pos, placeSettings, rand);
        for (Template.BlockInfo blockInfo : template.filterBlocks(pos, placeSettings, Blocks.STRUCTURE_BLOCK)) {
            if (blockInfo.nbt != null) {
                StructureMode structuremode = StructureMode.valueOf(blockInfo.nbt.getString("mode"));
                if (structuremode == StructureMode.DATA) {
                    handleDataBlock(world, facing, blockInfo.pos, blockInfo.nbt.getString("metadata"), mirror, rand);
                }
            }
        }
        if (pedestal) {
            generatePedestal(world, pos, template, facing, mirror);
        }
        if (sand) {
            coverWithSand(world, pos, template, facing, mirror, rand);
        }
    }

    public void handleDataBlock(ServerWorld world, Direction facing, BlockPos pos, String data, boolean mirror, Random rand) {
        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        boolean flag = false;
        switch (data) {
            case "cardboard":
                if (rand.nextInt(5) == 0) {
                    world.setBlockAndUpdate(pos, ModBlocks.CARDBOARD_BOX.get().defaultBlockState());
                }
                flag = true;
                break;
        }

        if (!flag) {
            Utils.getLogger().error("Structure " + this.registryName.toString() + " did not handle Structure Block " + data);
        }
    }

    /*
     * Dimensions of the building, necessary for city buildings. Scattered buildings
     * do not require overriding this, though it is encouraged.
     */
    public BlockPos getDimensions(ServerWorld world, Direction facing, boolean mirror) {
        if (res == null)
            return BlockPos.ZERO;
        MinecraftServer minecraftserver = world.getServer();
        TemplateManager templatemanager = world.getStructureManager();

        Template template = templatemanager.get(res);
        if (template == null) {
            return BlockPos.ZERO;
        }
        Rotation rot = Utils.facingToRotation(facing.getCounterClockWise());
        return template.getSize(rot).offset(1,0,1);
    }

    /*
     * Generates a pedestal under the structure with the shape of the bottom most
     * layer of the structure. Uses either the bottom most blockstate of the
     * structure proper
     */
    public void generatePedestal(ServerWorld world, BlockPos pos, Template template, Direction facing, boolean mirror) {

    }

    public void coverWithSand(ServerWorld world, BlockPos pos, Template template, Direction facing, boolean mirror,
                              Random rand) {

    }

    public Building setAllowedBiomes(Biome... biomes) {
        this.allowedBiomes = new HashSet<Biome>(Arrays.asList(biomes));
        return this;
    }

    public Building setAllowedBlocks(Block... blocks) {
        this.allowedBlocks = blocks;
        return this;
    }

    public Building setAllowedCityTypes(CityType... types) {
        this.allowedCityTypes = new HashSet<CityType>(Arrays.asList(types));
        return this;
    }

    public Building setPedestal(BlockState pedestal) {
        this.pedestalState = pedestal;
        return this;
    }

    public Building setHasPedestal(boolean hasPedestal) {
        this.hasPedestal = hasPedestal;
        return this;
    }

    public Building setCanBeMirrored(boolean canBeMirrored) {
        this.canBeMirrored = canBeMirrored;
        return this;
    }

    public ResourceLocation getBookshelfLootTable(Random rand) {
        return rand.nextInt(12) == 0 ? ModLootTables.BOOKSHELF_RARE : ModLootTables.BOOKSHELF_COMMON;
    }
}
