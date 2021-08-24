package nuparu.sevendaystomine.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.item.IScrapable;

public class BlockSinkFaucet extends BlockHorizontalBase implements IScrapable , IWaterLoggable {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public VoxelShape NORTH = Block.box(4, 6, 11.2, 12, 11, 16);
	public VoxelShape SOUTH = Block.box(4, 6, 0.0F, 12, 11, 4.8);
	public VoxelShape WEST = Block.box(11.2, 6, 4, 16, 11, 12);
	public VoxelShape EAST = Block.box(0.0F, 6, 4, 4.8, 11, 12);

	private EnumMaterial material = EnumMaterial.IRON;
	private int weight = 3;

	public BlockSinkFaucet() {
		super(AbstractBlock.Properties.of(Material.METAL).sound(SoundType.METAL).strength(16, 2).noOcclusion());
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.SOUTH).setValue(WATERLOGGED, Boolean.valueOf(false)));
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

	@Override
	public void setMaterial(EnumMaterial mat) {
		material = mat;
	}

	@Override
	public EnumMaterial getItemMaterial() {
		return material;
	}

	@Override
	public void setWeight(int newWeight) {
		weight = newWeight;
	}

	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public boolean canBeScraped() {
		return true;
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos posOther,
			boolean p_220069_6_) {
		Direction Direction = (Direction) state.getValue(BlockHorizontalBase.FACING);

		if (!world.getBlockState(pos.relative(Direction.getOpposite())).getMaterial().isSolid()) {
			this.dropResources(state, world, pos);
			world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}

		super.neighborChanged(state, world, pos, blockIn, posOther, p_220069_6_);
	}

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		return this.defaultBlockState().setValue(FACING, context.getClickedFace()).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
	}

	@Override
	public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, IWorld p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
		if (p_196271_1_.getValue(WATERLOGGED)) {
			p_196271_4_.getLiquidTicks().scheduleTick(p_196271_5_, Fluids.WATER, Fluids.WATER.getTickDelay(p_196271_4_));
		}

		return super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
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
