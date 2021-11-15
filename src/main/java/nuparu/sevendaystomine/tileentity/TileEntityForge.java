package nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import nuparu.sevendaystomine.crafting.forge.IForgeRecipe;
import nuparu.sevendaystomine.init.ModRecipeSerializers;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.IContainerCallbacks;
import nuparu.sevendaystomine.inventory.block.ContainerForge;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;

public class TileEntityForge extends TileEntityItemHandler<ItemHandlerNameable>
		implements INamedContainerProvider, IContainerCallbacks, ITickableTileEntity, IInventory {

	public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.forge");
	private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();

	public int burnTime;
	public int currentItemBurnTime;
	public int cookTime;
	public int totalCookTime;

	IForgeRecipe<TileEntityForge> currentRecipe;


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
						.setValue(AbstractFurnaceBlock.LIT, this.isBurning()), 3);
			}
		}

		if (flag1) {
			this.setChanged();
		}

	}

	private boolean canSmelt() {
		IForgeRecipe<TileEntityForge> irecipe = this.level.getRecipeManager().getRecipeFor(ModRecipeSerializers.FORGE_RECIPE_TYPE, this, this.level).orElse(null);

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
				this.getInventory().setStackInSlot(TileEntityForge.EnumSlots.OUTPUT_SLOT.ordinal(), result);
			}
			else if (ItemStack.isSame(currentOutput, result)
					&& currentOutput.getCount() + result.getCount() <= Math.min(
					getOutputSlot().getItem().getItemStackLimit(getOutputSlot()),
					this.getInventory().getStackInSlot(TileEntityForge.EnumSlots.OUTPUT_SLOT.ordinal()).getMaxStackSize())) {
				currentOutput.grow(result.getCount());
				this.getInventory().setStackInSlot(TileEntityForge.EnumSlots.OUTPUT_SLOT.ordinal(), currentOutput);
			}
			ResourceLocation resourcelocation = currentRecipe.getId();
			this.recipesUsed.addTo(resourcelocation, 1);
			consumeInput();
		}
	}

	public void consumeInput() {

		ItemStack mold = this.getMoldSlot();
		if(mold.getItem() == Items.BUCKET){
			mold.shrink(1);
			if(mold.getCount() <= 0){
				this.getInventory().setStackInSlot(EnumSlots.MOLD_SLOT.ordinal(), ItemStack.EMPTY);
			}
		}
		else {
			mold.hurt(1, level.random, null);
			if (mold.getDamageValue() >= mold.getMaxDamage()) {
				if (mold.getCount() > 0) {
					mold.shrink(1);
				} else {
					this.getInventory().setStackInSlot(EnumSlots.MOLD_SLOT.ordinal(), ItemStack.EMPTY);
				}
			}
		}

		if(currentRecipe.consume(this)) return;
		//Generic consume implementation - move to ForgeRecipeShapeless later
		for(int i = 0; i < 4; i++){
			ItemStack stack = this.getInventory().getStackInSlot(i);
			stack.shrink(1);
			if(stack.isEmpty()){
				getInventory().setStackInSlot(i,ItemStack.EMPTY);
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

		CompoundNBT compoundnbt = compound.getCompound("RecipesUsed");

		for(String s : compoundnbt.getAllKeys()) {
			this.recipesUsed.put(new ResourceLocation(s), compoundnbt.getInt(s));
		}
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);

		compound.putInt("BurnTime", (short) this.burnTime);
		compound.putInt("CookTime", (short) this.cookTime);
		compound.putInt("CookTimeTotal", (short) this.totalCookTime);

		CompoundNBT compoundnbt = new CompoundNBT();
		this.recipesUsed.forEach((p_235643_1_, p_235643_2_) -> compoundnbt.putInt(p_235643_1_.toString(), p_235643_2_));
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
				createExperience(p_235640_1_, p_235640_2_, entry.getIntValue(), ((IForgeRecipe)p_235642_4_).getExperience());
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

	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return ContainerForge.createContainerServerSide(windowID, playerInventory, this);
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
