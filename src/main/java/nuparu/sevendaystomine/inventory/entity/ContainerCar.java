package nuparu.sevendaystomine.inventory.entity;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.capability.ExtendedInventoryProvider;
import nuparu.sevendaystomine.capability.IItemHandlerExtended;
import nuparu.sevendaystomine.entity.CarEntity;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModContainers;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.inventory.slot.SlotMinibike;

import java.util.ArrayList;

public class ContainerCar extends Container {

    private final World world;
    public boolean addedChest = false;
    public ArrayList<Slot> chestSlots = new ArrayList<Slot>();
    public CarEntity carEntity;
    IItemHandlerExtended inventory;

    protected ContainerCar(int windowID, PlayerInventory invPlayer, CarEntity carEntity) {
        super(ModContainers.CAR.get(), windowID);
        this.world = invPlayer.player.level;
        this.carEntity = carEntity;
        inventory = carEntity.getCapability(ExtendedInventoryProvider.EXTENDED_INV_CAP, null).orElse(null);

        this.addSlot(new SlotMinibike(inventory, 0, 8, 8, ModItems.MINIBIKE_HANDLES.get()));
        this.addSlot(new SlotMinibike(inventory, 1, 8, 26, ModBlocks.WHEELS.get().asItem()));
        this.addSlot(new SlotMinibike(inventory, 2, 8, 44, ModBlocks.WHEELS.get().asItem()));

        this.addSlot(new SlotMinibike(inventory, 3, 152, 8, ModItems.MINIBIKE_SEAT.get()));
        this.addSlot(new SlotMinibike(inventory, 4, 152, 26, ModItems.CAR_BATTERY.get()));
        this.addSlot(
                new SlotMinibike(inventory, 5, 152, 44, ModItems.SMALL_ENGINE.get()));

        for (int k = 0; k < 9; ++k) {
            addSlot(new Slot(invPlayer, k, 8 + k * 18, 142));
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }


        bindChest();
    }

    public static ContainerCar createContainerClientSide(int windowID, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        return new ContainerCar(windowID, playerInventory, (CarEntity) playerInventory.player.level.getEntity(packetBuffer.readInt()));
    }

    public static ContainerCar createContainerServerSide(int windowID, PlayerInventory playerInventory, CarEntity entity) {
        return new ContainerCar(windowID, playerInventory, entity);
    }

    public void bindChest() {
        int l = 0;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {

                Slot slot = new SlotItemHandler(inventory, 6 + l, 181 + j * 18, 8 + i * 18);
                this.chestSlots.add(slot);
                this.addSlot(slot);
                l++;
            }
        }
    }

    /*public void unbindChest() {
        addedChest = false;
        for (Slot slot : chestSlots) {
            //this.inventoryItemStacks.remove(this.inventorySlots.indexOf(slot));
            if (!this.minibike.level.isClientSide()) {
                float width = this.minibike.getDimensions(Pose.STANDING).width/2f;
                float height = this.minibike.getDimensions(Pose.STANDING).height;
                InventoryHelper.dropItemStack(this.minibike.level, this.minibike.position().x+width, this.minibike.position().y + height, this.minibike.position().z+width, slot.getItem());
            }
            this.minibike.replaceItemInInventory(slot.getSlotIndex(), ItemStack.EMPTY);
            slot.setChanged();

            this.slots.remove(slot);
        }
    }*/

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
            Item item = stack.getItem();

            if (index < 7) {
                if (!this.moveItemStackTo(stack, 7, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(originalStack, stack);
                if (!carEntity.level.isClientSide()) {
                    carEntity.updateInventory();
                }
            } else {
                if (item == ModItems.SMALL_ENGINE.get()) {
                    if (!this.moveItemStackTo(stack, 0, 6, true)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onQuickCraft(originalStack, stack);
                } else if (item == ModItems.MINIBIKE_HANDLES.get()) {
                    if (!this.moveItemStackTo(stack, 0, 6, true)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onQuickCraft(originalStack, stack);
                } else if (item == ModItems.MINIBIKE_SEAT.get()) {
                    if (!this.moveItemStackTo(stack, 0, 6, true)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onQuickCraft(originalStack, stack);
                } else if (item == ModItems.CAR_BATTERY.get()) {
                    if (!this.moveItemStackTo(stack, 0, 6, true)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onQuickCraft(originalStack, stack);
                }
                else if (item == ModBlocks.WHEELS.get().asItem()) {
                    if (!this.moveItemStackTo(stack, 0, 6, true)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onQuickCraft(originalStack, stack);
                }
                else if (item == Blocks.CHEST.asItem()) {
                    if (!this.moveItemStackTo(stack, 0, 6, true)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onQuickCraft(originalStack, stack);
                }

                else if (index <= 32) {
                    if (!this.moveItemStackTo(stack, 33, 41, true)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onQuickCraft(originalStack, stack);
                } else if (index <= 41) {
                    if (!this.moveItemStackTo(stack, 6, 32, true)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onQuickCraft(originalStack, stack);
                }
            }



               /* if (!this.moveItemStackTo(stack, 0, 9, false)) {
                return ItemStack.EMPTY;
            }*/

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
