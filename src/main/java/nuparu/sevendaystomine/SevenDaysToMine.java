package nuparu.sevendaystomine;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.block.WoodType;
import net.minecraft.command.CommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import nuparu.sevendaystomine.advancements.ModTriggers;
import nuparu.sevendaystomine.block.repair.RepairManager;
import nuparu.sevendaystomine.capability.CapabilityHandler;
import nuparu.sevendaystomine.capability.ChunkDataProvider;
import nuparu.sevendaystomine.capability.ExtendedInventoryProvider;
import nuparu.sevendaystomine.capability.ExtendedPlayerProvider;
import nuparu.sevendaystomine.client.gui.ConfigScreen;
import nuparu.sevendaystomine.command.*;
import nuparu.sevendaystomine.config.ConfigHelper;
import nuparu.sevendaystomine.crafting.RecipeManager;
import nuparu.sevendaystomine.events.*;
import nuparu.sevendaystomine.init.*;
import nuparu.sevendaystomine.item.guide.BookDataManager;
import nuparu.sevendaystomine.loot.function.ModLootFunctionManager;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.potions.Potions;
import nuparu.sevendaystomine.proxy.ClientProxy;
import nuparu.sevendaystomine.proxy.CommonProxy;
import nuparu.sevendaystomine.proxy.StartupClient;
import nuparu.sevendaystomine.proxy.StartupCommon;
import nuparu.sevendaystomine.util.VanillaManager;
import nuparu.sevendaystomine.util.book.BookData;
import nuparu.sevendaystomine.util.book.BookData.CraftingMatrix;
import nuparu.sevendaystomine.util.book.BookData.Page;
import nuparu.sevendaystomine.world.gen.city.CityBuildings;
import nuparu.sevendaystomine.world.gen.city.CityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map.Entry;

@Mod(SevenDaysToMine.MODID)
public class SevenDaysToMine {
    public static final String MODID = "sevendaystomine";

    public static final Logger LOGGER = LogManager.getLogger();

