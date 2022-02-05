package nuparu.sevendaystomine.mixin;

import net.minecraft.world.chunk.Chunk;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IChunkData;
import nuparu.sevendaystomine.util.ModConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(World.class)
public abstract class MixinWorld {

	@Shadow public abstract Chunk getChunkAt(BlockPos p_175726_1_);

	@Inject(method = "Lnet/minecraft/world/World;setBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z", at = @At("RETURN"), remap = ModConstants.REMAP)
	public void setBlock(BlockPos pos, BlockState state, int p_241211_3_, int p_241211_4_,
			CallbackInfoReturnable<Boolean> cir) {
		//System.out.println(p_241211_3_ + " " + p_241211_4_);
		if (!isClientSide() && p_241211_3_ != 20) {
			Chunk chunk = this.getChunkAt(pos);
			if (chunk != null) {
				IChunkData data = CapabilityHelper.getChunkData(chunk);
				if(data != null){
					data.removeBreakData(pos);
				}
			}
		}
	}

	@Shadow
	public boolean isClientSide() {
		throw new IllegalStateException("Mixin failed to shadow isClientSide()");
	}
}