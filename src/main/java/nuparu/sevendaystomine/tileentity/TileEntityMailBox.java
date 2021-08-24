package nuparu.sevendaystomine.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.block.ContainerSmall;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;

public class TileEntityMailBox extends TileEntityItemHandler<ItemHandlerNameable>{

	private static final int INVENTORY_SIZE = 9;
	private static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.mailbox");
	
	public TileEntityMailBox() {
		super(ModTileEntities.MAIL_BOX.get());
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
		return ContainerSmall.createContainerServerSide(windowID, playerInventory, this);
	}

}