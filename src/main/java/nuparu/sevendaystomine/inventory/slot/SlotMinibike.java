package nuparu.sevendaystomine.inventory.slot;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotMinibike extends SlotItemHandler {

    protected Item validItem;

    public SlotMinibike(IItemHandler itemHandler, int index, int xPosition, int yPosition, Item validItem) {
        super(itemHandler, index, xPosition, yPosition);
        this.validItem = validItem;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() == validItem;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }
}
