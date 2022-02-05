package nuparu.sevendaystomine.crafting;

import java.util.Map;

import com.google.gson.JsonObject;
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
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.init.ModRecipeSerializers;
import nuparu.sevendaystomine.item.ItemRecipeBook;

public class RecipeLockedShaped extends ShapedRecipe implements IRecipeLocked{

	public String recipe;
	boolean quality;

	public RecipeLockedShaped(ResourceLocation p_i48162_1_, String p_i48162_2_, int p_i48162_3_, int p_i48162_4_,
			NonNullList<Ingredient> p_i48162_5_, ItemStack p_i48162_6_, String recipe, boolean quality) {
		super(p_i48162_1_, p_i48162_2_, p_i48162_3_, p_i48162_4_, p_i48162_5_, p_i48162_6_);
		this.recipe = recipe;
		this.quality = quality;
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
	public boolean matches(CraftingInventory inv, World worldIn) {
		PlayerEntity player = null;
		Container c = ObfuscationReflectionHelper.getPrivateValue(CraftingInventory.class, inv, "field_70465_c");
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
		if (player == null)
			return false;

		IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
		return (!ServerConfig.recipeBooksRequired.get() || iep.hasRecipe(recipe)) && super.matches(inv, worldIn);

	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {

		NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(inv.getContainerSize(), ItemStack.EMPTY);
		// WATER BUCKETS CAUSE ISSUES DUE TO RETURNING THE EMPTY BUCKET (like setting
		// stacksize to 128, for example), THERE DOES NOT SEEM TO BE ANY WAY TO DISABLE
		// THIS NATURALLY
		if (getResultItem().isEmpty() || this.getResultItem().getItem() != ModItems.CONCRETE_MIX.get()) {
			for (int i = 0; i < nonnulllist.size(); ++i) {
				ItemStack itemstack = inv.getItem(i);

				nonnulllist.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
			}
		}

		return nonnulllist;
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.LOCKED_SHAPED.get();
	}

	@Override
	public String getRecipe() {
		return this.recipe;
	}

	public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>
			implements IRecipeSerializer<RecipeLockedShaped> {
		private static final ResourceLocation NAME = new ResourceLocation(SevenDaysToMine.MODID, "locked_shaped");

		public RecipeLockedShaped fromJson(ResourceLocation p_199425_1_, JsonObject json) {
			String s = JSONUtils.getAsString(json, "group", "");
			Map<String, Ingredient> map = RecipeQualityShaped
					.keyFromJson(JSONUtils.getAsJsonObject(json, "key"));
			String[] astring = RecipeQualityShaped
					.shrink(RecipeQualityShaped.patternFromJson(JSONUtils.getAsJsonArray(json, "pattern")));
			int i = astring[0].length();
			int j = astring.length;
			NonNullList<Ingredient> nonnulllist = RecipeQualityShaped.dissolvePattern(astring, map, i, j);
			ItemStack itemstack = RecipeQualityShaped.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));

			if (!json.has("recipe"))
				throw new JsonSyntaxException("Property recipe not specified");
			String recipe = json.get("recipe").getAsString();

			boolean quality = false;

			if (json.has("quality")) {
				quality = json.get("quality").getAsBoolean();
			}
			
			return new RecipeLockedShaped(p_199425_1_, s, i, j, nonnulllist, itemstack,recipe,quality);
		}

		public RecipeLockedShaped fromNetwork(ResourceLocation p_199426_1_, PacketBuffer p_199426_2_) {
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
			return new RecipeLockedShaped(p_199426_1_, s, i, j, nonnulllist, itemstack,recipe,quality);
		}

		public void toNetwork(PacketBuffer p_199427_1_, RecipeLockedShaped p_199427_2_) {
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
