package nuparu.sevendaystomine.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.IFactory;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModEntities;

import javax.annotation.Nullable;

public class TwistedZombieEntity<T extends TwistedZombieEntity> extends ZombieBipedEntity {

	public TwistedZombieEntity(EntityType<TwistedZombieEntity> type, World world) {
		super(type, world);
	}

	public TwistedZombieEntity(World world) {
		this(ModEntities.TWISTED_ZOMBIE.get(), world);
	}

	@Override
	protected int getExperienceReward(PlayerEntity p_70693_1_) {
		return 25;
	}

	public static AttributeModifierMap createAttributes() {
		return MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 64.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.12F).add(Attributes.ATTACK_DAMAGE, 6.0D)
				.add(Attributes.ARMOR, 25.0D).add(Attributes.MAX_HEALTH, 150).build();
	}

	@Override
	@Nullable
	public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_,
			SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
		p_213386_4_ = super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
		this.populateDefaultEquipmentSlots(p_213386_2_);

		return p_213386_4_;
	}
	
	@Override
	public boolean hurt(DamageSource source, float amount)
    {
		return super.hurt(source, this.isOnFire() ? amount*4.25f : amount/3);
    }

	@Override
	public void aiStep() {

		if (this.level.isClientSide) {
			for(int i = 0; i < random.nextInt(2); ++i) {
				this.level.addParticle(ParticleTypes.WARPED_SPORE, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
			}
		}

		super.aiStep();
	}
	
	public class Factory implements IFactory<TwistedZombieEntity> {
		@Override
		public TwistedZombieEntity create(EntityType<TwistedZombieEntity> type, World world) {
			return new TwistedZombieEntity(type, world);
		}
	}
}
