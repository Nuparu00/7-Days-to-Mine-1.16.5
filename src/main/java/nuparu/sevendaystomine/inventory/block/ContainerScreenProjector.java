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
import nuparu.sevendaystomine.tileentity.TileEntityScreenProjector;

public class ContainerScreenProjector extends Container {

    private final World world;
    TileEntityScreenProjector tileEntity;

    protected ContainerScreenProjector(int windowID, PlayerInventory invPlayer, TileEntityScreenProjector tileEntity) {
        super(ModContainers.SCREEN_PROJECTOR.get(), windowID);
        this.world = invPlayer.player.level;
        this.tileEntity = tileEntity;

        addSlot(new SlotItemHandler(tileEntity.getInventory(), 0, 80, 54));
        for (int k = 0; k < 9; ++k) {
            addSlot(new Slot(invPlayer, k, 8 + k * 18, 144));
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 86 + i * 18));
            }
        }
    }

    public static ContainerScreenProjector createContainerClientSide(int windowID, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        return new ContainerScreenProjector(windowID,playerInventory, (TileEntityScreenProjector) playerInventory.player.level.getBlockEntity(packetBuffer.readBlockPos()));
    }

    public static ContainerScreenProjector createContainerServerSide(int windowID, PlayerInventory playerInventory, TileEntityScreenProjector tileEntity){
        return new ContainerScreenProjector(windowID, playerInventory, tileEntity);
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

            if (index == 0) {
                if (!this.moveItemStackTo(stack, 1, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, 0, 1, false)) {
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
