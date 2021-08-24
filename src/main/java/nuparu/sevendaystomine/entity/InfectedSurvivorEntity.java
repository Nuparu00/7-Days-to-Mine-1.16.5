package nuparu.sevendaystomine.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.IFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModEntities;

public class InfectedSurvivorEntity<T extends InfectedSurvivorEntity> extends ZombieBipedEntity {

	public InfectedSurvivorEntity(EntityType<InfectedSurvivorEntity> type, World world) {
		super(type, world);
	}

	public InfectedSurvivorEntity(World world) {
		this(ModEntities.INFECTED_SURVIVOR.get(), world);
	}

	@Override
	protected int getExperienceReward(PlayerEntity p_70693_1_) {
		return 15;
	}

	public class Factory implements IFactory<InfectedSurvivorEntity> {
		@Override
		public InfectedSurvivorEntity create(EntityType<InfectedSurvivorEntity> type, World world) {
			return new InfectedSurvivorEntity(type, world);
		}
	}
}
