package nuparu.sevendaystomine.entity;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectUtils;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.config.CommonConfig;

public class ZombieBaseEntity extends MonsterEntity {

	public boolean nightRun = true;

	public final static UUID NIGHT_BOOST_ID = UUID.fromString("da53c6d8-c01f-11e7-abc4-cec278b6b50a");
	public final static UUID BLOODMOON_SPEED_BOOST_ID = UUID.fromString("2ca21e76-c020-11e7-abc4-cec278b6b50a");
	public final static UUID BLOODMOON_RANGE_BOOST_ID = UUID.fromString("4340be6a-c8bf-11e7-a80b-cec278b6b50a");
	public final static UUID BLOODMOON_DAMAGE_BOOST_ID = UUID.fromString("dc7572f6-d05f-4df6-afee-7fa78046ec54");
	public final static UUID BLOODMOON_ARMOR_BOOST_ID = UUID.fromString("b859cf4a-b7cd-486f-9b59-ebabfdd0985e");

	public static final AttributeModifier NIGHT_SPEED_BOOST = new AttributeModifier(
			UUID.fromString("da53c6d8-c01f-11e7-abc4-cec278b6b50a"), "nightSpeedBoost", 0.75f,
			AttributeModifier.Operation.MULTIPLY_BASE);
	public static final AttributeModifier BLOODMOON_SPEED_BOOST = new AttributeModifier(
			UUID.fromString("2ca21e76-c020-11e7-abc4-cec278b6b50a"), "bloodmoonSpeedBoost", 0.2f,
			AttributeModifier.Operation.MULTIPLY_BASE);
	public static final AttributeModifier BLOODMOON_DAMAGE_BOOST = new AttributeModifier(
			UUID.fromString("dc7572f6-d05f-4df6-afee-7fa78046ec54"), "bloodmoonDamageBoost", 0.5f,
			AttributeModifier.Operation.MULTIPLY_BASE);
	public static final AttributeModifier BLOODMOON_RANGE_BOOST = new AttributeModifier(
			UUID.fromString("4340be6a-c8bf-11e7-a80b-cec278b6b50a"), "bloodmoonRangeBoost", 0.5f,
			AttributeModifier.Operation.MULTIPLY_BASE);
	public static final AttributeModifier BLOODMOON_ARMOR_BOOST = new AttributeModifier(
			UUID.fromString("b859cf4a-b7cd-486f-9b59-ebabfdd0985e"), "bloodmoonArmorBoost", 4f,
			AttributeModifier.Operation.ADDITION);

	ModifiableAttributeInstance speed;
	ModifiableAttributeInstance range;
	ModifiableAttributeInstance armor;
	ModifiableAttributeInstance attack;

	// public Horde horde;

	public ZombieBaseEntity(EntityType<? extends ZombieBaseEntity> type, World world) {
		super(type, world);
	}

	public static AttributeModifierMap createAttributes() {
		return MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 64.0D)
				.add(Attributes.MOVEMENT_SPEED, (double) 0.175F).add(Attributes.ATTACK_DAMAGE, 4.0D)
				.add(Attributes.ARMOR, 0.0D).add(Attributes.MAX_HEALTH, 60).build();
	}

	@Override
	public void tick() {
		super.tick();

		if (speed == null) {
			speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
		}

		if (range == null) {
			range = this.getAttribute(Attributes.FOLLOW_RANGE);
		}

		if (armor == null) {
			armor = this.getAttribute(Attributes.ARMOR);
		}

		if (attack == null) {
			attack = this.getAttribute(Attributes.ATTACK_DAMAGE);
		}

		if (!this.level.isClientSide()) {
			if (Utils.isBloodmoonProper(level)) {
				if (!speed.hasModifier(BLOODMOON_SPEED_BOOST)) {
					speed.addTransientModifier(BLOODMOON_SPEED_BOOST);
				}
				if (!range.hasModifier(BLOODMOON_RANGE_BOOST)) {
					range.addTransientModifier(BLOODMOON_RANGE_BOOST);
				}
				if (!armor.hasModifier(BLOODMOON_ARMOR_BOOST)) {
					armor.addTransientModifier(BLOODMOON_ARMOR_BOOST);
				}
				if (!attack.hasModifier(BLOODMOON_DAMAGE_BOOST)) {
					attack.addTransientModifier(BLOODMOON_DAMAGE_BOOST);
				}
			} else {
				if (speed.hasModifier(BLOODMOON_SPEED_BOOST)) {
					speed.removeModifier(BLOODMOON_SPEED_BOOST);
				}
				if (range.hasModifier(BLOODMOON_RANGE_BOOST)) {
					range.removeModifier(BLOODMOON_RANGE_BOOST);
				}
				if (armor.hasModifier(BLOODMOON_ARMOR_BOOST)) {
					armor.removeModifier(BLOODMOON_ARMOR_BOOST);
				}
				if (attack.hasModifier(BLOODMOON_DAMAGE_BOOST)) {
					attack.removeModifier(BLOODMOON_DAMAGE_BOOST);
				}
			}

			if (nightRun) {
				BlockPos pos = this.blockPosition();
				float light = this.level.getBrightness(pos) * 15;
				if (light < 10) {
					if (!speed.hasModifier(NIGHT_SPEED_BOOST)) {
						speed.addPermanentModifier(NIGHT_SPEED_BOOST);
					}
				} else {
					if (speed.hasModifier(NIGHT_SPEED_BOOST)) {
						speed.removeModifier(NIGHT_SPEED_BOOST);
					}
				}
			}
		}
	}

	public float getDigSpeed(BlockState state, @Nullable BlockPos pos) {
		float f = this.getMainHandItem().getDestroySpeed(state);
		if (f > 1.0F) {
			int i = EnchantmentHelper.getBlockEfficiency(this);
			ItemStack itemstack = this.getMainHandItem();
			if (i > 0 && !itemstack.isEmpty()) {
				f += (float) (i * i + 1);
			}
		}

		if (EffectUtils.hasDigSpeed(this)) {
			f *= 1.0F + (float) (EffectUtils.getDigSpeedAmplification(this) + 1) * 0.2F;
		}

		if (this.hasEffect(Effects.DIG_SLOWDOWN)) {
			float f1;
			switch (this.getEffect(Effects.DIG_SLOWDOWN).getAmplifier()) {
			case 0:
				f1 = 0.3F;
				break;
			case 1:
				f1 = 0.09F;
				break;
			case 2:
				f1 = 0.0027F;
				break;
			case 3:
			default:
				f1 = 8.1E-4F;
			}

			f *= f1;
		}

		if (this.isEyeInFluid(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(this)) {
			f /= 5.0F;
		}

		if (!this.onGround) {
			f /= 5.0F;
		}

		return f / 10f;
	}

	public Vector3d corpseRotation() {
		return Vector3d.ZERO;
	}

	public Vector3d corpseTranslation() {
		return Vector3d.ZERO;
	}

	public boolean customCoprseTransform() {
		return false;
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.ZOMBIE_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
		return SoundEvents.ZOMBIE_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ZOMBIE_DEATH;
	}

	protected SoundEvent getStepSound() {
		return SoundEvents.ZOMBIE_STEP;
	}

	protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
		this.playSound(this.getStepSound(), 0.15F, 1.0F);
	}
}
