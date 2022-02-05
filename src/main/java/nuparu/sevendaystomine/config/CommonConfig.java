package nuparu.sevendaystomine.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

import java.util.Arrays;
import java.util.List;

public class CommonConfig {

    public static BooleanValue survivalGuide;
    public static BooleanValue disableBlockToIngot;
    public static IntValue infectionChanceModifier;

    // Mobs

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

    public static IntValue spawnWeightInfectedSurvivor;
    public static IntValue spawnMinInfectedSurvivor;
    public static IntValue spawnMaxInfectedSurvivor;

    public static IntValue spawnWeightSoulBurntZombie;
    public static IntValue spawnMinSoulBurntZombie;
    public static IntValue spawnMaxSoulBurntZombie;

    public static DoubleValue zombieKnockbackResistance;

    // World

    public static IntValue wolfHordeFrequency;
    public static DoubleValue genericHordeChance;

    // World gen

    public static BooleanValue generateRoads;
    public static IntValue roadMinY;
    public static IntValue roadMaxY;
    public static BooleanValue sandRoadCover;

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
    public static IntValue wastelandForestWeight;
    public static IntValue wastelandWeight;


    public static IntValue copperMinY;
    public static IntValue copperMaxY;
    public static IntValue copperCount;
    public static IntValue copperVeinSize;

    public static IntValue tinMinY;
    public static IntValue tinMaxY;
    public static IntValue tinCount;
    public static IntValue tinVeinSize;

    public static IntValue zincMinY;
    public static IntValue zincMaxY;
    public static IntValue zincCount;
    public static IntValue zincVeinSize;

    public static IntValue leadMinY;
    public static IntValue leadMaxY;
    public static IntValue leadCount;
    public static IntValue leadVeinSize;

    public static IntValue potassiumMinY;
    public static IntValue potassiumMaxY;
    public static IntValue potassiumCount;
    public static IntValue potassiumVeinSize;

    public static IntValue cinnabarMinY;
    public static IntValue cinnabarMaxY;
    public static IntValue cinnabarCount;
    public static IntValue cinnabarVeinSize;

    public static IntValue cityMinDistance;
    public static IntValue cityAvgDistance;

    public static IntValue villageMinDistance;
    public static IntValue villageAvgDistance;

    public static IntValue cityToVillageMinDistance;
    public static IntValue largeStructureMaxSize;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> disabledRecipes;

