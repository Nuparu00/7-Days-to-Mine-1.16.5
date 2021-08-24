package nuparu.sevendaystomine.network.packets;

import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.electricity.network.INetwork;

public class SendPacketMessage{

	String packet;
	BlockPos to;
	BlockPos from;

	public SendPacketMessage() {

	}

	public SendPacketMessage(BlockPos from,BlockPos to,String packet) {
		this.from = from;
		this.to = to;
		this.packet = packet;
	}

	public static void encode(SendPacketMessage msg, PacketBuffer buf) {
		buf.writeUtf(msg.packet);
		buf.writeBlockPos(msg.to);
		buf.writeBlockPos(msg.from);

	}

	public static SendPacketMessage decode(PacketBuffer buf) {
		return new SendPacketMessage(buf.readBlockPos(),buf.readBlockPos(),buf.readUtf());
	}

	public static class Handler {

		public static void handle(SendPacketMessage msg, Supplier<NetworkEvent.Context> ctx) {

			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				ServerPlayerEntity player = ctx.get().getSender();
				World world = player.level;
				
				BlockPos fromPos = msg.from;
				BlockPos toPos = msg.to;
				
				TileEntity fromTE = world.getBlockEntity(fromPos);
				if(fromTE == null || !(fromTE instanceof INetwork)) return;
				TileEntity toTE = world.getBlockEntity(toPos);
				if(toTE == null || !(toTE instanceof INetwork)) return;

				if(player.distanceToSqr((double) fromPos.getX() + 0.5D, (double) fromPos.getY() + 0.5D,
							(double) fromPos.getZ() + 0.5D) > 64.0D) {
					return;
				}

				INetwork from = (INetwork)fromTE;
				INetwork to = (INetwork)toTE;
				if(!from.isConnectedTo(to)) return;
				to.sendPacket(msg.packet,from,player);
			});
		}
	}

}
