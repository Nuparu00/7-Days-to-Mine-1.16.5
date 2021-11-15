package nuparu.sevendaystomine.inventory.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;

public class UncraftResultInventory implements IInventory {
    private final NonNullList<ItemStack> itemStacks = NonNullList.withSize(25, ItemStack.EMPTY);
    @Nullable
    private IRecipe<?> recipeUsed;

    public int getContainerSize() {
        return 25;
    }

    public boolean isEmpty() {
        for(ItemStack itemstack : this.itemStacks) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public ItemStack getItem(int slot) {
        return this.itemStacks.get(slot);
    }

    public ItemStack removeItem(int slot, int amount) {


        return ItemStackHelper.takeItem(this.itemStacks, slot);
    }

    public ItemStack removeItemNoUpdate(int slot) {
        return ItemStackHelper.takeItem(this.itemStacks, slot);
    }

    public void setItem(int slot, ItemStack p_70299_2_) {
        this.itemStacks.set(slot, p_70299_2_);
    }

    public void setChanged() {
    }

    public boolean stillValid(PlayerEntity p_70300_1_) {
        return true;
    }

    public void clearContent() {
        this.itemStacks.clear();
    }

    public void setRecipeUsed(@Nullable IRecipe<?> p_193056_1_) {
        this.recipeUsed = p_193056_1_;
    }

    @Nullable
    public IRecipe<?> getRecipeUsed() {
        return this.recipeUsed;
    }
}