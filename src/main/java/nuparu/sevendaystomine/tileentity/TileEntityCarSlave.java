package nuparu.sevendaystomine.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;

public class TileEntityCarSlave extends TileEntityCar{

	public BlockPos masterPos;

	private TileEntityCarMaster masterTE;
	private int index = 0;

	public TileEntityCarSlave() {
		super(ModTileEntities.CAR_SLAVE.get());
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);

		if (compound.contains("masterPos")) {
			this.masterPos = BlockPos.of(compound.getLong("masterPos"));
		} else {
			this.masterPos = null;
		}

		if (level != null && masterPos != null) {
			TileEntity TE = level.getBlockEntity(masterPos);
			if (TE != null && TE instanceof TileEntityCarMaster) {
				masterTE = (TileEntityCarMaster) TE;
			}
		}
		this.index = compound.getInt("index");
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);


		if (masterPos != null) {
			compound.putLong("masterPos", this.masterPos.asLong());
		}

		compound.putInt("index", this.index);

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

	public void setIndex(int i) {
		this.index = i;
		markForUpdate();
	}

	public int getIndex() {
		return this.index;
	}

	public void setMaster(BlockPos pos, TileEntityCarMaster masterTE) {
		if (!level.isClientSide()) {
			this.masterPos = pos;
			this.masterTE = masterTE;
		}
		markForUpdate();
	}

	@Override
	public TileEntityCarMaster getMaster() {
		if (this.masterTE == null && masterPos != null) {
			TileEntity TE = level.getBlockEntity(masterPos);
			if (TE instanceof TileEntityCarMaster) {
				this.masterTE = (TileEntityCarMaster) TE;
			}
		}
		return this.masterTE;
	}


}
