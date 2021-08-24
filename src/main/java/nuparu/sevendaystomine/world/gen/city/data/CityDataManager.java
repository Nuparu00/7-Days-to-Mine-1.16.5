package nuparu.sevendaystomine.world.gen.city.data;

import com.google.gson.*;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.item.guide.BookDataManager;
import nuparu.sevendaystomine.util.book.BookData;
import nuparu.sevendaystomine.util.book.BookDataParser;
import nuparu.sevendaystomine.world.gen.city.City;

import java.util.*;

public class CityDataManager extends JsonReloadListener {

    private static final Gson GSON = (new GsonBuilder()).create();

    public static CityDataManager instance = new CityDataManager();

    private List<String> streetNames = new ArrayList<>();
    private List<String> cityNames = new ArrayList<>();

    public CityDataManager() {
        super(GSON,"city");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn,
                         IProfiler profilerIn) {
        streetNames.clear();
        cityNames.clear();
System.out.println("APPLY");
        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            /*ResourceLocation key = entry.getKey();
            System.out.println("APPLYX " + key.toString());
            JsonElement jsonElement = entry.getValue();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray streets = jsonObject.getAsJsonArray();
            for(JsonElement streetElement : streets){
                String name = streetElement.getAsString();
                System.out.println(name);
                streetNames.add(name);
            }*/

            /*JsonArray cities = jsonObject.getAsJsonArray("cities");
            for(JsonElement cityElement : cities){
                String name = cityElement.getAsString();
                cityNames.add(name);
            }*/
        }
    }

    public String getRandomStreet(Random rand) {
        if (streetNames == null || streetNames.isEmpty())
            return "Missing Street";
        return streetNames.get(rand.nextInt(streetNames.size()));
    }

    public String getRandomCityName(Random rand) {
        if (cityNames == null || cityNames.isEmpty())
            return "Genericville";
        return cityNames.get(rand.nextInt(cityNames.size()));
    }

    public String getRandomStreetForCity(City city) {
        if (streetNames == null || streetNames.isEmpty())
            return "Missing Street";
        int index = city.rand.nextInt(city.unclaimedStreetNames.size());
        String name = city.unclaimedStreetNames.get(index);
        city.unclaimedStreetNames.remove(index);
        return name;
    }

    public List<String> getStreetNames(){
        return new ArrayList<>(streetNames);
    }
}