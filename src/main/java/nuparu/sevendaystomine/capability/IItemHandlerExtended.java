package nuparu.sevendaystomine.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.INameable;
import net.minecraftforge.items.IItemHandler;

public interface IItemHandlerExtended extends IItemHandler,INameable{
	public void copy(IItemHandler from);
    public CompoundNBT serializeNBT();
    public void deserializeNBT(CompoundNBT nbt);
    public void setSize(int size);
}
