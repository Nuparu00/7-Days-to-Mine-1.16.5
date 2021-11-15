package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.tileentity.TileEntityRadio;

public class BlockRadio extends BlockHorizontalBase {

	private static final VoxelShape NORTH = Block.box(2, 0.0F, 5, 14, 7.5F, 11);
	private static final VoxelShape SOUTH = Block.box(2, 0.0F, 5F, 14, 7.5F, 11);
	private static final VoxelShape WEST = Block.box(5F, 0.0F, 2, 11, 7.5F, 14);
	private static final VoxelShape EAST = Block.box(5, 0.0F, 2, 11, 7.5F, 14);

	public BlockRadio(AbstractBlock.Properties properties) {
		super(properties);
	}

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

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityRadio();
	}
	
	@Override
	public void onRemove(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
		if (world.isClientSide()) {
			SevenDaysToMine.proxy.stopLoudSound(blockPos);
		}
		super.onRemove(state, world, blockPos, newState, isMoving);
	}


	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult rayTraceResult) {
		TileEntityRadio te = (TileEntityRadio) worldIn.getBlockEntity(pos);
		te.cycleRadio();
		return ActionResultType.SUCCESS;

	}

}
