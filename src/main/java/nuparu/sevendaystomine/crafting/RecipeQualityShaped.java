package nuparu.sevendaystomine.crafting;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModRecipeSerializers;

@SuppressWarnings("deprecation")
public class RecipeQualityShaped extends ShapedRecipe {

	public RecipeQualityShaped(ResourceLocation p_i48162_1_, String p_i48162_2_, int p_i48162_3_, int p_i48162_4_,
			NonNullList<Ingredient> p_i48162_5_, ItemStack p_i48162_6_) {
		super(p_i48162_1_, p_i48162_2_, p_i48162_3_, p_i48162_4_, p_i48162_5_, p_i48162_6_);
	}

	public ItemStack assemble(CraftingInventory inv) {
		ItemStack stack = super.assemble(inv);
		if (stack != null) {
			Container c = ObfuscationReflectionHelper.getPrivateValue(CraftingInventory.class, inv, "field_70465_c");
			PlayerEntity player = null;
			if (c instanceof nuparu.sevendaystomine.inventory.block.ContainerWorkbench) {
				nuparu.sevendaystomine.inventory.block.ContainerWorkbench container = (nuparu.sevendaystomine.inventory.block.ContainerWorkbench) c;
				player = container.player;
			} else if (c instanceof WorkbenchContainer) {
				WorkbenchContainer container = (WorkbenchContainer) (c);
				CraftingResultSlot slot = (CraftingResultSlot) container.getSlot(0);
				player = ObfuscationReflectionHelper.getPrivateValue(CraftingResultSlot.class, slot,
						"field_75238_b");
			} else if (c instanceof PlayerContainer) {
				PlayerContainer container = (PlayerContainer) (c);
				player = ObfuscationReflectionHelper.getPrivateValue(PlayerContainer.class, container,
						"field_82862_h");
			}
			if (player != null) {
				stack.getOrCreateTag().putInt("Quality", (int) Math
						.min(Math.max(Math.floor(player.totalExperience / ServerConfig.xpPerQuality.get()), 1), 600));
			}
		}
		return stack;

	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.QUALITY_SHAPED.get();
	}

	public static NonNullList<Ingredient> dissolvePattern(String[] p_192402_0_, Map<String, Ingredient> p_192402_1_,
			int p_192402_2_, int p_192402_3_) {
		NonNullList<Ingredient> nonnulllist = NonNullList.withSize(p_192402_2_ * p_192402_3_, Ingredient.EMPTY);
		Set<String> set = Sets.newHashSet(p_192402_1_.keySet());
		set.remove(" ");

		for (int i = 0; i < p_192402_0_.length; ++i) {
			for (int j = 0; j < p_192402_0_[i].length(); ++j) {
				String s = p_192402_0_[i].substring(j, j + 1);
				Ingredient ingredient = p_192402_1_.get(s);
				if (ingredient == null) {
					throw new JsonSyntaxException(
							"Pattern references symbol '" + s + "' but it's not defined in the key");
				}

				set.remove(s);
				nonnulllist.set(j + p_192402_2_ * i, ingredient);
			}
		}

		if (!set.isEmpty()) {
			throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
		} else {
			return nonnulllist;
		}
	}

	@VisibleForTesting
	public static String[] shrink(String... p_194134_0_) {
		int i = Integer.MAX_VALUE;
		int j = 0;
		int k = 0;
		int l = 0;

		for (int i1 = 0; i1 < p_194134_0_.length; ++i1) {
			String s = p_194134_0_[i1];
			i = Math.min(i, firstNonSpace(s));
			int j1 = lastNonSpace(s);
			j = Math.max(j, j1);
			if (j1 < 0) {
				if (k == i1) {
					++k;
				}

				++l;
			} else {
				l = 0;
			}
		}

		if (p_194134_0_.length == l) {
			return new String[0];
		} else {
			String[] astring = new String[p_194134_0_.length - l - k];

			for (int k1 = 0; k1 < astring.length; ++k1) {
				astring[k1] = p_194134_0_[k1 + k].substring(i, j + 1);
			}

			return astring;
		}
	}

	public static int firstNonSpace(String p_194135_0_) {
		int i;
		for (i = 0; i < p_194135_0_.length() && p_194135_0_.charAt(i) == ' '; ++i) {
		}

		return i;
	}

	public static int lastNonSpace(String p_194136_0_) {
		int i;
		for (i = p_194136_0_.length() - 1; i >= 0 && p_194136_0_.charAt(i) == ' '; --i) {
		}

		return i;
	}

	static Field f_MAX_HEIGHT;
	static Field f_MAX_WIDTH;

