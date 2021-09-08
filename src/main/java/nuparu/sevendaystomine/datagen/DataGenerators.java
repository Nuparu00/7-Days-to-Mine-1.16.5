package nuparu.sevendaystomine.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import nuparu.sevendaystomine.datagen.loot.BaseLootTableProvider;
import nuparu.sevendaystomine.SevenDaysToMine;

@Mod.EventBusSubscriber(modid = SevenDaysToMine.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        System.out.println("gatherData()");
        DataGenerator generator = event.getGenerator();
        //generator.addProvider(new Recipes(generator));
        generator.addProvider(new BaseLootTableProvider(generator));
    }
}