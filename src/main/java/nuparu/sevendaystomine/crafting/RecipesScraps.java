package nuparu.sevendaystomine.crafting;

import java.util.Iterator;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModRecipes;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.item.IScrapable;
import nuparu.sevendaystomine.util.ItemUtils;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.util.VanillaManager;
import nuparu.sevendaystomine.config.CommonConfig;

public class RecipesScraps implements ICraftingRecipe {

	private ItemStack resultItem = ItemStack.EMPTY;

	protected String resourceDomain;
	protected ResourceLocation resourceLocation;

	public RecipesScraps() {
		this(null);
	}

	public RecipesScraps(String resourceDomain) {
		this.resourceDomain = resourceDomain != null ? resourceDomain.toLowerCase() : resourceDomain;
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		if (matchesAnyOtherRecipe(inv, worldIn)) {
			return false;
		}

		this.resultItem = ItemStack.EMPTY;
		EnumMaterial mat = EnumMaterial.NONE;
		int weight = 0;
		for (int k1 = 0; k1 < inv.getContainerSize(); ++k1) {
			ItemStack itemstack = inv.getItem(k1);

			if (!itemstack.isEmpty()) {
				Item item = itemstack.getItem();
				if (item instanceof IScrapable) {

					IScrapable scrap = (IScrapable) item;

					if (!scrap.canBeScraped())
						return false;
					if (scrap.getItemMaterial() != mat && mat != EnumMaterial.NONE)
						return false;
					weight += scrap.getWeight();
					mat = scrap.getItemMaterial();
				} else if (item instanceof BlockItem && ((BlockItem) item).getBlock() != null
						&& ((BlockItem) item).getBlock() instanceof IScrapable) {

					IScrapable scrap = (IScrapable) ((BlockItem) item).getBlock();

					if (!scrap.canBeScraped())
						return false;
					if (scrap.getItemMaterial() != mat && mat != EnumMaterial.NONE)
						return false;
					weight += scrap.getWeight();
					mat = scrap.getItemMaterial();
				} else if (VanillaManager.getVanillaScrapable(item) != null) {
					VanillaManager.VanillaScrapableItem scrapable = VanillaManager.getVanillaScrapable(item);
					if (!scrapable.canBeScraped())
						return false;
					if (scrapable.getMaterial() != mat && mat != EnumMaterial.NONE)
						return false;
					weight += scrapable.getWeight();
					mat = scrapable.getMaterial();
				} else {
					return false;
				}
			}
		}
		if (weight == 0)
			return false;
		resultItem = new ItemStack(ItemUtils.INSTANCE.getScrapResult(mat),
				(int) Math.floor(weight * CommonConfig.scrapCoefficient.get()));
		if (!this.resultItem.isEmpty()) {

			return true;
		}
		return false;

	}

	public boolean matchesAnyOtherRecipe(CraftingInventory inv, World worldIn) {
		Iterator<IRecipe<?>> recipes = Utils.getServer().getRecipeManager().getRecipes().iterator();
		while (recipes.hasNext()) {
			IRecipe<?> recipe = recipes.next();
			if (!(recipe instanceof ICraftingRecipe))
				continue;
			ICraftingRecipe craftingRecipe = (ICraftingRecipe) recipe;
			if (!(craftingRecipe instanceof RecipesScraps)) {
				if (craftingRecipe.matches(inv, worldIn)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns an Item that is the result of this recipe
	 */
	@Nullable
	@Override
	public ItemStack assemble(CraftingInventory inv) {
		return this.resultItem.copy();
	}

	@Override
	public boolean canCraftInDimensions(int x, int y) {
		return x * y >= 1;
	}

	@Override
	public ItemStack getResultItem() {
		return this.resultItem.copy();
	}

	@Override
	public ResourceLocation getId() {

		return new ResourceLocation(SevenDaysToMine.MODID, "recipe_scrap");
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipes.SCRAP.get();
	}

	public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>
			implements IRecipeSerializer<RecipesScraps> {
		private static final ResourceLocation NAME = new ResourceLocation(SevenDaysToMine.MODID, "recipe_scrap");

		public RecipesScraps fromJson(ResourceLocation p_199425_1_, JsonObject json) {
			return new RecipesScraps();
		}

		public RecipesScraps fromNetwork(ResourceLocation p_199426_1_, PacketBuffer p_199426_2_) {

			return new RecipesScraps();
		}

		public void toNetwork(PacketBuffer p_199427_1_, RecipesScraps p_199427_2_) {
		}
	}

}
