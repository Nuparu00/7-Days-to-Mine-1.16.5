package nuparu.sevendaystomine.network.packets;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
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
				SevenDaysToMine.proxy.addRecoil(msg.recoil, Minecraft.getInstance().player);
				if (msg.flash) {
					if (msg.main) {
						RenderEventHandler.shotAnimationTimer = System.currentTimeMillis()+150;
						RenderEventHandler.mainMuzzleFlash = 8;
						RenderEventHandler.mainMuzzleFlashAngle=Minecraft.getInstance().level.random.nextDouble()*360;
					} else {
						RenderEventHandler.sideMuzzleFlash = 5;
						RenderEventHandler.sideMuzzleFlashAngle=Minecraft.getInstance().level.random.nextDouble()*360;
					}
				}
			});
		}
	}
}
