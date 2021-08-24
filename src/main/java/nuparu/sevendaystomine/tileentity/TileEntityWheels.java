package nuparu.sevendaystomine.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.item.ItemQuality;
import nuparu.sevendaystomine.util.MathUtils;

public class TileEntityWheels extends TileEntity {

	protected ItemStack stack = ItemStack.EMPTY;

	public TileEntityWheels() {
		super(ModTileEntities.WHEELS.get());
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		if (compound.contains("stack", Constants.NBT.TAG_COMPOUND)) {
			this.stack = ItemStack.of(compound.getCompound("stack"));
		} else {
			this.stack = generateItemStack();
		}
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.put("stack", this.stack.serializeNBT());

		return compound;
	}

	public void setItemStack(ItemStack stack) {
		this.stack = stack.copy();
		this.stack.setCount(1);
	}

	public ItemStack getItemStack() {
		return this.stack;
	}

	public ItemStack generateItemStack() {
		ItemStack is = new ItemStack(ModBlocks.WHEELS.get());
		this.stack = ItemQuality.setQualityForStack(is, MathUtils.getIntInRange(1, CommonConfig.maxQuality.get()));
		return is;
	}

}
