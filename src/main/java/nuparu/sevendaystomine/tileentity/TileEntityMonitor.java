package nuparu.sevendaystomine.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import nuparu.sevendaystomine.block.BlockMonitor;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.EnumDeviceType;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.block.ContainerMonitor;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.SyncTileEntityMessage;
import nuparu.sevendaystomine.tileentity.TileEntityComputer.EnumSystem;
import nuparu.sevendaystomine.util.ModConstants;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileEntityMonitor extends TileEntity implements ITickableTileEntity, IVoltage, INamedContainerProvider {

    private boolean on = false;
    private long voltage = 0;
    private long capacity = 40;
    private BlockPos compPos = BlockPos.ZERO;
    private TileEntityComputer computerTE;

    private ArrayList<PlayerEntity> lookingPlayers = new ArrayList<PlayerEntity>();

    public TileEntityMonitor() {
        super(ModTileEntities.MONITOR.get());
    }

    @SuppressWarnings("unchecked")
    public ArrayList<PlayerEntity> getLookingPlayers() {
        return (ArrayList<PlayerEntity>) lookingPlayers.clone();
    }

    public void addPlayer(PlayerEntity player) {
        if (!lookingPlayers.contains(player)) {
            lookingPlayers.add(player);
            CompoundNBT nbt = save(new CompoundNBT());

            PacketManager.sendTo(PacketManager.syncTileEntity,
                    new SyncTileEntityMessage(save(new CompoundNBT()), worldPosition), (ServerPlayerEntity) player);

            if (computerTE != null) {

                nbt = computerTE.save(new CompoundNBT());

                PacketManager.sendTo(PacketManager.syncTileEntity,
                        new SyncTileEntityMessage(computerTE.save(new CompoundNBT()), computerTE.getPos()),
                        (ServerPlayerEntity) player);
            }
        }
    }

    public void removePlayer(PlayerEntity player) {
        lookingPlayers.remove(player);
    }

    @Override
    public void tick() {

        // Check for available computer

        if (computerTE == null) {
            if (compPos != BlockPos.ZERO) {
                TileEntity TE = level.getBlockEntity(worldPosition.offset(compPos));
                if (TE != null && TE instanceof TileEntityComputer
                        && ((TileEntityComputer) TE).getMonitorTE() == null) {
                    computerTE = (TileEntityComputer) TE;
                    computerTE.setMonitorTE(this);
                } else {
                    compPos = BlockPos.ZERO;
                }
            } else {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        for (int k = -1; k <= 1; k++) {
                            if (i == 0 && j == 0 && k == 0)
                                continue;

                            BlockPos pos2 = worldPosition.offset(i, j, k);
                            TileEntity TE = level.getBlockEntity(pos2);

                            if (TE != null && TE instanceof TileEntityComputer) {
                                TileEntityComputer compTE = (TileEntityComputer) TE;
                                if (compTE.getMonitorTE() != null) {
                                    BlockPos pos3 = compTE.getMonitorTE().getPos();
                                    if (pos3.getX() == worldPosition.getX() && pos3.getY() == worldPosition.getY()
                                            && pos3.getZ() == worldPosition.getZ()) {
                                        computerTE = compTE;
                                        computerTE.setMonitorTE(this);
                                        compPos = pos2.subtract(worldPosition);
                                        this.markForUpdate();
                                    }
                                } else if (compTE.getMonitorTE() == null) {
                                    computerTE = compTE;
                                    computerTE.setMonitorTE(this);
                                    compPos = pos2.subtract(worldPosition);
                                    this.markForUpdate();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (level.isClientSide()) {
            return;
        }

        if (computerTE != null && level.getBlockEntity(worldPosition.offset(compPos)) == null) {
            computerTE = null;
            compPos = BlockPos.ZERO;
        }

        // handles the block/texture change
        BlockState state = level.getBlockState(worldPosition);
        Block block = state.getBlock();
        CompoundNBT nbt = this.save(new CompoundNBT());
        if (computerTE != null) {
            if (this.isOn() && computerTE.isOn() && computerTE.isCompleted()) {
                EnumSystem system = computerTE.getSystem();
                if (system == EnumSystem.NONE) {
                    if (block != ModBlocks.MONITOR_OFF.get()) {
                        level.setBlockAndUpdate(worldPosition,
                                applyPropertiesToState(ModBlocks.MONITOR_OFF.get(), state));
                        level.getBlockEntity(worldPosition).load(level.getBlockState(worldPosition), nbt);
                        if (computerTE != null && level.getBlockEntity(worldPosition) instanceof TileEntityMonitor) {
                            TileEntityMonitor te = (TileEntityMonitor) level.getBlockEntity(worldPosition);
                            computerTE.setMonitorTE(te);
                            te.computerTE = computerTE;
                        }
                    }
                } else if (system == EnumSystem.LINUX) {
                    if (block != ModBlocks.MONITOR_LINUX.get()) {
                        level.setBlockAndUpdate(worldPosition,
                                applyPropertiesToState(ModBlocks.MONITOR_LINUX.get(), state));
                        level.getBlockEntity(worldPosition).load(level.getBlockState(worldPosition), nbt);
                        if (computerTE != null && level.getBlockEntity(worldPosition) instanceof TileEntityMonitor) {
                            TileEntityMonitor te = (TileEntityMonitor) level.getBlockEntity(worldPosition);
                            computerTE.setMonitorTE(te);
                            te.computerTE = computerTE;
                        }
                    }
                } else if (system == EnumSystem.MAC) {
                    if (block != ModBlocks.MONITOR_MAC.get()) {
                        level.setBlockAndUpdate(worldPosition,
                                applyPropertiesToState(ModBlocks.MONITOR_MAC.get(), state));
                        level.getBlockEntity(worldPosition).load(level.getBlockState(worldPosition), nbt);
                        if (computerTE != null && level.getBlockEntity(worldPosition) instanceof TileEntityMonitor) {
                            TileEntityMonitor te = (TileEntityMonitor) level.getBlockEntity(worldPosition);
                            computerTE.setMonitorTE(te);
                            te.computerTE = computerTE;
                        }
                    }
                } else if (system == EnumSystem.WIN98) {
                    if (block != ModBlocks.MONITOR_WIN98.get()) {
                        level.setBlockAndUpdate(worldPosition,
                                applyPropertiesToState(ModBlocks.MONITOR_WIN98.get(), state));
                        level.getBlockEntity(worldPosition).load(level.getBlockState(worldPosition), nbt);
                        if (computerTE != null && level.getBlockEntity(worldPosition) instanceof TileEntityMonitor) {
                            TileEntityMonitor te = (TileEntityMonitor) level.getBlockEntity(worldPosition);
                            computerTE.setMonitorTE(te);
                            te.computerTE = computerTE;
                        }
                    }
                } else if (system == EnumSystem.WINXP) {
                    if (block != ModBlocks.MONITOR_WINXP.get()) {
                        level.setBlockAndUpdate(worldPosition,
                                applyPropertiesToState(ModBlocks.MONITOR_WINXP.get(), state));
                        level.getBlockEntity(worldPosition).load(level.getBlockState(worldPosition), nbt);
                        if (computerTE != null && level.getBlockEntity(worldPosition) instanceof TileEntityMonitor) {
                            TileEntityMonitor te = (TileEntityMonitor) level.getBlockEntity(worldPosition);
                            computerTE.setMonitorTE(te);
                            te.computerTE = computerTE;
                        }
                    }
                } else if (system == EnumSystem.WIN7) {
                    if (block != ModBlocks.MONITOR_WIN7.get()) {
                        level.setBlockAndUpdate(worldPosition,
                                applyPropertiesToState(ModBlocks.MONITOR_WIN7.get(), state));
                        level.getBlockEntity(worldPosition).load(level.getBlockState(worldPosition), nbt);
                        if (computerTE != null && level.getBlockEntity(worldPosition) instanceof TileEntityMonitor) {
                            TileEntityMonitor te = (TileEntityMonitor) level.getBlockEntity(worldPosition);
                            computerTE.setMonitorTE(te);
                            te.computerTE = computerTE;
                        }
                    }
                } else if (system == EnumSystem.WIN8) {
                    if (block != ModBlocks.MONITOR_WIN8.get()) {
                        level.setBlockAndUpdate(worldPosition,
                                applyPropertiesToState(ModBlocks.MONITOR_WIN8.get(), state));
                        level.getBlockEntity(worldPosition).load(level.getBlockState(worldPosition), nbt);
                        if (computerTE != null && level.getBlockEntity(worldPosition) instanceof TileEntityMonitor) {
                            TileEntityMonitor te = (TileEntityMonitor) level.getBlockEntity(worldPosition);
                            computerTE.setMonitorTE(te);
                            te.computerTE = computerTE;
                        }
                    }
                } else if (system == EnumSystem.WIN10) {
                    if (block != ModBlocks.MONITOR_WIN10.get()) {
                        level.setBlockAndUpdate(worldPosition,
                                applyPropertiesToState(ModBlocks.MONITOR_WIN10.get(), state));
                        level.getBlockEntity(worldPosition).load(level.getBlockState(worldPosition), nbt);
                        if (computerTE != null && level.getBlockEntity(worldPosition) instanceof TileEntityMonitor) {
                            TileEntityMonitor te = (TileEntityMonitor) level.getBlockEntity(worldPosition);
                            computerTE.setMonitorTE(te);
                            te.computerTE = computerTE;
                        }
                    }
                }
                this.voltage -= this.getRequiredPower();
            } else {
                if (block != ModBlocks.MONITOR_OFF.get()) {
                    level.setBlockAndUpdate(worldPosition, applyPropertiesToState(ModBlocks.MONITOR_OFF.get(), state));
                    level.getBlockEntity(worldPosition).load(level.getBlockState(worldPosition), nbt);
                    if (computerTE != null && level.getBlockEntity(worldPosition) instanceof TileEntityMonitor) {
                        TileEntityMonitor te = (TileEntityMonitor) level.getBlockEntity(worldPosition);
                        computerTE.setMonitorTE(te);
                        te.computerTE = computerTE;
                    }
                }
            }
        } else {
            if (block != ModBlocks.MONITOR_OFF.get()) {
                level.setBlockAndUpdate(worldPosition, applyPropertiesToState(ModBlocks.MONITOR_OFF.get(), state));
                level.getBlockEntity(worldPosition).load(level.getBlockState(worldPosition), nbt);
                if (computerTE != null && level.getBlockEntity(worldPosition) instanceof TileEntityMonitor) {
                    TileEntityMonitor te = (TileEntityMonitor) level.getBlockEntity(worldPosition);
                    computerTE.setMonitorTE(te);
                    te.computerTE = computerTE;
                }
            }
        }

    }

    public boolean isOn() {
        return this.on && this.voltage >= this.getRequiredPower();
    }

    public boolean isUsableByPlayer(PlayerEntity player) {
        return this.level.getBlockEntity(this.worldPosition) == this && player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64;
    }

    private BlockState applyPropertiesToState(Block newState, BlockState oldState) {
        return applyPropertiesToState(newState.defaultBlockState(), oldState);
    }

    private BlockState applyPropertiesToState(BlockState newState, BlockState oldState) {
        return newState.setValue(BlockMonitor.FACING, oldState.getValue(BlockMonitor.FACING));
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
        this.compPos = BlockPos.of(compound.getLong("compPos"));
        this.on = compound.getBoolean("on");
        this.voltage = compound.getLong("power");
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putLong("compPos", compPos.asLong());
        compound.putBoolean("on", on);
        compound.putLong("power", voltage);
        return compound;
    }

    public boolean getState() {
        return this.on;
    }

    public void setState(boolean state) {
        this.on = state;
        markForUpdate();
    }

    public TileEntityComputer getComputer() {
        return this.computerTE;
    }

    public void setComputer(TileEntityComputer computer) {
        this.computerTE = computer;
        this.compPos = computer.getPos().subtract(worldPosition);
        markForUpdate();
    }

    public BlockPos getComputerPos() {
        return this.compPos;
    }

    public void setComputerPos(BlockPos pos2) {
        this.compPos = pos2;
        markForUpdate();
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
        return 6;
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
        if (!this.on)
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
    public BlockPos getPos() {
        return this.worldPosition;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Monitor");
    }

    @Nullable
    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity p_createMenu_3_) {
        return ContainerMonitor.createContainerServerSide(windowID, playerInventory, this);
    }
}
