package nuparu.sevendaystomine.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;

public abstract class TileEntitySafe extends TileEntityItemHandler<ItemHandlerNameable> implements ITickableTileEntity {

	public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.safe");

	private String customName;
	public boolean locked = true;
	public boolean init = false;
	


	public TileEntitySafe(TileEntityType<?> type) {
		super(type);
	}

	@Override
	protected ItemHandlerNameable createInventory() {
		return new ItemHandlerNameable(9, DEFAULT_NAME);
	}

	@Override
	public void onContainerOpened(PlayerEntity player) {
	}

	@Override
	public void onContainerClosed(PlayerEntity player) {
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return this.level.getBlockEntity(worldPosition) == this
				&& player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) <= 64;
	}


	@Override
	public ITextComponent getDisplayName() {
		return this.getInventory().getDisplayName();
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);

		
		if (compound.contains("CustomName", 8)) {
			this.customName = compound.getString("CustomName");
		}
		this.locked = compound.getBoolean("Locked");
		this.init = compound.getBoolean("Init");
		ListNBT nbtList = compound.getList("openedBy", Constants.NBT.TAG_INT);
		for (int i = 0; i < nbtList.size(); i++) {
			int id = nbtList.getInt(i);
			Entity entity =level.getEntity(id);
		}
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		 super.save(compound);
		compound.putBoolean("Locked", this.locked);
		compound.putBoolean("Init", this.init);
		ListNBT nbtList = new ListNBT();
		compound.put("openedBy", nbtList);

		return compound;
	}

	public void setCustomInventoryName(String name) {
		this.customName = name;
	}

	@Override
	public void tick() {

	}

	public abstract boolean tryToUnlock();

	public void unlock() {

	}

	public void lock() {

	}
}
