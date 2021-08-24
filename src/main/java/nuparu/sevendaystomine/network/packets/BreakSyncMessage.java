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

public class BreakSyncMessage {
	CompoundNBT data;
	BlockPos pos;

	public BreakSyncMessage() {

	}

	public BreakSyncMessage(CompoundNBT data, BlockPos pos) {
		this.data = data;
		this.pos = pos;
	}

	public static void encode(BreakSyncMessage msg, PacketBuffer buf) {
		buf.writeNbt(msg.data);
		buf.writeBlockPos(msg.pos);
	}

	public static BreakSyncMessage decode(PacketBuffer buf) {
		return new BreakSyncMessage(buf.readNbt(),buf.readBlockPos());
	}

	public static class Handler {

		public static void handle(BreakSyncMessage msg, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				Minecraft mc = Minecraft.getInstance();
				World world = mc.level;
				IChunk ichunk = world.getChunk(msg.pos);
				if(ichunk instanceof Chunk) {
					Chunk chunk = (Chunk)ichunk;
					IChunkData data = CapabilityHelper.getChunkData(chunk);
					data.readFromNBT(msg.data);
				}
				/*BreakSavedData data = BreakSavedData.get(world);
				if (data != null && msg.data != null) {
					data.readFromNBT(msg.data);
				}*/
			});
			ctx.get().setPacketHandled(true);
		}
	}
}