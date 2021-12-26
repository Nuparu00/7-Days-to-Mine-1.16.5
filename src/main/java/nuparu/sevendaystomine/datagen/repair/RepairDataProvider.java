package nuparu.sevendaystomine.datagen.repair;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.loot.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.block.repair.RepairDataManager;
import nuparu.sevendaystomine.block.repair.RepairEntry;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModItems;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RepairDataProvider implements IDataProvider {
    private static final Logger LOGGER = LogManager.getLogger();

    private final DataGenerator generator;
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public RepairDataProvider(DataGenerator generator) {
        this.generator = generator;
    }

    private static Path createPath(Path p_218439_0_, ResourceLocation p_218439_1_) {
        return p_218439_0_.resolve("data/" + p_218439_1_.getNamespace() + "/repairs/" + p_218439_1_.getPath() + ".json");
    }

    @Override
    public void run(DirectoryCache p_200398_1_) throws IOException {
        Path path = this.generator.getOutputFolder();
        Map<ResourceLocation, RepairEntry> map = Maps.newHashMap();
        List<RepairEntry> entries = gatherRepairEntries();
        for(RepairEntry entry : entries){
            Path path1 = createPath(path, entry.name);
            try {
                System.out.println(RepairEntry.serialize(entry));
                IDataProvider.save(GSON, p_200398_1_, RepairEntry.serialize(entry), path1);
            } catch (Exception ioexception) {
                LOGGER.error("Couldn't save repair entry {}", path1, ioexception);
            }
        }
    }

    public List<RepairEntry> gatherRepairEntries(){
        ArrayList<RepairEntry> entries = new ArrayList<>();
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.NOTE_BLOCK));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.JUKEBOX));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.CHEST));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.TRAPPED_CHEST));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.CRAFTING_TABLE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.DRESSER.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.IRON_SCRAP.get())).setRepairAmount(0.1).build(ModBlocks.COMPUTER.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.IRON_SCRAP.get())).setRepairAmount(0.1).build(ModBlocks.MONITOR_OFF.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.IRON_SCRAP.get())).setRepairAmount(0.1).build(ModBlocks.MONITOR_LINUX.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.IRON_SCRAP.get())).setRepairAmount(0.1).build(ModBlocks.MONITOR_MAC.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.IRON_SCRAP.get())).setRepairAmount(0.1).build(ModBlocks.MONITOR_WIN7.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.IRON_SCRAP.get())).setRepairAmount(0.1).build(ModBlocks.MONITOR_WIN8.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.IRON_SCRAP.get())).setRepairAmount(0.1).build(ModBlocks.MONITOR_WIN10.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.IRON_SCRAP.get())).setRepairAmount(0.1).build(ModBlocks.MONITOR_WIN98.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.IRON_SCRAP.get())).setRepairAmount(0.1).build(ModBlocks.MONITOR_WINXP.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.IRON_SCRAP.get())).setRepairAmount(0.1).build(ModBlocks.COOKING_POT.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.IRON_SCRAP.get())).setRepairAmount(0.1).build(ModBlocks.COOKING_GRILL.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.IRON_SCRAP.get())).setRepairAmount(0.1).build(ModBlocks.COOKING_GRILL_BEAKER.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.GLASS_SCRAP.get())).setRepairAmount(0.1).build(ModBlocks.BEAKER.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SAND_DUST.get())).setRepairAmount(0.1).build(ModBlocks.SANDBAGS.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SAND_DUST.get())).setRepairAmount(0.1).build(ModBlocks.SAND_LAYER.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SAND_DUST.get())).setRepairAmount(0.1).build(ModBlocks.RED_SAND_LAYER.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.GLASS_SCRAP.get())).setRepairAmount(0.1).build(ModBlocks.REDSTONE_LAMP_BROKEN.get()));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.ACACIA_LOG_SPIKE.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.BIRCH_LOG_SPIKE.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.DARK_OAK_LOG_SPIKE.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.OAK_LOG_SPIKE.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.SPRUCE_LOG_SPIKE.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.JUNGLE_LOG_SPIKE.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_FUNGI.get())).setRepairAmount(0.1).build(ModBlocks.CRIMSON_LOG_SPIKE.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_FUNGI.get())).setRepairAmount(0.1).build(ModBlocks.WARPED_LOG_SPIKE.get()));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.BLACK_BED));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.BLUE_BED));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.BROWN_BED));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.CYAN_BED));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.GRAY_BED));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.GREEN_BED));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.LIGHT_BLUE_BED));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.LIGHT_GRAY_BED));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.LIME_BED));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.MAGENTA_BED));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.ORANGE_BED));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.PINK_BED));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.RED_BED));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.WHITE_BED));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.PURPLE_BED));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.YELLOW_BED));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.STRING)).setRepairAmount(0.1).build(Blocks.BLACK_WOOL));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.STRING)).setRepairAmount(0.1).build(Blocks.BLUE_WOOL));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.STRING)).setRepairAmount(0.1).build(Blocks.BROWN_WOOL));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.STRING)).setRepairAmount(0.1).build(Blocks.CYAN_WOOL));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.STRING)).setRepairAmount(0.1).build(Blocks.GRAY_WOOL));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.STRING)).setRepairAmount(0.1).build(Blocks.GREEN_WOOL));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.STRING)).setRepairAmount(0.1).build(Blocks.LIGHT_BLUE_WOOL));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.STRING)).setRepairAmount(0.1).build(Blocks.LIGHT_GRAY_WOOL));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.STRING)).setRepairAmount(0.1).build(Blocks.LIME_WOOL));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.STRING)).setRepairAmount(0.1).build(Blocks.MAGENTA_WOOL));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.STRING)).setRepairAmount(0.1).build(Blocks.ORANGE_WOOL));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.STRING)).setRepairAmount(0.1).build(Blocks.PINK_WOOL));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.STRING)).setRepairAmount(0.1).build(Blocks.RED_WOOL));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.STRING)).setRepairAmount(0.1).build(Blocks.WHITE_WOOL));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.STRING)).setRepairAmount(0.1).build(Blocks.PURPLE_WOOL));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.STRING)).setRepairAmount(0.1).build(Blocks.YELLOW_WOOL));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.TABLE_ACACIA.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.TABLE_BIRCH.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.TABLE_BIG_OAK.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.TABLE_JUNGLE.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.TABLE_OAK.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.TABLE_SPRUCE.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_FUNGI.get())).setRepairAmount(0.1).build(ModBlocks.TABLE_CRIMSON.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_FUNGI.get())).setRepairAmount(0.1).build(ModBlocks.TABLE_WARPED.get()));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.CHAIR_ACACIA.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.CHAIR_BIRCH.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.CHAIR_BIG_OAK.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.CHAIR_JUNGLE.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.CHAIR_OAK.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.CHAIR_SPRUCE.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_FUNGI.get())).setRepairAmount(0.1).build(ModBlocks.CHAIR_CRIMSON.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_FUNGI.get())).setRepairAmount(0.1).build(ModBlocks.CHAIR_WARPED.get()));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.ACACIA_LOG));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.BIRCH_LOG));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.DARK_OAK_LOG));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.JUNGLE_LOG));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.OAK_LOG));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.SPRUCE_LOG));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_FUNGI.get())).setRepairAmount(0.1).build(Blocks.CRIMSON_STEM));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_FUNGI.get())).setRepairAmount(0.1).build(Blocks.WARPED_STEM));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.ACACIA_BOOKSHELF.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.BIRCH_BOOKSHELF.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.DARK_OAK_BOOKSHELF.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.JUNGLE_BOOKSHELF.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.OAK_BOOKSHELF.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(ModBlocks.SPRUCE_BOOKSHELF.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_FUNGI.get())).setRepairAmount(0.1).build(ModBlocks.CRIMSON_BOOKSHELF.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_FUNGI.get())).setRepairAmount(0.1).build(ModBlocks.WARPED_BOOKSHELF.get()));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.OAK_DOOR));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.OAK_FENCE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.OAK_FENCE_GATE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.OAK_PRESSURE_PLATE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.OAK_BUTTON));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.OAK_SIGN));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.OAK_WALL_SIGN));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.OAK_SLAB));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.OAK_STAIRS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.OAK_TRAPDOOR));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.ACACIA_DOOR));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.ACACIA_FENCE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.ACACIA_FENCE_GATE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.ACACIA_PRESSURE_PLATE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.ACACIA_BUTTON));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.ACACIA_SIGN));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.ACACIA_WALL_SIGN));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.ACACIA_SLAB));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.ACACIA_STAIRS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.ACACIA_TRAPDOOR));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.BIRCH_DOOR));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.BIRCH_FENCE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.BIRCH_FENCE_GATE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.BIRCH_PRESSURE_PLATE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.BIRCH_BUTTON));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.BIRCH_SIGN));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.BIRCH_WALL_SIGN));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.BIRCH_SLAB));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.BIRCH_STAIRS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.BIRCH_TRAPDOOR));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.JUNGLE_DOOR));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.JUNGLE_FENCE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.JUNGLE_FENCE_GATE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.JUNGLE_PRESSURE_PLATE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.JUNGLE_BUTTON));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.JUNGLE_SIGN));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.JUNGLE_WALL_SIGN));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.JUNGLE_SLAB));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.JUNGLE_STAIRS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.JUNGLE_TRAPDOOR));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.SPRUCE_DOOR));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.SPRUCE_FENCE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.SPRUCE_FENCE_GATE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.SPRUCE_PRESSURE_PLATE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.SPRUCE_BUTTON));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.SPRUCE_SIGN));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.SPRUCE_WALL_SIGN));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.SPRUCE_SLAB));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.SPRUCE_STAIRS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.SPRUCE_TRAPDOOR));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.DARK_OAK_DOOR));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.DARK_OAK_FENCE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.DARK_OAK_FENCE_GATE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.DARK_OAK_PRESSURE_PLATE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.DARK_OAK_BUTTON));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.DARK_OAK_SIGN));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.DARK_OAK_WALL_SIGN));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.DARK_OAK_SLAB));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.DARK_OAK_STAIRS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.DARK_OAK_TRAPDOOR));



        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.STRIPPED_ACACIA_LOG));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.STRIPPED_BIRCH_LOG));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.STRIPPED_DARK_OAK_LOG));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.STRIPPED_JUNGLE_LOG));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.STRIPPED_OAK_LOG));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.STRIPPED_SPRUCE_LOG));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_FUNGI.get())).setRepairAmount(0.1).build(Blocks.STRIPPED_CRIMSON_STEM));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_FUNGI.get())).setRepairAmount(0.1).build(Blocks.STRIPPED_WARPED_STEM));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.ACACIA_PLANKS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.BIRCH_PLANKS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.DARK_OAK_PLANKS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.JUNGLE_PLANKS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.OAK_PLANKS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.1).build(Blocks.SPRUCE_PLANKS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_FUNGI.get())).setRepairAmount(0.1).build(Blocks.CRIMSON_PLANKS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_FUNGI.get())).setRepairAmount(0.1).build(Blocks.WARPED_PLANKS));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.2).build(ModBlocks.ACACIA_FRAME.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.2).build(ModBlocks.BIRCH_FRAME.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.2).build(ModBlocks.DARK_OAK_FRAME.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.2).build(ModBlocks.JUNGLE_FRAME.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.2).build(ModBlocks.OAK_FRAME.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.2).build(ModBlocks.SPRUCE_FRAME.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.2).build(ModBlocks.CRIMSON_FRAME.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.2).build(ModBlocks.WARPED_FRAME.get()));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.2).build(ModBlocks.ACACIA_PLANKS_REINFORCED.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.2).build(ModBlocks.BIRCH_PLANKS_REINFORCED.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.2).build(ModBlocks.DARK_OAK_PLANKS_REINFORCED.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.2).build(ModBlocks.JUNGLE_PLANKS_REINFORCED.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.2).build(ModBlocks.OAK_PLANKS_REINFORCED.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.2).build(ModBlocks.SPRUCE_PLANKS_REINFORCED.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.2).build(ModBlocks.CRIMSON_PLANKS_REINFORCED.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.PLANK_WOOD.get())).setRepairAmount(0.2).build(ModBlocks.WARPED_PLANKS_REINFORCED.get()));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.IRON_SCRAP.get())).setRepairAmount(0.2).build(ModBlocks.ACACIA_PLANKS_REINFORCED_IRON.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.IRON_SCRAP.get())).setRepairAmount(0.2).build(ModBlocks.BIRCH_PLANKS_REINFORCED_IRON.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.IRON_SCRAP.get())).setRepairAmount(0.2).build(ModBlocks.DARK_OAK_PLANKS_REINFORCED_IRON.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.IRON_SCRAP.get())).setRepairAmount(0.2).build(ModBlocks.JUNGLE_PLANKS_REINFORCED_IRON.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.IRON_SCRAP.get())).setRepairAmount(0.2).build(ModBlocks.OAK_PLANKS_REINFORCED_IRON.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.IRON_SCRAP.get())).setRepairAmount(0.2).build(ModBlocks.SPRUCE_PLANKS_REINFORCED_IRON.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.IRON_SCRAP.get())).setRepairAmount(0.2).build(ModBlocks.CRIMSON_PLANKS_REINFORCED_IRON.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.IRON_SCRAP.get())).setRepairAmount(0.2).build(ModBlocks.WARPED_PLANKS_REINFORCED_IRON.get()));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.STONE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.ANDESITE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.GRANITE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.DIORITE));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.STRUCTURE_STONE.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.STRUCTURE_ANDESITE.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.STRUCTURE_GRANITE.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.STRUCTURE_DIORITE.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.BRICK_MOSSY.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.ANDESITE_BRICKS.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.ANDESITE_BRICKS_CRACKED.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.ANDESITE_BRICKS_MOSSY.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.DIORITE_BRICKS.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.DIORITE_BRICKS_CRACKED.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.DIORITE_BRICKS_MOSSY.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.GRANITE_BRICKS.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.GRANITE_BRICKS_CRACKED.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.GRANITE_BRICKS_MOSSY.get()));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.MARBLE.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.BASALT.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.RHYOLITE.get()));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.MARBLE_COBBLESTONE.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.BASALT_COBBLESTONE.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.RHYOLITE_COBBLESTONE.get()));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.MARBLE_BRICKS.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.BASALT_BRICKS.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.RHYOLITE_BRICKS.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.MARBLE_BRICKS_CRACKED.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.BASALT_BRICKS_CRACKED.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.RHYOLITE_BRICKS_CRACKED.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.MARBLE_BRICKS_MOSSY.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.BASALT_BRICKS_MOSSY.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.RHYOLITE_BRICKS_MOSSY.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.MARBLE_POLISHED.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.BASALT_POLISHED.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.RHYOLITE_POLISHED.get()));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.COBBLESTONE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.MOSSY_COBBLESTONE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.STONE_BRICKS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.STONE_BRICK_STAIRS_CRACCKED.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.MOSSY_STONE_BRICKS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.CRACKED_STONE_BRICKS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.CHISELED_STONE_BRICKS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.STONE_SLAB));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.STONE_BRICK_SLAB));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.SMOOTH_STONE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.SMOOTH_STONE_SLAB));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.STONE_BUTTON));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.STONE_PRESSURE_PLATE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.STONE_STAIRS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.COBBLESTONE_SLAB));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.COBBLESTONE_STAIRS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.COBBLESTONE_WALL));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.MOSSY_COBBLESTONE_SLAB));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.MOSSY_COBBLESTONE_STAIRS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.MOSSY_STONE_BRICK_SLAB));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.MOSSY_STONE_BRICK_WALL));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.FURNACE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.DISPENSER));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.DROPPER));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.IRON_INGOT)).build(Blocks.BLAST_FURNACE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(Blocks.SMOKER));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.IRON_INGOT)).build(Blocks.IRON_DOOR));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.IRON_INGOT)).build(Blocks.IRON_TRAPDOOR));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SMALL_STONE.get())).build(ModBlocks.FORGE.get()));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.IRON_INGOT)).build(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.GOLD_INGOT)).build(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.PRISMARINE_SHARD)).build(Blocks.PRISMARINE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.PRISMARINE_SHARD)).build(Blocks.PRISMARINE_BRICK_SLAB));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.PRISMARINE_SHARD)).build(Blocks.PRISMARINE_BRICK_STAIRS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.PRISMARINE_SHARD)).build(Blocks.PRISMARINE_BRICKS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.PRISMARINE_SHARD)).build(Blocks.PRISMARINE_SLAB));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.PRISMARINE_SHARD)).build(Blocks.PRISMARINE_STAIRS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.PRISMARINE_SHARD)).build(Blocks.PRISMARINE_WALL));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.PRISMARINE_SHARD)).build(Blocks.DARK_PRISMARINE));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.PRISMARINE_SHARD)).build(Blocks.DARK_PRISMARINE_SLAB));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.PRISMARINE_SHARD)).build(Blocks.DARK_PRISMARINE_STAIRS));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.IRON_INGOT)).build(Blocks.IRON_BLOCK));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.GOLD_INGOT)).build(Blocks.GOLD_BLOCK));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.DIAMOND)).build(Blocks.DIAMOND_BLOCK));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.REDSTONE)).build(Blocks.REDSTONE_BLOCK));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.LAPIS_LAZULI)).build(Blocks.LAPIS_BLOCK));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.COAL)).build(Blocks.COAL_BLOCK));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.CHARCOAL)).build(Blocks.COAL_BLOCK));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.LAPIS_LAZULI)).build(Blocks.LAPIS_BLOCK));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.BONE)).build(Blocks.BONE_BLOCK));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.QUARTZ)).build(Blocks.QUARTZ_BLOCK));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.QUARTZ)).build(Blocks.QUARTZ_BRICKS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.QUARTZ)).build(Blocks.QUARTZ_PILLAR));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.QUARTZ)).build(Blocks.QUARTZ_SLAB));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.QUARTZ)).build(Blocks.QUARTZ_STAIRS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.QUARTZ)).build(Blocks.CHISELED_QUARTZ_BLOCK));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.QUARTZ)).build(Blocks.SMOOTH_QUARTZ));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.QUARTZ)).build(Blocks.SMOOTH_QUARTZ_SLAB));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.QUARTZ)).build(Blocks.SMOOTH_QUARTZ_STAIRS));


        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.INGOT_COPPER.get())).build(ModBlocks.COPPER_BLOCK.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.INGOT_BRONZE.get())).build(ModBlocks.BRONZE_BLOCK.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.INGOT_BRASS.get())).build(ModBlocks.BRASS_BLOCK.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.INGOT_LEAD.get())).build(ModBlocks.LEAD_BLOCK.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.INGOT_STEEL.get())).build(ModBlocks.STEEL_BLOCK.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.NETHERITE_INGOT)).build(Blocks.NETHERITE_BLOCK));

        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.BRICK)).build(Blocks.BRICKS));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.BRICK)).build(ModBlocks.BRICK_MOSSY.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.BRICK)).build(ModBlocks.BRICK_MOSSY_SLAB.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.BRICK)).build(ModBlocks.BRICKS_MOSSY_STAIRS.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.BRICK)).build(ModBlocks.DARK_BRICKS.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.BRICK)).build(ModBlocks.DARK_BRICKS_SLAB.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.BRICK)).build(ModBlocks.DARK_BRICKS_STAIRS.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.BRICK)).build(ModBlocks.DARK_BRICKS_MOSSY.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.BRICK)).build(ModBlocks.DARK_BRICKS_MOSSY_SLAB.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.BRICK)).build(ModBlocks.DARK_BRICKS_MOSSY_STAIRS.get()));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SAND_DUST.get())).build(Blocks.SAND));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(ModItems.SAND_DUST.get())).build(Blocks.RED_SAND));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.CLAY_BALL)).build(Blocks.CLAY));


        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.SHULKER_SHELL)).build(Blocks.SHULKER_BOX));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.SHULKER_SHELL)).build(Blocks.BLACK_SHULKER_BOX));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.SHULKER_SHELL)).build(Blocks.BLUE_SHULKER_BOX));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.SHULKER_SHELL)).build(Blocks.BROWN_SHULKER_BOX));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.SHULKER_SHELL)).build(Blocks.CYAN_SHULKER_BOX));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.SHULKER_SHELL)).build(Blocks.GRAY_SHULKER_BOX));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.SHULKER_SHELL)).build(Blocks.GREEN_SHULKER_BOX));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.SHULKER_SHELL)).build(Blocks.LIGHT_BLUE_SHULKER_BOX));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.SHULKER_SHELL)).build(Blocks.LIGHT_GRAY_SHULKER_BOX));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.SHULKER_SHELL)).build(Blocks.LIME_SHULKER_BOX));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.SHULKER_SHELL)).build(Blocks.MAGENTA_SHULKER_BOX));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.SHULKER_SHELL)).build(Blocks.ORANGE_SHULKER_BOX));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.SHULKER_SHELL)).build(Blocks.PINK_SHULKER_BOX));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.SHULKER_SHELL)).build(Blocks.PURPLE_SHULKER_BOX));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.SHULKER_SHELL)).build(Blocks.RED_SHULKER_BOX));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.SHULKER_SHELL)).build(Blocks.WHITE_SHULKER_BOX));
        entries.add(new RepairEntry.Builder().addRepairItem(new ItemStack(Items.SHULKER_SHELL)).build(Blocks.YELLOW_SHULKER_BOX));

        return entries;
    }

    @Override
    public String getName() {
        return "RepairData";
    }

}
