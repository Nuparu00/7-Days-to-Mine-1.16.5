package nuparu.sevendaystomine.crafting.chemistry;

import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import nuparu.sevendaystomine.tileentity.TileEntityChemistryStation;
import nuparu.sevendaystomine.tileentity.TileEntityForge;

public interface IChemistryRecipe<T extends TileEntityChemistryStation> extends IRecipe<T> {
	float getExperience();
	int getCookingTime();
}
