package nuparu.sevendaystomine.events;

import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;

@OnlyIn(Dist.CLIENT)
public class TextureStitcherEventHandler {
	@SubscribeEvent
	  public void stitcherEventPre(TextureStitchEvent.Pre event) {

		AtlasTexture map = event.getMap();
		ResourceLocation stitching = map.location();

		if(!stitching.equals(AtlasTexture.LOCATION_BLOCKS))
			return;

		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID,"entity/particles/blood"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID,"entity/particles/vomit"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID,"items/empty_backpack_slot"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID,"items/empty_circuit_slot"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID,"items/empty_paper_slot"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID,"entity/particles/muzzle_flash"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID,"items/empty_scrap_slot"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID,"items/empty_ink_slot"));

		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/solar_panel"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/wind_turbine_propeller"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/airplanerotor_turbine"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/camera"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/globe"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/turret"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/turret_advanced"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sedan_red"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sedan_blue"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sedan_yellow"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sedan_green"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sedan_black"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sedan_white"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/police_car"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/ambulance"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleepingbag/red"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleepingbag/light_gray"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleepingbag/purple"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleepingbag/pink"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleepingbag/magenta"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleepingbag/lime"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleepingbag/light_blue"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleepingbag/green"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleepingbag/gray"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleepingbag/cyan"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleepingbag/brown"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleepingbag/blue"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleepingbag/black"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleepingbag/white"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleepingbag/yellow"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleepingbag/orange"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/backpack"));
		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/riot_shield"));

		event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "blocks/ink_still"));
	  }

	@SubscribeEvent
	public void modelRegistryEvent(ModelRegistryEvent event) {
	}

}
