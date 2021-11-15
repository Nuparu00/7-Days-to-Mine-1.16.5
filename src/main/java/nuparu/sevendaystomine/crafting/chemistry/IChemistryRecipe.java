package nuparu.sevendaystomine.crafting.chemistry;

import net.minecraft.item.crafting.IRecipe;
import nuparu.sevendaystomine.tileentity.TileEntityChemistryStation;

public interface IChemistryRecipe<T extends TileEntityChemistryStation> extends IRecipe<T> {
	float getExperience();
	int getCookingTime();
}
