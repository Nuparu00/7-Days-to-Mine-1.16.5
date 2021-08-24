package nuparu.sevendaystomine.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.util.MathUtils;

public class BlockDoorLocked extends BlockDoorBase {

	private static long nextSoundAllowed = 0;

	public BlockDoorLocked(AbstractBlock.Properties properties) {
		super(properties);
	}

	public void unlock(World world, BlockPos pos, BlockState state) {
		BlockPos bottom = pos;
		if (state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER) {
			bottom = pos.below();
		}

		Direction facing = state.getValue(DoorBlock.FACING);
		boolean open = state.getValue(DoorBlock.OPEN);
		boolean powered = state.getValue(DoorBlock.POWERED);
		DoorHingeSide hinge = state.getValue(DoorBlock.HINGE);

		world.setBlockAndUpdate(bottom.above(),
				Blocks.OAK_DOOR.defaultBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER)
						.setValue(DoorBlock.FACING, facing).setValue(DoorBlock.OPEN, open)
						.setValue(DoorBlock.POWERED, powered).setValue(DoorBlock.HINGE, hinge));

		world.setBlockAndUpdate(bottom,
				Blocks.OAK_DOOR.defaultBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER)
						.setValue(DoorBlock.FACING, facing).setValue(DoorBlock.OPEN, open)
						.setValue(DoorBlock.POWERED, powered).setValue(DoorBlock.HINGE, hinge));

	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
		if (System.currentTimeMillis() >= nextSoundAllowed && !player.isCrouching()) {
			nextSoundAllowed = System.currentTimeMillis() + 500l;
			world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), ModSounds.DOOR_LOCKED.get(), SoundCategory.BLOCKS,
					0.75f + MathUtils.getFloatInRange(0, 0.25f), 0.8f + MathUtils.getFloatInRange(0, 0.2f),false);
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public void neighborChanged(BlockState p_220069_1_, World p_220069_2_, BlockPos p_220069_3_, Block p_220069_4_, BlockPos p_220069_5_, boolean p_220069_6_) {

	}

}
