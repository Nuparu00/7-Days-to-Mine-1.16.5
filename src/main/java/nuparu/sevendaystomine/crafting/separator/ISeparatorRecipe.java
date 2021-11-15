package nuparu.sevendaystomine.crafting.separator;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import nuparu.sevendaystomine.tileentity.TileEntitySeparator;
import org.apache.commons.lang3.tuple.Pair;

public interface ISeparatorRecipe<T extends TileEntitySeparator> extends IRecipe<T> {
	float getExperience();
	int getCookingTime();
	Pair<ItemStack,ItemStack> assemblePair(T separator);
}
