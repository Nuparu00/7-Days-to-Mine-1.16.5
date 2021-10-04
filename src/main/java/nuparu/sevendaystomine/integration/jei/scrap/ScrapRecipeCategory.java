package nuparu.sevendaystomine.integration.jei.scrap;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICustomCraftingCategoryExtension;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Size2i;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.crafting.grill.GrillRecipeShapeless;

import java.util.List;

public class ScrapRecipeCategory implements IRecipeCategory<ScrapRecipeWrapper> {
    public static ResourceLocation ID = new ResourceLocation(SevenDaysToMine.MODID,"scrap");

    private static final int craftOutputSlot = 0;
    private static final int craftInputSlot1 = 1;

    public static final int width = 116;
    public static final int height = 54;

    private final IDrawable background;
    private final IDrawable icon;
    private final ITextComponent localizedName;
    private final ICraftingGridHelper craftingGridHelper;

    public ScrapRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation("jei:textures/gui/gui_vanilla.png");
        background = guiHelper.createDrawable(location, 0, 60, width, height);
        icon = guiHelper.createDrawableIngredient(new ItemStack(Blocks.CRAFTING_TABLE));
        localizedName = new TranslationTextComponent("gui.jei.category.craftingTable");
        craftingGridHelper = guiHelper.createCraftingGridHelper(craftInputSlot1);
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends ScrapRecipeWrapper> getRecipeClass() {
        return ScrapRecipeWrapper.class;
    }

    @Override
    public String getTitle() {
        return getTitleAsTextComponent().getString();
    }

    @Override
    public ITextComponent getTitleAsTextComponent() {
        return localizedName;
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
    public void setIngredients(ScrapRecipeWrapper recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM,recipe.result);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ScrapRecipeWrapper recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(craftOutputSlot, false, 94, 18);

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                int index = craftInputSlot1 + x + (y * 3);
                guiItemStacks.init(index, true, x * 18, y * 18);
            }
        }

        recipeLayout.setShapeless();
        guiItemStacks.set(ingredients);
    }
}
