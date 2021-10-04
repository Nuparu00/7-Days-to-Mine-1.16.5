package nuparu.sevendaystomine.crafting.pot;

import net.minecraft.item.crafting.IRecipe;
import nuparu.sevendaystomine.tileentity.TileEntityCookingPot;
import nuparu.sevendaystomine.tileentity.TileEntityGrill;

public interface ICookingPotRecipe<T extends TileEntityCookingPot> extends IRecipe<T> {
	float getExperience();
	int getCookingTime();
}
