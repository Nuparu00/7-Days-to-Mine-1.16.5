package nuparu.sevendaystomine.network.packets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.entity.CarEntity;
import nuparu.sevendaystomine.init.ModSounds;

import java.util.function.Supplier;

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
                if (riding instanceof CarEntity && ((CarEntity) riding).honkCooldown == 0 && riding.getPassengers().indexOf(player) == 0) {
                    System.out.println("Beep beep beep");
					((CarEntity) riding).honkCooldown = 15;
					player.level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.HONK.get(), SoundCategory.PLAYERS, player.level.random.nextFloat() * 0.2f + 1.4f, player.level.random.nextFloat() * 0.05f + 0.8f);
                }
            });
        }
    }
}
