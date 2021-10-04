package nuparu.sevendaystomine.crafting.forge;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.registries.ForgeRegistryEntry;
import nuparu.sevendaystomine.crafting.scrap.ScrapDataManager;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.init.ModRecipeSerializers;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.tileentity.TileEntityForge;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ForgeRecipeMaterial implements IForgeRecipe<TileEntityForge> {
    private final ResourceLocation id;
    private final String group;
    private final ItemStack result;
    private final ItemStack mold;
    private final NonNullList<MaterialStack> ingredients;
    protected final float experience;
    protected final int cookingTime;

    public ForgeRecipeMaterial(ResourceLocation resourceLocation, String group, ItemStack result, ItemStack mold, NonNullList<MaterialStack> ingredients, float experience, int cookingTime) {
        this.id = resourceLocation;
        this.group = group;
        this.result = result;
        this.mold = mold;
        this.ingredients = ingredients;
        this.experience = experience;
        this.cookingTime = cookingTime;
    }

    @Override
    public boolean matches(TileEntityForge forge, World world) {
        if(!ItemStack.isSameIgnoreDurability(forge.getMoldSlot(),mold)) return false;

        HashMap<EnumMaterial,Integer> invMap = new HashMap<>();
        int i = 0;
        for(ItemStack stack : forge.getActiveInventory()){
            if(stack.isEmpty()) continue;
            if(!ScrapDataManager.instance.hasEntry(stack)) return false;
            MaterialStack materialStack = ScrapDataManager.instance.getEntry(stack).toMaterialStack();
            int weight = materialStack.weight* stack.getCount();
            if(invMap.containsKey(materialStack.material)){
                weight+=invMap.get(materialStack.material);
            }
            invMap.put(materialStack.material,weight);
        }

        if(invMap.size() != ingredients.size()) return false;

        double ratio = -1;
        for(MaterialStack materialStack : ingredients){
            EnumMaterial material = materialStack.material;
            if(!invMap.containsKey(material)) return false;
            if(invMap.get(material) < materialStack.weight) return false;
            double r = (double)invMap.get(material)/materialStack.weight;
            //System.out.println(invMap.get(material) + " "  + materialStack.weight + " " + r);
            if(ratio == -1){
                ratio = r;
                continue;
            }
            if(ratio != r) return false;
        }

        return true;
    }

    @Override
    public int getRatio(TileEntityForge forge){
        HashMap<EnumMaterial,Integer> invMap = new HashMap<>();
        for(ItemStack stack : forge.getActiveInventory()){
            if(stack.isEmpty()) continue;
            if(!ScrapDataManager.instance.hasEntry(stack)) return -1;
            MaterialStack materialStack = ScrapDataManager.instance.getEntry(stack).toMaterialStack();
            int weight = materialStack.weight* stack.getCount();
            if(invMap.containsKey(materialStack.material)){
                weight+=invMap.get(materialStack.material);
            }
            invMap.put(materialStack.material,weight);
        }

        double ratio = -1;
        for(MaterialStack materialStack : ingredients){
            EnumMaterial material = materialStack.material;
            if(!invMap.containsKey(material)) return -1;
            if(invMap.get(material) < materialStack.weight) return -1;
            double r = (double)invMap.get(material)/materialStack.weight;
            if(ratio == -1){
                ratio = r;
                continue;
            }
            if(ratio != r) return -1;
        }

        if(ratio % 1 == 0){
            ratio = 1;
        }

        return (int) Math.floor(ratio);
    }

    @Override
    public ItemStack assemble(TileEntityForge forge) {

        int ratio = this.getRatio(forge);
        if(ratio <= 0) return ItemStack.EMPTY;
        ItemStack stack = this.result.copy();
        stack.setCount(stack.getCount()*ratio);
        return stack;
    }

    @Override
    public boolean consume(TileEntityForge forge){
        ArrayList<ItemStack> rest = new ArrayList<ItemStack>();
        int ratio = this.getRatio(forge);
        if(ratio <= 0) return true;
        for(MaterialStack materialStack : ingredients){
            EnumMaterial material = materialStack.material;
            int weightToConsume = materialStack.weight*ratio;
            ItemStack stack = consumeWeight(forge,material,weightToConsume);
            if(!stack.isEmpty()) {
                rest.add(stack);
            }
        }

        //Puts the leftovers to the inventory and drops them if there is no room left for them
        for(ItemStack stack : rest){
            for(int i = TileEntityForge.EnumSlots.INPUT_SLOT.ordinal();i < TileEntityForge.EnumSlots.INPUT_SLOT4.ordinal(); i++){
                ItemStack slotStack = forge.getInventory().getStackInSlot(i);
                if(slotStack.isEmpty()){
                    int countToAdd = Math.min(Math.min(stack.getCount(),forge.getMaxStackSize()),stack.getMaxStackSize());
                    ItemStack stackToAdd = stack.copy();
                    stackToAdd.setCount(countToAdd);
                    forge.getInventory().setStackInSlot(i,stackToAdd);
                    stack.shrink(countToAdd);
                }
                else if(ItemStack.isSame(stack,slotStack) && slotStack.getCount() < slotStack.getMaxStackSize()){
                    int delta = Math.min(slotStack.getMaxStackSize() - slotStack.getCount(), stack.getCount());
                    slotStack.grow(delta);
                    stack.shrink(delta);
                }

                if(stack.isEmpty()) break;
            }
            if (!stack.isEmpty()) {
                InventoryHelper.dropItemStack(forge.getLevel(), forge.getBlockPos().getX() + 0.5, forge.getBlockPos().getY() + 1, forge.getBlockPos().getZ() + 0.5, stack);
            }
        }

        return true;
    }


    public ItemStack consumeWeight(TileEntityForge forge, EnumMaterial material, int weightToConsume){
        if(weightToConsume == 0) return ItemStack.EMPTY;
        List<ItemStack> inv = forge.getActiveInventory();
        for(int i = 0; i < inv.size(); i++){
            ItemStack stack = inv.get(i);
            if(!ScrapDataManager.instance.hasEntry(stack)) continue;
            MaterialStack materialStack = ScrapDataManager.instance.getEntry(stack).toMaterialStack();
            if(materialStack.material!=material) continue;
            int weight = materialStack.weight * stack.getCount();

            int currentConsume = Math.min(weight,weightToConsume);
            int itemCount = (int) Math.ceil((double)currentConsume/materialStack.weight);
            int consumedWeight = itemCount*materialStack.weight;
            System.out.println(material + " " + consumedWeight + " " + currentConsume + " " + weight + " " + weightToConsume);

            weightToConsume-=consumedWeight;

            stack.shrink(itemCount);
            if(stack.isEmpty()){
                forge.getInventory().setStackInSlot(i,ItemStack.EMPTY);
                inv.set(i,ItemStack.EMPTY);
            }

            if(weightToConsume <= 0){
                break;
            }
        }

        //Gets leftover stack
        if(weightToConsume < 0){
            ScrapDataManager.ScrapEntry entry = ScrapDataManager.instance.getSmallestItem(material);
            ItemStack rest = new ItemStack(entry.item, (int) Math.ceil(-(double)weightToConsume/entry.weight));
            return rest;
        }

        return ItemStack.EMPTY;
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

    public ItemStack getMoldItem() {
        return this.mold.copy();
    }
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.create();
    }

    public NonNullList<MaterialStack> getMaterialIngredients() {
        return this.ingredients;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.FORGE_MATERIAL.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipeSerializers.FORGE_RECIPE_TYPE;
    }

    public float getExperience() {
        return this.experience;
    }

    public int getCookingTime() {
        return this.cookingTime;
    }

    public static class Factory extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ForgeRecipeMaterial> {
        int defaultCookingTime = 600;

        @Override
        public ForgeRecipeMaterial fromJson(ResourceLocation recipeId, JsonObject json) {
            String s = JSONUtils.getAsString(json, "group", "");
            NonNullList<MaterialStack> nonnulllist = materialsFromJson(JSONUtils.getAsJsonArray(json, "ingredients"),recipeId);
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (nonnulllist.size() > 2 * 2) {
                throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + 2 * 2);
            } else {
                ItemStack itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
                ItemStack mold = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "mold"));
                float experience = JSONUtils.getAsFloat(json, "experience", 0.0F);
                int cookingTime = JSONUtils.getAsInt(json, "cookingtime", this.defaultCookingTime);
                return new ForgeRecipeMaterial(recipeId, s, itemstack ,mold, nonnulllist,experience,cookingTime);
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

        private static NonNullList<MaterialStack> materialsFromJson(JsonArray p_199568_0_, ResourceLocation recipeId) {
            NonNullList<MaterialStack> nonnulllist = NonNullList.create();

            for(int i = 0; i < p_199568_0_.size(); ++i) {
                JsonElement element = p_199568_0_.get(i);
                JsonObject object = element.getAsJsonObject();
                if(!object.has("material")){
                    throw new JsonParseException("No materialstack material for forge material recipe " + recipeId.toString());
                }
                EnumMaterial material = EnumMaterial.byName(object.get("material").getAsString());
                int weight = 1;
                if(object.has("weight")){
                    weight = object.get("weight").getAsInt();
                }
                nonnulllist.add(new MaterialStack(material,weight));
            }

            return nonnulllist;
        }

        @Nullable
        @Override
        public ForgeRecipeMaterial fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            String s = buffer.readUtf(32767);
            int i = buffer.readVarInt();
            NonNullList<MaterialStack> nonnulllist = NonNullList.withSize(i, MaterialStack.EMPTY);

            for(int j = 0; j < nonnulllist.size(); ++j) {
                nonnulllist.set(j, MaterialStack.fromNetwork(buffer));
            }

            ItemStack itemstack = buffer.readItem();
            ItemStack mold = buffer.readItem();
            float experience = buffer.readFloat();
            int cookingTime = buffer.readVarInt();
            return new ForgeRecipeMaterial(recipeId, s, itemstack, mold, nonnulllist,experience,cookingTime);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, ForgeRecipeMaterial recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeVarInt(recipe.ingredients.size());
            Iterator var3 = recipe.ingredients.iterator();

            while(var3.hasNext()) {
                MaterialStack ingredient = (MaterialStack)var3.next();
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.result);
            buffer.writeItem(recipe.mold);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookingTime);
        }
    }
}
