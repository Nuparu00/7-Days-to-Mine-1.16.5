package nuparu.sevendaystomine.capability;

import java.util.concurrent.Callable;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ExtendedInventoryProvider implements ICapabilitySerializable<CompoundNBT> {
    @CapabilityInject(IItemHandlerExtended.class)
    public static Capability<IItemHandlerExtended> EXTENDED_INV_CAP = null;
    protected LazyOptional<IItemHandlerExtended> instance = LazyOptional.of(EXTENDED_INV_CAP::getDefaultInstance);
    

    public static void register() {
        CapabilityManager.INSTANCE.register(IItemHandlerExtended.class, new ExtendedInventoryStorage(), new Callable<IItemHandlerExtended>() {

            @Override
            public IItemHandlerExtended call() throws Exception {
                return new ExtendedInventory();
            }
        });
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return cap == EXTENDED_INV_CAP ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) EXTENDED_INV_CAP.getStorage().writeNBT(EXTENDED_INV_CAP, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null);

    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
    	EXTENDED_INV_CAP.getStorage().readNBT(EXTENDED_INV_CAP,this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")),null,nbt);
    }
    
	public ExtendedInventoryProvider setSize(int size) {
		instance.orElseGet(null).setSize(size);
		return this;
	}

}