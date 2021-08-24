package nuparu.sevendaystomine.network.packets;

import java.io.IOException;
import java.util.HashMap;
import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.util.PlayerInventorySyncHelper;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.util.item.BufferedCache;
import nuparu.sevendaystomine.util.item.ItemCache;

public class SyncInventoryMessage {

	private String name;
	private BufferedCache items;

	public SyncInventoryMessage() {

	}

	public SyncInventoryMessage(ItemCache items, String name) {
		this.items = items.serialize();
		this.name = name;
	}

	public SyncInventoryMessage(BufferedCache items, String name) {
		this.items = items;
		this.name = name;
	}

	public static void encode(SyncInventoryMessage msg, PacketBuffer buf) {
		buf.writeUtf(msg.name);
		byte[] dataArgs = new byte[0];
		try {
			dataArgs = Utils.convertToBytes(msg.items);
		} catch (IOException e) {
			e.printStackTrace();
		}
		buf.writeBytes(dataArgs);
	}

	public static SyncInventoryMessage decode(PacketBuffer buf) {
		String name = buf.readUtf();
		BufferedCache items = null;
		byte[] bytes = new byte[buf.readableBytes()];
		if (bytes.length > 0) {
			int readerIndex = buf.readerIndex();
			buf.getBytes(readerIndex, bytes);
			Object obj = null;
			try {
				obj = Utils.convertFromBytes(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			if (obj != null && obj instanceof BufferedCache) {
				items = (BufferedCache) obj;
			}
		}
		return new SyncInventoryMessage(items, name);
	}

	public static class Handler {

		public static void handle(SyncInventoryMessage msg, Supplier<NetworkEvent.Context> ctx) {

			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				if (PlayerInventorySyncHelper.itemsCache == null) {
					PlayerInventorySyncHelper.itemsCache = new HashMap<String, ItemCache>();
				}
				PlayerInventorySyncHelper.itemsCache.put(msg.name, msg.items.deserialize());

			});
		}
	}

}
