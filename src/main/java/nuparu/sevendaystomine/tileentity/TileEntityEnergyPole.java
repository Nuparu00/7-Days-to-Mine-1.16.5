package nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.block.BlockEnergyPole;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.EnumDeviceType;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.util.ModConstants;

public class TileEntityEnergyPole extends TileEntity implements ITickableTileEntity, IVoltage {

	protected List<ElectricConnection> inputs = new ArrayList<ElectricConnection>();
	protected List<ElectricConnection> outputs = new ArrayList<ElectricConnection>();

	protected long voltage = 0;
	protected long capacity = 1000;

	public TileEntityEnergyPole() {
		super(ModTileEntities.ENERGY_POLE.get());
	}
	
	public TileEntityEnergyPole(TileEntityType<?> type) {
		super(type);

	}

	public void markForUpdate() {
		level.sendBlockUpdated(worldPosition, level.getBlockState(this.worldPosition),
				level.getBlockState(worldPosition), 3);
		setChanged();
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbtTag = new CompoundNBT();
		this.save(nbtTag);
		return new SUpdateTileEntityPacket(this.worldPosition, 0, nbtTag);
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return save(new CompoundNBT());
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

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);

		this.voltage = compound.getLong("power");
		if (compound.contains("inputs", Constants.NBT.TAG_COMPOUND)) {
			inputs.clear();
			CompoundNBT in = compound.getCompound("inputs");
			int size = in.getInt("size");
			for (int i = 0; i < size; i++) {
				CompoundNBT nbt = in.getCompound("input_" + i);
				ElectricConnection connection = new ElectricConnection();
				connection.readNBT(nbt);
				inputs.add(connection);
			}
		}

		if (compound.contains("outputs", Constants.NBT.TAG_COMPOUND)) {
			outputs.clear();
			CompoundNBT in = compound.getCompound("outputs");
			int size = in.getInt("size");
			for (int i = 0; i < size; i++) {
				CompoundNBT nbt = in.getCompound("output" + i);
				ElectricConnection connection = new ElectricConnection();
				connection.readNBT(nbt);
				outputs.add(connection);
			}
		}

	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		 super.save(compound);

		compound.putLong("power", voltage);

		CompoundNBT in = new CompoundNBT();

		in.putInt("size", getInputs().size());
		for (int i = 0; i < inputs.size(); i++) {
			ElectricConnection connection = inputs.get(i);
			in.put("input_" + i, connection.writeNBT(new CompoundNBT()));
		}

		CompoundNBT out = new CompoundNBT();

		out.putInt("size", getOutputs().size());
		for (int i = 0; i < outputs.size(); i++) {
			ElectricConnection connection = outputs.get(i);
			out.put("output" + i, connection.writeNBT(new CompoundNBT()));
		}

		compound.put("inputs", in);
		compound.put("outputs", out);