	public static String[] patternFromJson(JsonArray p_192407_0_) {
		String[] astring = new String[p_192407_0_.size()];

		if(f_MAX_HEIGHT == null){
			try {
				f_MAX_HEIGHT = ShapedRecipe.class.getDeclaredField("MAX_HEIGHT");
				f_MAX_HEIGHT.setAccessible(true);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}

		if(f_MAX_WIDTH == null){
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

		if (astring.length > MAX_HEIGHT) {
			throw new JsonSyntaxException("Invalid pattern: too many rows, " + MAX_HEIGHT + " is maximum");
		} else if (astring.length == 0) {
			throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
		} else {
			for (int i = 0; i < astring.length; ++i) {
				String s = JSONUtils.convertToString(p_192407_0_.get(i), "pattern[" + i + "]");
				if (s.length() > MAX_WIDTH) {
					throw new JsonSyntaxException("Invalid pattern: too many columns, " + MAX_WIDTH + " is maximum");
				}

				if (i > 0 && astring[0].length() != s.length()) {
					throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
				}

				astring[i] = s;
			}

			return astring;
		}
	}

	public static Map<String, Ingredient> keyFromJson(JsonObject p_192408_0_) {
		Map<String, Ingredient> map = Maps.newHashMap();

		for (Entry<String, JsonElement> entry : p_192408_0_.entrySet()) {
			if (entry.getKey().length() != 1) {
				throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey()
						+ "' is an invalid symbol (must be 1 character only).");
			}

			if (" ".equals(entry.getKey())) {
				throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
			}

			map.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
		}

		map.put(" ", Ingredient.EMPTY);
		return map;
	}

	public static ItemStack itemFromJson(JsonObject p_199798_0_) {
		String s = JSONUtils.getAsString(p_199798_0_, "item");
		Item item = Registry.ITEM.getOptional(new ResourceLocation(s)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + s + "'"));
		if (p_199798_0_.has("data")) {
			throw new JsonParseException("Disallowed data tag found");
		} else {
			int i = JSONUtils.getAsInt(p_199798_0_, "count", 1);
			return net.minecraftforge.common.crafting.CraftingHelper.getItemStack(p_199798_0_, true);
		}
	}

	public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>
			implements IRecipeSerializer<RecipeQualityShaped> {
		private static final ResourceLocation NAME = new ResourceLocation(SevenDaysToMine.MODID, "quality_shaped");

		public RecipeQualityShaped fromJson(ResourceLocation p_199425_1_, JsonObject p_199425_2_) {
			String s = JSONUtils.getAsString(p_199425_2_, "group", "");
			Map<String, Ingredient> map = RecipeQualityShaped
					.keyFromJson(JSONUtils.getAsJsonObject(p_199425_2_, "key"));
			String[] astring = RecipeQualityShaped
					.shrink(RecipeQualityShaped.patternFromJson(JSONUtils.getAsJsonArray(p_199425_2_, "pattern")));
			int i = astring[0].length();
			int j = astring.length;
			NonNullList<Ingredient> nonnulllist = RecipeQualityShaped.dissolvePattern(astring, map, i, j);
			ItemStack itemstack = RecipeQualityShaped.itemFromJson(JSONUtils.getAsJsonObject(p_199425_2_, "result"));
			return new RecipeQualityShaped(p_199425_1_, s, i, j, nonnulllist, itemstack);
		}

		public RecipeQualityShaped fromNetwork(ResourceLocation p_199426_1_, PacketBuffer p_199426_2_) {
			int i = p_199426_2_.readVarInt();
			int j = p_199426_2_.readVarInt();
			String s = p_199426_2_.readUtf(32767);
			NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);

			for (int k = 0; k < nonnulllist.size(); ++k) {
				nonnulllist.set(k, Ingredient.fromNetwork(p_199426_2_));
			}

			ItemStack itemstack = p_199426_2_.readItem();
			return new RecipeQualityShaped(p_199426_1_, s, i, j, nonnulllist, itemstack);
		}

		public void toNetwork(PacketBuffer p_199427_1_, RecipeQualityShaped p_199427_2_) {
			p_199427_1_.writeVarInt(p_199427_2_.getWidth());
			p_199427_1_.writeVarInt(p_199427_2_.getHeight());
			p_199427_1_.writeUtf(p_199427_2_.getGroup());

			for (Ingredient ingredient : p_199427_2_.getIngredients()) {
				ingredient.toNetwork(p_199427_1_);
			}

			p_199427_1_.writeItem(p_199427_2_.getResultItem());
		}
	}

}
