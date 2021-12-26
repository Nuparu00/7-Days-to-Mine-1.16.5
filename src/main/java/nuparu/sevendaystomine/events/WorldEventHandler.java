package nuparu.sevendaystomine.events;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.IUpgradeable;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IChunkData;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.crafting.scrap.ScrapDataManager;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.init.*;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.BreakSyncTrackingMessage;
import nuparu.sevendaystomine.util.DamageSources;
import nuparu.sevendaystomine.util.VanillaManager;
import nuparu.sevendaystomine.util.VanillaManager.VanillaBlockUpgrade;

public class WorldEventHandler {
	/*
	 * CALLED WHEN MOB MANUALLY "DIGS" A BLOCK --> HANDLES THE BREAK LOGIC
	 */
	@SubscribeEvent
	public void mobDig(MobBreakEvent event) {
		BlockState state = event.state;
		Block block = state.getBlock();
		World world = event.world;
		BlockPos pos = event.pos;

		//Downgrades upgradeable block
		if (state.getBlock() instanceof IUpgradeable) {
			IUpgradeable upgradeable = (IUpgradeable) state.getBlock();
			world.setBlockAndUpdate(pos, upgradeable.getPrev(world, pos, state));
			upgradeable.onDowngrade(world, pos, state);
			return;
		} else {
			VanillaBlockUpgrade upgrade = VanillaManager.getVanillaUpgrade(state);
			if (upgrade != null) {
				world.setBlockAndUpdate(pos, upgrade.getPrev());
				return;
			}
		}

		//Handles double blocks
		if (!(block instanceof DoublePlantBlock)) {
			FluidState fluidstate = world.getFluidState(pos);
			block.removedByPlayer(state, world, pos, null, true, fluidstate);
		} else {
			world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}
		world.sendBlockUpdated(pos, state, Blocks.AIR.defaultBlockState(), 3);
	}

	@SubscribeEvent
	public void onPlayerDig(BlockEvent.BreakEvent event) {
		BlockPos pos = event.getPos();
		IWorld world = event.getWorld();
		BlockState state = event.getState();
		Block block = state.getBlock();

		if (block instanceof IUpgradeable && ((IUpgradeable) block).getPrev(world, pos, state) != null
				&& ((IUpgradeable) block).getPrev(world, pos, state).getBlock() != Blocks.AIR) {
			IUpgradeable upgradeable = (IUpgradeable) state.getBlock();
			event.setCanceled(true);
			world.setBlock(pos, upgradeable.getPrev(world, pos, state),2);
			upgradeable.onDowngrade(world, pos, state);
			Block prev = upgradeable.getPrev(world, pos, state).getBlock();
			if (!(prev instanceof IUpgradeable))
				return;
			for (ItemStack stack : ((IUpgradeable) prev).getItems()) {
				int count = (int) (stack.getCount() * Math.random());
				if (count > 0) {
					ItemStack s = stack.copy();
					s.setCount(count);
				}
			}
			return;
		} else {
			VanillaBlockUpgrade upgrade = VanillaManager.getVanillaUpgrade(state);
			if (upgrade != null && upgrade.getPrev() != null && upgrade.getPrev().getBlock() != Blocks.AIR) {
				event.setCanceled(true);
				world.setBlock(pos, upgrade.getPrev(),2);
				if (upgrade.getPrev().getBlock() instanceof IUpgradeable) {
					ItemStack[] stacks = (upgrade.getPrev().getBlock() instanceof IUpgradeable)
							? (((IUpgradeable) upgrade.getPrev().getBlock()).getItems())
							: ((VanillaManager.getVanillaUpgrade(upgrade.getPrev()) != null)
							? (VanillaManager.getVanillaUpgrade(upgrade.getPrev()).getItems())
							: (null));

					if (stacks != null) {
						for (ItemStack stack : stacks) {
							int count = (int) (stack.getCount() * Math.random());
							if (count > 0) {
								ItemStack s = stack.copy();
								s.setCount(count);
								//event.getDrops().add(s);
							}
						}
					}
				}
				return;
			}
		}
	}

