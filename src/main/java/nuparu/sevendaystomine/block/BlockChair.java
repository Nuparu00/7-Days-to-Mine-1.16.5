package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import nuparu.sevendaystomine.util.Utils;

public class BlockChair extends BlockHorizontalBase implements IWaterLoggable {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	private static final VoxelShape SOUTH = Block.box(0.1875F * 16, 0.0F, 0.3125F * 16, 0.8125F * 16, 0.6875F * 16, 0.9375F * 16);
	private static final VoxelShape NORTH = Block.box(0.1875F * 16, 0.0F, 0.0625F * 16, 0.8125F * 16, 0.6875F * 16, 0.6875F * 16);
	private static final VoxelShape WEST = Block.box(0.0625F * 16, 0.0F, 0.1875F * 16, 0.6875F * 16, 0.6875F * 16, 0.8125F * 16);
	private static final VoxelShape EAST = Block.box(0.3125F * 16, 0.0F, 0.1875F * 16, 0.9375F * 16, 0.6875F * 16, 0.8125F * 16);

	public BlockChair(AbstractBlock.Properties properties) {
		super(properties.noOcclusion());
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.SOUTH).setValue(WATERLOGGED, Boolean.FALSE));
	}


	@Override
	public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_,
			ISelectionContext p_220053_4_) {
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
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos,
			PlayerEntity playerIn, Hand hand, BlockRayTraceResult result) {
		if (!playerIn.isCrouching()) {
			Utils.mountBlock(worldIn, pos, playerIn,0.45f);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.FAIL;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState p_204507_1_) {
		return p_204507_1_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_204507_1_);
	}
}
