package nuparu.sevendaystomine.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.IFactory;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModEntities;

public class CrawlerZombieEntity<T extends CrawlerZombieEntity> extends ZombieBipedEntity {

	public CrawlerZombieEntity(EntityType<CrawlerZombieEntity> type, World world) {
		super(type, world);
	}

	public CrawlerZombieEntity(World world) {
		this(ModEntities.CRAWLER_ZOMBIE.get(), world);
	}

	@Override
	protected int getExperienceReward(PlayerEntity p_70693_1_) {
		return 10;
	}
	
	public static AttributeModifierMap createAttributes() {
		return MonsterEntity.createMonsterAttributes( ).add(Attributes.FOLLOW_RANGE, 56.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.125f).add(Attributes.ATTACK_DAMAGE, 3.0D)
				.add(Attributes.ARMOR, 0.0D).add(Attributes.MAX_HEALTH, 50).build();
	}

	@Override
	public Vector3d corpseRotation() {
		return new Vector3d(90,0,0);
	}

	@Override
	public Vector3d corpseTranslation() {
		return new Vector3d(0,-0.25,0);
	}

	@Override
	public boolean customCoprseTransform() {
		return true;
	}

	public class Factory implements IFactory<CrawlerZombieEntity> {
		@Override
		public CrawlerZombieEntity create(EntityType<CrawlerZombieEntity> type, World world) {
			return new CrawlerZombieEntity(type, world);
		}
	}
}
