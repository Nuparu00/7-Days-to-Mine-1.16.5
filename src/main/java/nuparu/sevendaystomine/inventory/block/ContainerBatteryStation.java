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
import nuparu.sevendaystomine.tileentity.TileEntityBatteryStation;

public class ContainerBatteryStation extends Container {

    private final World world;
    TileEntityBatteryStation tileEntity;

    protected ContainerBatteryStation(int windowID, PlayerInventory invPlayer, TileEntityBatteryStation tileEntity) {
        super(ModContainers.BATTERY_STATION.get(), windowID);
        this.world = invPlayer.player.level;
        this.tileEntity = tileEntity;

        addSlot(new SlotItemHandler(tileEntity.getInventory(), 0, 62, 48));
        addSlot(new SlotItemHandler(tileEntity.getInventory(), 1, 98, 48));


        for (int k = 0; k < 9; ++k) {
            addSlot(new Slot(invPlayer, k, 8 + k * 18, 142));
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    public static ContainerBatteryStation createContainerClientSide(int windowID, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        return new ContainerBatteryStation(windowID, playerInventory, (TileEntityBatteryStation) playerInventory.player.level.getBlockEntity(packetBuffer.readBlockPos()));
    }

    public static ContainerBatteryStation createContainerServerSide(int windowID, PlayerInventory playerInventory, TileEntityBatteryStation tileEntity) {
        return new ContainerBatteryStation(windowID, playerInventory, tileEntity);
    }

    public TileEntityBatteryStation getTileEntity() {
        return tileEntity;
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

            if (index < 2) {
                if (!this.moveItemStackTo(stack, 2, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, 0, 2, false)) {
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
