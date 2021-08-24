package nuparu.sevendaystomine.inventory.block;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
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
import nuparu.sevendaystomine.init.ModContainers;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.tileentity.TileEntityItemHandler;

public class ContainerTurretAdvanced extends Container {

    private final World world;
    TileEntityItemHandler<ItemHandlerNameable> tileEntity;

    private final ResourceLocation NO_CIRCUIT_SLOT = new ResourceLocation(SevenDaysToMine.MODID,"items/empty_circuit_slot");

    protected ContainerTurretAdvanced(int windowID, PlayerInventory invPlayer, TileEntityItemHandler<ItemHandlerNameable> tileEntity) {
        super(ModContainers.TURRET_ADVANCED.get(), windowID);
        this.world = invPlayer.player.level;
        this.tileEntity = tileEntity;

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                addSlot(new SlotItemHandler(tileEntity.getInventory(), col + row * 3, 8 + (col+3) * 18, 18 + row * 18));
            }
        }
        addSlot(new SlotItemHandler(tileEntity.getInventory(),  9, 125, 36){
            @OnlyIn(Dist.CLIENT)
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(PlayerContainer.BLOCK_ATLAS, NO_CIRCUIT_SLOT);
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

    public static ContainerTurretAdvanced createContainerClientSide(int windowID, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        return new ContainerTurretAdvanced(windowID,playerInventory, (TileEntityItemHandler<ItemHandlerNameable>) playerInventory.player.level.getBlockEntity(packetBuffer.readBlockPos()));
    }

    public static ContainerTurretAdvanced createContainerServerSide(int windowID, PlayerInventory playerInventory, TileEntityItemHandler<ItemHandlerNameable> tileEntity){
        return new ContainerTurretAdvanced(windowID, playerInventory, tileEntity);
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

            if (index < 10) {
                if (!this.moveItemStackTo(stack, 10, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, 0, 10, false)) {
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
