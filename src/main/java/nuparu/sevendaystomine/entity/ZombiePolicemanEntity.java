package nuparu.sevendaystomine.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.IFactory;
import net.minecraft.entity.IChargeableMob;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.entity.ai.PolicemanSwellGoal;
import nuparu.sevendaystomine.init.ModEntities;

public class ZombiePolicemanEntity<T extends ZombiePolicemanEntity> extends ZombieBipedEntity{
	private static final DataParameter<Integer> DATA_SWELL_DIR = EntityDataManager.defineId(ZombiePolicemanEntity.class,
			DataSerializers.INT);
	private static final DataParameter<Boolean> DATA_IS_POWERED = EntityDataManager
			.defineId(ZombiePolicemanEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> DATA_IS_IGNITED = EntityDataManager
			.defineId(ZombiePolicemanEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> VOMIT_TIMER = EntityDataManager.defineId(ZombiePolicemanEntity.class,
			DataSerializers.INT);

	private int oldSwell;
	private int swell;
	private int maxSwell = 30;
	private int explosionRadius = 3;

	public static final int RECHARGE_TIME = 100;
	
	public ZombiePolicemanEntity(EntityType<ZombiePolicemanEntity> type, World world) {
		super(type, world);
	}

	public ZombiePolicemanEntity(World world) {
		this(ModEntities.ZOMBIE_POLICEMAN.get(), world);
	}
	
	@Override
	protected void registerGoals() {
	    this.goalSelector.addGoal(2, new PolicemanSwellGoal(this));
		super.registerGoals();
	}

	@Override
	protected int getExperienceReward(PlayerEntity p_70693_1_) {
		return 15;
	}

	public static AttributeModifierMap createAttributes() {
		return MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 64.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.15F).add(Attributes.ATTACK_DAMAGE, 7.0D)
				.add(Attributes.ARMOR, 3.0D).add(Attributes.MAX_HEALTH, 120).build();
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		return super.hurt(source, this.isOnFire() ? amount * 4.25f : amount / 3);
	}

	public boolean isPowered() {
		return this.entityData.get(DATA_IS_POWERED);
	}

	@OnlyIn(Dist.CLIENT)
	public float getSwelling(float p_70831_1_) {
		return MathHelper.lerp(p_70831_1_, (float) this.oldSwell, (float) this.swell) / (float) (this.maxSwell - 2);
	}

	public int getSwellDir() {
		return this.entityData.get(DATA_SWELL_DIR);
	}

	public void setSwellDir(int p_70829_1_) {
		this.entityData.set(DATA_SWELL_DIR, p_70829_1_);
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_SWELL_DIR, -1);
		this.entityData.define(DATA_IS_POWERED, false);
		this.entityData.define(DATA_IS_IGNITED, false);
		this.entityData.define(VOMIT_TIMER, 0);
	}

	public void addAdditionalSaveData(CompoundNBT p_213281_1_) {
		super.addAdditionalSaveData(p_213281_1_);
		if (this.entityData.get(DATA_IS_POWERED)) {
			p_213281_1_.putBoolean("powered", true);
		}

		p_213281_1_.putShort("Fuse", (short) this.maxSwell);
		p_213281_1_.putByte("ExplosionRadius", (byte) this.explosionRadius);
		p_213281_1_.putBoolean("ignited", this.isIgnited());
		p_213281_1_.putInt("vomit_timer", getVomitTimer());
	}

	public void readAdditionalSaveData(CompoundNBT p_70037_1_) {
		super.readAdditionalSaveData(p_70037_1_);
		this.entityData.set(DATA_IS_POWERED, p_70037_1_.getBoolean("powered"));
		if (p_70037_1_.contains("Fuse", 99)) {
			this.maxSwell = p_70037_1_.getShort("Fuse");
		}

		if (p_70037_1_.contains("ExplosionRadius", 99)) {
			this.explosionRadius = p_70037_1_.getByte("ExplosionRadius");
		}

		if (p_70037_1_.getBoolean("ignited")) {
			this.ignite();
		}
		if (p_70037_1_.contains("vomit_timer", Constants.NBT.TAG_INT)) {
			setVomitTimer(p_70037_1_.getInt("vomit_timer"));
		}
	}

	public void tick() {
		if (this.isAlive()) {
			if (getVomitTimer() > 0) {
				if (getVomitTimer() < 55) {
					//setAnimation(EnumAnimationState.DEFAULT);
				}
				setVomitTimer(getVomitTimer() - 1);
			}
			this.oldSwell = this.swell;
			if (this.isIgnited()) {
				this.setSwellDir(1);
			}

			int i = this.getSwellDir();
			if (i > 0 && this.swell == 0) {
				this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
			}

			this.swell += i;
			if (this.swell < 0) {
				this.swell = 0;
			}

			if (this.swell >= this.maxSwell) {
				this.swell = this.maxSwell;
				this.explodeCreeper();
			}
		}

		super.tick();
	}

	private void explodeCreeper() {
		if (!this.level.isClientSide) {
			Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level,
					this) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
			float f = this.isPowered() ? 2.0F : 1.0F;
			this.dead = true;
			this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float) this.explosionRadius * f,
					explosion$mode);
			this.remove();
		}

	}

	public boolean isIgnited() {
		return this.entityData.get(DATA_IS_IGNITED);
	}

	public void ignite() {
		this.entityData.set(DATA_IS_IGNITED, true);
	}

	public int getVomitTimer() {

		return this.entityData.get(VOMIT_TIMER);
	}

	public void setVomitTimer(int vomitTimer) {
		this.entityData.set(VOMIT_TIMER, vomitTimer);
	}

	public class Factory implements IFactory<ZombiePolicemanEntity> {
		@Override
		public ZombiePolicemanEntity create(EntityType<ZombiePolicemanEntity> type, World world) {
			return new ZombiePolicemanEntity(type, world);
		}
	}
}
