package nuparu.sevendaystomine.network.packets;

import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class HonkMessage {

	public static void encode(HonkMessage msg, PacketBuffer buf) {
	}

	public static HonkMessage decode(PacketBuffer buf) {
		return new HonkMessage();
	}
	
	public static class Handler {

		public static void handle(HonkMessage msg, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				ServerPlayerEntity player = ctx.get().getSender();
				Entity riding = player.getVehicle();
				/*if(riding != null && riding instanceof EntityCar) {
					if(riding.getPassengers().indexOf(player) == 0) {
						System.out.println("Beep beep beep");
						player.level.playSound(null,player.getX(), player.getY(), player.getZ(), ModSounds.HONK, SoundCategory.PLAYERS, player.level.random.nextFloat()*0.2f+1.4f, player.level.random.nextFloat()*0.05f+0.8f);
					}
				}*/
			});
		}
	}
}
