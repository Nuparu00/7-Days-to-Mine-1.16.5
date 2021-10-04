package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntityTorch;

public class BlockTorchEnhancedWall extends WallTorchBlock implements IBlockBase {

	public BlockTorchEnhancedWall() {
		super(AbstractBlock.Properties.of(Material.DECORATION).noCollission().instabreak().lightLevel((p_235470_0_) -> {
			return 14;
		}).sound(SoundType.WOOD), ParticleTypes.FLAME);
	}

	public static void extinguish(World world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (!(state.getBlock() instanceof TorchBlock))
			return;
		world.setBlockAndUpdate(pos, ModBlocks.TORCH_UNLIT_WALL.get().defaultBlockState().setValue(WallTorchBlock.FACING,
				state.getValue(WallTorchBlock.FACING)));
		world.playLocalSound((double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F),
				(double) ((float) pos.getZ() + 0.5F), SoundEvents.FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F,
				2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F, false);
		for (int i = 0; i < 5; ++i) {
			double d0 = (double) pos.getX() + world.random.nextDouble() * 0.6D + 0.2D;
			double d1 = (double) pos.getY() + world.random.nextDouble() * 0.6D + 0.2D;
			double d2 = (double) pos.getZ() + world.random.nextDouble() * 0.6D + 0.2D;
			world.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityTorch();
	}
	
	@Override
	public BlockItem createBlockItem() {
		 return null;
	}
}
