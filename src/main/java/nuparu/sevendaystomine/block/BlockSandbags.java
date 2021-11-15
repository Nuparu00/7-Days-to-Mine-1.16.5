package nuparu.sevendaystomine.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class BlockSandbags extends FallingBlock implements IBlockBase, IWaterLoggable {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	public static final VoxelShape SUPPORT_SHAPE = Block.box(1,0,1,15,15,15);

	public BlockSandbags() {
		super(AbstractBlock.Properties.of(Material.SAND).sound(SoundType.SAND).strength(1,1.2f).noOcclusion());
	      this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.SOUTH).setValue(WATERLOGGED, Boolean.FALSE));
	}

	public BlockState rotate(BlockState p_185499_1_, Rotation p_185499_2_) {
		return p_185499_1_.setValue(FACING, p_185499_2_.rotate(p_185499_1_.getValue(FACING)));
	}

	public BlockState mirror(BlockState p_185471_1_, Mirror p_185471_2_) {
		return p_185471_1_.rotate(p_185471_2_.getRotation(p_185471_1_.getValue(FACING)));
	}


	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
	}

	@Override
	public void tick(BlockState p_225534_1_, ServerWorld p_225534_2_, BlockPos p_225534_3_, Random p_225534_4_) {
		if (p_225534_2_.isEmptyBlock(p_225534_3_.below()) || isFree(p_225534_2_.getBlockState(p_225534_3_.below())) && p_225534_3_.getY() >= 0) {
			FallingBlockEntity fallingblockentity = new FallingBlockEntity(p_225534_2_, (double)p_225534_3_.getX() + 0.5D, p_225534_3_.getY(), (double)p_225534_3_.getZ() + 0.5D, p_225534_2_.getBlockState(p_225534_3_).setValue(WATERLOGGED,false));
			this.falling(fallingblockentity);
			p_225534_2_.addFreshEntity(fallingblockentity);
		}
	}


	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}

	@Override
	public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, IWorld p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
		if (p_196271_1_.getValue(WATERLOGGED)) {
			p_196271_4_.getLiquidTicks().scheduleTick(p_196271_5_, Fluids.WATER, Fluids.WATER.getTickDelay(p_196271_4_));
		}

		return super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
	}


	@Override
	public FluidState getFluidState(BlockState p_204507_1_) {
		return p_204507_1_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_204507_1_);
	}

	@Override
	public BlockItem createBlockItem() {
		 final Item.Properties properties = new Item.Properties().tab(getItemGroup());
		 return new BlockItem(this, properties);
	}

	@Deprecated
	@Override
	public VoxelShape getBlockSupportShape(BlockState state, IBlockReader p_230335_2_, BlockPos p_230335_3_) {
		return SUPPORT_SHAPE;
	}

}
