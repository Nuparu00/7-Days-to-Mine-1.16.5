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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import nuparu.sevendaystomine.init.ModEntities;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.util.MathUtils;

public class MolotovEntity extends ProjectileItemEntity {


    public long age = 0;

    public MolotovEntity(EntityType<MolotovEntity> fragmentationGrenadeEntityEntityType, World world) {
        super(fragmentationGrenadeEntityEntityType, world);
    }

    public MolotovEntity(World p_i50155_2_) {
        super(ModEntities.MOLOTOV.get(), p_i50155_2_);
        setItem(new ItemStack(ModItems.MOLOTOV.get()));
    }

    public MolotovEntity(World p_i1774_1_, LivingEntity p_i1774_2_) {
        super(ModEntities.MOLOTOV.get(), p_i1774_2_, p_i1774_1_);
        setItem(new ItemStack(ModItems.MOLOTOV.get()));
    }

    public MolotovEntity(World p_i1775_1_, double p_i1775_2_, double p_i1775_4_, double p_i1775_6_) {
        super(ModEntities.MOLOTOV.get(), p_i1775_2_, p_i1775_4_, p_i1775_6_, p_i1775_1_);
        setItem(new ItemStack(ModItems.MOLOTOV.get()));
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.MOLOTOV.get();
    }

    @OnlyIn(Dist.CLIENT)
    private IParticleData getParticle() {
        ItemStack itemstack = new ItemStack(ModItems.MOLOTOV.get());
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
/*
        double dot =  direction.normalize().dot(normal);
        Vector3d res = direction.subtract(normal.multiply(2*dot,2*dot,2*dot));

        this.setDeltaMovement(res.x*direction.length()*0.4f, res.y*direction.length()*0.4f, res.z*direction.length()*0.4f);

    */
        double width = this.getBbWidth();
        double height = this.getBbHeight();
        Vector3i normali = result.getDirection().getNormal();
        Vector3d normal = new Vector3d(normali.getX(), normali.getY(), normali.getZ());

        for (int i = 0; i < 6; i++) {
            double motionX = normal.x * MathUtils.getDoubleInRange(0.05, 0.1);
            double motionY = normal.y * MathUtils.getDoubleInRange(0.05, 0.1);
            double motionZ = normal.z * MathUtils.getDoubleInRange(0.05, 0.1);

            if (Math.abs(normali.getX()) == 1) {
                motionY = MathUtils.getDoubleInRange(-0.05, 0.05);
                motionZ = MathUtils.getDoubleInRange(-0.05, 0.05);
            } else if (Math.abs(normali.getY()) == 1) {
                motionX = MathUtils.getDoubleInRange(-0.05, 0.05);
                motionZ = MathUtils.getDoubleInRange(-0.05, 0.05);
            } else {
                motionX = MathUtils.getDoubleInRange(-0.05, 0.05);
                motionY = MathUtils.getDoubleInRange(-0.05, 0.05);
            }

            level.addParticle(getParticle(), getX() , getY() , getZ() , motionX,
                    motionY, motionZ);
        }
        if (!level.isClientSide()) {


            level.playSound(null, getX(), getY(), getZ(), SoundEvents.SPLASH_POTION_BREAK, SoundCategory.AMBIENT,
                    0.9f + this.random.nextFloat() * 0.2f, 0.9f + this.random.nextFloat() * 0.2f);


            System.out.println(normali.toString());
            for (int i = 0; i < 32; i++) {

                FlameEntity flame = new FlameEntity(getX() + width / 2 + normal.x * MathUtils.getDoubleInRange(0.05,0.5), getY() + height / 2 + normal.y * MathUtils.getDoubleInRange(0.05,0.5), getZ() + width / 2 + normal.z * MathUtils.getDoubleInRange(0.05,0.5), level);
                double motionX = normal.x * MathUtils.getDoubleInRange(0.1, 0.3);
                double motionY = normal.y * MathUtils.getDoubleInRange(0.1, 0.3);
                double motionZ = normal.z * MathUtils.getDoubleInRange(0.1, 0.3);


                if (Math.abs(normali.getX()) == 1) {
                    motionY = MathUtils.getDoubleInRange(-0.3, 0.3);
                    motionZ = MathUtils.getDoubleInRange(-0.3, 0.3);
                } else if (Math.abs(normali.getY()) == 1) {
                    motionX = MathUtils.getDoubleInRange(-0.3, 0.3);
                    motionZ = MathUtils.getDoubleInRange(-0.3, 0.3);
                } else {
                    motionX = MathUtils.getDoubleInRange(-0.3, 0.3);
                    motionY = MathUtils.getDoubleInRange(-0.3, 0.3);
                }

                flame.setDeltaMovement(motionX, motionY, motionZ);

                /*flame.motionY = rand.nextDouble() * 0.2 - 0.1 + vec.getY()*0.25;
                flame.motionX = rand.nextDouble() * 0.8 - 0.4 + vec.getX()*0.5;
                flame.motionZ = rand.nextDouble() * 0.8 - 0.4 + vec.getZ()*0.5;*/
                flame.setGravity(0.04F);
                level.addFreshEntity(flame);
            }
        }

        this.kill();
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
            if (this.age >= 600) {
                this.level.explode(this, getX(), getY(), getZ(), 3, Explosion.Mode.BREAK);
                this.kill();
                return;
            }
        }
    }
}
