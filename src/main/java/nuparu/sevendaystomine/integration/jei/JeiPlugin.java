package nuparu.sevendaystomine.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.category.extensions.IExtendableRecipeCategory;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.gui.inventory.*;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.crafting.IRecipeLocked;
import nuparu.sevendaystomine.crafting.chemistry.ChemistryRecipeShapeless;
import nuparu.sevendaystomine.crafting.forge.ForgeRecipeMaterial;
import nuparu.sevendaystomine.crafting.forge.ForgeRecipeShapeless;
import nuparu.sevendaystomine.crafting.scrap.ScrapDataManager;
import nuparu.sevendaystomine.crafting.workbench.RecipeWorkbenchShaped;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.init.ModRecipeSerializers;
import nuparu.sevendaystomine.integration.jei.beaker.BeakerRecipeCategory;
import nuparu.sevendaystomine.integration.jei.chemistry.ChemistryShapelessRecipeCategory;
import nuparu.sevendaystomine.integration.jei.forge.ForgeMaterialRecipeCategory;
import nuparu.sevendaystomine.integration.jei.forge.ForgeShapelessRecipeCategory;
import nuparu.sevendaystomine.integration.jei.grill.GrillRecipeCategory;
import nuparu.sevendaystomine.integration.jei.locked.LockedExtension;
import nuparu.sevendaystomine.integration.jei.pot.PotRecipeCategory;
import nuparu.sevendaystomine.integration.jei.scrap.ScrapRecipeCategory;
import nuparu.sevendaystomine.integration.jei.scrap.ScrapRecipeWrapper;
import nuparu.sevendaystomine.integration.jei.workbench.WorkbenchRecipeCategory;
import nuparu.sevendaystomine.inventory.block.*;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.tileentity.TileEntityChemistryStation;
import nuparu.sevendaystomine.tileentity.TileEntityForge;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
    public static IGuiHelper guiHelper;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(SevenDaysToMine.MODID,"jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IJeiHelpers helpers = registry.getJeiHelpers();
        final IGuiHelper gui = helpers.getGuiHelper();

        guiHelper = gui;

        registry.addRecipeCategories(new WorkbenchRecipeCategory(gui));
        registry.addRecipeCategories(new GrillRecipeCategory(gui));
        registry.addRecipeCategories(new ForgeShapelessRecipeCategory(gui));
        registry.addRecipeCategories(new ScrapRecipeCategory(gui));
        registry.addRecipeCategories(new ForgeMaterialRecipeCategory(gui));
        registry.addRecipeCategories(new PotRecipeCategory(gui));
        registry.addRecipeCategories(new BeakerRecipeCategory(gui));
        registry.addRecipeCategories(new ChemistryShapelessRecipeCategory(gui));
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registry) {
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();

        registry.addRecipes(getRecipes(manager, RecipeWorkbenchShaped.class),WorkbenchRecipeCategory.ID);
        registry.addRecipes(getRecipes(manager, ModRecipeSerializers.GRILL.getA()),new ResourceLocation(SevenDaysToMine.MODID,"cooking_grill"));
        registry.addRecipes(getRecipes(manager, ForgeRecipeShapeless.class),ForgeShapelessRecipeCategory.ID);
        registry.addRecipes(getRecipes(manager, ForgeRecipeMaterial.class),ForgeMaterialRecipeCategory.ID);
        registry.addRecipes(getScrapRecipes(),ScrapRecipeCategory.ID);
        registry.addRecipes(getRecipes(manager, ModRecipeSerializers.COOKING_POT.getA()),PotRecipeCategory.ID);
        registry.addRecipes(getRecipes(manager, ModRecipeSerializers.BEAKER.getA()),BeakerRecipeCategory.ID);
        registry.addRecipes(getRecipes(manager, ChemistryRecipeShapeless.class), ChemistryShapelessRecipeCategory.ID);

        registry.addIngredientInfo(new ItemStack(ModBlocks.WORKBENCH.get()), VanillaTypes.ITEM, new TranslationTextComponent("jei.information.workbench"));
        registry.addIngredientInfo(new ItemStack(ModBlocks.SEPARATOR.get()), VanillaTypes.ITEM, new TranslationTextComponent("jei.information.separator"));
        registry.addIngredientInfo(new ItemStack(ModItems.STONE_AXE.get()), VanillaTypes.ITEM, new TranslationTextComponent("jei.information.stone_axe"));
        registry.addIngredientInfo(new ItemStack(ModBlocks.WIND_TURBINE.get()), VanillaTypes.ITEM, new TranslationTextComponent("jei.information.wind_turbine"));
        registry.addIngredientInfo(new ItemStack(ModBlocks.SOLAR_PANEL.get()), VanillaTypes.ITEM, new TranslationTextComponent("jei.information.solar_panel"));
        registry.addIngredientInfo(new ItemStack(ModBlocks.GENERATOR_COMBUSTION.get()), VanillaTypes.ITEM, new TranslationTextComponent("jei.information.combustion_generator"));
        registry.addIngredientInfo(new ItemStack(ModBlocks.GENERATOR_GAS.get()), VanillaTypes.ITEM, new TranslationTextComponent("jei.information.gas_generator"));
        registry.addIngredientInfo(new ItemStack(ModBlocks.FORGE.get()), VanillaTypes.ITEM, new TranslationTextComponent("jei.information.forge"));
        registry.addIngredientInfo(new ItemStack(ModBlocks.CAMERA.get()), VanillaTypes.ITEM, new TranslationTextComponent("jei.information.camera"));
        registry.addIngredientInfo(new ItemStack(ModBlocks.BATTERY_STATION.get()), VanillaTypes.ITEM, new TranslationTextComponent("jei.information.battery_station"));
        registry.addIngredientInfo(new ItemStack(ModItems.STETHOSCOPE.get()), VanillaTypes.ITEM, new TranslationTextComponent("jei.information.stethoscope"));
        registry.addIngredientInfo(new ItemStack(ModItems.BLOOD_DRAW_KIT.get()), VanillaTypes.ITEM, new TranslationTextComponent("jei.information.blood_draw_kit"));
        registry.addIngredientInfo(new ItemStack(ModBlocks.THERMOMETER.get()), VanillaTypes.ITEM, new TranslationTextComponent("jei.information.thermometer"));

    }

    public static Collection<?> getRecipes(RecipeManager manager, IRecipeType<?> type){
        return manager.getRecipes().parallelStream().filter(iRecipe -> iRecipe.getType() == type).collect(Collectors.toList());
    }

    public static Collection<?> getRecipes(RecipeManager manager, Class clazz){
        return manager.getRecipes().parallelStream().filter(iRecipe -> clazz.isAssignableFrom(iRecipe.getClass())).collect(Collectors.toList());
    }

    /*
    Tries to generate all possibly one-item scrap recipes
     */
    public static List<ScrapRecipeWrapper> getScrapRecipes(){
        ArrayList<ScrapRecipeWrapper> result = new ArrayList<>();

        for(EnumMaterial material : EnumMaterial.values()){
            if(ScrapDataManager.instance.hasScrapResult(material)){
                //ItemStack scrapResult = ScrapDataManager.instance.getScrapResult(material);
                ScrapDataManager.ScrapEntry scrapResult = ScrapDataManager.instance.getScrapResult(material);
                for(ScrapDataManager.ScrapEntry entry : ScrapDataManager.instance.getScraps()){
                    if(entry.item == null) continue;
                    if(entry.material == material) {
                        if (entry.weight > scrapResult.weight) {
                            NonNullList<ItemStack> ingredients = NonNullList.create();
                            ingredients.add(new ItemStack(entry.item,1));
                            //For some reason the stack sometimes is air, no idea why, maybe wrong item id in some of the scrap files
                            if(ingredients.get(0).isEmpty()) continue;
                            result.add(new ScrapRecipeWrapper(ingredients, new ItemStack(scrapResult.item, (int) Math.floor(entry.weight * CommonConfig.scrapCoefficient.get()))));
                        }
                        else{
                            int inputCount = (int)Math.ceil(1d/(entry.weight * CommonConfig.scrapCoefficient.get()));
                            if(inputCount < 1) continue;;
                            NonNullList<ItemStack> ingredients = NonNullList.create();
                            ingredients.add(new ItemStack(entry.item,inputCount));
                            //For some reason the stack sometimes is air, no idea why, maybe wrong item id in some of the scrap files
                            if(ingredients.get(0).isEmpty()) continue;
                            result.add(new ScrapRecipeWrapper(ingredients, new ItemStack(scrapResult.item, (int) Math.floor(entry.weight * inputCount * CommonConfig.scrapCoefficient.get()))));

                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.COOKING_GRILL.get()), new ResourceLocation(SevenDaysToMine.MODID,"cooking_grill"));
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.WORKBENCH.get()), WorkbenchRecipeCategory.ID);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FORGE.get()), ForgeShapelessRecipeCategory.ID);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FORGE.get()), ForgeMaterialRecipeCategory.ID);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.COOKING_POT.get()), PotRecipeCategory.ID);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.BEAKER.get()), BeakerRecipeCategory.ID);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CHEMISTRY_STATION.get()), ChemistryShapelessRecipeCategory.ID);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(ContainerGrill.class,new ResourceLocation(SevenDaysToMine.MODID,"cooking_grill"),0,4,5,36);
        registration.addRecipeTransferHandler(ContainerWorkbench.class,new ResourceLocation(SevenDaysToMine.MODID,"workbench"),1,25,26,36);
        registration.addRecipeTransferHandler(ContainerForge.class,ForgeMaterialRecipeCategory.ID, 0,5,TileEntityForge.EnumSlots.MOLD_SLOT.ordinal(),36);
        registration.addRecipeTransferHandler(ContainerCookingPot.class,PotRecipeCategory.ID,0,4,5,36);
        registration.addRecipeTransferHandler(ContainerBeaker.class,BeakerRecipeCategory.ID,0,4,5,36);
        registration.addRecipeTransferHandler(ContainerChemistryStation.class, ChemistryShapelessRecipeCategory.ID, 0,4, TileEntityChemistryStation.EnumSlots.values().length, 36);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(GuiGrill.class, 89, 37, 28, 23, new ResourceLocation(SevenDaysToMine.MODID,"cooking_grill"));
        registration.addRecipeClickArea(GuiForge.class, 118, 43, 28, 23, ForgeShapelessRecipeCategory.ID, ForgeMaterialRecipeCategory.ID);
        registration.addRecipeClickArea(GuiCookingPot.class, 89, 37, 28, 23, PotRecipeCategory.ID);
        registration.addRecipeClickArea(GuiBeaker.class, 89, 37, 28, 23, BeakerRecipeCategory.ID);
        registration.addRecipeClickArea(GuiChemistryStation.class, 118, 43, 28, 23, ChemistryShapelessRecipeCategory.ID);
        registration.addRecipeClickArea(GuiWorkbench.class, 98, 43, 28, 23, WorkbenchRecipeCategory.ID, VanillaRecipeCategoryUid.CRAFTING);
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        IExtendableRecipeCategory<ICraftingRecipe, ICraftingCategoryExtension> craftingCategory = registration.getCraftingCategory();
        craftingCategory.addCategoryExtension(IRecipeLocked.class, LockedExtension::new);
    }
}
