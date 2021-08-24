package nuparu.sevendaystomine.network.packets;

import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.entity.human.EntityHuman;
import nuparu.sevendaystomine.util.dialogue.Subtitle;
import nuparu.sevendaystomine.util.dialogue.SubtitleHelper;

public class AddSubtitleMessage{

	private int entityID;
	private String dialogue;
	private double duration;

	public AddSubtitleMessage() {

	}

	public AddSubtitleMessage(EntityHuman entity, String dialogue, double duration) {
		this.entityID = entity.getId();
		this.dialogue = dialogue;
		this.duration = duration;
	}
	
	public AddSubtitleMessage(int entity, String dialogue, double duration) {
		this.entityID = entity;
		this.dialogue = dialogue;
		this.duration = duration;
	}

	public static void encode(AddSubtitleMessage msg, PacketBuffer buf) {
		buf.writeInt(msg.entityID);
		buf.writeUtf(msg.dialogue);
		buf.writeDouble(msg.duration);

	}

	public static AddSubtitleMessage decode(PacketBuffer buf) {
		return new AddSubtitleMessage(buf.readInt(), buf.readUtf(), buf.readDouble());
	}

	public static class Handler {

		public static void handle(AddSubtitleMessage msg, Supplier<NetworkEvent.Context> ctx) {

			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				int entityID = msg.entityID;
				double duration = msg.duration;
				String dialogue = msg.dialogue;

				PlayerEntity player = SevenDaysToMine.proxy.getPlayerEntityFromContext(ctx);
				World world = player.level;

				Entity entity = world.getEntity(entityID);
				if (entity == null || !(entity instanceof EntityHuman))
					return;

				EntityHuman human = (EntityHuman) entity;

				SubtitleHelper.INSTANCE.addSubtitleToQueue(new Subtitle(human, dialogue, duration));
			});
		}
	}

}
