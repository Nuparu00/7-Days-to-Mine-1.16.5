package nuparu.sevendaystomine.entity;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.EntityType.IFactory;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModEntities;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.ItemQuality;

public class SoulBurntZombieEntity<T extends SoulBurntZombieEntity> extends ZombieBipedEntity {

	public SoulBurntZombieEntity(EntityType<SoulBurntZombieEntity> type, World world) {
		super(type, world);
	}

	public SoulBurntZombieEntity(World world) {
		this(ModEntities.SOUL_BURNT_ZOMBIE.get(), world);
	}

	@Override
	protected int getExperienceReward(PlayerEntity p_70693_1_) {
		return 15;
	}

	public static AttributeModifierMap createAttributes() {
		return MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 54.0D)
				.add(Attributes.MOVEMENT_SPEED, (double) 0.2F).add(Attributes.ATTACK_DAMAGE, 3.5D)
				.add(Attributes.ARMOR, 0.3D).add(Attributes.MAX_HEALTH, 120).build();
	}

	@Override
	public boolean isOnFire() {
		return false;
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
		super.tick();
		double height = this.getBbHeight();
		double width = this.getBbWidth();

		for (int i = 0; i < 1 + level.random.nextInt(3); i++) {
			level.addParticle(ParticleTypes.LARGE_SMOKE, getX() + level.random.nextDouble() * 0.3 - 0.15,
					getY() + height / 2, getZ() + level.random.nextDouble() * 0.3 - 0.15, 0.0D, 0.0D, 0.0D);
		}
		if (random.nextDouble() < 0.1D) {
			level.playLocalSound(getX() + width / 2, getY() + height / 2, getZ() + width / 2, SoundEvents.BLAZE_BURN,
					SoundCategory.HOSTILE, 1.0F, 1.0F, false);
		}
		if (!this.level.isClientSide() && random.nextInt(5) == 0) {
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
				
				if(bottom.getBlock() == Blocks.SOUL_SAND || bottom.getBlock() == Blocks.SOUL_SOIL) {
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

	public class Factory implements IFactory<SoulBurntZombieEntity> {
		@Override
		public SoulBurntZombieEntity create(EntityType<SoulBurntZombieEntity> type, World world) {
			return new SoulBurntZombieEntity(type, world);
		}
	}
}
