package nuparu.sevendaystomine.init;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FancyFoliagePlacer;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.trunkplacer.FancyTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.gen.feature.*;

import java.util.OptionalInt;

public class ModFeatures {

	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES,
			SevenDaysToMine.MODID);

	public static Feature<IFeatureConfig> largeRock;
	public static Feature<IFeatureConfig> smallStone;
	public static Feature<IFeatureConfig> berryBush;
	public static Feature<IFeatureConfig> stick;
	public static Feature<IFeatureConfig> corn;
	public static Feature<IFeatureConfig> city;
	public static Feature<IFeatureConfig> roads;
	public static Feature<IFeatureConfig> cars;

	public static ConfiguredFeature<?, ?> largeRockFeature;
	public static ConfiguredFeature<?, ?> smallStoneFeature;
	public static ConfiguredFeature<?, ?> berryBushFeature;
	public static ConfiguredFeature<?, ?> stickFeature;
	public static ConfiguredFeature<?, ?> cornFeature;
	public static ConfiguredFeature<?, ?> cityFeature;
	public static ConfiguredFeature<?, ?> roadsFeature;
	public static ConfiguredFeature<?, ?> carsFeature;
	public static ConfiguredFeature<?, ?> burntTreeFeatureLarge;
	public static ConfiguredFeature<?, ?> burntTreeFeature;
	public static ConfiguredFeature<?, ?> burntTrees;

	public static void finishRegistration(RegistryEvent.Register<Feature<?>> event){
		largeRock = new FeatureStoneRock(NoFeatureConfig.CODEC);
		event.getRegistry().register(largeRock.setRegistryName("large_rock"));
		largeRockFeature = largeRock.configured(IFeatureConfig.NONE)
				.decorated(Placement.NOPE.configured(IPlacementConfig.NONE)).chance(2);
		Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(SevenDaysToMine.MODID,"large_rock"), largeRockFeature);

		smallStone = new FeatureSmallStone(NoFeatureConfig.CODEC);
		event.getRegistry().register(smallStone.setRegistryName("small_stone"));
		smallStoneFeature = smallStone.configured(IFeatureConfig.NONE)
				.decorated(Placement.NOPE.configured(IPlacementConfig.NONE)).chance(1);
		Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(SevenDaysToMine.MODID,"small_stone"), smallStoneFeature);

		berryBush = new FeatureBerryBush(BlockClusterFeatureConfig.CODEC);
		event.getRegistry().register(berryBush.setRegistryName("berry_bush"));
		berryBushFeature = berryBush.configured((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.BEDROCK.defaultBlockState()), SimpleBlockPlacer.INSTANCE)).xspread(4).yspread(4).zspread(4).canReplace().tries(64).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK.defaultBlockState().getBlock())).noProjection().build())
				.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(4);
		Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(SevenDaysToMine.MODID,"berry_bush"), berryBushFeature);

		stick = new FeatureStick(NoFeatureConfig.CODEC);
		event.getRegistry().register(stick.setRegistryName("stick"));
		stickFeature = stick.configured(IFeatureConfig.NONE)
				.decorated(Placement.NOPE.configured(IPlacementConfig.NONE)).chance(2);
		Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(SevenDaysToMine.MODID,"stick"), stickFeature);

		corn = new FeatureCorn(BlockClusterFeatureConfig.CODEC);
		event.getRegistry().register(corn.setRegistryName("corn"));
		cornFeature = corn.configured((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.BEDROCK.defaultBlockState()), SimpleBlockPlacer.INSTANCE)).xspread(4).yspread(4).zspread(4).canReplace().tries(64).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK.defaultBlockState().getBlock())).noProjection().build())
				.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(4);
		Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(SevenDaysToMine.MODID,"corn"), cornFeature);

		city = new FeatureCity(NoFeatureConfig.CODEC);
		event.getRegistry().register(city.setRegistryName("city"));
		cityFeature = city.configured(IFeatureConfig.NONE)
				.decorated(Placement.NOPE.configured(IPlacementConfig.NONE)).chance(1);
		Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(SevenDaysToMine.MODID,"city"), cityFeature);

		roads = new FeatureRoads(NoFeatureConfig.CODEC);
		event.getRegistry().register(roads.setRegistryName("roads"));
		roadsFeature = roads.configured(IFeatureConfig.NONE)
				.decorated(Placement.NOPE.configured(IPlacementConfig.NONE));
		Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(SevenDaysToMine.MODID,"roads"), roadsFeature);

		cars = new FeatureCars(NoFeatureConfig.CODEC);
		event.getRegistry().register(cars.setRegistryName("cars"));
		carsFeature = cars.configured(IFeatureConfig.NONE)
				.decorated(Placement.NOPE.configured(IPlacementConfig.NONE));
		Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(SevenDaysToMine.MODID,"cars"), carsFeature);

		burntTreeFeatureLarge = Feature.TREE.configured((new BaseTreeFeatureConfig.Builder(
				new SimpleBlockStateProvider(ModBlocks.BURNT_LOG.get().defaultBlockState()),
				new SimpleBlockStateProvider(Blocks.AIR.defaultBlockState()),
				new FancyFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(4), 4),
				new FancyTrunkPlacer(3, 11, 0),
				new TwoLayerFeature(0, 0, 0, OptionalInt.of(4)))).ignoreVines().heightmap(Heightmap.Type.MOTION_BLOCKING).build()).decorated(Features.Placements.HEIGHTMAP).decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(3, 0.5f, 2)));
		Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(SevenDaysToMine.MODID,"burnt_tree_large"), burntTreeFeatureLarge);

		burntTreeFeature = Feature.TREE.configured((new BaseTreeFeatureConfig.Builder(
				new SimpleBlockStateProvider(ModBlocks.BURNT_LOG.get().defaultBlockState()),
				new SimpleBlockStateProvider(Blocks.AIR.defaultBlockState()),
				new BlobFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(0), 3),
				new StraightTrunkPlacer(4, 2, 0),
				new TwoLayerFeature(1, 0, 1))).ignoreVines().build()).decorated(Features.Placements.HEIGHTMAP).decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(4, 0.25f, 2)));
		Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(SevenDaysToMine.MODID,"burnt_tree"), burntTreeFeature);

		burntTrees  = Feature.RANDOM_SELECTOR.configured(new MultipleRandomFeatureConfig(ImmutableList.of(ModFeatures.burntTreeFeature.weighted(0.8F)), ModFeatures.burntTreeFeatureLarge)).decorated(Features.Placements.HEIGHTMAP_SQUARE).decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(2, 0.1F, 1)));
		Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(SevenDaysToMine.MODID,"burnt_trees"), burntTrees);


	}

}
