package nuparu.sevendaystomine.tileentity;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.ChestType;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.block.RowedContainer;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.util.ItemUtils;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityCarMaster extends TileEntityCar implements INamedContainerProvider, ILootTableProvider {
    private static final int INVENTORY_SIZE = 27;
    private static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.car");
    protected final LazyOptional<ItemHandlerNameable> inventory = LazyOptional.of(this::createInventory);

    @Nullable
    protected ResourceLocation lootTable;
    protected long lootTableSeed;

    protected int openCount;


    public TileEntityCarMaster() {
        super(ModTileEntities.CAR_MASTER.get());
    }

    @Override
    public TileEntityCarMaster getMaster() {
        return this;
    }

    protected ItemHandlerNameable createInventory() {
        return new ItemHandlerNameable(INVENTORY_SIZE, DEFAULT_NAME);
    }


    @Nullable
    public ItemHandlerNameable getInventory() {
        return this.inventory.orElse(null);
    }

    public NonNullList<ItemStack> getDrops() {
        return Utils.dropItemHandlerContents(getInventory(), level.random);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);
        if (!this.tryLoadLootTable(compound)) {
            if (getInventory() != null && compound.contains("ItemHandler")) {
                getInventory().deserializeNBT(compound.getCompound("ItemHandler"));
            }
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        if (!this.trySaveLootTable(compound)) {
            if (getInventory() != null) {
                compound.put("ItemHandler", getInventory().serializeNBT());
            }
        }
        return compound;
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return inventory.cast();
        }
        return super.getCapability(cap, side);
    }

    public ResourceLocation getLootTable() {
        return null;
    }

    public boolean tryLoadLootTable(CompoundNBT p_184283_1_) {
        if (p_184283_1_.contains("LootTable", 8)) {
            this.lootTable = new ResourceLocation(p_184283_1_.getString("LootTable"));
            this.lootTableSeed = p_184283_1_.getLong("LootTableSeed");
            return true;
        } else {
            return false;
        }
    }

    public boolean trySaveLootTable(CompoundNBT p_184282_1_) {
        if (this.lootTable == null) {
            return false;
        } else {
            p_184282_1_.putString("LootTable", this.lootTable.toString());
            if (this.lootTableSeed != 0L) {
                p_184282_1_.putLong("LootTableSeed", this.lootTableSeed);
            }

            return true;
        }
    }

    public void unpackLootTable(@Nullable PlayerEntity p_184281_1_) {
        if (this.lootTable != null && this.level.getServer() != null) {
            LootTable loottable = this.level.getServer().getLootTables().get(this.lootTable);
            if (p_184281_1_ instanceof ServerPlayerEntity) {
                CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayerEntity)p_184281_1_, this.lootTable);
            }

            this.lootTable = null;
            LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld)this.level)).withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf(this.worldPosition)).withOptionalRandomSeed(this.lootTableSeed);
            if (p_184281_1_ != null) {
                lootcontext$builder.withLuck(p_184281_1_.getLuck()).withParameter(LootParameters.THIS_ENTITY, p_184281_1_);
            }

            ItemUtils.fill(loottable,this.getInventory(), lootcontext$builder.create(LootParameterSets.CHEST));
        }

    }

    public void setLootTable(ResourceLocation p_189404_1_, long p_189404_2_) {
        this.lootTable = p_189404_1_;
        this.lootTableSeed = p_189404_2_;
    }

    public void setLootTable(ResourceLocation p_189404_1_) {
        this.lootTable = p_189404_1_;
    }

    public boolean isUsableByPlayer(PlayerEntity player) {
        return this.level.getBlockEntity(this.worldPosition) == this && player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64;
    }


    @Override
    public ITextComponent getDisplayName() {
        return this.getInventory().getDisplayName();
    }

    public void setDisplayName(ITextComponent displayName) {
        getInventory().setDisplayName(displayName);
    }

    public void setDisplayName(String displayName) {
        getInventory().setDisplayName(new StringTextComponent(displayName));
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return RowedContainer.createContainerServerSide(windowId, playerInventory,this);
    }


    @OnlyIn(Dist.CLIENT)
    public double getViewDistance() {
        return 128.0D;
    }


    public void startOpen(PlayerEntity player) {
        if (this.openCount == 0) {
            this.level.playSound((PlayerEntity)null, worldPosition.getX()+0.5, worldPosition.getY()+0.5, worldPosition.getZ()+0.5, ModSounds.CAR_OPEN.get(), SoundCategory.BLOCKS, MathUtils.getFloatInRange(0.45f,0.55f), MathUtils.getFloatInRange(0.75f,1.15f));
        }
        this.openCount++;
    }

    public void stopOpen(PlayerEntity player) {
        this.openCount--;
        if (this.openCount == 0) {
            this.level.playSound((PlayerEntity)null, worldPosition.getX()+0.5, worldPosition.getY()+0.5, worldPosition.getZ()+0.5, ModSounds.CAR_CLOSE.get(), SoundCategory.BLOCKS, MathUtils.getFloatInRange(0.45f,0.55f), MathUtils.getFloatInRange(0.75f,1.15f));
        }
    }
}
