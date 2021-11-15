package nuparu.sevendaystomine.item;

import net.minecraft.item.ItemStack;

public interface IQuality {

	int getQuality(ItemStack stack);

	EnumQuality getQualityTierFromStack(ItemStack stack);

	EnumQuality getQualityTierFromInt(int quality);

	void setQuality(ItemStack stack, int quality);

}
