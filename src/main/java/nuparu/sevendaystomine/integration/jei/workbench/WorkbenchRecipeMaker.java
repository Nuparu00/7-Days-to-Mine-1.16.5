package nuparu.sevendaystomine.integration.jei.workbench;

import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.helpers.IStackHelper;
import nuparu.sevendaystomine.crafting.workbench.RecipeWorkbenchShaped;

import java.util.List;

public class WorkbenchRecipeMaker {

    public static List<RecipeWorkbenchShaped> getRecipes(IJeiHelpers helpers) {
        IStackHelper stackHelper = helpers.getStackHelper();
        return RecipeWorkbenchShaped.RECIPES;
    }
}
