package nuparu.sevendaystomine.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class ChunkDataStorage implements Capability.IStorage<IChunkData> {

	@Override
	public INBT writeNBT(Capability<IChunkData> capability, IChunkData instance, Direction side) {
		CompoundNBT nbt = new CompoundNBT();
		instance.writeToNBT(nbt);
		return nbt;
	}

	@Override
	public void readNBT(Capability<IChunkData> capability, IChunkData instance, Direction side, INBT nbt) {
		if (nbt instanceof CompoundNBT) {
			CompoundNBT tag = (CompoundNBT) nbt;
			instance.readFromNBT(tag);
		}
	}
}