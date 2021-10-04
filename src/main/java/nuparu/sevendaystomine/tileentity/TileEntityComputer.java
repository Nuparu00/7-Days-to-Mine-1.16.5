package nuparu.sevendaystomine.tileentity;

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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.LongNBT;
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
import nuparu.sevendaystomine.computer.HardDrive;
import nuparu.sevendaystomine.computer.process.BootingProcess;
import nuparu.sevendaystomine.computer.process.CreateAccountProcess;
import nuparu.sevendaystomine.computer.process.MacCreateAccountProcess;
import nuparu.sevendaystomine.computer.process.MacDesktopProcess;
import nuparu.sevendaystomine.computer.process.ProcessRegistry;
import nuparu.sevendaystomine.computer.process.TickingProcess;
import nuparu.sevendaystomine.computer.process.WindowedProcess;
import nuparu.sevendaystomine.computer.process.WindowsCreateAccountProcess;
import nuparu.sevendaystomine.computer.process.WindowsDesktopProcess;
import nuparu.sevendaystomine.computer.process.WindowsLoginProcess;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.EnumDeviceType;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.electricity.network.INetwork;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.block.ContainerComputer;
import nuparu.sevendaystomine.inventory.block.ContainerSmall;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.SyncTileEntityMessage;
import nuparu.sevendaystomine.util.ModConstants;

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

	// processes
	private ArrayList<TickingProcess> processes = new ArrayList<TickingProcess>();
	public Thread codeBus = null;
	// Persistent data
	private boolean registered = false;
	private String username = "";
	private String password = "";
	private String hint = "";

	// hard drive
	@Nullable
	private HardDrive hardDrive;

	private ArrayList<BlockPos> network = new ArrayList<BlockPos>();

	public TileEntityComputer() {
		super(ModTileEntities.COMPUTER.get());
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

	public HardDrive getHardDrive() {
		return this.hardDrive;
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
		processes.clear();
		startProcess(new BootingProcess());
	}

	public void stopComputer() {
		setState(EnumState.OFF);
		processes.clear();
		this.markForUpdate();
	}

	@SuppressWarnings("unchecked")
	public void startProcess(TickingProcess process) {
		process.setComputer(this);

		if (level != null && level.isClientSide()) {
			if (process instanceof WindowedProcess) {
				int order = 0;
				for (TickingProcess tp : (ArrayList<TickingProcess>) this.getProcessesList().clone()) {
					if (tp instanceof WindowedProcess) {
						order++;
					}
				}
				if (((WindowedProcess) process).getWindowOrder() == -1) {
					((WindowedProcess) process).setWindowOrder(order);
				}
			}
		}
		processes.add(process);
		this.markForUpdate();
	}

	public void startProcess(CompoundNBT nbt, boolean sync) {
		if (isCompleted() && isOn()) {
			ArrayList<TickingProcess> processes2 = getProcessesList();

			if (!nbt.contains(ProcessRegistry.RES_KEY)) {
				return;
			}

			boolean flag = false;

			for (TickingProcess process : processes2) {
				if (nbt.hasUUID("id") && process.getId().equals(nbt.getUUID("id"))) {
					process.readFromNBT(nbt);
					process.setComputer(this);
					flag = true;
					break;
				}
			}

			if (flag) {
				this.markForUpdate();
				return;
			}
			if (sync) {
				return;
			}
			String res = nbt.getString(ProcessRegistry.RES_KEY);
			TickingProcess process = ProcessRegistry.INSTANCE.getByRes(new ResourceLocation(res));
			if (process != null) {
				process.readFromNBT(nbt);
				startProcess(process);
			}
		}

	}

	public void killProcess(TickingProcess process) {
		processes.remove(process);
		this.markForUpdate();
	}

	public TickingProcess getProcessByUUID(UUID id) {
		for (TickingProcess process : getProcessesList()) {
			if (process.getId().equals(id)) {
				return process;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<TickingProcess> getProcessesList() {
		return (ArrayList<TickingProcess>) (processes.clone());
	}

	public void onBootFinished() {
		if (hardDrive == null) {
			hardDrive = new HardDrive(this);
		}
		setState(EnumState.LOGIN);
		if (!isRegistered()) {
			switch (system) {
			default:
				startProcess(new WindowsCreateAccountProcess());
				break;

			case MAC:
				startProcess(new MacCreateAccountProcess());
				break;
			}

		} else {
			if (password.isEmpty()) {
				setState(EnumState.NORMAL);
				switch (system) {
				default:
					startProcess(new WindowsDesktopProcess());
					break;

				case MAC:
					startProcess(new MacDesktopProcess());
					break;
				}
			} else {
				startProcess(new WindowsLoginProcess());
			}
		}
	}

	public void onLogin(WindowsLoginProcess process) {
		if (verifyPassword(process.password)) {
			setState(EnumState.NORMAL);
			switch (system) {
			default:
				startProcess(new WindowsDesktopProcess());
				break;

			case MAC:
				startProcess(new MacDesktopProcess());
				break;
			}
			killProcess(process);
		}
	}

	public void onAccountCreated(CreateAccountProcess process) {
		setState(EnumState.NORMAL);
		setRegistered(true);
		switch (system) {
		default:
			startProcess(new WindowsDesktopProcess());
			break;

		case MAC:
			startProcess(new MacDesktopProcess());
			break;
		}

		this.username = process.username;
		this.password = process.password;
		this.hint = process.hint;
		killProcess(process);
	}

	@Override
	public void tick() {

		if (level.isClientSide()) {
			return;
		}
		if (isOn()) {
			if (!on || !isCompleted() || this.voltage < this.getRequiredPower()) {
				stopComputer();
				return;
			}
			this.voltage -= this.getRequiredPower();
		} else if (on && isCompleted() && this.voltage > this.getRequiredPower()) {
			this.startComputer();
		}
		if (!level.isClientSide()) {
			for (TickingProcess process : getProcessesList()) {
				process.tick();
			}
		}

	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.state = EnumState.getEnum(compound.getString("state"));
		this.system = EnumSystem.getEnum(compound.getString("system"));
		this.voltage = compound.getLong("power");

		this.username = compound.getString("username");
		this.password = compound.getString("password");
		this.hint = compound.getString("hint");
		this.on = compound.getBoolean("on");
		this.setRegistered(compound.getBoolean("registered"));

		ListNBT list = compound.getList("processes", Constants.NBT.TAG_COMPOUND);

		ArrayList<TickingProcess> processes2 = getProcessesList();

		processes.clear();

		for (int i = 0; i < list.size(); ++i) {
			CompoundNBT nbt = list.getCompound(i);
			if (!nbt.contains(ProcessRegistry.RES_KEY)) {
				continue;
			}

			boolean flag = false;

			for (TickingProcess process : processes2) {
				if (nbt.contains("id") && process.getId().compareTo(nbt.getUUID("id")) == 0) {
					process.readFromNBT(nbt);
					startProcess(process);
					flag = true;
					break;
				}
			}

			if (flag) {
				continue;
			}

			String res = nbt.getString(ProcessRegistry.RES_KEY);
			TickingProcess process = ProcessRegistry.INSTANCE.getByRes(new ResourceLocation(res));
			if (process != null) {
				process.readFromNBT(nbt);
				startProcess(process);
			}
		}

		if (compound.contains("drive")) {
			if (this.hardDrive != null) {
				this.hardDrive.readFromNBT(compound.getCompound("drive"));
			} else {
				this.hardDrive = new HardDrive(this);
				this.hardDrive.readFromNBT(compound.getCompound("drive"));
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
		compound.putString("system", system.getName());
		compound.putLong("power", voltage);

		compound.putString("username", username);
		compound.putString("password", password);
		compound.putString("hint", hint);
		compound.putBoolean("on", on);

		compound.putBoolean("registered", isRegistered());
		ListNBT list = new ListNBT();
		for (TickingProcess process : (ArrayList<TickingProcess>) processes.clone()) {
			list.add(process.save(new CompoundNBT()));
		}
		compound.put("processes", list);

		if (hardDrive != null) {
			compound.put("drive", hardDrive.save(new CompoundNBT()));
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
			if (isPartCorrect(i) == false) {
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

	public TileEntityMonitor getMonitorTE() {
		return monitorTE;
	}

	public void setMonitorTE(TileEntityMonitor monitorTE) {
		this.monitorTE = monitorTE;
	}

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public String getUsername() {
		return username;
	}

	public String getHint() {
		return hint;
	}

	public boolean verifyPassword(String s) {
		return s.equals(password);
	}

	public enum EnumState {
		OFF("off"), BOOT_SCREEN("boot"), STARTING("starting"), SIGNUP("signup"), LOGIN("login"), NORMAL("normal"),
		CRASH("crash");

		private String name;

		private EnumState(String name) {
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

	public enum EnumSystem {
		NONE("none", ""), MAC("mac", "Mac OS X"), LINUX("linux", "Linux"), WIN98("win98", "Windows 98"),
		WINXP("winXp", "Windows XP"), WIN7("win7", "Windows 7"), WIN8("win8", "Windows 8"),
		WIN10("win10", "Windows 10"), TERMINAL("terminal", "Terminal");

		private String name;
		private String readable;

		private EnumSystem(String name, String readable) {
			this.name = name;
			this.readable = readable;
		}

		public String getName() {
			return name;
		}

		public String getReadeable() {
			return this.readable;
		}

		public static EnumSystem getEnum(String value) {
			for (EnumSystem st : EnumSystem.values()) {
				if (st.name.equalsIgnoreCase(value)) {
					return st;
				}
			}
			return EnumSystem.NONE;
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

	public void createGenericAccount() {
		if (this.isRegistered())
			return;
		setRegistered(true);
		this.username = "Admin";
		this.password = "Admin";
		this.hint = "Admin";
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
		return 12;
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
			lost = (long) Math.round(delta * ModConstants.DROP_PER_BLOCK * connection.getDistance());
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
		return ((ItemHandlerNameable)this.getInventory()).getDisplayName();
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
