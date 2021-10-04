package nuparu.sevendaystomine.integration.jei.scrap;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.List;

public class ScrapRecipeWrapper {
    NonNullList<ItemStack> ingredients;
    ItemStack result;

    public ScrapRecipeWrapper(NonNullList<ItemStack> ingredients, ItemStack result){
        this.ingredients = ingredients;
        this.result = result;
    }

    public List<Ingredient> getIngredients(){
        ArrayList<Ingredient> list = new ArrayList<>();
        for(ItemStack stack : ingredients){
            list.add(Ingredient.of(stack));
        }

        return list;
    }
}
