package nuparu.sevendaystomine.network.packets;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.computer.process.TickingProcess;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;

public class KillProcessMessage{

	private BlockPos pos;
	private UUID uuid;
	
	public KillProcessMessage() {
		
	}
	
	public KillProcessMessage(BlockPos pos, UUID uuid) {
		this.pos = pos;
		this.uuid = uuid;
	}
	public static void encode(KillProcessMessage msg, PacketBuffer buf) {
		buf.writeBlockPos(msg.pos);
		buf.writeUUID(msg.uuid);

	}

	public static KillProcessMessage decode(PacketBuffer buf) {
		return new KillProcessMessage(buf.readBlockPos(), buf.readUUID());
	}

	public static class Handler {

		public static void handle(KillProcessMessage msg, Supplier<NetworkEvent.Context> ctx) {

			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				ServerPlayerEntity player = ctx.get().getSender();
				World world = player.level;

				BlockPos pos = msg.pos;
				UUID id = msg.uuid;

				TileEntity TE = world.getBlockEntity(pos);

				if (TE != null && TE instanceof TileEntityComputer) {
					TileEntityComputer computerTE = (TileEntityComputer) TE;
					if (computerTE.getMonitorTE() != null && computerTE.getMonitorTE().getLookingPlayers().contains(player)) {
						TickingProcess process = computerTE.getProcessByUUID(id);
						if (process != null) {
							computerTE.killProcess(process);
						}
					}
				}

			});
		}
	}
}
