package nuparu.sevendaystomine.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import nuparu.sevendaystomine.block.BlockTorchEnhanced;
import nuparu.sevendaystomine.block.BlockTorchEnhancedWall;
import nuparu.sevendaystomine.init.ModTileEntities;

public class TileEntityTorch extends TileEntity implements ITickableTileEntity {
	public TileEntityTorch() {
		super(ModTileEntities.TORCH.get());
	}

	public int age = 0;

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.age = compound.getInt("age");
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.putInt("age", this.age);
		return compound;
	}

	@Override
	public void tick() {
		if (level != null) {
			if ((this.age++ >= 20000) || (level.isRaining() && level.canSeeSky(this.worldPosition))) {
				BlockState state = level.getBlockState(worldPosition);
				Block block = state.getBlock();

				if (block instanceof BlockTorchEnhanced) {
					BlockTorchEnhanced.extinguish(level, worldPosition);

				} else if (block instanceof BlockTorchEnhancedWall) {
					BlockTorchEnhancedWall.extinguish(level, worldPosition);
				}
			}
		}
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
