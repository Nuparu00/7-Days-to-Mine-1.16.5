package nuparu.sevendaystomine.entity;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

import javax.annotation.Nullable;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.EntityType.IFactory;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.JumpController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.ClimberPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.init.ModEntities;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.ItemQuality;

public class SpiderZombieEntity<T extends SpiderZombieEntity> extends ZombieBipedEntity {

	private static final DataParameter<Byte> DATA_FLAGS_ID = EntityDataManager.defineId(SpiderEntity.class,
			DataSerializers.BYTE);
	private int jumpTicks;
	private int jumpDuration;
	private boolean wasOnGround;
	private int jumpDelayTicks;

	public SpiderZombieEntity(EntityType<SpiderZombieEntity> type, World world) {
		super(type, world);
		this.jumpControl = new SpiderZombieEntity.JumpHelperController(this);
		this.moveControl = new SpiderZombieEntity.MoveHelperController(this);
	}

	public SpiderZombieEntity(World world) {
		this(ModEntities.SPIDER_ZOMBIE.get(), world);
	}

	@Override
	protected int getExperienceReward(PlayerEntity p_70693_1_) {
		return 15;
	}

	public static AttributeModifierMap createAttributes() {
		return MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 40.0D)
				.add(Attributes.MOVEMENT_SPEED, (double) 0.22F).add(Attributes.ATTACK_DAMAGE, 3.0D)
				.add(Attributes.ARMOR, 0D).add(Attributes.MAX_HEALTH, 55).build();
	}

	protected PathNavigator createNavigation(World p_175447_1_) {
		return new ClimberPathNavigator(this, p_175447_1_);
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_FLAGS_ID, (byte) 0);
	}

	public void tick() {
		super.tick();
		if (!this.level.isClientSide) {
			this.setClimbing(this.horizontalCollision);
		}

	}

	public boolean onClimbable() {
		return this.isClimbing();
	}

	public boolean isClimbing() {
		return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
	}

	public void setClimbing(boolean p_70839_1_) {
		byte b0 = this.entityData.get(DATA_FLAGS_ID);
		if (p_70839_1_) {
			b0 = (byte) (b0 | 1);
		} else {
			b0 = (byte) (b0 & -2);
		}

		this.entityData.set(DATA_FLAGS_ID, b0);
	}

	protected float getJumpPower() {
		if (!this.horizontalCollision
				&& (!this.moveControl.hasWanted() || !(this.moveControl.getWantedY() > this.getY() + 0.5D))) {
			Path path = this.navigation.getPath();
			if (path != null && !path.isDone()) {
				Vector3d vector3d = path.getNextEntityPos(this);
				if (vector3d.y > this.getY() + 0.5D) {
					return 0.5F;
				}
			}

			return this.moveControl.getSpeedModifier() <= 0.6D ? 0.2F : 0.3F;
		} else {
			return 0.5F;
		}
	}

	protected void jumpFromGround() {
		super.jumpFromGround();
		double d0 = this.moveControl.getSpeedModifier();
		if (d0 > 0.0D) {
			double d1 = getHorizontalDistanceSqr(this.getDeltaMovement());
			if (d1 < 0.01D) {
				this.moveRelative(0.6F, new Vector3d(0.0D, 0.0D, 1.0D));
			}
		}

		if (!this.level.isClientSide) {
			this.level.broadcastEntityEvent(this, (byte) 1);
		}

	}

	@OnlyIn(Dist.CLIENT)
	public float getJumpCompletion(float p_175521_1_) {
		return this.jumpDuration == 0 ? 0.0F : ((float) this.jumpTicks + p_175521_1_) / (float) this.jumpDuration;
	}

	public void setSpeedModifier(double p_175515_1_) {
		this.getNavigation().setSpeedModifier(p_175515_1_);
		this.moveControl.setWantedPosition(this.moveControl.getWantedX(), this.moveControl.getWantedY(),
				this.moveControl.getWantedZ(), p_175515_1_);
	}

	public void setJumping(boolean p_70637_1_) {
		super.setJumping(p_70637_1_);
		/*
		 * if (p_70637_1_) { this.playSound(this.getJumpSound(), this.getSoundVolume(),
		 * ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
		 * }
		 */

	}

	public void startJumping() {
		this.setJumping(true);
		this.jumpDuration = 10;
		this.jumpTicks = 0;
	}

	public void customServerAiStep() {
		if (this.jumpDelayTicks > 0) {
			--this.jumpDelayTicks;
		}

		if (this.onGround) {
			if (!this.wasOnGround) {
				this.setJumping(false);
				this.checkLandingDelay();
			}

			if (this.jumpDelayTicks == 0) {
				LivingEntity livingentity = this.getTarget();
				if (livingentity != null && this.distanceToSqr(livingentity) < 16.0D) {
					this.facePoint(livingentity.getX(), livingentity.getZ());
					this.moveControl.setWantedPosition(livingentity.getX(), livingentity.getY(), livingentity.getZ(),
							this.moveControl.getSpeedModifier());
					this.startJumping();
					this.wasOnGround = true;
				}
			}

			SpiderZombieEntity.JumpHelperController SpiderZombieEntity$jumphelpercontroller = (SpiderZombieEntity.JumpHelperController) this.jumpControl;
			if (!SpiderZombieEntity$jumphelpercontroller.wantJump()) {
				if (this.moveControl.hasWanted() && this.jumpDelayTicks == 0) {
					Path path = this.navigation.getPath();
					Vector3d vector3d = new Vector3d(this.moveControl.getWantedX(), this.moveControl.getWantedY(),
							this.moveControl.getWantedZ());
					if (path != null && !path.isDone()) {
						vector3d = path.getNextEntityPos(this);
					}

					this.facePoint(vector3d.x, vector3d.z);
					this.startJumping();
				}
			} else if (!SpiderZombieEntity$jumphelpercontroller.canJump()) {
				this.enableJumpControl();
			}
		}

		this.wasOnGround = this.onGround;
	}

	public boolean canSpawnSprintParticle() {
		return false;
	}

	private void facePoint(double p_175533_1_, double p_175533_3_) {
		this.yRot = (float) (MathHelper.atan2(p_175533_3_ - this.getZ(), p_175533_1_ - this.getX())
				* (double) (180F / (float) Math.PI)) - 90.0F;
	}

	private void enableJumpControl() {
		((SpiderZombieEntity.JumpHelperController) this.jumpControl).setCanJump(true);
	}

	private void disableJumpControl() {
		((SpiderZombieEntity.JumpHelperController) this.jumpControl).setCanJump(false);
	}

	private void setLandingDelay() {
		if (this.moveControl.getSpeedModifier() < 2.2D) {
			this.jumpDelayTicks = 4;
		} else {
			this.jumpDelayTicks = 1;
		}

	}

	private void checkLandingDelay() {
		this.setLandingDelay();
		this.disableJumpControl();
	}

	public void aiStep() {
		super.aiStep();
		if (this.jumpTicks != this.jumpDuration) {
			++this.jumpTicks;
		} else if (this.jumpDuration != 0) {
			this.jumpTicks = 0;
			this.jumpDuration = 0;
			this.setJumping(false);
		}

	}

	@Override
	protected int calculateFallDamage(float p_225508_1_, float p_225508_2_) {
		return super.calculateFallDamage(p_225508_1_, p_225508_2_/2);
	}

	public class JumpHelperController extends JumpController {
		private final SpiderZombieEntity rabbit;
		private boolean canJump;

		public JumpHelperController(SpiderZombieEntity p_i45863_2_) {
			super(p_i45863_2_);
			this.rabbit = p_i45863_2_;
		}

		public boolean wantJump() {
			return this.jump;
		}

		public boolean canJump() {
			return this.canJump;
		}

		public void setCanJump(boolean p_180066_1_) {
			this.canJump = p_180066_1_;
		}

		public void tick() {
			if (this.jump) {
				this.rabbit.startJumping();
				this.jump = false;
			}

		}
	}

	static class MoveHelperController extends MovementController {
		private final SpiderZombieEntity rabbit;
		private double nextJumpSpeed;

		public MoveHelperController(SpiderZombieEntity p_i45862_1_) {
			super(p_i45862_1_);
			this.rabbit = p_i45862_1_;
		}

		public void tick() {
			if (this.rabbit.onGround && !this.rabbit.jumping
					&& !((SpiderZombieEntity.JumpHelperController) this.rabbit.jumpControl).wantJump()) {
				this.rabbit.setSpeedModifier(0.0D);
			} else if (this.hasWanted()) {
				this.rabbit.setSpeedModifier(this.nextJumpSpeed);
			}

			super.tick();
		}

		public void setWantedPosition(double p_75642_1_, double p_75642_3_, double p_75642_5_, double p_75642_7_) {
			if (this.rabbit.isInWater()) {
				p_75642_7_ = 1.5D;
			}

			super.setWantedPosition(p_75642_1_, p_75642_3_, p_75642_5_, p_75642_7_);
			if (p_75642_7_ > 0.0D) {
				this.nextJumpSpeed = p_75642_7_;
			}

		}
	}

	public class Factory implements IFactory<SpiderZombieEntity> {
		@Override
		public SpiderZombieEntity create(EntityType<SpiderZombieEntity> type, World world) {
			return new SpiderZombieEntity(type, world);
		}
	}
}
