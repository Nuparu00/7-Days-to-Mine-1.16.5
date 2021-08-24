package nuparu.sevendaystomine.tileentity;

public class TileEntityCampfire { /*extends TileEntity implements IContainerCallbacks, ILootContainer, ITickable {

	private static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.campfire");
	
	public enum EnumSlots {
		INPUT_SLOT, INPUT_SLOT2, INPUT_SLOT3, INPUT_SLOT4, OUTPUT_SLOT, FUEL_SLOT, POT_SLOT
	}

	private int burnTime;
	private int currentItemBurnTime;
	private int cookTime;
	private int totalCookTime;
	
	public ICampfireRecipe currentRecipe = null;
	
	private final ItemHandlerNameable HANDLER_INPUT = new ItemHandlerNameable(4, DEFAULT_NAME);
	private final ItemHandlerNameable HANDLER_OUTPUT = new ItemHandlerNameable(1, DEFAULT_NAME);
	private final ItemHandlerNameable HANDLER_FUEL = new ItemHandlerNameable(1, DEFAULT_NAME){
		@Override
	    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
	    {
	        return !stack.isEmpty();
	    }
	};
	private final ItemHandlerNameable HANDLER_POT = new ItemHandlerNameable(1, DEFAULT_NAME) {
		@Override
	    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
	    {
	        return !stack.isEmpty() && ((stack.getItem() instanceof BlockItem && ((BlockItem)stack.getItem()).getBlock() instanceof BlockCookware) || stack.getItem() instanceof ItemCookware || stack.getItem() instanceof ItemBucket || stack.getItem() == ModItems.COOKING_GRILL);
	    }
	};

	public TileEntityCampfire() {

	}

	@Override
	public void update() {
		boolean flag = this.isBurning();
		boolean flag1 = false;

		if (this.isBurning()) {
			--this.burnTime;
		}

		if (!this.level.isClientSide()) {
			ItemStack itemstack = this.getInventory().getItem(EnumSlots.FUEL_SLOT.ordinal());
            if (this.isBurning()) {
            	AxisAlignedBB AABB = new AxisAlignedBB(pos.getX() + 0.2, pos.getY(), pos.getZ() + 0.2, pos.getX() + 0.8,
    					pos.getY() + 0.8, pos.getZ() + 0.8);
    			List<LivingEntity> list = world.getEntitiesOfClass(LivingEntity.class, AABB);
    			for (LivingEntity entity : list) {
    				if (world.random.nextInt(4) == 0) {
    					entity.setFire(5 + world.random.nextInt(5));
    				}
    			}
            }
            if (this.isBurning() || hasPot() && !isInputEmpty()) {
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

			if (flag != this.isBurning()) {
				flag1 = true;
				BlockCampfire.setState(this.isBurning(), this.level, this.pos);
			}
		}

		if (flag1) {
			world.markBlockRangeForRenderUpdate(pos, pos);
			world.sendBlockUpdated(pos, getState(), getState(), 3);
			world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
			setChanged();
		}



	}

	private boolean canSmelt() {
		if (!getOutputSlot().isEmpty() && getOutputSlot().getCount() > Math.min(getOutputSlot().getItem().getItemStackLimit(getOutputSlot()),64))
			return false;

		if (isInputEmpty() || !hasPot())
			return false;

		for (ICampfireRecipe recipe : CampfireRecipeManager.getInstance().getRecipes()) {
			if (recipe.matches(this, this.level)) {
				if (!getOutputSlot().isEmpty() && !ItemStack.isSame(getOutputSlot(), recipe.getOutput(this)))
					return false;
				if (!ItemStack.isSame(getPotSlot(), recipe.getPot()))
					return false;
				if (getOutputSlot().getCount() + recipe.getResult().getCount() <= Math.min(getOutputSlot().getItem().getItemStackLimit(getOutputSlot()),64)) {
					currentRecipe = recipe;
					return true;
				}
			}
		}
		return false;
	}

	public void smeltItem() {

		ICampfireRecipe recipeToUse = null;
		for (ICampfireRecipe recipe : CampfireRecipeManager.getInstance().getRecipes()) {
			if (recipe.matches(this, this.level)) {
				recipeToUse = recipe;
				break;
			}
		}
		if (recipeToUse != null) {

			ItemStack currentOutput = getOutputSlot();
			if (ItemStack.isSame(getPotSlot(), recipeToUse.getPot())) {

				if (currentOutput.isEmpty()) {
					getInventory().setStackInSlot(EnumSlots.OUTPUT_SLOT.ordinal(), recipeToUse.getResult());

				} else {
					if (ItemStack.isSame(currentOutput, recipeToUse.getResult()) && currentOutput.getCount()
							+ recipeToUse.getResult().getCount() <= Math.min(getOutputSlot().getItem().getItemStackLimit(getOutputSlot()),64)) {

						currentOutput.grow(recipeToUse.getResult().getCount());
						getInventory().setStackInSlot(EnumSlots.OUTPUT_SLOT.ordinal(), currentOutput);
					}
				}
				consumeInput(recipeToUse);
			}
		}
	}

	public void consumeInput(ICampfireRecipe recipe) {
		recipe.consumeInput(this);
	}

	public boolean isInputEmpty() {
		return (getInventory().getItem(EnumSlots.INPUT_SLOT.ordinal()).isEmpty()
				&& getInventory().getItem(EnumSlots.INPUT_SLOT2.ordinal()).isEmpty()
				&& getInventory().getItem(EnumSlots.INPUT_SLOT3.ordinal()).isEmpty()
				&& getInventory().getItem(EnumSlots.INPUT_SLOT4.ordinal()).isEmpty());
	}

	public boolean hasPot() {
		return !getInventory().getItem(EnumSlots.POT_SLOT.ordinal()).isEmpty();
	}

	public boolean hasFuel() {
		return !getInventory().getItem(EnumSlots.FUEL_SLOT.ordinal()).isEmpty();
	}

	public ItemStack getOutputSlot() {
		return getInventory().getItem(EnumSlots.OUTPUT_SLOT.ordinal());
	}

	public ItemStack getPotSlot() {
		return getInventory().getItem(EnumSlots.POT_SLOT.ordinal());
	}

	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		if (compound.contains("HandlerInput")) {
			HANDLER_INPUT.deserializeNBT(compound.getCompoundTag("HandlerInput"));
		}
		if (compound.contains("HandlerOutput")) {
			HANDLER_OUTPUT.deserializeNBT(compound.getCompoundTag("HandlerOutput"));
		}
		if (compound.contains("HandlerFuel")) {
			HANDLER_FUEL.deserializeNBT(compound.getCompoundTag("HandlerFuel"));
		}
		if (compound.contains("HandlerMold")) {
			HANDLER_POT.deserializeNBT(compound.getCompoundTag("HandlerMold"));
		}
		
		this.burnTime = compound.getInt("BurnTime");
		this.cookTime = compound.getInt("CookTime");
		this.totalCookTime = compound.getInt("CookTimeTotal");
		this.currentItemBurnTime = ForgeHooks.getBurnTime(this.getInventory().getItem(1));
	}

	public CompoundNBT save(CompoundNBT compound) {
		 super.save(compound);
		compound.put("HandlerInput", HANDLER_INPUT.serializeNBT());
		compound.put("HandlerOutput", HANDLER_OUTPUT.serializeNBT());
		compound.put("HandlerFuel", HANDLER_FUEL.serializeNBT());
		compound.put("HandlerMold", HANDLER_POT.serializeNBT());
		
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
		list.add(getInventory().getItem(EnumSlots.INPUT_SLOT.ordinal()));
		list.add(getInventory().getItem(EnumSlots.INPUT_SLOT2.ordinal()));
		list.add(getInventory().getItem(EnumSlots.INPUT_SLOT3.ordinal()));
		list.add(getInventory().getItem(EnumSlots.INPUT_SLOT4.ordinal()));
		return list;
	}

	public ItemStack[][] getActiveInventoryAsArray() {
		ItemStack[][] array = new ItemStack[2][2];

		array[0][0] = getInventory().getItem(EnumSlots.INPUT_SLOT.ordinal());
		array[0][1] = getInventory().getItem(EnumSlots.INPUT_SLOT2.ordinal());
		array[1][0] = getInventory().getItem(EnumSlots.INPUT_SLOT3.ordinal());
		array[1][1] = getInventory().getItem(EnumSlots.INPUT_SLOT4.ordinal());

		return array;
	}
	
	public ICampfireRecipe getCurrentRecipe() {
		return this.currentRecipe;
	}


	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		if (this.level.getBlockEntity(this.pos) != this) {
			return false;
		} else {
			return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
					(double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}
	

	private BlockState getState() {
		return world.getBlockState(pos);
	}


	@OnlyIn(Dist.CLIENT)
	public static boolean isBurning(TileEntityCampfire campfire) {
		return campfire.getField(0) > 0;
	}

	public boolean isBurning() {
		return this.burnTime > 0;
	}



	public static boolean isItemFuel(ItemStack stack) {
		return ForgeHooks.getBurnTime(stack) > 0;
	}

	public CombinedInvWrapper getInventory() {
		return new CombinedInvWrapper(this.HANDLER_INPUT, this.HANDLER_OUTPUT, this.HANDLER_FUEL, this.HANDLER_POT);
	}
	
	public NonNullList<ItemStack> getDrops() {
		return Utils.dropItemHandlerContents(getInventory(), getWorld().random);
	}

	public void setDisplayName(String displayName) {
		HANDLER_INPUT.setDisplayName(new StringTextComponent(displayName));
	}
	
	public ITextComponent getDisplayName() {
		return HANDLER_INPUT.getDisplayName();
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
	public boolean hasCapability(Capability<?> capability, Direction facing) {
		return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, Direction facing) {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			switch(facing) {
			case UP : return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(HANDLER_INPUT);
			case DOWN : return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(HANDLER_OUTPUT);
			case NORTH :
			case WEST :
			case SOUTH :
			case EAST : return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new CombinedInvWrapper(HANDLER_FUEL,HANDLER_POT));
			}
		}

		return null;
	}

	@Override
	public ResourceLocation getLootTable() {
		return null;
	}

	@Override
	public void onContainerOpened(PlayerEntity player) {
	}

	@Override
	public void onContainerClosed(PlayerEntity player) {
	}
	
	public ContainerCampfire createContainer(PlayerEntity player) {
		final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player
				.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP);
		final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory,
				playerInventory);

		return new ContainerCampfire(playerInventoryWrapper, getInventory(), this, player);
	}
*/
}
