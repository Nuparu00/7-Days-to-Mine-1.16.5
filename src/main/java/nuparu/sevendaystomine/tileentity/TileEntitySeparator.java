package nuparu.sevendaystomine.tileentity;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import nuparu.sevendaystomine.crafting.separator.ISeparatorRecipe;
import nuparu.sevendaystomine.crafting.separator.SeparatorRecipeManager;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.EnumDeviceType;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.block.ContainerForge;
import nuparu.sevendaystomine.inventory.block.ContainerSeparator;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.util.ModConstants;

public class TileEntitySeparator extends TileEntityItemHandler<ItemHandlerNameable> implements ITickableTileEntity, IVoltage {

	private static final int INVENTORY_SIZE = 3;
	private static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.separator");
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

	public ISeparatorRecipe currentRecipe = null;

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

	public void setDisplayName(String displayName) {
		getInventory().setDisplayName(new StringTextComponent(displayName));
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
					if(this.totalCookTime == 0) {
						this.totalCookTime = this.getCookTime(null);
					}
					else if (this.cookTime >= this.totalCookTime) {
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

	private boolean canSmelt() {
		if(this.voltage < this.getRequiredPower()) return false;
		
		ISeparatorRecipe recipeToUse = null;
		for (ISeparatorRecipe recipe : SeparatorRecipeManager.getInstance().getRecipes()) {
			if (recipe.matches(this, this.level)) {
				recipeToUse = recipe;
				break;
			}
		}
		if(recipeToUse == null)
			return false;
		if (getInput().isEmpty())
			return false;
		if (!getOutputLeft().isEmpty() && getOutputLeft().getCount() >= 64)
			return false;
		if (!getOutputRight().isEmpty() && getOutputRight().getCount() >= 64)
			return false;
		for (ISeparatorRecipe recipe : SeparatorRecipeManager.getInstance().getRecipes()) {
			if (recipe.matches(this, this.level)) {
				List<ItemStack> output = recipe.getOutputs(this);
				if (!getOutputLeft().isEmpty() && !ItemStack.isSame(getOutputLeft(), output.get(0))) {
					continue;
				}
				if (!getOutputRight().isEmpty() && !ItemStack.isSame(getOutputRight(), output.get(1))) {
					continue;
				}
				if (getOutputLeft().getCount() + output.get(0).getCount() > 64) {
					continue;
				}
				if (getOutputRight().getCount() + output.get(1).getCount() > 64) {
					continue;
				}
				
				currentRecipe = recipe;
				return true;
			}
		}
		return false;
	}

	public void smeltItem() {

		ISeparatorRecipe recipeToUse = null;
		for (ISeparatorRecipe recipe : SeparatorRecipeManager.getInstance().getRecipes()) {
			if (recipe.matches(this, this.level)) {
				recipeToUse = recipe;
				break;
			}
		}
		if (recipeToUse != null) {

			ItemStack currentOutput = getOutputLeft();
			if (currentOutput.isEmpty()) {
				getInventory().setStackInSlot(1, recipeToUse.getOutputs(this).get(0).copy());

			} else {
				if (ItemStack.isSame(currentOutput, recipeToUse.getOutputs(this).get(0)) && currentOutput.getCount()
						+ recipeToUse.getOutputs(this).get(0).getCount() <= 64) {
					currentOutput.grow(recipeToUse.getOutputs(this).get(0).getCount());
					getInventory().setStackInSlot(1, currentOutput);
				}
			}
			
			currentOutput = getOutputRight();
			if (currentOutput.isEmpty()) {
				getInventory().setStackInSlot(2, recipeToUse.getOutputs(this).get(1).copy());

			} else {
				if (ItemStack.isSame(currentOutput, recipeToUse.getOutputs(this).get(1)) && currentOutput.getCount()
						+ recipeToUse.getOutputs(this).get(1).getCount() <= 64) {

					currentOutput.grow(recipeToUse.getOutputs(this).get(1).getCount());
					getInventory().setStackInSlot(2, currentOutput);
				}
			}
			consumeInput(recipeToUse);
			this.voltage -= this.getRequiredPower();
		}
	}

	public void consumeInput(ISeparatorRecipe recipe) {
		recipe.consumeInput(this);
	}

	public boolean isBurning() {
		return true;
	}
	
	public int getCookTime(ItemStack stack) {
		return 300;
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
	}
	@Override
	public CompoundNBT save(CompoundNBT compound) {
		 super.save(compound);
		compound.putInt("CookTime", (short) this.cookTime);
		compound.putInt("CookTimeTotal", (short) this.totalCookTime);
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
			lost = (long) Math.round(delta * ModConstants.DROP_PER_BLOCK * connection.getDistance());
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
		long toAdd = Math.min(this.capacity-this.voltage, maxReceive);
		if(!simulate) {
			this.voltage+=toAdd;
		}
		return (int)toAdd;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		long toExtract = Math.min(this.voltage, maxExtract);
		if(!simulate) {
			this.voltage-=toExtract;
		}
		return (int)toExtract;
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

	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return ContainerSeparator.createContainerServerSide(windowID, playerInventory, this);
	}

	@Override
	public BlockPos getPos() {
		return worldPosition;
	}
	
}
