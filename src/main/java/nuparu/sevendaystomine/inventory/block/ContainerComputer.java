package nuparu.sevendaystomine.inventory.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.init.ModContainers;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;

public class ContainerComputer extends Container {

    private final World world;
    TileEntityComputer tileEntity;

    protected ContainerComputer(int windowID, PlayerInventory invPlayer, TileEntityComputer tileEntity) {
        super(ModContainers.COMPUTER.get(), windowID);
        this.world = invPlayer.player.level;
        this.tileEntity = tileEntity;

        addSlot(new SlotItemHandler(tileEntity.getInventory(), 0, 12, 28));
        addSlot(new SlotItemHandler(tileEntity.getInventory(), 1, 152, 33));
        addSlot(new SlotItemHandler(tileEntity.getInventory(), 2, 152, 12));
        addSlot(new SlotItemHandler(tileEntity.getInventory(), 3, 12, 56));
        addSlot(new SlotItemHandler(tileEntity.getInventory(), 4, 152, 63));
        addSlot(new SlotItemHandler(tileEntity.getInventory(), 5, 120, 8));
        addSlot(new SlotItemHandler(tileEntity.getInventory(), 6, 191, 25));

        for (int k = 0; k < 9; ++k) {
            addSlot(new Slot(invPlayer, k, 8 + k * 18, 142));
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    public static ContainerComputer createContainerClientSide(int windowID, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        return new ContainerComputer(windowID,playerInventory, (TileEntityComputer) playerInventory.player.level.getBlockEntity(packetBuffer.readBlockPos()));
    }

    public static ContainerComputer createContainerServerSide(int windowID, PlayerInventory playerInventory, TileEntityComputer tileEntity){
        return new ContainerComputer(windowID, playerInventory, tileEntity);
    }


    @Override
    public boolean stillValid(PlayerEntity player) {
        return tileEntity.isUsableByPlayer(player);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        final Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            final ItemStack stack = slot.getItem();
            final ItemStack originalStack = stack.copy();

            if (index < 7) {
                if (!this.moveItemStackTo(stack, 7, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, 0, 7, false)) {
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
