package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.tileentity.TileEntityWoodenLogSpike;

public class BlockWoodenLogSpike extends BlockBase implements IWaterLoggable {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public BlockWoodenLogSpike(AbstractBlock.Properties properties) {
		super(properties.noCollission().noOcclusion());
		this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(false)));
	}

	@Override
	public void entityInside(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		if (!(entityIn instanceof LivingEntity)) {
			return;
		}
		entityIn.makeStuckInBlock(state, new Vector3d(0.25D, (double) 0.05F, 0.25D));
		if (entityIn instanceof PlayerEntity) {
			if (((PlayerEntity) entityIn).isCreative() || ((PlayerEntity) entityIn).isSpectator()) {
				return;
			}
		}
		entityIn.hurt(DamageSource.GENERIC, 5);
		if (this == ModBlocks.WOODEN_SPIKES.get()) {
			degradeBlock(pos, worldIn);
		}
		TileEntity te = worldIn.getBlockEntity(pos);
		if (te != null && te instanceof TileEntityWoodenLogSpike) {
			TileEntityWoodenLogSpike tileEntity = (TileEntityWoodenLogSpike) te;
			tileEntity.dealDamage(1);
		}
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
			boolean flag) {
		if (!this.canSurvive(state, worldIn, pos)) {
			dropResources(worldIn.getBlockState(pos), worldIn, pos);
			worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}
	}

	@Override
	public boolean canSurvive(BlockState p_196260_1_, IWorldReader p_196260_2_, BlockPos p_196260_3_) {
		BlockPos blockpos = p_196260_3_.below();
		return canSupportRigidBlock(p_196260_2_, blockpos) || canSupportCenter(p_196260_2_, blockpos, Direction.UP);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityWoodenLogSpike();
	}

	public static void degradeBlock(BlockPos pos, World world) {
		/*Block block = world.getBlockState(pos).getBlock();
		if (block == ModBlocks.WOODEN_SPIKES.get()) {
			world.setBlockAndUpdate(pos, ModBlocks.WOODEN_SPIKES_BLOODED.get().defaultBlockState());
		} else if (block == ModBlocks.WOODEN_SPIKES_BLOODED.get()) {
			world.setBlockAndUpdate(pos, ModBlocks.WOODEN_SPIKES_BROKEN.get().defaultBlockState());
		} else if (block == ModBlocks.WOODEN_SPIKES_BROKEN.get()) {
			world.destroyBlock(pos, false);
		}*/
	}

	@Override
	public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if(state.getBlock() != newState.getBlock()){
			super.onRemove(state, world, pos, newState, isMoving);
		}
	}

		@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
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

/*
	public BlockRenderType getRenderShape(BlockState p_149645_1_) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}*/
}