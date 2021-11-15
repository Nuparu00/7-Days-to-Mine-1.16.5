package nuparu.sevendaystomine.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.IUpgrader;
import nuparu.sevendaystomine.item.ItemUpgrader;

public class BlockWoodenDoorIronReinforced extends BlockDoorBase implements IUpgradeable {

	public BlockWoodenDoorIronReinforced(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack[] getItems() {
		return new ItemStack[] { new ItemStack(ModItems.IRON_SCRAP.get(), 6) };
	}

	@Override
	public SoundEvent getSound() {
		return ModSounds.UPGRADE_WOOD.get();
	}

	@Override
	public BlockState getPrev(IWorld world, BlockPos pos, BlockState original) {
		return ModBlocks.WOODEN_DOOR_REINFORCED.get().defaultBlockState().setValue(FACING, original.getValue(FACING))
				.setValue(OPEN, original.getValue(OPEN)).setValue(HINGE, original.getValue(HINGE))
				.setValue(POWERED, original.getValue(POWERED)).setValue(HALF, original.getValue(HALF));
	}

	@Override
	public BlockState getResult(IWorld world, BlockPos pos) {
		return null;
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
		world.setBlock(pos, getPrev(world,pos,oldState).setValue(HALF, half),2);
	}

}