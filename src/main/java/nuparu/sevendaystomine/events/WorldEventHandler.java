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
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
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
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.init.ModConfiguredStructures;
import nuparu.sevendaystomine.init.ModFeatures;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.init.ModStructureFeatures;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.item.IScrapable;
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

		if (!(block instanceof DoublePlantBlock)) {
			FluidState fluidstate = world.getFluidState(pos);
			block.removedByPlayer(state, world, pos, (PlayerEntity) null, true, fluidstate);
		} else {
			world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}
		world.sendBlockUpdated(pos, state, Blocks.AIR.defaultBlockState(), 3);
	}

	@SubscribeEvent
	public void onBlockBreakEvent(BlockEvent.BreakEvent event) {
		BlockState oldState = event.getState();
		IWorld world = event.getWorld();
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
			// BreakSavedData.get(world).removeBreakData(event.getPos(), world);
		}
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

	/*
	 * CALLED ON WORLD LOAD - HANDLES INIIAL LOADING OF WORLD SAVED DATA
	 */
	@SubscribeEvent
	public void loadWorld(WorldEvent.Load event) {

	}

	/*
	 * Syncs block damage
	 */
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		World world = event.getPlayer().level;
		if (event.getPlayer() instanceof ServerPlayerEntity && !world.isClientSide()) {
			ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
			// BreakSavedData.get(world).sync(player);
		}
	}

	@SubscribeEvent
	public void onPlayLoundSoundAtEntity(LoudSoundEvent event) {
		if (event.pos != null) {
			float range = event.volume * 6.4f;
			AxisAlignedBB aabb = new AxisAlignedBB(event.pos).inflate(range, range / 2, range);
			List<Entity> entities = event.world.getEntitiesOfClass(Entity.class, aabb);
			/*
			 * for (Entity e : entities) { if (e instanceof INoiseListener) { INoiseListener
			 * noiseListener = (INoiseListener) e; noiseListener.addNoise(new Noise(null,
			 * event.pos, event.world, event.volume, 1)); } }
			 */
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
		if (item instanceof IScrapable) {
			IScrapable scrapable = (IScrapable) item;
			if (scrapable.getItemMaterial() == EnumMaterial.WOOD) {
				event.setBurnTime(200 * scrapable.getWeight());
			}
		} else if (item instanceof BlockItem) {
			BlockItem itemBlock = (BlockItem) item;
			Block block = itemBlock.getBlock();
			if (block instanceof IScrapable) {
				IScrapable scrapable = (IScrapable) block;
				if (scrapable.getItemMaterial() == EnumMaterial.WOOD) {
					event.setBurnTime(200 * scrapable.getWeight());
				}
			}
		} else if (VanillaManager.getVanillaScrapable(item) != null) {
			VanillaManager.VanillaScrapableItem scrapable = VanillaManager.getVanillaScrapable(item);
			if (scrapable.getMaterial() == EnumMaterial.WOOD) {
				event.setBurnTime(200 * scrapable.getWeight());
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

		event.getGeneration().getFeatures(GenerationStage.Decoration.SURFACE_STRUCTURES).add(() -> ModFeatures.largeRockFeature);
		event.getGeneration().getFeatures(GenerationStage.Decoration.SURFACE_STRUCTURES).add(() -> ModFeatures.smallStoneFeature);
		event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).add(() -> ModFeatures.berryBushFeature);
		event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_PILLAGER_OUTPOST_RUINED);
		event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_WINDMILL);
		event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_RUINS);

		if(!BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.WATER)) {
			if(!BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.MOUNTAIN) && !BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.HILLS)) {
				event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_CITY);
			}
			if(!BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.SANDY)) {
				event.getGeneration().getFeatures(GenerationStage.Decoration.SURFACE_STRUCTURES).add(() -> ModFeatures.stickFeature);
			}
		}
		if(BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.PLAINS) || BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.SAVANNA)) {
			event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).add(() -> ModFeatures.cornFeature);
		}

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
			serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
		}
	}


}