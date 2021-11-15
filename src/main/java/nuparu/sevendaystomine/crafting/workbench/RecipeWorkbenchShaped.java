package nuparu.sevendaystomine.crafting.workbench;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.crafting.IRecipeLocked;
import nuparu.sevendaystomine.crafting.RecipeQualityShaped;
import nuparu.sevendaystomine.init.ModRecipeSerializers;
import nuparu.sevendaystomine.item.ItemRecipeBook;

public class RecipeWorkbenchShaped extends ShapedRecipe implements IRecipeLocked{

	public static final List<RecipeWorkbenchShaped> RECIPES = new ArrayList<RecipeWorkbenchShaped>();
	
	String recipe;
	boolean quality;

	public RecipeWorkbenchShaped(ResourceLocation res, String group, int width, int height, NonNullList<Ingredient> ingredients,
			ItemStack result, String recipe, boolean quality) {
		super(res, group, width, height, ingredients, result);
		this.recipe = recipe;
		this.quality = quality;
		RECIPES.add(this);
	}

	@Override
	public ItemStack assemble(CraftingInventory inv) {
		ItemStack stack = super.assemble(inv);
		if (stack != null && !stack.isEmpty() && inv != null) {
			if (stack.getItem() instanceof ItemRecipeBook
					&& ((ItemRecipeBook) stack.getItem()).getRecipe().equals(recipe)) {
				((ItemRecipeBook) stack.getItem()).setRead(stack, true);
			}
			if (!quality)
				return stack;

			Container c = ObfuscationReflectionHelper.getPrivateValue(CraftingInventory.class, inv, "field_70465_c");

			PlayerEntity player = null;
			if (c instanceof nuparu.sevendaystomine.inventory.block.ContainerWorkbench) {
			nuparu.sevendaystomine.inventory.block.ContainerWorkbench container = (nuparu.sevendaystomine.inventory.block.ContainerWorkbench) c;
			player = container.player;
		} else  if (c instanceof WorkbenchContainer) {
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
						.min(Math.max(Math.floor(player.totalExperience / CommonConfig.xpPerQuality.get()), 1), 600));
			}
		}
		return stack;

	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		if (recipe == null || recipe.isEmpty())
			return super.matches(inv, worldIn);
		PlayerEntity player = null;
		Container c = ObfuscationReflectionHelper.getPrivateValue(CraftingInventory.class, inv, "field_70465_c");

		if (c instanceof nuparu.sevendaystomine.inventory.block.ContainerWorkbench) {
		nuparu.sevendaystomine.inventory.block.ContainerWorkbench container = (nuparu.sevendaystomine.inventory.block.ContainerWorkbench) c;
		player = container.player;
	} else  if (c instanceof WorkbenchContainer) {
			WorkbenchContainer container = (WorkbenchContainer) (c);
			CraftingResultSlot slot = (CraftingResultSlot) container.getSlot(0);
			player = ObfuscationReflectionHelper.getPrivateValue(CraftingResultSlot.class, slot,
					"field_75238_b");
		} else if (c instanceof PlayerContainer) {
			PlayerContainer container = (PlayerContainer) (c);
			player = ObfuscationReflectionHelper.getPrivateValue(PlayerContainer.class, container,
					"field_82862_h");
		}
		if (player == null)
			return false;

		IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
		return (!CommonConfig.recipeBooksRequired.get() || iep.hasRecipe(recipe)) && super.matches(inv, worldIn);

	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.WORKBENCH_SHAPED.get();
	}

	@Override
	public String getRecipe() {
		return this.recipe;
	}

	public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>
			implements IRecipeSerializer<RecipeWorkbenchShaped> {
		private static final ResourceLocation NAME = new ResourceLocation(SevenDaysToMine.MODID, "workbench_shaped");

		public RecipeWorkbenchShaped fromJson(ResourceLocation p_199425_1_, JsonObject json) {
			ShapedRecipe.setCraftingSize(5,5);
			String s = JSONUtils.getAsString(json, "group", "");
			Map<String, Ingredient> map = RecipeQualityShaped
					.keyFromJson(JSONUtils.getAsJsonObject(json, "key"));
			String[] astring = RecipeQualityShaped
					.shrink(RecipeQualityShaped.patternFromJson(JSONUtils.getAsJsonArray(json, "pattern")));
			int i = astring[0].length();
			int j = astring.length;
			NonNullList<Ingredient> nonnulllist = RecipeQualityShaped.dissolvePattern(astring, map, i, j);
			ItemStack itemstack = RecipeQualityShaped.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));

			String recipe = "";
			if (json.has("recipe")) {
				recipe = json.get("recipe").getAsString();
			}
			boolean quality = false;

			if (json.has("quality")) {
				quality = json.get("quality").getAsBoolean();
			}
			
			return new RecipeWorkbenchShaped(p_199425_1_, s, i, j, nonnulllist, itemstack,recipe,quality);
		}

		public RecipeWorkbenchShaped fromNetwork(ResourceLocation p_199426_1_, PacketBuffer p_199426_2_) {
			int i = p_199426_2_.readVarInt();
			int j = p_199426_2_.readVarInt();
			String s = p_199426_2_.readUtf(32767);
			String recipe = p_199426_2_.readUtf(32767);
			boolean quality = p_199426_2_.readBoolean();
			NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);

			for (int k = 0; k < nonnulllist.size(); ++k) {
				nonnulllist.set(k, Ingredient.fromNetwork(p_199426_2_));
			}

			ItemStack itemstack = p_199426_2_.readItem();
			return new RecipeWorkbenchShaped(p_199426_1_, s, i, j, nonnulllist, itemstack,recipe,quality);
		}

		public void toNetwork(PacketBuffer p_199427_1_, RecipeWorkbenchShaped p_199427_2_) {
			p_199427_1_.writeVarInt(p_199427_2_.getWidth());
			p_199427_1_.writeVarInt(p_199427_2_.getHeight());
			p_199427_1_.writeUtf(p_199427_2_.getGroup());
			p_199427_1_.writeUtf(p_199427_2_.recipe);
			p_199427_1_.writeBoolean(p_199427_2_.quality);

			for (Ingredient ingredient : p_199427_2_.getIngredients()) {
				ingredient.toNetwork(p_199427_1_);
			}

			p_199427_1_.writeItem(p_199427_2_.getResultItem());
		}
	}
}
