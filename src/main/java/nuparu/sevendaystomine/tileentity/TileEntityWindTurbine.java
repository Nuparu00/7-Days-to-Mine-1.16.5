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
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.block.BlockWindTurbine;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.EnumDeviceType;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.ModConstants;

public class TileEntityWindTurbine extends TileEntity implements ITickableTileEntity, IVoltage {
	private List<ElectricConnection> inputs = new ArrayList<ElectricConnection>();
	private List<ElectricConnection> outputs = new ArrayList<ElectricConnection>();
	private long capacity = 500l;
	private long voltage = 0;

	public float angle;
	public float anglePrev;
	public float wind;
	public int tickCounter = 0;

	public Direction facing;

	public TileEntityWindTurbine() {
		super(ModTileEntities.WIND_TURBINE.get());
	}

	public void tick() {

		boolean flag = false;
		if (facing == null) {
			this.facing = level.getBlockState(worldPosition).getValue(BlockWindTurbine.FACING).getOpposite();
			setChanged();
		}
		anglePrev = angle;
		angle += wind;
		while (angle >= 360) {
			angle -= 360;
		}

		float windPrev = wind;
		wind = getWindAccess() / 2f;
		if (wind >= 0.5f) {
			long voltagePrev = voltage;
			wind = (1 + (wind * (worldPosition.getY() / 63))) * (4 + level.rainLevel * 3 + level.thunderLevel * 5);
			if (tickCounter++ >= 20) {
				voltage = MathUtils.clamp(voltage + (long) (wind), 0, capacity);
				tickCounter = 0;
			}
			if (voltage != voltagePrev || windPrev != wind) {
				flag = true;
			}
		} else {
			wind = 0.001f;
		}
		Iterator<ElectricConnection> iterator = outputs.iterator();
		while (iterator.hasNext()) {
			ElectricConnection connection = iterator.next();
			IVoltage voltage = connection.getTo(level);
			if (voltage != null) {
				long l = voltage.tryToSendPower(getOutputForConnection(connection), connection);
				this.voltage -= l;
				if (l != 0) {
					flag = true;
				}
			} else {
				iterator.remove();
				flag = true;
			}
		}

		iterator = inputs.iterator();
		while (iterator.hasNext()) {
			ElectricConnection connection = iterator.next();
			IVoltage voltage = connection.getFrom(level);
			if (voltage == null) {
				iterator.remove();
				flag = true;
			}
		}

		if (flag) {
			this.setChanged();
		}

	}

	public float getWindAccess() {
		int count = 0;
		int iterations = 0;
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				BlockPos p = (worldPosition.relative(facing.getClockWise(), -2)).relative(facing.getClockWise(), x);
				for (int z = 1; z < 3; z++) {
					BlockPos b = p.relative(facing, z);
					if (level.getBlockState(new BlockPos(b.getX(), b.getY() + z, b.getZ())).getMaterial().isSolid()) {
						count++;
					}
					iterations++;
				}

			}
		}
		return (float) 1 - (count / iterations);
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

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		voltage = compound.getLong("voltage");
		wind = compound.getFloat("wind");
		if (compound.contains("facing", 3)) {
			this.facing = Direction.from2DDataValue(compound.getInt("facing"));
		}
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
		tickCounter = compound.getInt("tickCounter");

	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		 super.save(compound);
		compound.putLong("voltage", voltage);
		compound.putFloat("wind", wind);
		if (facing != null) {
			compound.putInt("facing", this.facing.get2DDataValue());
		}
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
		compound.putInt("tickCounter", tickCounter);

		return compound;
	}

	@Override
	public EnumDeviceType getDeviceType() {
		return EnumDeviceType.GENERATOR;
	}

	@Override
	public int getMaximalInputs() {
		return 4;
	}

	@Override
	public int getMaximalOutputs() {
		return 4;
	}

	@Override
	public List<ElectricConnection> getInputs() {
		return inputs;
	}

	@Override
	public List<ElectricConnection> getOutputs() {
		return outputs;
	}

	@Override
	public long getOutput() {
		if (this.getVoltageStored() >= getMaxOutput()) {
			return getMaxOutput();
		}
		return this.getVoltageStored();
	}

	@Override
	public long getMaxOutput() {
		return 5;
	}

	@Override
	public long getOutputForConnection(ElectricConnection connection) {
		return getOutput();
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

	@Override
	public long tryToSendPower(long power, ElectricConnection connection) {
		long canBeAdded = capacity - voltage;
		long delta = Math.min(canBeAdded, power);
		long lost = 0;
		if (connection != null) {
			lost = (long) Math.round(delta * ModConstants.DROP_PER_BLOCK * connection.getDistance());
		}
		long realDelta = delta - lost;
		this.voltage += realDelta;

		return delta;
	}

	private static final Vector3d offset = new Vector3d(0.5, 0.5, 0.5);

	@Override
	public Vector3d getWireOffset() {
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
				markForUpdate();
				return true;
			}
		}
		
		for(ElectricConnection output : getOutputs()) {
			if(output.getTo().equals(voltage.getPos())) {
				
				this.outputs.remove(output);
				markForUpdate();
				return true;
			}
		}
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
