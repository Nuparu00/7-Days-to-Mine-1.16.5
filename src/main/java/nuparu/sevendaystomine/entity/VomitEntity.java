package nuparu.sevendaystomine.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
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
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModEntities;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.util.Utils;

public class VomitEntity extends ProjectileItemEntity {


    public long age = 0;

    public VomitEntity(EntityType<VomitEntity> fragmentationGrenadeEntityEntityType, World world) {
        super(fragmentationGrenadeEntityEntityType, world);
    }

    public VomitEntity(World p_i50155_2_) {
        super(ModEntities.VOMIT.get(), p_i50155_2_);
        setItem(new ItemStack(ModItems.VOMIT.get()));
    }

    public VomitEntity(World p_i1774_1_, LivingEntity p_i1774_2_) {
        super(ModEntities.VOMIT.get(), p_i1774_2_, p_i1774_1_);
        setItem(new ItemStack(ModItems.VOMIT.get()));
    }

    public VomitEntity(World p_i1775_1_, double p_i1775_2_, double p_i1775_4_, double p_i1775_6_) {
        super(ModEntities.VOMIT.get(), p_i1775_2_, p_i1775_4_, p_i1775_6_, p_i1775_1_);
        setItem(new ItemStack(ModItems.VOMIT.get()));
    }


    @Override
    protected Item getDefaultItem() {
        return ModItems.FLARE.get();
    }

    @OnlyIn(Dist.CLIENT)
    private IParticleData getParticle() {
        ItemStack itemstack = new ItemStack(ModItems.FLARE.get());
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
    protected void onHitEntity(EntityRayTraceResult entityRayTraceResult) {
        Entity entity = entityRayTraceResult.getEntity();
        entity.hurt(DamageSource.GENERIC,4f);
        this.playSound(SoundEvents.SLIME_SQUISH_SMALL, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        this.kill();
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult result) {
        BlockState state = this.level.getBlockState(result.getBlockPos());
        BlockPos pos = result.getBlockPos();
        state.onProjectileHit(this.level, state, result, this);
        /*Vector3i normali = result.getDirection().getNormal();
        Vector3d direction = this.getDeltaMovement();
        Vector3d normal = new Vector3d(normali.getX(),normali.getY(),normali.getZ());

        double dot =  direction.normalize().dot(normal);
        Vector3d res = direction.subtract(normal.multiply(2*dot,2*dot,2*dot));

        this.setDeltaMovement(res.x*direction.length()*0.3f, res.y*direction.length()*0.3f, res.z*direction.length()*0.3f);
    */
        if (ServerConfig.bulletsBreakBlocks.get() && !level.isClientSide()) {
            if (state.getMaterial() == Material.GLASS) {
                this.level.destroyBlock(pos, false);
            } else if (!Utils.damageBlock((ServerWorld) level, pos,
                    (float) (4 / state.getDestroySpeed(level, pos)) * 0.003125f,
                    true)) {
            }
        }
        this.playSound(SoundEvents.SLIME_SQUISH_SMALL, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
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
            if (this.age >= 240) {
                this.kill();
                return;
            }
        }
    }
}
