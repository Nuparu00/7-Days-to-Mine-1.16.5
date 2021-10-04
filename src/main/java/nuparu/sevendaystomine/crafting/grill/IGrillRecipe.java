package nuparu.sevendaystomine.crafting.grill;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import nuparu.sevendaystomine.tileentity.TileEntityForge;
import nuparu.sevendaystomine.tileentity.TileEntityGrill;

public interface IGrillRecipe<T extends TileEntityGrill> extends IRecipe<T> {
	float getExperience();
	int getCookingTime();
}
