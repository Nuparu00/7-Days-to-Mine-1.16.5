package nuparu.sevendaystomine.crafting.forge;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import nuparu.sevendaystomine.tileentity.TileEntityForge;

public interface IForgeRecipe<T extends TileEntityForge> extends IRecipe<T> {
	ItemStack getMoldItem();
	float getExperience();
	int getCookingTime();
	int getRatio(TileEntityForge forge);

	default boolean consume(TileEntityForge forge){
		return false;
	}
}
