package nuparu.sevendaystomine.capability;

import java.util.HashMap;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.block.repair.BreakData;

public interface IChunkData {
	
	public BreakData getBreakData(BlockPos pos);
	
	public BreakData setBreaakData(BlockPos pos, BreakData data); 
	
	public BreakData setBreaakData(BlockPos pos, float data);

	public BreakData addBreakData(BlockPos pos, float delta);

	public void removebreakData(BlockPos pos);

	public void removebreakData(BreakData data);
	
	public void update(ServerWorld world);
	
	public void syncTo(ServerPlayerEntity player);
	
	public void readFromNBT(CompoundNBT nbt);
	
	public CompoundNBT writeToNBT(CompoundNBT nbt);
	
	public HashMap<BlockPos, BreakData> getData();
	
	public void markDirty();
	
	public void setOwner(Chunk chunk);

}
