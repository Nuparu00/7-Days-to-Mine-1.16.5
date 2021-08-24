package nuparu.sevendaystomine.network.packets;

import java.util.function.Supplier;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.item.ItemAnalogCamera;
import nuparu.sevendaystomine.util.MathUtils;

public class CameraDimensionsMessage{

	double deltaWidth;
	double deltaHeight;
	double deltaZoom;

	public CameraDimensionsMessage() {

	}

	public CameraDimensionsMessage(double deltaWidth, double deltaHeight, double deltaZoom) {
		this.deltaWidth = deltaWidth;
		this.deltaHeight = deltaHeight;
		this.deltaZoom = deltaZoom;
	}

	public static void encode(CameraDimensionsMessage msg, PacketBuffer buf) {
		buf.writeDouble(msg.deltaWidth);
		buf.writeDouble(msg.deltaHeight);
		buf.writeDouble(msg.deltaZoom);
	}

	public static CameraDimensionsMessage decode(PacketBuffer buf) {
		return new CameraDimensionsMessage(buf.readDouble(),buf.readDouble(),buf.readDouble());
	}
	
	public static class Handler {

		public static void handle(CameraDimensionsMessage msg, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				ServerPlayerEntity player = ctx.get().getSender();
				ItemStack stack = player.getMainHandItem();
				if (stack.isEmpty())
					return;

				if (!(stack.getItem() instanceof ItemAnalogCamera))
					return;

				ItemAnalogCamera.setWidth(
						MathHelper.clamp(msg.deltaWidth + ItemAnalogCamera.getWidth(stack, player), 0.25, 1), stack,
						player);
				ItemAnalogCamera.setHeight(
						MathHelper.clamp(msg.deltaHeight + ItemAnalogCamera.getHeight(stack, player), 0.25, 1),
						stack, player);
				ItemAnalogCamera.setZoom(
						MathUtils.roundToNDecimal(
								MathHelper.clamp(msg.deltaZoom + ItemAnalogCamera.getZoom(stack, player), 1, 4), 1),
						stack, player);
			});
		}
	}
}
