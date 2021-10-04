package nuparu.sevendaystomine.block;

import java.util.Random;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.init.ModBlocks;

public class BlockTorchUnlit extends TorchBlock implements IBlockBase {

	public BlockTorchUnlit() {
		super(AbstractBlock.Properties.of(Material.DECORATION).noCollission().instabreak().sound(SoundType.WOOD), null);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState p_180655_1_, World p_180655_2_, BlockPos p_180655_3_, Random p_180655_4_) {
	}
	
	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult rayTraceResult) {
		if(player.isCrouching()) return super.use(state, worldIn, pos, player, hand, rayTraceResult);
		if (worldIn.isClientSide())
			return ActionResultType.SUCCESS;
		
		ItemStack stack = player.getItemInHand(hand);
		Item item = stack.getItem();
		if(item == Items.FLINT_AND_STEEL) {
			lit(worldIn,pos);
			if (!player.isCreative()) {
				stack.hurt(1, worldIn.random, (ServerPlayerEntity) player);
			}
		}
		else if (item == Items.TORCH) {
			lit(worldIn,pos);
		}
		
		return ActionResultType.SUCCESS;
	}


	public static void lit(World world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (!(state.getBlock() instanceof TorchBlock))
			return;
		world.setBlockAndUpdate(pos, ModBlocks.TORCH_LIT.get().defaultBlockState());
		world.playLocalSound((double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F),
				(double) ((float) pos.getZ() + 0.5F), SoundEvents.FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 0.5F,
				2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F, false);
	}
	@Override
	public BlockItem createBlockItem() {
		 return null;
	}
}
