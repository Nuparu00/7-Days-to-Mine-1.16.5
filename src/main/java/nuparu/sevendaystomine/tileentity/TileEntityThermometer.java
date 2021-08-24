package nuparu.sevendaystomine.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import nuparu.sevendaystomine.block.BlockThermometer;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.util.ITemperature;

public class TileEntityThermometer extends TileEntity implements ITickableTileEntity {

	Direction facing;

	public TileEntityThermometer() {
		super(ModTileEntities.THERMOMETER.get());
	}

	@Override
	public void tick() {
		boolean emit = level.getBlockState(worldPosition).getValue(BlockThermometer.POWERED);
		if (facing == null) {
			this.facing = level.getBlockState(worldPosition).getValue(BlockThermometer.FACING).getOpposite();
		}
		boolean flag = false;
		BlockPos blockPos = worldPosition.relative(facing);
		TileEntity te = level.getBlockEntity(blockPos);
		if (te != null) {

			if (te instanceof ITemperature) {
				ITemperature it = (ITemperature) te;
				if (it.getTemperature() >= 0.9) {
					flag = true;
					if (!emit) {
						level.setBlockAndUpdate(worldPosition,
								level.getBlockState(worldPosition).setValue(BlockThermometer.POWERED, true));
					}
				}
			}
		}
		if (!flag && emit) {
			level.setBlockAndUpdate(worldPosition,
					level.getBlockState(worldPosition).setValue(BlockThermometer.POWERED, false));
		}
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		if (compound.contains("facing", 3)) {
			this.facing = Direction.from2DDataValue(compound.getInt("facing"));
		}

	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		if (facing != null) {
			compound.putInt("facing", this.facing.get2DDataValue());
		}

		return compound;
	}

}
