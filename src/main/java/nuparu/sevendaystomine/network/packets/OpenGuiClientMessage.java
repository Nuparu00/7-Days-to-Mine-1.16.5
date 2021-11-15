package nuparu.sevendaystomine.network.packets;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.SevenDaysToMine;

public class OpenGuiClientMessage {

	int id;
	int x;
	int y;
	int z;

	public OpenGuiClientMessage() {

	}

	public OpenGuiClientMessage(int id, int x, int y, int z) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static void encode(OpenGuiClientMessage msg, PacketBuffer buf) {
		buf.writeInt(msg.id);
		buf.writeInt(msg.x);
		buf.writeInt(msg.y);
		buf.writeInt(msg.z);
	}

	public static OpenGuiClientMessage decode(PacketBuffer buf) {
		return new OpenGuiClientMessage(buf.readInt(),buf.readInt(),buf.readInt(),buf.readInt());
	}
	
	public static class Handler {

		public static void handle(OpenGuiClientMessage msg, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				PlayerEntity player = SevenDaysToMine.proxy.getPlayerEntityFromContext(ctx);
				if(player == null) {
					return;
				}
				SevenDaysToMine.proxy.openClientSideGui(msg.id,msg.x, msg.y, msg.z);
				//System.out.println("DO NO USE THIS");
				//NetworkHooks.openGui(player, containerSupplier);
				return;
			});
		}
	}
}
