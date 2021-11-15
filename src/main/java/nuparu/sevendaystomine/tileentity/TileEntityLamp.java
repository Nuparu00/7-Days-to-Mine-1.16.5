package nuparu.sevendaystomine.tileentity;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import nuparu.sevendaystomine.block.BlockLamp;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.EnumDeviceType;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.util.ModConstants;

public class TileEntityLamp extends TileEntity implements ITickableTileEntity, IVoltage {

	private long voltage = 0;
	private long capacity = 30;

	private boolean on = false;

	public TileEntityLamp() {
		super(ModTileEntities.LAMP.get());
	}

	@Override
	public EnumDeviceType getDeviceType() {
		return EnumDeviceType.CONSUMER;
	}

	@Override
	public int getMaximalInputs() {
		return 0;
	}

	@Override
	public int getMaximalOutputs() {
		return 0;
	}

	@Override
	public List<ElectricConnection> getInputs() {
		return null;
	}

	@Override
	public List<ElectricConnection> getOutputs() {
		return null;
	}

	@Override
	public long getOutput() {
		return 0;
	}

	@Override
	public long getMaxOutput() {
		return 0;
	}

	@Override
	public long getOutputForConnection(ElectricConnection connection) {
		return 0;
	}

	@Override
	public boolean tryToConnect(ElectricConnection connection) {
		return false;
	}

	@Override
	public boolean canConnect(ElectricConnection connection) {
		return false;
	}

	@Override
	public long getRequiredPower() {
		return 1;
	}

	@Override
	public long getCapacity() {
		return this.capacity;
	}

	@Override
	public long getVoltageStored() {
		return this.voltage;
	}

	@Override
	public void storePower(long power) {
		this.voltage += power;
		if (this.voltage > this.getCapacity()) {
			this.voltage = this.getCapacity();
		}
		if (this.voltage < 0) {
			this.voltage = 0;
		}
	}

	@Override
	public long tryToSendPower(long power, ElectricConnection connection) {
		long canBeAdded = capacity - voltage;
		long delta = Math.min(canBeAdded, power);
		long lost = 0;
		if (connection != null) {
			lost = Math.round(delta * ModConstants.DROP_PER_BLOCK * connection.getDistance());
		}
		long realDelta = delta - lost;
		this.voltage += realDelta;

		return delta;
	}

	@Override
	public Vector3d getWireOffset() {
		return null;
	}

	@Override
	public boolean isPassive() {
		return true;
	}

	@Override
	public void tick() {
		if (level.isClientSide())
			return;
		if (on && this.voltage >= this.getRequiredPower()) {
			if (!BlockLamp.isLit(level, worldPosition)) {
				BlockLamp.setState(level, worldPosition, true);
				markForUpdate();
			}
			this.voltage -= this.getRequiredPower();
		} else {
			if (BlockLamp.isLit(level, worldPosition)) {
				BlockLamp.setState(level, worldPosition, false);
				markForUpdate();
			}
		}
		if (this.voltage < 0) {
			this.voltage = 0;
		}
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);

		this.voltage = compound.getLong("power");
		this.on = compound.getBoolean("on");

	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);

		compound.putLong("power", voltage);
		compound.putBoolean("on", on);
		return compound;
	}

	public void setState(boolean state) {
		this.on = state;
		markForUpdate();
	}

	public boolean getState() {
		return this.on;
	}

	public void markForUpdate() {
		level.sendBlockUpdated(worldPosition, level.getBlockState(this.worldPosition),
				level.getBlockState(worldPosition), 3);
		setChanged();
	}

	@Override
	public boolean disconnect(IVoltage voltage) {
		return false;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		long toAdd = Math.min(this.capacity - this.voltage, maxReceive);
		if (!simulate) {
			this.voltage += toAdd;
		}
		return (int) toAdd;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		long toExtract = Math.min(this.voltage, maxExtract);
		if (!simulate) {
			this.voltage -= toExtract;
		}
		return (int) toExtract;
	}

	@Override
	public int getEnergyStored() {
		return (int) this.voltage;
	}

	@Override
	public int getMaxEnergyStored() {
		return (int) this.capacity;
	}

	@Override
	public boolean canExtract() {
		return this.capacity > 0;
	}

	@Override
	public boolean canReceive() {
		return this.voltage < this.capacity;
	}

	@Override
	public BlockPos getPos() {
		return worldPosition;
	}
}
