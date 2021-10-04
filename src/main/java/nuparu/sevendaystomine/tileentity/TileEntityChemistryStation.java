package nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import nuparu.sevendaystomine.crafting.chemistry.IChemistryRecipe;
import nuparu.sevendaystomine.crafting.forge.IForgeRecipe;
import nuparu.sevendaystomine.init.ModRecipeSerializers;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.block.ContainerChemistryStation;
import nuparu.sevendaystomine.inventory.block.ContainerForge;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;

import javax.annotation.Nullable;

public class TileEntityChemistryStation extends TileEntityItemHandler<ItemHandlerNameable> implements ITickableTileEntity, IInventory {

	private static final int INVENTORY_SIZE = 6;
	private static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.chemistry_station");
	private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
	
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

	private IChemistryRecipe<TileEntityChemistryStation> currentRecipe = null;

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
		IChemistryRecipe<TileEntityChemistryStation> irecipe = this.level.getRecipeManager().getRecipeFor(ModRecipeSerializers.CHEMISTRY_RECIPE_TYPE, this, this.level).orElse(null);

		if(irecipe != null){
			this.currentRecipe = irecipe;
			return true;
		}
		return false;
	}

	public void smeltItem() {

		if(currentRecipe != null){
			ItemStack result = currentRecipe.assemble(this);
			ItemStack currentOutput = getOutputSlot();
			if (currentOutput.isEmpty()) {
				this.getInventory().setStackInSlot(TileEntityChemistryStation.EnumSlots.OUTPUT_SLOT.ordinal(), result);
			}
			else if (ItemStack.isSame(currentOutput, result)
					&& currentOutput.getCount() + result.getCount() <= Math.min(
					getOutputSlot().getItem().getItemStackLimit(getOutputSlot()),
					this.getInventory().getStackInSlot(TileEntityChemistryStation.EnumSlots.OUTPUT_SLOT.ordinal()).getMaxStackSize())) {
				currentOutput.grow(result.getCount());
				this.getInventory().setStackInSlot(TileEntityChemistryStation.EnumSlots.OUTPUT_SLOT.ordinal(), currentOutput);
			}
			ResourceLocation resourcelocation = currentRecipe.getId();
			this.recipesUsed.addTo(resourcelocation, 1);
			consumeInput();
		}
	}

	public void consumeInput() {
		for(int i = 0; i < 4; i++){
			ItemStack stack = this.getInventory().getStackInSlot(i);
			stack.shrink(1);
			if(stack.isEmpty()){
				getInventory().setStackInSlot(i,ItemStack.EMPTY);
			}
		}
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

		CompoundNBT compoundnbt = compound.getCompound("RecipesUsed");

		for(String s : compoundnbt.getAllKeys()) {
			this.recipesUsed.put(new ResourceLocation(s), compoundnbt.getInt(s));
		}
	}

	public CompoundNBT save(CompoundNBT compound) {
		 super.save(compound);
		compound.putInt("BurnTime", (short) this.burnTime);
		compound.putInt("CookTime", (short) this.cookTime);
		compound.putInt("CookTimeTotal", (short) this.totalCookTime);

		CompoundNBT compoundnbt = new CompoundNBT();
		this.recipesUsed.forEach((p_235643_1_, p_235643_2_) -> {
			compoundnbt.putInt(p_235643_1_.toString(), p_235643_2_);
		});
		compound.put("RecipesUsed", compoundnbt);

		return compound;
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
				createExperience(p_235640_1_, p_235640_2_, entry.getIntValue(), ((IChemistryRecipe)p_235642_4_).getExperience());
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

	public int getCookTime(ItemStack stack) {
		return currentRecipe == null ? 600 : currentRecipe.getCookingTime();
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
	Present only because of IRecipe implementation, do not use!
	 */
	@Override
	public int getContainerSize() {
		return 4;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public ItemStack getItem(int i) {
		return getInventory().getStackInSlot(i);
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
	public void setItem(int p_70299_1_, ItemStack p_70299_2_) {

	}

	@Override
	public boolean stillValid(PlayerEntity p_70300_1_) {
		return false;
	}

	@Override
	public void clearContent() {

	}

}