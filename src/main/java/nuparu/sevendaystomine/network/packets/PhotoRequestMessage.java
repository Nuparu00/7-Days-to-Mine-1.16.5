package nuparu.sevendaystomine.network.packets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.util.photo.PhotoCatcherServer;

public class PhotoRequestMessage {

	private String path;

	public PhotoRequestMessage() {

	}

	public PhotoRequestMessage(String path) {
		this.path = path;
	}

	public static void encode(PhotoRequestMessage msg, PacketBuffer buf) {
		buf.writeUtf(msg.path);
	}

	public static PhotoRequestMessage decode(PacketBuffer buf) {
		return new PhotoRequestMessage(buf.readUtf());
	}

	public static class Handler {

		public static void handle(PhotoRequestMessage msg, Supplier<NetworkEvent.Context> ctx) {

			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				ServerPlayerEntity player = ctx.get().getSender();
				ServerWorld world = player.getLevel();
				File file = new File(
						DimensionType.getStorageFolder(world.dimension(),
								world.getServer().getWorldPath(FolderName.ROOT).toFile()),
						"/resources/photos/" + msg.path);
				if (file.exists() && !file.isDirectory()) {
					BufferedImage buffered = null;
					try {
						buffered = ImageIO.read(file);
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}
					if (buffered == null)
						return;
					sendFile(buffered, player, msg.path);
				}
			});
		}

		public static void sendFile(BufferedImage img, ServerPlayerEntity player, String name) {
			byte[] bytes = Utils.getBytes(img);
			List<byte[]> chunks = Utils.divideArray(bytes, 1000000);
			int parts = chunks.size();
			for (int i = 0; i < parts; i++) {
				PacketManager.sendTo(PacketManager.photoToClient, new PhotoToClientMessage(chunks.get(i), i, parts, name), player);
			}
		}
	}

}
