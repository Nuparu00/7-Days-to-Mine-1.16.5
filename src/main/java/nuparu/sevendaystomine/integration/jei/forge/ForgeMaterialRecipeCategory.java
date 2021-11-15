package nuparu.sevendaystomine.integration.jei.forge;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.crafting.forge.ForgeRecipeMaterial;
import nuparu.sevendaystomine.crafting.forge.MaterialStack;
import nuparu.sevendaystomine.crafting.scrap.ScrapDataManager;
import nuparu.sevendaystomine.item.EnumMaterial;

import java.util.ArrayList;

public class ForgeMaterialRecipeCategory extends AbstractForgeRecipeCategory<ForgeRecipeMaterial>{

    public static ResourceLocation ID = new ResourceLocation(SevenDaysToMine.MODID,"forge_material");
    public ForgeMaterialRecipeCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class getRecipeClass() {
        return ForgeRecipeMaterial.class;
    }

    @Override
    public void setIngredients(ForgeRecipeMaterial recipe, IIngredients ingredients) {
        ArrayList<Ingredient> list = new ArrayList<>();
        list.add(Ingredient.of(recipe.getMoldItem()));

        for(MaterialStack materialStack : recipe.getMaterialIngredients()){
            EnumMaterial material = materialStack.getMaterial();
            int weight = materialStack.getWeight();

            ScrapDataManager.ScrapEntry entry = ScrapDataManager.instance.getSmallestItem(material);
            if(entry != null){
                ItemStack stack = new ItemStack(entry.item, (int)Math.ceil((double)weight/entry.weight));
                if(stack.isEmpty()) continue;
                list.add(Ingredient.of(stack));
            }

        }

        ingredients.setInputIngredients(list);
        ingredients.setOutput(VanillaTypes.ITEM,recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ForgeRecipeMaterial recipe, IIngredients ingredients) {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        int id = 0;
        stacks.init(id++, true, 2,37);

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                stacks.init(id++, true, 40 + j * 18 - 5, 11 + i * 18 - 5);
            }
        }
        stacks.init(id++, false, 104, 37);
        stacks.set(ingredients);
    }


}
