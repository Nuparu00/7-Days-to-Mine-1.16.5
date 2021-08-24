package nuparu.sevendaystomine.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.IFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModEntities;

public class ReanimatedCorpseEntity<T extends ReanimatedCorpseEntity> extends ZombieBipedEntity {

	public ReanimatedCorpseEntity(EntityType<ReanimatedCorpseEntity> type, World world) {
		super(type, world);
	}

	public ReanimatedCorpseEntity(World world) {
		this(ModEntities.REANIMATED_CORPSE.get(), world);
	}

	@Override
	protected int getExperienceReward(PlayerEntity p_70693_1_) {
		return 15;
	}

	public class Factory implements IFactory<ReanimatedCorpseEntity> {
		@Override
		public ReanimatedCorpseEntity create(EntityType<ReanimatedCorpseEntity> type, World world) {
			return new ReanimatedCorpseEntity(type, world);
		}
	}
}
