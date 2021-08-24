package nuparu.sevendaystomine.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import nuparu.sevendaystomine.init.ModTileEntities;

public class TileEntityGlobe extends TileEntity implements ITickableTileEntity {

	private double speed;
	public double anglePrev;
	public double angle;

	public TileEntityGlobe() {
		super(ModTileEntities.GLOBE.get());
	}

	@Override
	public void tick() {
		this.anglePrev = angle;
		if (speed != 0) {
			speed = 0.98f * speed;
		}
		angle += speed;
		while (angle > 360) {
			angle -=360;
			anglePrev -= 360;
		}
		while (angle < 0) {
			angle +=360;
			anglePrev += 360;
		}
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.speed = compound.getDouble("speed");
		this.angle = compound.getDouble("angle");
		this.anglePrev = compound.getDouble("anglePrev");
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.putDouble("angle", this.angle);
		compound.putDouble("speed", this.speed);
		compound.putDouble("anglePrev", this.anglePrev);
		return compound;

	}

	public void addSpeed(double speed) {
		this.speed += speed;
		markForUpdate();
	}

	public double getSpeed() {
		return this.speed;
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbtTag = new CompoundNBT();
		this.save(nbtTag);
		return new SUpdateTileEntityPacket(this.worldPosition, 0, nbtTag);
	}

	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT nbt = save(new CompoundNBT());
		return nbt;
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		CompoundNBT tag = pkt.getTag();
		load(level.getBlockState(pkt.getPos()), tag);
		if (hasLevel()) {
			level.sendBlockUpdated(pkt.getPos(), level.getBlockState(this.worldPosition),
					level.getBlockState(pkt.getPos()), 2);
		}
	}
	
	public void markForUpdate() {
		level.sendBlockUpdated(worldPosition, level.getBlockState(this.worldPosition),
				level.getBlockState(worldPosition), 2);
		setChanged();
	}
}
