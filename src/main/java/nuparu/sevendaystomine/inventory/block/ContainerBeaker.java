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
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.init.ModContainers;
import nuparu.sevendaystomine.tileentity.TileEntityBeaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContainerBeaker extends Container {

    private static final Logger LOGGER = LogManager.getLogger();

    // -------- methods used by the ContainerScreen to render parts of the display
    private IIntArray intArray;
    private final World world; // needed for some helper methods
    public TileEntityBeaker beaker;

    public ContainerBeaker(int windowID, PlayerInventory invPlayer, TileEntityBeaker beaker, IIntArray intArray) {
        super(ModContainers.BEAKER.get(), windowID);
        if (ModContainers.BEAKER.get() == null)
            throw new IllegalStateException(
                    "Must initialise container type before constructing a ContainerBeaker!");
        this.intArray = intArray;
        this.world = invPlayer.player.level;
        this.beaker = beaker;

        addDataSlots(intArray); // tell vanilla to keep the IIntArray synchronised between client and
        // server Containers
        if(beaker != null) {
            addSlot(new SlotItemHandler(beaker.getInventory(), TileEntityBeaker.EnumSlots.INPUT_SLOT.ordinal(), 47, 27));
            addSlot(new SlotItemHandler(beaker.getInventory(), TileEntityBeaker.EnumSlots.INPUT_SLOT2.ordinal(), 65, 27));
            addSlot(new SlotItemHandler(beaker.getInventory(), TileEntityBeaker.EnumSlots.INPUT_SLOT3.ordinal(), 47, 45));
            addSlot(new SlotItemHandler(beaker.getInventory(), TileEntityBeaker.EnumSlots.INPUT_SLOT4.ordinal(), 65, 45));

            addSlot(new SlotOutput(invPlayer.player, beaker, beaker.getInventory(), TileEntityBeaker.EnumSlots.OUTPUT_SLOT.ordinal(), 123, 36));

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

    public static ContainerBeaker createContainerServerSide(int windowID, PlayerInventory playerInventory, TileEntityBeaker beaker){
        return new ContainerBeaker(windowID, playerInventory, beaker, beaker.dataAccess);
    }

    public static ContainerBeaker createContainerClientSide(int windowID, PlayerInventory playerInventory,
                                                            net.minecraft.network.PacketBuffer extraData) {
        return new ContainerBeaker(windowID, playerInventory, (TileEntityBeaker) playerInventory.player.level.getBlockEntity(extraData.readBlockPos()), new IntArray(4));
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return this.beaker != null && this.beaker.canPlayerAccessInventory(player);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot == null || !slot.hasItem())
            return ItemStack.EMPTY;
        ItemStack sourceItemStack = slot.getItem();
        itemstack = sourceItemStack.copy();

        if (index < 5) {
            slot.onQuickCraft(sourceItemStack, itemstack);
            if (!this.moveItemStackTo(sourceItemStack, 5, 39, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (isIngredient(sourceItemStack.getItem())) {
                if (!this.moveItemStackTo(sourceItemStack, 0, 4, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 5 && index < 32) {
                if (!this.moveItemStackTo(sourceItemStack, 32, 41, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 32 && index < 41 && !this.moveItemStackTo(sourceItemStack, 7, 32, false)) {
                return ItemStack.EMPTY;
            }
        }
        return ItemStack.EMPTY;
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

    public boolean isIngredient(Item item) {

        return false;
    }

    // SlotOutput is a slot that will not accept any item
    public static class SlotOutput extends SlotItemHandler {
        private final PlayerEntity player;
        private final TileEntityBeaker grill;
        private int removeCount;
        public SlotOutput(PlayerEntity player, TileEntityBeaker grill,IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
            this.player = player;
            this.grill = grill;
        }

        // if this function returns false, the player won't be able to insert the given
        // item into this slot
        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        public ItemStack remove(int p_75209_1_) {
            if (this.hasItem()) {
                this.removeCount += Math.min(p_75209_1_, this.getItem().getCount());
            }

            return super.remove(p_75209_1_);
        }


        public ItemStack onTake(PlayerEntity p_190901_1_, ItemStack p_190901_2_) {
            this.checkTakeAchievements(p_190901_2_);
            super.onTake(p_190901_1_, p_190901_2_);
            return p_190901_2_;
        }

        protected void onQuickCraft(ItemStack p_75210_1_, int p_75210_2_) {
            this.removeCount += p_75210_2_;
            this.checkTakeAchievements(p_75210_1_);
        }

        protected void checkTakeAchievements(ItemStack p_75208_1_) {
            p_75208_1_.onCraftedBy(this.player.level, this.player, this.removeCount);
            if (!this.player.level.isClientSide) {
                grill.awardUsedRecipesAndPopExperience(this.player);
            }

            this.removeCount = 0;
            net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerSmeltedEvent(this.player, p_75208_1_);
        }
    }

}
