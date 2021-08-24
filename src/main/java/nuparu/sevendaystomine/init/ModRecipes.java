package nuparu.sevendaystomine.init;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.crafting.DummyFurnaceRecipe;
import nuparu.sevendaystomine.crafting.DummyRecipe;
import nuparu.sevendaystomine.crafting.RecipeGunShapeless;
import nuparu.sevendaystomine.crafting.RecipeLockedShaped;
import nuparu.sevendaystomine.crafting.RecipeLockedShapeless;
import nuparu.sevendaystomine.crafting.RecipeQualityShaped;
import nuparu.sevendaystomine.crafting.RecipeQualityShapeless;
import nuparu.sevendaystomine.crafting.RecipesScraps;
import nuparu.sevendaystomine.crafting.workbench.RecipeWorkbenchShaped;

public class ModRecipes {
	public static final DeferredRegister<IRecipeSerializer<?>> RECIPES = DeferredRegister
			.create(ForgeRegistries.RECIPE_SERIALIZERS, SevenDaysToMine.MODID);
	
	public static final RegistryObject<IRecipeSerializer<?>> QUALITY_SHAPED = RECIPES.register("quality_shaped",
			() -> new RecipeQualityShaped.Serializer());
	public static final RegistryObject<IRecipeSerializer<?>> LOCKED_SHAPED = RECIPES.register("locked_shaped",
			() -> new RecipeLockedShaped.Serializer());
	public static final RegistryObject<IRecipeSerializer<?>> QUALITY_SHAPELESS = RECIPES.register("quality_shapeless",
			() -> new RecipeQualityShapeless.Serializer());
	public static final RegistryObject<IRecipeSerializer<?>> LOCKED_SHAPELESS = RECIPES.register("locked_shapeless",
			() -> new RecipeLockedShapeless.Serializer());
	public static final RegistryObject<IRecipeSerializer<?>> SCRAP = RECIPES.register("recipe_scrap",
			() -> new RecipesScraps.Serializer());
	public static final RegistryObject<IRecipeSerializer<?>> DUMMY_CRAFTING = RECIPES.register("dummy_crafting",
			() -> new DummyRecipe.Serializer());
	public static final RegistryObject<IRecipeSerializer<?>> DUMMY_FURNACE = RECIPES.register("dummy_furnace",
			() -> new DummyFurnaceRecipe.Serializer());
	public static final RegistryObject<IRecipeSerializer<?>> GUN = RECIPES.register("gun",
			() -> new RecipeGunShapeless.Serializer());

	public static final RegistryObject<IRecipeSerializer<?>> WORKBENCH_SHAPED = RECIPES.register("workbench_shaped",
			() -> new RecipeWorkbenchShaped.Serializer());
}
