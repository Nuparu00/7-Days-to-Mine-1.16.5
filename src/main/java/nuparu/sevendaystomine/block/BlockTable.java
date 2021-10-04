package nuparu.sevendaystomine.block;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import nuparu.sevendaystomine.item.EnumMaterial;

public class BlockTable extends BlockBase implements IWaterLoggable {

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
	public static final BooleanProperty EAST = BlockStateProperties.EAST;
	public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
	public static final BooleanProperty WEST = BlockStateProperties.WEST;

	public VoxelShape TOP = Block.box(0,14,0,16,16,16);
	/*
	LEGS:
	AB
	CD
	 */

	public VoxelShape LEG_A = Block.box(2,0,12,4,14,14);
	public VoxelShape LEG_B = Block.box(12,0,12,14,14,14);
	public VoxelShape LEG_C = Block.box(2,0,2,4,14,4);
	public VoxelShape LEG_D = Block.box(12,0,2,14,14,4);


	public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = Util
			.make(Maps.newEnumMap(Direction.class), (p_203421_0_) -> {
				p_203421_0_.put(Direction.NORTH, NORTH);
				p_203421_0_.put(Direction.EAST, EAST);
				p_203421_0_.put(Direction.SOUTH, SOUTH);
				p_203421_0_.put(Direction.WEST, WEST);
			});

	public BlockTable(AbstractBlock.Properties properties) {
		super(properties.noOcclusion());
		this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, Boolean.valueOf(false))
				.setValue(EAST, Boolean.valueOf(false)).setValue(SOUTH, Boolean.valueOf(false))
				.setValue(WEST, Boolean.valueOf(false)).setValue(WATERLOGGED, Boolean.valueOf(false)));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_,
							   ISelectionContext p_220053_4_) {
		boolean north = state.getValue(NORTH);
		boolean south = state.getValue(SOUTH);
		boolean west = state.getValue(WEST);
		boolean east = state.getValue(EAST);

		VoxelShape result = TOP;

		if(!south){
			if(!west){
				result = VoxelShapes.or(result,LEG_A);
			}
			if(!east){
				result = VoxelShapes.or(result,LEG_B);
			}
		}

		if(!north){
			if(!west){
				result = VoxelShapes.or(result,LEG_C);
			}
			if(!east){
				result = VoxelShapes.or(result,LEG_D);
			}
		}
		return result;
	}

	public boolean connectsTo(BlockState p_220111_1_, boolean p_220111_2_, Direction p_220111_3_) {
		Block block = p_220111_1_.getBlock();
		return block instanceof BlockTable;
	}

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IBlockReader iblockreader = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		BlockPos blockpos1 = blockpos.north();
		BlockPos blockpos2 = blockpos.east();
		BlockPos blockpos3 = blockpos.south();
		BlockPos blockpos4 = blockpos.west();
		BlockState blockstate = iblockreader.getBlockState(blockpos1);
		BlockState blockstate1 = iblockreader.getBlockState(blockpos2);
		BlockState blockstate2 = iblockreader.getBlockState(blockpos3);
		BlockState blockstate3 = iblockreader.getBlockState(blockpos4);
		return super.getStateForPlacement(context)
				.setValue(
						NORTH,
						Boolean.valueOf(this.connectsTo(blockstate,
								blockstate.isFaceSturdy(iblockreader, blockpos1, Direction.SOUTH), Direction.SOUTH)))
				.setValue(EAST,
						Boolean.valueOf(this.connectsTo(blockstate1,
								blockstate1.isFaceSturdy(iblockreader, blockpos2, Direction.WEST), Direction.WEST)))
				.setValue(SOUTH,
						Boolean.valueOf(this.connectsTo(blockstate2,
								blockstate2.isFaceSturdy(iblockreader, blockpos3, Direction.NORTH), Direction.NORTH)))
				.setValue(WEST, Boolean.valueOf(this.connectsTo(blockstate3,
						blockstate3.isFaceSturdy(iblockreader, blockpos4, Direction.EAST), Direction.EAST))).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
	}

	public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_,
			IWorld p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
		if (p_196271_1_.getValue(WATERLOGGED)) {
			p_196271_4_.getLiquidTicks().scheduleTick(p_196271_5_, Fluids.WATER, Fluids.WATER.getTickDelay(p_196271_4_));
		}

		return p_196271_2_.getAxis().getPlane() == Direction.Plane.HORIZONTAL
				? p_196271_1_.setValue(PROPERTY_BY_DIRECTION.get(p_196271_2_),
						Boolean.valueOf(this.connectsTo(p_196271_3_,
								p_196271_3_.isFaceSturdy(p_196271_4_, p_196271_6_, p_196271_2_.getOpposite()),
								p_196271_2_.getOpposite())))
				: super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
		p_206840_1_.add(NORTH, EAST, SOUTH, WEST, WATERLOGGED);
	}
	public FluidState getFluidState(BlockState p_204507_1_) {
		return p_204507_1_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_204507_1_);
	}

}
