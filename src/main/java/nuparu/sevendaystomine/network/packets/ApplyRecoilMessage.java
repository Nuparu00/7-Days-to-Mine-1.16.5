package nuparu.sevendaystomine.network.packets;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.events.RenderEventHandler;

public class ApplyRecoilMessage{


	protected float recoil;
	protected boolean main;
	protected boolean flash;
	public ApplyRecoilMessage() {

	}

	public ApplyRecoilMessage(float recoil, boolean main, boolean flash) {
		this.recoil = recoil;
		this.main = main;
		this.flash = flash;
	}

	public static void encode(ApplyRecoilMessage msg, PacketBuffer buf) {
		buf.writeFloat(msg.recoil);
		buf.writeBoolean(msg.main);
		buf.writeBoolean(msg.flash);

	}

	public static ApplyRecoilMessage decode(PacketBuffer buf) {
		return new ApplyRecoilMessage(buf.readFloat(), buf.readBoolean(), buf.readBoolean());
	}

	public static class Handler {

		public static void handle(ApplyRecoilMessage msg, Supplier<NetworkEvent.Context> ctx) {

			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				PlayerEntity player = SevenDaysToMine.proxy.getPlayerEntityFromContext(ctx);
				SevenDaysToMine.proxy.addRecoil(msg.recoil, player);
				RenderEventHandler.shotAnimationTimer = System.currentTimeMillis()+150;
				if (msg.flash) {
					if (msg.main) {
						RenderEventHandler.mainMuzzleFlash = 8;
						RenderEventHandler.mainMuzzleFlashAngle=player.level.random.nextDouble()*360;
					} else {
						RenderEventHandler.sideMuzzleFlash = 5;
						RenderEventHandler.sideMuzzleFlashAngle=player.level.random.nextDouble()*360;
					}
				}
			});
		}
	}
}
