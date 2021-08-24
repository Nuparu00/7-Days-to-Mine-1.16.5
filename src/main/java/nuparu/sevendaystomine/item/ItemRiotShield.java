package nuparu.sevendaystomine.item;

import javax.annotation.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;

public class ItemRiotShield extends ShieldItem {

	public ItemRiotShield() {
     super(new Item.Properties().durability(500).tab(ItemGroup.TAB_COMBAT));
	}

	@Override
	public boolean isShield(ItemStack stack, @Nullable LivingEntity entity) {
		return true;
	}
}
