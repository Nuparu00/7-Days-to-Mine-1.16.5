package nuparu.sevendaystomine.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.block.ContainerPrinter;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;

public class TileEntityPrinter extends TileEntityItemHandler<ItemHandlerNameable> implements ITickableTileEntity {

	//how much ink in the printer
	private int ink = 0;

	public static final int CAPACITY = 100;

	private static final int INVENTORY_SIZE = 3;


	private static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.printer");

	public TileEntityPrinter() {
		super(ModTileEntities.PRINTER.get());
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);

		this.ink = compound.getInt("ink");
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		 super.save(compound);
		compound.putInt("ink", ink);

		return compound;
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
		return this.getInventory().getDisplayName();
	}


	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return ContainerPrinter.createContainerServerSide(windowID, playerInventory, this);
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
        return save(new CompoundNBT());
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

	public int getInk(){
		return this.ink;
	}

	public void setInk(int newInk){
		this.ink = Math.min(newInk,CAPACITY);
	}

	public void addInk(int delta){
		setInk(ink+delta);
	}

	@Override
	public void tick() {
		if(level.isClientSide()) return;
		if(this.ink < CAPACITY){
			ItemStack inkSlot = this.getInventory().getStackInSlot(0);
			if(!inkSlot.isEmpty() && inkSlot.getItem() == Items.INK_SAC){
				addInk(5);
				inkSlot.shrink(1);
				if(inkSlot.getCount() <= 0){
					this.getInventory().setStackInSlot(0,ItemStack.EMPTY);
				}
				markForUpdate();
			}
		}
	}
}
