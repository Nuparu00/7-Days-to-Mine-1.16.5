package nuparu.sevendaystomine.events;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.client.util.CameraHelper;
import nuparu.sevendaystomine.config.ClientConfig;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModGameRules;
import nuparu.sevendaystomine.potions.Potions;
import nuparu.sevendaystomine.util.DamageSources;
import nuparu.sevendaystomine.util.PlayerUtils;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.MiscSavedData;

public class TickHandler {
	public static float recoil = 0;
	public static float antiRecoil = 0;
	public static int time = 0;
	public static int useCount = 0;

	@OnlyIn(Dist.CLIENT)
	public static int windCounter;

	@OnlyIn(Dist.CLIENT)
	public static int beat;

	@OnlyIn(Dist.CLIENT)
	private static ResourceLocation bleedShaderRes;
	@OnlyIn(Dist.CLIENT)
	private static ResourceLocation nightShaderRes;
	@OnlyIn(Dist.CLIENT)
	public static ResourceLocation drunkShaderRes;

	@OnlyIn(Dist.CLIENT)
	public static Method f_loadShader;

	private static Method f_setSize;

	@OnlyIn(Dist.CLIENT)
	private static Minecraft mc;

	@OnlyIn(Dist.CLIENT)
	public static boolean bloodmoon;
	@OnlyIn(Dist.CLIENT)
	public static Field f_MOON_PHASES_TEXTURES;
	@OnlyIn(Dist.CLIENT)
	public static ResourceLocation bloodmoon_texture;
	@OnlyIn(Dist.CLIENT)
	public static ResourceLocation default_moon_texture;

	private long nextTorchCheck = 0l;

	@SuppressWarnings("deprecation")
	public static void init(Dist side) {
		if (side == Dist.CLIENT) {
			bloodmoon_texture = new ResourceLocation(SevenDaysToMine.MODID, "textures/environment/moon_phases.png");
			default_moon_texture = new ResourceLocation("textures/environment/moon_phases.png");

			//f_loadShader = ReflectionHelper.findMethod(WorldRenderer.class, "loadShader", "func_175069_a", ResourceLocation.class);
			mc = Minecraft.getInstance();

			windCounter = 10;
			beat = 999999;

			bleedShaderRes = new ResourceLocation(SevenDaysToMine.MODID + ":shaders/post/blur_bleed.json");
			nightShaderRes = new ResourceLocation(SevenDaysToMine.MODID + ":shaders/post/night.json");
			drunkShaderRes = new ResourceLocation(SevenDaysToMine.MODID + ":shaders/post/drunk.json");

		}
		//f_setSize = ReflectionHelper.findMethod(Entity.class, "setSize", "func_70105_a", float.class, float.class);
	}

	public TickHandler() {

	}

	@SuppressWarnings("deprecation")
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onRenderTick(TickEvent.RenderTickEvent event) {
		if (!ClientConfig.postprocessingShaders.get())
			return;

	}

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		if (event.world == null || !(event.world instanceof ServerWorld))
			return;
		ServerWorld world = (ServerWorld) event.world;
		MinecraftServer server = world.getServer();

		//HordeSavedData.getOrCreate(world).update(world);
		//BreakSavedData.get(world).update(world);

		//System.out.println(NBTUtil.writeBlockState(Blocks.BRICK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)).toString());
		if (world.dimension() != server.overworld().dimension() || world.isClientSide() || CommonConfig.airdropFrequency.get() <= 0
				|| event.phase != TickEvent.Phase.START)
			return;
		if (server == null || server.getPlayerList().getPlayerCount() == 0)
			return;

		long time = world.getDayTime() % 24000;
		MiscSavedData miscData = MiscSavedData.getOrCreate(world);

		/*if (time >= 6000 && miscData.getLastAirdrop() != Utils.getDay(world)
				&& Utils.getDay(world) % ModConfig.world.airdropFrequency == 0) {
			miscData.setLastAirdrop(Utils.getDay(world));
			BlockPos pos = Utils.getAirdropPos(world);

			EntityAirdrop e = new EntityAirdrop(world, world.getSharedSpawnPos().above(255));
			world.addFreshEntity(e);
			e.setPos(pos.getX(), pos.getY(), pos.getZ());
			server.getPlayerList().broadcastMessage(new TranslationTextComponent("airdrop.message",
					pos.getX() + MathUtils.getIntInRange(world.random, 32, 128) * (world.random.nextBoolean() ? 1 : -1),
					pos.getZ() + MathUtils.getIntInRange(world.random, 32, 128) * (world.random.nextBoolean() ? 1 : -1)),ChatType.GAME_INFO,Util.NIL_UUID);
		}*/

	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			PlayerEntity player = event.player;
			if (player == null || !player.isAlive())
				return;

