package nuparu.sevendaystomine;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.block.WoodType;
import net.minecraft.command.CommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import nuparu.sevendaystomine.advancements.ModTriggers;
import nuparu.sevendaystomine.capability.CapabilityHandler;
import nuparu.sevendaystomine.capability.ChunkDataProvider;
import nuparu.sevendaystomine.capability.ExtendedInventoryProvider;
import nuparu.sevendaystomine.capability.ExtendedPlayerProvider;
import nuparu.sevendaystomine.command.*;
import nuparu.sevendaystomine.config.ConfigHelper;
import nuparu.sevendaystomine.crafting.RecipeManager;
import nuparu.sevendaystomine.events.*;
import nuparu.sevendaystomine.init.*;
import nuparu.sevendaystomine.loot.function.ModLootFunctionManager;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.potions.Potions;
import nuparu.sevendaystomine.proxy.ClientProxy;
import nuparu.sevendaystomine.proxy.CommonProxy;
import nuparu.sevendaystomine.proxy.StartupClient;
import nuparu.sevendaystomine.proxy.StartupCommon;
import nuparu.sevendaystomine.util.VanillaManager;
import nuparu.sevendaystomine.world.gen.city.CityBuildings;
import nuparu.sevendaystomine.world.gen.city.CityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Mod(SevenDaysToMine.MODID)
public class SevenDaysToMine {
    public static final String MODID = "sevendaystomine";

    public static final Logger LOGGER = LogManager.getLogger();

    static final String CLIENT_PROXY_CLASS = "nuparu.sevendaystomine.proxy.ClientProxy";
    static final String SERVER_PROXY_CLASS = "nuparu.sevendaystomine.proxy.CommonProxy";
    public static SevenDaysToMine instance;
    public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new,
            () -> CommonProxy::new);


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

        ModAttributes.ATTRIBUTES.register(bus);
        ModSounds.SOUNDS.register(bus);
        ModBlocks.BLOCKS.register(bus);
        ModItems.ITEMS.register(bus);
        Potions.EFFECTS.register(bus);
        ModBiomes.BIOMES.register(bus);
        ModDataSerializers.SERIALIZERS.register(bus);
        ModRecipeSerializers.SERIALIZERS.register(bus);
        ModLootModifiers.LOOT_MODIFIERS.register(bus);

        ModFluids.FLUIDS.register(bus);
        //ModRecipes.RECIPES.register(bus);

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

        proxy.preInit();
    }

    public void onConstruct(FMLConstructModEvent event) {
        SevenDaysToMine.proxy.onConstruct(event);
    }

    public void setup(final FMLCommonSetupEvent event) {
        System.out.println("FMLCommonSetupEvent");
        event.enqueueWork(() -> {
            System.out.println("enqueueWork");
            ModGameRules.register();
            ModStructureFeatures.setupStructures();
            ModConfiguredStructures.registerConfiguredStructures();
            ModStructureProcessors.register();
            ModStructurePoolElements.register();
            ModLootModifiers.registerConditions();
            System.out.println("denqueueWork");
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
        CityType.init();
        CityBuildings.init();

        // Alters Vanilla
        VanillaManager.modifyVanilla();
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }


    public static List<String> recipesToRemove = new ArrayList<String>();

    static{
        recipesToRemove.add("minecraft:oak_planks");
        recipesToRemove.add("minecraft:birch_planks");
        recipesToRemove.add("minecraft:spruce_planks");
        recipesToRemove.add("minecraft:jungle_planks");
        recipesToRemove.add("minecraft:dark_oak_planks");
        recipesToRemove.add("minecraft:acacia_planks");
        recipesToRemove.add("minecraft:crimson_planks");
        recipesToRemove.add("minecraft:warped_planks");
        recipesToRemove.add("minecraft:furnace");
        recipesToRemove.add("minecraft:wooden_sword");
        recipesToRemove.add("minecraft:wooden_spade");
        recipesToRemove.add("minecraft:wooden_pickaxe");
        recipesToRemove.add("minecraft:wooden_axe");
        recipesToRemove.add("minecraft:wooden_hoe");
        recipesToRemove.add("minecraft:stone_sword");
        recipesToRemove.add("minecraft:stone_spade");
        recipesToRemove.add("minecraft:stone_pickaxe");
        recipesToRemove.add("minecraft:stone_axe");
        recipesToRemove.add("minecraft:stone_hoe");
        recipesToRemove.add("minecraft:iron_sword");
        recipesToRemove.add("minecraft:gold_sword");
        recipesToRemove.add("minecraft:diamond_sword");
        recipesToRemove.add("minecraft:diamond_shovel");
        recipesToRemove.add("minecraft:diamond_pickaxe");
        recipesToRemove.add("minecraft:diamond_axe");
        recipesToRemove.add("minecraft:diamond_hoe");
        recipesToRemove.add("minecraft:diamond_helmet");
        recipesToRemove.add("minecraft:diamond_chestplate");
        recipesToRemove.add("minecraft:diamond_leggings");
        recipesToRemove.add("minecraft:diamond_boots");
        recipesToRemove.add("minecraft:iron_ingot");
        recipesToRemove.add("minecraft:iron_ingot_from_blasting");
        recipesToRemove.add("minecraft:gold_ingot");
        recipesToRemove.add("minecraft:gold_ingot_from_blasting");
    }

    @SubscribeEvent
    public void onServerStarted(FMLServerStartedEvent event) {
            Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipes = event.getServer().getRecipeManager().recipes;
        Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> newRecipez = new HashMap<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>>();
            for (IRecipeType<?> key : recipes.keySet()) {
                Map<ResourceLocation, IRecipe<?>> original = recipes.get(key);
                HashMap<ResourceLocation, IRecipe<?>> newMap = new HashMap<ResourceLocation, IRecipe<?>>();
                if(original != null){
                    for(Entry<ResourceLocation, IRecipe<?>> recipe : original.entrySet()){
                        ResourceLocation location = recipe.getKey();
                        IRecipe<?> value = recipe.getValue();

                        if(!recipesToRemove.contains(location.toString())){
                            newMap.put(location,value);
                        }
                    }
                }
                newRecipez.put(key,newMap);
            }
        event.getServer().getRecipeManager().recipes = newRecipez;
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
