package nuparu.sevendaystomine.tileentity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.block.ContainerSmall;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;

public class TileEntityTrashBin extends TileEntityItemHandler<ItemHandlerNameable>
		implements ITickableTileEntity, IHopper {

	private static final int INVENTORY_SIZE = 9;
	private static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.trash_bin");

	static VoxelShape INSIDE = Block.box(2.0D, 11.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	static VoxelShape ABOVE = Block.box(0.0D, 16.0D, 0.0D, 16.0D, 32.0D, 16.0D);
	static VoxelShape SUCK = VoxelShapes.or(INSIDE, ABOVE);

	public TileEntityTrashBin() {
		super(ModTileEntities.TRASH_BIN.get());
	}

	@Override
	protected ItemHandlerNameable createInventory() {
		return new ItemHandlerNameable(INVENTORY_SIZE, DEFAULT_NAME);
	}

	@Override
	public void tick() {
		if (!level.isClientSide()) {
			List<ItemEntity> entities = HopperTileEntity.getItemsAtAndAbove(this);
			ItemStack stack = this.getInventory().getStackInSlot(0);
			for (ItemEntity entity : entities) {
				ItemStack entityStack = entity.getItem().copy();
				if (entityStack.isEmpty()) {
					continue;
				}
				if (isEmpty()) {
					this.getInventory().setStackInSlot(0, entityStack);
					entity.kill();
				} else if (entityStack.getItem() == stack.getItem()
						&& entityStack.getDamageValue() == stack.getDamageValue()
						&& ItemStack.tagMatches(entityStack, stack)) {
					int maxDelta = this.getInventory().getSlotLimit(0) - stack.getCount();
					int amount = Math.min(maxDelta, entityStack.getCount());
					entityStack.shrink(amount);
					stack.grow(amount);
					if (entityStack.getCount() <= 0) {
						entity.kill();
					}
				}
			}
		}

	}

	@Override
	public void onContainerOpened(PlayerEntity player) {

	}

	@Override
	public void onContainerClosed(PlayerEntity player) {

	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return this.level.getBlockEntity(this.worldPosition) == this
				&& player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5,
						this.worldPosition.getZ() + 0.5) <= 64;
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
		return ((ItemHandlerNameable) this.getInventory()).getDisplayName();
	}

	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return ContainerSmall.createContainerServerSide(windowID, playerInventory, this);
	}

	@Override
	public int getContainerSize() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public ItemStack getItem(int p_70301_1_) {
		return null;
	}

	@Override
	public ItemStack removeItem(int p_70298_1_, int p_70298_2_) {
		return null;
	}

	@Override
	public ItemStack removeItemNoUpdate(int p_70304_1_) {
		return null;
	}

	@Override
	public void setItem(int p_70299_1_, ItemStack p_70299_2_) {

	}

	@Override
	public boolean stillValid(PlayerEntity p_70300_1_) {
		return false;
	}

	@Override
	public void clearContent() {
	}

	@Override
	public double getLevelX() {
		return worldPosition.getX();
	}

	@Override
	public double getLevelY() {
		return worldPosition.getY();
	}

	@Override
	public double getLevelZ() {
		return worldPosition.getZ();
	}

}