			World world = player.level;

			IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
			if (player instanceof ServerPlayerEntity && !player.isCreative() && !player.isSpectator()) {
				if (world.getGameRules().getBoolean(ModGameRules.handleThirst)) {
					handleExtendedPlayer(player, world, extendedPlayer);
				}
				IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
				ServerPlayerEntity playerMP = (ServerPlayerEntity) player;
				long time = world.getDayTime() % 24000;
				int day = Utils.getDay(world);
				/*if (!world.isClientSide() && world.getDifficulty() != Difficulty.PEACEFUL) {
					if (Utils.isBloodmoon(world) && time > 13000 && time < 23000) {

						if (iep.getBloodmoon() != day) {
							BlockPos pos = playerMP.blockPosition();
							BloodmoonHorde horde = new BloodmoonHorde(pos, world, playerMP);
							horde.addTarget(playerMP);
							horde.start();
							iep.setBloodmoon(day);

							world.playSound(null, pos, ModSounds.HORDE, SoundCategory.HOSTILE,
									world.random.nextFloat() * 0.1f + 0.95f, world.random.nextFloat() * 0.1f + 0.95f);
						}

					} else if (time > 1000 && time < 1060 && iep.getWolfHorde() != day && Utils.isWolfHorde(world)) {

						ZombieWolfHorde horde = new ZombieWolfHorde(player.blockPosition(), world, player);
						horde.addTarget(playerMP);
						horde.start();
						iep.setWolfHorde(day);
					} else if (!iep.hasHorde(world)) {
						CitySavedData csd = CitySavedData.get(world);
						CityData city = csd.getClosestCity(player.blockPosition(), 100);

						if (world.random.nextDouble() < ModConfig.world.genericHordeChance
								* (city == null ? 1 : (1 + ((10 * city.getZombieLevel() / 1024f))))) {
							GenericHorde horde = new GenericHorde(player.blockPosition(), world, player);
							if (city != null && city.getZombieLevel() > 0) {
								city.setZombieLevel(city.getZombieLevel() - (horde.waves * horde.getZombiesInWave()));
							}
							horde.addTarget(playerMP);
							horde.start();
							iep.setHorde(day);
						}
					}*
				}*/
				if (Utils.isBloodmoon(day - 1) && time < 1000 && iep.getLastBloodmoonSurvivalCheck() < day) {
					//ModTriggers.BLOODMOON_SURVIVAL.trigger(playerMP);
					iep.setLastBloodmoonSurvivalCheck(day);
				}
			}
			if (extendedPlayer.isInfected()) {
				int time = extendedPlayer.getInfectionTime();
				extendedPlayer.setInfectionTime(time + 1);
				EffectInstance effect = player.getEffect(Potions.INFECTION.get());
				int amplifier = PlayerUtils.getInfectionStage(time);
				int timeLeft = PlayerUtils.getCurrentInectionStageRemainingTime(time);

				if (effect == null || effect.getAmplifier() != amplifier) {
					player.addEffect(new EffectInstance(Potions.INFECTION.get(), Math.min(24000, timeLeft), amplifier));
				}

				if (amplifier == PlayerUtils.getNumberOfstages() - 1) {
					player.hurt(DamageSources.infection, 1);
				}
			}

