package nuparu.sevendaystomine.world.gen.city;

import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.gen.city.plot.building.*;

import java.util.HashMap;
import java.util.Random;

public class CityBuildings {

    private static HashMap<ResourceLocation, Building> buildings = new HashMap<>();


    public static void init(){
        buildings.clear();
    }

    public static void addBuilding(ResourceLocation resourceLocation, Building building){
        buildings.put(resourceLocation,building);
    }

    public static Building getBuilding(ResourceLocation resourceLocation){
        return buildings.get(resourceLocation);
    }


    public static Building getRandomBuilding(Random rand) {
        int total = 0;
        for (Building building : buildings.values()) {
            total += building.weight;
        }
        int i = rand.nextInt(total);
        for (Building building : buildings.values()) {
            i -= building.weight;
            if (i <= 0) {
                return building;
            }
        }
        // Failsafe
        return buildings.get(rand.nextInt(buildings.size()));
    }
}
