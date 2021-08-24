package nuparu.sevendaystomine.config;

import java.util.ArrayList;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import nuparu.sevendaystomine.client.gui.EnumHudPosition;

import java.util.Arrays;
import java.util.List;

public class CommonConfig {

	// Player
	public static BooleanValue renderPlayerInventory;
	public static BooleanValue allowPhotos;
	public static BooleanValue allowCameras;
	public static BooleanValue survivalGuide;
	public static BooleanValue immersiveBlockBreaking;
	public static DoubleValue immersiveBlockBreakingModifier;
	public static DoubleValue scrapCoefficient;
	public static BooleanValue recipeBooksRequired;
	public static BooleanValue disableBlockToIngot;
	public static BooleanValue disableVanillaBlocksAndItems;
	public static DoubleValue xpPerQuality;
	public static IntValue maxQuality;
	public static BooleanValue thirstSystem;
	public static BooleanValue staminaSystem;
	public static ForgeConfigSpec.EnumValue<EnumQualityState> qualitySystem;
	public static IntValue infectionChanceModifier;
	public static BooleanValue backpackSlot;
	public static BooleanValue fragileLegs;
	public static BooleanValue extraPoisonousBerries;
	public static BooleanValue disableFoodStacking;
	public static ConfigValue<List<? extends Integer>> infectionStagesDuration;

	// Mobs

	public static IntValue zombiesRunMode;
	public static BooleanValue zombiesBreakBlocks;
	public static BooleanValue bulletsBreakBlocks;
	public static BooleanValue zombiesAttackAnimals;
	public static IntValue bleedingChanceModifier;
	public static BooleanValue zombieCorpses;
	public static IntValue spawnWeightReanimatedCorpse;
	public static IntValue spawnWeightBloatedZombie;
	public static IntValue spawnWeightPlaguedNurse;
	public static IntValue spawnWeightZombieCrawler;
	public static IntValue spawnWeightSpiderZombie;
	public static IntValue spawnWeightFrozenLumberjack;
	public static IntValue spawnWeightFrigidHunter;
	public static IntValue spawnWeightFrostbittenWorker;
	public static IntValue spawnWeightZombieWolf;
	public static IntValue spawnWeightZombiePig;
	public static IntValue spawnWeightBurntZombie;
	public static IntValue spawnWeightZombieMiner;
	public static IntValue spawnWeightFeralZombie;
	public static IntValue spawnMinReanimatedCorpse;
	public static IntValue spawnMinBloatedZombie;
	public static IntValue spawnMinPlaguedNurse;
	public static IntValue spawnMinZombieCrawler;
	public static IntValue spawnMinSpiderZombie;
	public static IntValue spawnMinFrozenLumberjack;
	public static IntValue spawnMinFrigidHunter;
	public static IntValue spawnMinFrostbittenWorker;
	public static IntValue spawnMinZombieWolf;
	public static IntValue spawnMinZombiePig;
	public static IntValue spawnMinBurntZombie;
	public static IntValue spawnMinZombieMiner;
	public static IntValue spawnMinFeralZombie;
	public static IntValue spawnMaxReanimatedCorpse;
	public static IntValue spawnMaxBloatedZombie;
	public static IntValue spawnMaxPlaguedNurse;
	public static IntValue spawnMaxZombieCrawler;
	public static IntValue spawnMaxSpiderZombie;
	public static IntValue spawnMaxFrozenLumberjack;
	public static IntValue spawnMaxFrigidHunter;
	public static IntValue spawnMaxFrostbittenWorker;
	public static IntValue spawnMaxZombieWolf;
	public static IntValue spawnMaxZombiePig;
	public static IntValue spawnMaxBurntZombie;
	public static IntValue spawnMaxZombieMiner;
	public static IntValue spawnMaxFeralZombie;
	public static DoubleValue zombieKnockbackResistance;
	public static BooleanValue monstersAttackHumanNPCs;

	// World

	public static IntValue bloodmoonFrequency;
	public static IntValue bloodmoonHordeWaves;
	public static IntValue bloodmoonHordeZombiesPerWaveMin;
	public static IntValue bloodmoonHordeZombiesPerWaveMax;
	public static IntValue wolfHordeFrequency;
	public static IntValue wolfHordeWaves;
	public static IntValue wolfHordeZombiesPerWave;
	public static DoubleValue genericHordeChance;
	public static IntValue genericHordeWaves;
	public static IntValue genericHordeZombiesPerWaveMin;
	public static IntValue genericHordeZombiesPerWaveMax;
	public static IntValue corpseLifespan;
	public static IntValue torchBurnTime;
	public static BooleanValue torchRainExtinguish;
	public static IntValue airdropFrequency;
	public static BooleanValue removeVanillaZommbies;
	public static IntValue hordeMinDistance;
	public static IntValue hordeMaxDistance;
	public static IntValue hordeWaveDelay;
	public static IntValue damageDecayRate;

