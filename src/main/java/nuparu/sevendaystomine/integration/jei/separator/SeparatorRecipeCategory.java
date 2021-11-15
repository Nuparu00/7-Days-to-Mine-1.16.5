package nuparu.sevendaystomine.integration.jei.separator;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.crafting.separator.SeparatorRecipe;
import nuparu.sevendaystomine.init.ModBlocks;

public class SeparatorRecipeCategory implements IRecipeCategory<SeparatorRecipe> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
            "textures/gui/container/separator.png");

    public static final ResourceLocation ID = new ResourceLocation(SevenDaysToMine.MODID, "separator");

    private final IDrawable background;
    private final IDrawable icon;
    private final String name;

    private final IDrawableAnimated flame;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrowsReversed;

    public SeparatorRecipeCategory(IGuiHelper helper) {
        background = helper.createDrawable(TEXTURE, 4, 4, 166, 79);
        icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.SEPARATOR.get()));
        name = "Separator";
        flame = helper.drawableBuilder(TEXTURE, 176, 0, 14, 14)
                .buildAnimated(200, IDrawableAnimated.StartDirection.TOP, true);
        this.cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<Integer, IDrawableAnimated>() {
                    @Override
                    public IDrawableAnimated load(Integer cookTime) {
                        return helper.drawableBuilder(TEXTURE, 176, 14, 24, 17)
                                .buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });

        this.cachedArrowsReversed = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<Integer, IDrawableAnimated>() {
                    @Override
                    public IDrawableAnimated load(Integer cookTime) {
                        return helper.drawableBuilder(TEXTURE, 176, 31, 25, 17)
                                .buildAnimated(cookTime, IDrawableAnimated.StartDirection.RIGHT, false);
                    }
                });
    }

    protected IDrawableAnimated getArrow(SeparatorRecipe recipe, boolean reversed) {
        int cookTime = recipe.getCookingTime();
        if (cookTime <= 0) {
            cookTime = 600;
        }
        return reversed ? this.cachedArrowsReversed.getUnchecked(cookTime) : this.cachedArrows.getUnchecked(cookTime);
    }

    protected void drawExperience(SeparatorRecipe recipe, MatrixStack matrixStack, int y) {
        float experience = recipe.getExperience();
        if (experience > 0) {
            TranslationTextComponent experienceString = new TranslationTextComponent("gui.jei.category.smelting.experience", experience);
            Minecraft minecraft = Minecraft.getInstance();
            FontRenderer fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(experienceString);
            fontRenderer.draw(matrixStack, experienceString, background.getWidth() - stringWidth, y, 0xFF808080);
        }
    }

    protected void drawCookTime(SeparatorRecipe recipe, MatrixStack matrixStack, int y) {
        int cookTime = recipe.getCookingTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            TranslationTextComponent timeString = new TranslationTextComponent("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            FontRenderer fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            fontRenderer.draw(matrixStack, timeString, background.getWidth() - stringWidth, y, 0xFF808080);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends SeparatorRecipe> getRecipeClass() {
        return SeparatorRecipe.class;
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
    public void setIngredients(SeparatorRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        NonNullList<ItemStack> output = NonNullList.create();
        output.add(recipe.getResultLeft());
        output.add(recipe.getResultRight());
        ingredients.setOutputs(VanillaTypes.ITEM,output);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SeparatorRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();

        stacks.init(0, true, 75, 37);
        stacks.init(1, false, 7, 37);
        stacks.init(2, false, 143, 37);
        stacks.set(ingredients);
    }

    @Override
    public void draw(SeparatorRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        flame.draw(matrixStack, 78, 55);
        getArrow(recipe,false).draw(matrixStack, 103, 38);
        getArrow(recipe,true).draw(matrixStack, 40, 38);
        matrixStack.pushPose();
        drawExperience(recipe, matrixStack, 10);
        drawCookTime(recipe, matrixStack, 65);
        RenderSystem.enableBlend();
        matrixStack.popPose();
    }
}
