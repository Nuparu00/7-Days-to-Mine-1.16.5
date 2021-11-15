package nuparu.sevendaystomine.tileentity;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import nuparu.sevendaystomine.crafting.grill.IGrillRecipe;
import nuparu.sevendaystomine.crafting.separator.ISeparatorRecipe;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.EnumDeviceType;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.init.ModRecipeSerializers;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.block.ContainerSeparator;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.util.ModConstants;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntitySeparator extends TileEntityItemHandler<ItemHandlerNameable> implements ITickableTileEntity, IVoltage, IInventory {

    private static final int INVENTORY_SIZE = 3;
    private static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.separator");
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
    public ISeparatorRecipe<TileEntitySeparator> currentRecipe = null;
    private long voltage = 0;
    private long capacity = 200;
    private int cookTime;
    private int totalCookTime;
    public final IIntArray dataAccess = new IIntArray() {
        public int get(int p_221476_1_) {
            switch (p_221476_1_) {
                case 0:
                    return TileEntitySeparator.this.cookTime;
                case 1:
                    return TileEntitySeparator.this.totalCookTime;
                default:
                    return 0;
            }
        }

        public void set(int p_221477_1_, int p_221477_2_) {
            switch (p_221477_1_) {
                case 0:
                    TileEntitySeparator.this.cookTime = p_221477_2_;
                    break;
                case 1:
                    TileEntitySeparator.this.totalCookTime = p_221477_2_;
            }

        }

        public int getCount() {
            return 2;
        }
    };

    public TileEntitySeparator() {
        super(ModTileEntities.SEPARATOR.get());
    }

    @Override
    protected ItemHandlerNameable createInventory() {
        return new ItemHandlerNameable(INVENTORY_SIZE, DEFAULT_NAME);
    }

    @Override
    public void onContainerOpened(PlayerEntity player) {

    }

    @Override
    public void onContainerClosed(PlayerEntity player) {

    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return this.level.getBlockEntity(this.worldPosition) == this && player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64;
    }

    @Override
    public ResourceLocation getLootTable() {
        return null;
    }

    @Override
    public void tick() {
        boolean flag1 = false;

        if (!getInput().isEmpty()) {
            if (this.canSmelt()) {
                ++this.cookTime;
                if (this.totalCookTime == 0) {
                    this.totalCookTime = this.getCookTime(null);
                } else if (this.cookTime >= this.totalCookTime) {
                    this.cookTime = 0;
                    this.totalCookTime = this.getCookTime(null);
                    this.smeltItem();
                    flag1 = true;
                }
            } else {
                this.cookTime = 0;
            }
        } else if (!this.isBurning() && this.cookTime > 0) {
            this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
        }

        if (flag1) {
            this.setChanged();
        }

        markForUpdate();
    }

    public void markForUpdate() {
        level.sendBlockUpdated(worldPosition, level.getBlockState(this.worldPosition),
                level.getBlockState(worldPosition), 3);
        setChanged();
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbtTag = new CompoundNBT();
        this.save(nbtTag);
        return new SUpdateTileEntityPacket(this.worldPosition, 0, nbtTag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return save(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getTag();
        load(level.getBlockState(pkt.getPos()), tag);
        if (hasLevel()) {
            level.sendBlockUpdated(pkt.getPos(), level.getBlockState(this.worldPosition),
                    level.getBlockState(pkt.getPos()), 2);
        }
    }

    private boolean canSmelt() {


        ISeparatorRecipe<TileEntitySeparator> irecipe = this.level.getRecipeManager().getRecipeFor(ModRecipeSerializers.SEPARATOR.getA(), this, this.level).orElse(null);

        if (irecipe != null) {
            this.currentRecipe = irecipe;
            return true;
        }

        return false;
    }

    public void smeltItem() {
        if (currentRecipe != null) {
            Pair<ItemStack, ItemStack> result = currentRecipe.assemblePair(this);
            ItemStack leftOutput = getOutputLeft();
            if (leftOutput.isEmpty()) {
                this.getInventory().setStackInSlot(1, result.getLeft());
            } else if (ItemStack.isSame(leftOutput, result.getLeft())
                    && leftOutput.getCount() + result.getLeft().getCount() <= Math.min(
                    getOutputLeft().getItem().getItemStackLimit(getOutputLeft()),
                    this.getInventory().getStackInSlot(1).getMaxStackSize())) {
                leftOutput.grow(result.getLeft().getCount());
                this.getInventory().setStackInSlot(1, leftOutput);
            }

            ItemStack rightOutput = getOutputRight();
            if (rightOutput.isEmpty()) {
                this.getInventory().setStackInSlot(2, result.getRight());
            } else if (ItemStack.isSame(rightOutput, result.getRight())
                    && rightOutput.getCount() + result.getRight().getCount() <= Math.min(
                    getOutputRight().getItem().getItemStackLimit(getOutputRight()),
                    this.getInventory().getStackInSlot(2).getMaxStackSize())) {
                rightOutput.grow(result.getRight().getCount());
                this.getInventory().setStackInSlot(2, rightOutput);
            }

            ResourceLocation resourcelocation = currentRecipe.getId();
            this.recipesUsed.addTo(resourcelocation, 1);
            consumeInput();
        }
    }

    public void consumeInput() {
        ItemStack stack = this.getInput();
        stack.shrink(1);
        if (stack.isEmpty()) {
            getInventory().setStackInSlot(0, ItemStack.EMPTY);
        }
    }

    public boolean isBurning() {
        return true;
    }

    public int getCookTime(ItemStack stack) {
        return currentRecipe == null ? 600 : currentRecipe.getCookingTime();
    }

    public void setRecipeUsed(@Nullable IRecipe<?> p_193056_1_) {
        if (p_193056_1_ != null) {
            ResourceLocation resourcelocation = p_193056_1_.getId();
            this.recipesUsed.addTo(resourcelocation, 1);
        }

    }


    public void awardUsedRecipesAndPopExperience(PlayerEntity p_235645_1_) {
        List<IRecipe<?>> list = this.getRecipesToAwardAndPopExperience(p_235645_1_.level, p_235645_1_.position());
        p_235645_1_.awardRecipes(list);
        this.recipesUsed.clear();
    }

    public List<IRecipe<?>> getRecipesToAwardAndPopExperience(World p_235640_1_, Vector3d p_235640_2_) {
        List<IRecipe<?>> list = Lists.newArrayList();

        for(Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
            p_235640_1_.getRecipeManager().byKey(entry.getKey()).ifPresent((p_235642_4_) -> {
                list.add(p_235642_4_);
                createExperience(p_235640_1_, p_235640_2_, entry.getIntValue(), ((IGrillRecipe)p_235642_4_).getExperience());
            });
        }

        return list;
    }

    private static void createExperience(World p_235641_0_, Vector3d p_235641_1_, int p_235641_2_, float p_235641_3_) {
        int i = MathHelper.floor((float)p_235641_2_ * p_235641_3_);
        float f = MathHelper.frac((float)p_235641_2_ * p_235641_3_);
        if (f != 0.0F && Math.random() < (double)f) {
            ++i;
        }

        while(i > 0) {
            int j = ExperienceOrbEntity.getExperienceValue(i);
            i -= j;
            p_235641_0_.addFreshEntity(new ExperienceOrbEntity(p_235641_0_, p_235641_1_.x, p_235641_1_.y, p_235641_1_.z, j));
        }

    }

    private BlockState getState() {
        return level.getBlockState(worldPosition);
    }

    public ItemStack getInput() {
        return getInventory().getStackInSlot(0);
    }

    public ItemStack getOutputLeft() {
        return getInventory().getStackInSlot(1);
    }

    public ItemStack getOutputRight() {
        return getInventory().getStackInSlot(2);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);

        this.cookTime = compound.getInt("CookTime");
        this.totalCookTime = compound.getInt("CookTimeTotal");

        CompoundNBT compoundnbt = compound.getCompound("RecipesUsed");
        for (String s : compoundnbt.getAllKeys()) {
            this.recipesUsed.put(new ResourceLocation(s), compoundnbt.getInt(s));
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putInt("CookTime", (short) this.cookTime);
        compound.putInt("CookTimeTotal", (short) this.totalCookTime);
        CompoundNBT compoundnbt = new CompoundNBT();
        this.recipesUsed.forEach((p_235643_1_, p_235643_2_) -> compoundnbt.putInt(p_235643_1_.toString(), p_235643_2_));
        compound.put("RecipesUsed", compoundnbt);

        return compound;
    }

    public int getCookTime() {
        return cookTime;
    }

    public int getTotalCookTime() {
        return totalCookTime;
    }

    @Override
    public EnumDeviceType getDeviceType() {
        return EnumDeviceType.CONSUMER;
    }

    @Override
    public int getMaximalInputs() {
        return 0;
    }

    @Override
    public int getMaximalOutputs() {
        return 0;
    }

    @Override
    public List<ElectricConnection> getInputs() {
        return null;
    }

    @Override
    public List<ElectricConnection> getOutputs() {
        return null;
    }

    @Override
    public long getOutput() {
        return 0;
    }

    @Override
    public long getMaxOutput() {
        return 0;
    }

    @Override
    public long getOutputForConnection(ElectricConnection connection) {
        return 0;
    }

    @Override
    public boolean tryToConnect(ElectricConnection connection) {
        return false;
    }

    @Override
    public boolean canConnect(ElectricConnection connection) {
        return false;
    }

    @Override
    public long getRequiredPower() {
        return 20;
    }

    @Override
    public long getCapacity() {
        return this.capacity;
    }

    @Override
    public long getVoltageStored() {
        return this.voltage;
    }

    @Override
    public void storePower(long power) {
        this.voltage += power;
        if (this.voltage > this.getCapacity()) {
            this.voltage = this.getCapacity();
        }
        if (this.voltage < 0) {
            this.voltage = 0;
        }
    }

    @Override
    public long tryToSendPower(long power, ElectricConnection connection) {
        long canBeAdded = capacity - voltage;
        long delta = Math.min(canBeAdded, power);
        long lost = 0;
        if (connection != null) {
            lost = Math.round(delta * ModConstants.DROP_PER_BLOCK * connection.getDistance());
        }
        long realDelta = delta - lost;
        this.voltage += realDelta;

        return delta;
    }

    @Override
    public Vector3d getWireOffset() {
        return null;
    }

    @Override
    public boolean isPassive() {
        return true;
    }

    @Override
    public boolean disconnect(IVoltage voltage) {
        return false;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        long toAdd = Math.min(this.capacity - this.voltage, maxReceive);
        if (!simulate) {
            this.voltage += toAdd;
        }
        return (int) toAdd;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        long toExtract = Math.min(this.voltage, maxExtract);
        if (!simulate) {
            this.voltage -= toExtract;
        }
        return (int) toExtract;
    }

    @Override
    public int getEnergyStored() {
        return (int) this.voltage;
    }

    @Override
    public int getMaxEnergyStored() {
        return (int) this.capacity;
    }

    @Override
    public boolean canExtract() {
        return this.capacity > 0;
    }

    @Override
    public boolean canReceive() {
        return this.voltage < this.capacity;
    }

    @Override
    public ITextComponent getDisplayName() {
        return this.getInventory().hasCustomName() ? this.getInventory().getDisplayName() : DEFAULT_NAME;
    }

    public void setDisplayName(String displayName) {
        getInventory().setDisplayName(new StringTextComponent(displayName));
    }

    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return ContainerSeparator.createContainerServerSide(windowID, playerInventory, this);
    }

    @Override
    public BlockPos getPos() {
        return worldPosition;
    }

    /*
	DONT USE ANY OF THESE - WE HAVE TO IMPLEMENT IINVENTORY BECAUSE OF THE IRECIPE
 	*/
    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int i) {
        return this.getInput();
    }

    @Override
    public ItemStack removeItem(int i, int count) {
        ItemStack stack = getItem(i);
        stack.shrink(count);
        if (stack.isEmpty()) {
            this.getInventory().setStackInSlot(i, ItemStack.EMPTY);
            return ItemStack.EMPTY;
        }
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {

        ItemStack stack = getItem(i);
        this.getInventory().setStackInSlot(i, ItemStack.EMPTY);
        return stack;

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
}
