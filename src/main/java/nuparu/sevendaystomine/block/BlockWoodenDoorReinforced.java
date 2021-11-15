package nuparu.sevendaystomine.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.item.IUpgrader;
import nuparu.sevendaystomine.item.ItemUpgrader;

public class BlockWoodenDoorReinforced extends BlockDoorBase implements IUpgradeable {

	public BlockWoodenDoorReinforced(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack[] getItems() {
		return new ItemStack[] { new ItemStack(Items.IRON_INGOT, 6) };
	}

	@Override
	public SoundEvent getSound() {
		return ModSounds.UPGRADE_WOOD.get();
	}

	@Override
	public BlockState getPrev(IWorld world, BlockPos pos, BlockState original) {
		return ModBlocks.WOODEN_DOOR.get().defaultBlockState().setValue(FACING, original.getValue(FACING))
				.setValue(OPEN, original.getValue(OPEN)).setValue(HINGE, original.getValue(HINGE))
				.setValue(POWERED, original.getValue(POWERED)).setValue(HALF, original.getValue(HALF));
	}

	@Override
	public BlockState getResult(IWorld world, BlockPos pos) {
		BlockState oldState = world.getBlockState(pos);

		return ModBlocks.WOODEN_DOOR_IRON_REINFORCED.get().defaultBlockState().setValue(FACING, oldState.getValue(FACING))
				.setValue(OPEN, oldState.getValue(OPEN)).setValue(HINGE, oldState.getValue(HINGE))
				.setValue(POWERED, oldState.getValue(POWERED)).setValue(HALF, oldState.getValue(HALF));
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult result) {
		if (player.getItemInHand(hand).getItem() instanceof IUpgrader && player.isCrouching()) {
			return ActionResultType.SUCCESS;
		}
		return super.use(state, world, pos, player, hand, result);
	}

	@Override
	public void onUpgrade(IWorld world, BlockPos pos, BlockState oldState) {
		DoubleBlockHalf half = oldState.getValue(HALF);
		if (half == DoubleBlockHalf.LOWER) {
			BlockPos blockPos = pos.above();
			BlockState state = world.getBlockState(blockPos);
			if (state.getBlock() == Blocks.AIR || state.getBlock() instanceof BlockDoorBase) {
				world.setBlock(blockPos, getResult(world, blockPos),2);
			}
		} else if (half == DoubleBlockHalf.UPPER) {
			BlockPos blockPos = pos.below();
            BlockState state = world.getBlockState(blockPos);
			if (state.getBlock() == Blocks.AIR || state.getBlock() instanceof BlockDoorBase) {
				world.setBlock(blockPos, getResult(world, blockPos),2);
			}
		}
	}

	@Override
	public void onDowngrade(IWorld world, BlockPos pos, BlockState oldState) {
		DoubleBlockHalf half = oldState.getValue(HALF);
		if (half == DoubleBlockHalf.LOWER) {
			pos = pos.above();
			half = DoubleBlockHalf.UPPER;
		} else if (half == DoubleBlockHalf.UPPER) {
			pos = pos.below();
			half = DoubleBlockHalf.LOWER;
		}
		world.setBlock(pos, getPrev(world,pos,oldState).setValue(HALF, half),3);
	}

}
