package nuparu.sevendaystomine.crafting.grill;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.registries.ForgeRegistryEntry;
import nuparu.sevendaystomine.init.ModRecipeSerializers;
import nuparu.sevendaystomine.tileentity.TileEntityGrill;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GrillRecipeShapeless implements IGrillRecipe<TileEntityGrill> {
    private final ResourceLocation id;
    private final String group;
    private final ItemStack result;
    private final NonNullList<Ingredient> ingredients;
    private final boolean isSimple;
    protected final float experience;
    protected final int cookingTime;

    public GrillRecipeShapeless(ResourceLocation resourceLocation, String group, ItemStack result, NonNullList<Ingredient> ingredients, float experience, int cookingTime) {
        this.id = resourceLocation;
        this.group = group;
        this.result = result;
        this.ingredients = ingredients;
        this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
        this.experience = experience;
        this.cookingTime = cookingTime;
    }

    @Override
    public boolean matches(TileEntityGrill grillInventory, World world) {
        RecipeItemHelper recipeitemhelper = new RecipeItemHelper();
        List<ItemStack> inputs = new ArrayList();
        int i = 0;

        for(int j = 0; j < grillInventory.getContainerSize(); ++j) {
            ItemStack itemstack = grillInventory.getItem(j);
            if (!itemstack.isEmpty()) {
                ++i;
                if (this.isSimple) {
                    recipeitemhelper.accountStack(itemstack, 1);
                } else {
                    inputs.add(itemstack);
                }
            }
        }

        boolean var10000;
        label43: {
            if (i == this.ingredients.size()) {
                if (this.isSimple) {
                    if (recipeitemhelper.canCraft(this, null)) {
                        break label43;
                    }
                } else if (RecipeMatcher.findMatches(inputs, this.ingredients) != null) {
                    break label43;
                }
            }

            var10000 = false;
            return var10000;
        }

        var10000 = true;
        return var10000;
    }

    @Override
    public ItemStack assemble(TileEntityGrill grillInventory) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return i * i1 >= this.ingredients.size();
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public ItemStack getResultItem() {
        return this.result.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.GRILL.getB().get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipeSerializers.GRILL.getA();
    }

    @Override
    public float getExperience() {
        return this.experience;
    }

    @Override
    public int getCookingTime() {
        return this.cookingTime;
    }

    public static class Factory extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<GrillRecipeShapeless> {
        int defaultCookingTime = 600;

        @Override
        public GrillRecipeShapeless fromJson(ResourceLocation recipeId, JsonObject json) {
            String s = JSONUtils.getAsString(json, "group", "");
            NonNullList<Ingredient> nonnulllist = itemsFromJson(JSONUtils.getAsJsonArray(json, "ingredients"));
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (nonnulllist.size() > 2 * 2) {
                throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + 2 * 2);
            } else {
                ItemStack itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
                float experience = JSONUtils.getAsFloat(json, "experience", 0.0F);
                int cookingTime = JSONUtils.getAsInt(json, "cookingtime", this.defaultCookingTime);
                return new GrillRecipeShapeless(recipeId, s, itemstack, nonnulllist,experience,cookingTime);
            }
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray p_199568_0_) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for(int i = 0; i < p_199568_0_.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(p_199568_0_.get(i));
                if (!ingredient.isEmpty()) {
                    nonnulllist.add(ingredient);
                }
            }

            return nonnulllist;
        }

        @Nullable
        @Override
        public GrillRecipeShapeless fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            String s = buffer.readUtf(32767);
            int i = buffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

            for(int j = 0; j < nonnulllist.size(); ++j) {
                nonnulllist.set(j, Ingredient.fromNetwork(buffer));
            }

            ItemStack itemstack = buffer.readItem();
            float experience = buffer.readFloat();
            int cookingTime = buffer.readVarInt();
            return new GrillRecipeShapeless(recipeId, s, itemstack, nonnulllist,experience,cookingTime);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, GrillRecipeShapeless recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeVarInt(recipe.ingredients.size());
            Iterator var3 = recipe.ingredients.iterator();

            while(var3.hasNext()) {
                Ingredient ingredient = (Ingredient)var3.next();
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.result);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookingTime);
        }
    }
}
