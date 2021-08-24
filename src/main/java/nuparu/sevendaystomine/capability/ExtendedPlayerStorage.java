package nuparu.sevendaystomine.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class ExtendedPlayerStorage implements Capability.IStorage<IExtendedPlayer> {

	@Override
	public INBT writeNBT(Capability<IExtendedPlayer> capability, IExtendedPlayer instance, Direction side) {
		CompoundNBT nbt = new CompoundNBT();
		instance.writeNBT(nbt);
		return nbt;
	}

	@Override
	public void readNBT(Capability<IExtendedPlayer> capability, IExtendedPlayer instance, Direction side, INBT nbt) {
		if (nbt instanceof CompoundNBT) {
			CompoundNBT tag = (CompoundNBT) nbt;
			instance.readNBT(tag);
		}
	}
}