package nuparu.sevendaystomine.crafting;

import net.minecraft.item.crafting.ICraftingRecipe;

public interface IRecipeLocked extends ICraftingRecipe {
    String getRecipe();

    default boolean hasRecipe() {
        return getRecipe() != null && !getRecipe().isEmpty();
    }

}
