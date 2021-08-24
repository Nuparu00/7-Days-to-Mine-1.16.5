package nuparu.sevendaystomine.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import nuparu.sevendaystomine.potions.Potions;

public class ItemTea extends ItemDrink {

	public ItemTea(Item.Properties properties, int thirst, int stamina) {
		super(properties, thirst, stamina);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity living) {
		stack = super.finishUsingItem(stack, worldIn, living);
		if (living instanceof PlayerEntity)
			return stack;
		PlayerEntity player = (PlayerEntity) living;
		player.removeEffect(Potions.DYSENTERY.get());
		return stack;
	}

}
