package nuparu.sevendaystomine.network.packets;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.crafting.scrap.ScrapDataManager;
import nuparu.sevendaystomine.integration.jei.JeiBridge;

import java.util.function.Supplier;

public class SyncScrapsMessage {

	CompoundNBT nbt;

	public SyncScrapsMessage() {
	}

	public SyncScrapsMessage(CompoundNBT nbt) {
		this.nbt = nbt;
	}
	
	public static void encode(SyncScrapsMessage msg, PacketBuffer buf) {
		buf.writeNbt(msg.nbt);
    }

    public static SyncScrapsMessage decode(PacketBuffer buf) {
		CompoundNBT nbt = buf.readNbt();
        return new SyncScrapsMessage(nbt);
    }
    
    public static class Handler {

        public static void handle(SyncScrapsMessage msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				ScrapDataManager.instance.load(msg.nbt);

				if(ModList.get().isLoaded("jei")) {
					JeiBridge.registerScrapRecipes();
				}
            });
			ctx.get().setPacketHandled(true);
        }
    } 
}