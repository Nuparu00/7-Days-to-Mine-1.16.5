package nuparu.sevendaystomine.entity;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.EntityType.IFactory;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.*;
import nuparu.sevendaystomine.init.ModEntities;

public class BurntZombieEntity<T extends BurntZombieEntity> extends ZombieBipedEntity {

	private static final DataParameter<Boolean> DATA_SOUL_CONVERSION_ID = EntityDataManager
			.defineId(BurntZombieEntity.class, DataSerializers.BOOLEAN);

	private int inSoulFireTime;
	private int conversionTime;

	public BurntZombieEntity(EntityType<BurntZombieEntity> type, World world) {
		super(type, world);
	}

	public BurntZombieEntity(World world) {
		this(ModEntities.BURNT_ZOMBIE.get(), world);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(DATA_SOUL_CONVERSION_ID, false);
	}

	public boolean isSoulFireConverting() {
		return this.getEntityData().get(DATA_SOUL_CONVERSION_ID);
	}

	@Override
	protected int getExperienceReward(PlayerEntity p_70693_1_) {
		return 15;
	}

	public static AttributeModifierMap createAttributes() {
		return MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 54.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.225F).add(Attributes.ATTACK_DAMAGE, 3.0D)
				.add(Attributes.ARMOR, 0.5D).add(Attributes.MAX_HEALTH, 115).build();
	}

	@Override
	public boolean isOnFire() {
		return true;
	}

	public float getBrightness() {
		return 1.0F;
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.HUSK_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
		return SoundEvents.HUSK_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.HUSK_DEATH;
	}

	protected SoundEvent getStepSound() {
		return SoundEvents.HUSK_STEP;
	}

	@Override
	public void tick() {

		double height = this.getBbHeight();
		double width = this.getBbWidth();
		if (!this.level.isClientSide && this.isAlive() && !this.isNoAi()) {
			if (this.isSoulFireConverting()) {
				--this.conversionTime;

				if (this.conversionTime < 0 && net.minecraftforge.event.ForgeEventFactory.canLivingConvert(this,
						ModEntities.SOUL_BURNT_ZOMBIE.get(), (timer) -> this.conversionTime = timer)) {
					this.doSoulFireConversion();
				}
			}
			if (level.getBlockState(new BlockPos(getX() + width / 2, getY() + height / 5, getZ() + width / 2))
					.getBlock() == Blocks.SOUL_FIRE) {
				++this.inSoulFireTime;
				if (this.inSoulFireTime >= 100 && !isSoulFireConverting()) {
					this.startSoulFireConversion(150);
				}
			} else {
				this.inSoulFireTime = -1;
			}

		}

		super.tick();

		for (int x = 0; x < 1 + level.random.nextInt(3); x++) {
			level.addParticle(ParticleTypes.LARGE_SMOKE, getX() + level.random.nextDouble() * 0.3 - 0.15,
					getY() + height / 2, getZ() + level.random.nextDouble() * 0.3 - 0.15, 0.0D, 0.0D, 0.0D);
		}
		if (random.nextDouble() < 0.1D) {
			/*
			 * level.playLocalSound(getX() + width / 2, getY() + height / 2, getZ() + width
			 * / 2, SoundEvents.BLAZE_BURN, SoundCategory.HOSTILE, 1.0F, 1.0F, false);
			 */

		}
		if (!this.level.isClientSide()) {
			int i = MathHelper.floor(this.getX());
			int j = MathHelper.floor(this.getY());
			int k = MathHelper.floor(this.getZ());

			if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
				return;
			}

			for (int l = 0; l < 4; ++l) {
				i = MathHelper.floor(this.getX() + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
				j = MathHelper.floor(this.getY());
				k = MathHelper.floor(this.getZ() + (double) ((float) (l / 2 % 2 * 2 - 1) * 0.25F));
				BlockPos blockpos = new BlockPos(i, j, k);

				BlockState bottom = level.getBlockState(blockpos.below());

				Block fireBlock = Blocks.FIRE;

				if (bottom.getBlock() == Blocks.SOUL_SAND || bottom.getBlock() == Blocks.SOUL_SOIL) {
					fireBlock = Blocks.SOUL_FIRE;
				}

				if (this.level.getBlockState(blockpos).getMaterial() == Material.AIR
						&& fireBlock.canSurvive(Blocks.FIRE.defaultBlockState(), this.level, blockpos)) {
					this.level.setBlockAndUpdate(blockpos, fireBlock.defaultBlockState());
				}
			}
		}
	}

	@Override
	public boolean isSensitiveToWater() {
		return true;
	}

	public boolean doHurtTarget(Entity p_70652_1_) {
		if (random.nextDouble() < 0.3) {
			p_70652_1_.setSecondsOnFire(8);
		}
		return super.doHurtTarget(p_70652_1_);
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT p_213281_1_) {
		super.addAdditionalSaveData(p_213281_1_);
		p_213281_1_.putBoolean("IsBaby", this.isBaby());
		p_213281_1_.putInt("InSoulFireTime", this.isInWater() ? this.inSoulFireTime : -1);
		p_213281_1_.putInt("SoulConversionTime", this.isSoulFireConverting() ? this.conversionTime : -1);
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT p_70037_1_) {
		super.readAdditionalSaveData(p_70037_1_);
		this.setBaby(p_70037_1_.getBoolean("IsBaby"));
		this.inSoulFireTime = p_70037_1_.getInt("InSoulFireTime");
		if (p_70037_1_.contains("SoulConversionTime", 99) && p_70037_1_.getInt("SoulConversionTime") > -1) {
			this.startSoulFireConversion(p_70037_1_.getInt("SoulConversionTime"));
		}

	}

	private void startSoulFireConversion(int p_204704_1_) {
		this.conversionTime = p_204704_1_;
		this.getEntityData().set(DATA_SOUL_CONVERSION_ID, true);
	}

	protected void doSoulFireConversion() {
		this.convertToZombieType(ModEntities.SOUL_BURNT_ZOMBIE.get());
		if (!this.isSilent()) {
			// this.level.levelEvent((PlayerEntity) null, 1040, this.blockPosition(), 0);
			double height = this.getBbHeight();
			double width = this.getBbWidth();
			level.playSound(null, getX() + width / 2, getY() + height / 2, getZ() + width / 2,
					SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundCategory.HOSTILE, 1F, 0.8F);
		}

	}

	protected void convertToZombieType(EntityType<? extends ZombieBaseEntity> p_234341_1_) {
		ZombieBaseEntity zombieentity = this.convertTo(p_234341_1_, true);
		if (zombieentity != null) {
			net.minecraftforge.event.ForgeEventFactory.onLivingConvert(this, zombieentity);
		}

	}

	public static boolean isDarkEnoughToSpawn(IServerWorld p_223323_0_, BlockPos p_223323_1_, Random p_223323_2_) {
		if (p_223323_0_.getBrightness(LightType.SKY, p_223323_1_) > p_223323_2_.nextInt(32)) {
			return false;
		} else {
			int i = p_223323_0_.getLevel().isThundering() ? p_223323_0_.getMaxLocalRawBrightness(p_223323_1_, 10) : p_223323_0_.getMaxLocalRawBrightness(p_223323_1_);
			return i <= p_223323_2_.nextInt(13);
		}
	}

	public static boolean checkMonsterSpawnRules(EntityType<? extends MonsterEntity> p_223325_0_, IServerWorld p_223325_1_, SpawnReason p_223325_2_, BlockPos p_223325_3_, Random p_223325_4_) {
		return p_223325_1_.getDifficulty() != Difficulty.PEACEFUL && isDarkEnoughToSpawn(p_223325_1_, p_223325_3_, p_223325_4_) && checkMobSpawnRules(p_223325_0_, p_223325_1_, p_223325_2_, p_223325_3_, p_223325_4_);
	}

	public class Factory implements IFactory<BurntZombieEntity> {
		@Override
		public BurntZombieEntity create(EntityType<BurntZombieEntity> type, World world) {
			return new BurntZombieEntity(type, world);
		}
	}
}
