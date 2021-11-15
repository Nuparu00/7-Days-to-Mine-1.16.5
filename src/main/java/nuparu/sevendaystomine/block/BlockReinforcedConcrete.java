package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModBlocks;

public class BlockReinforcedConcrete extends BlockBase {

	public static final IntegerProperty PHASE = IntegerProperty.create("phase", 0, 3);

	public BlockReinforcedConcrete() {
		super(AbstractBlock.Properties.of(Material.STONE).strength(10, 10));
		registerDefaultState(this.stateDefinition.any().setValue(PHASE, 0));
	}

	@Override
	public void onRemove(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
		tryToRemove(world, blockPos, state);
	}

	public boolean tryToRemove(World world, BlockPos pos, BlockState state) {
		if (!(state.getBlock() instanceof BlockReinforcedConcrete))
			return true;

		int value = state.getValue(PHASE);

		switch (value) {
		case 0:
			world.setBlockAndUpdate(pos, ModBlocks.REINFORCED_CONCRETE.get().defaultBlockState().setValue(PHASE, 1));
			return false;
		case 1:
			world.setBlockAndUpdate(pos, ModBlocks.REINFORCED_CONCRETE.get().defaultBlockState().setValue(PHASE, 2));
			return false;
		case 2:
			world.setBlockAndUpdate(pos, ModBlocks.REINFORCED_CONCRETE.get().defaultBlockState().setValue(PHASE, 3));
			return false;
		default:
		case 3:
			return world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}

	}
	
	@Override
	public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid)
    {
		return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
    }

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(PHASE, 0);
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(PHASE);
	}

}
