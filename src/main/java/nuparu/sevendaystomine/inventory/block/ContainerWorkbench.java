package nuparu.sevendaystomine.inventory.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.init.ModContainers;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.tileentity.TileEntityItemHandler;

import java.util.Optional;

public class ContainerWorkbench extends Container {

    private final CraftingInventory craftSlots = new CraftingInventory(this,5,5);
    private final CraftResultInventory resultSlots = new CraftResultInventory();
    private final PlayerEntity player;
    private final IWorldPosCallable access;

    private final World world;
    TileEntityItemHandler<ItemHandlerNameable> tileEntity;

    protected ContainerWorkbench(int windowID, PlayerInventory invPlayer, TileEntityItemHandler<ItemHandlerNameable> tileEntity, BlockPos pos) {
        super(ModContainers.WORKBENCH.get(), windowID);
        this.world = invPlayer.player.level;
        this.tileEntity = tileEntity;
        this.player = invPlayer.player;;
        this.access = IWorldPosCallable.create(invPlayer.player.level,pos);

        this.addSlot(new CraftingResultSlot(player, this.craftSlots, this.resultSlots, 0, 134, 44));

        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                this.addSlot(new Slot(this.craftSlots, j + i * 5, 8 + j * 18, 7 + i * 18));
            }
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 106 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            addSlot(new Slot(invPlayer, k, 8 + k * 18, 164));
        }
    }

    public static ContainerWorkbench createContainerClientSide(int windowID, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        BlockPos pos = packetBuffer.readBlockPos();
        return new ContainerWorkbench(windowID,playerInventory, (TileEntityItemHandler<ItemHandlerNameable>) playerInventory.player.level.getBlockEntity(pos),pos);
    }

    public static ContainerWorkbench createContainerServerSide(int windowID, PlayerInventory playerInventory, TileEntityItemHandler<ItemHandlerNameable> tileEntity, BlockPos pos){
        return new ContainerWorkbench(windowID, playerInventory, tileEntity,pos);
    }

    protected static void slotChangedCraftingGrid(int p_217066_0_, World p_217066_1_, PlayerEntity p_217066_2_, CraftingInventory p_217066_3_, CraftResultInventory p_217066_4_) {
        if (!p_217066_1_.isClientSide) {
            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)p_217066_2_;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<ICraftingRecipe> optional = p_217066_1_.getServer().getRecipeManager().getRecipeFor(IRecipeType.CRAFTING, p_217066_3_, p_217066_1_);
            if (optional.isPresent()) {
                ICraftingRecipe icraftingrecipe = optional.get();
                if (p_217066_4_.setRecipeUsed(p_217066_1_, serverplayerentity, icraftingrecipe)) {
                    itemstack = icraftingrecipe.assemble(p_217066_3_);
                }
            }

            p_217066_4_.setItem(0, itemstack);
            serverplayerentity.connection.send(new SSetSlotPacket(p_217066_0_, 0, itemstack));
        }
    }

    public void slotsChanged(IInventory p_75130_1_) {
        this.access.execute((p_217069_1_, p_217069_2_) -> {
            slotChangedCraftingGrid(this.containerId, p_217069_1_, this.player, this.craftSlots, this.resultSlots);
        });
    }

    public void fillCraftSlotsStackedContents(RecipeItemHelper p_201771_1_) {
        this.craftSlots.fillStackedContents(p_201771_1_);
    }

    public void clearCraftingContent() {
        this.craftSlots.clearContent();
        this.resultSlots.clearContent();
    }

    public boolean recipeMatches(IRecipe<? super CraftingInventory> p_201769_1_) {
        return p_201769_1_.matches(this.craftSlots, this.player.level);
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return tileEntity.isUsableByPlayer(player);
    }

    public void removed(PlayerEntity p_75134_1_) {
        super.removed(p_75134_1_);
        this.access.execute((p_217068_2_, p_217068_3_) -> {
            this.clearContainer(p_75134_1_, p_217068_2_, this.craftSlots);
        });
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity entity, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == 0) {
                this.access.execute((p_217067_2_, p_217067_3_) -> {
                    itemstack1.getItem().onCraftedBy(itemstack1, p_217067_2_, entity);
                });
                if (!this.moveItemStackTo(itemstack1, 26, 62, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index >= 26 && index < 62) {
                if (!this.moveItemStackTo(itemstack1, 1, 26, false)) {
                    if (index < 53) {
                        if (!this.moveItemStackTo(itemstack1, 53, 62, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemstack1, 26, 53, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemstack1, 26, 62, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            ItemStack itemstack2 = slot.onTake(entity, itemstack1);
            if (index == 0) {
                entity.drop(itemstack2, false);
            }
        }

        return itemstack;
    }

    public boolean canTakeItemForPickAll(ItemStack p_94530_1_, Slot p_94530_2_) {
        return p_94530_2_.container != this.resultSlots && super.canTakeItemForPickAll(p_94530_1_, p_94530_2_);
    }

    public int getResultSlotIndex() {
        return 0;
    }

    public int getGridWidth() {
        return this.craftSlots.getWidth();
    }

    public int getGridHeight() {
        return this.craftSlots.getHeight();
    }

    @OnlyIn(Dist.CLIENT)
    public int getSize() {
        return 26;
    }

    @OnlyIn(Dist.CLIENT)
    public RecipeBookCategory getRecipeBookType() {
        return RecipeBookCategory.CRAFTING;
    }
}
