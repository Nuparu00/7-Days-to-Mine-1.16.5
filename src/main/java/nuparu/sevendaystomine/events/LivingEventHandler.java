package nuparu.sevendaystomine.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.monster.GuardianEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.potions.Potions;
import nuparu.sevendaystomine.util.EnumModParticleType;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.entity.ZombieBaseEntity;

public class LivingEventHandler {

	@SubscribeEvent
	public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
		/*LivingEntity livingEntity = event.getEntityLiving();
		World world = livingEntity.level;
		if (livingEntity.getHealth() < livingEntity.getMaxHealth()
				&& world.getBlockState(new BlockPos(livingEntity)).getBlock() instanceof BlockMercury) {
			livingEntity.addEffect(new EffectInstance(Potions.MERCURY_POISON.get(), 240));
		}*/
	}

	@SubscribeEvent
	public void onEntityAttack(LivingAttackEvent event) {
		DamageSource source = event.getSource();
		float amount = event.getAmount();
		LivingEntity entity = event.getEntityLiving();
		World world = entity.level;
/*
		if (entity instanceof EntityMinibike || entity instanceof EntityCar)
			return;
*/
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
			if (source.getEntity() != null && source.getEntity() instanceof ZombieBaseEntity) {
				if (world.random
						.nextInt(CommonConfig.infectionChanceModifier.get() * (getArmorItemsCount(player) + 1)) == 0) {
					Utils.infectPlayer(player, 0);
				}
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
				&& world.random.nextInt(CommonConfig.bleedingChanceModifier.get() * (getArmorItemsCount(entity) + 1)) == 0) {
			EffectInstance effect = new EffectInstance(Potions.BLEEDING.get(), Integer.MAX_VALUE, 1, false, false);
			effect.setCurativeItems(new ArrayList<ItemStack>());
			entity.addEffect(effect);
		}
		if (amount > 0) {
			for (int i = 0; i < (int) Math
					.round(MathUtils.getDoubleInRange(1, 5) * SevenDaysToMine.proxy.getParticleLevel()); i++) {
				double x = entity.getX() + MathUtils.getDoubleInRange(-1, 1) * entity.getBbWidth();
				double y = entity.getY() + MathUtils.getDoubleInRange(0, 1) * entity.getBbHeight();
				double z = entity.getZ() + MathUtils.getDoubleInRange(-1, 1) * entity.getBbWidth();
				SevenDaysToMine.proxy.addParticle(world, EnumModParticleType.BLOOD, x, y, z,
						MathUtils.getDoubleInRange(-1d, 1d) / 7d, MathUtils.getDoubleInRange(-0.5d, 1d) / 7d,
						MathUtils.getDoubleInRange(-1d, 1d) / 7d);
			}
		}
	}

	@SubscribeEvent
	public void onEntityFall(LivingFallEvent event) {
		LivingEntity living = event.getEntityLiving();
		if (CommonConfig.fragileLegs.get() && living instanceof PlayerEntity && !living.level.isClientSide()) {
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
		LivingEntity living = event.getEntityLiving();
		if (living.level.isClientSide())
			return;/*
		if (living instanceof EntityBandit && living.level.random.nextInt(30) == 0) {
			HorseEntity horse = new HorseEntity(living.level);
			horse.setPos(living.getX(), living.getY(), living.getZ());
			horse.setYBodyRot(living.yRot);
			horse.setTamed(true);

			living.level.addFreshEntity(horse);
			living.startRiding(horse);
			horse.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElseGet(null).insertItem(0,
				new ItemStack(Items.SADDLE), false);

		}*/
	}

	@SubscribeEvent
	public void onLivingDrop(LivingDropsEvent event) {
		LivingEntity living = event.getEntityLiving();
		if (living instanceof AnimalEntity) {
			DamageSource source = event.getSource();
			/*if (source.getDirectEntity() instanceof EntityZombieBase) {
				Iterator<ItemEntity> it = event.getDrops().iterator();
				while (it.hasNext()) {
					ItemEntity entity = it.next();
					if (entity.getItem().getItem().isEdible()) {
						it.remove();
					}

				}
			}*/

		}
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
/*
		if (!ModConfig.mobs.zombiesAttackAnimals)
			return;
		Entity entity = event.getEntity();
		if (entity instanceof AnimalEntity || entity instanceof VillagerEntity) {
			CreatureEntity creature = (CreatureEntity) entity;
			creature.goalSelector.addGoal(3,
					new EntityAIAvoidEntity<EntityZombieBase>(creature, EntityZombieBase.class, 6.0F, 1.0D, 1.2D));
		}
		if (entity instanceof CreatureEntity) {
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

	public static int getArmorItemsCount(LivingEntity living) {
		return (living.getItemBySlot(EquipmentSlotType.HEAD).isEmpty() ? 0 : 2)
				+ (living.getItemBySlot(EquipmentSlotType.CHEST).isEmpty() ? 0 : 3)
				+ (living.getItemBySlot(EquipmentSlotType.LEGS).isEmpty() ? 0 : 2)
				+ (living.getItemBySlot(EquipmentSlotType.FEET).isEmpty() ? 0 : 1);
	}
	
	@SubscribeEvent
	public void onEnttiyAttributeCreation(EntityAttributeCreationEvent event) {
		
	}

}
