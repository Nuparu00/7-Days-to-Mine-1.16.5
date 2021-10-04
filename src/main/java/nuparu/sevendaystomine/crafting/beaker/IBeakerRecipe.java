package nuparu.sevendaystomine.crafting.beaker;

import net.minecraft.item.crafting.IRecipe;
import nuparu.sevendaystomine.tileentity.TileEntityBeaker;
import nuparu.sevendaystomine.tileentity.TileEntityGrill;

public interface IBeakerRecipe<T extends TileEntityBeaker> extends IRecipe<T> {
	float getExperience();
	int getCookingTime();
}
