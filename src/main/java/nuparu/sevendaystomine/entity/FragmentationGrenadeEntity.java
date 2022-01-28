package nuparu.sevendaystomine.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.EndGatewayTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.init.ModEntities;
import nuparu.sevendaystomine.init.ModItems;

public class FragmentationGrenadeEntity extends ProjectileItemEntity {


    public long age = 0;

    public FragmentationGrenadeEntity(EntityType<FragmentationGrenadeEntity> fragmentationGrenadeEntityEntityType, World world) {
        super(fragmentationGrenadeEntityEntityType, world);
    }

    public FragmentationGrenadeEntity(World p_i50155_2_) {
        super(ModEntities.FRAGMENTATION_GRENADE.get(), p_i50155_2_);
        setItem(new ItemStack(ModItems.FRAGMENTATION_GRENADE.get()));
    }

    public FragmentationGrenadeEntity(World p_i1774_1_, LivingEntity p_i1774_2_) {
        super(ModEntities.FRAGMENTATION_GRENADE.get(), p_i1774_2_, p_i1774_1_);
        setItem(new ItemStack(ModItems.FRAGMENTATION_GRENADE.get()));
    }

    public FragmentationGrenadeEntity(World p_i1775_1_, double p_i1775_2_, double p_i1775_4_, double p_i1775_6_) {
        super(ModEntities.FRAGMENTATION_GRENADE.get(), p_i1775_2_, p_i1775_4_, p_i1775_6_, p_i1775_1_);
        setItem(new ItemStack(ModItems.FRAGMENTATION_GRENADE.get()));
    }


    @Override
    protected Item getDefaultItem() {
        return ModItems.FRAGMENTATION_GRENADE.get();
    }

    @OnlyIn(Dist.CLIENT)
    private IParticleData getParticle() {
        ItemStack itemstack = new ItemStack(ModItems.FRAGMENTATION_GRENADE.get());
        return itemstack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleData(ParticleTypes.ITEM, itemstack);
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte p_70103_1_) {
        if (p_70103_1_ == 3) {
            IParticleData iparticledata = this.getParticle();

            for(int i = 0; i < 8; ++i) {
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
        Vector3d normal = new Vector3d(normali.getX(),normali.getY(),normali.getZ());

        double dot =  direction.normalize().dot(normal);
        Vector3d res = direction.subtract(normal.multiply(2*dot,2*dot,2*dot));

        this.setDeltaMovement(res.x*direction.length()*0.4f, res.y*direction.length()*0.4f, res.z*direction.length()*0.4f);
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
            if (this.age >= 80) {
                this.level.explode(this,getX(),getY(),getZ(),3, Explosion.Mode.BREAK);
                this.kill();
                return;
            }
        }
    }
}
