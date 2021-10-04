package nuparu.sevendaystomine.integration.jei.workbench;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.gui.ingredient.ITooltipCallback;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.crafting.workbench.RecipeWorkbenchShaped;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.integration.jei.locked.LockedIcon;

import java.util.Collections;
import java.util.List;

public class WorkbenchRecipeCategory implements IRecipeCategory<RecipeWorkbenchShaped> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
            "textures/gui/container/workbench.png");
    public static final ResourceLocation ID = new ResourceLocation(SevenDaysToMine.MODID, "workbench");

    private final IDrawable background;
    private final IDrawable icon;
    private final String name;

    LockedIcon lockedIcon;

    int recipeWidth = 156;
    int recipeHeight = 95;

    public WorkbenchRecipeCategory(IGuiHelper helper) {
        background = helper.createDrawable(TEXTURE, 4, 4, recipeWidth, recipeHeight);
        icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.WORKBENCH.get()));
        name = "Workbench";
        lockedIcon = new LockedIcon();
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends RecipeWorkbenchShaped> getRecipeClass() {
        return RecipeWorkbenchShaped.class;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(RecipeWorkbenchShaped recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM,recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, RecipeWorkbenchShaped recipe, IIngredients ingredients) {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        int id = 0;
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                stacks.init(id++, true, 8 + j * 18 - 5, 7 + i * 18 - 5);
            }
        }
        stacks.init(id++, false, 129, 39);
        stacks.set(ingredients);
    }

    @Override
    public void draw(RecipeWorkbenchShaped recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        lockedIcon.draw(matrixStack,recipeWidth);
    }

    @Override
    public List<ITextComponent> getTooltipStrings(RecipeWorkbenchShaped recipe, double mouseX, double mouseY) {
        if(recipe.hasRecipe() && lockedIcon != null){
            List<ITextComponent> list = lockedIcon.getTooltipStrings((int)mouseX,(int)mouseY,recipeWidth,recipeHeight, recipe.getRecipe());
            if(list != null) return list;
        }
        return Collections.emptyList();
    }
}