	@SubscribeEvent
	public void onBlockBreakEvent(BlockEvent.BreakEvent event) {
		BlockState oldState = event.getState();
		IWorld world = event.getWorld();
		//Makes player bleed when destroying glass with bare hands
		if (event.getPlayer() instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
			if (!player.isCreative() && !player.isSpectator()) {
				if (player.getMainHandItem().isEmpty()) {
					if (oldState.getMaterial() == Material.GLASS) {
						if (player.level.random.nextInt(5) == 0) {
							player.hurt(DamageSources.sharpGlass, 2.0F);

						}
					}
				}
			}
		}
		//Makes sure to disconnect connections between electrical blocks
		if (world instanceof World) {
			TileEntity te = world.getBlockEntity(event.getPos());
			if (te != null && te instanceof IVoltage) {
				IVoltage voltage = (IVoltage) te;
				List<ElectricConnection> inputs = voltage.getInputs();
				if (inputs == null)
					return;
				for (ElectricConnection connection : inputs) {
					IVoltage from = connection.getFrom((World) world);
					if (from == null)
						continue;
					from.disconnect(voltage);
				}
			}
		}
	}

	@SubscribeEvent
	public void onFurnaceBurnTime(FurnaceFuelBurnTimeEvent event) {
		ItemStack fuel = event.getItemStack();
		if (fuel.isEmpty())
			return;
		Item item = fuel.getItem();
		if (item == Items.PAPER || item == Items.FILLED_MAP || item == Items.MAP) {
			event.setBurnTime(40);
		} else if (item == Items.BOOK) {
			event.setBurnTime(80);
		}
		 if (ScrapDataManager.instance.hasEntry(item)) {
			ScrapDataManager.ScrapEntry scrapEntry = ScrapDataManager.instance.getEntry(item);
			if (scrapEntry.material == EnumMaterial.WOOD) {
				event.setBurnTime(200 * scrapEntry.weight);
			}
		} else if (item == ModItems.CRUDE_BOW.get()) {
			event.setBurnTime(300);
		}
	}

