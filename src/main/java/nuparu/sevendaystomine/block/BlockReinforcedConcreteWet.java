package nuparu.sevendaystomine.block;

import java.util.Random;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.init.ModBlocks;

public class BlockReinforcedConcreteWet extends BlockBase {

	public BlockReinforcedConcreteWet() {
		super(AbstractBlock.Properties.of(Material.STONE));
	}


	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		if (world.isDay()) {
			world.setBlock(pos, ModBlocks.REINFORCED_CONCRETE.get().defaultBlockState(), 3);
		}

	}
}
