package nuparu.sevendaystomine.integration.jei.grill;

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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.crafting.grill.GrillRecipeShapeless;
import nuparu.sevendaystomine.init.ModBlocks;

public class GrillRecipeCategory implements IRecipeCategory<GrillRecipeShapeless> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
            "textures/gui/container/cooking_grill.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final String name;

    private final IDrawableAnimated flame;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    public GrillRecipeCategory(IGuiHelper helper) {
        background = helper.createDrawable(TEXTURE, 4, 4, 156, 79);
        icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.COOKING_GRILL.get()));
        name = "Cooking Grill";
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
    }

    protected IDrawableAnimated getArrow(GrillRecipeShapeless recipe) {
        int cookTime = recipe.getCookingTime();
        if (cookTime <= 0) {
            cookTime = 600;
        };
        return this.cachedArrows.getUnchecked(cookTime);
    }

    protected void drawExperience(GrillRecipeShapeless recipe, MatrixStack matrixStack, int y) {
        float experience = recipe.getExperience();
        if (experience > 0) {
            TranslationTextComponent experienceString = new TranslationTextComponent("gui.jei.category.smelting.experience", experience);
            Minecraft minecraft = Minecraft.getInstance();
            FontRenderer fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(experienceString);
            fontRenderer.draw(matrixStack, experienceString, background.getWidth() - stringWidth, y, 0xFF808080);
        }
    }

    protected void drawCookTime(GrillRecipeShapeless recipe, MatrixStack matrixStack, int y) {
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
        return new ResourceLocation(SevenDaysToMine.MODID, "cooking_grill");
    }

    @Override
    public Class<? extends GrillRecipeShapeless> getRecipeClass() {
        return GrillRecipeShapeless.class;
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
    public void setIngredients(GrillRecipeShapeless recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM,recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, GrillRecipeShapeless recipe, IIngredients ingredients) {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        int id = 0;
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                stacks.init(id++, true, 47 + j * 18 - 5, 27 + i * 18 - 5);
            }
        }
        recipeLayout.setShapeless();
        stacks.init(id++, false, 118, 31);
        stacks.set(ingredients);
    }

    @Override
    public void draw(GrillRecipeShapeless recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        flame.draw(matrixStack, 53, 58);
        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(matrixStack, 84, 32);
        matrixStack.pushPose();
        drawExperience(recipe, matrixStack, 10);
        drawCookTime(recipe, matrixStack, 65);
        RenderSystem.enableBlend();
        matrixStack.popPose();
    }
}