		return compound;
	}

	@Override
	public EnumDeviceType getDeviceType() {
		return EnumDeviceType.CONDUCTOR;
	}

	@Override
	public int getMaximalInputs() {
		return 10;
	}

	@Override
	public int getMaximalOutputs() {
		return 10;
	}

	@Override
	public List<ElectricConnection> getInputs() {
		return new ArrayList<ElectricConnection>(inputs);
	}

	@Override
	public List<ElectricConnection> getOutputs() {
		return new ArrayList<ElectricConnection>(outputs);
	}

	@Override
	public long getOutput() {
		return 0;
	}

	@Override
	public long getMaxOutput() {
		return 100;
	}

	@Override
	public long getOutputForConnection(ElectricConnection connection) {
		long l = (long) Math.floor(voltage / getOutputs().size());
		if (l > getMaxOutput()) {
			l = getMaxOutput();
		}

		return l;
	}

	@Override
	public boolean tryToConnect(ElectricConnection connection) {
		short type = -1;
		if (connection.getFrom().equals(worldPosition)) {
			type = 0;
		} else if (connection.getTo().equals(worldPosition)) {
			type = 1;
		}

		if (type == 0 && getOutputs().size() < getMaximalOutputs()) {
			this.outputs.add(connection);
			return true;
		}

		if (type == 1 && getInputs().size() < getMaximalInputs()) {
			this.inputs.add(connection);
			return true;
		}

		return false;
	}

	@Override
	public boolean canConnect(ElectricConnection connection) {
		short type = -1;
		if (connection.getFrom().equals(worldPosition)) {
			type = 0;
		} else if (connection.getTo().equals(worldPosition)) {
			type = 1;
		}

		if (type == 0 && getOutputs().size() < getMaximalOutputs()) {
			return true;
		}

		if (type == 1 && getInputs().size() < getMaximalInputs()) {
			return true;
		}

		return false;
	}

	@Override
	public long getRequiredPower() {
		return 0;
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
	public void tick() {
		if(level.isClientSide())return;
		if (this.voltage > 0) {
			int radius = (int) Math.ceil(ModConstants.PASSIVE_CONSUMER_RADIUS);
			int radiusSq = radius * radius;
			for (int i = -radius; i < radius; i++) {
				for (int j = -radius; j < radius; j++) {
					for (int k = -radius; k < radius; k++) {
						BlockPos blockPos = worldPosition.offset(i, j, k);
						if (blockPos.distSqr(worldPosition) > radiusSq)
							continue;
						TileEntity te = level.getBlockEntity(blockPos);
						if (te == null || !(te instanceof IVoltage))
							continue;
						IVoltage ivoltage = (IVoltage) te;
						if (!ivoltage.isPassive())
							continue;
						if (this.voltage < ivoltage.getRequiredPower())
							continue;
						long l = ivoltage.tryToSendPower(ivoltage.getRequiredPower(), null);
						this.voltage -= l;
						if (l != 0) {
							this.setChanged();
						}
					}
				}
			}
		}

		Iterator<ElectricConnection> iterator = outputs.iterator();
		while (iterator.hasNext()) {
			ElectricConnection connection = iterator.next();
			IVoltage voltage = connection.getTo(level);
			if (voltage != null) {
				long l = voltage.tryToSendPower(getOutputForConnection(connection), connection);
				this.voltage -= l;
				if (l != 0) {
					this.setChanged();
				}
			} else {
				iterator.remove();
				this.setChanged();
			}
		}

		iterator = inputs.iterator();
		while (iterator.hasNext()) {
			ElectricConnection connection = iterator.next();
			IVoltage voltage = connection.getFrom(level);
			if (voltage == null) {
				iterator.remove();
				this.setChanged();
			}
		}

		/*
		 * if (this.voltage > 0) { this.voltage *= (1 - ModConstants.DROP_PER_TICK); }
		 */

	}

	@Override
	public long getCapacity() {
		return capacity;
	}

	@Override
	public long getVoltageStored() {
		return voltage;
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

	protected static final Vector3d offset = new Vector3d(0.5, 0.75, 0.5);
	protected static final Vector3d offsetDown = new Vector3d(0.5, 0.75, 0.5);

	@Override
	public Vector3d getWireOffset() {
		BlockState state = level.getBlockState(worldPosition);
		if (state.getValue(BlockEnergyPole.FACING) == Direction.UP) {
			return offsetDown;
		}
		return offset;
	}

	@Override
	public boolean isPassive() {
		return false;
	}
	
	@Override
	public boolean disconnect(IVoltage voltage) {
		for(ElectricConnection input : getInputs()) {
			if(input.getFrom().equals(voltage.getPos())) {
				this.inputs.remove(input);
				this.markForUpdate();
				return true;
			}
		}
		
		for(ElectricConnection output : getOutputs()) {
			if(output.getTo().equals(voltage.getPos())) {
				this.outputs.remove(output);
				this.markForUpdate();
				return true;
			}
		}
		voltage.disconnect(this);
		return false;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		long toAdd = Math.min(this.capacity-this.voltage, maxReceive);
		if(!simulate) {
			this.voltage+=toAdd;
		}
		return (int)toAdd;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		long toExtract = Math.min(this.voltage, maxExtract);
		if(!simulate) {
			this.voltage-=toExtract;
		}
		return (int)toExtract;
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
