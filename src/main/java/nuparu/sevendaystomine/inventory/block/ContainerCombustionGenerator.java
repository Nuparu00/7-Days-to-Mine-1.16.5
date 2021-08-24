package nuparu.sevendaystomine.inventory.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.init.ModContainers;
import nuparu.sevendaystomine.tileentity.TileEntityCombustionGenerator;

public class ContainerCombustionGenerator extends Container {

    private final World world;
    private TileEntityCombustionGenerator tileEntity;
    private IIntArray intArray;

    protected ContainerCombustionGenerator(int windowID, PlayerInventory invPlayer, TileEntityCombustionGenerator tileEntity, IIntArray intArray) {
        super(ModContainers.COMBUSTION_GENERATOR.get(), windowID);
        this.world = invPlayer.player.level;
        this.tileEntity = tileEntity;
        this.intArray = intArray;

        addDataSlots(intArray);

        addSlot(new SlotItemHandler(tileEntity.getInventory(), 0, 29, 60));
        for (int k = 0; k < 9; ++k) {
            addSlot(new Slot(invPlayer, k, 8 + k * 18, 142));
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    public static ContainerCombustionGenerator createContainerClientSide(int windowID, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        return new ContainerCombustionGenerator(windowID,playerInventory, (TileEntityCombustionGenerator) playerInventory.player.level.getBlockEntity(packetBuffer.readBlockPos()),new IntArray(4));
    }

    public static ContainerCombustionGenerator createContainerServerSide(int windowID, PlayerInventory playerInventory, TileEntityCombustionGenerator tileEntity){
        return new ContainerCombustionGenerator(windowID, playerInventory, tileEntity,tileEntity.dataAccess);
    }

    public TileEntityCombustionGenerator getTileEntity(){
        return this.tileEntity;
    }

    public double fractionOfFuelRemaining(int fuelSlot) {
        if (intArray.get(1) <= 0)
            return 0;
        double fraction = intArray.get(0)
                / (double) intArray.get(1);
        return MathHelper.clamp(fraction, 0.0, 1.0);
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
