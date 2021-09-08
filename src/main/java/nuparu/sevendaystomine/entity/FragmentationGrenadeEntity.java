package nuparu.sevendaystomine.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.EndGatewayTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import nuparu.sevendaystomine.init.ModEntities;
import nuparu.sevendaystomine.init.ModItems;

public class FragmentationGrenadeEntity extends ProjectileItemEntity {


    public FragmentationGrenadeEntity(EntityType<FragmentationGrenadeEntity> fragmentationGrenadeEntityEntityType, World world) {
        super(fragmentationGrenadeEntityEntityType, world);
    }

    public FragmentationGrenadeEntity(World p_i50155_2_) {
        super(ModEntities.FRAGMENTATION_GRENADE.get(), p_i50155_2_);
    }

    public FragmentationGrenadeEntity(World p_i1774_1_, LivingEntity p_i1774_2_) {
        super(ModEntities.FRAGMENTATION_GRENADE.get(), p_i1774_2_, p_i1774_1_);
    }

    public FragmentationGrenadeEntity(World p_i1775_1_, double p_i1775_2_, double p_i1775_4_, double p_i1775_6_) {
        super(ModEntities.FRAGMENTATION_GRENADE.get(), p_i1775_2_, p_i1775_4_, p_i1775_6_, p_i1775_1_);
    }


    @Override
    protected Item getDefaultItem() {
        return ModItems.FRAGMENTATION_GRENADE.get();
    }

    @OnlyIn(Dist.CLIENT)
    private IParticleData getParticle() {
        ItemStack itemstack = this.getItemRaw();
        return (IParticleData)(itemstack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleData(ParticleTypes.ITEM, itemstack));
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

    public void tick() {
        super.tick();
        RayTraceResult raytraceresult = ProjectileHelper.getHitResult(this, this::canHitEntity);
        boolean flag = false;
        if (raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockRayTraceResult)raytraceresult).getBlockPos();
            BlockState blockstate = this.level.getBlockState(blockpos);
            if (blockstate.is(Blocks.NETHER_PORTAL)) {
                this.handleInsidePortal(blockpos);
                flag = true;
            } else if (blockstate.is(Blocks.END_GATEWAY)) {
                TileEntity tileentity = this.level.getBlockEntity(blockpos);
                if (tileentity instanceof EndGatewayTileEntity && EndGatewayTileEntity.canEntityTeleport(this)) {
                    ((EndGatewayTileEntity)tileentity).teleportEntity(this);
                }

                flag = true;
            }
        }

        if (raytraceresult.getType() != RayTraceResult.Type.MISS && !flag && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
            this.onHit(raytraceresult);
        }

        this.checkInsideBlocks();
        Vector3d vector3d = this.getDeltaMovement();
        double d2 = this.getX() + vector3d.x;
        double d0 = this.getY() + vector3d.y;
        double d1 = this.getZ() + vector3d.z;
        this.updateRotation();
        float f;
        if (this.isInWater()) {
            for(int i = 0; i < 4; ++i) {
                float f1 = 0.25F;
                this.level.addParticle(ParticleTypes.BUBBLE, d2 - vector3d.x * 0.25D, d0 - vector3d.y * 0.25D, d1 - vector3d.z * 0.25D, vector3d.x, vector3d.y, vector3d.z);
            }

            f = 0.8F;
        } else {
            f = 0.99F;
        }

        this.setDeltaMovement(vector3d.scale((double)f));
        if (!this.isNoGravity()) {
            Vector3d vector3d1 = this.getDeltaMovement();
            this.setDeltaMovement(vector3d1.x, vector3d1.y - (double)this.getGravity(), vector3d1.z);
        }

        this.setPos(d2, d0, d1);
    }

    protected void onHitEntity(EntityRayTraceResult p_213868_1_) {
    }

    protected void onHit(RayTraceResult rayTraceResult) {
        if (!this.level.isClientSide) {
            if(rayTraceResult instanceof BlockRayTraceResult){
                BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult)rayTraceResult;
                Vector3d motion = this.getDeltaMovement();
                //Direction of hit face
                Direction direction = blockRayTraceResult.getDirection();
                Vector3i normal = direction.getNormal();

                //Vector3i seems to not have normalize() method for some reason, even though there was one in 1.12.2
                Vector3d normalD = new Vector3d(normal.getX(),normal.getY(),normal.getZ());
                //normalD = normalD.normalize();

                Vector3d bounce = new Vector3d(motion.x, motion.y, motion.z);
                double dot = motion.dot(normalD);
                normalD.scale(2*dot);
                bounce.subtract(normalD);

                double motionX = bounce.x/2d;
                double motionY = bounce.y/2d;
                double motionZ = bounce.z/2d;

                if (motionX < 0.1) {
                    motionX = 0;
                }
                if (motionY < 0.1) {
                    motionY= 0;
                }
                if (motionZ< 0.1) {
                    motionZ = 0;
                }

                this.setDeltaMovement(motionX, motionY, motionZ);
                this.checkAndResetUpdateChunkPos();
                this.checkInsideBlocks();
            }
            /*this.level.broadcastEntityEvent(this, (byte)3);
            this.remove();*/
        }

    }

    protected void checkInsideBlocks() {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        BlockPos blockpos = new BlockPos(axisalignedbb.minX + 0.001D, axisalignedbb.minY + 0.001D, axisalignedbb.minZ + 0.001D);
        BlockPos blockpos1 = new BlockPos(axisalignedbb.maxX - 0.001D, axisalignedbb.maxY - 0.001D, axisalignedbb.maxZ - 0.001D);
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        if (this.level.hasChunksAt(blockpos, blockpos1)) {
            for(int i = blockpos.getX(); i <= blockpos1.getX(); ++i) {
                for(int j = blockpos.getY(); j <= blockpos1.getY(); ++j) {
                    for(int k = blockpos.getZ(); k <= blockpos1.getZ(); ++k) {
                        blockpos$mutable.set(i, j, k);
                        BlockState blockstate = this.level.getBlockState(blockpos$mutable);

                        try {
                            blockstate.entityInside(this.level, blockpos$mutable, this);
                            this.onInsideBlock(blockstate);
                        } catch (Throwable throwable) {
                            CrashReport crashreport = CrashReport.forThrowable(throwable, "Colliding entity with block");
                            CrashReportCategory crashreportcategory = crashreport.addCategory("Block being collided with");
                            CrashReportCategory.populateBlockDetails(crashreportcategory, blockpos$mutable, blockstate);
                            throw new ReportedException(crashreport);
                        }
                    }
                }
            }
        }

    }
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
