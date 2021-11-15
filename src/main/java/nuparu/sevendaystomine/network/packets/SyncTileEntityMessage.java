package nuparu.sevendaystomine.network.packets;

import java.util.function.Supplier;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.SevenDaysToMine;

public class SyncTileEntityMessage {

	CompoundNBT data;
	BlockPos pos;

	public SyncTileEntityMessage() {

	}

	public SyncTileEntityMessage(CompoundNBT data, BlockPos pos) {
		this.data = data;
		this.pos = pos;
	}

	public static void encode(SyncTileEntityMessage msg, PacketBuffer buf) {
		buf.writeNbt(msg.data);
		buf.writeBlockPos(msg.pos);
	}

	public static SyncTileEntityMessage decode(PacketBuffer buf) {
		return new SyncTileEntityMessage(buf.readNbt(), buf.readBlockPos());
	}

	public static class Handler {

		public static void handle(SyncTileEntityMessage msg, Supplier<NetworkEvent.Context> ctx) {

			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				BlockPos pos = msg.pos;
				CompoundNBT nbt = msg.data;
				World world = SevenDaysToMine.proxy.getWorld();
				if (world == null) {
					return;
				}
				TileEntity te = world.getBlockEntity(pos);
				if (te == null) {
					return;
				}
				te.deserializeNBT(nbt);
			});
		}
	}

}
