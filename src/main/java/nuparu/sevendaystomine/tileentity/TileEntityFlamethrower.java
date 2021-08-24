package nuparu.sevendaystomine.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import nuparu.sevendaystomine.block.BlockHorizontalBase;
import nuparu.sevendaystomine.init.ModFluids;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.block.ContainerFlamethrower;
import nuparu.sevendaystomine.inventory.block.ContainerGasGenerator;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;

public class TileEntityFlamethrower extends TileEntityItemHandler<ItemHandlerNameable> implements ITickableTileEntity {

	private static final int INVENTORY_SIZE = 1;
	public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.flamethrower");
	protected static final int MAX_VOLUME = 4000;
	

	protected FluidTank tank = new FluidTank(4000);

	Direction facing;

	public TileEntityFlamethrower() {
		super(ModTileEntities.FLAMETHROWER_TRAP.get());
		tank.setFluid(new FluidStack(ModFluids.GASOLINE.get(),0));
	}



	@Override
	public void tick() {
		if (facing == null) {
			facing = level.getBlockState(worldPosition).getValue(BlockHorizontalBase.FACING);
			if (facing == null) {
				return;
			}
		}
		
		if (tank.getFluidAmount() < MAX_VOLUME) {
			ItemStack stack = this.getInventory().getStackInSlot(0);
			if (!stack.isEmpty()) {
				Item item = stack.getItem();
				/*if (item instanceof UniversalBucket) {
					UniversalBucket bucket = (UniversalBucket) item;
					FluidStack fluidStack = bucket.getFluid(stack);
					if (fluidStack.getFluid() == ModFluids.GASOLINE.get()) {
						if (fluidStack.getAmount() <= MAX_VOLUME - tank.getFluidAmount()) {
							tank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
							this.getInventory().setStackInSlot(0, new ItemStack(Items.BUCKET));
							markForUpdate();
						}
					}
				}*/
				if (item == ModItems.GAS_CANISTER.get()) {
					if (250 <= tank.getCapacity() - tank.getFluidAmount()) {
						tank.fill(new FluidStack(ModFluids.GASOLINE.get(),250), IFluidHandler.FluidAction.EXECUTE);
						stack.shrink(1);
						if(stack.getCount() <= 0) {
							this.getInventory().setStackInSlot(0, ItemStack.EMPTY);
						}
						markForUpdate();
					}
				}
			}
		}

		boolean powered = level.hasNeighborSignal(worldPosition);
		if (powered && tank.getFluidAmount() > 0) {
			double x = worldPosition.getX() + 0.5;
			double y = worldPosition.getY() + 13 / 16d;
			double z = worldPosition.getZ() + 0.5;

			switch (facing) {
			case NORTH:
				z -= 0.51;
				break;
			case SOUTH:
				z += 0.51;
				break;
			case WEST:
				x -= 0.51;
				break;
			case EAST:
				x += 0.51;
				break;
			}

			Vector3i vec = facing.getNormal();
			float yaw = getYaw(vec) + 90;

			float pitch = 0f;
			for (int i = 0; i < 2; i++) {
				/*EntityFlame entity = new EntityFlame(level, x, y, z, yaw, pitch, 0.1f, 20f);
				if (!level.isClientSide()) {
					level.addFreshEntity(entity);
				}*/
			}
			tank.drain(1, IFluidHandler.FluidAction.EXECUTE);
		}
	}

	@Override
	protected ItemHandlerNameable createInventory() {
		return new ItemHandlerNameable(1, DEFAULT_NAME);
	}

	public static float getYaw(Vector3i vec) {
		double deltaX = vec.getX();
		double deltaZ = vec.getZ();
		double yaw = 0;
		if (deltaX != 0) {
			if (deltaX < 0) {
				yaw = 1.5 * Math.PI;
			} else {
				yaw = 0.5 * Math.PI;
			}
			yaw -= Math.atan(deltaZ / deltaX);
		} else if (deltaZ < 0) {
			yaw = Math.PI;
		}
		return (float) (-yaw * 180 / Math.PI - 90);
	}

	@Override
	public void load(BlockState state, CompoundNBT tag) {
		super.load(state,tag);
		tank.readFromNBT(tag);
	}

	@Override
	public CompoundNBT save(CompoundNBT tag) {
		super.save(tag);
		if (tank != null) {
			tank.writeToNBT(tag);
		}
		return tag;
	}
	
	public FluidTank getTank() {
		return this.tank;
	}
	
	public int getFluidGuiHeight(int maxHeight) {
		return (int) Math.ceil(getFluidPercentage() * (float) maxHeight);
	}
	
	public float getFluidPercentage() {
		return (float) getTank().getFluidAmount() / (float) getTank().getCapacity();
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


	@Override
	public ITextComponent getDisplayName() {
		return this.getInventory().hasCustomName() ? this.getInventory().getDisplayName() : DEFAULT_NAME;
	}


	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return ContainerFlamethrower.createContainerServerSide(windowID, playerInventory, this);
	}
}
