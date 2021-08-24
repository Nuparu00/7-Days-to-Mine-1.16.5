package nuparu.sevendaystomine.network.packets;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IChunkData;
import nuparu.sevendaystomine.events.ClientEventHandler;

public class BreakSyncTrackingMessage {
	CompoundNBT data;
	BlockPos pos;

	public BreakSyncTrackingMessage() {

	}

	public BreakSyncTrackingMessage(CompoundNBT data, BlockPos pos) {
		this.data = data;
		this.pos = pos;
	}

	public static void encode(BreakSyncTrackingMessage msg, PacketBuffer buf) {
		buf.writeNbt(msg.data);
		buf.writeBlockPos(msg.pos);
	}

	public static BreakSyncTrackingMessage decode(PacketBuffer buf) {
		return new BreakSyncTrackingMessage(buf.readNbt(),buf.readBlockPos());
	}

	public static class Handler {

		public static void handle(BreakSyncTrackingMessage msg, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				Minecraft mc = Minecraft.getInstance();
				World world = mc.level;
				
				
				ClientEventHandler.cachedChunks.put(msg.pos,msg.data);

				/*BreakSavedData data = BreakSavedData.get(world);
				if (data != null && msg.data != null) {
					data.readFromNBT(msg.data);
				}*/
			});
			ctx.get().setPacketHandled(true);
		}
	}
}