	// World gen

	public static IntValue maxCitySize;
	public static IntValue citySpacing;
	public static BooleanValue generateRoads;
	public static IntValue roadMinY;
	public static IntValue roadMaxY;
	public static BooleanValue structurePedestal;
	public static BooleanValue snowSandBuildingCover;
	public static BooleanValue sandRoadCover;
	public static BooleanValue smallStructuresOnly;
	public static DoubleValue scattereedStructureChanceModifier;
	public static IntValue minScatteredDistanceSq;
	public static IntValue minScatteredDistanceFromCities;
	public static IntValue copperOreGenerationRate;
	public static IntValue tinOreGenerationRate;
	public static IntValue zincOreGenerationRate;
	public static IntValue leadOreGenerationRate;
	public static IntValue potassiumOreGenerationRate;
	public static IntValue cinnabarOreGenerationRate;

	public static IntValue largeRockGenerationChance;
	public static IntValue largeRockGenerationRateMin;
	public static IntValue largeRockGenerationRateMax;

	public static IntValue smallRockGenerationChance;
	public static IntValue smallRockGenerationRateMin;
	public static IntValue smallRockGenerationRateMax;

	public static IntValue berryGenerationChance;
	public static IntValue berryGenerationRateMin;
	public static IntValue berryGenerationRateMax;

	public static IntValue stickGenerationChance;
	public static IntValue stickGenerationRateMin;
	public static IntValue stickGenerationRateMax;

	public static IntValue deadBushGenerationRateMin;
	public static IntValue deadBushGenerationRateMax;
	public static IntValue cinderBlockGenerationRateMin;
	public static IntValue cinderBlockGenerationRateMax;

	public static IntValue goldenrodGenerationRateMin;
	public static IntValue goldenrodGenerationRateMax;
	public static IntValue cornGenerationRateMin;
	public static IntValue cornGenerationRateMax;
	public static IntValue burntForestWeight;
	public static IntValue burntJungleWeight;
	public static IntValue deadForestWeight;
	public static IntValue wastelandWeight;

