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
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.init.ModEntities;

public class ZombiePigEntity<T extends ZombiePigEntity> extends ZombieQuadrapedEntity {


	public ZombiePigEntity(EntityType<ZombiePigEntity> type, World world) {
		super(type, world);
	}

	public ZombiePigEntity(World world) {
		this(ModEntities.ZOMBIE_PIG.get(), world);
	}

	@Override
	protected int getExperienceReward(PlayerEntity p_70693_1_) {
		return 10;
	}
	
	public static AttributeModifierMap createAttributes() {
		return MonsterEntity.createMonsterAttributes( ).add(Attributes.FOLLOW_RANGE, 32.0D)
				.add(Attributes.MOVEMENT_SPEED, (double) 0.19f).add(Attributes.ATTACK_DAMAGE, 3.0D)
				.add(Attributes.ARMOR, 0.0D).add(Attributes.MAX_HEALTH, 50).build();
	}


	protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
		this.playSound(SoundEvents.PIG_STEP, 0.15F, 1.0F);
	}

	protected SoundEvent getAmbientSound() {
			return SoundEvents.PIG_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
		return SoundEvents.PIG_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.PIG_DEATH;
	}

	public class Factory implements IFactory<ZombiePigEntity> {
		@Override
		public ZombiePigEntity create(EntityType<ZombiePigEntity> type, World world) {
			return new ZombiePigEntity(type, world);
		}
	}
}
