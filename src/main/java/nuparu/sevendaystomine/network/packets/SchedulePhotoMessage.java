package nuparu.sevendaystomine.network.packets;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.SevenDaysToMine;

public class SchedulePhotoMessage{
	
	public SchedulePhotoMessage() {
		
	}
	
	public static void encode(SchedulePhotoMessage msg, PacketBuffer buf) {
	}

	public static SchedulePhotoMessage decode(PacketBuffer buf) {
		return new SchedulePhotoMessage();
	}
	
	public static class Handler {

		public static void handle(SchedulePhotoMessage msg, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				SevenDaysToMine.proxy.schedulePhoto();
			});
		}
	}
}
