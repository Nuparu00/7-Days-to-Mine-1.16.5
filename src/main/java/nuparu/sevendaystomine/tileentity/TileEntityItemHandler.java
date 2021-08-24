package nuparu.sevendaystomine.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import nuparu.sevendaystomine.inventory.IContainerCallbacks;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.util.Utils;

public abstract class TileEntityItemHandler<INVENTORY extends ItemHandlerNameable>
		extends TileEntity implements IContainerCallbacks, INamedContainerProvider {

	public TileEntityItemHandler(TileEntityType<?> p_i48289_1_) {
		super(p_i48289_1_);
	}

	protected final LazyOptional<INVENTORY> inventory = LazyOptional.of(this::createInventory);

	protected abstract INVENTORY createInventory();

	@Nullable
	public INVENTORY getInventory() {
		return this.inventory.orElse(null);
	}

	public NonNullList<ItemStack> getDrops() {
		return Utils.dropItemHandlerContents(getInventory(), level.random);
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		if (getInventory() != null && compound.contains("ItemHandler")) {
			getInventory().deserializeNBT(compound.getCompound("ItemHandler"));
		}
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		if (getInventory() != null) {
			compound.put("ItemHandler", getInventory().serializeNBT());
		}
		return compound;
	}

	@Override
	@Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return inventory.cast();
		}
		return super.getCapability(cap, side);
	}

	public ResourceLocation getLootTable() {
		return null;
	}

	public void setDisplayName(ITextComponent displayName) {
		getInventory().setDisplayName(displayName);
	}
}