    public static void init(ForgeConfigSpec.Builder server) {

        // Players

        survivalGuide = server.comment("Should players receive the Survival Guide on their first spawn?")
                .define("player.survivalGuide", true);
        disableBlockToIngot = server
                .comment("Disables crafting of ingots from gold/iron blocks and nuggets from gold/iron ingots")
                .define("player.disableBlockToIngot", true);
        disabledRecipes = server.comment("The list of disabled crafting recipes").defineList(
                "player.disabledRecipes", Arrays.asList("minecraft:oak_planks", "minecraft:birch_planks", "minecraft:spruce_planks",
						"minecraft:jungle_planks", "minecraft:dark_oak_planks", "minecraft:acacia_planks", "minecraft:crimson_planks",
						"minecraft:warped_planks", "minecraft:furnace", "minecraft:wooden_sword", "minecraft:wooden_spade", "minecraft:wooden_pickaxe",
						"minecraft:wooden_axe", "minecraft:wooden_hoe", "minecraft:stone_sword", "minecraft:stone_spade", "minecraft:stone_pickaxe",
						"minecraft:stone_axe", "minecraft:stone_hoe", "minecraft:iron_sword", "minecraft:gold_sword", "minecraft:diamond_sword",
						"minecraft:diamond_shovel", "minecraft:diamond_pickaxe", "minecraft:diamond_axe", "minecraft:diamond_hoe", "minecraft:diamond_helmet",
						"minecraft:diamond_chestplate", "minecraft:diamond_leggings", "minecraft:diamond_boots", "minecraft:iron_ingot",
						"minecraft:iron_ingot_from_blasting", "minecraft:gold_ingot", "minecraft:gold_ingot_from_blasting"), it -> it instanceof String);

        infectionChanceModifier = server.comment("The larger the value, the less likely a player is to get infected")
                .defineInRange("player.infectionChanceModifier", 6, 1, Integer.MAX_VALUE);

		/*disableFoodStacking = server.comment("Should disable stacking of certain food items?")
				.define("player.disableFoodStacking", true);*/


        //Mobs

        // World

        wolfHordeFrequency = server.comment("How many days between individual wolf hordes (0 = disabled)").defineInRange("hordes.wolfHordeFrequency", 5,
                0, Integer.MAX_VALUE);
        genericHordeChance = server.comment("Chance of a generic random horde spawning every tick, unless a horde has already spawned that day for the player (0 = disabled)").defineInRange("hordes.genericHordeChance", 0.00001,
                0, Double.MAX_VALUE);


		/*
		WORLD GEN
		 */

        generateRoads = server.comment("Should generate roads?")
                .define("worldGen.generateRoads", true);
        largeRockGenerationChance = server.comment("The chance of a chunk being suitable for Large Rocks. Larger numbers makes them less likely").defineInRange("worldGen.largeRockGenerationChance", 3, 0,
                Integer.MAX_VALUE);
        largeRockGenerationRateMin = server.comment("The minimal number of Large Rocks in a chunk").defineInRange("worldGen.largeRockGenerationRateMin", 0, 0,
                Integer.MAX_VALUE);
        largeRockGenerationRateMax = server.comment("The maximal number of Large Rocks in a chunk").defineInRange("worldGen.largeRockGenerationRateMax", 2, 0,
                Integer.MAX_VALUE);

        smallRockGenerationChance = server.comment("The chance of a chunk being suitable for Small Rocks. Larger numbers makes them less likely").defineInRange("worldGen.largeRockGenerationChance", 2, 0,
                Integer.MAX_VALUE);
        smallRockGenerationRateMin = server.comment("The minimal number of Small Rocks in a chunk").defineInRange("worldGen.largeRockGenerationRateMin", 0, 0,
                Integer.MAX_VALUE);
        smallRockGenerationRateMax = server.comment("The maximal number of Small Rocks in a chunk").defineInRange("worldGen.largeRockGenerationRateMax", 4, 0,
                Integer.MAX_VALUE);

        berryGenerationChance = server.comment("The chance of a chunk being suitable for Berry Bushes. Larger numbers makes them less likely").defineInRange("worldGen.berryGenerationChance", 5, 0,
                Integer.MAX_VALUE);
        berryGenerationRateMin = server.comment("The minimal number of Berry Bushes in a chunk").defineInRange("worldGen.berryGenerationRateMin", 0, 0,
                Integer.MAX_VALUE);
        berryGenerationRateMax = server.comment("The maximal number of Berry Bushes in a chunk").defineInRange("worldGen.berryGenerationRateMax", 6, 0,
                Integer.MAX_VALUE);

        stickGenerationChance = server.comment("The chance of a chunk being suitable for sticks. Larger numbers makes them less likely").defineInRange("worldGen.stickGenerationChance", 2, 0,
                Integer.MAX_VALUE);
        stickGenerationRateMin = server.comment("The minimal number of sticks in a chunk").defineInRange("worldGen.stickGenerationRateMin", 0, 0,
                Integer.MAX_VALUE);
        stickGenerationRateMax = server.comment("The maximal number of sticks in a chunk").defineInRange("worldGen.stickGenerationRateMax", 5, 0,
                Integer.MAX_VALUE);

        roadMaxY = server.comment("The max Y of roads").defineInRange("worldGen.roadMaxY", 80, 0,
                255);
        roadMinY = server.comment("The min Y of roads").defineInRange("worldGen.roadMinY", 63, 0,
                255);
        sandRoadCover = server.comment("Should generate sand cover in sandy biomes over the intercity roads?")
                .define("worldGen.sandRoadCover", true);

        wastelandForestWeight = server.comment("The weighted probability of the Wasteland Forest biome").defineInRange("worldGen.wastelandForestWeight", 6, 0,
                Integer.MAX_VALUE);
        wastelandWeight = server.comment("The weighted probability of the Wasteland biome").defineInRange("worldGen.wastelandForestWeight", 3, 0,
                Integer.MAX_VALUE);

        copperMinY = server.comment("The min Y of Copper Ore").defineInRange("worldGen.copperMinY", 32, 0,
                Integer.MAX_VALUE);
        copperMaxY = server.comment("The max Y of Copper Ore").defineInRange("worldGen.copperMaxY", 128, 0,
                Integer.MAX_VALUE);
        copperCount = server.comment("The number of copper veins in a chunk?").defineInRange("worldGen.copperCount", 3, 0,
                Integer.MAX_VALUE);
        copperVeinSize = server.comment("The the maximal copper vein size").defineInRange("worldGen.copperVeinSize", 10, 0,
                Integer.MAX_VALUE);

        tinMinY = server.comment("The min Y of Tin Ore").defineInRange("worldGen.tinMinY", 5, 0,
                Integer.MAX_VALUE);
        tinMaxY = server.comment("The max Y of Tin Ore").defineInRange("worldGen.tinMaxY", 128, 0,
                Integer.MAX_VALUE);
        tinCount = server.comment("The number of tin veins in a chunk?").defineInRange("worldGen.tinCount", 8, 0,
                Integer.MAX_VALUE);
        tinVeinSize = server.comment("The the maximal tin vein size").defineInRange("worldGen.tinVeinSize", 5, 0,
                Integer.MAX_VALUE);

        zincMinY = server.comment("The min Y of Zinc Ore").defineInRange("worldGen.zincMinY", 5, 0,
                Integer.MAX_VALUE);
        zincMaxY = server.comment("The max Y of Zinc Ore").defineInRange("worldGen.zincMaxY", 128, 0,
                Integer.MAX_VALUE);
        zincCount = server.comment("The number of zinc veins in a chunk?").defineInRange("worldGen.zincCount", 6, 0,
                Integer.MAX_VALUE);
        zincVeinSize = server.comment("The the maximal zinc vein size").defineInRange("worldGen.zincVeinSize", 5, 0,
                Integer.MAX_VALUE);

        leadMinY = server.comment("The min Y of Lead Ore").defineInRange("worldGen.leadMinY", 10, 0,
                Integer.MAX_VALUE);
        leadMaxY = server.comment("The max Y of Lead Ore").defineInRange("worldGen.leadMaxY", 80, 0,
                Integer.MAX_VALUE);
        leadCount = server.comment("The number of lead veins in a chunk?").defineInRange("worldGen.leadCount", 8, 0,
                Integer.MAX_VALUE);
        leadVeinSize = server.comment("The the maximal lead vein size").defineInRange("worldGen.leadVeinSize", 6, 0,
                Integer.MAX_VALUE);

        potassiumMinY = server.comment("The min Y of Potassium Ore").defineInRange("worldGen.potassiumMinY", 50, 0,
                Integer.MAX_VALUE);
        potassiumMaxY = server.comment("The max Y of Potassium Ore").defineInRange("worldGen.potassiumMaxY", 128, 0,
                Integer.MAX_VALUE);
        potassiumCount = server.comment("The number of potassium veins in a chunk?").defineInRange("worldGen.potassiumCount", 3, 0,
                Integer.MAX_VALUE);
        potassiumVeinSize = server.comment("The the maximal potassium vein size").defineInRange("worldGen.potassiumVeinSize", 10, 0,
                Integer.MAX_VALUE);

        cinnabarMinY = server.comment("The min Y of Cinnabar Ore").defineInRange("worldGen.cinnabarMinY", 70, 0,
                Integer.MAX_VALUE);
        cinnabarMaxY = server.comment("The max Y of Cinnabar Ore").defineInRange("worldGen.cinnabarMaxY", 256, 0,
                Integer.MAX_VALUE);
        cinnabarCount = server.comment("The number of cinnabar veins in a chunk?").defineInRange("worldGen.cinnabarCount", 6, 0,
                Integer.MAX_VALUE);
        cinnabarVeinSize = server.comment("The the maximal cinnabar vein size").defineInRange("worldGen.cinnabarVeinSize", 3, 0,
                Integer.MAX_VALUE);

        cityMinDistance = server.comment("The minimal distance (in chunks) between two cities").defineInRange("worldGen.cityMinDistance", 30, 0,
                Integer.MAX_VALUE);
        cityAvgDistance = server.comment("The average distance (in chunks) between two cities").defineInRange("worldGen.cityAvgDistance", 50, 0,
                Integer.MAX_VALUE);
        villageMinDistance = server.comment("The minimal distance (in chunks) between two villages").defineInRange("worldGen.villageMinDistance", 28, 0,
                Integer.MAX_VALUE);
        villageAvgDistance = server.comment("The average distance (in chunks) between two villages").defineInRange("worldGen.villageAvgDistance", 40, 0,
                Integer.MAX_VALUE);

        cityToVillageMinDistance = server.comment("The min distance (in chunks) between a city and a village").defineInRange("worldGen.cityToVillageMinDistance", 24, 0,
                Integer.MAX_VALUE);

        largeStructureMaxSize = server.comment("The largest size (in blocks) a large jigsaw structure (cities and villages) can have. For reference, the Vanilla default is 80.").defineInRange("worldGen.largeStructureMaxSize", 128, 0,
                Integer.MAX_VALUE);

        //Mobs
        spawnWeightReanimatedCorpse = server.comment("The spawn weight of the Reanimated Corpse").defineInRange("mobs.spawnWeightReanimatedCorpse", 70, 0,
                Integer.MAX_VALUE);
        spawnMinReanimatedCorpse = server.comment("The minimal amount of Reanimated Corpses").defineInRange("mobs.spawnMinReanimatedCorpse", 3, 0,
                Integer.MAX_VALUE);
        spawnMaxReanimatedCorpse = server.comment("The maximal amount of Reanimated Corpses").defineInRange("mobs.spawnMaxReanimatedCorpse", 7, 0,
                Integer.MAX_VALUE);

        spawnWeightPlaguedNurse = server.comment("The spawn weight of the Plagued Nurse").defineInRange("mobs.spawnWeightPlaguedNurse", 70, 0,
                Integer.MAX_VALUE);
        spawnMinPlaguedNurse = server.comment("The minimal amount of Plagued Nurses").defineInRange("mobs.spawnMinPlaguedNurse", 3, 0,
                Integer.MAX_VALUE);
        spawnMaxPlaguedNurse = server.comment("The maximal amount of Plagued Nurses").defineInRange("mobs.spawnMaxPlaguedNurse", 7, 0,
                Integer.MAX_VALUE);

        spawnWeightFrozenLumberjack = server.comment("The spawn weight of the Frozen Lumberjack").defineInRange("mobs.spawnWeightFrozenLumberjack", 70, 0,
                Integer.MAX_VALUE);
        spawnMinFrozenLumberjack = server.comment("The minimal amount of Frozen Lumberjacks").defineInRange("mobs.spawnMinFrozenLumberjack", 3, 0,
                Integer.MAX_VALUE);
        spawnMaxFrozenLumberjack = server.comment("The maximal amount of Frozen Lumberjacks").defineInRange("mobs.spawnMaxFrozenLumberjack", 7, 0,
                Integer.MAX_VALUE);

        spawnWeightFrigidHunter = server.comment("The spawn weight of the Frigid Hunter").defineInRange("mobs.spawnWeightFrigidHunter", 70, 0,
                Integer.MAX_VALUE);
        spawnMinFrigidHunter = server.comment("The minimal amount of Frigid Hunters").defineInRange("mobs.spawnMinFrigidHunter", 3, 0,
                Integer.MAX_VALUE);
        spawnMaxFrigidHunter = server.comment("The maximal amount of Frigid Hunters").defineInRange("mobs.spawnMaxFrigidHunter", 7, 0,
                Integer.MAX_VALUE);

        spawnWeightFrostbittenWorker = server.comment("The spawn weight of the Frostbitten Worker").defineInRange("mobs.spawnWeightFrostbittenWorker", 70, 0,
                Integer.MAX_VALUE);
        spawnMinFrostbittenWorker = server.comment("The minimal amount of Frostbitten Workers").defineInRange("mobs.spawnMinFrostbittenWorker", 3, 0,
                Integer.MAX_VALUE);
        spawnMaxFrostbittenWorker = server.comment("The maximal amount of Frostbitten Workers").defineInRange("mobs.spawnMaxFrostbittenWorker", 7, 0,
                Integer.MAX_VALUE);

        spawnWeightZombieMiner = server.comment("The spawn weight of the Zombie Miner").defineInRange("mobs.spawnWeightZombieMiner", 70, 0,
                Integer.MAX_VALUE);
        spawnMinZombieMiner = server.comment("The minimal amount of Zombie Miners").defineInRange("mobs.spawnMinZombieMiner", 3, 0,
                Integer.MAX_VALUE);
        spawnMaxZombieMiner = server.comment("The maximal amount of Zombie Miners").defineInRange("mobs.spawnMaxZombieMiner", 7, 0,
                Integer.MAX_VALUE);

        spawnWeightSpiderZombie = server.comment("The spawn weight of the Spider Zombie").defineInRange("mobs.spawnWeightSpiderZombie", 70, 0,
                Integer.MAX_VALUE);
        spawnMinSpiderZombie = server.comment("The minimal amount of Spider Zombies").defineInRange("mobs.spawnMinSpiderZombie", 3, 0,
                Integer.MAX_VALUE);
        spawnMaxSpiderZombie = server.comment("The maximal amount of Spider Zombies").defineInRange("mobs.spawnMaxSpiderZombie", 7, 0,
                Integer.MAX_VALUE);

        spawnWeightZombieCrawler = server.comment("The spawn weight of the Crawler Zombie").defineInRange("mobs.spawnWeightZombieCrawler", 70, 0,
                Integer.MAX_VALUE);
        spawnMinZombieCrawler = server.comment("The minimal amount of Crawler Zombies").defineInRange("mobs.spawnMinZombieCrawler", 3, 0,
                Integer.MAX_VALUE);
        spawnMaxZombieCrawler = server.comment("The maximal amount of Crawler Zombies").defineInRange("mobs.spawnMaxZombieCrawler", 7, 0,
                Integer.MAX_VALUE);

        spawnWeightBurntZombie = server.comment("The spawn weight of the Burnt Zombie").defineInRange("mobs.spawnWeightBurntZombie", 70, 0,
                Integer.MAX_VALUE);
        spawnMinBurntZombie = server.comment("The minimal amount of Burnt Zombies").defineInRange("mobs.spawnMinBurntZombie", 3, 0,
                Integer.MAX_VALUE);
        spawnMaxBurntZombie = server.comment("The maximal amount of Burnt Zombies").defineInRange("mobs.spawnMaxBurntZombie", 7, 0,
                Integer.MAX_VALUE);

        spawnWeightSoulBurntZombie = server.comment("The spawn weight of the Soul Burnt Zombie").defineInRange("mobs.spawnWeightSoulBurntZombie", 70, 0,
                Integer.MAX_VALUE);
        spawnMinSoulBurntZombie = server.comment("The minimal amount of Soul Burnt Zombies").defineInRange("mobs.spawnMinSoulBurntZombie", 3, 0,
                Integer.MAX_VALUE);
        spawnMaxSoulBurntZombie = server.comment("The maximal amount of Soul Burnt Zombies").defineInRange("mobs.spawnMaxSoulBurntZombie", 7, 0,
                Integer.MAX_VALUE);

        spawnWeightBloatedZombie = server.comment("The spawn weight of the Bloated Zombie").defineInRange("mobs.spawnWeightBloatedZombie", 70, 0,
                Integer.MAX_VALUE);
        spawnMinBloatedZombie = server.comment("The minimal amount of Bloated Zombies").defineInRange("mobs.spawnMinBloatedZombie", 3, 0,
                Integer.MAX_VALUE);
        spawnMaxBloatedZombie = server.comment("The maximal amount of Bloated Zombies").defineInRange("mobs.spawnMaxBloatedZombie", 7, 0,
                Integer.MAX_VALUE);

        spawnWeightInfectedSurvivor = server.comment("The spawn weight of the Infected Survivor").defineInRange("mobs.spawnWeightInfectedSurvivor", 70, 0,
                Integer.MAX_VALUE);
        spawnMinInfectedSurvivor = server.comment("The minimal amount of Infected Survivors").defineInRange("mobs.spawnMinInfectedSurvivor", 3, 0,
                Integer.MAX_VALUE);
        spawnMaxInfectedSurvivor = server.comment("The maximal amount of Infected Survivors").defineInRange("mobs.spawnMaxInfectedSurvivor", 7, 0,
                Integer.MAX_VALUE);

        spawnWeightZombieWolf = server.comment("The spawn weight of the Zombie Wolf").defineInRange("mobs.spawnWeightZombieWolf", 70, 0,
                Integer.MAX_VALUE);
        spawnMinZombieWolf = server.comment("The minimal amount of Zombie Wolfs").defineInRange("mobs.spawnMinZombieWolf", 3, 0,
                Integer.MAX_VALUE);
        spawnMaxZombieWolf = server.comment("The maximal amount of Zombie Wolfs").defineInRange("mobs.spawnMaxZombieWolf", 7, 0,
                Integer.MAX_VALUE);

        spawnWeightZombiePig = server.comment("The spawn weight of the Zombie Pig").defineInRange("mobs.spawnWeightZombiePig", 70, 0,
                Integer.MAX_VALUE);
        spawnMinZombiePig = server.comment("The minimal amount of Zombie Pigs").defineInRange("mobs.spawnMinZombiePig", 3, 0,
                Integer.MAX_VALUE);
        spawnMaxZombiePig = server.comment("The maximal amount of Zombie Pigs").defineInRange("mobs.spawnMaxZombiePig", 7, 0,
                Integer.MAX_VALUE);

        spawnWeightFeralZombie = server.comment("The spawn weight of the Feral Zombie").defineInRange("mobs.spawnWeightFeralZombie", 1, 0,
                Integer.MAX_VALUE);
        spawnMinFeralZombie = server.comment("The minimal amount of Feral Zombies").defineInRange("mobs.spawnMinFeralZombie", 1, 0,
                Integer.MAX_VALUE);
        spawnMaxFeralZombie = server.comment("The maximal amount of Feral Zombies").defineInRange("mobs.spawnMaxFeralZombie", 1, 0,
                Integer.MAX_VALUE);

    }
}
