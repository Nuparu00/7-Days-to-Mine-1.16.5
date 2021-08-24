package nuparu.sevendaystomine.entity.ai;

import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IChunkData;
import nuparu.sevendaystomine.entity.ZombieBaseEntity;
import nuparu.sevendaystomine.util.Utils;

public class GoalBreakBlocks extends Goal {
	Block block;
	BlockPos blockPosition = BlockPos.ZERO;
	ZombieBaseEntity zombie;
	float stepSoundTickCounter;
	
	public GoalBreakBlocks(ZombieBaseEntity zombie){
		this.zombie = zombie;
	}

	@Override
	public boolean canUse() {
		//if (!net.minecraftforge.common.ForgeHooks.canEntityDestroy(this.zombie.level, this.blockPos, this.zombie))  return false;
		if(zombie.getTarget() != null) {
		
			BlockRayTraceResult blockRay = Utils.rayTrace(zombie.level, zombie, 2.5, FluidMode.NONE);
			if(blockRay != null && blockRay.getBlockPos() != null) {
				this.blockPosition = blockRay.getBlockPos();
				this.block = zombie.level.getBlockState(this.blockPosition).getBlock();
				return (block != null);
			}
		}
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		if(!(zombie.level instanceof ServerWorld)) return;
		ServerWorld world = (ServerWorld) zombie.level;
		if(world == null || blockPosition == null) return;
		Chunk chunk = world.getChunkAt(blockPosition);
		if(chunk == null) return;
		IChunkData ichunkdata = CapabilityHelper.getChunkData(chunk);

		BlockState state = world.getBlockState(blockPosition);
		float hardness = state.getDestroySpeed(world, blockPosition);
		if (state.getMaterial() != Material.AIR && hardness >= 0) {

			if (stepSoundTickCounter % 4.0F == 0.0F) {
				zombie.swing(world.random.nextInt(2) == 0 ? Hand.MAIN_HAND : Hand.OFF_HAND);
			}
			float m = (zombie.getDigSpeed(state, blockPosition) / hardness) / 32f;

			if (Utils.damageBlock(world, blockPosition, m, true, this.stepSoundTickCounter % 4.0F == 0.0F)) {
				world.levelEvent(2001, blockPosition, Block.getId(world.getBlockState(this.blockPosition)));
				this.stop();

			}
			++this.stepSoundTickCounter;
		}
	}

}
