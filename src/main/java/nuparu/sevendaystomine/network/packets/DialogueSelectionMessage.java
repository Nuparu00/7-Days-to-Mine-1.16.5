package nuparu.sevendaystomine.network.packets;

import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.entity.human.EntityHuman;

public class DialogueSelectionMessage {

	private int entityID;
	private String dialogue;

	public DialogueSelectionMessage() {

	}

	public DialogueSelectionMessage(String dialogue, EntityHuman entity) {

		this.entityID = entity.getId();
		this.dialogue = dialogue;
	}

	public DialogueSelectionMessage(String dialogue, int id) {

		this.entityID = id;
		this.dialogue = dialogue;
	}

	public static void encode(DialogueSelectionMessage msg, PacketBuffer buf) {
		buf.writeUtf(msg.dialogue);
		buf.writeInt(msg.entityID);
	}

	public static DialogueSelectionMessage decode(PacketBuffer buf) {
		return new DialogueSelectionMessage(buf.readUtf(), buf.readInt());
	}

	public static class Handler {

		public static void handle(DialogueSelectionMessage msg, Supplier<NetworkEvent.Context> ctx) {

			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				String dialogue = msg.dialogue;
				int entityID = msg.entityID;

				System.out.println("DDDD");
				ServerPlayerEntity player = ctx.get().getSender();
				World world = player.level;
				Entity entity = world.getEntity(entityID);
				if (entity == null || !(entity instanceof EntityHuman))
					return;

				System.out.println("DDDD");
				EntityHuman human = (EntityHuman) entity;
				if (human.distanceTo(player) > 6) {
					return;
				}
				System.out.println("DDDD");
				human.onDialogue(dialogue, player);

			});
		}
	}

}
