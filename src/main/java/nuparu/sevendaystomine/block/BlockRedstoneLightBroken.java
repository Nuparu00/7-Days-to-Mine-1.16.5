package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockRedstoneLightBroken extends BlockBase {

	public BlockRedstoneLightBroken() {
		super(AbstractBlock.Properties.of(Material.GLASS).sound(SoundType.GLASS).strength(0.3f));
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block p_220069_4_, BlockPos otherPos,
			boolean p_220069_6_) {
		if (!world.isClientSide) {
			if (world.hasNeighborSignal(pos)) {
				world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
				world.explode(null, pos.getX() + 0.5, pos.getY() + 0.25, pos.getZ() + 0.5, 0.1f, true,
						Explosion.Mode.DESTROY);
			}
		}

	}
}
