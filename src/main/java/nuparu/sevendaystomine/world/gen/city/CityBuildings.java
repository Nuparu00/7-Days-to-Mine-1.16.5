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
        addBuilding(new ResourceLocation(SevenDaysToMine.MODID,"apartment"),new Building(new ResourceLocation(SevenDaysToMine.MODID,"apartment"),10));
        addBuilding(new ResourceLocation(SevenDaysToMine.MODID,"brick_house_damaged"),new Building(new ResourceLocation(SevenDaysToMine.MODID,"brick_house_damaged"),10,-1));
        addBuilding(new ResourceLocation(SevenDaysToMine.MODID,"police_station"),new PoliceStationBuilding(3,-1));
        addBuilding(new ResourceLocation(SevenDaysToMine.MODID,"book_store"),new Building(new ResourceLocation(SevenDaysToMine.MODID,"book_store"),3).setCanBeMirrored(false));
        addBuilding(new ResourceLocation(SevenDaysToMine.MODID,"fire_station"),new Building(new ResourceLocation(SevenDaysToMine.MODID,"fire_station"),3));
        addBuilding(new ResourceLocation(SevenDaysToMine.MODID,"apartments_ruins_overgrown"),new Building(new ResourceLocation(SevenDaysToMine.MODID,"apartments_ruins_overgrown"),4));
        addBuilding(new ResourceLocation(SevenDaysToMine.MODID,"overgrown_house"),new Building(new ResourceLocation(SevenDaysToMine.MODID,"overgrown_house"),6));
        addBuilding(new ResourceLocation(SevenDaysToMine.MODID,"gray_house"),new Building(new ResourceLocation(SevenDaysToMine.MODID,"gray_house"),6));
        addBuilding(new ResourceLocation(SevenDaysToMine.MODID,"parking_lot"),new Building(new ResourceLocation(SevenDaysToMine.MODID,"parking_lot"),2));
        addBuilding(new ResourceLocation(SevenDaysToMine.MODID,"apartment_dark_bottom"),new ApartmentDarkBuilding(new ResourceLocation(SevenDaysToMine.MODID,"apartment_dark_bottom"),4,0));
        addBuilding(new ResourceLocation(SevenDaysToMine.MODID,"gas_station"),new GasStationBuilding(3,0));
        addBuilding(new ResourceLocation(SevenDaysToMine.MODID,"construction_site"),new ConstructionSiteBuilding(new ResourceLocation(SevenDaysToMine.MODID,"construction_site"),3,0));
        addBuilding(new ResourceLocation(SevenDaysToMine.MODID,"brick_house_damaged_inverted"),new Building(new ResourceLocation(SevenDaysToMine.MODID,"brick_house_damaged_inverted"),10,-1));
        addBuilding(new ResourceLocation(SevenDaysToMine.MODID,"apartments_big"),new ApartmentsBigBuilding(new ResourceLocation(SevenDaysToMine.MODID,"apartments_big_undamaged_0"),new ResourceLocation(SevenDaysToMine.MODID,"apartments_big_undamaged_1"),2));
        addBuilding(new ResourceLocation(SevenDaysToMine.MODID,"condo"),new Building(new ResourceLocation(SevenDaysToMine.MODID,"condo"),5,0));
        addBuilding(new ResourceLocation(SevenDaysToMine.MODID,"supermarket"),new SupermarketBuilding(3,0));
        addBuilding(new ResourceLocation(SevenDaysToMine.MODID,"menu_house"),new Building(new ResourceLocation(SevenDaysToMine.MODID,"menu_house"),10,-4));
        addBuilding(new ResourceLocation(SevenDaysToMine.MODID,"hospital"),new HospitalBuilding(6,0));
        addBuilding(new ResourceLocation(SevenDaysToMine.MODID,"car_repair"),new CarRepairBuilding(4,0));
        addBuilding(new ResourceLocation(SevenDaysToMine.MODID,"cemetery"),new CemeteryBuilding(4,-2));
        addBuilding(new ResourceLocation(SevenDaysToMine.MODID,"hotel"),new HotelBuilding(4,0));
        addBuilding(new ResourceLocation(SevenDaysToMine.MODID,"church"),new ChurchBuilding(6,0));
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
