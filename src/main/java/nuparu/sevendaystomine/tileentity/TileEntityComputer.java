package nuparu.sevendaystomine.tileentity;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.computer.EnumSystem;
import nuparu.sevendaystomine.computer.HardDrive;
import nuparu.sevendaystomine.computer.fix.Memory;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.EnumDeviceType;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.electricity.network.INetwork;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.block.ContainerComputer;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.SyncTileEntityMessage;
import nuparu.sevendaystomine.util.ModConstants;
import nuparu.sevendaystomine.util.Utils;
import org.apache.commons.io.FileUtils;

public class TileEntityComputer extends TileEntityItemHandler<ItemHandlerNameable>
		implements ITickableTileEntity, INetwork, IVoltage {
	private static final int INVENTORY_SIZE = 7;
	private static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.computer");

	private TileEntityMonitor monitorTE = null;

	// Machine variables
	public boolean on = false;
	private EnumState state = EnumState.OFF;
	private EnumSystem system = EnumSystem.NONE;
	private long voltage = 0;
	private long capacity = 60;


	// hard drive

	private ArrayList<BlockPos> network = new ArrayList<BlockPos>();
	private Memory memory;

	public TileEntityComputer() {
		super(ModTileEntities.COMPUTER.get());
		memory = new Memory();
	}

	public boolean isOn() {
		return state != EnumState.OFF;
	}

	public void setState(EnumState state) {
		this.state = state;
		this.setChanged();
	}

	public EnumSystem getSystem() {
		return this.system;
	}

	public EnumState getState() {
		return this.state;
	}


	public void installSystem(EnumSystem system) {
		this.system = system;
	}

	public void turnOn() {
		this.on = true;
		this.markForUpdate();
	}

	public void turnOff() {
		this.on = false;
		this.markForUpdate();
	}

	public void startComputer() {
		setState(EnumState.BOOT_SCREEN);
	}

	public void stopComputer() {
		setState(EnumState.OFF);
		this.markForUpdate();
	}



	@Override
	public void tick() {

		if (level.isClientSide()) {
			return;
		}
		if (isOn()) {
			if (!on /*|| !isCompleted()*|| this.voltage < this.getRequiredPower()*/) {
				stopComputer();
				return;
			}
			this.voltage -= this.getRequiredPower();
		} else if (on /*&& isCompleted() && this.voltage > this.getRequiredPower()*/) {
			this.startComputer();
		}
		if (!level.isClientSide()) {
			/*for (TickingProcess process : getProcessesList()) {
				process.tick();
			}*/
		}

	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.state = EnumState.getEnum(compound.getString("state"));
		this.system = EnumSystem.getEnum(compound.getString("system"));
		this.voltage = compound.getLong("power");


		this.on = compound.getBoolean("on");

		/*if (compound.contains("memory")) {
			if (this.memory != null) {
				this.memory.readFromNBT(compound.getCompound("memory"));
			} else {
				this.memory = new Memory();
				this.memory.readFromNBT(compound.getCompound("memory"));
			}
		}
		*/

		ResourceLocation dimension = null;
		if(level != null){
			dimension = level.dimension().location();
		}
		else if(compound.contains("level",Constants.NBT.TAG_STRING)){
			dimension = new ResourceLocation(compound.getString("level"));
		}

		if(dimension != null) {
			File file = new File(Utils.getCurrentSaveRootDirectory(),"computers/"+dimension.getNamespace()+"/"+dimension.getPath()+"/"+getPos().getX()+"#"+getPos().getY()+"#"+getPos().getZ()+".nbt");

			if(file.exists()){
				System.out.println("READ");
			}

		}
		network.clear();
		ListNBT list2 = compound.getList("network", Constants.NBT.TAG_LONG);
		for (int i = 0; i < list2.size(); ++i) {
			LongNBT nbt = (LongNBT) list2.get(i);
			BlockPos blockPos = BlockPos.of(nbt.getAsLong());
			network.add(blockPos);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.putString("state", state.getName());
		compound.putString("level", level.dimension().location().toString());
		compound.putString("system", system.getName());
		compound.putLong("power", voltage);

		compound.putBoolean("on", on);

		/*if (memory != null) {
			compound.put("drive", memory.save(new CompoundNBT()));
		}*/

		File file = new File(Utils.getCurrentSaveRootDirectory(),"computers/"+level.dimension().location().getNamespace()+"/"+level.dimension().location().getPath()+"/"+getPos().getX()+"#"+getPos().getY()+"#"+getPos().getZ()+".nbt");
		System.out.println(file.getAbsolutePath());
		if (!file.exists()) {
			new File(Utils.getCurrentSaveRootDirectory(),"computers/"+level.dimension().location().getNamespace()+"/"+level.dimension().location().getPath()).mkdirs();
			try {
				file.createNewFile();
				try {
					FileUtils.writeStringToFile(file,memory.save(new CompoundNBT()).getAsString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		ListNBT list2 = new ListNBT();
		for (BlockPos net : getConnections()) {
			list2.add(LongNBT.valueOf(net.asLong()));
		}

		compound.put("network", list2);

		return compound;
	}

	public boolean isCompleted() {
		for (int i = 0; i < 5; i++) {
			if (!isPartCorrect(i)) {
				return false;
			}
		}
		return true;
	}

	private boolean isPartCorrect(int slot) {

		ItemStack stack = this.getInventory().getStackInSlot(slot);
		if (stack.isEmpty()) {
			return false;
		}
		Item item = stack.getItem();

		switch (slot) {
		case 0:
			return item == ModItems.POWER_SUPPLY.get();
		case 1:
			return item == ModItems.MOTHERBOARD.get();
		case 2:
			return item == ModItems.CPU.get();
		case 3:
			return item == ModItems.GPU.get();
		case 4:
			return item == ModItems.HDD.get();
		case 5:
			return item == ModItems.RAM.get();
		}
		return false;
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (monitorTE != null && !level.isClientSide()) {
			CompoundNBT nbt = save(new CompoundNBT());
			for (PlayerEntity player : monitorTE.getLookingPlayers()) {
				PacketManager.sendTo(PacketManager.syncTileEntity,
						new SyncTileEntityMessage(nbt, worldPosition), (ServerPlayerEntity) player);
			}
		}
	}

	public void markForUpdate() {
		if(level != null) {
			level.sendBlockUpdated(worldPosition, level.getBlockState(this.worldPosition),
					level.getBlockState(worldPosition), 3);
		}
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

	public TileEntityMonitor getMonitorTE() {
		return monitorTE;
	}

	public void setMonitorTE(TileEntityMonitor monitorTE) {
		this.monitorTE = monitorTE;
	}



	public enum EnumState {
		OFF("off"), BOOT_SCREEN("boot"), STARTING("starting"), SIGNUP("signup"), LOGIN("login"), NORMAL("normal"),
		CRASH("crash");

		private String name;

		EnumState(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public static EnumState getEnum(String value) {
			for (EnumState st : EnumState.values()) {
				if (st.name.equalsIgnoreCase(value)) {
					return st;
				}
			}
			return EnumState.OFF;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BlockPos> getConnections() {
		return (List<BlockPos>) network.clone();
	}

	@Override
	public void connectTo(INetwork toConnect) {
		if (!isConnectedTo(toConnect)) {
			network.add(toConnect.getPosition());
			toConnect.connectTo(this);
			setChanged();
		}
	}

	@Override
	public void disconnect(INetwork toDisconnect) {
		if (isConnectedTo(toDisconnect)) {
			network.remove(toDisconnect.getPosition());
			toDisconnect.disconnect(this);
			setChanged();
		}
	}

	@Override
	public boolean isConnectedTo(INetwork net) {
		return network.contains(net.getPosition());
	}

	@Override
	public void disconnectAll() {
		for (BlockPos pos : getConnections()) {
			TileEntity te = level.getBlockEntity(pos);
			if (te instanceof INetwork) {
				((INetwork) te).disconnect(this);
			}
		}
	}

	@Override
	public BlockPos getPosition() {
		return this.getPos();
	}

	@Override
	public void sendPacket(String packet, INetwork from, PlayerEntity playerFrom) {

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
		return 4;
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
		if (!on)
			return 0;
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
	protected ItemHandlerNameable createInventory() {
		return new ItemHandlerNameable(INVENTORY_SIZE, DEFAULT_NAME);
	}


	@Override
	public void onContainerOpened(PlayerEntity player) {
		
	}

	@Override
	public void onContainerClosed(PlayerEntity player) {
		
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return this.level.getBlockEntity(this.worldPosition) == this && player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64;
	}

	public void setDisplayName(String displayName) {
		getInventory().setDisplayName(new StringTextComponent(displayName));
	}

	@Override
	public ResourceLocation getLootTable() {
		return null;
	}
	
	@Override
	public ITextComponent getDisplayName() {
		return this.getInventory().getDisplayName();
	}


	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return ContainerComputer.createContainerServerSide(windowID, playerInventory, this);
	}

	@Override
	public BlockPos getPos() {
		return this.worldPosition;
	}

}
