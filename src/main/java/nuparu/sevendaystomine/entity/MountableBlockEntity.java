package nuparu.sevendaystomine.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import nuparu.sevendaystomine.init.ModEntities;

public class MountableBlockEntity extends Entity {

    private BlockState blockState = null;
    private BlockPos blockPos = BlockPos.ZERO;

    private static final DataParameter<Float> DELTA_Y = EntityDataManager.<Float>defineId(MountableBlockEntity.class,
            DataSerializers.FLOAT);

    public MountableBlockEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public MountableBlockEntity(World world) {
        this(ModEntities.MOUNTABLE_BLOCK.get(), world);
    }

    public MountableBlockEntity(World world, double x, double y, double z) {
        this(world);
        this.setPos(x, y, z);
        blockPos = new BlockPos(x, y, z);
        blockState = world.getBlockState(blockPos);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!level.isClientSide()) {
            if (this.getPassengers().isEmpty() || this.level.getBlockState(blockPos) != blockState) {
                this.kill();
            }
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return getDeltaY();
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DELTA_Y, 0f);
    }

    public void setDeltaY(float deltaY) {
        this.entityData.set(DELTA_Y, deltaY);
    }

    public float getDeltaY() {
        return this.entityData.get(DELTA_Y);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compoundNBT) {
            setDeltaY(compoundNBT.getFloat("deltaY"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compoundNBT) {
        compoundNBT.putDouble("deltaY", getDeltaY());
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }
}
