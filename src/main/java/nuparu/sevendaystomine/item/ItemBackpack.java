package nuparu.sevendaystomine.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModItemGroups;

public class ItemBackpack extends Item {

	public ItemBackpack() {
		super(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_CLOTHING));
	}
	
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		if (!playerIn.isCrouching()) {
			/*NetworkHooks.openGui(playerIn, containerSupplier);
			playerIn.openGui(SevenDaysToMine.instance, 18, worldIn, (int)playerIn.getX(), (int)playerIn.getY(), (int)playerIn.getZ());
				return ActionResult.success(stack);*/
		}
		return ActionResult.pass(stack);
	}

	/*
	@Override
	@Nullable
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT compound) {
		return new ExtendedInventoryProvider().setSize(9);
	}*/
}
