package nuparu.sevendaystomine.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.IFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModEntities;
import nuparu.sevendaystomine.init.ModLootTables;

public class PlaguedNurseEntity<T extends PlaguedNurseEntity> extends ZombieBipedEntity {

	public PlaguedNurseEntity(EntityType<PlaguedNurseEntity> type, World world) {
		super(type, world);
	}

	public PlaguedNurseEntity(World world) {
		this(ModEntities.PLAGUED_NURSE.get(), world);
	}

	@Override
	protected int getExperienceReward(PlayerEntity p_70693_1_) {
		return 15;
	}

	@Override
	protected ResourceLocation getDefaultLootTable() {
		return ModLootTables.ZOMBIE_NURSE;
	}


	public class Factory implements IFactory<PlaguedNurseEntity> {
		@Override
		public PlaguedNurseEntity create(EntityType<PlaguedNurseEntity> type, World world) {
			return new PlaguedNurseEntity(type, world);
		}
	}
}
