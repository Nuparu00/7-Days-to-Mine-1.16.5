package nuparu.sevendaystomine.network.packets;

import java.io.File;
import java.util.function.Supplier;

import org.apache.commons.io.FilenameUtils;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.util.photo.PhotoCatcherServer;

public class PhotoToServerMessage {

	private byte[] bytes;
	private int index;
	private int parts;
	private String id;

	public PhotoToServerMessage() {

	}

	public PhotoToServerMessage(byte[] bytes, int index, int parts, String id) {
		this.bytes = bytes;
		this.index = index;
		this.parts = parts;
		this.id = id;
	}

	public static void encode(PhotoToServerMessage msg, PacketBuffer buf) {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putByteArray("image", msg.bytes);
		nbt.putInt("index", msg.index);
		nbt.putInt("parts", msg.parts);
		nbt.putString("id", msg.id);
		buf.writeNbt(nbt);
		
	}

	public static PhotoToServerMessage decode(PacketBuffer buf) {
		CompoundNBT nbt = buf.readNbt();
		byte[] bytes = nbt.getByteArray("image");
		int index = nbt.getInt("index");
		int parts = nbt.getInt("parts");
		String id = nbt.getString("id");
		
		return new PhotoToServerMessage(bytes,index,parts,id);
	}

	public static class Handler {

		public static void handle(PhotoToServerMessage msg, Supplier<NetworkEvent.Context> ctx) {

			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				if (!CommonConfig.allowPhotos.get()) return;
				ServerPlayerEntity player = ctx.get().getSender();

				File file = PhotoCatcherServer.addBytesToMap(msg.bytes, msg.id, msg.parts,
						msg.index, player.getName().getString().toLowerCase());

				if (file == null)
					return;

				player.level.playSound(null, player.blockPosition(), ModSounds.CAMERA_TAKE.get(), SoundCategory.PLAYERS, 0.3F,
						1.0F / (player.level.random.nextFloat() * 0.4F + 1.2F) + 1f * 0.5F);
				ItemStack stack = new ItemStack(ModItems.PHOTO.get());
				stack.getOrCreateTag().putString("path", FilenameUtils.getName(file.getPath()));
				if (!player.inventory.add(stack)) {
					player.drop(stack, false);
				}
			});
		}
	}

}
