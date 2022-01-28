package nuparu.sevendaystomine.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import nuparu.sevendaystomine.init.ModEntities;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.potions.Potions;

import java.util.List;

public class ChlorineGrenadeEntity extends ProjectileItemEntity {


    public long age = 0;

    public ChlorineGrenadeEntity(EntityType<ChlorineGrenadeEntity> fragmentationGrenadeEntityEntityType, World world) {
        super(fragmentationGrenadeEntityEntityType, world);
        setItem(new ItemStack(ModItems.CHLORINE_GRENADE.get()));
    }

    public ChlorineGrenadeEntity(World p_i50155_2_) {
        super(ModEntities.CHLORINE_GRENADE.get(), p_i50155_2_);
        setItem(new ItemStack(ModItems.CHLORINE_GRENADE.get()));
    }

    public ChlorineGrenadeEntity(World p_i1774_1_, LivingEntity p_i1774_2_) {
        super(ModEntities.CHLORINE_GRENADE.get(), p_i1774_2_, p_i1774_1_);
        setItem(new ItemStack(ModItems.CHLORINE_GRENADE.get()));
    }

    public ChlorineGrenadeEntity(World p_i1775_1_, double p_i1775_2_, double p_i1775_4_, double p_i1775_6_) {
        super(ModEntities.CHLORINE_GRENADE.get(), p_i1775_2_, p_i1775_4_, p_i1775_6_, p_i1775_1_);
        setItem(new ItemStack(ModItems.CHLORINE_GRENADE.get()));
    }


    @Override
    protected Item getDefaultItem() {
        return ModItems.CHLORINE_GRENADE.get();
    }

    @OnlyIn(Dist.CLIENT)
    private IParticleData getParticle() {
        ItemStack itemstack = new ItemStack(ModItems.CHLORINE_GRENADE.get());
        return itemstack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleData(ParticleTypes.ITEM, itemstack);
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte p_70103_1_) {
        if (p_70103_1_ == 3) {
            IParticleData iparticledata = this.getParticle();

            for (int i = 0; i < 8; ++i) {
                this.level.addParticle(iparticledata, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    @Override
    protected void onHitEntity(EntityRayTraceResult p_213868_1_) {

    }

    @Override
    protected void onHitBlock(BlockRayTraceResult result) {
        BlockState blockstate = this.level.getBlockState(result.getBlockPos());
        blockstate.onProjectileHit(this.level, blockstate, result, this);
        Vector3i normali = result.getDirection().getNormal();
        Vector3d direction = this.getDeltaMovement();
        Vector3d normal = new Vector3d(normali.getX(), normali.getY(), normali.getZ());

        double dot = direction.normalize().dot(normal);
        Vector3d res = direction.subtract(normal.multiply(2 * dot, 2 * dot, 2 * dot));

        this.setDeltaMovement(res.x * direction.length() * 0.4f, res.y * direction.length() * 0.4f, res.z * direction.length() * 0.4f);
    }

    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        age = compound.getLong("age");
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putLong("age", age);
    }

    @Override
    public void tick() {
        super.tick();

        this.age++;
        if (!level.isClientSide()) {
            if (this.age >= 1200) {
                this.kill();
                return;
            }
        }

        if (age > 120) {
            if (this.age == 121) {
                this.level.playSound(null, getX(), getY(), getZ(), ModSounds.GAS_LEAK.get(), SoundCategory.AMBIENT,
                        0.9f + this.random.nextFloat() * 0.2f, 0.9f + this.random.nextFloat() * 0.2f);
            }
            for (int i1 = 0; i1 < 5; ++i1) {

                double width = this.getBbWidth();
                double height = this.getBbHeight();
                if (isInWater()) {
                    this.level.addParticle(ParticleTypes.BUBBLE,
                            this.getX() + (this.random.nextDouble() * 0.2 + 0.4) * width,
                            this.getY() + this.random.nextDouble() * height,
                            this.getZ() + (this.random.nextDouble() * 0.2 + 0.4) * width,
                            random.nextFloat() - 0.5, random.nextFloat() + 0.3, random.nextFloat() - 0.5);
                } else {
                    int c = 1930808;
                    double d3 = (double) (c >> 16 & 255) / 255.0D;
                    double d4 = (double) (c >> 8 & 255) / 255.0D;
                    double d5 = (double) (c >> 0 & 255) / 255.0D;
                    this.level.addParticle(ParticleTypes.ENTITY_EFFECT,
                            this.getX() + (this.random.nextDouble() * 0.2 + 0.4) * width,
                            this.getY() + this.random.nextDouble() * height,
                            this.getZ() + (this.random.nextDouble() * 0.2 + 0.4) * width, d3, d4, d5);
                }
            }

            if (!level.isClientSide()) {
                AxisAlignedBB axisalignedbb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
                List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, axisalignedbb);
                if (!list.isEmpty()) {
                    for (LivingEntity livingentity : list) {
                        if (livingentity.isAffectedByPotions()) {
                            double d0 = this.distanceToSqr(livingentity);
                            if (d0 < 16.0D) {
                                double d1 = 1.0D - Math.sqrt(d0) / 4.0D;

                                Effect effect = Potions.CHLORINE_POISON.get();

                                int i = (int) (d1 * 600 + 0.5D);
                                if (i > 20) {
                                    livingentity.addEffect(new EffectInstance(effect, i, 0, false, true));
                                }


                            }
                        }
                    }
                }
            }
        }

    }
}
