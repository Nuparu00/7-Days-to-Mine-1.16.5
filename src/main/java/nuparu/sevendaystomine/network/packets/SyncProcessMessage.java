package nuparu.sevendaystomine.network.packets;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;

public class SyncProcessMessage{

	private BlockPos pos;
	private CompoundNBT nbt;
	
	public SyncProcessMessage() {
		
	}
	
	public SyncProcessMessage(BlockPos pos, CompoundNBT nbt) {
		this.pos = pos;
		this.nbt = nbt;
	}
	public static void encode(SyncProcessMessage msg, PacketBuffer buf) {
		buf.writeBlockPos(msg.pos);
		buf.writeNbt(msg.nbt);

	}

	public static SyncProcessMessage decode(PacketBuffer buf) {
		return new SyncProcessMessage(buf.readBlockPos(), buf.readNbt());
	}

	public static class Handler {

		public static void handle(SyncProcessMessage msg, Supplier<NetworkEvent.Context> ctx) {

			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				PlayerEntity player = ctx.get().getSender();
				World world = player.level;

				BlockPos pos = msg.pos;
				CompoundNBT nbt = msg.nbt;

				TileEntity TE = world.getBlockEntity(pos);

				if (TE != null && TE instanceof TileEntityComputer) {
					TileEntityComputer computerTE = (TileEntityComputer) TE;
					if (computerTE.getMonitorTE() != null && computerTE.getMonitorTE().getLookingPlayers().contains(player)) {
						//computerTE.startProcess(nbt,true);
					}
				}

			});
		}
	}

}
