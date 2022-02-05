package nuparu.sevendaystomine.events;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.entity.TwistedZombieEntity;
import nuparu.sevendaystomine.entity.VehicleEntity;
import nuparu.sevendaystomine.entity.ZombieBaseEntity;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.SpawnBloodMessage;
import nuparu.sevendaystomine.potions.Potions;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.PlayerUtils;
import nuparu.sevendaystomine.util.Utils;

import java.util.ArrayList;
import java.util.Iterator;

public class LivingEventHandler {

    public static int getArmorItemsCount(LivingEntity living) {
        return (living.getItemBySlot(EquipmentSlotType.HEAD).isEmpty() ? 0 : 2)
                + (living.getItemBySlot(EquipmentSlotType.CHEST).isEmpty() ? 0 : 3)
                + (living.getItemBySlot(EquipmentSlotType.LEGS).isEmpty() ? 0 : 2)
                + (living.getItemBySlot(EquipmentSlotType.FEET).isEmpty() ? 0 : 1);
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        World world = livingEntity.level;
        if (livingEntity.getHealth() < livingEntity.getMaxHealth()
                && world.getBlockState(livingEntity.blockPosition()).getBlock() == ModBlocks.MERCURY.get()) {
            livingEntity.addEffect(new EffectInstance(Potions.MERCURY_POISON.get(), 240));
        }
    }

