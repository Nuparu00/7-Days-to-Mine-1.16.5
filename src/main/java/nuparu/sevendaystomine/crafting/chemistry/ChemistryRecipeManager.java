package nuparu.sevendaystomine.crafting.chemistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.item.Items;
import net.minecraft.potion.Effects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import nuparu.sevendaystomine.init.ModItems;

public class ChemistryRecipeManager {
	private static ChemistryRecipeManager INSTANCE;

	private ArrayList<IChemistryRecipe> recipes = new ArrayList<IChemistryRecipe>();

	public ChemistryRecipeManager() {
		INSTANCE = this;
		addRecipes();
	}

	public static ChemistryRecipeManager getInstance() {
		return INSTANCE;
	}

	public ArrayList<IChemistryRecipe> getRecipes() {
		return this.recipes;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addRecipes() {
		addRecipe(new ChemistryRecipeShapeless(new ItemStack(ModItems.BOTTLED_WATER.get()),
				new ArrayList(Arrays.asList(new ItemStack(ModItems.BOTTLED_MURKY_WATER.get())))));
		addRecipe(new ChemistryRecipeShapeless(new ItemStack(ModItems.BOTTLED_BEER.get()),
				new ArrayList(Arrays.asList(new ItemStack(ModItems.BOTTLED_WATER.get()),
						new ItemStack(ModItems.CORN.get()), new ItemStack(Items.SUGAR)))));
		addRecipe(new ChemistryRecipeShapeless(new ItemStack(ModItems.BOTTLED_BEER.get()),
				new ArrayList(Arrays.asList(new ItemStack(ModItems.BOTTLED_WATER.get()), new ItemStack(Items.WHEAT),
						new ItemStack(Items.SUGAR)))));
		addRecipe(new ChemistryRecipeShapeless(new ItemStack(Items.GUNPOWDER),
				new ArrayList(Arrays.asList(new ItemStack(Items.COAL), new ItemStack(ModItems.POTASSIUM.get(), 2)))));
		addRecipe(new ChemistryRecipeShapeless(new ItemStack(ModItems.SALT.get()),
				new ArrayList(Arrays.asList(new ItemStack(ModItems.BOTTLED_WATER.get()),
						new ItemStack(ModItems.NATRIUM_TANK.get()), new ItemStack(ModItems.CHLORINE_TANK.get())))));

		ItemStack lingering = new ItemStack(Items.LINGERING_POTION);
		List<EffectInstance> list = Lists.<EffectInstance>newArrayList();
		list.add(new EffectInstance(Effects.POISON, 100, 0));
		PotionUtils.setCustomEffects(lingering, list);
		addRecipe(new ChemistryRecipeShapeless(lingering,
				new ArrayList(Arrays.asList(new ItemStack(ModItems.BANEBERRY.get()),
						new ItemStack(ModItems.BANEBERRY.get()), new ItemStack(ModItems.BOTTLED_BEER.get()),
						new ItemStack(ModItems.BOTTLED_BEER.get())))));
	}

	public void addRecipe(IChemistryRecipe recipe) {
		recipes.add(recipe);
	}
}
