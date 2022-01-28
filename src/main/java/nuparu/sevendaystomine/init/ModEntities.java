package nuparu.sevendaystomine.init;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.entity.*;
import nuparu.sevendaystomine.entity.human.SurvivorEntity;

public class ModEntities {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES,
			SevenDaysToMine.MODID);
	
	public static final  EntityType<ReanimatedCorpseEntity> REANIMATED_CORPSE_RAW = EntityType.Builder
			.of((EntityType.IFactory<ReanimatedCorpseEntity>) ReanimatedCorpseEntity::new,
					EntityClassification.MONSTER)
			.sized(0.6F, 1.95F)
			.build(new ResourceLocation(SevenDaysToMine.MODID, "reanimated_corpse").toString());
	
	public static final  EntityType<PlaguedNurseEntity> PLAGUED_NURSE_RAW = EntityType.Builder
			.of((EntityType.IFactory<PlaguedNurseEntity>) PlaguedNurseEntity::new,
					EntityClassification.MONSTER)
			.sized(0.6F, 1.95F)
			.build(new ResourceLocation(SevenDaysToMine.MODID, "plagued_nurse").toString());
	
	public static final  EntityType<FrozenLumberjackEntity> FROZEN_LUMBERJACK_RAW = EntityType.Builder
			.of((EntityType.IFactory<FrozenLumberjackEntity>) FrozenLumberjackEntity::new,
					EntityClassification.MONSTER)
			.sized(0.6F, 1.95F)
			.build(new ResourceLocation(SevenDaysToMine.MODID, "frozen_lumberjack").toString());
	
	public static final  EntityType<FrigidHunterEntity> FRIGID_HUNTER_RAW = EntityType.Builder
			.of((EntityType.IFactory<FrigidHunterEntity>) FrigidHunterEntity::new,
					EntityClassification.MONSTER)
			.sized(0.6F, 1.95F)
			.build(new ResourceLocation(SevenDaysToMine.MODID, "frigid_hunter").toString());
	
	public static final  EntityType<FrostbittenWorkerEntity> FROSTBITTEN_WORKER_RAW = EntityType.Builder
			.of((EntityType.IFactory<FrostbittenWorkerEntity>) FrostbittenWorkerEntity::new,
					EntityClassification.MONSTER)
			.sized(0.6F, 1.95F)
			.build(new ResourceLocation(SevenDaysToMine.MODID, "frostbitten_worker").toString());
	
	public static final  EntityType<MinerZombieEntity> MINER_ZOMBIE_RAW = EntityType.Builder
			.of((EntityType.IFactory<MinerZombieEntity>) MinerZombieEntity::new,
					EntityClassification.MONSTER)
			.sized(0.6F, 1.95F)
			.build(new ResourceLocation(SevenDaysToMine.MODID, "miner_zombie").toString());
	
	public static final  EntityType<SpiderZombieEntity> SPIDER_ZOMBIE_RAW = EntityType.Builder
			.of((EntityType.IFactory<SpiderZombieEntity>) SpiderZombieEntity::new,
					EntityClassification.MONSTER)
			.sized(0.6F, 1.5F)
			.build(new ResourceLocation(SevenDaysToMine.MODID, "spider_zombie").toString());
	
	public static final  EntityType<CrawlerZombieEntity> CRAWLER_ZOMBIE_RAW = EntityType.Builder
			.of((EntityType.IFactory<CrawlerZombieEntity>) CrawlerZombieEntity::new,
					EntityClassification.MONSTER)
			.sized(0.9f, 0.33F)
			.build(new ResourceLocation(SevenDaysToMine.MODID, "crawler_zombie").toString());
	
	public static final  EntityType<BurntZombieEntity> BURNT_ZOMBIE_RAW = EntityType.Builder
			.of((EntityType.IFactory<BurntZombieEntity>) BurntZombieEntity::new,
					EntityClassification.MONSTER)
			.sized(0.6F, 1.95F).fireImmune()
			.build(new ResourceLocation(SevenDaysToMine.MODID, "burnt_zombie").toString());
	

	public static final  EntityType<BloatedZombieEntity> BLOATED_ZOMBIE_RAW = EntityType.Builder
			.of((EntityType.IFactory<BloatedZombieEntity>) BloatedZombieEntity::new,
					EntityClassification.MONSTER)
			.sized(0.6F, 1.95F).fireImmune()
			.build(new ResourceLocation(SevenDaysToMine.MODID, "bloated_zombie").toString());
	
	public static final  EntityType<SoulBurntZombieEntity> SOUL_BURNT_ZOMBIE_RAW = EntityType.Builder
			.of((EntityType.IFactory<SoulBurntZombieEntity>) SoulBurntZombieEntity::new,
					EntityClassification.MONSTER)
			.sized(0.6F, 1.95F).fireImmune()
			.build(new ResourceLocation(SevenDaysToMine.MODID, "soul_burnt_zombie").toString());

	public static final  EntityType<ZombieSoldierEntity> SOLDIER_ZOMBIE_RAW = EntityType.Builder
			.of((EntityType.IFactory<ZombieSoldierEntity>) ZombieSoldierEntity::new,
					EntityClassification.MONSTER)
			.sized(0.6F, 1.95F)
			.build(new ResourceLocation(SevenDaysToMine.MODID, "soldier_zombie").toString());
	
	public static final  EntityType<InfectedSurvivorEntity> INFECTED_SURVIVOR_RAW = EntityType.Builder
			.of((EntityType.IFactory<InfectedSurvivorEntity>) InfectedSurvivorEntity::new,
					EntityClassification.MONSTER)
			.sized(0.6F, 1.95F)
			.build(new ResourceLocation(SevenDaysToMine.MODID, "infected_survivor").toString());
	
	public static final  EntityType<FeralZombieEntity> FERAL_ZOMBIE_RAW = EntityType.Builder
			.of((EntityType.IFactory<FeralZombieEntity>) FeralZombieEntity::new,
					EntityClassification.MONSTER)
			.sized(0.6F, 1.95F)
			.build(new ResourceLocation(SevenDaysToMine.MODID, "feral_zombie_entity").toString());
	
	public static final  EntityType<ZombiePolicemanEntity> ZOMBIE_POLICEMAN_RAW = EntityType.Builder
			.of((EntityType.IFactory<ZombiePolicemanEntity>) ZombiePolicemanEntity::new,
					EntityClassification.MONSTER)
			.sized(0.6F, 1.95F)
			.build(new ResourceLocation(SevenDaysToMine.MODID, "zombie_policeman").toString());

	public static final  EntityType<ZombieWolfEntity> ZOMBIE_WOLF_RAW = EntityType.Builder
			.of((EntityType.IFactory<ZombieWolfEntity>) ZombieWolfEntity::new,
					EntityClassification.MONSTER)
			.sized(0.6F, 0.85F)
			.build(new ResourceLocation(SevenDaysToMine.MODID, "zombie_wolf").toString());

	public static final  EntityType<ZombiePigEntity> ZOMBIE_PIG_RAW = EntityType.Builder
			.of((EntityType.IFactory<ZombiePigEntity>) ZombiePigEntity::new,
					EntityClassification.MONSTER)
			.sized(0.6F, 0.85F)
			.build(new ResourceLocation(SevenDaysToMine.MODID, "zombie_pig").toString());

	public static final  EntityType<ShotEntity> SHOT_RAW = EntityType.Builder
			.of((EntityType.IFactory<ShotEntity>) ShotEntity::new,
					EntityClassification.MISC)
			.sized(0.5F, 0.5F)
			.build(new ResourceLocation(SevenDaysToMine.MODID, "shot").toString());

	public static final  EntityType<RocketEntity> ROCKET_RAW = EntityType.Builder
			.of((EntityType.IFactory<RocketEntity>) RocketEntity::new,
					EntityClassification.MISC)
			.sized(0.5F, 0.5F)
			.build(new ResourceLocation(SevenDaysToMine.MODID, "rocket").toString());

	public static final  EntityType<FlameEntity> FLAME_RAW = EntityType.Builder
			.of((EntityType.IFactory<FlameEntity>) FlameEntity::new,
					EntityClassification.MISC)
			.sized(0.5F, 0.5F)
			.build(new ResourceLocation(SevenDaysToMine.MODID, "flame").toString());

	public static final  EntityType<SurvivorEntity> SURVIVOR_RAW = EntityType.Builder
			.of((EntityType.IFactory<SurvivorEntity>) SurvivorEntity::new,
					EntityClassification.CREATURE)
			.sized(0.6F, 1.95F)
			.build(new ResourceLocation(SevenDaysToMine.MODID, "survivor").toString());


	public static final  EntityType<FragmentationGrenadeEntity> FRAGMENTATION_GRENADE_RAW = EntityType.Builder
			.of((EntityType.IFactory<FragmentationGrenadeEntity>) FragmentationGrenadeEntity::new,
					EntityClassification.MISC)
			.sized(0.6F, 0.6F)
			.build(new ResourceLocation(SevenDaysToMine.MODID, "fragmentation_grenade").toString());

	public static final  EntityType<ChlorineGrenadeEntity> CHLORINE_GRENADE_RAW = EntityType.Builder
			.of((EntityType.IFactory<ChlorineGrenadeEntity>) ChlorineGrenadeEntity::new,
					EntityClassification.MISC)
			.sized(0.6F, 0.6F)
			.build(new ResourceLocation(SevenDaysToMine.MODID, "chlorine_grenade").toString());

	public static final  EntityType<MolotovEntity> MOLOTOV_RAW = EntityType.Builder
			.of((EntityType.IFactory<MolotovEntity>) MolotovEntity::new,
					EntityClassification.MISC)
			.sized(0.6F, 0.6F)
			.build(new ResourceLocation(SevenDaysToMine.MODID, "molotov").toString());

	public static final  EntityType<MountableBlockEntity> MOUNTABLE_BLOCK_RAW = EntityType.Builder
			.of((EntityType.IFactory<MountableBlockEntity>) MountableBlockEntity::new,
					EntityClassification.MISC)
			.sized(0.01F, 0.01F)
			.build(new ResourceLocation(SevenDaysToMine.MODID, "mountable_block").toString());

	public static final  EntityType<FlareEntity> FLARE_RAW = EntityType.Builder
			.of((EntityType.IFactory<FlareEntity>) FlareEntity::new,
					EntityClassification.MISC)
			.sized(0.6F, 0.6F)
			.build(new ResourceLocation(SevenDaysToMine.MODID, "flare").toString());


	public static final RegistryObject<EntityType<ReanimatedCorpseEntity>> REANIMATED_CORPSE = ENTITIES.register(
			"reanimated_corpse",
			() -> REANIMATED_CORPSE_RAW);
	
	public static final RegistryObject<EntityType<PlaguedNurseEntity>> PLAGUED_NURSE = ENTITIES.register(
			"plagued_nurse",
			() -> PLAGUED_NURSE_RAW);
	
	public static final RegistryObject<EntityType<FrozenLumberjackEntity>> FROZEN_LUMBERJACK = ENTITIES.register(
			"frozen_lumberjack",
			() -> FROZEN_LUMBERJACK_RAW);
	
	public static final RegistryObject<EntityType<FrigidHunterEntity>> FRIGID_HUNTER = ENTITIES.register(
			"frigid_hunter",
			() -> FRIGID_HUNTER_RAW);
	
	public static final RegistryObject<EntityType<FrostbittenWorkerEntity>> FROSTBITTEN_WORKER = ENTITIES.register(
			"frostbitten_worker",
			() -> FROSTBITTEN_WORKER_RAW);
	
	public static final RegistryObject<EntityType<MinerZombieEntity>> MINER_ZOMBIE = ENTITIES.register(
			"miner_zombie",
			() -> MINER_ZOMBIE_RAW);
	
	public static final RegistryObject<EntityType<SpiderZombieEntity>> SPIDER_ZOMBIE = ENTITIES.register(
			"spider_zombie",
			() -> SPIDER_ZOMBIE_RAW);
	
	public static final RegistryObject<EntityType<CrawlerZombieEntity>> CRAWLER_ZOMBIE = ENTITIES.register(
			"crawler_zombie",
			() -> CRAWLER_ZOMBIE_RAW);
	
	public static final RegistryObject<EntityType<BurntZombieEntity>> BURNT_ZOMBIE = ENTITIES.register(
			"burnt_zombie",
			() -> BURNT_ZOMBIE_RAW);
	
	public static final RegistryObject<EntityType<BloatedZombieEntity>> BLOATED_ZOMBIE = ENTITIES.register(
			"bloated_zombie",
			() -> BLOATED_ZOMBIE_RAW);
	
	public static final RegistryObject<EntityType<SoulBurntZombieEntity>> SOUL_BURNT_ZOMBIE = ENTITIES.register(
			"soul_burnt_zombie",
			() -> SOUL_BURNT_ZOMBIE_RAW);
	
	public static final RegistryObject<EntityType<ZombieSoldierEntity>> SOLDIER_ZOMBIE = ENTITIES.register(
			"soldier_zombie",
			() -> SOLDIER_ZOMBIE_RAW);
	
	public static final RegistryObject<EntityType<InfectedSurvivorEntity>> INFECTED_SURVIVOR = ENTITIES.register(
			"infected_survivor",
			() -> INFECTED_SURVIVOR_RAW);
	
	public static final RegistryObject<EntityType<FeralZombieEntity>> FERAL_ZOMBIE = ENTITIES.register(
			"feral_zombie",
			() -> FERAL_ZOMBIE_RAW);
	
	public static final RegistryObject<EntityType<ZombiePolicemanEntity>> ZOMBIE_POLICEMAN = ENTITIES.register(
			"zombie_policeman",
			() -> ZOMBIE_POLICEMAN_RAW);

	public static final RegistryObject<EntityType<ZombieWolfEntity>> ZOMBIE_WOLF = ENTITIES.register(
			"zombie_wolf",
			() -> ZOMBIE_WOLF_RAW);

	public static final RegistryObject<EntityType<ZombiePigEntity>> ZOMBIE_PIG = ENTITIES.register(
			"zombie_pig",
			() -> ZOMBIE_PIG_RAW);

	public static final RegistryObject<EntityType<ShotEntity>> SHOT = ENTITIES.register(
			"shot",
			() -> SHOT_RAW);

	public static final RegistryObject<EntityType<RocketEntity>> ROCKET = ENTITIES.register(
			"rocket",
			() -> ROCKET_RAW);

	public static final RegistryObject<EntityType<FlameEntity>> FLAME = ENTITIES.register(
			"flame",
			() -> FLAME_RAW);

	public static final RegistryObject<EntityType<LootableCorpseEntity>> LOOTABLE_CORPSE = ENTITIES.register(
			"lootable_corpse",
			() -> EntityType.Builder
			.of((EntityType.IFactory<LootableCorpseEntity>) LootableCorpseEntity::new,
					EntityClassification.MISC)
			.sized(1.5f, 0.45f).fireImmune().setTrackingRange(128).setShouldReceiveVelocityUpdates(true).setUpdateInterval(2)
			.build(new ResourceLocation(SevenDaysToMine.MODID, "lootable_corpse").toString()));

	public static final RegistryObject<EntityType<MinibikeEntity>> MINIBIKE = ENTITIES.register(
			"minibike",
			() -> EntityType.Builder
					.of((EntityType.IFactory<MinibikeEntity>) MinibikeEntity::new,
							EntityClassification.MISC)
					.sized(0.8F, 0.75F).setTrackingRange(128).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).updateInterval(1)
					.build(new ResourceLocation(SevenDaysToMine.MODID, "minibike").toString()));

	public static final RegistryObject<EntityType<CarEntity>> CAR = ENTITIES.register(
			"car",
			() -> EntityType.Builder
					.of((EntityType.IFactory<CarEntity>) CarEntity::new,
							EntityClassification.MISC)
					.sized(2F, 1.5F).setTrackingRange(128).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).updateInterval(1)
					.build(new ResourceLocation(SevenDaysToMine.MODID, "car").toString()));

	public static final RegistryObject<EntityType<AirdropEntity>> AIRDROP = ENTITIES.register(
			"airdrop",
			() -> EntityType.Builder
					.of((EntityType.IFactory<AirdropEntity>) AirdropEntity::new,
							EntityClassification.MISC)
					.sized(1f, 1f).fireImmune().setTrackingRange(256).clientTrackingRange(128)
					.build(new ResourceLocation(SevenDaysToMine.MODID, "airdorp").toString()));

	public static final RegistryObject<EntityType<SurvivorEntity>> SURVIVOR = ENTITIES.register(
			"survivor",
			() -> SURVIVOR_RAW);

	public static final RegistryObject<EntityType<FragmentationGrenadeEntity>> FRAGMENTATION_GRENADE = ENTITIES.register(
			"fragmentation_grenade",
			() -> FRAGMENTATION_GRENADE_RAW);

	public static final RegistryObject<EntityType<MountableBlockEntity>> MOUNTABLE_BLOCK = ENTITIES.register(
			"mountable_block",
			() -> MOUNTABLE_BLOCK_RAW);

	public static final RegistryObject<EntityType<FlareEntity>> FLARE = ENTITIES.register(
			"flare",
			() -> FLARE_RAW);

	public static final RegistryObject<EntityType<ChlorineGrenadeEntity>> CHLORINE_GRENADE = ENTITIES.register(
			"chlorine_grenade",
			() -> CHLORINE_GRENADE_RAW);

	public static final RegistryObject<EntityType<MolotovEntity>> MOLOTOV = ENTITIES.register(
			"molotov",
			() -> MOLOTOV_RAW);
}
