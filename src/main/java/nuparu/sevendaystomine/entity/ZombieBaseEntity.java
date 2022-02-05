package nuparu.sevendaystomine.entity;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectUtils;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.init.ModEntities;
import nuparu.sevendaystomine.init.ModLootTables;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.horde.Horde;
import nuparu.sevendaystomine.world.horde.HordeSavedData;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

public class ZombieBaseEntity extends MonsterEntity {

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
    public boolean nightRun = true;
    public Horde horde;
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
                .add(Attributes.MOVEMENT_SPEED, 0.175F).add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.ARMOR, 0.0D).add(Attributes.MAX_HEALTH, 45).build();
    }

    public static boolean isDarkEnoughToSpawn(IServerWorld p_223323_0_, BlockPos p_223323_1_, Random p_223323_2_) {
        if (p_223323_0_.getBrightness(LightType.SKY, p_223323_1_) > p_223323_2_.nextInt(32)) {
            return false;
        } else {
            int i = p_223323_0_.getLevel().isThundering() ? p_223323_0_.getMaxLocalRawBrightness(p_223323_1_, 10) : p_223323_0_.getMaxLocalRawBrightness(p_223323_1_);
            return i <= p_223323_2_.nextInt(11);
        }
    }

    public static boolean checkMonsterSpawnRules(EntityType<? extends MonsterEntity> p_223325_0_, IServerWorld p_223325_1_, SpawnReason p_223325_2_, BlockPos p_223325_3_, Random p_223325_4_) {
        return p_223325_1_.getDifficulty() != Difficulty.PEACEFUL && isDarkEnoughToSpawn(p_223325_1_, p_223325_3_, p_223325_4_) && checkMobSpawnRules(p_223325_0_, p_223325_1_, p_223325_2_, p_223325_3_, p_223325_4_);
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

    @Override
    public void die(DamageSource damageSource) {
        if (!this.level.isClientSide() && horde != null) {
            horde.onZombieKill(this);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if (level instanceof ServerWorld && horde == null && compound.contains("horde", Constants.NBT.TAG_COMPOUND)) {
            horde = HordeSavedData.getOrCreate((ServerWorld) level).getHordeByUUID(compound.getUUID("horde"));
            if (horde != null) {
                horde.addZombie(this);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        if (horde != null) {
            compound.putUUID("horde", horde.uuid);
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayerEntity player) {
        super.startSeenByPlayer(player);
        if (horde != null) {
            horde.onPlayerStartTacking(player, this);
        }
    }

    @Override
    public void stopSeenByPlayer(ServerPlayerEntity player) {
        super.stopSeenByPlayer(player);
        if (horde != null) {
            horde.onPlayerStopTacking(player, this);
        }
    }

    @Override
    public void killed(ServerWorld world, LivingEntity livingEntity) {
        super.killed(world, livingEntity);
        if(livingEntity instanceof PigEntity){
            ZombiePigEntity zombieentity = ((PigEntity)livingEntity).convertTo(ModEntities.ZOMBIE_PIG.get(), true);
            if (zombieentity != null) {
                net.minecraftforge.event.ForgeEventFactory.onLivingConvert(livingEntity, zombieentity);
            }
        }

        if(livingEntity instanceof WolfEntity){
            ZombieWolfEntity zombieentity = ((WolfEntity)livingEntity).convertTo(ModEntities.ZOMBIE_WOLF.get(), true);
            if (zombieentity != null) {
                net.minecraftforge.event.ForgeEventFactory.onLivingConvert(livingEntity, zombieentity);
            }
        }
    }



    @Override
    protected ResourceLocation getDefaultLootTable() {
        return ModLootTables.ZOMBIE_GENERIC;
    }
}
