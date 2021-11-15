package nuparu.sevendaystomine.inventory.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.capability.ExtendedInventoryProvider;
import nuparu.sevendaystomine.capability.IItemHandlerExtended;
import nuparu.sevendaystomine.entity.AirdropEntity;
import nuparu.sevendaystomine.init.ModContainers;

public class ContainerAirdrop extends Container {

    private final World world;
    AirdropEntity airdropEntity;
    IItemHandlerExtended inventory;

    protected ContainerAirdrop(int windowID, PlayerInventory invPlayer, AirdropEntity airdropEntity) {
        super(ModContainers.AIRDROP.get(), windowID);
        this.world = invPlayer.player.level;
        this.airdropEntity = airdropEntity;
        inventory = airdropEntity.getCapability(ExtendedInventoryProvider.EXTENDED_INV_CAP, null).orElse(null);

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

    public static ContainerAirdrop createContainerClientSide(int windowID, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        return new ContainerAirdrop(windowID,playerInventory, (AirdropEntity) playerInventory.player.level.getEntity(packetBuffer.readInt()));
    }

    public static ContainerAirdrop createContainerServerSide(int windowID, PlayerInventory playerInventory, AirdropEntity entity){
        return new ContainerAirdrop(windowID, playerInventory, entity);
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
}
