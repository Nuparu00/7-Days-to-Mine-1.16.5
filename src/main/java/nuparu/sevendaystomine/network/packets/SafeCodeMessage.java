package nuparu.sevendaystomine.network.packets;

import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.tileentity.TileEntityCodeSafe;

public class SafeCodeMessage{

	BlockPos pos;
	int toAdd;
	
	public SafeCodeMessage() {

	}

	public SafeCodeMessage(BlockPos pos, int toAdd) {
		this.pos = pos;
		this.toAdd = toAdd;
	}

	public static void encode(SafeCodeMessage msg, PacketBuffer buf) {
		buf.writeBlockPos(msg.pos);
		buf.writeInt(msg.toAdd);
	}

	public static SafeCodeMessage decode(PacketBuffer buf) {
		return new SafeCodeMessage(buf.readBlockPos(),buf.readInt());
	}

	public static class Handler {

		public static void handle(SafeCodeMessage msg, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				BlockPos pos = msg.pos;
				int toAdd = msg.toAdd;
				
				if (pos == null || toAdd == 0) {
					return;
				}

				ServerPlayerEntity player = ctx.get().getSender();
				//player.getServer().addTickable(() -> {
					World world = player.level;
					if (world == null) {
						return;
					}
					TileEntity te = world.getBlockEntity(pos);
					if (te instanceof TileEntityCodeSafe) {
						TileEntityCodeSafe safe = (TileEntityCodeSafe) te;
						int selectedCode = safe.getSelectedCode();

						int h = (selectedCode / 100) % 10;
						int d = (selectedCode / 10) % 10;
						int u = selectedCode % 10;

						int absToAdd = Math.abs(toAdd);
						if (absToAdd <= 10) {
							u += (toAdd / 10);
							if (u < 0) {
								u = 9;
							} else if (u > 9) {
								u = 0;
							}
						} else if (absToAdd <= 100) {
							d += (toAdd / 100);
							if (d < 0) {
								d = 9;
							} else if (d > 9) {
								d = 0;
							}
						} else if (absToAdd <= 1000) {
							h += (toAdd / 1000);
							if (h < 0) {
								h = 9;
							} else if (h > 9) {
								h = 0;
							}
						}

						String codeInString = new StringBuilder().append(h).append(d).append(u).toString();
						int codeInInt = Integer.parseInt(codeInString);
						safe.setSelectedCode(codeInInt, player);
						CompoundNBT nbt = safe.save(new CompoundNBT());
						nbt.remove("CorrectCode");
						PacketManager.sendToDimension(PacketManager.syncTileEntity, new SyncTileEntityMessage(nbt, pos), world::dimension);

					}
				/*});*/
			});
		}
	}
}
