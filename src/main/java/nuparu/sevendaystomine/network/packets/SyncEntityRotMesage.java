package nuparu.sevendaystomine.network.packets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.entity.human.EntityHuman;

import java.util.function.Supplier;

public class SyncEntityRotMesage {

	private int entityID;
	private double yRot;

	public SyncEntityRotMesage() {

	}

	public SyncEntityRotMesage(double yRot, Entity entity) {

		this.entityID = entity.getId();
		this.yRot = yRot;
	}

	public SyncEntityRotMesage(double yRot, int id) {

		this.entityID = id;
		this.yRot = yRot;
	}

	public static void encode(SyncEntityRotMesage msg, PacketBuffer buf) {
		buf.writeDouble(msg.yRot);
		buf.writeInt(msg.entityID);
	}

	public static SyncEntityRotMesage decode(PacketBuffer buf) {
		return new SyncEntityRotMesage(buf.readDouble(), buf.readInt());
	}

	public static class Handler {

		public static void handle(SyncEntityRotMesage msg, Supplier<NetworkEvent.Context> ctx) {

			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				double yRot = msg.yRot;
				int entityID = msg.entityID;

				World world = SevenDaysToMine.proxy.getWorld();
				Entity entity = world.getEntity(entityID);
				System.out.println("HIddH " + world.getEntity(entityID));
				if (entity == null)
					return;
				entity.yRot = (float) yRot;
				entity.yRotO = (float) yRot;
				System.out.println("HIH");

			});
		}
	}

}
