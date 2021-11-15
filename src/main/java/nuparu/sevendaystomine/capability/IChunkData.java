package nuparu.sevendaystomine.capability;

import java.util.HashMap;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.block.repair.BreakData;

public interface IChunkData {
	
	BreakData getBreakData(BlockPos pos);

	boolean hasBreakData(BlockPos pos);
	
	BreakData setBreakData(BlockPos pos, BreakData data);
	
	BreakData setBreakData(BlockPos pos, float data);

	BreakData addBreakData(BlockPos pos, float delta);

	void removeBreakData(BlockPos pos);

	void removeBreakData(BreakData data);
	
	void update(ServerWorld world);
	
	void syncTo(ServerPlayerEntity player);
	
	void readFromNBT(CompoundNBT nbt);
	
	CompoundNBT writeToNBT(CompoundNBT nbt);
	
	HashMap<BlockPos, BreakData> getData();
	
	void markDirty();
	
	void setOwner(Chunk chunk);

}
