package nuparu.sevendaystomine.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;

public interface IRecipeLocked extends ICraftingRecipe {
    String getRecipe();

    default boolean hasRecipe() {
        return getRecipe() != null && !getRecipe().isEmpty();
    }

}
