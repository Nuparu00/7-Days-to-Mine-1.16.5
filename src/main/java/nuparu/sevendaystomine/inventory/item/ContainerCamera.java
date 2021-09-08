package nuparu.sevendaystomine.inventory.item;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.ExtendedInventoryProvider;
import nuparu.sevendaystomine.capability.IItemHandlerExtended;
import nuparu.sevendaystomine.init.ModContainers;
import nuparu.sevendaystomine.init.ModItems;

public class ContainerCamera extends Container {

    private final ResourceLocation NO_PAPER_SLOT = new ResourceLocation(SevenDaysToMine.MODID,"items/empty_paper_slot");

    private final World world;
    ItemStack stack;
    IItemHandlerExtended inventory;

    protected ContainerCamera(int windowID, PlayerInventory invPlayer, ItemStack stack) {
        super(ModContainers.CAMERA.get(), windowID);
        this.world = invPlayer.player.level;
        inventory = stack.getCapability(ExtendedInventoryProvider.EXTENDED_INV_CAP, null).orElse(null);

        addSlot(new SlotItemHandler(inventory, 0, 80, 54){

            @OnlyIn(Dist.CLIENT)
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(PlayerContainer.BLOCK_ATLAS, NO_PAPER_SLOT);
            }
        });

        for (int k = 0; k < 9; ++k) {
            addSlot(new Slot(invPlayer, k, 8 + k * 18, 144));
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 86 + i * 18));
            }
        }
    }

    public static ContainerCamera createContainerClientSide(int windowID, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        return new ContainerCamera(windowID,playerInventory, packetBuffer.readItem());
    }

    public static ContainerCamera createContainerServerSide(int windowID, PlayerInventory playerInventory, ItemStack stack){
        return new ContainerCamera(windowID, playerInventory, stack);
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

    public ItemStack clicked(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
         PlayerInventory inventoryplayer = player.inventory;

        if (slotId == -999)
        {
            if (inventoryplayer.getCarried().getItem() == ModItems.ANALOG_CAMERA.get())
            {
                return ItemStack.EMPTY;
            }
        }
        else if(slotId >= 0) {
            Slot slot = this.getSlot(slotId);
            if(slot != null && slot.hasItem() && slot.getItem().getItem() == ModItems.ANALOG_CAMERA.get()) {
                return ItemStack.EMPTY;
            }
        }

        return super.clicked(slotId, dragType, clickTypeIn, player);
    }
}
