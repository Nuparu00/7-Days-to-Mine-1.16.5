package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import nuparu.sevendaystomine.item.EnumMaterial;

public class BlockCatwalkBase extends BlockBase implements  IWaterLoggable {
	public static final EnumProperty<CatwalkType> TYPE = EnumProperty.create("type", CatwalkType.class);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape BOTTOM_AABB = Block.box(0, 0, 0, 16, 0.0625*16, 16);
	protected static final VoxelShape TOP_AABB = Block.box(0, 0.9375*16, 0, 16, 16, 16);


	public BlockCatwalkBase(AbstractBlock.Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(TYPE, CatwalkType.BOTTOM).setValue(WATERLOGGED,
				Boolean.valueOf(false)));
	}

	public boolean useShapeForLightOcclusion(BlockState p_220074_1_) {
		return true;
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
		p_206840_1_.add(TYPE, WATERLOGGED);
	}

	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_,
			ISelectionContext p_220053_4_) {
		CatwalkType slabtype = p_220053_1_.getValue(TYPE);
		switch (slabtype) {
		case TOP:
			return TOP_AABB;
		default:
			return BOTTOM_AABB;
		}
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
		BlockPos blockpos = p_196258_1_.getClickedPos();
		BlockState blockstate = p_196258_1_.getLevel().getBlockState(blockpos);
		FluidState fluidstate = p_196258_1_.getLevel().getFluidState(blockpos);
		BlockState blockstate1 = this.defaultBlockState().setValue(TYPE, CatwalkType.BOTTOM).setValue(WATERLOGGED,
				Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
		Direction direction = p_196258_1_.getClickedFace();
		return direction != Direction.DOWN
				&& (direction == Direction.UP || !(p_196258_1_.getClickLocation().y - (double) blockpos.getY() > 0.5D))
						? blockstate1
						: blockstate1.setValue(TYPE, CatwalkType.TOP);

	}

	public boolean canBeReplaced(BlockState p_196253_1_, BlockItemUseContext p_196253_2_) {
		ItemStack itemstack = p_196253_2_.getItemInHand();
		CatwalkType slabtype = p_196253_1_.getValue(TYPE);
		if (itemstack.getItem() == this.asItem()) {
			if (p_196253_2_.replacingClickedOnBlock()) {
				boolean flag = p_196253_2_.getClickLocation().y - (double) p_196253_2_.getClickedPos().getY() > 0.5D;
				Direction direction = p_196253_2_.getClickedFace();
				if (slabtype == CatwalkType.BOTTOM) {
					return direction == Direction.UP || flag && direction.getAxis().isHorizontal();
				} else {
					return direction == Direction.DOWN || !flag && direction.getAxis().isHorizontal();
				}
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	public FluidState getFluidState(BlockState p_204507_1_) {
		return p_204507_1_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_204507_1_);
	}

	public boolean placeLiquid(IWorld p_204509_1_, BlockPos p_204509_2_, BlockState p_204509_3_,
			FluidState p_204509_4_) {
		return IWaterLoggable.super.placeLiquid(p_204509_1_, p_204509_2_, p_204509_3_, p_204509_4_);
	}

	public boolean canPlaceLiquid(IBlockReader p_204510_1_, BlockPos p_204510_2_, BlockState p_204510_3_,
			Fluid p_204510_4_) {
		return IWaterLoggable.super.canPlaceLiquid(p_204510_1_, p_204510_2_, p_204510_3_, p_204510_4_);
	}

	public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_,
			IWorld p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
		if (p_196271_1_.getValue(WATERLOGGED)) {
			p_196271_4_.getLiquidTicks().scheduleTick(p_196271_5_, Fluids.WATER,
					Fluids.WATER.getTickDelay(p_196271_4_));
		}

		return super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
	}

	public boolean isPathfindable(BlockState p_196266_1_, IBlockReader p_196266_2_, BlockPos p_196266_3_,
			PathType p_196266_4_) {
		switch (p_196266_4_) {
		case LAND:
			return false;
		case WATER:
			return p_196266_2_.getFluidState(p_196266_3_).is(FluidTags.WATER);
		case AIR:
			return false;
		default:
			return false;
		}
	}

	public enum CatwalkType implements IStringSerializable {
		TOP("top"), BOTTOM("bottom");

		private final String name;

		private CatwalkType(String p_i49332_3_) {
			this.name = p_i49332_3_;
		}

		public String toString() {
			return this.name;
		}

		public String getSerializedName() {
			return this.name;
		}
	}

}