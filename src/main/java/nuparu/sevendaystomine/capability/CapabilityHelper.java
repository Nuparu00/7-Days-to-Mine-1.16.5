package nuparu.sevendaystomine.capability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.chunk.Chunk;

public class CapabilityHelper {

	public static IExtendedPlayer getExtendedPlayer(PlayerEntity player) {
		return player.getCapability(ExtendedPlayerProvider.EXTENDED_PLAYER_CAP, null).orElse(null);
	}

	public static IItemHandlerExtended getExtendedInventory(PlayerEntity player) {
		return player.getCapability(ExtendedInventoryProvider.EXTENDED_INV_CAP, null).orElse(null);
	}

	public static IChunkData getChunkData(Chunk chunk) {
		return (ChunkDataProvider.CHUNK_DATA_CAP == null || chunk == null) ? null : chunk.getCapability(ChunkDataProvider.CHUNK_DATA_CAP, null).orElse(null);
	}
}