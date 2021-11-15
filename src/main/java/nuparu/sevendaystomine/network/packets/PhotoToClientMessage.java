package nuparu.sevendaystomine.network.packets;

import java.io.File;
import java.util.function.Supplier;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.client.util.ResourcesHelper;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.util.photo.PhotoCatcherClient;

public class PhotoToClientMessage{

	private byte[] bytes;
	private int index;
	private int parts;
	private String name;

	public PhotoToClientMessage() {

	}

	public PhotoToClientMessage(byte[] bytes, int index, int parts, String name) {
		this.bytes = bytes;
		this.index = index;
		this.parts = parts;
		this.name = name;
	}

	public static void encode(PhotoToClientMessage msg, PacketBuffer buf) {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putByteArray("image", msg.bytes);
		nbt.putInt("index", msg.index);
		nbt.putInt("parts", msg.parts);
		nbt.putString("id", msg.name);
		buf.writeNbt(nbt);
		
	}

	public static PhotoToClientMessage decode(PacketBuffer buf) {
		CompoundNBT nbt = buf.readNbt();
		byte[] bytes = nbt.getByteArray("image");
		int index = nbt.getInt("index");
		int parts = nbt.getInt("parts");
		String id = nbt.getString("id");
		
		return new PhotoToClientMessage(bytes,index,parts,id);
	}

	public static class Handler {

		public static void handle(PhotoToClientMessage msg, Supplier<NetworkEvent.Context> ctx) {

			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				if (!CommonConfig.allowPhotos.get()) return;
				File file = PhotoCatcherClient.addBytesToMap(msg.bytes, msg.parts, msg.index,
						msg.name);

				if (file == null)
					return;
				System.out.println(msg.name);
				ResourcesHelper.INSTANCE.addFile(file, msg.name);
			});
		}
	}

}
