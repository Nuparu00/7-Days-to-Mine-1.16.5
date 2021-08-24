package nuparu.sevendaystomine.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;

public class PacketEventHandler {

	@SubscribeEvent
	public void playerStartedTracking(PlayerEvent.StartTracking event) {
		PlayerEntity player = event.getPlayer();
		Entity target = event.getTarget();
		if (target instanceof PlayerEntity) {

			PlayerEntity targetPlayer = (PlayerEntity) target;
			IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(targetPlayer);
			extendedPlayer.onStartedTracking(targetPlayer);
		}
	}
}