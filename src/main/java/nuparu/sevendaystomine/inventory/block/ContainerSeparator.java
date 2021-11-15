package nuparu.sevendaystomine.inventory.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.init.ModContainers;
import nuparu.sevendaystomine.tileentity.TileEntitySeparator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContainerSeparator extends Container {

    private static final Logger LOGGER = LogManager.getLogger();

    // -------- methods used by the ContainerScreen to render parts of the display
    private IIntArray intArray;
    private final World world; // needed for some helper methods
    public TileEntitySeparator separator;

    public ContainerSeparator(int windowID, PlayerInventory invPlayer, TileEntitySeparator separator, IIntArray intArray) {
        super(ModContainers.SEPARATOR.get(), windowID);
        if (ModContainers.SEPARATOR.get() == null)
            throw new IllegalStateException(
                    "Must initialise ModContainers.SEPARATOR before constructing a ContainerSeparator!");
        this.intArray = intArray;
        this.world = invPlayer.player.level;
        this.separator = separator;

        addDataSlots(intArray); // tell vanilla to keep the IIntArray synchronised between client and
        // server Containers
        if(separator != null) {

            addSlot(new SlotItemHandler(separator.getInventory(), 0, 80, 42));
            addSlot(new SlotOutput(separator.getInventory(), 1, 12, 42));
            addSlot(new SlotOutput(separator.getInventory(), 2, 148, 42));

            for (int k = 0; k < 9; ++k) {
                addSlot(new Slot(invPlayer, k, 8 + k * 18, 142));
            }

            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 9; ++j) {
                    addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
                }
            }
        }
    }

    // --------- Customise the different slots (in particular - what items they will
    // accept)

    public static ContainerSeparator createContainerServerSide(int windowID, PlayerInventory playerInventory, TileEntitySeparator separator){
        return new ContainerSeparator(windowID, playerInventory, separator, separator.dataAccess);
    }

    public static ContainerSeparator createContainerClientSide(int windowID, PlayerInventory playerInventory,
                                                               net.minecraft.network.PacketBuffer extraData) {
        return new ContainerSeparator(windowID, playerInventory, (TileEntitySeparator) playerInventory.player.level.getBlockEntity(extraData.readBlockPos()), new IntArray(4));
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return this.separator != null && this.separator.isUsableByPlayer(player);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot == null || !slot.hasItem())
            return ItemStack.EMPTY;
        ItemStack sourceItemStack = slot.getItem();
        itemstack = sourceItemStack.copy();

        if (index < 3) {
            if (!this.moveItemStackTo(sourceItemStack, 3, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.moveItemStackTo(sourceItemStack, 0, 3, false)) {
            return ItemStack.EMPTY;
        }

        if (sourceItemStack.getCount() == 0) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return ItemStack.EMPTY;
    }

    /**
     * Returns the amount of fuel remaining on the currently burning item in the
     * given fuel slot.
     *
     * @return fraction remaining, between 0.0 - 1.0
     * @fuelSlot the number of the fuel slot (0..3)
     */
    public double fractionOfFuelRemaining(int fuelSlot) {
        if (intArray.get(1) <= 0)
            return 0;
        double fraction = intArray.get(0)
                / (double) intArray.get(1);
        return MathHelper.clamp(fraction, 0.0, 1.0);
    }

    /**
     * return the remaining burn time of the fuel in the given slot
     *
     * @param fuelSlot the number of the fuel slot (0..3)
     * @return seconds remaining
     */
    public int secondsOfFuelRemaining(int fuelSlot) {
        if (intArray.get(0) <= 0)
            return 0;
        return intArray.get(0) / 20; // 20 ticks per second
    }

    /**
     * Returns the amount of cook time completed on the currently cooking item.
     *
     * @return fraction remaining, between 0 - 1
     */
    public double fractionOfCookTimeComplete() {
        if (intArray.get(1) == 0)
            return 0;
        double fraction = intArray.get(0) / (double) intArray.get(1);
        return MathHelper.clamp(fraction, 0.0, 1.0);
    }

    public boolean isMold(Item item) {
        return false;
    }

    public boolean isIngredient(Item item) {

        return false;
    }

    // SlotFuel is a slot for fuel items
    public static class SlotFuel extends SlotItemHandler {

        public SlotFuel(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        // if this function returns false, the player won't be able to insert the given
        // item into this slot
        @Override
        public boolean mayPlace(ItemStack stack) {
            return ForgeHooks.getBurnTime(stack) > 0;
        }
    }


    // SlotOutput is a slot that will not accept any item
    public static class SlotOutput extends SlotItemHandler {
        public SlotOutput(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        // if this function returns false, the player won't be able to insert the given
        // item into this slot
        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }

}
