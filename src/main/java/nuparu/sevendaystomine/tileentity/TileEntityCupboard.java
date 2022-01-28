package nuparu.sevendaystomine.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.block.ContainerSmall;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.util.MathUtils;

public class TileEntityCupboard extends TileEntityItemHandler<ItemHandlerNameable>{

	private static final int INVENTORY_SIZE = 9;
	private static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.cupboard");
	protected int openCount;

	public TileEntityCupboard() {
		super(ModTileEntities.CUPBOARD.get());
	}
	
	
	@Override
	protected ItemHandlerNameable createInventory() {
		return new ItemHandlerNameable(INVENTORY_SIZE, DEFAULT_NAME);
	}

	@Override
	public void onContainerOpened(PlayerEntity player) {
		if (this.openCount == 0) {
			this.level.playSound((PlayerEntity)null, worldPosition.getX()+0.5, worldPosition.getY()+0.5, worldPosition.getZ()+0.5, ModSounds.CABINET_OPEN.get(), SoundCategory.BLOCKS, MathUtils.getFloatInRange(0.45f,0.55f), MathUtils.getFloatInRange(0.75f,1.15f));
		}
		this.openCount++;
	}

	@Override
	public void onContainerClosed(PlayerEntity player) {
		this.openCount--;
		if (this.openCount == 0) {
			this.level.playSound((PlayerEntity)null, worldPosition.getX()+0.5, worldPosition.getY()+0.5, worldPosition.getZ()+0.5, ModSounds.CABINET_CLOSE.get(), SoundCategory.BLOCKS, MathUtils.getFloatInRange(0.45f,0.55f), MathUtils.getFloatInRange(0.75f,1.15f));
		}
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
		return this.getInventory().getDisplayName();
	}


	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return ContainerSmall.createContainerServerSide(windowID, playerInventory, this);
	}

}