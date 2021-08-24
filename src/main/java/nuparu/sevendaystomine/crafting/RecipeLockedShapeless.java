package nuparu.sevendaystomine.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

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
import net.minecraft.item.crafting.ShapelessRecipe;
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
import nuparu.sevendaystomine.init.ModRecipes;
import nuparu.sevendaystomine.item.ItemRecipeBook;

@SuppressWarnings("deprecation")
public class RecipeLockedShapeless extends ShapelessRecipe {

	public String recipe;
	public boolean quality;
	
	public RecipeLockedShapeless(ResourceLocation p_i48161_1_, String p_i48161_2_, ItemStack p_i48161_3_,
			NonNullList<Ingredient> p_i48161_4_, String recipe, boolean quality) {
		super(p_i48161_1_, p_i48161_2_, p_i48161_3_, p_i48161_4_);
		this.recipe =recipe;
		this.quality = quality;
	}



	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		PlayerEntity player = null;
		Container c = ObfuscationReflectionHelper.getPrivateValue(CraftingInventory.class, inv, "field_70465_c");
		/*if (c instanceof nuparu.sevendaystomine.inventory.ContainerWorkbench) {
		nuparu.sevendaystomine.inventory.ContainerWorkbench container = (nuparu.sevendaystomine.inventory.ContainerWorkbench) c;
		player = container.player;
	} else*/  if (c instanceof WorkbenchContainer) {
			WorkbenchContainer container = (WorkbenchContainer) (c);
			CraftingResultSlot slot = (CraftingResultSlot) container.getSlot(0);
			player = (PlayerEntity) (ObfuscationReflectionHelper.getPrivateValue(CraftingResultSlot.class, slot,
					"field_75238_b"));
		} else if (c instanceof PlayerContainer) {
			PlayerContainer container = (PlayerContainer) (c);
			player = (PlayerEntity) (ObfuscationReflectionHelper.getPrivateValue(PlayerContainer.class, container,
					"field_82862_h"));
		}
		if (player == null)
			return false;

		IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
		return (!CommonConfig.recipeBooksRequired.get() || iep.hasRecipe(recipe)) && super.matches(inv, worldIn);

	}
	
	@Override
	public ItemStack assemble(CraftingInventory inv) {
		ItemStack stack = super.assemble(inv);
		if (stack != null && !stack.isEmpty()) {
			if (stack.getItem() instanceof ItemRecipeBook
					&& ((ItemRecipeBook) stack.getItem()).getRecipe().equals(recipe)) {
				((ItemRecipeBook) stack.getItem()).setRead(stack, true);
			}

			if (!quality)
				return stack;

			Container c = ObfuscationReflectionHelper.getPrivateValue(CraftingInventory.class, inv, "field_70465_c");

			PlayerEntity player = null;
			/*if (c instanceof nuparu.sevendaystomine.inventory.ContainerWorkbench) {
			nuparu.sevendaystomine.inventory.ContainerWorkbench container = (nuparu.sevendaystomine.inventory.ContainerWorkbench) c;
			player = container.player;
		} else*/  if (c instanceof WorkbenchContainer) {
				WorkbenchContainer container = (WorkbenchContainer) (c);
				CraftingResultSlot slot = (CraftingResultSlot) container.getSlot(0);
				player = (PlayerEntity) (ObfuscationReflectionHelper.getPrivateValue(CraftingResultSlot.class, slot,
						"field_75238_b"));
			} else if (c instanceof PlayerContainer) {
				PlayerContainer container = (PlayerContainer) (c);
				player = (PlayerEntity) (ObfuscationReflectionHelper.getPrivateValue(PlayerContainer.class, container,
						"field_82862_h"));
			}
			if (player != null) {
				if (stack.getOrCreateTag() == null) {
					stack.setTag(new CompoundNBT());
				}
				stack.getOrCreateTag().putInt("Quality", (int) Math
						.min(Math.max(Math.floor(player.totalExperience / CommonConfig.xpPerQuality.get()), 1), 600));
			}
		}
		return stack;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipes.LOCKED_SHAPELESS.get();
	}

	public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>
			implements IRecipeSerializer<RecipeLockedShapeless> {
		private static final ResourceLocation NAME = new ResourceLocation(SevenDaysToMine.MODID, "locked_shapeless");

		public RecipeLockedShapeless fromJson(ResourceLocation p_199425_1_, JsonObject json) {

			int MAX_HEIGHT = ObfuscationReflectionHelper.getPrivateValue(RecipeQualityShaped.class, null, "MAX_HEIGHT");
			int MAX_WIDTH = ObfuscationReflectionHelper.getPrivateValue(RecipeQualityShaped.class, null, "MAX_WIDTH");

			String s = JSONUtils.getAsString(json, "group", "");
			NonNullList<Ingredient> nonnulllist = itemsFromJson(JSONUtils.getAsJsonArray(json, "ingredients"));
			if (nonnulllist.isEmpty()) {
				throw new JsonParseException("No ingredients for shapeless recipe");
			} else if (nonnulllist.size() > MAX_WIDTH * MAX_HEIGHT) {
				throw new JsonParseException("Too many ingredients for shapeless recipe the max is "
						+ (MAX_WIDTH * MAX_HEIGHT));
			} else {
				ItemStack itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
				
				if (!json.has("recipe"))
					throw new JsonSyntaxException("Property recipe not specified");
				String recipe = json.get("recipe").getAsString();

				boolean quality = false;

				if (json.has("quality")) {
					quality = json.get("quality").getAsBoolean();
				}
				
				return new RecipeLockedShapeless(p_199425_1_, s, itemstack, nonnulllist,recipe,quality);
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

		public RecipeLockedShapeless fromNetwork(ResourceLocation p_199426_1_, PacketBuffer p_199426_2_) {
			String s = p_199426_2_.readUtf(32767);
			int i = p_199426_2_.readVarInt();
			String recipe = p_199426_2_.readUtf(32767);
			boolean quality = p_199426_2_.readBoolean();
			NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

			for (int j = 0; j < nonnulllist.size(); ++j) {
				nonnulllist.set(j, Ingredient.fromNetwork(p_199426_2_));
			}

			ItemStack itemstack = p_199426_2_.readItem();
			return new RecipeLockedShapeless(p_199426_1_, s, itemstack, nonnulllist,recipe,quality);
		}

		public void toNetwork(PacketBuffer p_199427_1_, RecipeLockedShapeless p_199427_2_) {
			p_199427_1_.writeUtf(p_199427_2_.getGroup());
			p_199427_1_.writeVarInt(p_199427_2_.getIngredients().size());
			p_199427_1_.writeUtf(p_199427_2_.recipe);
			p_199427_1_.writeBoolean(p_199427_2_.quality);

			for (Ingredient ingredient : p_199427_2_.getIngredients()) {
				ingredient.toNetwork(p_199427_1_);
			}

			p_199427_1_.writeItem(p_199427_2_.getResultItem());
		}
	}

}
