package nuparu.sevendaystomine.tileentity;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
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
import nuparu.sevendaystomine.crafting.grill.IGrillRecipe;
import nuparu.sevendaystomine.init.ModRecipeSerializers;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.IContainerCallbacks;
import nuparu.sevendaystomine.inventory.block.ContainerForge;
import nuparu.sevendaystomine.inventory.block.ContainerGrill;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileEntityGrill extends TileEntityItemHandler<ItemHandlerNameable>
		implements INamedContainerProvider, IContainerCallbacks, ITickableTileEntity, IInventory {

	public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.grill");
	private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();

	public int cookTime;
	public int totalCookTime;


	public enum EnumSlots {
		INPUT_SLOT, INPUT_SLOT2, INPUT_SLOT3, INPUT_SLOT4, OUTPUT_SLOT
	}

	@Override
	protected ItemHandlerNameable createInventory() {
		return new ItemHandlerNameable(EnumSlots.values().length, DEFAULT_NAME);
	}

	public final IIntArray dataAccess = new IIntArray() {
		public int get(int p_221476_1_) {
			switch (p_221476_1_) {
			case 0:
				return TileEntityGrill.this.cookTime;
			case 1:
				return TileEntityGrill.this.totalCookTime;
			default:
				return 0;
			}
		}

		public void set(int p_221477_1_, int p_221477_2_) {
			switch (p_221477_1_) {
			case 0:
				TileEntityGrill.this.cookTime = p_221477_2_;
				break;
			case 1:
				TileEntityGrill.this.totalCookTime = p_221477_2_;
				break;
			}

		}

		public int getCount() {
			return 2;
		}
	};

	private IGrillRecipe<TileEntityGrill> currentRecipe = null;



	public TileEntityGrill() {
		super(ModTileEntities.COOKING_GRILL.get());

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


		if (!this.level.isClientSide()) {
			if (this.isBurning() && !isInputEmpty()) {
				if(this.totalCookTime == 0){
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
			}
		}

		if (flag1) {
			this.setChanged();
		}

	}

	private boolean canSmelt() {


		IGrillRecipe<TileEntityGrill> irecipe = this.level.getRecipeManager().getRecipeFor(ModRecipeSerializers.GRILL.getA(), this, this.level).orElse(null);

		if(irecipe != null){
			this.currentRecipe = irecipe;
			return true;
		}

		return false;
	}

	public void smeltItem() {
		if(currentRecipe != null){
			ItemStack result = currentRecipe.getResultItem();
			ItemStack currentOutput = getOutputSlot();
			if (currentOutput.isEmpty()) {
				this.getInventory().setStackInSlot(EnumSlots.OUTPUT_SLOT.ordinal(), result);
			}
			else if (ItemStack.isSame(currentOutput, result)
					&& currentOutput.getCount() + result.getCount() <= Math.min(
					getOutputSlot().getItem().getItemStackLimit(getOutputSlot()),
					this.getInventory().getStackInSlot(EnumSlots.OUTPUT_SLOT.ordinal()).getMaxStackSize())) {
				currentOutput.grow(result.getCount());
				this.getInventory().setStackInSlot(EnumSlots.OUTPUT_SLOT.ordinal(), currentOutput);
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
		for (int i = EnumSlots.INPUT_SLOT.ordinal(); i < EnumSlots.INPUT_SLOT4.ordinal(); i++) {
			if (!this.getInventory().getStackInSlot(i).isEmpty())
				return false;
		}
		return true;
	}



	public ItemStack getOutputSlot() {
		return this.getInventory().getStackInSlot(EnumSlots.OUTPUT_SLOT.ordinal());
	}

	public ItemStack getIntputSlot(int i) {
		return this.getInventory().getStackInSlot(EnumSlots.INPUT_SLOT.ordinal()+i);
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);

		this.cookTime = compound.getInt("CookTime");
		this.totalCookTime = compound.getInt("CookTimeTotal");

		CompoundNBT compoundnbt = compound.getCompound("RecipesUsed");
		for(String s : compoundnbt.getAllKeys()) {
			this.recipesUsed.put(new ResourceLocation(s), compoundnbt.getInt(s));
		}
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);

		compound.putInt("CookTime", (short) this.cookTime);
		compound.putInt("CookTimeTotal", (short) this.totalCookTime);

		CompoundNBT compoundnbt = new CompoundNBT();
		this.recipesUsed.forEach((p_235643_1_, p_235643_2_) -> {
			compoundnbt.putInt(p_235643_1_.toString(), p_235643_2_);
		});
		compound.put("RecipesUsed", compoundnbt);

		return compound;
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

	public boolean isBurning() {
		BlockState state = level.getBlockState(getBlockPos().below());
		if(!(state.getBlock() instanceof CampfireBlock)) return false;
		return state.getValue(CampfireBlock.LIT);
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
		return new TranslationTextComponent("container.cooking_grill");
	}


	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return ContainerGrill.createContainerServerSide(windowID, playerInventory, this);
	}


	/*
	DONT USE ANY OF THESE - WE HAVE TO IMPLEMENT IINVENTORY BECAUSE OF THE IRECIPE
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
		return this.getIntputSlot(i);
	}

	@Override
	public ItemStack removeItem(int i, int count) {
		ItemStack stack = getItem(i);
		stack.shrink(count);
		if(stack.isEmpty()) {
			this.getInventory().setStackInSlot(i,ItemStack.EMPTY);
			return ItemStack.EMPTY;}
		return stack;
	}

	@Override
	public ItemStack removeItemNoUpdate(int i) {

		ItemStack stack = getItem(i);
		this.getInventory().setStackInSlot(i,ItemStack.EMPTY);
		return stack;

	}

	@Override
	public void setItem(int i, ItemStack stack) {
		this.getInventory().setStackInSlot(i,stack);
	}

	@Override
	public boolean stillValid(PlayerEntity p_70300_1_) {
		return false;
	}

	@Override
	public void clearContent() {

	}
}
