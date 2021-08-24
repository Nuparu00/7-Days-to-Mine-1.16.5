package nuparu.sevendaystomine.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ItemMP3 extends Item {

	public ItemMP3() {
		super(new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_TOOLS));
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		if (!playerIn.isCrouching()) {
			SevenDaysToMine.proxy.openClientOnlyGui(0, stack);
			return ActionResult.success(stack);
		}
		return ActionResult.pass(stack);
	}
}
