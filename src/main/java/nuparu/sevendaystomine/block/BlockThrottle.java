package nuparu.sevendaystomine.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class BlockThrottle extends BlockArmchair {

	private static final VoxelShape SOUTH = Block.box(0F, 0.0F, 0F, 1F*16, 0.75F*16, 0.9375F*16);
	private static final VoxelShape NORTH = Block.box(0F, 0.0F, 0.0625F*16, 1F*16, 0.75F*16, 1F*16);
	private static final VoxelShape WEST = Block.box(0.0625F*16, 0.0F, 0F, 1F*16, 0.75F*16, 1F*16);
	private static final VoxelShape EAST = Block.box(0F, 0.0F, 0F, 0.9375F*16, 0.75F*16, 1F*16);
	
	public BlockThrottle(Properties properties) {
		super(properties);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_,
			ISelectionContext p_220053_4_) {
		switch ((Direction) state.getValue(FACING)) {
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
}
