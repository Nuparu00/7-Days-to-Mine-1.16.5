package nuparu.sevendaystomine.world.gen.city;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.state.properties.SlabType;
import nuparu.sevendaystomine.block.BlockAsphalt;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.world.gen.city.street.lamp.CityStreetLamp;
import nuparu.sevendaystomine.world.gen.city.street.lamp.StreetLamp;
import nuparu.sevendaystomine.world.gen.city.street.limit.CityLimitSign;
import nuparu.sevendaystomine.world.gen.city.street.limit.LimitSign;
import nuparu.sevendaystomine.world.gen.city.street.traffic.CityTrafficLight;
import nuparu.sevendaystomine.world.gen.city.street.traffic.TrafficLight;

import java.util.ArrayList;
import java.util.List;

public class CityType {

    public static List<CityType> values = new ArrayList<>();
    public static CityType VILLAGE;
    public static CityType TOWN;
    public static CityType CITY;
    public static CityType METROPOLIS;
    String name;
    public boolean sewers = true;
    public int roadLength = 64;
    //How many "asphalt blocks"
    public int roadWidth = 7;
    //How many "pavement blocks" on each side
    public int pavementWidth = 3;
    public float populationMultiplier = 1f;
    //Asphalt block
    public BlockState roadBlock;
    public BlockState pavementBlock = null;

    public StreetLamp streetLamp = new CityStreetLamp();
    public TrafficLight trafficLight = new CityTrafficLight();
    public LimitSign limitSign = new CityLimitSign();

    public int maxSlope = 24;

    public int sewersHeight = 5;
    public int sewersWidth = 3;

    public int halfWidth;

    CityType(String name, boolean sewers, int roadLength, int roadWidth, int pavementWidth, float populationMultiplier) {
        this.name = name;
        this.sewers = sewers;
        this.roadLength = roadLength;
        this.roadWidth = roadWidth;
        this.pavementWidth = pavementWidth;
        this.halfWidth = (int) Math.ceil(roadWidth/2);
        roadBlock = ModBlocks.ASPHALT.get().defaultBlockState().setValue(BlockAsphalt.CITY, true);
    }

    CityType(String name, boolean sewers, int roadLength, int roadWidth, int pavementWidth, float populationMultiplier, BlockState roadBlock) {
        this(name, sewers, roadLength, roadWidth, pavementWidth, populationMultiplier);
        this.roadBlock = roadBlock;
    }

    CityType(String name, boolean sewers, int roadLength, int roadWidth, int pavementWidth, float populationMultiplier, BlockState roadBlock, BlockState pavementBlock) {
        this(name, sewers, roadLength, roadWidth, pavementWidth, populationMultiplier, roadBlock);
        this.pavementBlock = pavementBlock;
    }

    public static CityType getByName(String name) {
        for (CityType type : values) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return null;
    }


    public static void init() {


        VILLAGE = new CityType("village", false, 48, 5, 2, 0.25f, ModBlocks.ASPHALT.get().defaultBlockState().setValue(BlockAsphalt.CITY, true), Blocks.GRASS_PATH.defaultBlockState());
        TOWN = new CityType("town", false, 64, 5, 2, 0.5f, ModBlocks.ASPHALT.get().defaultBlockState().setValue(BlockAsphalt.CITY, true), Blocks.STONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.DOUBLE));
        CITY = new CityType("city", true, 80, 7, 3, 0.8f);
        METROPOLIS = new CityType("metropolis", true, 64, 7, 3, 1f);

        values.add(VILLAGE);
        values.add(TOWN);
        values.add(CITY);
        values.add(METROPOLIS);
    }

    public int getPavementWidth() {
        return pavementWidth;
    }

    public int getRoadWidth() {
        return roadWidth;
    }
}
