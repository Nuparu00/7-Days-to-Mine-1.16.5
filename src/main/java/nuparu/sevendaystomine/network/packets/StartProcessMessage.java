package nuparu.sevendaystomine.network.packets;

import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;

public class StartProcessMessage{

	private BlockPos pos;
	private CompoundNBT nbt;
	
	public StartProcessMessage() {
		
	}
	
	public StartProcessMessage(BlockPos pos, CompoundNBT nbt) {
		this.pos = pos;
		this.nbt = nbt;
	}
	
	public static void encode(StartProcessMessage msg, PacketBuffer buf) {
		buf.writeBlockPos(msg.pos);
		buf.writeNbt(msg.nbt);
	}

	public static StartProcessMessage decode(PacketBuffer buf) {
		return new StartProcessMessage(buf.readBlockPos(),buf.readNbt());
	}

	public static class Handler {

		public static void handle(StartProcessMessage msg, Supplier<NetworkEvent.Context> ctx) {

			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				ServerPlayerEntity player = ctx.get().getSender();
				World world = player.level;

				BlockPos pos = msg.pos;
				CompoundNBT nbt = msg.nbt;

				TileEntity TE = world.getBlockEntity(pos);

				if (TE != null && TE instanceof TileEntityComputer) {
					TileEntityComputer computerTE = (TileEntityComputer) TE;
					if (computerTE.getMonitorTE() != null && computerTE.getMonitorTE().getLookingPlayers().contains(player)) {
						//computerTE.startProcess(nbt,false);
					}
				}
			});
		}
	}

}
