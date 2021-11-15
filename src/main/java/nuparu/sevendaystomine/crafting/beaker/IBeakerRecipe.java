package nuparu.sevendaystomine.crafting.beaker;

import net.minecraft.item.crafting.IRecipe;
import nuparu.sevendaystomine.tileentity.TileEntityBeaker;

public interface IBeakerRecipe<T extends TileEntityBeaker> extends IRecipe<T> {
	float getExperience();
	int getCookingTime();
}
