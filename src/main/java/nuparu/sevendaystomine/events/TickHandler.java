package nuparu.sevendaystomine.events;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.advancements.ModTriggers;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.entity.AirdropEntity;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModGameRules;
import nuparu.sevendaystomine.init.ModLootTables;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.potions.Potions;
import nuparu.sevendaystomine.util.*;
import nuparu.sevendaystomine.world.MiscSavedData;
import nuparu.sevendaystomine.world.horde.BloodmoonHorde;
import nuparu.sevendaystomine.world.horde.GenericHorde;
import nuparu.sevendaystomine.world.horde.HordeSavedData;
import nuparu.sevendaystomine.world.horde.ZombieWolfHorde;

import java.util.ArrayList;

public class TickHandler {


    private long nextTorchCheck = 0L;

    public TickHandler() {

    }

    public static void init(Dist side) {
        if (side == Dist.CLIENT) {

            //f_loadShader = ReflectionHelper.findMethod(WorldRenderer.class, "loadShader", "func_175069_a", ResourceLocation.class);

        }
        //f_setSize = ReflectionHelper.findMethod(Entity.class, "setSize", "func_70105_a", float.class, float.class);
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
                    extendedPlayer.consumeThirst(1);
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
                        extendedPlayer.consumeThirst(1);
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

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.world == null || !(event.world instanceof ServerWorld))
            return;
        ServerWorld world = (ServerWorld) event.world;
        MinecraftServer server = world.getServer();

        HordeSavedData.getOrCreate(world).update(world);
        //BreakSavedData.get(world).update(world);

        //System.out.println(NBTUtil.writeBlockState(Blocks.BRICK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)).toString());
        if (world.dimension() != server.overworld().dimension() || world.isClientSide() || CommonConfig.airdropFrequency.get() <= 0
                || event.phase != TickEvent.Phase.START)
            return;
        if (server == null || server.getPlayerList().getPlayerCount() == 0)
            return;

        long time = world.getDayTime() % 24000;
        MiscSavedData miscData = MiscSavedData.getOrCreate(world);

        if (time >= 6000 && miscData.getLastAirdrop() != Utils.getDay(world)
                && Utils.getDay(world) % CommonConfig.airdropFrequency.get() == 0) {
            miscData.setLastAirdrop(Utils.getDay(world));
            BlockPos pos = Utils.getAirdropPos(world);

            //Spawns the airdrop at the world spawn as that one is always loaded
            AirdropEntity e = new AirdropEntity(world, world.getSharedSpawnPos().above(255));

            LootTable loottable = server.getLootTables().get(ModLootTables.AIRDROP);
            LootContext.Builder lootcontext$builder = (new LootContext.Builder(world)).withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf(pos));

            ItemUtils.fill(loottable,e.getInventory(), lootcontext$builder.create(LootParameterSets.CHEST));

            world.addFreshEntity(e);
            //Move the airdrop to the actual position
            e.setPos(pos.getX(), pos.getY(), pos.getZ());
            int x = pos.getX() + MathUtils.getIntInRange(world.random, 32, 128) * (world.random.nextBoolean() ? 1 : -1);
            int z = pos.getZ() + MathUtils.getIntInRange(world.random, 32, 128) * (world.random.nextBoolean() ? 1 : -1);
            server.getPlayerList().broadcastMessage(new TranslationTextComponent("airdrop.message",
                    x,
                    z), ChatType.GAME_INFO, Util.NIL_UUID);

            server.getPlayerList().broadcastMessage(new TranslationTextComponent("airdrop.message",
                    x,
                    z), ChatType.CHAT, Util.NIL_UUID);
        }

    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            PlayerEntity player = event.player;
            if (player == null || !player.isAlive())
                return;
            World world = player.level;
            if (world == null) return;

            IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
            if (player instanceof ServerPlayerEntity && !player.isCreative() && !player.isSpectator()) {
                if (world.getGameRules().getBoolean(ModGameRules.handleThirst)) {
                    handleExtendedPlayer(player, world, extendedPlayer);
                }
                IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
                ServerPlayerEntity playerMP = (ServerPlayerEntity) player;
                long time = world.getDayTime() % 24000;
                int day = Utils.getDay(world);
				if (!world.isClientSide() && world.getDifficulty() != Difficulty.PEACEFUL) {
					if (Utils.isBloodmoon(world) && time > 13000 && time < 23000) {

						if (iep.getBloodmoon() != day) {
							BlockPos pos = playerMP.blockPosition();
							BloodmoonHorde horde = new BloodmoonHorde(pos, (ServerWorld) world, playerMP);
							horde.addTarget(playerMP);
							horde.start();
							iep.setBloodmoon(day);

							world.playSound(null, pos, ModSounds.HORDE.get(), SoundCategory.HOSTILE,
									world.random.nextFloat() * 0.1f + 0.95f, world.random.nextFloat() * 0.1f + 0.95f);
						}

					} else if (time > 1000 && time < 1060 && iep.getWolfHorde() != day && Utils.isWolfHorde(world)) {

						ZombieWolfHorde horde = new ZombieWolfHorde(player.blockPosition(), (ServerWorld)world, player);
						horde.addTarget(playerMP);
						horde.start();
						iep.setWolfHorde(day);
					} else if (day != 1 && !iep.hasHorde(world)) {
						/*CitySavedData csd = CitySavedData.get(world);
						CityData city = csd.getClosestCity(player.blockPosition(), 100);*/

						if (world.random.nextDouble() < CommonConfig.genericHordeChance.get()
								/* (city == null ? 1 : (1 + ((10 * city.getZombieLevel() / 1024f))))*/) {
							GenericHorde horde = new GenericHorde(player.blockPosition(), (ServerWorld)world, player);
							/*if (city != null && city.getZombieLevel() > 0) {
								city.setZombieLevel(city.getZombieLevel() - (horde.waves * horde.getZombiesInWave()));
							}*/
							horde.addTarget(playerMP);
							horde.start();
							iep.setHorde(day);
						}
					}
				}
                if (Utils.isBloodmoon(day - 1) && time < 1000 && iep.getLastBloodmoonSurvivalCheck() < day) {
                    ModTriggers.BLOODMOON_SURVIVAL.trigger(playerMP, o -> true);
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
                nextTorchCheck = System.currentTimeMillis() + 1000L;
            }
        }
    }

}
