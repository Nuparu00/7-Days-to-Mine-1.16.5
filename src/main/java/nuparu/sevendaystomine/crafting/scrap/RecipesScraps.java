package nuparu.sevendaystomine.crafting.scrap;

import java.util.Iterator;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModRecipeSerializers;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.util.ItemUtils;
import nuparu.sevendaystomine.util.Utils;

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
				 if (ScrapDataManager.instance.hasEntry(item)) {
					ScrapDataManager.ScrapEntry entry = ScrapDataManager.instance.getEntry(item);
					if (!entry.canBeScrapped)
						return false;
					if (entry.material != mat && mat != EnumMaterial.NONE)
						return false;
					weight += entry.weight;
					mat = entry.material;
				} else {
					return false;
				}
			}
		}
		if (weight == 0)
			return false;
		resultItem = new ItemStack(ItemUtils.INSTANCE.getScrapResult(mat),
				(int) Math.floor(weight * ServerConfig.scrapCoefficient.get()));
		if (!this.resultItem.isEmpty()) {

			return true;
		}
		return false;

	}

	public boolean matchesAnyOtherRecipe(CraftingInventory inv, World worldIn) {
		if(Utils.getServer() == null) return false;
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
		return ModRecipeSerializers.SCRAP_SERIALIZER.get();
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
