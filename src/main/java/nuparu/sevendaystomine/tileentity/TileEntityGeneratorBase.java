package nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.EnumDeviceType;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.util.ITemperature;
import nuparu.sevendaystomine.util.ModConstants;

public abstract class TileEntityGeneratorBase extends TileEntityItemHandler<ItemHandlerNameable> implements IVoltage, ITemperature, ITickableTileEntity {


	protected List<ElectricConnection> inputs = new ArrayList<ElectricConnection>();
	protected List<ElectricConnection> outputs = new ArrayList<ElectricConnection>();

	protected int burnTime;
	private int currentBurnTime;

	public final IIntArray dataAccess = new IIntArray() {
		public int get(int p_221476_1_) {
			switch (p_221476_1_) {
				case 0:
					return TileEntityGeneratorBase.this.burnTime;
				case 1:
					return TileEntityGeneratorBase.this.currentBurnTime;
				default:
					return 0;
			}
		}

		public void set(int p_221477_1_, int p_221477_2_) {
			switch (p_221477_1_) {
				case 0:
					TileEntityGeneratorBase.this.burnTime = p_221477_2_;
					break;
				case 1:
					TileEntityGeneratorBase.this.currentBurnTime = p_221477_2_;
					break;
			}

		}

		public int getCount() {
			return 4;
		}
	};

	protected double temperature = 0;
	protected double temperatureLimit = 0.6;
	protected static final double basePower = 6;


	public boolean isBurning = false;

	protected long capacity = 100000L;
	protected long voltage = 0;
	
	public int soundCounter = 90;

	public TileEntityGeneratorBase(TileEntityType<?> type) {
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

		isBurning = compound.getBoolean("isBurning");
		temperature = compound.getDouble("temperature");
		temperatureLimit = compound.getDouble("tempLimit");
		voltage = compound.getLong("voltage");
		
		this.burnTime = compound.getInt("BurnTime");
		this.currentBurnTime = ForgeHooks.getBurnTime(this.getInventory().getStackInSlot(0));
		
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

		compound.putInt("BurnTime", (short) this.burnTime);
		compound.putBoolean("isBurning", isBurning);
		compound.putDouble("temperature", temperature);
		compound.putDouble("temperatureLimit", temperatureLimit);
		compound.putLong("voltage", voltage);

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

	public int countAdjacentBlocks(Block block) {
		int count = 0;
		for (int i = 0; i < 6; i++) {
			if (this.level.getBlockState(this.worldPosition.relative(Direction.values()[i])).getBlock() == block) {
				count++;
			}
		}
		return count;
	}

	public int countAdjacentMats(Material mat) {
		int count = 0;
		for (int i = 0; i < 6; i++) {
			if (this.level.getBlockState(this.worldPosition.relative(Direction.values()[i])).getMaterial() == mat) {
				count++;
			}
		}
		return count;
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		} else {
			return player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D,
					(double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
		}
	}


	@Override
	public void setTemperature(double temperature) {
		this.temperature = temperature;

	}

	@Override
	public void addTemperature(double delta) {
		this.temperature += delta;
	}

	@Override
	public double getTemperature() {
		return temperature;
	}

	@Override
	public EnumDeviceType getDeviceType() {
		return EnumDeviceType.GENERATOR;
	}

	@Override
	public int getMaximalInputs() {
		return 0;
	}

	@Override
	public int getMaximalOutputs() {
		return 4;
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
		if (this.getVoltageStored() >= getMaxOutput()) {
			return getMaxOutput();
		}
		return this.getVoltageStored();
	}

	public long getPowerPerUpdate() {
		long out = this.isBurning ? (long) (basePower + (basePower * temperature)) : 0;
		if (out > getMaxOutput()) {
			out = getMaxOutput();
		}
		return out;
	}

	@Override
	public long getMaxOutput() {
		return 120;
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

	public boolean isBurning() {
		return this.burnTime > 0;
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
	
	private static final Vector3d offset = new Vector3d(0.5, 0.5, 0.5);
	
	@Override
	public Vector3d getWireOffset() {
		return offset;
	}
	
	@Override
	public boolean isPassive() {
		return false;
	}

	public int getCurrentBurnTime() {
		return currentBurnTime;
	}
	
	public int getBurnTime() {
		return burnTime;
	}

	@Override
	public ITextComponent getDisplayName() {
		return this.getInventory().getDisplayName();
	}
}
