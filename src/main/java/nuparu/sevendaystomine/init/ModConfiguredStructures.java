package nuparu.sevendaystomine.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ModConfiguredStructures {
    /**
     * Static instance of our structure so we can reference it and add it to biomes easily.
     */
    public static StructureFeature<?, ?> CONFIGURED_CITY = ModStructureFeatures.CITY.get().configured(IFeatureConfig.NONE);
    public static StructureFeature<?, ?> CONFIGURED_PILLAGER_OUTPOST_RUINED = ModStructureFeatures.PILLAGER_OUTPOST_RUINED.get().configured(IFeatureConfig.NONE);
    public static StructureFeature<?, ?> CONFIGURED_WINDMILL = ModStructureFeatures.WINDMILL.get().configured(IFeatureConfig.NONE);
    public static StructureFeature<?, ?> CONFIGURED_RUINS = ModStructureFeatures.RUINS.get().configured(IFeatureConfig.NONE);

    /**
     * Registers the configured structure which is what gets added to the biomes.
     * Noticed we are not using a forge registry because there is none for configured structures.
     *
     * We can register configured structures at any time before a world is clicked on and made.
     * But the best time to register configured features by code is honestly to do it in FMLCommonSetupEvent.
     */
    public static void registerConfiguredStructures() {
        Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(SevenDaysToMine.MODID, "configured_city"), CONFIGURED_CITY);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(ModStructureFeatures.CITY.get(), CONFIGURED_CITY);

        Registry.register(registry, new ResourceLocation(SevenDaysToMine.MODID, "configured_pillager_outpost"), CONFIGURED_PILLAGER_OUTPOST_RUINED);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(ModStructureFeatures.PILLAGER_OUTPOST_RUINED.get(), CONFIGURED_PILLAGER_OUTPOST_RUINED);

        Registry.register(registry, new ResourceLocation(SevenDaysToMine.MODID, "configured_windmill"), CONFIGURED_WINDMILL);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(ModStructureFeatures.WINDMILL.get(), CONFIGURED_WINDMILL);

        Registry.register(registry, new ResourceLocation(SevenDaysToMine.MODID, "configured_ruins"), CONFIGURED_RUINS);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(ModStructureFeatures.RUINS.get(), CONFIGURED_RUINS);
    }
}
