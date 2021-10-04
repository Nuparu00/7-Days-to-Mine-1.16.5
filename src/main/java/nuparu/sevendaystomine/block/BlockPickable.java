package nuparu.sevendaystomine.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import nuparu.sevendaystomine.util.MathUtils;

public class BlockPickable extends BlockBase {
	public BlockPickable(Properties properties) {
		super(properties);
	}

	@Override
	public boolean canSurvive(BlockState p_196260_1_, IWorldReader p_196260_2_, BlockPos p_196260_3_) {
		BlockPos blockpos = p_196260_3_.below();
		BlockState blockstate = p_196260_2_.getBlockState(blockpos);
		return this.canSurviveOn(p_196260_2_, blockpos, blockstate);
	}

	public boolean canSurviveOn(IBlockReader world, BlockPos pos, BlockState state) {
		return state.isFaceSturdy(world, pos, Direction.UP) || state.is(Blocks.HOPPER);
	}

	@Override
	public void neighborChanged(BlockState p_220069_1_, World p_220069_2_, BlockPos p_220069_3_, Block p_220069_4_,
			BlockPos p_220069_5_, boolean p_220069_6_) {
		if (!p_220069_2_.isClientSide) {
			if (!p_220069_1_.canSurvive(p_220069_2_, p_220069_3_)) {
				p_220069_2_.destroyBlock(p_220069_3_, true);
			}

		}
	}
	
	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult rayTraceResult) {
		if (worldIn.isClientSide())
			return ActionResultType.SUCCESS;

		Block.dropResources(state, worldIn, pos);
		worldIn.setBlockAndUpdate(pos,Blocks.AIR.defaultBlockState());
		worldIn.playSound((PlayerEntity) null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
				SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundCategory.BLOCKS,
				MathUtils.getFloatInRange(0.9f, 1.1f), MathUtils.getFloatInRange(0.9f, 1.1f));

		return ActionResultType.SUCCESS;
	}

}
