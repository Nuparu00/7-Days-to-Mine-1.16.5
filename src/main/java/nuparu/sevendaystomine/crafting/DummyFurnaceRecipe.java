package nuparu.sevendaystomine.crafting;

import com.google.gson.JsonObject;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;

public class DummyFurnaceRecipe extends AbstractCookingRecipe {

	private final ResourceLocation location;

	public DummyFurnaceRecipe(ResourceLocation location, ItemStack result) {
		super(IRecipeType.SMELTING, location, "", null, result, 0, 0);
		this.location = location;
	}
	
	public static IRecipe from(IRecipe other)
    {
        return new DummyFurnaceRecipe(other.getId(),other.getResultItem());
    }

	public ItemStack getToastSymbol() {
		return new ItemStack(Blocks.FURNACE);
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return null;
	}

	public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>
			implements IRecipeSerializer<DummyFurnaceRecipe> {
		private static final ResourceLocation NAME = new ResourceLocation(SevenDaysToMine.MODID, "dummy_crafting");

		public DummyFurnaceRecipe fromJson(ResourceLocation p_199425_1_, JsonObject json) {
			return new DummyFurnaceRecipe(null,ItemStack.EMPTY);
		}

		public DummyFurnaceRecipe fromNetwork(ResourceLocation p_199426_1_, PacketBuffer p_199426_2_) {

			return new DummyFurnaceRecipe(p_199426_2_.readResourceLocation(),p_199426_2_.readItem());
		}

		public void toNetwork(PacketBuffer p_199427_1_, DummyFurnaceRecipe recipe) {
			p_199427_1_.writeItem(recipe.getResultItem());
			p_199427_1_.writeResourceLocation(recipe.getId());
		}
	}
}