package nuparu.sevendaystomine.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.ExtendedPlayer;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.crafting.scrap.ScrapDataManager;

import java.util.function.Supplier;

public class SyncScrapsMessage {

	CompoundNBT nbt;

	public SyncScrapsMessage() {
	}

	public SyncScrapsMessage(CompoundNBT nbt) {
	}
	
	public static void encode(SyncScrapsMessage msg, PacketBuffer buf) {
		buf.writeNbt(msg.nbt);
    }

    public static SyncScrapsMessage decode(PacketBuffer buf) {
		CompoundNBT nbt = buf.readNbt();
		if (nbt == null)
			return null;
        return new SyncScrapsMessage(nbt);
    }
    
    public static class Handler {

        public static void handle(SyncScrapsMessage msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				ScrapDataManager.instance.load(msg.nbt);
            });
            ctx.get().setPacketHandled(true);
        }
    } 
}