    @SubscribeEvent
    public void onLivingFinished(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntityLiving() instanceof ServerPlayerEntity && event.getItem().getItem() == Items.HONEY_BOTTLE) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();
            IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
            int time = iep.getInfectionTime();
            int stage = PlayerUtils.getInfectionStage(time);
            if (stage <= 1) {
                iep.setInfectionTime(-1);
                player.removeEffect(Potions.INFECTION.get());
            }
        }
    }


    @SubscribeEvent
    public void onLivingDrop(LivingDropsEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (living instanceof AnimalEntity) {
            DamageSource source = event.getSource();
            if (source.getDirectEntity() instanceof ZombieBaseEntity) {
                Iterator<ItemEntity> it = event.getDrops().iterator();
                while (it.hasNext()) {
                    ItemEntity entity = it.next();
                    if (entity.getItem().getItem().getFoodProperties() != null) {
                        it.remove();
                    }

                }
            }

        }
    }

    @SubscribeEvent
    public void onEntityAttack(LivingAttackEvent event) {
        DamageSource source = event.getSource();
        float amount = event.getAmount();
        LivingEntity entity = event.getEntityLiving();
        World world = entity.level;


        if (entity instanceof VehicleEntity)
            return;

        if (entity.isInvulnerable()) {
            return;
        }
        if (amount < 2)
            return;

        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (player.isCreative() || player.isSpectator()) {
                return;
            }
            if (source.getEntity() instanceof ZombieBaseEntity && world.random
                    .nextInt(CommonConfig.infectionChanceModifier.get() * (getArmorItemsCount(player) + 1)) == 0) {
                Utils.infectPlayer(player, 0);
            } else if (source.getEntity() instanceof TwistedZombieEntity && !world.isClientSide() && world.random
                    .nextInt(1) == 0) {
                EffectInstance effect = new EffectInstance(Potions.FUNGAL_INFECTION.get(), MathUtils.getIntInRange(world.random, 240, 600), 1, false, false);
                effect.setCurativeItems(new ArrayList<ItemStack>());
                entity.addEffect(effect);
            }

        }

        if (source == DamageSource.DROWN || source == DamageSource.FALL || source == DamageSource.HOT_FLOOR
                || source == DamageSource.ON_FIRE || source == DamageSource.OUT_OF_WORLD
                || source == DamageSource.STARVE || source == DamageSource.WITHER || source == DamageSource.MAGIC) {
            return;
        }

        if (entity instanceof AbstractSkeletonEntity || entity instanceof SquidEntity || entity instanceof SnowGolemEntity
                || entity instanceof GuardianEntity || entity instanceof WitherEntity || entity instanceof EnderDragonEntity
                || entity instanceof BlazeEntity || entity instanceof VexEntity || entity instanceof SlimeEntity
                || entity instanceof IronGolemEntity || entity instanceof EndermanEntity
                || entity instanceof EndermiteEntity) {
            return;
        }

        if (world.getDifficulty() != Difficulty.PEACEFUL && !world.isClientSide()
                && amount >= (entity.getMaxHealth() / 100 * 20)
                && world.random.nextInt(ServerConfig.bleedingChanceModifier.get() * (getArmorItemsCount(entity) + 1)) == 0) {
            EffectInstance effect = new EffectInstance(Potions.BLEEDING.get(), Integer.MAX_VALUE, 1, false, false);
            effect.setCurativeItems(new ArrayList<ItemStack>());
            entity.addEffect(effect);
        }

    }

    @SubscribeEvent
    public void onEntityFall(LivingFallEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (ServerConfig.fragileLegs.get() && living instanceof PlayerEntity && !living.level.isClientSide()) {
            if (event.getDistance() > 4
                    && living.level.random.nextFloat() * (Math.max(256 - (event.getDistance() * 25), 0)) <= 1) {
                living.removeEffect(Potions.SPLINTED_LEG.get());
                EffectInstance effect = new EffectInstance(Potions.BROKEN_LEG.get(),
                        (int) (MathUtils.getFloatInRange(0.5f, 1.5f) * (500 * event.getDistance())), 0, false, false);
                effect.setCurativeItems(new ArrayList<ItemStack>());
                living.addEffect(effect);
            }
        }

    }

    @SubscribeEvent
    public void onEntityHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        float amount = event.getAmount();
        LivingEntity entity = event.getEntityLiving();
        World world = entity.level;

        if (entity instanceof SlimeEntity || entity instanceof SkeletonEntity) return;

        if (entity instanceof MobEntity || entity instanceof PlayerEntity) {
            if (amount > 4 && !source.isFire() && !source.isBypassInvul() && source != DamageSource.FALL) {
                for (int i = 0; i < MathUtils.getIntInRange(world.random, 40, 80); i++) {
                /*double x = entity.getX() + MathUtils.getDoubleInRange(-1, 1) * entity.getBbWidth();
                double y = entity.getY() + MathUtils.getDoubleInRange(0, 1) * entity.getBbHeight();
                double z = entity.getZ() + MathUtils.getDoubleInRange(-1, 1) * entity.getBbWidth();
                SevenDaysToMine.proxy.addParticle(world, EnumModParticleType.BLOOD, x, y, z,
                        MathUtils.getDoubleInRange(-1d, 1d) / 7d, MathUtils.getDoubleInRange(-0.5d, 1d) / 7d,
                        MathUtils.getDoubleInRange(-1d, 1d) / 7d);*/

                    //world.addParticle(ModParticleTypes.BLOOD.get(),entity.getRandomX(1),entity.getRandomY(),entity.getRandomZ(1),MathUtils.getFloatInRange(-0.3f,0.3f),MathUtils.getFloatInRange(0.1f,0.25f),MathUtils.getFloatInRange(-0.3f,0.3f));
                    PacketManager.sendToTrackingEntity(PacketManager.spawnBlood, new SpawnBloodMessage(entity.getX(0.5), entity.getY() + entity.getBbHeight() * MathUtils.getFloatInRange(0.4f, 0.75f), entity.getZ(0.5), MathUtils.getFloatInRange(-0.1f, 0.1f), MathUtils.getFloatInRange(0.1f, 0.22f), MathUtils.getFloatInRange(-0.1f, 0.1f)), () -> entity);
                }
            }
        }
		/*LivingEntity living = event.getEntityLiving();
		if (living instanceof PlayerEntity || living instanceof EntityHuman || living instanceof VillagerEntity) {
			World world = living.level;
			List<EntityZombieBase> list = world.getEntitiesOfClass(EntityZombieBase.class,
					new AxisAlignedBB(living.getX(), living.getY(), living.getZ(), living.getX() + 1, living.getY() + 1,
							living.getZ() + 1).inflate(16, 16, 16));
			for (EntityZombieBase zombie : list) {
				if (zombie.getTarget() == null) {
					zombie.setTarget(living);
				}
			}
		}*/
    }


    @SubscribeEvent
    public void onLivingSpawn(LivingSpawnEvent.SpecialSpawn event) {
        if (ServerConfig.removeVanillaZommbies.get() && (event.getEntityLiving() instanceof HuskEntity || event.getEntityLiving() instanceof SkeletonEntity || event.getEntityLiving() instanceof StrayEntity)) {
            event.setCanceled(true);
        }

    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        /*if (event.getEntity() instanceof ZombieEntity) {
            event.setCanceled(true);
            return;
        }*/

        if (!ServerConfig.zombiesAttackAnimals.get())
            return;
        Entity entity = event.getEntity();
        if (entity instanceof AnimalEntity || entity instanceof VillagerEntity) {
            CreatureEntity creature = (CreatureEntity) entity;
            creature.goalSelector.addGoal(3,
                    new AvoidEntityGoal(creature, ZombieBaseEntity.class, 6.0F, 1.0D, 1.2D));
        }
		/*if (entity instanceof CreatureEntity) {
			CreatureEntity creature = (CreatureEntity) entity;
			Set<EntityAITaskEntry> tasks = new LinkedHashSet<EntityAITaskEntry>(creature.targetTasks.taskEntries);
			Iterator<EntityAITaskEntry> it = tasks.iterator();
			while (it.hasNext()) {
				EntityAITaskEntry entry = it.next();
				if(entry.action instanceof 	NearestAttackableTargetGoal<>) {
					NearestAttackableTargetGoal<><?> ai = (NearestAttackableTargetGoal<><?>)entry.action;
					Class<?> targetClass = ObfuscationReflectionHelper.getPrivateValue(NearestAttackableTargetGoal<>.class, ai, "field_75307_b");
					if(targetClass == PlayerEntity.class || targetClass == ServerPlayerEntity.class) {
					    boolean shouldCheckSight = ObfuscationReflectionHelper.getPrivateValue(EntityAITarget.class, ai, "field_75297_f");
					    boolean nearbyOnly = ObfuscationReflectionHelper.getPrivateValue(EntityAITarget.class, ai, "field_75303_a");
						creature.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(creature, EntityHuman.class, shouldCheckSight,nearbyOnly));
					}

				}
			}

		}*/

    }

    @SubscribeEvent
    public void onEnttiyAttributeCreation(EntityAttributeCreationEvent event) {

    }

}
