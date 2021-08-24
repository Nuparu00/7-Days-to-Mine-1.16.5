package nuparu.sevendaystomine.tileentity;

public class TileEntityKeySafe { /*extends TileEntitySafe {

	//-180 -- +180
	private float lockAngle;
	//In degrees
	private final float tolerance = 10;

	//-180 -- +180
	private float sweetPoint;
	
	private float force;
	
	private final float forceThreshold = 150;

	public TileEntityKeySafe() {
	}

	public boolean tryToUnlock() {
		if(Math.abs(lockAngle-sweetPoint)<=tolerance && force >= forceThreshold) {
			return true;
		}
		return false;
	}

	public void unlock() {

	}

	public void lock() {

	}
	
	public boolean tryToBreakPick(PlayerEntity player) {
		float diff = Math.abs(sweetPoint-lockAngle);
		if(force >= forceThreshold*(1-diff/180f)) {
			return true;
		}
		return false;
	}

	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.lockAngle = compound.getFloat("LockAngle");
		this.sweetPoint = compound.getFloat("SweetPoint");
		this.force = compound.getFloat("Force");
	}

	public CompoundNBT save(CompoundNBT compound) {
		 super.save(compound);
		compound.setFloat("LockAngle", this.lockAngle);
		compound.setFloat("SweetPoint", this.sweetPoint);
		compound.setFloat("Force", this.force);
		
		return compound;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		CompoundNBT nbtTag = new CompoundNBT();
		this.save(nbtTag);
		nbtTag.remove("SweetPoint");
		return new SPacketUpdateTileEntity(getPos(), 0, nbtTag);
	}

	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT nbt = save(new CompoundNBT());
		nbt.remove("SweetPoint");
		return nbt;
	}
	
	public String superSecretMethod() {
		return sweetPoint + " +- " + tolerance;
	}
	
	public void setAngle(float angle) {
		this.lockAngle = angle;
	}
	public void setForce(float force) {
		this.force = force;
	}
	
	public float getForce() {
		return this.force;
	}
	
	@Override
	public Container createContainer(PlayerEntity player) {
		final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player
				.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP);
		final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory,
				playerInventory);

		return new ContainerSmall(playerInventoryWrapper, inventory, player, this);
	}

	@Override
	public ResourceLocation getLootTable() {
		return null;
	}
*/
}