			// Changes vanilla torches to enhanced torches every x seconds
			if (System.currentTimeMillis() > nextTorchCheck) {
				for (int i = 0; i < player.inventory.items.size(); i++) {
					ItemStack s = player.inventory.items.get(i);
					if (s != null && s.getItem() == Item.byBlock(Blocks.TORCH)) {
						player.inventory.items.set(i, new ItemStack(ModBlocks.TORCH_LIT.get(), s.getCount()));
					}
				}
				for (int i = 0; i < player.inventory.armor.size(); i++) {
					ItemStack s = player.inventory.armor.get(i);
					if (s != null && s.getItem() == Item.byBlock(Blocks.TORCH)) {
						player.inventory.armor.set(i, new ItemStack(ModBlocks.TORCH_LIT.get(), s.getCount()));
					}
				}
				nextTorchCheck = System.currentTimeMillis() + 1000l;
			}
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onClientTick(TickEvent.ClientTickEvent event) {

		if (event.phase == TickEvent.Phase.START) {
			PlayerEntity player = mc.player;

			if (player == null)
				return;
			World world = player.level;

			if (recoil > 0) {
				recoil *= 0.8F;

				player.xRot -= recoil / 2;
				if (useCount < 25) {
					antiRecoil += recoil / 2;
					player.xRot += antiRecoil * 0.1F;
				}
				antiRecoil *= 0.8F;
			}

			IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
			if (iep != null && iep.getStamina() <= 0) {
				KeyBinding. set(mc.options.keySprint.getKey(), false);
				player.setSprinting(false);
			}
			if (ClientConfig.burntForestParticles.get()) {
				for (int l = 0; l < 500 * SevenDaysToMine.proxy.getParticleLevel(); ++l) {
					int i1 = MathHelper.floor(player.getX()) + world.random.nextInt(16) - world.random.nextInt(16);
					int j1 = MathHelper.floor(player.getY()) + world.random.nextInt(16) - world.random.nextInt(16);
					int k1 = MathHelper.floor(player.getZ()) + world.random.nextInt(16) - world.random.nextInt(16);
					BlockPos pos = new BlockPos(i1, j1, k1);
					Biome biome = world.getBiome(pos);

					/*if ((biome instanceof BiomeWastelandBase) && ((BiomeWastelandBase) biome).floatingParticles()) {
						if (world.random.nextInt(8) > Math.abs(world.getHeight(pos).getY() - j1)) {
							BlockState block = world.getBlockState(pos);

							if (block.getMaterial() == Material.AIR) {

								world.addParticle(ParticleTypes.ASH,
										(double) ((float) i1 + world.random.nextFloat()),
										(double) ((float) j1 + world.random.nextFloat()),
										(double) ((float) k1 + world.random.nextFloat()), 0.0D, -1D, 0.0D);
							}
						}
					}*/
				}
			}
			if (ClientEventHandler.takingPhoto) {
				CameraHelper.INSTANCE.saveScreenshot(mc.getWindow().getScreenWidth(), mc.getWindow().getScreenHeight(), mc.getMainRenderTarget(), player);
				ClientEventHandler.takingPhoto = false;
			}

		}

	}

	public static void handleExtendedPlayer(PlayerEntity player, World world, IExtendedPlayer extendedPlayer) {
		if (world.isClientSide()) {
			return;
		}

		if (world.getDifficulty() == Difficulty.PEACEFUL) {
			extendedPlayer.setThirst(1000);
			extendedPlayer.setStamina(1000);
			extendedPlayer.setDrinkCounter(0);
			return;
		}

		if (CommonConfig.thirstSystem.get()) {
			if (extendedPlayer.getDrinkCounter() >= 20) {
				extendedPlayer.setDrinkCounter(0);
				extendedPlayer.addThirst(35);
				extendedPlayer.addStamina(20);
				player.removeEffect(Potions.THIRST.get());

				if (world.random.nextInt(10) == 0) {
					EffectInstance effect = new EffectInstance(Potions.DYSENTERY.get(), world.random.nextInt(4000) + 18000, 0,
							false, false);
					effect.setCurativeItems(new ArrayList<ItemStack>());
					player.addEffect(effect);
				}
			} else if (extendedPlayer.getDrinkCounter() > 0) {
				extendedPlayer.setDrinkCounter(extendedPlayer.getDrinkCounter() - 1);
			}

			if (extendedPlayer.getThirst() > 0) {
				if (world.random.nextInt(25) == 0) {
					extendedPlayer.consumeThirst((int) 1);
				}
			}
			if (extendedPlayer.getThirst() <= 0) {
				EffectInstance effect = new EffectInstance(Potions.THIRST.get(), 4, 4, false, false);
				effect.setCurativeItems(new ArrayList<ItemStack>());
				player.addEffect(effect);
			}
		}

		if (CommonConfig.staminaSystem.get()) {
			if (player.isSprinting()) {
				if (CommonConfig.staminaSystem.get() && extendedPlayer.getStamina() > 0) {
					if (world.random.nextInt(3) == 0) {
						extendedPlayer.consumeStamina(2);
					}

					if (CommonConfig.thirstSystem.get() && world.random.nextInt(35) == 0) {
						extendedPlayer.consumeThirst((int) 1);
					}
				}

			} else if ((extendedPlayer.getThirst() >= 100 || !CommonConfig.thirstSystem.get())
					&& world.random.nextInt(8) == 0
					&& player.walkDist - player.walkDistO <= 0.05) {
				extendedPlayer.addStamina(1);
			}

			if (extendedPlayer.getStamina() <= 0) {
				player.setSprinting(false);
			}
		}

	}

}
