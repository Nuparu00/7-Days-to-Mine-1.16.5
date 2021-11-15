package nuparu.sevendaystomine.block;

import net.minecraft.block.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntityBigSignMaster;
import nuparu.sevendaystomine.tileentity.TileEntityBigSignSlave;

public class BlockBigSignMaster extends WallSignBlock
{

	public BlockBigSignMaster(Properties propertiesIn, WoodType woodTypeIn)
	{
		super(propertiesIn, woodTypeIn);
	}

	@Override
	public boolean hasTileEntity(BlockState stateIn)
	{
		return true;
	}

	protected static final VoxelShape EAST = Block.box(0.0D, 0D, 0, 0.125D*16, 16, 1D*16);
	protected static final VoxelShape WEST = Block.box(0.875D*16, 0.0, 0D, 1.0D*16, 1D*16, 1D*16);
	protected static final VoxelShape SOUTH = Block.box(0, 0.0, 0.0D, 1D*16, 1D*16, 0.125D*16);
	protected static final VoxelShape NORTH = Block.box(0, 0.0, 0.875D*16, 1D*16, 1D*16, 1.0D*16);

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_,
							   ISelectionContext p_220053_4_) {
		switch (state.getValue(HorizontalBlock.FACING)) {
			default:
			case NORTH:
				return NORTH;
			case SOUTH:
				return SOUTH;
			case WEST:
				return WEST;
			case EAST:
				return EAST;
		}
	}

	/*
	If we don't override this, it the Blockitem causes recursion for some unknown rason, even though the other sign block does not experience this issue
	 */
	@Override
	public String getDescriptionId() {
		return "block.sevendaystomine.big_sign";
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world){
		return new TileEntityBigSignMaster();
	}
	@Override
	public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState p_220082_4_, boolean p_220082_5_) {
		TileEntity TE = worldIn.getBlockEntity(pos);
		if (!(TE instanceof TileEntityBigSignMaster)) {
			return;
		}
		TileEntityBigSignMaster masterTE = (TileEntityBigSignMaster) TE;

		Direction facing = state.getValue(FACING);

		for (int width = -3; width <= 3; width++) {
			for (int height = 0; height < 3; height++) {
				if (width == 0 && height == 0) {
					continue;
				}
				BlockPos pos2 = pos.relative(facing.getClockWise(), width).above(height);
				BlockState state2 = ModBlocks.BIG_SIGN_SLAVE.get().defaultBlockState().setValue(BlockBigSignSlave.FACING,
						facing);
				worldIn.setBlockAndUpdate(pos2, state2);
				worldIn.sendBlockUpdated(pos2, Blocks.AIR.defaultBlockState(), state2, 3);
				masterTE.addSlave(pos2);
				TileEntity TE2 = worldIn.getBlockEntity(pos2);
				if (TE2 instanceof TileEntityBigSignSlave) {
					TileEntityBigSignSlave slave = (TileEntityBigSignSlave) TE2;
					slave.setParent(pos);
				}
			}
		}

	}

	@Override
	public void onRemove(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
		TileEntity tileentity = world.getBlockEntity(blockPos);

		if (tileentity instanceof TileEntityBigSignMaster) {
			TileEntityBigSignMaster master = (TileEntityBigSignMaster) tileentity;
			for (BlockPos pos2 : master.getSlaves()) {
				world.destroyBlock(pos2, false);
			}
		}
		super.onRemove(state, world, blockPos, newState, isMoving);
	}

	@Deprecated
	@Override
	public VoxelShape getBlockSupportShape(BlockState state, IBlockReader p_230335_2_, BlockPos p_230335_3_) {
		return  VoxelShapes.block();
	}
}
