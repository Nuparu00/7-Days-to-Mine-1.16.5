package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.item.EnumMaterial;

public class BlockFakeAnvil extends BlockFallingBase {
	public static final DirectionProperty FACING = HorizontalBlock.FACING;
	private static final VoxelShape BASE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D);
	private static final VoxelShape X_LEG1 = Block.box(3.0D, 4.0D, 4.0D, 13.0D, 5.0D, 12.0D);
	private static final VoxelShape X_LEG2 = Block.box(4.0D, 5.0D, 6.0D, 12.0D, 10.0D, 10.0D);
	private static final VoxelShape X_TOP = Block.box(0.0D, 10.0D, 3.0D, 16.0D, 16.0D, 13.0D);
	private static final VoxelShape Z_LEG1 = Block.box(4.0D, 4.0D, 3.0D, 12.0D, 5.0D, 13.0D);
	private static final VoxelShape Z_LEG2 = Block.box(6.0D, 5.0D, 4.0D, 10.0D, 10.0D, 12.0D);
	private static final VoxelShape Z_TOP = Block.box(3.0D, 10.0D, 0.0D, 13.0D, 16.0D, 16.0D);
	private static final VoxelShape X_AXIS_AABB = VoxelShapes.or(BASE, X_LEG1, X_LEG2, X_TOP);
	private static final VoxelShape Z_AXIS_AABB = VoxelShapes.or(BASE, Z_LEG1, Z_LEG2, Z_TOP);

	public BlockFakeAnvil(AbstractBlock.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
		return this.defaultBlockState().setValue(FACING, p_196258_1_.getHorizontalDirection().getClockWise());
	}

	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_,
			ISelectionContext p_220053_4_) {
		Direction direction = p_220053_1_.getValue(FACING);
		return direction.getAxis() == Direction.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
	}

	protected void falling(FallingBlockEntity p_149829_1_) {
		p_149829_1_.setHurtsEntities(true);
	}

	public void onLand(World p_176502_1_, BlockPos p_176502_2_, BlockState p_176502_3_, BlockState p_176502_4_,
			FallingBlockEntity p_176502_5_) {
		if (!p_176502_5_.isSilent()) {
			p_176502_1_.levelEvent(1031, p_176502_2_, 0);
		}

	}

	public void onBroken(World p_190974_1_, BlockPos p_190974_2_, FallingBlockEntity p_190974_3_) {
		if (!p_190974_3_.isSilent()) {
			p_190974_1_.levelEvent(1029, p_190974_2_, 0);
		}

	}

	@Nullable
	public static BlockState damage(BlockState p_196433_0_) {
		if (p_196433_0_.is(Blocks.ANVIL)) {
			return Blocks.CHIPPED_ANVIL.defaultBlockState().setValue(FACING, p_196433_0_.getValue(FACING));
		} else {
			return p_196433_0_.is(Blocks.CHIPPED_ANVIL)
					? Blocks.DAMAGED_ANVIL.defaultBlockState().setValue(FACING, p_196433_0_.getValue(FACING))
					: null;
		}
	}

	public BlockState rotate(BlockState p_185499_1_, Rotation p_185499_2_) {
		return p_185499_1_.setValue(FACING, p_185499_2_.rotate(p_185499_1_.getValue(FACING)));
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
		p_206840_1_.add(FACING);
	}

	public boolean isPathfindable(BlockState p_196266_1_, IBlockReader p_196266_2_, BlockPos p_196266_3_,
			PathType p_196266_4_) {
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	public int getDustColor(BlockState p_189876_1_, IBlockReader p_189876_2_, BlockPos p_189876_3_) {
		return p_189876_1_.getMapColor(p_189876_2_, p_189876_3_).col;
	}
}
