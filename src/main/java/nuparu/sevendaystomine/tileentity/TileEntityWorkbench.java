package nuparu.sevendaystomine.tileentity;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.block.ContainerWorkbench;
import nuparu.sevendaystomine.inventory.block.ContainerWorkbenchUncrafting;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;

public class TileEntityWorkbench extends TileEntityItemHandler<ItemHandlerNameable> {


	private static final int INVENTORY_SIZE = 1;
	private static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.workbench");

	public TileEntityWorkbench() {
		super(ModTileEntities.WORKBENCH.get());
	}
	
	@Override
	protected ItemHandlerNameable createInventory() {
		return new ItemHandlerNameable(INVENTORY_SIZE, DEFAULT_NAME){
			@Override
		    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
		    {
		        return !stack.isEmpty() && stack.getItem() == ModItems.IRON_SCRAP.get();
		    }
		};
	}
	
/*
	@Override
	public Container createContainer(PlayerEntity player) {
		final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player
				.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP);
		final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory,
				playerInventory);
		if (player.isCrouching()) {
			return new ContainerWorkbench(player, playerInventoryWrapper, getInventory(), this, this.getPos());
		} else {
			return new ContainerWorkbenchUncrafting(player, playerInventoryWrapper, getInventory(), this,
					this.getPos());
		}
	}
	
	public Container createContainer(PlayerEntity player, boolean crafting) {
		final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player
				.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP);
		final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory,
				playerInventory);
		
		if (crafting) {
			return new ContainerWorkbench(player, playerInventoryWrapper, getInventory(), this, this.getPos());
		} else {
			return new ContainerWorkbenchUncrafting(player, playerInventoryWrapper, getInventory(), this,
					this.getPos());
		}
	}*/

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
		return this.getInventory().hasCustomName() ? this.getInventory().getDisplayName() : DEFAULT_NAME;
	}

	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity p_createMenu_3_) {

		return playerInventory.player.isCrouching() ? ContainerWorkbenchUncrafting.createContainerServerSide(windowID, playerInventory, this,worldPosition) : ContainerWorkbench.createContainerServerSide(windowID, playerInventory, this,worldPosition);
	}

}