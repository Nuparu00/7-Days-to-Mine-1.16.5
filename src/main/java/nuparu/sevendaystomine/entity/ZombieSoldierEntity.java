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
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModEntities;
import nuparu.sevendaystomine.init.ModLootTables;

public class ZombieSoldierEntity<T extends ZombieSoldierEntity> extends ZombieBipedEntity {

	public ZombieSoldierEntity(EntityType<ZombieSoldierEntity> type, World world) {
		super(type, world);
	}

	public ZombieSoldierEntity(World world) {
		this(ModEntities.SOLDIER_ZOMBIE.get(), world);
	}

	@Override
	protected int getExperienceReward(PlayerEntity p_70693_1_) {
		return 22;
	}

	public static AttributeModifierMap createAttributes() {
		return MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 64.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.15F).add(Attributes.ATTACK_DAMAGE, 5.0D)
				.add(Attributes.ARMOR, 1.0D).add(Attributes.MAX_HEALTH, 60).build();
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
	protected ResourceLocation getDefaultLootTable() {
		return ModLootTables.ZOMBIE_SOLDIER;
	}


	public class Factory implements IFactory<ZombieSoldierEntity> {
		@Override
		public ZombieSoldierEntity create(EntityType<ZombieSoldierEntity> type, World world) {
			return new ZombieSoldierEntity(type, world);
		}
	}
}
