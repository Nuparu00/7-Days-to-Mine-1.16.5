package nuparu.sevendaystomine.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import nuparu.sevendaystomine.potions.Potions;

public class ItemAdvancedBandage extends ItemBandage {
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
				player.addEffect(new EffectInstance(Effects.REGENERATION, 200, 1));
			}
		}
	}
}
