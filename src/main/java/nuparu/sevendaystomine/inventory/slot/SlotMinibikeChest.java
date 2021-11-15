package nuparu.sevendaystomine.inventory.slot;

import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.inventory.entity.ContainerMinibike;

public class SlotMinibikeChest extends SlotItemHandler {

    ContainerMinibike containerMinibike;

    public SlotMinibikeChest(IItemHandler itemHandler, int index, int xPosition, int yPosition,ContainerMinibike containerMinibike) {
        super(itemHandler, index, xPosition, yPosition);
        this.containerMinibike = containerMinibike;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() == Blocks.CHEST.asItem();
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.hasItem()) {
            if (this.getItem().getItem() == Blocks.CHEST.asItem()) {
                if (!containerMinibike.addedChest) {
                    containerMinibike.bindChest();
                }
            }
        } else {
            if (containerMinibike.addedChest) {
                containerMinibike.unbindChest();
            }
        }
    }
}