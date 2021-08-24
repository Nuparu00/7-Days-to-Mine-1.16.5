package nuparu.sevendaystomine.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.client.util.ResourcesHelper.Image;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.block.ContainerScreenProjector;
import nuparu.sevendaystomine.inventory.block.ContainerTiny;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.item.ItemPhoto;

public class TileEntityScreenProjector extends TileEntityItemHandler<ItemHandlerNameable>{

	private int horizontalOffset = 0;
	private int verticalOffset = 0;
	
	@OnlyIn(Dist.CLIENT)
	public Image image;
	@OnlyIn(Dist.CLIENT)
	public long nextUpdate;

	private static final int INVENTORY_SIZE = 9;
	private static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.projector");
	
	public TileEntityScreenProjector() {
		super(ModTileEntities.SCREEN_PROJECTOR.get());
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public net.minecraft.util.math.AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);

		this.horizontalOffset = compound.getInt("horizontalOffset");
		this.verticalOffset = compound.getInt("verticalOffset");
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		 super.save(compound);
		compound.putInt("horizontalOffset", horizontalOffset);
		compound.putInt("verticalOffset", verticalOffset);

		return compound;
	}

	public boolean hasValidImage() {
		ItemStack stack = this.getInventory().getStackInSlot(0);
		if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof ItemPhoto)
				|| stack.getOrCreateTag() == null)
			return false;

		return stack.getOrCreateTag().contains("path", Constants.NBT.TAG_STRING);
	}

	public String getImagePath() {
		if (!hasValidImage())
			return "";
		ItemStack stack = this.getInventory().getStackInSlot(0);
		return stack.getOrCreateTag().getString("path");
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
		return ContainerScreenProjector.createContainerServerSide(windowID, playerInventory, this);
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
}
