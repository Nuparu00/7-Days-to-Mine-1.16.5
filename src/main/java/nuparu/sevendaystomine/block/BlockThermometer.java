package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModItemGroups;
import nuparu.sevendaystomine.tileentity.TileEntityThermometer;

public class BlockThermometer extends BlockTileProvider<TileEntityThermometer> {

	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	private static final VoxelShape NORTH = Block.box(0.25F*16, 0.25F*16, 0.75F*16, 0.75F*16, 0.875F*16, 16);
	private static final VoxelShape SOUTH = Block.box(0.25F*16, 0.25F*16, 0F, 0.75F*16, 0.75F*16, 0.5F*16);
	private static final VoxelShape WEST = Block.box(0.5F*16, 0.25F*16, 0.25F*16, 16, 0.75F*16, 0.75F*16);
	private static final VoxelShape EAST = Block.box(0F, 0.25F*16, 0.25F*16, 0.5F*16, 0.75F*16, 0.75F*16);
	private static final VoxelShape UP = Block.box(0.25F*16, 0.0F, 0.125F*16, 0.75F*16, 0.25F*16, 0.75F*16);
	private static final VoxelShape DOWN = Block.box(0.25F*16, 0.5F*16, 0.25F*16, 0.75F*16, 16, 0.75F*16);

	public BlockThermometer(AbstractBlock.Properties properties) {
		super(properties);
		this.registerDefaultState(
				this.defaultBlockState().setValue(FACING, Direction.DOWN).setValue(POWERED, Boolean.FALSE));
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
		case UP:
			return UP;
		case DOWN:
			return DOWN;
		}
	}

	@Override
	public void neighborChanged(BlockState blockstate, World world, BlockPos pos, Block blockIn, BlockPos fromPos,
			boolean flag) {
		BlockState state = world.getBlockState(pos.relative(world.getBlockState(pos).getValue(FACING).getOpposite()));
		if (state.getBlock() == Blocks.AIR) {
			dropResources(world.getBlockState(pos), world, pos);
			world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityThermometer();
	}

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(FACING, context.getClickedFace());
	}

	@Override
	public int getSignal(BlockState p_180656_1_, IBlockReader p_180656_2_, BlockPos p_180656_3_,
			Direction p_180656_4_) {
		return p_180656_1_.getValue(POWERED) ? 15 : 0;
	}

	@Override
	public int getDirectSignal(BlockState p_176211_1_, IBlockReader p_176211_2_, BlockPos p_176211_3_,
			Direction p_176211_4_) {
		return p_176211_1_.getValue(POWERED) ? 15 : 0;
	}

	@Override
	public boolean isSignalSource(BlockState p_149744_1_) {
		return true;
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED);
	}

	@Override
	public ItemGroup getItemGroup() {
		return ModItemGroups.TAB_ELECTRICITY;
	}
}
