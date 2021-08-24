package nuparu.sevendaystomine.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import nuparu.sevendaystomine.block.BlockBookshelfEnhanced;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.block.ContainerForge;
import nuparu.sevendaystomine.inventory.block.ContainerSmall;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;

public class TileEntityBookshelf extends TileEntityItemHandler<ItemHandlerNameable> {

	private static final int INVENTORY_SIZE = 9;
	private static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.bookshelf");
	
	public TileEntityBookshelf() {
		super(ModTileEntities.BOOKSHELF.get());
	}
	
	
	@Override
	protected ItemHandlerNameable createInventory() {
		final TileEntityBookshelf te = this;
		return new ItemHandlerNameable(INVENTORY_SIZE, DEFAULT_NAME){

			@Override
			protected void onContentsChanged(int slot)
			{
				super.onContentsChanged(slot);
				te.updateBlock();
			}
		};
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

	public boolean isEmpty() {
		for (int i = 0; i < this.getInventory().getSlots(); i++ ){
			ItemStack itemstack = this.getInventory().getStackInSlot(i);
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public void updateBlock() {
		if(level.isClientSide()) return;
		BlockState blockState = this.level.getBlockState(this.worldPosition);
		boolean state = blockState.getValue(BlockBookshelfEnhanced.FULL);
		if (isEmpty() && state) {
			level.setBlock(worldPosition, blockState.setValue(BlockBookshelfEnhanced.FULL, false),3);
		} else if (!isEmpty() && !state) {
			level.setBlock(worldPosition, blockState.setValue(BlockBookshelfEnhanced.FULL, true),3);
		}
	}

}