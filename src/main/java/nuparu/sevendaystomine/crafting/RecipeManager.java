package nuparu.sevendaystomine.crafting;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import nuparu.sevendaystomine.util.Utils;

@SuppressWarnings("deprecation")
public class RecipeManager {

	public static RecipeManager INSTANCE;

	public RecipeManager() {
		INSTANCE = this;
	}

	public void init() {

	}

	public static void removeRecipe(Item item) {
		Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipes = ObfuscationReflectionHelper.getPrivateValue(
				net.minecraft.item.crafting.RecipeManager.class, Utils.getServer().getRecipeManager(), "recipes");
		Iterator<Map.Entry<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>>> itr = recipes.entrySet().iterator();

		while (itr.hasNext()) {
			Entry<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> entry = itr.next();

			Map<ResourceLocation, IRecipe<?>> map = entry.getValue();

			Iterator<Map.Entry<ResourceLocation, IRecipe<?>>> itrInside = entry.getValue().entrySet().iterator();

			while (itrInside.hasNext()) {
				Entry<ResourceLocation, IRecipe<?>> entryInside = itrInside.next();
				IRecipe<?> recipeOriginal = entryInside.getValue();
				if (!(recipeOriginal instanceof ICraftingRecipe))
					continue;

				ICraftingRecipe craftingRecipe = (ICraftingRecipe) recipeOriginal;
				ItemStack result = recipeOriginal.getResultItem();
				if (result != null && result.getItem() == item) {
					map.put(entryInside.getKey(), DummyRecipe.from(craftingRecipe));
				}

			}
		}

		/*
		 * for (IRecipe r : recipes) { ItemStack output = r.getResultItem(); if
		 * (output.getItem() == item) { recipeRegistry.remove(r.getRegistryName());
		 * recipeRegistry.register(DummyRecipe.from(r)); } }
		 */
	}

	public static void removeRecipe(ItemStack itemStack) {
		Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipes = ObfuscationReflectionHelper.getPrivateValue(
				net.minecraft.item.crafting.RecipeManager.class, Utils.getServer().getRecipeManager(), "recipes");
		Iterator<Map.Entry<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>>> itr = recipes.entrySet().iterator();

		while (itr.hasNext()) {
			Entry<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> entry = itr.next();

			Map<ResourceLocation, IRecipe<?>> map = entry.getValue();

			Iterator<Map.Entry<ResourceLocation, IRecipe<?>>> itrInside = entry.getValue().entrySet().iterator();

			while (itrInside.hasNext()) {
				Entry<ResourceLocation, IRecipe<?>> entryInside = itrInside.next();
				IRecipe<?> recipeOriginal = entryInside.getValue();
				if (!(recipeOriginal instanceof ICraftingRecipe))
					continue;

				ICraftingRecipe craftingRecipe = (ICraftingRecipe) recipeOriginal;
				ItemStack result = recipeOriginal.getResultItem();
				if (result != null && ItemStack.isSame(result,itemStack)) {
					map.put(entryInside.getKey(), DummyRecipe.from(craftingRecipe));
				}

			}
		}
	}

	public static void removeRecipe(Block block) {
		//removeRecipe(Item.byBlock(block));
	}

	public static void removeItem(Item item) {
		//removeRecipe(item);
	}

	public static void removeItem(Block block) {
		removeItem(Item.byBlock(block));
	}

	public static void removeSmelting(ItemStack resultStack, String modID) {
		Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipes = ObfuscationReflectionHelper.getPrivateValue(
				net.minecraft.item.crafting.RecipeManager.class, Utils.getServer().getRecipeManager(), "recipes");
		Iterator<Map.Entry<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>>> itr = recipes.entrySet().iterator();

		while (itr.hasNext()) {
			Entry<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> entry = itr.next();

			Map<ResourceLocation, IRecipe<?>> map = entry.getValue();

			Iterator<Map.Entry<ResourceLocation, IRecipe<?>>> itrInside = entry.getValue().entrySet().iterator();

			while (itrInside.hasNext()) {
				Entry<ResourceLocation, IRecipe<?>> entryInside = itrInside.next();
				IRecipe<?> recipeOriginal = entryInside.getValue();
				if (!(recipeOriginal instanceof AbstractCookingRecipe))
					continue;

				AbstractCookingRecipe craftingRecipe = (AbstractCookingRecipe) recipeOriginal;
				ItemStack result = recipeOriginal.getResultItem();
				if (result != null && ItemStack.isSame(result,resultStack)) {
					map.put(entryInside.getKey(), DummyFurnaceRecipe.from(craftingRecipe));
				}

			}
		}
	}
}
