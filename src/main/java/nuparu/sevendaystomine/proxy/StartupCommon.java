package nuparu.sevendaystomine.proxy;

import net.minecraft.block.WoodType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.entity.*;
import nuparu.sevendaystomine.entity.human.SurvivorEntity;
import nuparu.sevendaystomine.init.ModBiomes;
import nuparu.sevendaystomine.init.ModEntities;

public class StartupCommon {
	  @SubscribeEvent
	  public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
		  GlobalEntityTypeAttributes.put(ModEntities.REANIMATED_CORPSE.get(), ZombieBaseEntity.createAttributes());
		  GlobalEntityTypeAttributes.put(ModEntities.PLAGUED_NURSE.get(), ZombieBaseEntity.createAttributes());
		  GlobalEntityTypeAttributes.put(ModEntities.FROZEN_LUMBERJACK.get(), FrozenLumberjackEntity.createAttributes());
		  GlobalEntityTypeAttributes.put(ModEntities.FRIGID_HUNTER.get(), FrigidHunterEntity.createAttributes());
		  GlobalEntityTypeAttributes.put(ModEntities.FROSTBITTEN_WORKER.get(), FrostbittenWorkerEntity.createAttributes());
		  GlobalEntityTypeAttributes.put(ModEntities.MINER_ZOMBIE.get(), MinerZombieEntity.createAttributes());
		  GlobalEntityTypeAttributes.put(ModEntities.SPIDER_ZOMBIE.get(), SpiderZombieEntity.createAttributes());
		  GlobalEntityTypeAttributes.put(ModEntities.CRAWLER_ZOMBIE.get(), CrawlerZombieEntity.createAttributes());
		  GlobalEntityTypeAttributes.put(ModEntities.BURNT_ZOMBIE.get(), BurntZombieEntity.createAttributes());
		  GlobalEntityTypeAttributes.put(ModEntities.BLOATED_ZOMBIE.get(), BloatedZombieEntity.createAttributes());
		  GlobalEntityTypeAttributes.put(ModEntities.SOUL_BURNT_ZOMBIE.get(), SoulBurntZombieEntity.createAttributes());
		  GlobalEntityTypeAttributes.put(ModEntities.SOLDIER_ZOMBIE.get(), ZombieSoldierEntity.createAttributes());
		  GlobalEntityTypeAttributes.put(ModEntities.INFECTED_SURVIVOR.get(), ZombieBaseEntity.createAttributes());
		  GlobalEntityTypeAttributes.put(ModEntities.FERAL_ZOMBIE.get(), FeralZombieEntity.createAttributes());
		  GlobalEntityTypeAttributes.put(ModEntities.ZOMBIE_POLICEMAN.get(), ZombiePolicemanEntity.createAttributes());
		  GlobalEntityTypeAttributes.put(ModEntities.ZOMBIE_WOLF.get(), ZombieWolfEntity.createAttributes());
		  GlobalEntityTypeAttributes.put(ModEntities.ZOMBIE_PIG.get(), ZombiePigEntity.createAttributes());
		  GlobalEntityTypeAttributes.put(ModEntities.SURVIVOR.get(), SurvivorEntity.createAttributes());
		  GlobalEntityTypeAttributes.put(ModEntities.MINIBIKE.get(), MinibikeEntity.createAttributes());
		  GlobalEntityTypeAttributes.put(ModEntities.CAR.get(), CarEntity.createAttributes());


		  event.enqueueWork(() -> {
		  	WoodType.register(SevenDaysToMine.STREET_WOOD_TYPE);
			  EntitySpawnPlacementRegistry.register(ModEntities.REANIMATED_CORPSE.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ZombieBaseEntity::checkMonsterSpawnRules);
			  EntitySpawnPlacementRegistry.register(ModEntities.PLAGUED_NURSE.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ZombieBaseEntity::checkMonsterSpawnRules);
			  EntitySpawnPlacementRegistry.register(ModEntities.FROZEN_LUMBERJACK.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ZombieBaseEntity::checkMonsterSpawnRules);
			  EntitySpawnPlacementRegistry.register(ModEntities.FRIGID_HUNTER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ZombieBaseEntity::checkMonsterSpawnRules);
			  EntitySpawnPlacementRegistry.register(ModEntities.FROSTBITTEN_WORKER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ZombieBaseEntity::checkMonsterSpawnRules);
			  EntitySpawnPlacementRegistry.register(ModEntities.MINER_ZOMBIE.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ZombieBaseEntity::checkMonsterSpawnRules);
			  EntitySpawnPlacementRegistry.register(ModEntities.SPIDER_ZOMBIE.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ZombieBaseEntity::checkMonsterSpawnRules);
			  EntitySpawnPlacementRegistry.register(ModEntities.CRAWLER_ZOMBIE.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ZombieBaseEntity::checkMonsterSpawnRules);
			  EntitySpawnPlacementRegistry.register(ModEntities.BURNT_ZOMBIE.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ZombieBaseEntity::checkMonsterSpawnRules);
			  EntitySpawnPlacementRegistry.register(ModEntities.BLOATED_ZOMBIE.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ZombieBaseEntity::checkMonsterSpawnRules);
			  EntitySpawnPlacementRegistry.register(ModEntities.SOUL_BURNT_ZOMBIE.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BurntZombieEntity::checkMonsterSpawnRules);
			  EntitySpawnPlacementRegistry.register(ModEntities.INFECTED_SURVIVOR.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ZombieBaseEntity::checkMonsterSpawnRules);
			  EntitySpawnPlacementRegistry.register(ModEntities.SOLDIER_ZOMBIE.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ZombieBaseEntity::checkMonsterSpawnRules);
			  EntitySpawnPlacementRegistry.register(ModEntities.ZOMBIE_WOLF.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ZombieBaseEntity::checkMonsterSpawnRules);
			  EntitySpawnPlacementRegistry.register(ModEntities.ZOMBIE_PIG.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ZombieBaseEntity::checkMonsterSpawnRules);
			  EntitySpawnPlacementRegistry.register(ModEntities.BURNT_ZOMBIE.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BurntZombieEntity::checkMonsterSpawnRules);
});

		  BiomeManager.addBiome(BiomeManager.BiomeType.WARM,new BiomeManager.BiomeEntry(ModBiomes.createKey(ModBiomes.WASTELAND_PLAINS),3));
		  BiomeManager.addBiome(BiomeManager.BiomeType.WARM,new BiomeManager.BiomeEntry(ModBiomes.createKey(ModBiomes.WASTELAND_FOREST),6));
	  }

}
