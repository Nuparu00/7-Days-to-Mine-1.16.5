package nuparu.sevendaystomine.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nuparu.sevendaystomine.block.BlockHorizontalBase;
import nuparu.sevendaystomine.tileentity.TileEntityCamera;

public class EntityCameraView extends Entity {

	private BlockState blockState = null;
	private BlockPos blockPos = BlockPos.ZERO;

	public TileEntityCamera te;

	public float initRotation;
	public int direction = 1;

	public EntityCameraView(World worldIn, EntityType type) {
		super(type, worldIn);
	}

	public EntityCameraView(World worldIn, double x, double y, double z, EntityType type) {
		this(worldIn,type);
		setPos(x + 0.5D, y + 0.1D, z + 0.5D);
		blockPos = new BlockPos(x, y, z);
		blockState = this.level.getBlockState(blockPos);
		switch (blockState.getValue(BlockHorizontalBase.FACING)) {
		default:
		case SOUTH:
			initRotation = 0;
			direction = 1;
			break;
		case NORTH:
			initRotation = 180;
			direction = 1;
			break;
		case EAST:
			initRotation = 270;
			direction = 1;
			break;
		case WEST:
			initRotation = 90;
			direction = 1;
			break;
		}
		TileEntity tile = level.getBlockEntity(blockPos);
		if (tile instanceof TileEntityCamera) {
			this.te = (TileEntityCamera) tile;
		} else if(!level.isClientSide()){
			kill();
		}
	}

	public EntityCameraView(World worldIn, double x, double y, double z, TileEntityCamera te, EntityType type) {
		this(worldIn,type);
		if (te == null && !level.isClientSide()){
			kill();
			return;
		}
		setPos(x + 0.5D, y + 0.1D, z + 0.5D);
		blockPos = new BlockPos(x, y, z);
		blockState = this.level.getBlockState(blockPos);
		this.te = te;

		switch (blockState.getValue(BlockHorizontalBase.FACING)) {
		default:
		case SOUTH:
			initRotation = 0;
			direction = 1;
			break;
		case NORTH:
			initRotation = 180;
			direction = 1;
			break;
		case EAST:
			initRotation = 270;
			direction = 1;
			break;
		case WEST:
			initRotation = 90;
			direction = 1;
			break;
		}

	}

	@Override
	public void tick() {
		if (!this.level.isClientSide()) {
			if (this.level.getBlockState(blockPos) != blockState) {
				this.kill();
			}
		}
	}

	public BlockPos getBlockPos() {
		return this.blockPos;
	}

	@Override
	protected void defineSynchedData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT p_70037_1_) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT p_213281_1_) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		// TODO Auto-generated method stub
		return null;
	}
}
