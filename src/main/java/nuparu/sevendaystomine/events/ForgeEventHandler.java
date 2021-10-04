package nuparu.sevendaystomine.events;

import net.minecraft.block.Block;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.crafting.scrap.ScrapDataManager;
import nuparu.sevendaystomine.entity.human.dialogue.DialogueDataManager;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.item.guide.BookDataManager;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.city.data.CityDataManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = SevenDaysToMine.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler {
	
	@SubscribeEvent
	public static void addReloadListener(AddReloadListenerEvent event) {
		System.out.println("addReloadListener");
		//event.addListener(BookDataManager.instance);
		event.addListener(DialogueDataManager.instance);
		event.addListener(CityDataManager.instance);
		event.addListener(ScrapDataManager.instance);
		//event.addListener(Animations.instance);
	}


}
