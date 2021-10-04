package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.tileentity.TileEntityCalendar;

public class BlockCalendar extends BlockHorizontalBase {

	private static final VoxelShape NORTH = Block.box(5, 7, 15, 11, 16, 16);
	private static final VoxelShape SOUTH = Block.box(5, 7, 0.0F, 11, 16, 1);
	private static final VoxelShape WEST = Block.box(15, 7, 5, 16, 16, 11);
	private static final VoxelShape EAST = Block.box(0.0F, 7, 5, 1, 16, 11);

	public BlockCalendar(AbstractBlock.Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_,
			ISelectionContext p_220053_5_) {
		switch (state.getValue(FACING)) {
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

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityCalendar();
	}

	@Override
	public boolean canSurvive(BlockState p_196260_1_, IWorldReader p_196260_2_, BlockPos p_196260_3_) {
		Direction direction = p_196260_1_.getValue(FACING);
		BlockPos blockpos = p_196260_3_.relative(direction.getOpposite());
		BlockState blockstate = p_196260_2_.getBlockState(blockpos);
		return this.canSurviveOn(p_196260_2_, blockpos, blockstate, direction);
	}

	private boolean canSurviveOn(IBlockReader p_235552_1_, BlockPos p_235552_2_, BlockState state, Direction direction) {
		return state.isFaceSturdy(p_235552_1_, p_235552_2_, direction.getOpposite());
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block,
								BlockPos pos2, boolean p_220069_6_) {
		if (!world.isClientSide) {
			if (!state.canSurvive(world, pos)) {
				world.destroyBlock(pos, true);
			}

		}
	}


}
