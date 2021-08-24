package nuparu.sevendaystomine.capability;

import java.util.concurrent.Callable;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ChunkDataProvider implements ICapabilitySerializable<CompoundNBT> {
    @CapabilityInject(IChunkData.class)
    public static Capability<IChunkData> CHUNK_DATA_CAP = null;
    protected LazyOptional<IChunkData> instance = LazyOptional.of(CHUNK_DATA_CAP::getDefaultInstance);

    public static void register() {
        CapabilityManager.INSTANCE.register(IChunkData.class, new ChunkDataStorage(), new Callable<IChunkData>() {

            @Override
            public IChunkData call() throws Exception {
                return new ChunkData();
            }
        });
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return cap == CHUNK_DATA_CAP ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) CHUNK_DATA_CAP.getStorage().writeNBT(CHUNK_DATA_CAP, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null);

    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
    	CHUNK_DATA_CAP.getStorage().readNBT(CHUNK_DATA_CAP,this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")),null,nbt);
    }

	public ChunkDataProvider setOwner(Chunk chunk) {
		instance.orElseGet(null).setOwner(chunk);
		return this;
	}

}