package nuparu.sevendaystomine.integration.jei.workbench;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;

import java.util.List;

public class WorkbenchRecipe {
    private final List<ItemStack> inputs;
    private final ItemStack output;

    public WorkbenchRecipe(List<ItemStack> inputs, ItemStack output) {
        super();
        this.inputs = inputs;
        this.output = output;
    }

    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, inputs);
        ingredients.setOutput(VanillaTypes.ITEM, output);
    }


}
