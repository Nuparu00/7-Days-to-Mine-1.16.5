package nuparu.sevendaystomine.entity;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.EntityType.IFactory;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModEntities;

public class FeralZombieEntity<T extends FeralZombieEntity> extends ZombieBipedEntity {

	public FeralZombieEntity(EntityType<FeralZombieEntity> type, World world) {
		super(type, world);
	}

	public FeralZombieEntity(World world) {
		this(ModEntities.FERAL_ZOMBIE.get(), world);
	}

	@Override
	protected int getExperienceReward(PlayerEntity p_70693_1_) {
		return 15;
	}

	public static AttributeModifierMap createAttributes() {
		return MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 64.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.ATTACK_DAMAGE, 8.0D)
				.add(Attributes.ARMOR, 20.0D).add(Attributes.MAX_HEALTH, 250).build();
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
	
	
	public class Factory implements IFactory<FeralZombieEntity> {
		@Override
		public FeralZombieEntity create(EntityType<FeralZombieEntity> type, World world) {
			return new FeralZombieEntity(type, world);
		}
	}
}