    static final String CLIENT_PROXY_CLASS = "nuparu.sevendaystomine.proxy.ClientProxy";
    static final String SERVER_PROXY_CLASS = "nuparu.sevendaystomine.proxy.CommonProxy";
    public static SevenDaysToMine instance;
    public static CommonProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(),
            () -> () -> new CommonProxy());


    public static final WoodType STREET_WOOD_TYPE = WoodType.create(new ResourceLocation(SevenDaysToMine.MODID, "street").toString());

    public SevenDaysToMine() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        bus.addListener(this::onConstruct);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHelper.commonConfig);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHelper.clientConfig);
        /*ConfigHelper.loadConfig(ConfigHelper.commonConfig,
                FMLPaths.CONFIGDIR.get().resolve("sevendaystomine-common.toml").toString());

        ConfigHelper.loadConfig(ConfigHelper.clientConfig,
                FMLPaths.CONFIGDIR.get().resolve("sevendaystomine-client.toml").toString());*/

        // Register the configuration GUI factory
        /*ModLoadingContext.get().registerExtensionPoint(
                ExtensionPoint.CONFIGGUIFACTORY,
                () -> (mc, screen) -> new ConfigScreen()
        );*/


        MinecraftForge.EVENT_BUS.register(this);
        bus.register(ModTileEntities.class);
        bus.register(StartupCommon.class);
        bus.register(StartupClient.class);

        ModSounds.SOUNDS.register(bus);
        ModBlocks.BLOCKS.register(bus);
        ModItems.ITEMS.register(bus);
        Potions.EFFECTS.register(bus);
        ModDataSerializers.SERIALIZERS.register(bus);

        ModFluids.FLUIDS.register(bus);
        ModRecipes.RECIPES.register(bus);

        ModTileEntities.TILE_ENTITIES.register(bus);
        ModContainers.CONTAINERS.register(bus);
        ModEntities.ENTITIES.register(bus);
        ModPaintingTypes.PAINTING_TYPES.register(bus);
        ModFeatures.FEATURES.register(bus);
        ModStructureFeatures.STRUCTURE_FEATURES.register(bus);
        ModEnchantments.ENCHANTMENTS.register(bus);

        new RecipeManager().init();
        //Registers custom loot functions
        new ModLootFunctionManager();


        // Event Handlers
        MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
        MinecraftForge.EVENT_BUS.register(new LivingEventHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
        MinecraftForge.EVENT_BUS.register(new WorldEventHandler());
        MinecraftForge.EVENT_BUS.register(new TickHandler());
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        // Alters Vanilla
        VanillaManager.modifyVanilla();

        proxy.preInit();
    }

    public void onConstruct(FMLConstructModEvent event) {
        SevenDaysToMine.proxy.onConstruct(event);
    }

    public void setup(final FMLCommonSetupEvent event) {

        event.enqueueWork(() -> {
            ModGameRules.register();
            ModStructureFeatures.setupStructures();
            ModConfiguredStructures.registerConfiguredStructures();
        });

        ModTriggers.register();
        PacketManager.setup();
        ExtendedInventoryProvider.register();
        ExtendedPlayerProvider.register();
        ChunkDataProvider.register();


        ModBlocks.OAK_FRAME.get().setItems(new ItemStack[]{new ItemStack(ModItems.PLANK_WOOD.get(), 6)});
        ModBlocks.BIRCH_FRAME.get().setItems(new ItemStack[]{new ItemStack(ModItems.PLANK_WOOD.get(), 6)});
        ModBlocks.SPRUCE_FRAME.get().setItems(new ItemStack[]{new ItemStack(ModItems.PLANK_WOOD.get(), 6)});
        ModBlocks.JUNGLE_FRAME.get().setItems(new ItemStack[]{new ItemStack(ModItems.PLANK_WOOD.get(), 6)});
        ModBlocks.ACACIA_FRAME.get().setItems(new ItemStack[]{new ItemStack(ModItems.PLANK_WOOD.get(), 6)});
        ModBlocks.DARK_OAK_FRAME.get().setItems(new ItemStack[]{new ItemStack(ModItems.PLANK_WOOD.get(), 6)});
        ModBlocks.WARPED_FRAME.get().setItems(new ItemStack[]{new ItemStack(ModItems.PLANK_WOOD.get(), 6)});
        ModBlocks.CRIMSON_FRAME.get().setItems(new ItemStack[]{new ItemStack(ModItems.PLANK_WOOD.get(), 6)});

        ModBlocks.OAK_PLANKS_REINFORCED.get().setItems(new ItemStack[]{new ItemStack(ModItems.PLANK_WOOD.get(), 6)});
        ModBlocks.BIRCH_PLANKS_REINFORCED.get().setItems(new ItemStack[]{new ItemStack(ModItems.PLANK_WOOD.get(), 6)});
        ModBlocks.SPRUCE_PLANKS_REINFORCED.get().setItems(new ItemStack[]{new ItemStack(ModItems.PLANK_WOOD.get(), 6)});
        ModBlocks.JUNGLE_PLANKS_REINFORCED.get().setItems(new ItemStack[]{new ItemStack(ModItems.PLANK_WOOD.get(), 6)});
        ModBlocks.ACACIA_PLANKS_REINFORCED.get().setItems(new ItemStack[]{new ItemStack(ModItems.PLANK_WOOD.get(), 6)});
        ModBlocks.DARK_OAK_PLANKS_REINFORCED.get().setItems(new ItemStack[]{new ItemStack(ModItems.PLANK_WOOD.get(), 6)});
        ModBlocks.CRIMSON_PLANKS_REINFORCED.get().setItems(new ItemStack[]{new ItemStack(ModItems.PLANK_WOOD.get(), 6)});
        ModBlocks.WARPED_PLANKS_REINFORCED.get().setItems(new ItemStack[]{new ItemStack(ModItems.PLANK_WOOD.get(), 6)});


        ModBlocks.REBAR_FRAME.get().setItems(new ItemStack[]{new ItemStack(ModItems.PLANK_WOOD.get(), 6)});
        ModBlocks.BURNT_FRAME.get().setItems(new ItemStack[]{new ItemStack(ModItems.PLANK_WOOD.get(), 3)});
        ModBlocks.BURNT_PLANKS.get().setItems(new ItemStack[]{new ItemStack(ModItems.PLANK_WOOD.get(), 6)});


        proxy.init();

        proxy.postInit();
        // Loads repairs
        RepairManager.repairsInit();
        CityType.init();
        CityBuildings.init();
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }


    @SubscribeEvent
    public void onServerStarted(FMLServerStartedEvent event) {

        for (Entry<ResourceLocation, BookData> entry : BookDataManager.instance.getBooks().entrySet()) {
            BookData data = entry.getValue();
            for (Page page : data.getPages()) {
                for (CraftingMatrix matrix : page.crafting) {
                    matrix.loadRecipe(event.getServer());
                }
            }
        }
    }

    @SubscribeEvent
    public void onRegisterCommandEvent(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> commandDispatcher = event.getDispatcher();

        CommandAirdrop.register(commandDispatcher);
        CommandSetQuality.register(commandDispatcher);
        CommandCure.register(commandDispatcher);
        CommandHorde.register(commandDispatcher);
        CommandHydrate.register(commandDispatcher);
        CommandSetBreakData.register(commandDispatcher);
        CommandPlacePrefab.register(commandDispatcher);
        CommandGiveNote.register(commandDispatcher);
    }

}
