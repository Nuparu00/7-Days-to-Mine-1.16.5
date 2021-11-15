package nuparu.sevendaystomine.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModRecipeSerializers;
import nuparu.sevendaystomine.item.IQuality;

import java.lang.reflect.Field;

public class RecipeGunShapeless extends RecipeLockedShapeless {

	public RecipeGunShapeless(ResourceLocation p_i48161_1_, String p_i48161_2_, ItemStack p_i48161_3_,
			NonNullList<Ingredient> p_i48161_4_, String recipe) {
		super(p_i48161_1_, p_i48161_2_, p_i48161_3_, p_i48161_4_, recipe, false);
	}

	public ItemStack output = ItemStack.EMPTY;

	@Override
	public ItemStack assemble(CraftingInventory inv) {
		ItemStack stack = super.assemble(inv);

		int count = 0;
		int qualitySum = 0;

		for (int k = 0; k < inv.getContainerSize(); ++k) {
			ItemStack itemstack = inv.getItem(k);
			if (!itemstack.isEmpty() && itemstack.getItem() instanceof IQuality) {
				count++;
				qualitySum += ((IQuality) itemstack.getItem()).getQuality(itemstack);
			}

		}
		((IQuality) stack.getItem()).setQuality(stack, (int) ((float) qualitySum / count));

		output = stack;

		return stack;

	}

	@Override
	public ItemStack getResultItem() {
		return output.isEmpty() ? super.getResultItem() : output;
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.GUN_SHAPELESS.get();
	}


	public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>
			implements IRecipeSerializer<RecipeGunShapeless> {
		private static final ResourceLocation NAME = new ResourceLocation(SevenDaysToMine.MODID, "gun_shapeless");

		static Field f_MAX_HEIGHT;
		static Field f_MAX_WIDTH;

		public RecipeGunShapeless fromJson(ResourceLocation p_199425_1_, JsonObject json) {

			if (f_MAX_HEIGHT == null) {
				try {
					f_MAX_HEIGHT = ShapedRecipe.class.getDeclaredField("MAX_HEIGHT");
					f_MAX_HEIGHT.setAccessible(true);
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
			}

			if (f_MAX_WIDTH == null) {
				try {
					f_MAX_WIDTH = ShapedRecipe.class.getDeclaredField("MAX_WIDTH");
					f_MAX_WIDTH.setAccessible(true);
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
			}

			int MAX_HEIGHT = 0;
			try {
				MAX_HEIGHT = (int) f_MAX_HEIGHT.get(null);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			int MAX_WIDTH = 0;
			try {
				MAX_WIDTH = (int) f_MAX_WIDTH.get(null);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			String s = JSONUtils.getAsString(json, "group", "");
			NonNullList<Ingredient> nonnulllist = itemsFromJson(JSONUtils.getAsJsonArray(json, "ingredients"));
			if (nonnulllist.isEmpty()) {
				throw new JsonParseException("No ingredients for shapeless recipe");
			} else if (nonnulllist.size() > MAX_WIDTH * MAX_HEIGHT) {
				throw new JsonParseException(
						"Too many ingredients for shapeless recipe the max is " + (MAX_WIDTH * MAX_HEIGHT));
			} else {
				ItemStack itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));

				if (!json.has("recipe"))
					throw new JsonSyntaxException("Property recipe not specified");
				String recipe = json.get("recipe").getAsString();

				return new RecipeGunShapeless(p_199425_1_, s, itemstack, nonnulllist, recipe);
			}
		}

		private static NonNullList<Ingredient> itemsFromJson(JsonArray p_199568_0_) {
			NonNullList<Ingredient> nonnulllist = NonNullList.create();

			for (int i = 0; i < p_199568_0_.size(); ++i) {
				Ingredient ingredient = Ingredient.fromJson(p_199568_0_.get(i));
				if (!ingredient.isEmpty()) {
					nonnulllist.add(ingredient);
				}
			}

			return nonnulllist;
		}

		public RecipeGunShapeless fromNetwork(ResourceLocation p_199426_1_, PacketBuffer p_199426_2_) {
			String s = p_199426_2_.readUtf(32767);
			int i = p_199426_2_.readVarInt();
			String recipe = p_199426_2_.readUtf(32767);
			NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

			for (int j = 0; j < nonnulllist.size(); ++j) {
				nonnulllist.set(j, Ingredient.fromNetwork(p_199426_2_));
			}

			ItemStack itemstack = p_199426_2_.readItem();
			return new RecipeGunShapeless(p_199426_1_, s, itemstack, nonnulllist, recipe);
		}

		public void toNetwork(PacketBuffer p_199427_1_, RecipeGunShapeless p_199427_2_) {
			p_199427_1_.writeUtf(p_199427_2_.getGroup());
			p_199427_1_.writeVarInt(p_199427_2_.getIngredients().size());
			p_199427_1_.writeUtf(p_199427_2_.recipe);

			for (Ingredient ingredient : p_199427_2_.getIngredients()) {
				ingredient.toNetwork(p_199427_1_);
			}

			p_199427_1_.writeItem(p_199427_2_.getResultItem());
		}
	}

}
