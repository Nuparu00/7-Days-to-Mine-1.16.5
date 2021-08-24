package nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import nuparu.sevendaystomine.crafting.chemistry.ChemistryRecipeManager;
import nuparu.sevendaystomine.crafting.chemistry.IChemistryRecipe;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.block.ContainerChemistryStation;
import nuparu.sevendaystomine.inventory.block.ContainerForge;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;

public class TileEntityChemistryStation extends TileEntityItemHandler<ItemHandlerNameable> implements ITickableTileEntity{

	private static final int INVENTORY_SIZE = 6;
	private static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.chemistry_station");
	
	private int burnTime;
	private int currentItemBurnTime;
	private int cookTime;
	private int totalCookTime;

	public final IIntArray dataAccess = new IIntArray() {
		public int get(int p_221476_1_) {
			switch (p_221476_1_) {
				case 0:
					return TileEntityChemistryStation.this.burnTime;
				case 1:
					return TileEntityChemistryStation.this.currentItemBurnTime;
				case 2:
					return TileEntityChemistryStation.this.cookTime;
				case 3:
					return TileEntityChemistryStation.this.totalCookTime;
				default:
					return 0;
			}
		}

		public void set(int p_221477_1_, int p_221477_2_) {
			switch (p_221477_1_) {
				case 0:
					TileEntityChemistryStation.this.burnTime = p_221477_2_;
					break;
				case 1:
					TileEntityChemistryStation.this.currentItemBurnTime = p_221477_2_;
					break;
				case 2:
					TileEntityChemistryStation.this.cookTime = p_221477_2_;
					break;
				case 3:
					TileEntityChemistryStation.this.totalCookTime = p_221477_2_;
			}

		}

		public int getCount() {
			return 4;
		}
	};

	private IChemistryRecipe currentRecipe = null;
	
	public enum EnumSlots {
		INPUT_SLOT, INPUT_SLOT2, INPUT_SLOT3, INPUT_SLOT4, OUTPUT_SLOT, FUEL_SLOT
	}
	
	public TileEntityChemistryStation() {
		super(ModTileEntities.CHEMISTRY_STATION.get());
	}
	
	
	@Override
	protected ItemHandlerNameable createInventory() {
		return new ItemHandlerNameable(INVENTORY_SIZE, DEFAULT_NAME);
	}
	
	@Override
	public void tick() {
		boolean flag1 = false;

		if (this.isBurning()) {
			--this.burnTime;
		}
		if (!this.level.isClientSide()) {
			ItemStack itemstack = this.getInventory().getStackInSlot(EnumSlots.FUEL_SLOT.ordinal());
			if (this.isBurning() || hasFuel() && !isInputEmpty()) {
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
								this.getInventory().setStackInSlot(EnumSlots.FUEL_SLOT.ordinal(), item1);
							}
						}
					}
					this.totalCookTime = this.getCookTime(null);
				}

				if (this.isBurning() && this.canSmelt()) {
					++this.cookTime;
					if (this.cookTime == this.totalCookTime) {
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
		}

		if (flag1) {
			markForUpdate();
		}



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
		CompoundNBT nbt = save(new CompoundNBT());
		return nbt;
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
	
	private BlockState getState() {
		return level.getBlockState(worldPosition);
	}
	
	private boolean canSmelt() {
		if (!getOutputSlot().isEmpty() && getOutputSlot().getCount() > 64)
			return false;
		if (isInputEmpty() || !hasFuel())
			return false;
		for (IChemistryRecipe recipe : ChemistryRecipeManager.getInstance().getRecipes()) {
			if (recipe.matches(this, this.level)) {
				ItemStack output = recipe.getOutput(this);
				if (!getOutputSlot().isEmpty() && !ItemStack.isSame(getOutputSlot(), output)) {
					continue;
				}
				if (getOutputSlot().getCount() + output.getCount() <= 64) {
					currentRecipe = recipe;
					return true;
				}
			}
		}
		return false;
	}

	public void smeltItem() {

		IChemistryRecipe recipeToUse = null;
		for (IChemistryRecipe recipe : ChemistryRecipeManager.getInstance().getRecipes()) {
			if (recipe.matches(this, this.level)) {
				recipeToUse = recipe;
				break;
			}
		}
		if (recipeToUse != null) {

			ItemStack currentOutput = getOutputSlot();
			if (currentOutput.isEmpty()) {
				getInventory().setStackInSlot(EnumSlots.OUTPUT_SLOT.ordinal(), recipeToUse.getOutput(this));

			} else {
				if (ItemStack.isSame(currentOutput, recipeToUse.getOutput(this)) && currentOutput.getCount()
						+ recipeToUse.getOutput(this).getCount() <= 64) {

					currentOutput.grow(recipeToUse.getOutput(this).getCount());
					getInventory().setStackInSlot(EnumSlots.OUTPUT_SLOT.ordinal(), currentOutput);
				}
			}
			consumeInput(recipeToUse);

		}
	}

	public void consumeInput(IChemistryRecipe recipe) {
		recipe.consumeInput(this);
	}

	public boolean isInputEmpty() {
		return (getInventory().getStackInSlot(EnumSlots.INPUT_SLOT.ordinal()).isEmpty()
				&& getInventory().getStackInSlot(EnumSlots.INPUT_SLOT2.ordinal()).isEmpty()
				&& getInventory().getStackInSlot(EnumSlots.INPUT_SLOT3.ordinal()).isEmpty()
				&& getInventory().getStackInSlot(EnumSlots.INPUT_SLOT4.ordinal()).isEmpty());
	}

	public boolean hasFuel() {
		return !getInventory().getStackInSlot(EnumSlots.FUEL_SLOT.ordinal()).isEmpty();
	}

	public ItemStack getOutputSlot() {
		return getInventory().getStackInSlot(EnumSlots.OUTPUT_SLOT.ordinal());
	}

	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.burnTime = compound.getInt("BurnTime");
		this.cookTime = compound.getInt("CookTime");
		this.totalCookTime = compound.getInt("CookTimeTotal");
		this.currentItemBurnTime = ForgeHooks.getBurnTime(this.getInventory().getStackInSlot(1));
	}

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

	public IChemistryRecipe getCurrentRecipe() {
		return this.currentRecipe;
	}

	@OnlyIn(Dist.CLIENT)
	public static boolean isBurning(TileEntityChemistryStation inventory) {
		return inventory.isBurning();
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

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return this.level.getBlockEntity(this.worldPosition) == this && player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64;
	}

	public void setDisplayName(String displayName) {
		getInventory().setDisplayName(new StringTextComponent(displayName));
	}

	@Override
	public ResourceLocation getLootTable() {
		return null;
	}
	
	@Override
	public ITextComponent getDisplayName() {
		return ((ItemHandlerNameable)this.getInventory()).getDisplayName();
	}


	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return ContainerChemistryStation.createContainerServerSide(windowID, playerInventory, this);
	}
	
	/*
	public void updateBlock() {
		BlockState BlockState = this.level.getBlockState(this.pos);
		boolean state = BlockState.getValue(BlockBookshelfEnhanced.FULL);
		if (isEmpty() && state) {
			world.setBlockState(pos, BlockState.setValue(BlockBookshelfEnhanced.FULL, false));
		} else if (!isEmpty() && !state) {
			world.setBlockState(pos, BlockState.setValue(BlockBookshelfEnhanced.FULL, true));
		}
	}

	public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newState) {
		return (oldState.getBlock() != newState.getBlock())
				|| oldState.getValue(BlockBookshelfEnhanced.FACING) != newState.getValue(BlockBookshelfEnhanced.FACING);
	}*/

}