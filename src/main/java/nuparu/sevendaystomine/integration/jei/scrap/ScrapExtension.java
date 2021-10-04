package nuparu.sevendaystomine.integration.jei.scrap;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICustomCraftingCategoryExtension;
import net.minecraft.item.ItemStack;
import nuparu.sevendaystomine.crafting.scrap.RecipesScraps;

public class ScrapExtension<T extends RecipesScraps> implements ICustomCraftingCategoryExtension {

    private final T recipe;

    public ScrapExtension(T recipe) {
        this.recipe = recipe;
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = layout.getItemStacks();
    }

    @Override
    public void setIngredients(IIngredients ingredients) {

    }
}