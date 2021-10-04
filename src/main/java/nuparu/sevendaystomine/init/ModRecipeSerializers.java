package nuparu.sevendaystomine.init;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.crafting.*;
import nuparu.sevendaystomine.crafting.beaker.BeakerRecipeShapeless;
import nuparu.sevendaystomine.crafting.chemistry.ChemistryRecipeShapeless;
import nuparu.sevendaystomine.crafting.chemistry.IChemistryRecipe;
import nuparu.sevendaystomine.crafting.forge.ForgeRecipeMaterial;
import nuparu.sevendaystomine.crafting.forge.ForgeRecipeShapeless;
import nuparu.sevendaystomine.crafting.forge.IForgeRecipe;
import nuparu.sevendaystomine.crafting.pot.CookingPotRecipeShapeless;
import nuparu.sevendaystomine.crafting.scrap.RecipesScraps;
import nuparu.sevendaystomine.crafting.grill.GrillRecipeShapeless;
import nuparu.sevendaystomine.crafting.workbench.RecipeWorkbenchShaped;
import nuparu.sevendaystomine.tileentity.TileEntityChemistryStation;
import nuparu.sevendaystomine.tileentity.TileEntityForge;

import java.util.function.Supplier;

public class ModRecipeSerializers {

	public static final DeferredRegister<IRecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS,
			SevenDaysToMine.MODID);

	public static final RegistryObject<RecipesScraps.Serializer> SCRAP_SERIALIZER = SERIALIZERS.register("recipe_scrap",
			() -> new RecipesScraps.Serializer());

	public static final RegistryObject<RecipeWorkbenchShaped.Serializer> WORKBENCH_SHAPED = SERIALIZERS.register("workbench_shaped",
			() -> new RecipeWorkbenchShaped.Serializer());

	public static final RegistryObject<RecipeQualityShaped.Serializer> QUALITY_SHAPED = SERIALIZERS.register("quality_shaped",
			() -> new RecipeQualityShaped.Serializer());

	public static final RegistryObject<RecipeQualityShapeless.Serializer> QUALITY_SHAPELESS = SERIALIZERS.register("quality_shapeless",
			() -> new RecipeQualityShapeless.Serializer());


	public static final RegistryObject<RecipeLockedShaped.Serializer> LOCKED_SHAPED = SERIALIZERS.register("locked_shaped",
			() -> new RecipeLockedShaped.Serializer());

	public static final RegistryObject<RecipeLockedShapeless.Serializer> LOCKED_SHAPELESS = SERIALIZERS.register("locked_shapeless",
			() -> new RecipeLockedShapeless.Serializer());

	public static final RegistryObject<IRecipeSerializer<?>> GUN_SHAPELESS = SERIALIZERS.register("gun_shapeless",
			() -> new RecipeGunShapeless.Serializer());

	public static final RegistryObject<IRecipeSerializer<?>> FORGE_SHAPELESS = SERIALIZERS.register("forge_shapeless",
			() -> new ForgeRecipeShapeless.Factory());
	public static final RegistryObject<IRecipeSerializer<?>> FORGE_MATERIAL = SERIALIZERS.register("forge_material",
			() -> new ForgeRecipeMaterial.Factory());
	public static final RegistryObject<IRecipeSerializer<?>> CHEMISTRY_SHAPELESS = SERIALIZERS.register("chemistry_shapeless",
			() -> new ChemistryRecipeShapeless.Factory());

	//Recipe Types
	public static final IRecipeType<IForgeRecipe<TileEntityForge>> FORGE_RECIPE_TYPE = IRecipeType.register(new ResourceLocation(SevenDaysToMine.MODID, "forge").toString());
	public static final IRecipeType<IChemistryRecipe<TileEntityChemistryStation>> CHEMISTRY_RECIPE_TYPE = IRecipeType.register(new ResourceLocation(SevenDaysToMine.MODID, "chemistry").toString());

	public static final Tuple<IRecipeType<GrillRecipeShapeless>, RegistryObject<IRecipeSerializer<GrillRecipeShapeless>>> GRILL = registerRecipeType("grill_shapeless", GrillRecipeShapeless.Factory::new);
	public static final Tuple<IRecipeType<CookingPotRecipeShapeless>, RegistryObject<IRecipeSerializer<CookingPotRecipeShapeless>>> COOKING_POT = registerRecipeType("cooking_pot_shapeless", CookingPotRecipeShapeless.Factory::new);
	public static final Tuple<IRecipeType<BeakerRecipeShapeless>, RegistryObject<IRecipeSerializer<BeakerRecipeShapeless>>> BEAKER = registerRecipeType("beaker_shapeless", BeakerRecipeShapeless.Factory::new);


	//Creedit: https://github.com/Tslat/Advent-Of-Ascension/blob/dfc01e41f4eda6fcc9748e796d31e3f6b1e38d9b/source/common/registration/AoARecipes.java#L20
	private static <T extends IRecipe<I>, I extends IInventory> Tuple<IRecipeType<T>, RegistryObject<IRecipeSerializer<T>>> registerRecipeType(String id, Supplier<IRecipeSerializer<T>> serializer) {
		return new Tuple<IRecipeType<T>, RegistryObject<IRecipeSerializer<T>>>(Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(SevenDaysToMine.MODID, id), new IRecipeType<T>() {
			@Override
			public String toString() {
				return id;
			}
		}), SERIALIZERS.register(id, serializer));
	}

	private static <T extends IRecipe<I>, I extends IInventory> Tuple<IRecipeType<T>, RegistryObject<IRecipeSerializer<T>>> registerRecipeType(String type, String id, Supplier<IRecipeSerializer<T>> serializer) {
		return new Tuple<IRecipeType<T>, RegistryObject<IRecipeSerializer<T>>>(Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(SevenDaysToMine.MODID, type), new IRecipeType<T>() {
			@Override
			public String toString() {
				return id;
			}
		}), SERIALIZERS.register(id, serializer));
	}
}
