package nuparu.sevendaystomine.network.packets;

import java.io.File;
import java.util.function.Supplier;

import org.apache.commons.io.FilenameUtils;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.util.photo.PhotoCatcherServer;

public class SyncThrottleMessage {

	int id;
	float throttle;
	public SyncThrottleMessage() {
		
	}
	
	public SyncThrottleMessage(int id, float throttle) {
		this.id = id;
		this.throttle = throttle;
	}

	public static void encode(SyncThrottleMessage msg, PacketBuffer buf) {
		buf.writeInt(msg.id);
		buf.writeFloat(msg.throttle);
		
	}

	public static SyncThrottleMessage decode(PacketBuffer buf) {
		return new SyncThrottleMessage(buf.readInt(),buf.readFloat());
	}

	public static class Handler {

		public static void handle(SyncThrottleMessage msg, Supplier<NetworkEvent.Context> ctx) {

			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				ServerPlayerEntity player = ctx.get().getSender();
				World world = player.level;
				
				int id = msg.id;
				float throttle = msg.throttle;
				
				Entity entity = world.getEntity(id);
				
				if(entity == null) return;
				
				/*if(!(entity instanceof EntityMinibike)) return;
				
				EntityMinibike minibike = (EntityMinibike)entity;
				if(minibike.getControllingPassenger() != player) return;*/
			});
		}
	}

}
