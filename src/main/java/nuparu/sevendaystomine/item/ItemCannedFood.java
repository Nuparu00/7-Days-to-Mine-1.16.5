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
		super(properties.tab(ItemGroup.TAB_FOOD).craftRemainder(ModItems.EMPTY_CAN.get()));
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity living) {
		ItemStack original = stack.copy();
		ItemStack result = super.finishUsingItem(stack, worldIn, living);
		if (!(living instanceof PlayerEntity))
			return result;
		PlayerEntity player = (PlayerEntity) living;
		if ((original.getDamageValue()+1 == original.getMaxDamage()) && this.hasContainerItem(result)) {
			ItemStack itemStack = this.getContainerItem(original).copy();
			if (!worldIn.isClientSide()) {
				if (!player.addItem(itemStack)) {
					player.drop(itemStack, false);
				}
			}
		}
		return result;
	}

}
