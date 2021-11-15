package nuparu.sevendaystomine.crafting.separator;

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
import nuparu.sevendaystomine.tileentity.TileEntitySeparator;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SeparatorRecipe implements ISeparatorRecipe<TileEntitySeparator> {
    private final ResourceLocation id;
    private final String group;
    private final ItemStack resultLeft;
    private final ItemStack resultRight;
    private final NonNullList<Ingredient> ingredients;
    private final boolean isSimple;
    protected final float experience;
    protected final int cookingTime;

    public SeparatorRecipe(ResourceLocation resourceLocation, String group, ItemStack resultLeft, ItemStack resultRight, NonNullList<Ingredient> ingredients, float experience, int cookingTime) {
        this.id = resourceLocation;
        this.group = group;
        this.resultLeft = resultLeft;
        this.resultRight = resultRight;
        this.ingredients = ingredients;
        this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
        this.experience = experience;
        this.cookingTime = cookingTime;
    }

    @Override
    public boolean matches(TileEntitySeparator separatorInventory, World world) {
        RecipeItemHelper recipeitemhelper = new RecipeItemHelper();
        List<ItemStack> inputs = new ArrayList();
        int i = 0;

        for(int j = 0; j < separatorInventory.getContainerSize(); ++j) {
            ItemStack itemstack = separatorInventory.getItem(j);
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
    public ItemStack assemble(TileEntitySeparator tileEntitySeparator) {
        return ItemStack.EMPTY;
    }

    public Pair<ItemStack,ItemStack> assemblePair(TileEntitySeparator separatorInventory) {
        return Pair.of(resultLeft.copy(),resultLeft.copy());
    }

    public ItemStack getResultLeft(){
        return resultLeft.copy();
    }

    public ItemStack getResultRight(){
        return resultRight.copy();
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
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.SEPARATOR.getB().get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipeSerializers.SEPARATOR.getA();
    }

    @Override
    public float getExperience() {
        return this.experience;
    }

    @Override
    public int getCookingTime() {
        return this.cookingTime;
    }

    public static class Factory extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SeparatorRecipe> {
        int defaultCookingTime = 600;

        @Override
        public SeparatorRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String s = JSONUtils.getAsString(json, "group", "");
            NonNullList<Ingredient> nonnulllist = itemsFromJson(JSONUtils.getAsJsonArray(json, "ingredients"));
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for separator recipe");
            } else if (nonnulllist.size() > 1) {
                throw new JsonParseException("Too many ingredients for separator recipe the max is " + 2 * 2);
            } else {
                ItemStack resultLeft = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "left"));
                ItemStack resulrRight = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "right"));
                float experience = JSONUtils.getAsFloat(json, "experience", 0.0F);
                int cookingTime = JSONUtils.getAsInt(json, "cookingtime", this.defaultCookingTime);
                return new SeparatorRecipe(recipeId, s, resultLeft,resulrRight, nonnulllist,experience,cookingTime);
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
        public SeparatorRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            String s = buffer.readUtf(32767);
            int i = buffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

            for(int j = 0; j < nonnulllist.size(); ++j) {
                nonnulllist.set(j, Ingredient.fromNetwork(buffer));
            }

            ItemStack resultLeft = buffer.readItem();
            ItemStack resultRight = buffer.readItem();
            float experience = buffer.readFloat();
            int cookingTime = buffer.readVarInt();
            return new SeparatorRecipe(recipeId, s, resultLeft,resultRight, nonnulllist,experience,cookingTime);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, SeparatorRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeVarInt(recipe.ingredients.size());
            Iterator var3 = recipe.ingredients.iterator();

            while(var3.hasNext()) {
                Ingredient ingredient = (Ingredient)var3.next();
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.resultLeft);
            buffer.writeItem(recipe.resultRight);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookingTime);
        }
    }
}
