package nuparu.sevendaystomine.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModItems;

public class ItemCannedFood extends Item {

	public ItemCannedFood(Item.Properties properties) {
		super(properties.tab(ItemGroup.TAB_FOOD));
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity living) {
		stack = super.finishUsingItem(stack, worldIn, living);
		if (living instanceof PlayerEntity)
			return stack;
		PlayerEntity player = (PlayerEntity) living;
		if (stack.getDamageValue() + 1 == stack.getMaxDamage() && this.hasContainerItem(stack)) {
			ItemStack itemStack = this.getContainerItem(stack);
			/*if (!player.addItem(itemStack)) {
				player.drop(itemStack, false);
			}*/
			return itemStack.copy();
		}
		return stack;
	}

}
