package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import nuparu.sevendaystomine.init.ModItemGroups;
import nuparu.sevendaystomine.tileentity.TileEntityEnergyPole;

public class BlockEnergyPole extends BlockBase {

	public static final DirectionProperty FACING = BlockStateProperties.FACING;

	private static final VoxelShape NORTH = Block.box(0.25F*16, 0.25F*16, 0.5F*16, 0.75F*16, 0.75F*16, 1F*16);
	private static final VoxelShape SOUTH = Block.box(0.25F*16, 0.25F*16, 0F, 0.75F*16, 0.75F*16, 0.5F*16);
	private static final VoxelShape WEST = Block.box(0.5F*16, 0.25F*16, 0.25F*16, 1F*16, 0.75F*16, 0.75F*16);
	private static final VoxelShape EAST = Block.box(0F, 0.25F*16, 0.25F*16, 0.5F*16, 0.75F*16, 0.75F*16);
	private static final VoxelShape UP = Block.box(0.375F*16, 0.0F, 0.375F*16, 0.625F*16, 1F*16, 0.625F*16);
	private static final VoxelShape DOWN = Block.box(0.25F*16, 0.5F*16, 0.25F*16, 0.75F*16, 1F*16, 0.75F*16);

	public BlockEnergyPole(AbstractBlock.Properties properties) {
		super(properties);
	    this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.DOWN));
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

	public BlockState rotate(BlockState p_185499_1_, Rotation p_185499_2_) {
		return p_185499_1_.setValue(FACING, p_185499_2_.rotate(p_185499_1_.getValue(FACING)));
	}

	public BlockState mirror(BlockState p_185471_1_, Mirror p_185471_2_) {
		return p_185471_1_.rotate(p_185471_2_.getRotation(p_185471_1_.getValue(FACING)));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityEnergyPole();
	}
	
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(FACING, context.getClickedFace());
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public ItemGroup getItemGroup() {
		return ModItemGroups.TAB_ELECTRICITY;
	}
}