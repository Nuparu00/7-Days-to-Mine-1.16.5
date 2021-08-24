package nuparu.sevendaystomine.capability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.block.repair.BreakData;
import nuparu.sevendaystomine.block.repair.BreakHelper;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.init.ModGameRules;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.BreakSyncMessage;

public class ChunkData implements IChunkData {

	private HashMap<BlockPos, BreakData> map = new HashMap<BlockPos, BreakData>();
	private long lastUpdate = 0;

	private Chunk owner;

	@Override
	public BreakData getBreakData(BlockPos pos) {
		return map.get(pos);
	}

	@Override
	public BreakData setBreaakData(BlockPos pos, BreakData data) {
		map.put(pos, data);
		markDirty();
		return data;
	}

	@Override
	public BreakData setBreaakData(BlockPos pos, float data) {
		BreakData breakData = new BreakData(owner.getWorldForge().getGameTime(), data);

		return setBreaakData(pos, breakData);
	}

	@Override
	public BreakData addBreakData(BlockPos pos, float delta) {
		BreakData breakData = this.getBreakData(pos);
		if (breakData == null) {
			breakData = new BreakData(owner.getWorldForge().getGameTime(), delta);
		} else {
			breakData.addState(delta).setLastChange(owner.getWorldForge().getGameTime());
		}
		return setBreaakData(pos, breakData);
	}

	@Override
	public void removebreakData(BlockPos pos) {
		map.remove(pos);
		markDirty();
	}

	@Override
	public void removebreakData(BreakData data) {
		map.values().remove(data);
		markDirty();
	}

	@Override
	public void update(ServerWorld world) {
		int decayRate = world.getGameRules().getInt(ModGameRules.damageDecayRate);
		if (decayRate <= 0) {
			decayRate = CommonConfig.damageDecayRate.get();
		}
		if (decayRate <= 0) {
			return;
		}
		if (Math.abs(world.getGameTime() - lastUpdate) <= decayRate)
			return;
		
		boolean dirty = false;
		
		@SuppressWarnings("unchecked")
		HashMap<BlockPos, BreakData> localMap = (HashMap<BlockPos, BreakData>)map.clone();
		ArrayList<BlockPos> toRemove = new ArrayList<BlockPos>();
		
		Iterator<Entry<BlockPos, BreakData>> it = localMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<BlockPos, BreakData> pair = it.next();
	        BreakData data = pair.getValue();
	        if (Math.abs(world.getGameTime() - data.getLastChange()) >= decayRate) {
				if (data.getState() > 0.4) {
					data.addState(0.1f);
					dirty = true;
					if (data.getState() >= 1) {
						world.destroyBlock(pair.getKey(), false);
						toRemove.add(pair.getKey());
						continue;
					}
				} else {
					data.addState(-0.1f);
					dirty = true;
					if (data.getState() <= 0) {
						toRemove.add(pair.getKey());
						continue;
					}
				}
				data.setLastChange(world.getGameTime());
			}
	        
	    }
	    
	    map.keySet().removeAll(toRemove);
	    lastUpdate = world.getGameTime();
		dirty = true;
		if (dirty) {
			markDirty();
		}
		
	}

	@Override
	public void syncTo(ServerPlayerEntity player) {
		PacketManager.sendTo(PacketManager.blockBreakSync, new BreakSyncMessage(this.writeToNBT(new CompoundNBT()),owner.getPos().getWorldPosition()),player);
	}

	@Override
	public void readFromNBT(CompoundNBT nbt) {
		lastUpdate = nbt.getLong("lastUpdate");
		map = BreakHelper.of(nbt.getCompound("map"));
	}

	@Override
	public CompoundNBT writeToNBT(CompoundNBT nbt) {
		nbt.putLong("lastUpdate", lastUpdate);
		nbt.put("map",BreakHelper.save(map));
		
		return nbt;
	}

	@Override
	public HashMap<BlockPos, BreakData> getData() {
		return (HashMap<BlockPos, BreakData>) this.map.clone();
	}

	@Override
	public void markDirty() {
		owner.markUnsaved();
		PacketManager.sendToChunk(PacketManager.blockBreakSync,  new BreakSyncMessage(this.writeToNBT(new CompoundNBT()),owner.getPos().getWorldPosition()), () -> owner);
	}
	

	public void setOwner(Chunk chunk) {
		this.owner = chunk;
	}

}
