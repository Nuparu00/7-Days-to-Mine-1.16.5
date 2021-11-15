package nuparu.sevendaystomine.network.packets;

import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.ExtendedPlayer;
import nuparu.sevendaystomine.capability.IExtendedPlayer;

public class PlayerCapabilitySyncMessage {

	IExtendedPlayer extendedPlayer;
	int playerID;

	public PlayerCapabilitySyncMessage() {
		this.extendedPlayer = null;
		this.playerID = 0;
	}

	public PlayerCapabilitySyncMessage(IExtendedPlayer extendedPlayer, PlayerEntity player) {
		this.extendedPlayer = extendedPlayer;
		this.playerID = player.getId();
	}

	public PlayerCapabilitySyncMessage(IExtendedPlayer extendedPlayer, int playerID) {
		this.extendedPlayer = extendedPlayer;
		this.playerID = playerID;
	}
	
	public static void encode(PlayerCapabilitySyncMessage msg, PacketBuffer buf) {
		buf.writeInt(msg.playerID);
		CompoundNBT nbt = new CompoundNBT();
		if (msg.extendedPlayer != null) {
			msg.extendedPlayer.writeNBT(nbt);
		}
		buf.writeNbt(nbt);
    }

    public static PlayerCapabilitySyncMessage decode(PacketBuffer buf) {
    	int playerID = buf.readInt();
		CompoundNBT nbt = buf.readNbt();
		if (nbt == null)
			return null;
		IExtendedPlayer extendedPlayer = new ExtendedPlayer();
		extendedPlayer.readNBT(nbt);
        return new PlayerCapabilitySyncMessage(extendedPlayer,playerID);
    }
    
    public static class Handler {

        public static void handle(PlayerCapabilitySyncMessage msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
            	PlayerEntity player = SevenDaysToMine.proxy.getPlayerEntityFromContext(ctx);
                IExtendedPlayer extendedPlayer = msg.extendedPlayer;
                int playerID = msg.playerID;
                if(player != null && player.level != null){
                     Entity entity = player.level.getEntity(playerID);
                     if(entity != null && entity instanceof PlayerEntity){
                          PlayerEntity player2 = (PlayerEntity)entity;
                          IExtendedPlayer localExtendedPlayer = CapabilityHelper.getExtendedPlayer(player2);
                          localExtendedPlayer.copy(extendedPlayer);
                     }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    } 
}