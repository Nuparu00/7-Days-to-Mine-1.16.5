package nuparu.sevendaystomine.inventory.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.init.ModContainers;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.tileentity.TileEntityCarMaster;
import nuparu.sevendaystomine.tileentity.TileEntityItemHandler;

public class RowedContainer extends Container {
    private final TileEntityCarMaster container;
    private final int containerRows;

    public RowedContainer(int windowID, PlayerInventory playerInventory, TileEntityCarMaster p_i50092_4_, int rows) {
        super(ModContainers.ROWED.get(), windowID);
        this.container = p_i50092_4_;
        this.containerRows = rows;
        p_i50092_4_.startOpen(playerInventory.player);
        int i = (this.containerRows - 4) * 18;

        for(int j = 0; j < this.containerRows; ++j) {
            for(int k = 0; k < 9; ++k) {
                this.addSlot(new SlotItemHandler(p_i50092_4_.getInventory(), k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 161 + i));
        }

    }

    public static RowedContainer createContainerClientSide(int windowID, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        return new RowedContainer(windowID,playerInventory, (TileEntityCarMaster) playerInventory.player.level.getBlockEntity(packetBuffer.readBlockPos()),3);
    }

    public static RowedContainer createContainerServerSide(int windowID, PlayerInventory playerInventory, TileEntityCarMaster tileEntity){
        return new RowedContainer(windowID, playerInventory, tileEntity,3);
    }

    public boolean stillValid(PlayerEntity p_75145_1_) {
        return this.container.isUsableByPlayer(p_75145_1_);
    }

    public ItemStack quickMoveStack(PlayerEntity p_82846_1_, int p_82846_2_) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(p_82846_2_);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (p_82846_2_ < this.containerRows * 9) {
                if (!this.moveItemStackTo(itemstack1, this.containerRows * 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.containerRows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    public void removed(PlayerEntity p_75134_1_) {
        super.removed(p_75134_1_);
        this.container.stopOpen(p_75134_1_);
    }

    @OnlyIn(Dist.CLIENT)
    public int getRowCount() {
        return this.containerRows;
    }
}