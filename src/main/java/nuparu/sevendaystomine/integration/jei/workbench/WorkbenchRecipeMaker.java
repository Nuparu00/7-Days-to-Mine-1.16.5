package nuparu.sevendaystomine.integration.jei.workbench;

import com.google.common.collect.Lists;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.helpers.IStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import nuparu.sevendaystomine.crafting.workbench.RecipeWorkbenchShaped;

import java.util.ArrayList;
import java.util.List;

public class WorkbenchRecipeMaker {

    public static List<RecipeWorkbenchShaped> getRecipes(IJeiHelpers helpers) {
        IStackHelper stackHelper = helpers.getStackHelper();
        return RecipeWorkbenchShaped.RECIPES;
    }
}
