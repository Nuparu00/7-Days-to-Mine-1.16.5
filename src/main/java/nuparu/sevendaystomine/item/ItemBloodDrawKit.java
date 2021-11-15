package nuparu.sevendaystomine.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.UseAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import nuparu.sevendaystomine.init.ModItemGroups;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.util.DamageSources;

public class ItemBloodDrawKit extends Item {

	public ItemBloodDrawKit() {
		super(new Item.Properties().stacksTo(16).durability(16).tab(ModItemGroups.TAB_MEDICINE));
	}


	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);

		playerIn.startUsingItem(handIn);
		return ActionResult.success(itemstack);

	}

	@Override
	public void releaseUsing(ItemStack stack, World world, LivingEntity entityLiving, int timeLeft) {

		if (entityLiving instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entityLiving;
			int dur = this.getUseDuration(stack) - timeLeft;
			if (dur >= this.getUseDuration(stack) * 0.15f) {
				ItemStack bloodBag = new ItemStack(ModItems.BLOOD_BAG.get());

				/*RayTraceResult entityRay = Utils.raytraceEntities(entityLiving, 2);
				if (entityRay != null) {
					if (entityRay. != null && entityRay.entityHit instanceof LivingEntity) {
						LivingEntity clickedLiving = (LivingEntity) entityRay.entityHit;
						if (!(clickedLiving instanceof MobEntity) && !(clickedLiving instanceof PlayerEntity)) {
							toHurt = clickedLiving;
						} else if ((clickedLiving instanceof PlayerEntity)
								&& !((PlayerEntity) clickedLiving).isCreative()
								&& !((PlayerEntity) clickedLiving).isSpectator()) {
							toHurt = clickedLiving;
						}
					}
				}*/

				if (player instanceof PlayerEntity) {
					bloodBag.getOrCreateTag().putString("donor", player.getUUID().toString());
				}

				if (!world.isClientSide() && player != player) {
					//ModTriggers.MOSCO.trigger((ServerPlayerEntity) player);
				}

				if (!player.inventory.add(bloodBag)) {
					player.drop(bloodBag, false);
				}
				((LivingEntity) player).hurt(DamageSources.bleeding, 4);
				if (player instanceof ServerPlayerEntity) {
					stack.hurt(1, world.random, (ServerPlayerEntity) entityLiving);
					if(stack.getDamageValue() >= this.getMaxDamage(stack)) {
						stack.setCount(0);
					}
				}
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
