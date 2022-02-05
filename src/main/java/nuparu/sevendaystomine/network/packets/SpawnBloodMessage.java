package nuparu.sevendaystomine.network.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.ClientConfig;
import nuparu.sevendaystomine.init.ModParticleTypes;

import java.util.function.Supplier;

public class SpawnBloodMessage {


    protected double posX;
    protected double posY;
    protected double posZ;
    protected double motionX;
    protected double motionY;
    protected double motionZ;


    public SpawnBloodMessage() {

    }

    public SpawnBloodMessage(double posX, double posY, double posZ, double motionX, double motionY, double motionZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
    }

    public static void encode(SpawnBloodMessage msg, PacketBuffer buf) {
        buf.writeDouble(msg.posX);
        buf.writeDouble(msg.posY);
        buf.writeDouble(msg.posZ);
        buf.writeDouble(msg.motionX);
        buf.writeDouble(msg.motionY);
        buf.writeDouble(msg.motionZ);

    }

    public static SpawnBloodMessage decode(PacketBuffer buf) {
        return new SpawnBloodMessage(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    public static class Handler {

        public static void handle(SpawnBloodMessage msg, Supplier<NetworkEvent.Context> ctx) {

            ctx.get().enqueueWork(() -> {
                ctx.get().setPacketHandled(true);
                World world = SevenDaysToMine.proxy.getWorld();
                if (ClientConfig.bloodParticles.get()) {
                    world.addParticle(ModParticleTypes.BLOOD.get(), msg.posX, msg.posY, msg.posZ, msg.motionX, msg.motionY, msg.motionZ);
                }
            });
        }
    }
}
