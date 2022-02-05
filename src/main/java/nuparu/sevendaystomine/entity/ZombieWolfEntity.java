package nuparu.sevendaystomine.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.IFactory;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.init.ModEntities;

public class ZombieWolfEntity<T extends ZombieWolfEntity> extends ZombieBipedEntity {
	private float interestedAngle;
	private float interestedAngleO;
	private boolean isWet;
	private boolean isShaking;
	private float shakeAnim;
	private float shakeAnimO;

	public ZombieWolfEntity(EntityType<ZombieWolfEntity> type, World world) {
		super(type, world);
	}

	public ZombieWolfEntity(World world) {
		this(ModEntities.ZOMBIE_WOLF.get(), world);
	}

	@Override
	protected int getExperienceReward(PlayerEntity p_70693_1_) {
		return 18;
	}
	
	public static AttributeModifierMap createAttributes() {
		return MonsterEntity.createMonsterAttributes( ).add(Attributes.FOLLOW_RANGE, 64.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.25000001192092896d).add(Attributes.ATTACK_DAMAGE, 4.0D)
				.add(Attributes.ARMOR, 0.0D).add(Attributes.MAX_HEALTH, 50).build();
	}

	@OnlyIn(Dist.CLIENT)
	public boolean isWet() {
		return this.isWet;
	}

	@OnlyIn(Dist.CLIENT)
	public float getWetShade(float p_70915_1_) {
		return Math.min(0.5F + MathHelper.lerp(p_70915_1_, this.shakeAnimO, this.shakeAnim) / 2.0F * 0.5F, 1.0F);
	}

	@OnlyIn(Dist.CLIENT)
	public float getBodyRollAngle(float p_70923_1_, float p_70923_2_) {
		float f = (MathHelper.lerp(p_70923_1_, this.shakeAnimO, this.shakeAnim) + p_70923_2_) / 1.8F;
		if (f < 0.0F) {
			f = 0.0F;
		} else if (f > 1.0F) {
			f = 1.0F;
		}

		return MathHelper.sin(f * (float)Math.PI) * MathHelper.sin(f * (float)Math.PI * 11.0F) * 0.15F * (float)Math.PI;
	}

	@OnlyIn(Dist.CLIENT)
	public float getHeadRollAngle(float p_70917_1_) {
		return MathHelper.lerp(p_70917_1_, this.interestedAngleO, this.interestedAngle) * 0.15F * (float)Math.PI;
	}

	protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
		this.playSound(SoundEvents.WOLF_STEP, 0.15F, 1.0F);
	}

	protected SoundEvent getAmbientSound() {
			return SoundEvents.WOLF_GROWL;
	}

	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
		return SoundEvents.WOLF_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.WOLF_DEATH;
	}


	public boolean isInSittingPose() {
		return false;
	}

	@Override
	public Vector3d corpseRotation() {
		return new Vector3d(0,0,90);
	}

	@Override
	public Vector3d corpseTranslation() {
		return new Vector3d(0,0,0);
	}

	@Override
	public boolean customCoprseTransform() {
		return true;
	}

	public class Factory implements IFactory<ZombieWolfEntity> {
		@Override
		public ZombieWolfEntity create(EntityType<ZombieWolfEntity> type, World world) {
			return new ZombieWolfEntity(type, world);
		}
	}
}
