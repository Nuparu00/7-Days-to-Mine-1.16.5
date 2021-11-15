package nuparu.sevendaystomine.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.INameable;
import net.minecraftforge.items.IItemHandler;

public interface IItemHandlerExtended extends IItemHandler,INameable{
	void copy(IItemHandler from);
    CompoundNBT serializeNBT();
    void deserializeNBT(CompoundNBT nbt);
    void setSize(int size);
}
