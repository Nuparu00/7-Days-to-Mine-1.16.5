package nuparu.sevendaystomine.inventory.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.crafting.forge.ForgeRecipeManager;
import nuparu.sevendaystomine.crafting.forge.IForgeRecipe;
import nuparu.sevendaystomine.init.ModContainers;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.item.ItemScrap;
import nuparu.sevendaystomine.tileentity.ItemZoneContents;
import nuparu.sevendaystomine.tileentity.TileEntityForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContainerForge extends Container {

    private static final Logger LOGGER = LogManager.getLogger();

    // -------- methods used by the ContainerScreen to render parts of the display
    private IIntArray intArray;
    private final World world; // needed for some helper methods
    TileEntityForge forge;

    public ContainerForge(int windowID, PlayerInventory invPlayer,TileEntityForge forge, IIntArray intArray) {
        super(ModContainers.FORGE.get(), windowID);
        if (ModContainers.FORGE.get() == null)
            throw new IllegalStateException(
                    "Must initialise containerTypeContainerForge before constructing a ContainerForge!");
        this.intArray = intArray;
        this.world = invPlayer.player.level;
        this.forge = forge;

        addDataSlots(intArray); // tell vanilla to keep the IIntArray synchronised between client and
        // server Containers
        if(forge != null) {
            addSlot(new SlotItemHandler(forge.getInventory(), TileEntityForge.EnumSlots.INPUT_SLOT.ordinal(), 78, 11));
            addSlot(new SlotItemHandler(forge.getInventory(), TileEntityForge.EnumSlots.INPUT_SLOT2.ordinal(), 97, 11));
            addSlot(new SlotItemHandler(forge.getInventory(), TileEntityForge.EnumSlots.INPUT_SLOT3.ordinal(), 78, 29));
            addSlot(new SlotItemHandler(forge.getInventory(), TileEntityForge.EnumSlots.INPUT_SLOT4.ordinal(), 97, 29));

            addSlot(new SlotOutput(forge.getInventory(), TileEntityForge.EnumSlots.OUTPUT_SLOT.ordinal(), 148, 42));
            addSlot(new SlotFuel(forge.getInventory(), TileEntityForge.EnumSlots.FUEL_SLOT.ordinal(), 88, 63));
            addSlot(new SlotItemHandler(forge.getInventory(), TileEntityForge.EnumSlots.MOLD_SLOT.ordinal(), 45, 42));

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

    public static ContainerForge createContainerServerSide(int windowID, PlayerInventory playerInventory,TileEntityForge forge){
        return new ContainerForge(windowID, playerInventory, forge, forge.dataAccess);
    }

    public static ContainerForge createContainerClientSide(int windowID, PlayerInventory playerInventory,
                                                           net.minecraft.network.PacketBuffer extraData) {
        return new ContainerForge(windowID, playerInventory, (TileEntityForge) playerInventory.player.level.getBlockEntity(extraData.readBlockPos()), new IntArray(4));
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return this.forge != null && this.forge.canPlayerAccessInventory(player);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot == null || !slot.hasItem())
            return ItemStack.EMPTY;
        ItemStack sourceItemStack = slot.getItem();
        itemstack = sourceItemStack.copy();

        if (index < 7) {
            slot.onQuickCraft(sourceItemStack, itemstack);
            if (!this.moveItemStackTo(sourceItemStack, 7, 39, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (TileEntityForge.isItemFuel(sourceItemStack)) {
                if (!this.moveItemStackTo(sourceItemStack, 5, 6, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (isMold(sourceItemStack.getItem())) {
                if (!this.moveItemStackTo(sourceItemStack, 6, 7, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (isIngredient(sourceItemStack.getItem())) {
                if (!this.moveItemStackTo(sourceItemStack, 0, 4, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 7 && index < 34) {
                if (!this.moveItemStackTo(sourceItemStack, 34, 43, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 34 && index < 43 && !this.moveItemStackTo(sourceItemStack, 7, 34, false)) {
                return ItemStack.EMPTY;
            }
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
        if (intArray.get(3) == 0)
            return 0;
        double fraction = intArray.get(2) / (double) intArray.get(3);
        return MathHelper.clamp(fraction, 0.0, 1.0);
    }

    public boolean isMold(Item item) {
        for (IForgeRecipe recipe : ForgeRecipeManager.getInstance().getRecipes()) {
            if (recipe.getMold() != null && !recipe.getMold().isEmpty() && recipe.getMold().getItem() == item) {
                return true;
            }
        }
        return false;
    }

    public boolean isIngredient(Item item) {
        if (item instanceof ItemScrap)
            return true;
        for (IForgeRecipe recipe : ForgeRecipeManager.getInstance().getRecipes()) {
            if (recipe.getIngredients() != null) {
                for (ItemStack stack : recipe.getIngredients()) {
                    if (!stack.isEmpty() && stack.getItem() == item) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // SlotFuel is a slot for fuel items
    public class SlotFuel extends SlotItemHandler {

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
    public class SlotOutput extends SlotItemHandler {
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
