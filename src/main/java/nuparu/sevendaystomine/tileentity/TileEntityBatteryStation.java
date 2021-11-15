package nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.EnumDeviceType;
import nuparu.sevendaystomine.electricity.IBattery;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.block.ContainerBatteryStation;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.util.ModConstants;

public class TileEntityBatteryStation extends TileEntityItemHandler<ItemHandlerNameable>
		implements ITickableTileEntity, IVoltage {
	public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.battery_station");
	private List<ElectricConnection> inputs = new ArrayList<ElectricConnection>();
	private List<ElectricConnection> outputs = new ArrayList<ElectricConnection>();
	private long capacity = 10000;
	private long voltage = 0;

	public TileEntityBatteryStation() {
		super(ModTileEntities.BATTERY_STATION.get());
	}

	@Override
	public void tick() {

		//
		if (level.isClientSide())
			return;
		boolean flag = false;

		if (voltage < capacity) {
			ItemStack input = getInventory().getStackInSlot(0);
			if (!input.isEmpty() && input.getItem() instanceof IBattery) {
				IBattery battery = (IBattery) input.getItem();
				long inputVoltage = battery.getVoltage(input, level);
				if (inputVoltage > 0) {
					long deltaVoltage = Math.min((capacity - voltage), Math.min(10, inputVoltage));
					if (deltaVoltage > 0) {
						battery.drainVoltage(input, level, deltaVoltage);
						this.voltage += deltaVoltage;
						flag = true;
					}
				}
			}
		}

		ItemStack output = getInventory().getStackInSlot(1);
		if (!output.isEmpty() && output.getItem() instanceof IBattery) {
			IBattery battery = (IBattery) output.getItem();
			long outputVoltage = battery.getVoltage(output, level);
			long outputCapacity = battery.getCapacity(output, level);
			if (outputVoltage < outputCapacity) {
				long deltaVoltage = Math.min((outputCapacity - outputVoltage), Math.min(10, voltage));
				if (deltaVoltage > 0) {
					this.voltage -= (deltaVoltage - battery.tryToAddVoltage(output, level, deltaVoltage));
					flag = true;
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
			this.markForUpdate();
		}
	}

	@Override
	protected ItemHandlerNameable createInventory() {
		return new ItemHandlerNameable(2, DEFAULT_NAME);
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		voltage = compound.getLong("voltage");

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
	public EnumDeviceType getDeviceType() {
		return EnumDeviceType.STORAGE;
	}

	@Override
	public int getMaximalInputs() {
		return 16;
	}

	@Override
	public int getMaximalOutputs() {
		return 164;
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
		return 80;
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
			lost = Math.round(delta * ModConstants.DROP_PER_BLOCK * connection.getDistance());
		}
		long realDelta = delta - lost;
		this.voltage += realDelta;
		if (realDelta > 0) {
			markForUpdate();
		}

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
	public void onContainerOpened(PlayerEntity player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onContainerClosed(PlayerEntity player) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return this.level.getBlockEntity(this.worldPosition) == this
				&& player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5,
						this.worldPosition.getZ() + 0.5) <= 64;
	}

	@Override
	public ResourceLocation getLootTable() {
		return null;
	}

	@Override
	public boolean disconnect(IVoltage voltage) {
		for (ElectricConnection input : getInputs()) {
			if (input.getFrom().equals(voltage.getPos())) {
				this.inputs.remove(input);
				markForUpdate();
				return true;
			}
		}

		for (ElectricConnection output : getOutputs()) {
			if (output.getTo().equals(voltage.getPos())) {
				this.outputs.remove(output);
				markForUpdate();
				return true;
			}
		}
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
	public ITextComponent getDisplayName() {
		return this.getInventory().hasCustomName() ? this.getInventory().getDisplayName() : DEFAULT_NAME;
	}

	public void setDisplayName(String displayName) {
		getInventory().setDisplayName(new StringTextComponent(displayName));
	}

	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return ContainerBatteryStation.createContainerServerSide(windowID, playerInventory, this);
	}

	@Override
	public BlockPos getPos() {
		return worldPosition;
	}

}
