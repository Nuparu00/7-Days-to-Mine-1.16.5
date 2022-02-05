package nuparu.sevendaystomine.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class ServerConfig {

    // Player
    public static ForgeConfigSpec.BooleanValue renderPlayerInventory;
    public static ForgeConfigSpec.BooleanValue allowPhotos;
    public static ForgeConfigSpec.BooleanValue allowCameras;
    public static ForgeConfigSpec.BooleanValue immersiveBlockBreaking;
    public static ForgeConfigSpec.DoubleValue immersiveBlockBreakingModifier;
    public static ForgeConfigSpec.DoubleValue scrapCoefficient;
    public static ForgeConfigSpec.BooleanValue recipeBooksRequired;
    public static ForgeConfigSpec.DoubleValue xpPerQuality;
    public static ForgeConfigSpec.IntValue maxQuality;
    public static ForgeConfigSpec.BooleanValue thirstSystem;
    public static ForgeConfigSpec.BooleanValue staminaSystem;
    public static ForgeConfigSpec.EnumValue<EnumQualityState> qualitySystem;
    public static ForgeConfigSpec.BooleanValue backpackSlot;
    public static ForgeConfigSpec.BooleanValue fragileLegs;
    public static ForgeConfigSpec.BooleanValue extraPoisonousBerries;
    public static ForgeConfigSpec.ConfigValue<List<? extends Integer>> infectionStagesDuration;
    public static ForgeConfigSpec.IntValue zombiesRunMode;
    public static ForgeConfigSpec.BooleanValue zombiesBreakBlocks;
    public static ForgeConfigSpec.BooleanValue bulletsBreakBlocks;
    public static ForgeConfigSpec.BooleanValue zombiesAttackAnimals;
    public static ForgeConfigSpec.IntValue bleedingChanceModifier;
    public static ForgeConfigSpec.BooleanValue zombieCorpses;
    public static ForgeConfigSpec.IntValue bloodmoonFrequency;
    public static ForgeConfigSpec.IntValue bloodmoonHordeWaves;
    public static ForgeConfigSpec.IntValue bloodmoonHordeZombiesPerWaveMin;
    public static ForgeConfigSpec.IntValue bloodmoonHordeZombiesPerWaveMax;
    public static ForgeConfigSpec.IntValue wolfHordeWaves;
    public static ForgeConfigSpec.IntValue wolfHordeZombiesPerWave;
    public static ForgeConfigSpec.IntValue genericHordeWaves;
    public static ForgeConfigSpec.IntValue genericHordeZombiesPerWaveMin;
    public static ForgeConfigSpec.IntValue genericHordeZombiesPerWaveMax;
    public static ForgeConfigSpec.IntValue corpseLifespan;
    public static ForgeConfigSpec.IntValue torchBurnTime;
    public static ForgeConfigSpec.BooleanValue torchRainExtinguish;
    public static ForgeConfigSpec.IntValue airdropFrequency;
    public static ForgeConfigSpec.BooleanValue removeVanillaZommbies;
    public static ForgeConfigSpec.IntValue hordeMinDistance;
    public static ForgeConfigSpec.IntValue hordeMaxDistance;
    public static ForgeConfigSpec.IntValue hordeWaveDelay;
    public static ForgeConfigSpec.IntValue damageDecayRate;

    public static void init(ForgeConfigSpec.Builder server) {
        //Players
        renderPlayerInventory = server.comment(
                "Controls rendering of player's items (weapons, tools). If false on a server, no one will be able to see the items regardless of their settings")
                .define("player.renderPlayerInventory", true);
        allowPhotos = server.comment("Can players take photos with the Analog Camera item?")
                .define("player.allowPhotos", true);
        allowCameras = server.comment("Can players use cameras (the block)?").define("player.allowCameras", true);
        xpPerQuality = server.comment("How many XP per 1 Quality point").defineInRange("player.xpPerQuality", 5, 0,
                Double.MAX_VALUE);
        maxQuality = server.comment("Maximal possible Quality").defineInRange("player.maxQuality", 600, 0,
                Integer.MAX_VALUE);
        thirstSystem = server.comment("Should use the thirst system?").define("player.thirstSystem", true);
        staminaSystem = server.comment("Should use the stamina system?").define("player.staminaSystem", true);
        qualitySystem = server.comment("Should use the quality system?").defineEnum("player.qualitySystem" , EnumQualityState.ALL,EnumQualityState.values());
        backpackSlot = server.comment(
                "Should add the backpack slot to the player inventory (does not affect the texture, if you turn this off, you should used a resourcepack where the slot is removed from the texture)")
                .define("player.backpackSlot", true);
        fragileLegs = server.comment("Can a player break their legs on fall?").define("player.fragileLegs", true);
        extraPoisonousBerries = server.comment("Does the baneberry give the poison status effect?")
                .define("player.extraPoisonousBerries", true);
        infectionStagesDuration = server.comment("The duration of the individual infection stages").defineList(
                "player.infection_stages_duration", Arrays.asList(24000, 24000, 24000, 24000, 24000, 24000, 24000 ), it -> it instanceof Integer);
        immersiveBlockBreaking = server.comment("Makes block breaking slower and more immersive")
                .define("player.immersiveBlockBreaking", true);
        immersiveBlockBreakingModifier = server.comment(
                "Affects the block breaking speed when immersiveBlockBreaking is true. The higher the number, the slower the breaking is")
                .defineInRange("player.immersiveBlockBreakingModifier", 32d, 0, Double.MAX_VALUE);
        scrapCoefficient = server.comment("Controls how much scrap you get from scrapping in inventory")
                .defineInRange("player.scrapCoefficient", 0.5, 0, 1);
        recipeBooksRequired = server.comment("Do recipes have to be unlocked using the recipe books?")
                .define("player.recipeBooksRequired", true);

        // Mobs

        zombiesRunMode = server.comment("Maximal possible Quality").defineInRange("mobs.zombiesRunMode", 1, 0, 2);
        zombiesBreakBlocks = server.comment("Can zombies break blocks?")
                .define("mobs.zombiesBreakBlocks", true);
        bulletsBreakBlocks = server.comment("Can bullets break blocks?")
                .define("mobs.bulletsBreakBlocks", true);
        zombiesAttackAnimals = server.comment("Can zombies attack animals (and vanilla villagers)?")
                .define("mobs.zombiesAttackAnimals", false);
        bleedingChanceModifier = server.comment("Maximal possible Quality").defineInRange("mobs.bleedingChanceModifier",
                10, 0, Integer.MAX_VALUE);
        zombieCorpses = server.comment("Do corpses spawn on zombies' death?")
                .define("mobs.zombieCorpses", true);

        //World

        bloodmoonFrequency = server.comment("How many days between individual bloodmoons (0 = disabled)").defineInRange("hordes.bloodmoonFrequency", 7,
                0, Integer.MAX_VALUE);
        bloodmoonHordeWaves = server.comment("How many waves bloodmoon horde has").defineInRange("hordes.bloodmoonHordeWaves",
                8, 0, Integer.MAX_VALUE);
        bloodmoonHordeZombiesPerWaveMin = server.comment("Minimal number of zombies the bloodmoon horde wave has - used during the first few bloodmoons")
                .defineInRange("hordes.bloodmoonHordeZombiesPerWaveMin", 6, 0, Integer.MAX_VALUE);
        bloodmoonHordeZombiesPerWaveMax = server.comment("Maximal number of zombies the bloodmoon horde wave has - used  after the fifth bloodmoon")
                .defineInRange("hordes.bloodmoonHordeZombiesPerWaveMax", 15, 0, Integer.MAX_VALUE);
        wolfHordeWaves = server.comment("How many waves wolf horde has").defineInRange("hordes.wolfHordeWaves", 3, 0,
                Integer.MAX_VALUE);
        wolfHordeZombiesPerWave = server.comment("How many zombies wolf horde wave has")
                .defineInRange("hordes.wolfHordeZombiesPerWave", 8, 0, Integer.MAX_VALUE);
        genericHordeWaves = server.comment("How many waves generic horde has").defineInRange("hordes.genericHordeWaves", 4,
                0, Integer.MAX_VALUE);
        genericHordeZombiesPerWaveMin = server.comment("Minimal number of zombies the horde wave has - used during the first few bloodmoons")
                .defineInRange("hordes.genericHordeZombiesPerWaveMin", 4, 0, Integer.MAX_VALUE);
        genericHordeZombiesPerWaveMax = server.comment("Maximal number of zombies the horde wave has - reached after the fifth bloodmoon")
                .defineInRange("hordes.genericHordeZombiesPerWaveMax", 5, 0, Integer.MAX_VALUE);
        corpseLifespan = server.comment("How many ticks until a corpse decays").defineInRange("mobs.corpseLifespan", 20000, 0,
                Integer.MAX_VALUE);
        torchBurnTime = server.comment("How many ticks until a torch burns out (-1 = infinity)").defineInRange("mobs.torchBurnTime", 22000, 0,
                Integer.MAX_VALUE);
        torchRainExtinguish = server.comment("Does rain extinguish burning torches?")
                .define("mobs.torchRainExtinguish", true);
        airdropFrequency = server.comment("How many days between individual airdrops (0 = disabled)").defineInRange("mobs.airdropFrequency", 1,
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


    }
}
