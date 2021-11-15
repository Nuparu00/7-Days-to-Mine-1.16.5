package nuparu.sevendaystomine.events;

import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.repair.RepairDataManager;
import nuparu.sevendaystomine.crafting.scrap.ScrapDataManager;
import nuparu.sevendaystomine.entity.human.dialogue.DialogueDataManager;
import nuparu.sevendaystomine.world.gen.city.data.CityDataManager;

@Mod.EventBusSubscriber(modid = SevenDaysToMine.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler {
	
	@SubscribeEvent
	public static void addReloadListener(AddReloadListenerEvent event) {
		System.out.println("addReloadListener");
		//event.addListener(BookDataManager.instance);
		event.addListener(DialogueDataManager.instance);
		event.addListener(CityDataManager.instance);
		event.addListener(ScrapDataManager.instance);
		event.addListener(RepairDataManager.instance);
		//event.addListener(Animations.instance);
	}


}
