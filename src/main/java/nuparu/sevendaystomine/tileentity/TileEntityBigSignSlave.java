package nuparu.sevendaystomine.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import nuparu.sevendaystomine.init.ModTileEntities;

public class TileEntityBigSignSlave extends TileEntity {

	protected BlockPos parent = BlockPos.ZERO;
	protected boolean slave = false;

	public TileEntityBigSignSlave() {
		super(ModTileEntities.BIG_SIGN_SLAVE.get());
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.putLong("parent", this.parent.asLong());
		compound.putBoolean("slave", this.slave);
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.slave = compound.getBoolean("slave");
		this.parent = BlockPos.of(compound.getLong("parent"));
	}

	public void setParent(BlockPos pos) {
		this.parent = pos;
	}

	public BlockPos getParent() {
		return this.parent;
	}
}
