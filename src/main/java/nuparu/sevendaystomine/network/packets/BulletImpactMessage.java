package nuparu.sevendaystomine.network.packets;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class BulletImpactMessage {


	protected double posX;
	protected double posY;
	protected double posZ;
	protected double motionX;
	protected double motionY;
	protected double motionZ;
	protected BlockPos pos;
	
	
	public BulletImpactMessage() {

	}

	public BulletImpactMessage(double posX, double posY, double posZ, double motionX, double motionY, double motionZ, BlockPos pos) {
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
		this.pos = pos;
	}

	public static void encode(BulletImpactMessage msg, PacketBuffer buf) {
		buf.writeDouble(msg.posX);
		buf.writeDouble(msg.posY);
		buf.writeDouble(msg.posZ);
		buf.writeDouble(msg.motionX);
		buf.writeDouble(msg.motionY);
		buf.writeDouble(msg.motionZ);
		buf.writeBlockPos(msg.pos);

	}

	public static BulletImpactMessage decode(PacketBuffer buf) {
		return new BulletImpactMessage(buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readBlockPos());
	}

	public static class Handler {

		public static void handle(BulletImpactMessage msg, Supplier<NetworkEvent.Context> ctx) {

			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				World world = Minecraft.getInstance().level;
				for (int j = 0; j < 20; j++) {
					world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, world.getBlockState(msg.pos)).setPos(msg.pos),
							msg.posX + msg.motionX + world.random.nextDouble() * 0.2 - 0.1,
							msg.posY + msg.motionY + world.random.nextDouble() * 0.2 - 0.1,
							msg.posZ + msg.motionZ + world.random.nextDouble() * 0.2 - 0.1,
							(-2.5 + world.random.nextDouble() * 0.5) * msg.motionX,
							(-2.5 + world.random.nextDouble() * 0.5) * msg.motionY,
							(-2.5 + world.random.nextDouble() * 0.5) * msg.motionZ);
				}
			});
		}
	}
}