	public static void init(ForgeConfigSpec.Builder server) {

		// Players

		renderPlayerInventory = server.comment(
				"Cotrols rendering of player's items (weapons, tools). If false on a server, no one will be able to see the items regardless of their settings")
				.define("player.renderPlayerInventory", true);
		allowPhotos = server.comment("Can players take photos with the Analog Camera item?")
				.define("player.allowPhotos", true);
		allowCameras = server.comment("Can players use cameras (the block)?").define("player.allowCameras", true);
		survivalGuide = server.comment("Should players receive the Survival Guide on their first spawn?")
				.define("player.survivalGuide", true);
		immersiveBlockBreaking = server.comment("Makes block breaking slower and more immersive")
				.define("player.immersiveBlockBreaking", true);
		immersiveBlockBreakingModifier = server.comment(
				"Affects the block breaking speed when immersiveBlockBreaking is true. The higher the number, the slower the breaking is")
				.defineInRange("player.immersiveBlockBreakingModifier", 32d, 0, Double.MAX_VALUE);
		scrapCoefficient = server.comment("Controls how much scrap you get from scrapping in inventory")
				.defineInRange("player.scrapCoefficient", 0.5, 0, 1);
		recipeBooksRequired = server.comment("Do recipes have to be unlocked using the recipe books?")
				.define("player.recipeBooksRequired", true);
		disableBlockToIngot = server
				.comment("Disables crafting of ingots from gold/iron blocks and nuggets from gold/iron ingots")
				.define("player.disableBlockToIngot", true);
		disableVanillaBlocksAndItems = server
				.comment("Makes certain Vanilla blocks and items (tools, armors, furnace...) uncraftable")
				.define("player.disableVanillaBlocksAndItems", true);
		xpPerQuality = server.comment("How many XP per 1 Quality point").defineInRange("player.xpPerQuality", 5, 0,
				Double.MAX_VALUE);
		maxQuality = server.comment("Maximal possible Quality").defineInRange("player.maxQuality", 600, 0,
				Integer.MAX_VALUE);
		thirstSystem = server.comment("Should use the thirst system?").define("player.thirstSystem", true);
		staminaSystem = server.comment("Should use the stamina system?").define("player.staminaSystem", true);
		qualitySystem = server.comment("Should use the quality system?").defineEnum("player.qualitySystem" , EnumQualityState.ALL,EnumQualityState.values());
		infectionChanceModifier = server.comment("The larger the value, the less likely a player is to get infected")
				.defineInRange("player.infectionChanceModifier", 6, 1, Integer.MAX_VALUE);
		backpackSlot = server.comment(
				"Should add the backpack slot to the player inventory (does not affect the texture, if you turn this off, you should used a resourcepack where the slot is removed from the texture)")
				.define("player.backpackSlot", true);
		fragileLegs = server.comment("Can a player break their legs on fall?").define("player.fragileLegs", true);
		extraPoisonousBerries = server.comment("Does the baneberry give the poison status effect?")
				.define("player.extraPoisonousBerries", true);
		disableFoodStacking = server.comment("Should disable stacking of certain food items?")
				.define("player.disableFoodStacking", true);

		infectionStagesDuration = server.comment("The duration of the individual infection stages").defineList(
				"player.infection_stages_duration", Arrays.asList(24000, 24000, 24000, 24000, 24000, 24000, 24000 ),it -> it instanceof Integer);

		// Mobs

		zombiesRunMode = server.comment("Maximal possible Quality").defineInRange("mobs.zombiesRunMode", 1, 0, 2);
		zombiesBreakBlocks = server.comment("Can players take photos with the Analog Camera item?")
				.define("mobs.zombiesBreakBlocks", true);
		bulletsBreakBlocks = server.comment("Can players take photos with the Analog Camera item?")
				.define("mobs.bulletsBreakBlocks", true);
		zombiesAttackAnimals = server.comment("Can players take photos with the Analog Camera item?")
				.define("mobs.zombiesAttackAnimals", true);
		bleedingChanceModifier = server.comment("Maximal possible Quality").defineInRange("mobs.bleedingChanceModifier",
				10, 0, Integer.MAX_VALUE);
		zombieCorpses = server.comment("Can players take photos with the Analog Camera item?")
				.define("mobs.zombieCorpses", true);

		// World

		bloodmoonFrequency = server.comment("How many days between individual bloodmoons (0 = disabled)").defineInRange("hordes.bloodmoonFrequency", 7,
				0, Integer.MAX_VALUE);
		bloodmoonHordeWaves = server.comment("How many waves bloodmoon horde has").defineInRange("hordes.bloodmoonHordeWaves",
				8, 0, Integer.MAX_VALUE);
		bloodmoonHordeZombiesPerWaveMin = server.comment("Minimal number of zombies the bloodmoon horde wave has - used during the first few bloodmoons")
				.defineInRange("hordes.bloodmoonHordeZombiesPerWaveMin", 5, 0, Integer.MAX_VALUE);
		bloodmoonHordeZombiesPerWaveMax = server.comment("Maximal number of zombies the bloodmoon horde wave has - used  after the fifth bloodmoon")
				.defineInRange("hordes.bloodmoonHordeZombiesPerWaveMax", 15, 0, Integer.MAX_VALUE);
		wolfHordeFrequency = server.comment("How many days between individual wolf hordes (0 = disabled)").defineInRange("hordes.wolfHordeFrequency", 5,
				0, Integer.MAX_VALUE);
		wolfHordeWaves = server.comment("How many waves wolf horde has").defineInRange("hordes.wolfHordeWaves", 8, 0,
				Integer.MAX_VALUE);
		wolfHordeZombiesPerWave = server.comment("How many zombies wolf horde wave has")
				.defineInRange("hordes.wolfHordeZombiesPerWave", 8, 0, Integer.MAX_VALUE);
		genericHordeChance = server.comment("Chance of a generic random horde spawning every tick, unless a horde has already spawned that day for the player (0 = disabled)").defineInRange("hordes.genericHordeChance", 0.00001,
				0, Double.MAX_VALUE);
		genericHordeWaves = server.comment("How many waves generic horde has").defineInRange("hordes.genericHordeWaves", 4,
				0, Integer.MAX_VALUE);
		genericHordeZombiesPerWaveMin = server.comment("Minimal number of zombies the horde wave has - used during the first few bloodmoons")
				.defineInRange("hordes.genericHordeZombiesPerWaveMin", 4, 0, Integer.MAX_VALUE);
		genericHordeZombiesPerWaveMax = server.comment("Maximal number of zombies the horde wave has - reached after the fifth bloodmoon")
				.defineInRange("hordes.genericHordeZombiesPerWaveMax", 8, 0, Integer.MAX_VALUE);
		corpseLifespan = server.comment("How many ticks until a corpse decays").defineInRange("mobs.corpseLifespan", 20000, 0,
				Integer.MAX_VALUE);
		torchBurnTime = server.comment("How many ticks until a torch burns out (-1 = infinity)").defineInRange("mobs.torchBurnTime", 22000, 0,
				Integer.MAX_VALUE);
		torchRainExtinguish = server.comment("Does rain extinguish burning torches?")
				.define("mobs.torchRainExtinguish", true);
		airdropFrequency = server.comment("How many days between individual airdrops (0 = disabled)").defineInRange("mobs.airdropFrequency", 3,
				0, Integer.MAX_VALUE);
		removeVanillaZommbies = server.comment("Should remove vanilla zombies (and skeletons, husks)?")
				.define("mobs.removeVanillaZommbies", true);
		hordeMinDistance = server.comment("The minimal distance from the player that a horde can spawn").defineInRange("hordes.bleedingChanceModifier", 30,
				0, Integer.MAX_VALUE);
		hordeMaxDistance = server.comment("The maximal distance from the player that a horde can spawn").defineInRange("hordes.bleedingChanceModifier", 30,
				0, Integer.MAX_VALUE);
		hordeWaveDelay = server.comment("The delay in ticks between individual waves").defineInRange("hordes.hordeWaveDelay", 200, 0,
				Integer.MAX_VALUE);
		damageDecayRate = server.comment("The rate of damaged blocks decay, how often does the decay update (12000 = every 12000 ticks - twice a day). Non-positive values disable the decay. Can be overridden by the damageDecayRate gamerule").defineInRange("world.damageDecayRate", 12000, -1,
				Integer.MAX_VALUE);

		/*
		WORLD GEN
		 */

		maxCitySize = server.comment("How many streets can a city have").defineInRange("gen.maxCitySize", 14, 1,
				Integer.MAX_VALUE);

		largeRockGenerationChance = server.comment("The chance of a chunk being suitable for Large Rocks. Larger numbers makes them less likely").defineInRange("gen.largeRockGenerationChance", 3, 0,
				Integer.MAX_VALUE);
		largeRockGenerationRateMin = server.comment("The minimal number of Large Rocks in a chunk").defineInRange("gen.largeRockGenerationRateMin", 0, 0,
				Integer.MAX_VALUE);
		largeRockGenerationRateMax = server.comment("The maximal number of Large Rocks in a chunk").defineInRange("gen.largeRockGenerationRateMax", 2, 0,
				Integer.MAX_VALUE);

		smallRockGenerationChance = server.comment("The chance of a chunk being suitable for Small Rocks. Larger numbers makes them less likely").defineInRange("gen.largeRockGenerationChance", 2, 0,
				Integer.MAX_VALUE);
		smallRockGenerationRateMin = server.comment("The minimal number of Small Rocks in a chunk").defineInRange("gen.largeRockGenerationRateMin", 0, 0,
				Integer.MAX_VALUE);
		smallRockGenerationRateMax = server.comment("The maximal number of Small Rocks in a chunk").defineInRange("gen.largeRockGenerationRateMax", 4, 0,
				Integer.MAX_VALUE);

		berryGenerationChance = server.comment("The chance of a chunk being suitable for Berry Bushes. Larger numbers makes them less likely").defineInRange("gen.berryGenerationChance", 5, 0,
				Integer.MAX_VALUE);
		berryGenerationRateMin = server.comment("The minimal number of Berry Bushes in a chunk").defineInRange("gen.berryGenerationRateMin", 0, 0,
				Integer.MAX_VALUE);
		berryGenerationRateMax = server.comment("The maximal number of Berry Bushes in a chunk").defineInRange("gen.berryGenerationRateMax", 6, 0,
				Integer.MAX_VALUE);

		stickGenerationChance = server.comment("The chance of a chunk being suitable for sticks. Larger numbers makes them less likely").defineInRange("gen.stickGenerationChance", 2, 0,
				Integer.MAX_VALUE);
		stickGenerationRateMin = server.comment("The minimal number of sticks in a chunk").defineInRange("gen.stickGenerationRateMin", 0, 0,
				Integer.MAX_VALUE);
		stickGenerationRateMax = server.comment("The maximal number of sticks in a chunk").defineInRange("gen.stickGenerationRateMax", 5, 0,
				Integer.MAX_VALUE);

		citySpacing = server.comment("How many chunks between potential City locations").defineInRange("gen.citySpacing", 32, 1,
				Integer.MAX_VALUE);
		maxCitySize = server.comment("How many streets can a city have").defineInRange("gen.maxCitySize", 14, 0,
				Integer.MAX_VALUE);

	}
}
