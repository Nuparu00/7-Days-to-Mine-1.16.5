package nuparu.sevendaystomine.item;

import java.util.HashSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;

import nuparu.sevendaystomine.block.ISalvageable;
import nuparu.sevendaystomine.util.MathUtils;

public class ItemWrench extends ItemUpgrader {
	public ItemWrench(IItemTier tier) {
		super(1, 0, tier, new HashSet<Block>(), new Item.Properties().stacksTo(1));
		setEffectiveness(0.33334f);
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		PlayerEntity playerIn = context.getPlayer();
		Hand hand = context.getHand();
		World worldIn = context.getLevel();
		
		ItemStack itemstack = playerIn.getItemInHand(hand);

		BlockPos pos = new BlockPos(itemstack.getOrCreateTag().getInt("X"),
				itemstack.getOrCreateTag().getInt("Y"), itemstack.getOrCreateTag().getInt("Z"));
		BlockState state = worldIn.getBlockState(pos);
		Block block = state.getBlock();
		if (block instanceof ISalvageable) {
			ISalvageable salvageable = (ISalvageable) block;
			worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), salvageable.getSound(),
					SoundCategory.BLOCKS, MathUtils.getFloatInRange(0.5f, 0.75f), MathUtils.getFloatInRange(0.9f, 1f));
			itemstack.getOrCreateTag().putFloat("Percent",
					itemstack.getOrCreateTag().getFloat("Percent") - effect*salvageable.getUpgradeRate(worldIn, pos, state, playerIn) / 30f);
			playerIn.swing(hand);
			if (itemstack.getOrCreateTag().getFloat("Percent") <= -1F) {
				if (playerIn instanceof ServerPlayerEntity) {
					itemstack.hurt(1, random, (ServerPlayerEntity) playerIn);
				}
				salvageable.onSalvage(worldIn, pos, state);
				if (!worldIn.isClientSide()) {
					//ModTriggers.BLOCK_UPGRADE.trigger((ServerPlayerEntity) playerIn, state);
				}
				itemstack.getOrCreateTag().putFloat("Percent", 0F);
				if (!worldIn.isClientSide()) {
					List<ItemStack> stacks = salvageable.getItems(worldIn, pos, state, playerIn);
					for (ItemStack stack : stacks) {
						if (!playerIn.addItem(stack)) {
							playerIn.drop(stack, false);
						}
					}
				}
			}
			return ActionResultType.SUCCESS;
		}
		return super.useOn(context);
	}
}
