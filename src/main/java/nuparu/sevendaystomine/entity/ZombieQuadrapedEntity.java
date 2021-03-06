package nuparu.sevendaystomine.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.entity.ai.GoalBreakBlocks;
import nuparu.sevendaystomine.init.ModLootTables;
import nuparu.sevendaystomine.util.ItemUtils;

public abstract class ZombieQuadrapedEntity<T extends ZombieQuadrapedEntity> extends ZombieBaseEntity {

	public ZombieQuadrapedEntity(EntityType<T> type, World world) {
		super(type, world);
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new GoalBreakBlocks(this));
		this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
		this.addBehaviourGoals();
	}

	protected void addBehaviourGoals() {
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
		if (ServerConfig.zombiesAttackAnimals.get()) {
			this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false));
			this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
			this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, TurtleEntity.class, 10, true, false,
					TurtleEntity.BABY_ON_LAND_SELECTOR));
			this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AnimalEntity.class, true));
		}
	}

	@Override
	protected int getExperienceReward(PlayerEntity p_70693_1_) {
		return 10;
	}

	@Override
	protected void tickDeath() {
		super.tickDeath();
		if (ServerConfig.zombieCorpses.get()) {
			++this.deathTime;

			if (this.deathTime == 20) {

				remove(false);
				LootableCorpseEntity lootable = new LootableCorpseEntity(level);
				lootable.setOriginal(this);
				lootable.setPos(getX(), getY(), getZ());
				dead = true;
				if (!this.level.isClientSide()) {
					LootTable loottable = this.level.getServer().getLootTables().get(getDefaultLootTable());
					LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld) this.level)).withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf(this.blockPosition()));
					if (this.lastHurtByPlayer != null) {
						lootcontext$builder.withLuck(lastHurtByPlayer.getLuck()).withParameter(LootParameters.THIS_ENTITY, lastHurtByPlayer);
					}
					ItemUtils.fill(loottable, lootable.getInventory(), lootcontext$builder.create(LootParameterSets.CHEST));
					level.addFreshEntity(lootable);
				}

				for (int i = 0; i < 20; ++i) {
					double d0 = this.random.nextGaussian() * 0.02D;
					double d1 = this.random.nextGaussian() * 0.02D;
					double d2 = this.random.nextGaussian() * 0.02D;
					this.level.addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(),
							this.getRandomZ(1.0D), d0, d1, d2);
				}
			}
		}
		/*
		 * } else { super.tickDeath(); }
		 */
	}
	@Override
	protected ResourceLocation getDefaultLootTable() {
		return ModLootTables.ZOMBIE_ANIMAL;
	}

}
