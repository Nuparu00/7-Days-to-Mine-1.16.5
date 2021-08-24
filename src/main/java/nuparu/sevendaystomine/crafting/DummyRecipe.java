package nuparu.sevendaystomine.crafting;

import com.google.gson.JsonObject;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModRecipes;

public class DummyRecipe implements ICraftingRecipe {
	private final ItemStack output;
	private final ResourceLocation location;

	public DummyRecipe(ItemStack output) {
		this.output = output;
		this.location = null;
	}

	public DummyRecipe(ItemStack resultItem, ResourceLocation id) {

		this.output = resultItem;
		this.location = id;
	}

	public static IRecipe from(IRecipe other) {
		return new DummyRecipe(other.getResultItem(), other.getId());
	}

	@Override
	public boolean matches(CraftingInventory p_77569_1_, World p_77569_2_) {
		return false;
	}

	@Override
	public ItemStack assemble(CraftingInventory p_77572_1_) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
		return false;
	}

	@Override
	public ItemStack getResultItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public ResourceLocation getId() {
		return location;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipes.DUMMY_CRAFTING.get();
	}

	public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>
			implements IRecipeSerializer<DummyRecipe> {
		private static final ResourceLocation NAME = new ResourceLocation(SevenDaysToMine.MODID, "dummy_crafting");

		public DummyRecipe fromJson(ResourceLocation p_199425_1_, JsonObject json) {
			return new DummyRecipe(ItemStack.EMPTY);
		}

		public DummyRecipe fromNetwork(ResourceLocation p_199426_1_, PacketBuffer p_199426_2_) {

			return new DummyRecipe(p_199426_2_.readItem(),p_199426_2_.readResourceLocation());
		}

		public void toNetwork(PacketBuffer p_199427_1_, DummyRecipe recipe) {
			p_199427_1_.writeItem(recipe.getResultItem());
			p_199427_1_.writeResourceLocation(recipe.getId());
		}
	}

}