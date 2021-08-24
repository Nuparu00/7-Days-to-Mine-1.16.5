package nuparu.sevendaystomine.entity;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

import javax.annotation.Nullable;

import net.minecraft.block.Blocks;
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
import net.minecraft.util.Hand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModEntities;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.ItemQuality;

public class FrostbittenWorkerEntity<T extends FrostbittenWorkerEntity> extends ZombieBipedEntity {

	public FrostbittenWorkerEntity(EntityType<FrostbittenWorkerEntity> type, World world) {
		super(type, world);
	}

	public FrostbittenWorkerEntity(World world) {
		this(ModEntities.FROSTBITTEN_WORKER.get(), world);
	}

	@Override
	protected int getExperienceReward(PlayerEntity p_70693_1_) {
		return 15;
	}

	public static AttributeModifierMap createAttributes() {
		return MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 64.0D)
				.add(Attributes.MOVEMENT_SPEED, (double) 0.15F).add(Attributes.ATTACK_DAMAGE, 4.5D)
				.add(Attributes.ARMOR, 1.0D).add(Attributes.MAX_HEALTH, 90).build();
	}

	@Override
	@Nullable
	public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_,
			SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
		p_213386_4_ = super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
		this.populateDefaultEquipmentSlots(p_213386_2_);

		return p_213386_4_;
	}
	
	protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
		if (random.nextInt(4) != 0)
			return;
		this.setItemSlot(EquipmentSlotType.MAINHAND, ItemQuality.setQualityForStack(new ItemStack(ModItems.IRON_AXE.get()), 1));
	}

	public class Factory implements IFactory<FrostbittenWorkerEntity> {
		@Override
		public FrostbittenWorkerEntity create(EntityType<FrostbittenWorkerEntity> type, World world) {
			return new FrostbittenWorkerEntity(type, world);
		}
	}
}
