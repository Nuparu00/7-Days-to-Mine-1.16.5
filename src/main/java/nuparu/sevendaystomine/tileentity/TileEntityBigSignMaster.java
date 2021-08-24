package nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.init.ModTileEntities;

public class TileEntityBigSignMaster extends SignTileEntity
{

	protected List<BlockPos> slaves = new ArrayList<BlockPos>();

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);

		ListNBT list = new ListNBT();
		for (BlockPos blockPos : this.slaves) {
			list.add(NBTUtil.writeBlockPos(blockPos));
		}
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		if (!compound.contains("list", Constants.NBT.TAG_LIST))
			return;

		ListNBT list = compound.getList("list", Constants.NBT.TAG_COMPOUND);
		this.slaves.clear();
		for (int i = 0; i < list.size(); i++) {
			this.slaves.add(NBTUtil.readBlockPos(list.getCompound(i)));
		}

	}

	public void addSlave(BlockPos blockPos) {
		slaves.add(blockPos);
	}

	public List<BlockPos> getSlaves() {
		return slaves;
	}

	public TileEntityType<?> getType() {
		return ModTileEntities.BIG_SIGN_MASTER.get();
	}
}
