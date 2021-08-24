package nuparu.sevendaystomine.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import nuparu.sevendaystomine.tileentity.TileEntityCodeSafe;

public class ItemStethoscope extends Item {
	
	public ItemStethoscope() {
		super(new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_TOOLS));
		
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		PlayerEntity player = context.getPlayer();
		Hand hand = context.getHand();
		World world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		ItemStack itemstack = player.getItemInHand(hand);
		
		if (!world.isClientSide() && player.isCrouching() && hand == Hand.MAIN_HAND) {
			TileEntity tileEntity = world.getBlockEntity(pos);
			if (tileEntity == null || !(tileEntity instanceof TileEntityCodeSafe)) {
				return super.useOn(context);
			}
			TileEntityCodeSafe te = (TileEntityCodeSafe) tileEntity;

			int numPos = world.random.nextInt(3);
			int guess = world.random.nextInt(10);

			int test = te.testDigit(player, guess, numPos);

			player.sendMessage(new TranslationTextComponent(
					test == -1 ? "safe.code.less" : (test == 1 ? "safe.code.greater" : "safe.code.equal"), numPos + 1,
					guess),Util.NIL_UUID);
			if (player instanceof ServerPlayerEntity) {
				itemstack.hurt(1, random, (ServerPlayerEntity) player);
			}
			return ActionResultType.SUCCESS;

		}
		return super.useOn(context);
	}
}
