package nuparu.sevendaystomine.item;

import java.util.ArrayList;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;
import nuparu.sevendaystomine.potions.Potions;

public class ItemAlcoholDrink extends ItemDrink {

	public ItemAlcoholDrink(Item.Properties properties, int thirst, int stamina) {
		super(properties, thirst, stamina);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity living) {
		stack = super.finishUsingItem(stack, worldIn, living);
		if (living instanceof PlayerEntity)
			return stack;
		PlayerEntity player = (PlayerEntity) living;

		if (worldIn.isClientSide())
			return stack;
		if (!player.hasEffect(Potions.ALCOHOL_BUZZ.get()) && !player.hasEffect(Potions.DRUNK.get())
				&& !player.hasEffect(Potions.ALCOHOL_POISON.get())) {
			EffectInstance effect = new EffectInstance(Potions.ALCOHOL_BUZZ.get(), 6000, 4, false, false);
			effect.setCurativeItems(new ArrayList<ItemStack>());
			player.addEffect(effect);
		} else if (player.hasEffect(Potions.ALCOHOL_BUZZ.get()) && random.nextInt(3) != 0) {
			EffectInstance effect = player.getEffect(Potions.ALCOHOL_BUZZ.get()) != null
					? player.getEffect(Potions.ALCOHOL_BUZZ.get())
					: player.getEffect(Potions.DRUNK.get());
			if (worldIn.random.nextInt(Math.max(6000 - (effect.getDuration() * effect.getAmplifier()), 1)) == 0) {
				EffectInstance effect_new = new EffectInstance(Potions.DRUNK.get(), 6000, 4, false, false);
				effect_new.setCurativeItems(new ArrayList<ItemStack>());
				player.addEffect(effect_new);
				player.removeEffect(Potions.ALCOHOL_BUZZ.get());
			}
		} else if (player.hasEffect(Potions.DRUNK.get())
				|| player.hasEffect(Potions.ALCOHOL_POISON.get()) && random.nextInt(5) != 0) {
			EffectInstance effect = player.getEffect(Potions.DRUNK.get());
			if (effect != null) {
				if (worldIn.random.nextInt(Math.max(6000 - (effect.getDuration() * effect.getAmplifier()), 1)) == 0) {
					EffectInstance effect_new = new EffectInstance(Potions.ALCOHOL_POISON.get(), 6000, 4, false, false);
					effect_new.setCurativeItems(new ArrayList<ItemStack>());
					player.addEffect(effect_new);
					player.removeEffect(Potions.DRUNK.get());
				}
			} else {
				EffectInstance effect_new = new EffectInstance(Potions.ALCOHOL_POISON.get(), 6000, 4, false, false);
				effect_new.setCurativeItems(new ArrayList<ItemStack>());
				player.addEffect(effect_new);
			}
		}
		return stack;
	}

}
