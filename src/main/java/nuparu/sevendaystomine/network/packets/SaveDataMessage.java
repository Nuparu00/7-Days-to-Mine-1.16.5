package nuparu.sevendaystomine.network.packets;

import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.item.ItemCircuit;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;

public class SaveDataMessage{

	String data;
	BlockPos pos;

	public SaveDataMessage() {

	}

	public SaveDataMessage(String data, BlockPos pos) {
		this.data = data;
		this.pos = pos;
	}

	public static void encode(SaveDataMessage msg, PacketBuffer buf) {
		buf.writeUtf(msg.data);
		buf.writeBlockPos(msg.pos);

	}

	public static SaveDataMessage decode(PacketBuffer buf) {
		return new SaveDataMessage(buf.readUtf(), buf.readBlockPos());
	}

	public static class Handler {

		public static void handle(SaveDataMessage msg, Supplier<NetworkEvent.Context> ctx) {

			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				ServerPlayerEntity player = ctx.get().getSender();
				World world = player.level;
				
				BlockPos pos = msg.pos;
				
				
				if(player.distanceToSqr((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
							(double) pos.getZ() + 0.5D) > 64.0D) {
					return;
				}
				
				String data = msg.data;
				if (world == null) {
					return;
				}

				TileEntity te = world.getBlockEntity(pos);
				if (te == null || !(te instanceof TileEntityComputer)) {
					return;
				}

				TileEntityComputer computer = (TileEntityComputer) te;
				if (!computer.isCompleted() || !computer.isOn() /*|| !computer.canOpen(player)*/) {
					return;
				}

				ItemStack stack = computer.getInventory().getStackInSlot(6);
				if (stack.isEmpty() || !(stack.getItem() instanceof ItemCircuit)) {
					return;
				}
				stack.getOrCreateTag().putString("data", data);

			});
		}
	}
}
