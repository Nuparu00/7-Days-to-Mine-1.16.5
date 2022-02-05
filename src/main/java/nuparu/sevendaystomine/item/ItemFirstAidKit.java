package nuparu.sevendaystomine.item;

import java.util.ArrayList;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.item.UseAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModItemGroups;
import nuparu.sevendaystomine.potions.Potions;

public class ItemFirstAidKit extends Item {

	public ItemFirstAidKit() {
		super(new Item.Properties().stacksTo(8).durability(0).tab(ModItemGroups.TAB_MEDICINE));
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);

		playerIn.startUsingItem(handIn);
		return ActionResult.success(itemstack);

	}

	@Override
	public int getUseDuration(ItemStack itemStack) {
		return 82000;
	}

	@Override
	public UseAction getUseAnimation(ItemStack itemStack) {
		return UseAction.BOW;
	}

	@Override
	public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entityLiving;
			int dur = this.getUseDuration(stack) - timeLeft;
			System.out.println(dur);
			if (dur >= 20) {
				if (!player.isCreative()) {
					stack.shrink(1);
					if (stack.isEmpty()) {
						player.inventory.removeItem(stack);
					}
				}
				player.removeEffect(Potions.BLEEDING.get());
				player.addEffect(new EffectInstance(Effects.REGENERATION, 600, 1));
				EffectInstance brokeLegEffect = player.getEffect(Potions.BROKEN_LEG.get());
				if (brokeLegEffect != null) {
					player.removeEffect(Potions.BROKEN_LEG.get());
					EffectInstance effect = new EffectInstance(Potions.SPLINTED_LEG.get(), brokeLegEffect.getDuration(),
							brokeLegEffect.getAmplifier(), brokeLegEffect.isAmbient(),
							brokeLegEffect.isVisible());
					effect.setCurativeItems(new ArrayList<ItemStack>());
					player.addEffect(effect);
				}
			}
		}
	}
}
