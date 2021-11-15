package nuparu.sevendaystomine.network.packets;

import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;

public class SendRedstoneSignalMessage {

	byte strength;
	BlockPos pos;
	Direction facing;

	public SendRedstoneSignalMessage() {

	}

	public SendRedstoneSignalMessage(byte strength, BlockPos pos, Direction facing) {
		this.strength = strength;
		this.pos = pos;
		this.facing = facing;
	}
	

	public SendRedstoneSignalMessage(int strength, BlockPos pos, Direction facing) {
		this((byte)strength,pos,facing);
	}

	public static void encode(SendRedstoneSignalMessage msg, PacketBuffer buf) {
		buf.writeByte(msg.strength);
		buf.writeBlockPos(msg.pos);
		buf.writeUtf(msg.facing.getName());
	}

	public static SendRedstoneSignalMessage decode(PacketBuffer buf) {
		return new SendRedstoneSignalMessage(buf.readByte(),buf.readBlockPos(),Direction.byName(buf.readUtf()));
	}
	
	public static class Handler {

		public static void handle(SendRedstoneSignalMessage msg, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				ServerPlayerEntity player = ctx.get().getSender();
				World world = player.level;
				
				BlockPos pos = msg.pos;
				
				TileEntity te = world.getBlockEntity(pos);
				if(te == null || !(te instanceof TileEntityComputer)) return;

				if(player.distanceToSqr((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
							(double) pos.getZ() + 0.5D) > 64.0D) {
					return;
				}

				TileEntityComputer computer = (TileEntityComputer)te;
				//computer.updateRedstoneSignal(msg.facing, msg.strength);
			});
		}
	}

}
