package nuparu.sevendaystomine.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.block.ContainerTiny;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;

import java.util.List;
import java.util.stream.Collectors;

public class TileEntityTrashBin extends TileEntityItemHandler<ItemHandlerNameable>
        implements ITickableTileEntity, IHopper {

    private static final int INVENTORY_SIZE = 1;
    private static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.trash_bin");

    static VoxelShape INSIDE = Block.box(2.0D, 11.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    static VoxelShape ABOVE = Block.box(0.0D, 16.0D, 0.0D, 16.0D, 32.0D, 16.0D);
    static VoxelShape SUCK = VoxelShapes.or(INSIDE, ABOVE);

    public TileEntityTrashBin() {
        super(ModTileEntities.TRASH_BIN.get());
    }

    public static List<ItemEntity> getItemsAtAndAbove(IHopper p_200115_0_) {
        return p_200115_0_.getSuckShape().toAabbs().stream().flatMap((p_200110_1_) -> p_200115_0_.getLevel().getEntitiesOfClass(ItemEntity.class, p_200110_1_.move(p_200115_0_.getLevelX() - 0.5D, p_200115_0_.getLevelY() - 0.5D, p_200115_0_.getLevelZ() - 0.5D), EntityPredicates.ENTITY_STILL_ALIVE).stream()).collect(Collectors.toList());
    }

    @Override
    protected ItemHandlerNameable createInventory() {
        return new ItemHandlerNameable(INVENTORY_SIZE, DEFAULT_NAME);
    }

    @Override
    public void tick() {
        if (!level.isClientSide()) {
            if (inventoryFull()) return;
            List<ItemEntity> entities = getItemsAtAndAbove(this);
            ItemStack stack = this.getInventory().getStackInSlot(0);
            for (ItemEntity entity : entities) {
                ItemStack entityStack = entity.getItem().copy();
                if (entityStack.isEmpty()) {
                    continue;
                }
                if (isEmpty()) {
                    this.getInventory().setStackInSlot(0, entityStack);
                    entity.kill();
                } else if (entityStack.getItem() == stack.getItem()
                        && entityStack.getDamageValue() == stack.getDamageValue()
                        && ItemStack.tagMatches(entityStack, stack)) {
                    int maxDelta = Math.min(this.getInventory().getSlotLimit(0), this.getInventory().getStackInSlot(0).getMaxStackSize()) - stack.getCount();
                    if(maxDelta <= 0) continue;
                    int amount = Math.min(maxDelta, entityStack.getCount());
                    entityStack.shrink(amount);
                    entity.setItem(entityStack);
                    stack.grow(amount);
                    if (entityStack.getCount() <= 0) {
                        entity.kill();
                    }
                }
            }
        }

    }

    @Override
    public void onContainerOpened(PlayerEntity player) {

    }

    @Override
    public void onContainerClosed(PlayerEntity player) {

    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return this.level.getBlockEntity(this.worldPosition) == this
                && player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5,
                this.worldPosition.getZ() + 0.5) <= 64;
    }

    @Override
    public ResourceLocation getLootTable() {
        return null;
    }

    @Override
    public ITextComponent getDisplayName() {
        return this.getInventory().getDisplayName();
    }

    public void setDisplayName(String displayName) {
        getInventory().setDisplayName(new StringTextComponent(displayName));
    }

    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return ContainerTiny.createContainerServerSide(windowID, playerInventory, this);
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return this.getInventory().getStackInSlot(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int i) {
        return this.getInventory().getStackInSlot(i);
    }

    @Override
    public ItemStack removeItem(int p_70298_1_, int p_70298_2_) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_70304_1_) {
        return null;
    }

    @Override
    public void setItem(int i, ItemStack stack) {
        this.getInventory().setStackInSlot(i, stack);
    }

    @Override
    public boolean stillValid(PlayerEntity p_70300_1_) {
        return false;
    }

    @Override
    public void clearContent() {
    }

    @Override
    public double getLevelX() {
        return worldPosition.getX() + 0.5D;
    }

    @Override
    public double getLevelY() {
        return worldPosition.getY() + 0.5;
    }

    @Override
    public double getLevelZ() {
        return worldPosition.getZ() + 0.5D;
    }

    private boolean inventoryFull() {
        ItemStack itemStack = this.getInventory().getStackInSlot(0);
        return (!itemStack.isEmpty() && itemStack.getCount() >= itemStack.getMaxStackSize());
    }

}
