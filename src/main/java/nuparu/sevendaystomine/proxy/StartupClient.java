package nuparu.sevendaystomine.proxy;

import net.minecraft.block.WoodType;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.animation.AnimationModel;
import nuparu.sevendaystomine.client.animation.AnimationModelRenderers;
import nuparu.sevendaystomine.client.animation.Animations;
import nuparu.sevendaystomine.client.gui.inventory.*;
import nuparu.sevendaystomine.client.renderer.entity.*;
import nuparu.sevendaystomine.client.renderer.tileentity.*;
import nuparu.sevendaystomine.entity.ZombieWolfEntity;
import nuparu.sevendaystomine.init.*;

public class StartupClient {


    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        ScreenManager.register(ModContainers.FORGE.get(), GuiForge::new);
        ScreenManager.register(ModContainers.SMALL.get(), GuiSmallContainer::new);
        ScreenManager.register(ModContainers.TINY.get(), GuiTinyContainer::new);
        ScreenManager.register(ModContainers.CHEMISTRY_STATION.get(), GuiChemistryStation::new);
        ScreenManager.register(ModContainers.SEPARATOR.get(), GuiSeparator::new);
        ScreenManager.register(ModContainers.BATTERY_STATION.get(), GuiBatteryStation::new);
        ScreenManager.register(ModContainers.COMBUSTION_GENERATOR.get(), GuiCombustionGenerator::new);
        ScreenManager.register(ModContainers.GAS_GENERATOR.get(), GuiGasGenerator::new);
        ScreenManager.register(ModContainers.FLAMETHROWER.get(), GuiFlamethrower::new);
        ScreenManager.register(ModContainers.SCREEN_PROJECTOR.get(), GuiScreenProjector::new);
        ScreenManager.register(ModContainers.WORKBENCH.get(), GuiWorkbench::new);
        ScreenManager.register(ModContainers.COMPUTER.get(), GuiComputer::new);
        ScreenManager.register(ModContainers.MONITOR.get(), GuiMonitor::new);
        ScreenManager.register(ModContainers.TURRET_ADVANCED.get(), GuiTurretAdvanced::new);


        RenderTypeLookup.setRenderLayer(ModBlocks.BANEBERRY_PLANT.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.BLUEBERRY_PLANT.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.BEAKER.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.GARBAGE.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.TORCH_LIT.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.TORCH_LIT_WALL.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.TORCH_UNLIT.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.TORCH_UNLIT_WALL.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.CHEMISTRY_STATION.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.MICROWAVE.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.SCREEN_PROJECTOR.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.TRAFFIC_LIGHT.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.TRAFFIC_LIGHT_PEDESTRIAN.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.CORN_PLANT.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.COFFEE_PLANT.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.GOLDENROD.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.LOCKED_DOOR.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.BUSH.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.METAL_LADDER.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.SKELETON.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.SKELETON_SITTING.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.SEPARATOR.get(), RenderType.translucent());

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.REANIMATED_CORPSE.get(), ReanimatedCorpseRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.LOOTABLE_CORPSE.get(), LootableCorpseRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.PLAGUED_NURSE.get(), PlaguedNurseRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.FRONZE_LUMBERJACK.get(), FrozenLumberjackRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.FRIGID_HUNTER.get(), FrigidHunterRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.FROSTBITTEN_WORKER.get(), FrostbittenWorkerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.MINER_ZOMBIE.get(), MinerZombieRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.SPIDER_ZOMBIE.get(), SpiderZombieRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.CRAWLER_ZOMBIE.get(), CrawlerZombieRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BURNT_ZOMBIE.get(), BurntZombieRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BLOATED_ZOMBIE.get(), BloatedZombieRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.SOUL_BURNT_ZOMBIE.get(), SoulBurntZombieRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.SOLDIER_ZOMBIE.get(), ZombieSoldierRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.INFECTED_SURVIVOR.get(), InfectedSurvivorRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.FERAL_ZOMBIE.get(), FeralZombieRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ZOMBIE_POLICEMAN.get(), ZombiePolicemanRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ZOMBIE_WOLF.get(), ZombieWolfRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ZOMBIE_PIG.get(), ZombiePigRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.SURVIVOR.get(), SurvivorRenderer::new);


        ClientRegistry.bindTileEntityRenderer(ModTileEntities.SOLAR_PANEL.get(), TileEntitySolarPanelRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.WIND_TURBINE.get(), TileEntityWindTurbineRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.AIRPLANE_ROTOR.get(), TileEntityAirplaneEngineRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.CAMERA.get(), TileEntityCameraRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.GLOBE.get(), TileEntityGlobeRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.TURRET_BASE.get(), TileEntityTurretBaseRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.TURRET_ADVANCED.get(), TileEntityTurretAdvancedRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.CAR_MASTER.get(), TileEntityCarMasterRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.SLEEPING_BAG.get(), TileEntitySleepingBagRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.CALENDAR.get(), TileEntityCalendarRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.STREET_SIGN.get(), TileEntityStreetSignRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.BIG_SIGN_MASTER.get(), TileEntityBigSignRenderer::new);


        ItemModelsProperties.register(ModItems.CRUDE_BOW.get(),new ResourceLocation("pull"), (stack, world, entity) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
            }
        });

        ItemModelsProperties.register(ModItems.CRUDE_BOW.get(),new ResourceLocation("pulling"), (stack, world, entity) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

        ItemModelsProperties.register(ModItems.COMPOUND_BOW.get(),new ResourceLocation("pull"), (stack, world, entity) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
            }
        });

        ItemModelsProperties.register(ModItems.COMPOUND_BOW.get(),new ResourceLocation("pulling"), (stack, world, entity) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

        event.enqueueWork(() -> {
            Atlases.addWoodType(SevenDaysToMine.STREET_WOOD_TYPE);
        });
    }
}