	@SubscribeEvent
	public void onChunkWatchEvent(ChunkWatchEvent event) {
		ServerWorld world = event.getWorld();
		BlockPos pos = event.getPos().getWorldPosition();
		Chunk chunk = world.getChunkAt(pos);
		//Sync damage blocks to the client
		if (chunk != null) {
			IChunkData data = CapabilityHelper.getChunkData(chunk);
			if (data != null) {
				PacketManager.sendTo(PacketManager.blockBreakSyncTracking,
						new BreakSyncTrackingMessage(data.writeToNBT(new CompoundNBT()), pos), event.getPlayer());
			}
		}
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		RegistryKey<Biome> biomeKey = RegistryKey.create(ForgeRegistries.Keys.BIOMES,
				Objects.requireNonNull(event.getName(),
						"Non existing biome detected!"));

		//Entity part
		MobSpawnInfoBuilder spawns = event.getSpawns();
		List<MobSpawnInfo.Spawners> monsterSpawner = spawns.getSpawner(EntityClassification.MONSTER);

		if(BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.NETHER)) {
			if (Biomes.SOUL_SAND_VALLEY == biomeKey) {
				monsterSpawner.add(new MobSpawnInfo.Spawners(ModEntities.SOUL_BURNT_ZOMBIE.get(), CommonConfig.spawnWeightSoulBurntZombie.get(), CommonConfig.spawnMinSoulBurntZombie.get(), CommonConfig.spawnMaxSoulBurntZombie.get()));
			}
			else{
				monsterSpawner.add(new MobSpawnInfo.Spawners(ModEntities.BURNT_ZOMBIE.get(), CommonConfig.spawnWeightBurntZombie.get(), CommonConfig.spawnMinBurntZombie.get(), CommonConfig.spawnMaxBurntZombie.get()));
			}
		}
		else {
			monsterSpawner.add(new MobSpawnInfo.Spawners(ModEntities.REANIMATED_CORPSE.get(), CommonConfig.spawnWeightReanimatedCorpse.get(), CommonConfig.spawnMinReanimatedCorpse.get(), CommonConfig.spawnMaxReanimatedCorpse.get()));
			monsterSpawner.add(new MobSpawnInfo.Spawners(ModEntities.PLAGUED_NURSE.get(), CommonConfig.spawnWeightPlaguedNurse.get(), CommonConfig.spawnMinPlaguedNurse.get(), CommonConfig.spawnMaxPlaguedNurse.get()));

			monsterSpawner.add(new MobSpawnInfo.Spawners(ModEntities.MINER_ZOMBIE.get(), CommonConfig.spawnWeightZombieMiner.get(), CommonConfig.spawnMinZombieMiner.get(), CommonConfig.spawnMaxZombieMiner.get()));
			monsterSpawner.add(new MobSpawnInfo.Spawners(ModEntities.SPIDER_ZOMBIE.get(), CommonConfig.spawnWeightSpiderZombie.get(), CommonConfig.spawnMinSpiderZombie.get(), CommonConfig.spawnMaxSpiderZombie.get()));
			monsterSpawner.add(new MobSpawnInfo.Spawners(ModEntities.CRAWLER_ZOMBIE.get(), CommonConfig.spawnWeightZombieCrawler.get(), CommonConfig.spawnMinZombieCrawler.get(), CommonConfig.spawnMaxZombieCrawler.get()));
			monsterSpawner.add(new MobSpawnInfo.Spawners(ModEntities.INFECTED_SURVIVOR.get(), CommonConfig.spawnWeightInfectedSurvivor.get(), CommonConfig.spawnMinInfectedSurvivor.get(), CommonConfig.spawnMaxInfectedSurvivor.get()));
			monsterSpawner.add(new MobSpawnInfo.Spawners(ModEntities.ZOMBIE_WOLF.get(), CommonConfig.spawnWeightZombieWolf.get(), CommonConfig.spawnMinZombieWolf.get(), CommonConfig.spawnMaxZombieWolf.get()));
			monsterSpawner.add(new MobSpawnInfo.Spawners(ModEntities.ZOMBIE_PIG.get(), CommonConfig.spawnWeightZombiePig.get(), CommonConfig.spawnMinZombiePig.get(), CommonConfig.spawnMaxZombiePig.get()));

			if (BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.SNOWY) || BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.COLD)) {
				monsterSpawner.add(new MobSpawnInfo.Spawners(ModEntities.FROZEN_LUMBERJACK.get(), CommonConfig.spawnWeightFrozenLumberjack.get(), CommonConfig.spawnMinFrozenLumberjack.get(), CommonConfig.spawnMaxFrozenLumberjack.get()));
				monsterSpawner.add(new MobSpawnInfo.Spawners(ModEntities.FRIGID_HUNTER.get(), CommonConfig.spawnWeightFrigidHunter.get(), CommonConfig.spawnMinFrigidHunter.get(), CommonConfig.spawnMaxFrigidHunter.get()));
				monsterSpawner.add(new MobSpawnInfo.Spawners(ModEntities.FROSTBITTEN_WORKER.get(), CommonConfig.spawnWeightFrostbittenWorker.get(), CommonConfig.spawnMinFrostbittenWorker.get(), CommonConfig.spawnMaxFrostbittenWorker.get()));
			}
		}
		//World generation part
		event.getGeneration().getFeatures(GenerationStage.Decoration.SURFACE_STRUCTURES).add(() -> ModFeatures.largeRockFeature);
		event.getGeneration().getFeatures(GenerationStage.Decoration.SURFACE_STRUCTURES).add(() -> ModFeatures.smallStoneFeature);
		event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).add(() -> ModFeatures.berryBushFeature);
		event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_PILLAGER_OUTPOST_RUINED);
		event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_WINDMILL);
		event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_RUINS);
		event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_OBSERVATORY);

		if(!BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.WATER)) {
			if(!BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.MOUNTAIN) && !BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.HILLS)) {
				event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_CITY);
				//event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_VILLAGE);
				event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_MILITARY_BASE);
				event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_AIRPLANE);
				event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_HELICOPTER);
				if (!BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.SANDY)) {
					event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_AIRPFIELD);
				}
			}
			if(!BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.SANDY)) {
				event.getGeneration().getFeatures(GenerationStage.Decoration.SURFACE_STRUCTURES).add(() -> ModFeatures.stickFeature);
			}
		}

		if(!BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.WATER) || BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.RIVER)) {
			event.getGeneration().getFeatures(GenerationStage.Decoration.RAW_GENERATION).add(() -> ModFeatures.roadsFeature);
			event.getGeneration().getFeatures(GenerationStage.Decoration.TOP_LAYER_MODIFICATION).add(() -> ModFeatures.carsFeature);
		}

		if(BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.WATER)) {
			event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_YACHT);
			event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_SUNKEN_YACHT);
			event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_CRUISER);
			event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_CARGO_SHIP);
			event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_CARGO_SHIP_SUNKEN);
		}

		if(biomeKey.location().equals(ModBiomes.WASTELAND_FOREST.getId())){
			//System.out.println("BURNS BURNS BURNS, THE RING OF FIRE");
			event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).add(() -> ModFeatures.burntTreeFeatureLarge.decorated(Features.Placements.HEIGHTMAP).decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(5, 0.25f, 2))));
			event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).add(() -> ModFeatures.burntTreeFeature.decorated(Features.Placements.HEIGHTMAP).decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(5, 0.25f, 2))));
			event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_LANDFILL);
		}


		if(BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.PLAINS) || BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.SAVANNA)) {
			event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).add(() -> ModFeatures.cornFeature);
		}

		if(!event.getCategory().equals(Biome.Category.NETHER) && !event.getCategory().equals(Biome.Category.THEEND)){
			addOreFeature(event.getGeneration(),OreFeatureConfig.FillerBlockType.NATURAL_STONE,ModBlocks.ORE_COPPER.get().defaultBlockState(), 10,32,128,3);
			addOreFeature(event.getGeneration(),OreFeatureConfig.FillerBlockType.NATURAL_STONE,ModBlocks.ORE_CINNABAR.get().defaultBlockState(), 3,70,256,6);
			addOreFeature(event.getGeneration(),OreFeatureConfig.FillerBlockType.NATURAL_STONE,ModBlocks.ORE_LEAD.get().defaultBlockState(), 6,10,80,8);
			addOreFeature(event.getGeneration(),OreFeatureConfig.FillerBlockType.NATURAL_STONE,ModBlocks.ORE_POTASSIUM.get().defaultBlockState(), 10,50,128,3);
			addOreFeature(event.getGeneration(),OreFeatureConfig.FillerBlockType.NATURAL_STONE,ModBlocks.ORE_TIN.get().defaultBlockState(), 5,5,128,8);
			addOreFeature(event.getGeneration(),OreFeatureConfig.FillerBlockType.NATURAL_STONE,ModBlocks.ORE_ZINC.get().defaultBlockState(), 5,5,128,6);
		}

	}

	void addOreFeature(BiomeGenerationSettingsBuilder settings, RuleTest fillerType, BlockState state, int veinSize, int minHeight, int maxHeight, int amount){
		settings.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(fillerType,state,veinSize)).decorated(Placement.RANGE.configured(new TopSolidRangeConfig(minHeight,0,maxHeight))).squared().count(amount));
	}

	private static Method GETCODEC_METHOD;
	@SubscribeEvent
	public void addDimensionalSpacing(final WorldEvent.Load event) {
		if(event.getWorld() instanceof ServerWorld){
			ServerWorld serverWorld = (ServerWorld)event.getWorld();

			/*
			 * Skip Terraforged's chunk generator as they are a special case of a mod locking down their chunkgenerator.
			 * They will handle your structure spacing for your if you add to WorldGenRegistries.NOISE_GENERATOR_SETTINGS in your structure's registration.
			 * This here is done with reflection as this tutorial is not about setting up and using Mixins.
			 * If you are using mixins, you can call the codec method with an invoker mixin instead of using reflection.
			 */
			try {
				if(GETCODEC_METHOD == null) GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
				ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkSource().generator));
				if(cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
			}
			catch(Exception e){
				SevenDaysToMine.LOGGER.error("Was unable to check if " + serverWorld.dimension().location() + " is using Terraforged's ChunkGenerator.");
			}

			/*
			 * Prevent spawning our structure in Vanilla's superflat world as
			 * people seem to want their superflat worlds free of modded structures.
			 * Also that vanilla superflat is really tricky and buggy to work with in my experience.
			 */
			if(serverWorld.getChunkSource().getGenerator() instanceof FlatChunkGenerator &&
					serverWorld.dimension().equals(World.OVERWORLD)){
				return;
			}

			/*
			 * putIfAbsent so people can override the spacing with dimension datapacks themselves if they wish to customize spacing more precisely per dimension.
			 * Requires AccessTransformer  (see resources/META-INF/accesstransformer.cfg)
			 *
			 * NOTE: if you add per-dimension spacing configs, you can't use putIfAbsent as WorldGenRegistries.NOISE_GENERATOR_SETTINGS in FMLCommonSetupEvent
			 * already added your default structure spacing to some dimensions. You would need to override the spacing with .put(...)
			 * And if you want to do dimension blacklisting, you need to remove the spacing entry entirely from the map below to prevent generation safely.
			 */
			Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig());
			tempMap.putIfAbsent(ModStructureFeatures.CITY.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructureFeatures.CITY.get()));
			tempMap.putIfAbsent(ModStructureFeatures.PILLAGER_OUTPOST_RUINED.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructureFeatures.PILLAGER_OUTPOST_RUINED.get()));
			tempMap.putIfAbsent(ModStructureFeatures.WINDMILL.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructureFeatures.WINDMILL.get()));
			tempMap.putIfAbsent(ModStructureFeatures.RUINS.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructureFeatures.RUINS.get()));
			tempMap.putIfAbsent(ModStructureFeatures.OBSERVATORY.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructureFeatures.OBSERVATORY.get()));
			tempMap.putIfAbsent(ModStructureFeatures.AIRPLANE.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructureFeatures.AIRPLANE.get()));
			tempMap.putIfAbsent(ModStructureFeatures.MILITARY_BASE.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructureFeatures.MILITARY_BASE.get()));
			tempMap.putIfAbsent(ModStructureFeatures.HELICOPTER.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructureFeatures.HELICOPTER.get()));
			tempMap.putIfAbsent(ModStructureFeatures.LANDFILL.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructureFeatures.LANDFILL.get()));
			tempMap.putIfAbsent(ModStructureFeatures.YACHT.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructureFeatures.YACHT.get()));
			tempMap.putIfAbsent(ModStructureFeatures.SUNKEN_YACHT.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructureFeatures.SUNKEN_YACHT.get()));
			tempMap.putIfAbsent(ModStructureFeatures.CRUISER.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructureFeatures.CRUISER.get()));
			tempMap.putIfAbsent(ModStructureFeatures.CARGO_SHIP.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructureFeatures.CARGO_SHIP.get()));
			tempMap.putIfAbsent(ModStructureFeatures.CARGO_SHIP_SUNKEN.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructureFeatures.CARGO_SHIP_SUNKEN.get()));
			tempMap.putIfAbsent(ModStructureFeatures.AIRFIELD.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructureFeatures.AIRFIELD.get()));
			//tempMap.putIfAbsent(ModStructureFeatures.VILLAGE.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructureFeatures.VILLAGE.get()));
			serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
		}
	}



	/*@SubscribeEvent
	public static void onLootLoad(LootTableLoadEvent event) {
		if (event.getName().equals(new ResourceLocation("minecraft","blocks/grass"))) {
			event.getTable().addPool(LootPool.lootPool().add(TableLootEntry.lootTableReference(new ResourceLocation(SevenDaysToMine.MODID,"grass"))).build());
		}
	}*/
}