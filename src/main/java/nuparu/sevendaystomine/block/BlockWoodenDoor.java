package nuparu.sevendaystomine.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.ItemUpgrader;

public class BlockWoodenDoor extends BlockDoorBase implements IUpgradeable {

	public BlockWoodenDoor(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack[] getItems() {
		return new ItemStack[] { new ItemStack(ModItems.PLANK_WOOD.get(), 6) };
	}

	@Override
	public SoundEvent getSound() {
		return ModSounds.UPGRADE_WOOD.get();
	}

	@Override
	public BlockState getPrev(World world, BlockPos pos, BlockState original) {
		return Blocks.AIR.defaultBlockState();
	}

	@Override
	public BlockState getResult(World world, BlockPos pos) {
		BlockState oldState = world.getBlockState(pos);

		return ModBlocks.WOODEN_DOOR_REINFORCED.get().defaultBlockState().setValue(FACING, oldState.getValue(FACING))
				.setValue(OPEN, oldState.getValue(OPEN)).setValue(HINGE, oldState.getValue(HINGE))
				.setValue(POWERED, oldState.getValue(POWERED)).setValue(HALF, oldState.getValue(HALF));
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult result) {
		if (player.getItemInHand(hand).getItem() instanceof ItemUpgrader && player.isCrouching()) {
			return ActionResultType.SUCCESS;
		}
		return super.use(state, world, pos, player, hand, result);
	}

	@Override
	public void onUpgrade(World world, BlockPos pos, BlockState oldState) {
		DoubleBlockHalf half = oldState.getValue(HALF);
		if (half == DoubleBlockHalf.LOWER) {
			BlockPos blockPos = pos.above();
			BlockState state = world.getBlockState(blockPos);
			if (state.getBlock() == Blocks.AIR || state.getBlock() instanceof BlockDoorBase) {
				world.setBlockAndUpdate(blockPos, getResult(world, blockPos));
			}
		} else if (half == DoubleBlockHalf.UPPER) {
			BlockPos blockPos = pos.below();
			;
			BlockState state = world.getBlockState(blockPos);
			if (state.getBlock() == Blocks.AIR || state.getBlock() instanceof BlockDoorBase) {
				world.setBlockAndUpdate(blockPos, getResult(world, blockPos));
			}
		}
	}

	@Override
	public void onDowngrade(World world, BlockPos pos, BlockState oldState) {

	}

}
