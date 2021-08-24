package nuparu.sevendaystomine.tileentity;

import java.util.Iterator;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.init.ModFluids;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.block.ContainerCombustionGenerator;
import nuparu.sevendaystomine.inventory.block.ContainerGasGenerator;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.util.MathUtils;

public class TileEntityGasGenerator extends TileEntityGeneratorBase{
	private static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.generator.gas");

	protected FluidTank tank = new FluidTank(4000);


	protected int timer;
	protected boolean isEmpty = true;


	public TileEntityGasGenerator() {
		super(ModTileEntities.GAS_GENERATOR.get());
		tank.setFluid(new FluidStack(ModFluids.GASOLINE.get(),0));
	}

	@Override
	protected ItemHandlerNameable createInventory() {
		return new ItemHandlerNameable(1, DEFAULT_NAME);
	}
	
	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		timer = compound.getInt("timer");
		isEmpty = compound.getBoolean("isEmpty");
		tank.readFromNBT(compound);
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		 super.save(compound);
	
		compound.putBoolean("isEmpty", isEmpty);
		compound.putInt("timer", timer);
		if (tank != null) {
			tank.writeToNBT(compound);
		}
		return compound;
	}

	@Override
	public void tick() {

		if(level.isClientSide()) return;

		boolean flag = false;

		int lava = countAdjacentMats(Material.LAVA);
		int air = countAdjacentBlocks(Blocks.AIR);
		int water = countAdjacentMats(Material.WATER);
		int ice = countAdjacentMats(Material.ICE);
		int packedIce = countAdjacentMats(Material.ICE_SOLID);
		int snow = countAdjacentMats(Material.SNOW);
		int fire = countAdjacentMats(Material.FIRE);
		
		int hot = fire + lava * 2;
		int cold = snow + packedIce * 2 + ice * 2 + water;
		temperatureLimit = (float) (0.35 + (hot * 0.65) - (cold * 0.65));


		if (this.isBurning && tank.getFluid() != null && tank.getFluidAmount() > 0) {

			System.out.println("GAS " + tank.getFluidAmount());
			tank.drain(1, FluidAction.EXECUTE);
			System.out.println("GAZ " + tank.getFluidAmount());
			if (this.voltage < this.capacity) {
				this.storePower(getPowerPerUpdate());
			}
			if (temperature < temperatureLimit) {

				temperature += (0.0002 * hot);
			}
			if(!level.isClientSide() && ++soundCounter >= 90) {
				level.playSound(null, worldPosition, ModSounds.MINIBIKE_IDLE.get(), SoundCategory.BLOCKS, MathUtils.getFloatInRange(0.95f, 1.05f), MathUtils.getFloatInRange(0.95f, 1.05f));
				soundCounter = 0;
			}

			flag = true;
		}
		if (temperature > 0) {
			temperature -= (0.00001 * ((2 * cold) + air));
		}

		if (temperature > 1) {
			this.level.explode((Entity) null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), 2, true,Explosion.Mode.BREAK);
		}
		if (tank.getFluidAmount() < tank.getCapacity()) {
			ItemStack stack = this.getInventory().getStackInSlot(0);
			if (!stack.isEmpty()) {
				Item item = stack.getItem();
				/*if (item instanceof UniversalBucket) {
					UniversalBucket bucket = (UniversalBucket) item;
					FluidStack fluidStack = bucket.getFluid(stack);
					if (fluidStack.getFluid() == ModFluids.GASOLINE.get()) {
						if (fluidStack.getAmount() <= tank.getCapacity() - tank.getFluidAmount()) {
							tank.fill(fluidStack, FluidAction.EXECUTE);
							this.getInventory().setStackInSlot(0, new ItemStack(Items.BUCKET));
						}
					}
				}
				else*/ if (item == ModItems.GAS_CANISTER.get()) {
					if (250 <= tank.getCapacity() - tank.getFluidAmount()) {
						tank.fill(new FluidStack(ModFluids.GASOLINE.get(),250), FluidAction.EXECUTE);
						stack.shrink(1);
						if(stack.getCount() <= 0) {
							this.getInventory().setStackInSlot(0, ItemStack.EMPTY);
						}
					}
				}
			}
		}

		if (this.isBurning && tank.getFluidAmount() <= 0) {
			isBurning = false;
			tank.setFluid(new FluidStack(ModFluids.GASOLINE.get(), 0));
		}
		if (temperature < 0) {
			temperature = 0;
		}

		Iterator<ElectricConnection> iterator = outputs.iterator();
		while (iterator.hasNext()) {
			ElectricConnection connection = iterator.next();
			IVoltage voltage = connection.getTo(level);
			if (voltage != null) {
				long l = voltage.tryToSendPower(getOutputForConnection(connection),connection);
				this.voltage -= l;
				if (l != 0) {
					flag = true;
				}
			} else {
				iterator.remove();
				flag = true;
			}
		}

		iterator = inputs.iterator();
		while (iterator.hasNext()) {
			ElectricConnection connection = iterator.next();
			IVoltage voltage = connection.getFrom(level);
			if (voltage == null) {
				iterator.remove();
				flag = true;
			}
		}

		if (flag) {
			this.markForUpdate();
		}
	}


	public float getFluidPercentage() {
		return (float) getTank().getFluidAmount() / (float) getTank().getCapacity();
	}

	public int getFluidGuiHeight(int maxHeight) {
		return (int) Math.ceil(getFluidPercentage() * (float) maxHeight);
	}

	public FluidTank getTank() {
		return tank;
	}

	public void setTank(FluidTank tank) {
		this.tank = tank;
	}
	
	public boolean isBurning() {
		return this.burnTime > 0;
	}

	@Override
	public void onContainerOpened(PlayerEntity player) {
		
	}

	@Override
	public void onContainerClosed(PlayerEntity player) {
		
	}

	@Override
	public ResourceLocation getLootTable() {
		return null;
	}
	
	@Override
	public boolean disconnect(IVoltage voltage) {
		for(ElectricConnection input : getInputs()) {
			if(input.getFrom().equals(voltage.getPos())) {
				this.inputs.remove(input);
				markForUpdate();
				return true;
			}
		}
		
		for(ElectricConnection output : getOutputs()) {
			if(output.getTo().equals(voltage.getPos())) {
				this.outputs.remove(output);
				markForUpdate();
				return true;
			}
		}
		return false;
	}
	public void markForUpdate() {
		level.sendBlockUpdated(worldPosition, level.getBlockState(this.worldPosition),
				level.getBlockState(worldPosition), 3);
		setChanged();
	}

	public void switchGenerator(){
		this.isBurning = !this.isBurning;
		this.setChanged();
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
	public BlockPos getPos() {
		return worldPosition;
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
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return ContainerGasGenerator.createContainerServerSide(windowID, playerInventory, this);
	}
}
