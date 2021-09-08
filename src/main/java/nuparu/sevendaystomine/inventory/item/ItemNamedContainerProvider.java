package nuparu.sevendaystomine.inventory.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public abstract class ItemNamedContainerProvider implements INamedContainerProvider {

    public ItemStack stack;
    public ITextComponent name;

    public ItemNamedContainerProvider(ItemStack stack, ITextComponent name){
        this.stack = stack;
        this.name = name;
    }


    @Override
    public ITextComponent getDisplayName() {
        return name;
    }

    @Nullable
    @Override
    public abstract Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_);
}
