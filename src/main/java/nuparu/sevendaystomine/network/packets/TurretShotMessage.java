package nuparu.sevendaystomine.network.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.SevenDaysToMine;

import java.util.function.Supplier;

public class TurretShotMessage {


	protected double posX;
	protected double posY;
	protected double posZ;


	public TurretShotMessage() {

	}

	public TurretShotMessage(double posX, double posY, double posZ) {
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}

	public static void encode(TurretShotMessage msg, PacketBuffer buf) {
		buf.writeDouble(msg.posX);
		buf.writeDouble(msg.posY);
		buf.writeDouble(msg.posZ);

	}

	public static TurretShotMessage decode(PacketBuffer buf) {
		return new TurretShotMessage(buf.readDouble(),buf.readDouble(),buf.readDouble());
	}

	public static class Handler {

		public static void handle(TurretShotMessage msg, Supplier<NetworkEvent.Context> ctx) {

			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				World world = SevenDaysToMine.proxy.getWorld();
				world.addParticle(ParticleTypes.SMOKE, msg.posX, msg.posY - 0.2,
						msg.posZ, 0.0D, 0.075D, 0.0D);
				world.addParticle(ParticleTypes.FLAME, msg.posX, msg.posY - 0.2,
						msg.posZ, 0.0D, 0.0D, 0.0D);
			});
		}
	}
}
