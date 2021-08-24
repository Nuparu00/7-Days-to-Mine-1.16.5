package nuparu.sevendaystomine.tileentity;

import java.util.Iterator;

import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.ForgeHooks;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.block.ContainerBatteryStation;
import nuparu.sevendaystomine.inventory.block.ContainerCombustionGenerator;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.util.MathUtils;

public class TileEntityCombustionGenerator extends TileEntityGeneratorBase {
	private static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.generator.combustion");

	public TileEntityCombustionGenerator() {
		super(ModTileEntities.COMBUSTION_GENERATOR.get());
	}

	@Override
	protected ItemHandlerNameable createInventory() {
		return new ItemHandlerNameable(1, DEFAULT_NAME);
	}
	
	@Override
	public void tick() {

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

		if (this.isBurning()) {
			--this.burnTime;
			if (voltage < capacity) {
				storePower(getPowerPerUpdate());
			}
			if (temperature < temperatureLimit) {

				temperature += (0.0002 * (hot+0.1));
			}
			if(!level.isClientSide() && ++soundCounter >= 90) {
				level.playSound(null, worldPosition, ModSounds.MINIBIKE_IDLE.get(), SoundCategory.BLOCKS, MathUtils.getFloatInRange(0.95f, 1.05f), MathUtils.getFloatInRange(0.95f, 1.05f));
				soundCounter = 0;
			}

			flag = true;
		}
		else if(!level.isClientSide()) {
			soundCounter = 90;
		}

		ItemStack itemstack = this.getInventory().getStackInSlot(0);
		if (!this.isBurning() && voltage < capacity) {
			this.burnTime = ForgeHooks.getBurnTime(itemstack);
			if (this.isBurning()) {
				if (!itemstack.isEmpty()) {
					Item item = itemstack.getItem();
					itemstack.shrink(1);

					if (itemstack.isEmpty()) {
						ItemStack item1 = item.getContainerItem(itemstack);
						this.getInventory().setStackInSlot(0, item1);
					}
				}
			} else if (isBurning) {
				isBurning = false;
			}
		}

		if (temperature > 0) {
			temperature -= (0.000001 * ((2 * cold) + air/2));
		}

		if (temperature > 1) {
			this.level.explode((Entity) null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), 2, true,Explosion.Mode.BREAK);
		}
		if (temperature < 0) {
			temperature = 0;
		}

		Iterator<ElectricConnection> iterator = outputs.iterator();
		while (iterator.hasNext()) {
			ElectricConnection connection = iterator.next();
			IVoltage voltage = connection.getTo(level);
			if (voltage != null) {
				long l = voltage.tryToSendPower(getOutputForConnection(connection), connection);
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
			this.setChanged();
		}
	}

	@Override
	public long getPowerPerUpdate() {
		long out = isBurning() ? (long) (basePower + (basePower * 8*temperature)) : 0;
		if (out > getMaxOutput()) {
			out = getMaxOutput();
		}
		return out;
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
	public BlockPos getPos() {
		return worldPosition;
	}

	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return ContainerCombustionGenerator.createContainerServerSide(windowID, playerInventory, this);
	}

}