package nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import nuparu.sevendaystomine.crafting.forge.ForgeRecipeManager;
import nuparu.sevendaystomine.crafting.forge.ForgeResult;
import nuparu.sevendaystomine.crafting.forge.IForgeRecipe;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.IContainerCallbacks;
import nuparu.sevendaystomine.inventory.block.ContainerForge;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.item.ItemMold;

public class TileEntityForge extends TileEntityItemHandler<ItemHandlerNameable>
		implements INamedContainerProvider, IContainerCallbacks, ITickableTileEntity {

	public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.forge");

	public int burnTime;
	public int currentItemBurnTime;
	public int cookTime;
	public int totalCookTime;

	public enum EnumSlots {
		INPUT_SLOT, INPUT_SLOT2, INPUT_SLOT3, INPUT_SLOT4, OUTPUT_SLOT, FUEL_SLOT, MOLD_SLOT
	}

	@Override
	protected ItemHandlerNameable createInventory() {
		return new ItemHandlerNameable(EnumSlots.values().length, DEFAULT_NAME);
	}

	public final IIntArray dataAccess = new IIntArray() {
		public int get(int p_221476_1_) {
			switch (p_221476_1_) {
			case 0:
				return TileEntityForge.this.burnTime;
			case 1:
				return TileEntityForge.this.currentItemBurnTime;
			case 2:
				return TileEntityForge.this.cookTime;
			case 3:
				return TileEntityForge.this.totalCookTime;
			default:
				return 0;
			}
		}

		public void set(int p_221477_1_, int p_221477_2_) {
			switch (p_221477_1_) {
			case 0:
				TileEntityForge.this.burnTime = p_221477_2_;
				break;
			case 1:
				TileEntityForge.this.currentItemBurnTime = p_221477_2_;
				break;
			case 2:
				TileEntityForge.this.cookTime = p_221477_2_;
				break;
			case 3:
				TileEntityForge.this.totalCookTime = p_221477_2_;
			}

		}

		public int getCount() {
			return 4;
		}
	};

	private IForgeRecipe currentRecipe = null;
	private ForgeResult currentResult = null;



	public TileEntityForge() {
		super(ModTileEntities.FORGE.get());

	}

	public boolean canPlayerAccessInventory(PlayerEntity player) {
		if (this.level.getBlockEntity(this.worldPosition) != this)
			return false;
		final double X_CENTRE_OFFSET = 0.5;
		final double Y_CENTRE_OFFSET = 0.5;
		final double Z_CENTRE_OFFSET = 0.5;
		final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
		return player.distanceToSqr(worldPosition.getX() + X_CENTRE_OFFSET, worldPosition.getY() + Y_CENTRE_OFFSET,
				worldPosition.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
	}

	@Override
	public void tick() {
		boolean flag = this.isBurning();
		boolean flag1 = false;

		if (this.isBurning()) {
			--this.burnTime;
		}

		if (!this.level.isClientSide()) {
			ItemStack itemstack = this.getFuelSlot();
			if (this.isBurning() || hasMold() && !isInputEmpty()) {
				if (!this.isBurning() && this.canSmelt()) {
					this.burnTime = ForgeHooks.getBurnTime(itemstack);
					this.currentItemBurnTime = this.burnTime;

					if (this.isBurning()) {
						flag1 = true;

						if (!itemstack.isEmpty()) {
							Item item = itemstack.getItem();
							itemstack.shrink(1);

							if (itemstack.isEmpty()) {
								ItemStack item1 = item.getContainerItem(itemstack);
								this.getInventory().setStackInSlot(EnumSlots.FUEL_SLOT.ordinal(),item1);
							}
						}
					}
					this.totalCookTime = this.getCookTime(null);
				}

				if (this.isBurning() && this.canSmelt()) {
					++this.cookTime;
					if (this.cookTime >= this.totalCookTime) {
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

			if (flag != this.isBurning()) {
				flag1 = true;
				this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition)
						.setValue(AbstractFurnaceBlock.LIT, Boolean.valueOf(this.isBurning())), 3);
			}
		}

		if (flag1) {
			this.setChanged();
		}

	}

	private boolean canSmelt() {

		if (!getOutputSlot().isEmpty()
				&& getOutputSlot().getCount() > Math.min(getOutputSlot().getItem().getItemStackLimit(getOutputSlot()),
						this.getInventory().getStackInSlot(EnumSlots.OUTPUT_SLOT.ordinal()).getMaxStackSize()))
			return false;
		if (isInputEmpty() || !hasMold())
			return false;

		for (IForgeRecipe recipe : ForgeRecipeManager.getInstance().getRecipes()) {
			ForgeResult result = recipe.matches(this, this.level);
			if (result.matches) {
				ItemStack output = recipe.getOutput(this);
				if (!getOutputSlot().isEmpty() && !ItemStack.isSame(getOutputSlot(), output)) {
					continue;
				}
				if (!ItemStack.isSameIgnoreDurability(getMoldSlot(), recipe.getMold())) {
					continue;
				}

				if (getOutputSlot().getCount() + output.getCount() <= Math.min(
						getOutputSlot().getItem().getItemStackLimit(getOutputSlot()),
						this.getInventory().getStackInSlot(EnumSlots.OUTPUT_SLOT.ordinal()).getMaxStackSize())) {
					currentRecipe = recipe;
					currentResult = result;
					return true;
				}
			}
		}
		return false;
	}

	public void smeltItem() {
		IForgeRecipe recipeToUse = null;
		ForgeResult resultToUse = null;
		for (IForgeRecipe recipe : ForgeRecipeManager.getInstance().getRecipes()) {
			ForgeResult result = recipe.matches(this, this.level);
			if (result.matches) {
				if (ItemStack.isSameIgnoreDurability(getMoldSlot(), recipe.getMold())) {
					recipeToUse = recipe;
					resultToUse = result;
					break;
				}
			}
		}
		if (recipeToUse != null) {

			ItemStack currentOutput = getOutputSlot();
			if (ItemStack.isSameIgnoreDurability(getMoldSlot(), recipeToUse.getMold())) {
				ItemStack mold = getMoldSlot();
				if (currentOutput.isEmpty()) {
					this.getInventory().setStackInSlot(EnumSlots.OUTPUT_SLOT.ordinal(), recipeToUse.getOutput(this));

				} else {
					if (ItemStack.isSame(currentOutput, recipeToUse.getOutput(this))
							&& currentOutput.getCount() + recipeToUse.getOutput(this).getCount() <= Math.min(
									getOutputSlot().getItem().getItemStackLimit(getOutputSlot()),
							this.getInventory().getStackInSlot(EnumSlots.OUTPUT_SLOT.ordinal()).getMaxStackSize())) {

						currentOutput.grow(recipeToUse.getOutput(this).getCount());
						mold.hurt(1, level.random, null);

						this.getInventory().setStackInSlot(EnumSlots.OUTPUT_SLOT.ordinal(), currentOutput);
					}
				}
				mold.hurt(1, level.random, null);
				if (mold.getDamageValue() >= mold.getMaxDamage()) {
					if (mold.getCount() > 0) {
						mold.shrink(1);
					} else {
						this.getInventory().setStackInSlot(EnumSlots.MOLD_SLOT.ordinal(),  ItemStack.EMPTY);
					}
				}
				consumeInput(recipeToUse);
			}
		}
	}

	public void consumeInput(IForgeRecipe recipe) {
		List<ItemStack> left = recipe.consumeInput(this);
		if (left != null && !left.isEmpty()) {
			for (ItemStack stack : left) {
				for (int i = EnumSlots.INPUT_SLOT.ordinal(); i < EnumSlots.INPUT_SLOT4.ordinal(); i++) {
					ItemStack slot = getIntputSlot(i);
					if (slot.isEmpty()) {
						this.getInventory().setStackInSlot(i, stack.copy());
						stack = ItemStack.EMPTY;
						break;
					}
					if (ItemStack.isSame(stack, slot) && slot.getCount() < slot.getMaxStackSize()) {
						int delta = Math.min(slot.getMaxStackSize() - slot.getCount(), stack.getCount());
						slot.grow(delta);
						stack.shrink(delta);
					}
				}
				if (!stack.isEmpty()) {
					InventoryHelper.dropItemStack(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1,
							worldPosition.getZ() + 0.5, stack);
				}
			}
		}
	}

	public boolean isInputEmpty() {
		for (int i = EnumSlots.INPUT_SLOT.ordinal(); i < EnumSlots.INPUT_SLOT4.ordinal(); i++) {
			if (!this.getInventory().getStackInSlot(i).isEmpty())
				return false;
		}
		return true;
	}

	public boolean hasMold() {
		return !this.getInventory().getStackInSlot(EnumSlots.MOLD_SLOT.ordinal()).isEmpty();
	}

	public boolean hasFuel() {
		return  !this.getInventory().getStackInSlot(EnumSlots.FUEL_SLOT.ordinal()).isEmpty();
	}

	public ItemStack getOutputSlot() {
		return this.getInventory().getStackInSlot(EnumSlots.OUTPUT_SLOT.ordinal());
	}

	public ItemStack getMoldSlot() {
		return this.getInventory().getStackInSlot(EnumSlots.MOLD_SLOT.ordinal());
	}

	public ItemStack getFuelSlot() {
		return this.getInventory().getStackInSlot(EnumSlots.FUEL_SLOT.ordinal());
	}

	public ItemStack getIntputSlot(int i) {
		return this.getInventory().getStackInSlot(EnumSlots.INPUT_SLOT.ordinal()+i);
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);

		this.burnTime = compound.getInt("BurnTime");
		this.cookTime = compound.getInt("CookTime");
		this.totalCookTime = compound.getInt("CookTimeTotal");
		this.currentItemBurnTime = ForgeHooks.getBurnTime(getFuelSlot());
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);

		compound.putInt("BurnTime", (short) this.burnTime);
		compound.putInt("CookTime", (short) this.cookTime);
		compound.putInt("CookTimeTotal", (short) this.totalCookTime);

		return compound;
	}

	public int getCookTime(ItemStack stack) {
		return 600;
	}

	public List<ItemStack> getActiveInventory() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		list.add(getInventory().getStackInSlot(EnumSlots.INPUT_SLOT.ordinal()));
		list.add(getInventory().getStackInSlot(EnumSlots.INPUT_SLOT2.ordinal()));
		list.add(getInventory().getStackInSlot(EnumSlots.INPUT_SLOT3.ordinal()));
		list.add(getInventory().getStackInSlot(EnumSlots.INPUT_SLOT4.ordinal()));
		return list;
	}

	public ItemStack[][] getActiveInventoryAsArray() {
		ItemStack[][] array = new ItemStack[2][2];

		array[0][0] = getInventory().getStackInSlot(EnumSlots.INPUT_SLOT.ordinal());
		array[0][1] = getInventory().getStackInSlot(EnumSlots.INPUT_SLOT2.ordinal());
		array[1][0] = getInventory().getStackInSlot(EnumSlots.INPUT_SLOT3.ordinal());
		array[1][1] = getInventory().getStackInSlot(EnumSlots.INPUT_SLOT4.ordinal());

		return array;
	}

	public IForgeRecipe getCurrentRecipe() {
		return this.currentRecipe;
	}

	public boolean isUsableByPlayer(PlayerEntity player) {
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		} else {
			return player.distanceToSqr((double) this.worldPosition.getX() + 0.5D,
					(double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static boolean isBurning(TileEntityForge forge) {
		return forge.getField(0) > 0;
	}

	public boolean isBurning() {
		return this.burnTime > 0;
	}

	public static boolean isItemFuel(ItemStack stack) {
		return ForgeHooks.getBurnTime(stack) > 0;
	}

	@Override
	public void onContainerOpened(PlayerEntity player) {

	}

	@Override
	public void onContainerClosed(PlayerEntity player) {

	}

	public void dropAllContents(World world, BlockPos blockPos) {
		InventoryHelper.dropContents(world, blockPos, this.getDrops());
	}

	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container.forge");
	}

	public int getField(int id) {
		switch (id) {
		case 0:
			return this.burnTime;
		case 1:
			return this.currentItemBurnTime;
		case 2:
			return this.cookTime;
		case 3:
			return this.totalCookTime;
		default:
			return 0;
		}
	}

	public void setField(int id, int value) {
		switch (id) {
		case 0:
			this.burnTime = value;
			break;
		case 1:
			this.currentItemBurnTime = value;
			break;
		case 2:
			this.cookTime = value;
			break;
		case 3:
			this.totalCookTime = value;
		}
	}

	public int getFieldCount() {
		return 4;
	}

	public ForgeResult getCurrentResult() {
		return currentResult;
	}

	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return ContainerForge.createContainerServerSide(windowID, playerInventory, this);
	}
}
