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

public class ExtendedPlayerProvider implements ICapabilitySerializable<CompoundNBT> {
    @CapabilityInject(IExtendedPlayer.class)
    public static Capability<IExtendedPlayer> EXTENDED_PLAYER_CAP = null;
    protected LazyOptional<IExtendedPlayer> instance = LazyOptional.of(EXTENDED_PLAYER_CAP::getDefaultInstance);

    public static void register() {
        CapabilityManager.INSTANCE.register(IExtendedPlayer.class, new ExtendedPlayerStorage(), new Callable<IExtendedPlayer>() {

            @Override
            public IExtendedPlayer call() throws Exception {
                return new ExtendedPlayer();
            }
        });
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return cap == EXTENDED_PLAYER_CAP ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) EXTENDED_PLAYER_CAP.getStorage().writeNBT(EXTENDED_PLAYER_CAP, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null);

    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
    	EXTENDED_PLAYER_CAP.getStorage().readNBT(EXTENDED_PLAYER_CAP,this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")),null,nbt);
    }

	public ExtendedPlayerProvider setOwner(PlayerEntity player) {
		instance.orElseGet(null).setOwner(player);
		return this;
	}

}