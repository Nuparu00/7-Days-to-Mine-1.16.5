package nuparu.sevendaystomine.inventory.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.block.BlockChemistryStation;
import nuparu.sevendaystomine.block.BlockCombustionGenerator;
import nuparu.sevendaystomine.block.BlockGenerator;
import nuparu.sevendaystomine.block.BlockWorkbench;
import nuparu.sevendaystomine.init.ModContainers;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.inventory.slot.SlotWorkbenchScrap;
import nuparu.sevendaystomine.inventory.slot.SlotWorkbenchUncraftingResult;
import nuparu.sevendaystomine.item.*;
import nuparu.sevendaystomine.tileentity.TileEntityWorkbench;
import nuparu.sevendaystomine.util.ItemUtils;
import nuparu.sevendaystomine.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class ContainerWorkbenchUncrafting extends Container {

    private UncraftingInventory craftSlots = new UncraftingInventory(this,1,1);
    private UncraftResultInventory resultSlots = new UncraftResultInventory();
    public final PlayerEntity player;
    private final IWorldPosCallable access;

    private final World world;
    TileEntityWorkbench tileEntity;

    protected ContainerWorkbenchUncrafting(int windowID, PlayerInventory invPlayer, TileEntityWorkbench tileEntity, BlockPos pos) {
        super(ModContainers.WORKBENCH_UNCRAFTING.get(), windowID);
        this.world = invPlayer.player.level;
        this.tileEntity = tileEntity;
        this.player = invPlayer.player;
        craftSlots = new UncraftingInventory(this,1,1);
        resultSlots = new UncraftResultInventory();
        this.access = IWorldPosCallable.create(invPlayer.player.level,pos);

        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                this.addSlot(new SlotWorkbenchUncraftingResult(invPlayer.player,craftSlots,resultSlots, j + i * 5, 80 + j * 18, 7 + i * 18,this));
            }
        }

        this.addSlot(new Slot(this.craftSlots, 0, 26, 44));


        this.addSlot(new SlotWorkbenchScrap(tileEntity.getInventory(), 0, 26, 70, this));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 106 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            addSlot(new Slot(invPlayer, k, 8 + k * 18, 164));
        }
    }

    public static ContainerWorkbenchUncrafting createContainerClientSide(int windowID, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        BlockPos pos = packetBuffer.readBlockPos();
        return new ContainerWorkbenchUncrafting(windowID,playerInventory, (TileEntityWorkbench) playerInventory.player.level.getBlockEntity(pos),pos);
    }

    public static ContainerWorkbenchUncrafting createContainerServerSide(int windowID, PlayerInventory playerInventory, TileEntityWorkbench tileEntity, BlockPos pos){
        return new ContainerWorkbenchUncrafting(windowID, playerInventory, tileEntity,pos);
    }

    public void slotsChanged(IInventory p_75130_1_) {
        this.access.execute((p_217069_1_, p_217069_2_) -> updateCraftingGrid(world, player, craftSlots, resultSlots,this.tileEntity.getInventory().getStackInSlot(0)));
    }

    public void clearCraftingContent() {
        this.craftSlots.clearContent();
        this.resultSlots.clearContent();
    }

    public void onScrapChanged(ItemStack scrap) {
        this.access.execute((p_217069_1_, p_217069_2_) -> updateCraftingGrid(world, player, craftSlots, resultSlots,scrap));
    }

    protected void slotChangedCraftingGrid(World world, PlayerEntity player, UncraftingInventory input,
                                           UncraftResultInventory output) {
        updateCraftingGrid(world, player, input, output, this.tileEntity.getInventory().getStackInSlot(0));
    }

    protected void updateCraftingGrid(World world, PlayerEntity player, UncraftingInventory input,
                                      UncraftResultInventory output, ItemStack scrap) {
        /*if (!world.isClientSide()) {
            ServerPlayerEntity entityplayermp = (ServerPlayerEntity) player;
            Object[] items = ForgeRegistries.ITEMS.getValues().toArray();
            for (int m = 0; m < 25; m++) {
                ItemStack stack = new ItemStack((IItemProvider) items[world.random.nextInt(items.length)]);
                entityplayermp.connection.send(new SSetSlotPacket(this.containerId, m, stack));
                this.setItem(m, stack);
            }
        }
        if(true) return;*/

        if (!world.isClientSide()) {
            ServerPlayerEntity entityplayermp = (ServerPlayerEntity) player;
            if (scrap.isEmpty()) {
                for (int m = 0; m < 25; m++) {
                    //output.setItem(m, ItemStack.EMPTY);
                    entityplayermp.connection.send(new SSetSlotPacket(this.containerId, m, ItemStack.EMPTY));
                    this.setItem(m,ItemStack.EMPTY);
                }
                return;
            }
            ItemStack itemstack = input.getItem(0);
            if(!isUncraftable(itemstack)) {
                for (int m = 0; m < 25; m++) {
                    //output.setItem(m, ItemStack.EMPTY);
                    entityplayermp.connection.send(new SSetSlotPacket(this.containerId, m, ItemStack.EMPTY));
                    this.setItem(m,ItemStack.EMPTY);
                }
                return;
            }
            IRecipe irecipe = Utils.getRecipesForStack(itemstack, world.getServer());
            if (irecipe != null) {
                NonNullList<Ingredient> list = irecipe.getIngredients();
                int i = 3;
                int j = irecipe instanceof net.minecraftforge.common.crafting.IShapedRecipe
                        ? Math.max(3, ((net.minecraftforge.common.crafting.IShapedRecipe) irecipe).getRecipeHeight())
                        : i;
                int k = irecipe instanceof net.minecraftforge.common.crafting.IShapedRecipe
                        ? ((net.minecraftforge.common.crafting.IShapedRecipe) irecipe).getRecipeWidth()
                        : i;
                int l = 1;

                int qualityItems = 0;
                List<ItemStack> items = new ArrayList<ItemStack>();
                for (int m = 0; m < 25; m++) {
                    ItemStack stack = ItemStack.EMPTY;
                    if (list.size() > m) {
                        Ingredient ingredient = list.get(m);
                        if (ingredient.getItems().length > 0) {
                            stack = ingredient.getItems()[0].copy();
                            if(ItemUtils.isQualityItem(stack)) {
                                qualityItems++;
                            }
                        }
                    }
                    items.add(stack);
                }

                int originalQuality = -1;
                if (itemstack.getItem() instanceof IQuality) {
                    IQuality quality = (IQuality) itemstack.getItem();
                    originalQuality = quality.getQuality(itemstack);
                }
                else{
                    originalQuality = ItemQuality.getQualityForPlayer(player);
                }

                for (int m = 0; m < 25; m++) {
                    ItemStack stack = items.get(m);
                    if (originalQuality > 0 && stack.getItem() instanceof IQuality) {
                        IQuality quality = (IQuality) stack.getItem();
                        quality.setQuality(stack,
                                Math.max(1, (int) (originalQuality * 0.75f - world.random.nextInt(10))));
                    }

                    //output.setItem(m, stack);
                    entityplayermp.connection.send(new SSetSlotPacket(this.containerId, m, stack));
                    this.setItem(m,stack);
                }

            }
        }
    }

    public static boolean isUncraftable(ItemStack stack) {
        if (stack.isEmpty())
            return false;
        Item item = stack.getItem();
        if (item instanceof ItemGun) {
            return true;
        } else if (item instanceof BlockItem) {
            BlockItem itemBlock = (BlockItem) item;
            Block block = itemBlock.getBlock();
            if (block instanceof BlockGenerator) {
                return true;
            }
            if (block instanceof BlockWorkbench) {
                return true;
            }
            if (block instanceof BlockCombustionGenerator) {
                return true;
            }
            if (block instanceof BlockChemistryStation) {
                return true;
            }
        } else if (item instanceof ItemBullet) {
            return true;
        } else if (item instanceof ItemFuelTool) {
            return true;
        }
        return false;
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return tileEntity.isUsableByPlayer(player);
    }

    public void removed(PlayerEntity p_75134_1_) {
        super.removed(p_75134_1_);
        this.access.execute((p_217068_2_, p_217068_3_) -> this.clearContainer(p_75134_1_, p_217068_2_, this.craftSlots));
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity entity, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index == 0) {
                // itemstack1.getItem().onCreated(itemstack1, this.worldObj, playerIn);

                if (!this.moveItemStackTo(itemstack1, 27, 63, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index >= 27 && index < 63) {
                if (itemstack1.getItem() == ModItems.IRON_SCRAP.get()) {
                    if (!this.moveItemStackTo(itemstack1, 26, 27, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }

            } else if (index == 26) {
                if (!this.moveItemStackTo(itemstack1, 27, 63, true)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            ItemStack itemstack2 = slot.onTake(player, itemstack1);

            if (index == 0) {
                player.drop(itemstack2, false);
            }
        }

        return itemstack;
    }

    public boolean canTakeItemForPickAll(ItemStack p_94530_1_, Slot p_94530_2_) {
        return super.canTakeItemForPickAll(p_94530_1_, p_94530_2_);
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

    public TileEntityWorkbench getTileEntity(){
        return tileEntity;
    }
}
