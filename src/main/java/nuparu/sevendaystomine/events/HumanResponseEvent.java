package nuparu.sevendaystomine.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import nuparu.sevendaystomine.entity.human.EntityHuman;

@Cancelable
public class HumanResponseEvent extends Event {

	public HumanResponseEvent(EntityHuman human, PlayerEntity player, String dialogue) {
		
	}
}
