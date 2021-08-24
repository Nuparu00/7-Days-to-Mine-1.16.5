package nuparu.sevendaystomine.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.util.Utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityCarMaster extends TileEntityCar{

	private static final int INVENTORY_SIZE = 9;
	private static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.car");
	
	public TileEntityCarMaster() {
		super(ModTileEntities.CAR_MASTER.get());
	}
	
	
	@Override
	public TileEntityCarMaster getMaster() {
		return this;
	}

	protected final LazyOptional<ItemHandlerNameable> inventory = LazyOptional.of(this::createInventory);

	protected ItemHandlerNameable createInventory() {
		return new ItemHandlerNameable(INVENTORY_SIZE, DEFAULT_NAME);
	}


	@Nullable
	public ItemHandlerNameable getInventory() {
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


	public boolean isUsableByPlayer(PlayerEntity player) {
		return this.level.getBlockEntity(this.worldPosition) == this && player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64;
	}

	public void setDisplayName(String displayName) {
		getInventory().setDisplayName(new StringTextComponent(displayName));
	}


}
