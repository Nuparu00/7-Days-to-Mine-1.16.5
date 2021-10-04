package nuparu.sevendaystomine.integration.jei.locked;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICustomCraftingCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import nuparu.sevendaystomine.crafting.IRecipeLocked;
import nuparu.sevendaystomine.crafting.RecipeLockedShaped;
import nuparu.sevendaystomine.crafting.grill.GrillRecipeShapeless;
import nuparu.sevendaystomine.crafting.scrap.RecipesScraps;
import nuparu.sevendaystomine.integration.jei.JeiPlugin;

import java.util.Collections;
import java.util.List;

public class LockedExtension<T extends IRecipeLocked> implements ICraftingCategoryExtension {

    private final T recipe;
    LockedIcon lockedIcon;
    int recipeWidth;
    int recipeHeight;

    public LockedExtension(T recipe) {
        this.recipe = recipe;
        lockedIcon = new LockedIcon();
    }

    @Override
    public void setIngredients(IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM,recipe.getResultItem());
    }


    @Override
    public void drawInfo(int recipeWidth, int recipeHeight, MatrixStack matrixStack, double mouseX, double mouseY) {
        this.recipeWidth = recipeWidth;
        this.recipeHeight = recipeHeight;
        //drawRecipe(recipe,matrixStack,recipeHeight-10,recipeWidth);
        lockedIcon.draw(matrixStack,recipeWidth);
    }

    @Override
    public List<ITextComponent> getTooltipStrings(double mouseX, double mouseY)
    {
        if(lockedIcon != null){
            List<ITextComponent> list = lockedIcon.getTooltipStrings((int)mouseX,(int)mouseY,recipeWidth,recipeHeight, recipe.getRecipe());
            if(list != null) return list;
        }
        return Collections.emptyList();
    }

    protected void drawRecipe(T recipe, MatrixStack matrixStack, int y, int recipeWidth) {
        String book = recipe.getRecipe();
        if (book != null && !book.isEmpty()) {
            TranslationTextComponent experienceString = new TranslationTextComponent("gui.jei.locked.recipe",book);
            Minecraft minecraft = Minecraft.getInstance();
            FontRenderer fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(experienceString);
            fontRenderer.draw(matrixStack, experienceString, recipeWidth-stringWidth, y, 0xFF808080);
        }
        RenderSystem.enableBlend();
    }
}