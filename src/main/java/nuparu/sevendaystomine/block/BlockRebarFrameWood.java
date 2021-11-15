package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModItems;

public class BlockRebarFrameWood extends BlockBase {

	public BlockRebarFrameWood() {
		super(AbstractBlock.Properties.of(Material.METAL).sound(SoundType.METAL).strength(2.33f, 10)
				.harvestTool(ToolType.PICKAXE).harvestLevel(1).noOcclusion());
	}


	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn,
			Hand hand, BlockRayTraceResult rayTraceResult) {
		ItemStack stack = playerIn.getItemInHand(hand);
		if (stack.getItem() == ModItems.CONCRETE_MIX.get()) {
			worldIn.setBlockAndUpdate(pos, ModBlocks.REINFORCED_CONCRETE_WET.get().defaultBlockState());
			if (!worldIn.isClientSide()) {
				//ModTriggers.BLOCK_UPGRADE.trigger((ServerPlayerEntity) playerIn, ModBlocks.REINFORCED_CONCRETE_WET.get().defaultBlockState());
			}
			playerIn.playSound(SoundEvents.BUCKET_EMPTY, 1.0F, 1.0F);
			if (!playerIn.isCreative()) {
				stack.shrink(1);
				if (stack.getCount() <= 0) {
					playerIn.setItemInHand(hand, ItemStack.EMPTY);
				}
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.FAIL;
	}
}
