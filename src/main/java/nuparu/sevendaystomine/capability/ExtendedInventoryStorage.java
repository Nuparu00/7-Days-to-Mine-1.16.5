package nuparu.sevendaystomine.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class ExtendedInventoryStorage implements Capability.IStorage<IItemHandlerExtended> {

	@Override
	public INBT writeNBT(Capability<IItemHandlerExtended> capability, IItemHandlerExtended instance, Direction side) {
		return instance.serializeNBT();
	}

	@Override
	public void readNBT(Capability<IItemHandlerExtended> capability, IItemHandlerExtended instance, Direction side, INBT nbt) {
		if (nbt instanceof CompoundNBT) {
			CompoundNBT tag = (CompoundNBT) nbt;
			instance.deserializeNBT(tag);
		}
	}

}