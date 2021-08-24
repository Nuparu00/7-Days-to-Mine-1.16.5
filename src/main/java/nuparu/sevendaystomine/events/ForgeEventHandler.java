package nuparu.sevendaystomine.events;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.entity.human.dialogue.DialogueDataManager;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.item.guide.BookDataManager;
import nuparu.sevendaystomine.world.gen.city.data.CityDataManager;

@Mod.EventBusSubscriber(modid = SevenDaysToMine.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler {
	
	@SubscribeEvent
	public static void addReloadListener(AddReloadListenerEvent event) {
		System.out.println("addReloadListener");
		event.addListener(BookDataManager.instance);
		event.addListener(DialogueDataManager.instance);
		event.addListener(CityDataManager.instance);
		//event.addListener(Animations.instance);
	}

	@SubscribeEvent
	public static void onMissingBlockMapping(RegistryEvent.MissingMappings<Block> event) {
		for (RegistryEvent.MissingMappings.Mapping<Block> entry : event.getAllMappings()) {
			if(entry.key.toString().equals("sevendaystomine:Asphalt")){
				entry.remap(ModBlocks.ASPHALT.get());
			}
		}
	}


}
