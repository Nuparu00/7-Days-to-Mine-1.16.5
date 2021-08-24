package nuparu.sevendaystomine.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.UseAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import nuparu.sevendaystomine.init.ModItemGroups;

public class ItemBloodBag extends Item {
	
	public ItemBloodBag() {
		super(new Item.Properties().stacksTo(1).durability(0).tab(ModItemGroups.TAB_MEDICINE));
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
					
					CompoundNBT nbt = stack.getOrCreateTag();
					if(nbt != null && nbt.contains("donor", Constants.NBT.TAG_STRING)) {
						String uuid = nbt.getString("donor");
						if(!worldIn.isClientSide() && !uuid.equals(player.getUUID().toString())) {
							//ModTriggers.BLOOD_BOND.trigger((ServerPlayerEntity) player);
						}
					}
					stack.shrink(1);
					if (stack.isEmpty()) {
						player.inventory.removeItem(stack);
					}
				}
				player.heal(4);
			}
		}
	}

	@Override
	public int getUseDuration(ItemStack itemStack) {
		return 200;
	}

	@Override
	public UseAction getUseAnimation(ItemStack itemStack) {
		return UseAction.BOW;
	}

}
