package nuparu.sevendaystomine.inventory.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.ExtendedInventoryProvider;
import nuparu.sevendaystomine.capability.IItemHandlerExtended;
import nuparu.sevendaystomine.init.ModContainers;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.tileentity.TileEntityItemHandler;

public class ContainerBackpack extends Container {

    private final World world;
    ItemStack stack;
    IItemHandlerExtended inventory;

    protected ContainerBackpack(int windowID, PlayerInventory invPlayer, ItemStack stack) {
        super(ModContainers.BACKPACK.get(), windowID);
        this.world = invPlayer.player.level;
        inventory = stack.getCapability(ExtendedInventoryProvider.EXTENDED_INV_CAP, null).orElse(null);

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                addSlot(new SlotItemHandler(inventory, col + row * 3, 8 + (col+3) * 18, 18 + row * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            addSlot(new Slot(invPlayer, k, 8 + k * 18, 144));
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 86 + i * 18));
            }
        }
    }

    public static ContainerBackpack createContainerClientSide(int windowID, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        return new ContainerBackpack(windowID,playerInventory, packetBuffer.readItem());
    }

    public static ContainerBackpack createContainerServerSide(int windowID, PlayerInventory playerInventory, ItemStack stack){
        return new ContainerBackpack(windowID, playerInventory, stack);
    }


    @Override
    public boolean stillValid(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        final Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            final ItemStack stack = slot.getItem();
            final ItemStack originalStack = stack.copy();

            if (index < 9) {
                if (!this.moveItemStackTo(stack, 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, 0, 9, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            return originalStack;
        }

        return ItemStack.EMPTY;
    }

    public ItemStack clicked(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
         PlayerInventory inventoryplayer = player.inventory;

        if (slotId == -999)
        {
            if (inventoryplayer.getCarried().getItem() == ModItems.BACKPACK.get())
            {
                return ItemStack.EMPTY;
            }
        }
        else if(slotId >= 0) {
            Slot slot = this.getSlot(slotId);
            if(slot != null && slot.hasItem() && slot.getItem().getItem() == ModItems.BACKPACK.get()) {
                return ItemStack.EMPTY;
            }
        }

        return super.clicked(slotId, dragType, clickTypeIn, player);
    }
}
