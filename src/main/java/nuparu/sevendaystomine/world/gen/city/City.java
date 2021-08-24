package nuparu.sevendaystomine.world.gen.city;

import com.google.common.collect.Maps;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.city.data.CityDataManager;
import nuparu.sevendaystomine.world.gen.city.plot.Plot;
import nuparu.sevendaystomine.world.gen.city.street.Crossing;
import nuparu.sevendaystomine.world.gen.city.street.Street;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class City {

    public BlockPos start;
    public WorldGenRegion world;

    private List<Street> streets = new ArrayList<Street>();
    private BlockingQueue<Street> streetsQueue = new LinkedBlockingQueue<Street>();

    public HashMap<Integer, String> streetNamesX = Maps.newHashMap();
    public HashMap<Integer, String> streetNamesZ = Maps.newHashMap();

    public int roadsLimit;

    private boolean allStreetsFound = false;
    public List<String> unclaimedStreetNames = null;

    public Random rand;

    public String name = "Genericville";
    public CityType type = CityType.CITY;

    public int population;

    public City() {

    }

    public City(WorldGenRegion world, BlockPos start, CityType type, Random rand) {
        this.start = start;
        this.type = type;
        this.rand = rand;
        this.start = new BlockPos(this.start.getX(), 255, this.start.getZ());

        this.world = world;
        this.unclaimedStreetNames = CityDataManager.instance.getStreetNames();
        this.name = CityDataManager.instance.getRandomCityName(this.rand);
        this.roadsLimit = 4;
        roadsLimit = Math.round(roadsLimit * type.populationMultiplier);
    }

    public static City foundCity(WorldGenRegion world, ChunkPos pos, Random rand) {
        return foundCity(world, new BlockPos(pos.x * 16 + 8, 0, pos.z * 16 + 8), rand);
    }

    public static City foundCity(WorldGenRegion world, BlockPos pos) {
        return foundCity(world, pos, new Random(world.getSeed() + (pos.getX() / 16) - (pos.getZ() / 16)));
    }
    public static City foundCity(WorldGenRegion world, BlockPos pos, Random rand) {
        rand = new Random(0);
        Biome biome = world.getBiome(pos);
        CityType type = CityType.TOWN;
        if (rand.nextInt(3) == 0) {
            type = CityType.VILLAGE;
        } else if (rand.nextInt(2) == 0) {
            type = CityType.CITY;
        }
        return new City(world, pos, type, rand);
    }

    public void addStreet(Street street) {
        for (Street st : streets) {
            if (Utils.compareBlockPos(st.end, street.end)) {
                if (!st.isConnectedTo(street)) {
                    st.addConnectedStreet(street);
                }
                if (!street.isConnectedTo(st)) {
                    street.addConnectedStreet(st);
                }
            }
        }

        street.preGen();
        streets.add(street);
        street.streetIndex = streets.size();
        try {
            streetsQueue.put(street);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isThereStreet(BlockPos s, BlockPos e) {
        for (Street street : streets) {
            if ((street.start.equals(s) && street.end.equals(e)) || (street.start.equals(e) && street.end.equals(s))) {
                return true;
            }
        }
        return false;
    }

    public int getStreetsCount() {
        return this.streets.size();
    }

    public void startCityGen() {
        long timeStamp = System.currentTimeMillis();
        if (!areAllStreetsFound()) {
            prepareStreets();
        }
        if (this.name.equals("Caprica City")) {
            this.population = 50298;
        } else {
            this.population = this.roadsLimit
                    * (MathUtils.getIntInRange(rand, 128, 1024) * (type == CityType.METROPOLIS ? 32
                    : (type == CityType.CITY ? 16 : (type == CityType.TOWN ? 2 : 1))));
        }
        generate();
        //CitySavedData.get(world).addCity(this);
        if (!world.isClientSide()) {
            Utils.getLogger().info(this.name + " generated at " + start.getX() + " " + start.getZ() + " within "
                    + (System.currentTimeMillis() - timeStamp) + "ms with " + streets.size() + " streets");
        }
    }

    public void prepareStreets() {
        BlockPos bp_start = Utils.getTopGroundBlock(start, world, true);
        for (Direction facing : Utils.HORIZONTALS) {
            if (getStreetsCount() < this.roadsLimit) {
                BlockPos blockpos = bp_start.relative(facing, type.roadLength - 1);
                Biome biome = world.getBiome(blockpos);
                if(BiomeDictionary.hasType(RegistryKey.create(ForgeRegistries.Keys.BIOMES,biome.getRegistryName()), BiomeDictionary.Type.WATER)) continue;
                int height = 128;
                int deltaHeight = bp_start.getY() - height;
                if (deltaHeight <= type.maxSlope) {
                    Street street = new Street(world, bp_start, new BlockPos(blockpos.getX(), height, blockpos.getZ()),
                            facing, this);
                    street.canBranch = false;

                    if (facing == Direction.WEST || facing == Direction.EAST) {
                        if (streetNamesZ.containsKey(bp_start.getZ())) {
                            street.name = streetNamesZ.get(bp_start.getZ());
                        } else {
                            String name = CityDataManager.instance.getRandomStreetForCity(this);
                            streetNamesZ.put(bp_start.getZ(), name);
                            street.name = name;
                        }
                    }
                    if (facing == Direction.SOUTH || facing == Direction.NORTH) {
                        if (streetNamesX.containsKey(bp_start.getX())) {
                            street.name = streetNamesX.get(bp_start.getX());
                        } else {
                            String name = CityDataManager.instance.getRandomStreetForCity(this);
                            streetNamesX.put(bp_start.getX(), name);
                            street.name = name;
                        }
                    }

                    if (!isThereStreet(street.start, street.end)) {
                        addStreet(street);
                    }
                }
            }
        }
        int attempts = 0;
        while (streetsQueue.size() > 0 && attempts < 8192) {
            addIteration();
            attempts++;
        }
        setAllStreetsFound(true);
    }

    public void addIteration() {
        int size = streetsQueue.size();
        int attempts = 0;
        if (size > 0) {
            while (size > 0 && attempts < 16) {
                try {
                    Street street = streetsQueue.take();
                    street.tryToContinueStreets();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                attempts++;
                size--;
            }
        }
    }

    public boolean areAllStreetsFound() {
        return allStreetsFound;
    }

    public void setAllStreetsFound(boolean state) {
        allStreetsFound = state;
    }

    public void generate() {
        for (Street street : streets) {
            street.generate();
        }
        for (Street street : streets) {
            street.checkEnding();
        }
        /*if (type.sewers) {
            for (Street street : streets) {
                street.generateSewers();
            }
        }
        for (Street street : streets) {
            street.decorate();
        }*/
        for (Street street : streets) {
            //street.generateBuildings();
        }
    }

    public int getStreetsAtCrossingCount(BlockPos pos, int tolerance) {
        Iterable<BlockPos> poses = BlockPos.betweenClosed(
                pos.relative(Direction.NORTH, tolerance).relative(Direction.WEST, tolerance).below(5),
                pos.relative(Direction.SOUTH, tolerance).relative(Direction.EAST, tolerance).above(5));
        Crossing crossing = new Crossing(this);
        int i = 0;
        for (Street street : streets) {
            for (BlockPos pos2 : poses) {
                if (pos2.getX() == street.start.getX() && pos2.getY() == street.start.getY()
                        && pos2.getZ() == street.start.getZ()) {
                    crossing.addStreet(street);
                    street.startCrossing = crossing;
                    i++;
                } else if (pos2.getX() == street.end.getX() && pos2.getY() == street.end.getY()
                        && pos2.getZ() == street.end.getZ()) {
                    crossing.addStreet(street);
                    street.endCrossing = crossing;
                    i++;
                }
                if (i == 4) {
                    break;
                }
            }

            if (i == 4) {
                break;
            }
        }

        return i;

    }

    public boolean doesPlotIntersect(Plot plot) {
        for (Street street : streets) {
            for (Plot plot2 : street.plots) {
                if (plot2.intersects(plot))
                    return true;
            }
        }
        return false;
    }
}
