package nuparu.sevendaystomine.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.UseAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModItemGroups;
import nuparu.sevendaystomine.potions.Potions;

public class ItemBandage extends Item {

	public ItemBandage() {
		super(new Item.Properties().stacksTo(8).tab(ModItemGroups.TAB_MEDICINE));
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);

		playerIn.startUsingItem(handIn);
		return ActionResult.success(itemstack);

	}

	@Override
	public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entityLiving;
			int dur = this.getUseDuration(stack) - timeLeft;
			if (dur <= this.getUseDuration(stack) * 0.1f) {
				if (!player.isCreative()) {
					stack.shrink(1);
					if (stack.isEmpty()) {
						player.inventory.removeItem(stack);
					}
				}
				player.removeEffect(Potions.BLEEDING.get());
			}
		}
	}
	
	@Override
	public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand)
    {
		if(target.getEffect(Potions.BLEEDING.get()) != null) {
			target.removeEffect(Potions.BLEEDING.get());
			stack.shrink(1);
	        return ActionResultType.SUCCESS;
		}
        return ActionResultType.PASS;
    }

}
