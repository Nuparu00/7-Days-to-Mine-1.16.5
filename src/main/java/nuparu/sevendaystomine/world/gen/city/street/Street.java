package nuparu.sevendaystomine.world.gen.city.street;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.block.BlockAsphalt;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.city.Cars;
import nuparu.sevendaystomine.world.gen.city.City;
import nuparu.sevendaystomine.world.gen.city.CityBuildings;
import nuparu.sevendaystomine.world.gen.city.CityType;
import nuparu.sevendaystomine.world.gen.city.data.CityDataManager;
import nuparu.sevendaystomine.world.gen.city.plot.Plot;
import nuparu.sevendaystomine.world.gen.city.plot.building.Building;

import java.util.ArrayList;
import java.util.List;

public class Street {
    public BlockPos start;
    public BlockPos end;
    public WorldGenRegion world;

    public Crossing startCrossing = null;
    public Crossing endCrossing = null;

    public City city;

    public Direction facing;
    public boolean canBranch = true;
    public boolean tunnel = false;
    public int branchIndex = 0;

    public int streetIndex = 0;

    public String name;
    public List<Plot> plots = new ArrayList<Plot>();

    private Street previousStreet;
    private List<Street> connectedStreets = new ArrayList<Street>();
    private BlockState pavementState;

    public Street(WorldGenRegion world, BlockPos start, BlockPos end, Direction facing, City city) {
        this.world = world;
        this.start = start;
        this.end = end;
        this.facing = facing;
        this.city = city;
        this.name = "missing.street";
    }

    public void addConnectedStreet(Street street) {
        connectedStreets.add(street);
    }

    public void addPreviousStreet(Street street) {
        this.previousStreet = street;
        addConnectedStreet(street);
    }

    public boolean isConnectedTo(Street street) {
        return connectedStreets.contains(street);
    }

    public void tryToContinueStreets() {
        for (Direction f : Utils.HORIZONTALS) {
            if (f != facing.getOpposite() && city.getStreetsCount() < city.roadsLimit) {
                if (canBranch || f == facing) {
                    BlockPos blockpos = end.relative(f, city.type.roadLength - 1);
                    Biome biome = world.getBiome(blockpos);
                    //if (BiomeDictionary.hasType(RegistryKey.create(ForgeRegistries.Keys.BIOMES, biome.getRegistryName()), BiomeDictionary.Type.WATER)) continue;
                    int height = 128;
                    int deltaHeight = Math.abs(end.getY() - height);
                    if (deltaHeight <= city.type.maxSlope) {
                        Street street = new Street(world, end,
                                new BlockPos(blockpos.getX(), height - 1, blockpos.getZ()), f, this.city);
                        street.canBranch = !canBranch;

                        if (f == Direction.WEST || facing == Direction.EAST) {
                            if (city.streetNamesZ.containsKey(end.getZ())) {
                                street.name = city.streetNamesZ.get(end.getZ());
                            } else {
                                String name = CityDataManager.instance.getRandomStreetForCity(city);
                                city.streetNamesZ.put(end.getZ(), name);
                                street.name = name;
                            }
                        }
                        if (f == Direction.SOUTH || facing == Direction.NORTH) {
                            if (city.streetNamesX.containsKey(end.getX())) {
                                street.name = city.streetNamesX.get(end.getX());
                            } else {
                                String name = CityDataManager.instance.getRandomStreetForCity(city);
                                city.streetNamesX.put(end.getX(), name);
                                street.name = name;
                            }
                        }

                        if (!city.isThereStreet(street.start, street.end)) {
                            city.addStreet(street);
                            connectedStreets.add(street);
                            street.addPreviousStreet(this);
                        }
                    }
                }
            }
        }
    }

    public void preGen() {
        int startHeight = Utils.getTopSolidGroundHeight(start, world) - 1;
        int endHeight = Utils.getTopSolidGroundHeight(end, world) - 1;

        start = new BlockPos(start.getX(), startHeight, start.getZ());
        end = new BlockPos(end.getX(), endHeight, end.getZ());
    }

    public void checkEnding() {
        int cross = 0;
        if (endCrossing == null) {
            cross = city.getStreetsAtCrossingCount(end, 4);
        } else {
            cross = endCrossing.getStreetsCont();
        }
        if (cross > 2)
            return;

        if (cross == 2) {
            for (Street street : endCrossing.getStreets()) {
                if (street != this && street.facing == facing) {
                    return;
                }
            }

            int r = (int) Math.ceil(city.type.getRoadWidth() / 2);
            for (int i = 1; i < r + city.type.getPavementWidth() + 1; i++) {
                for (int j = 1; j < r + city.type.getPavementWidth() + 1; j++) {
                    BlockPos pos = end.relative(facing.getCounterClockWise(), i).relative(facing, j);
                    BlockState state = city.type.pavementBlock == null ? Blocks.STONE_BRICKS.defaultBlockState()
                            : city.type.pavementBlock;
                    if (i <= r && j <= r) {
                        state = city.type.roadBlock;
                    }
                    Block block2 = world.getBlockState(pos).getBlock();
                    /*if (block2 != city.type.roadBlock.getBlock() && block2 != Blocks.WHITE_CONCRETE) {
                        world.setBlock(pos, state);
                        BlockPos pos2 = pos.below();
                        while (world.getBlockState(pos2).getBlock().rep(world, pos2)) {
                            world.setBlock(pos2, ModBlocks.STRUCTURE_STONE.getDefaultState());
                            pos2 = pos2.below();
                        }
                    } else {
                        break;
                    }*/
                }
            }
        }

    }

    public void generate() {
System.out.println("STREET GEN");
        int width = city.type.getRoadWidth() + (city.type.getPavementWidth() * 2);
        for (int forward = 0; forward <= city.type.roadLength - 1; forward++) {
            int y = (int) Math.round(Utils.lerp(start.getY(), end.getY(), forward / (float) city.type.roadLength));

            for (int side = 0; side < width; side++) {
                int offset = side - Math.round(width / 2f) + 1;
                BlockPos pos = new BlockPos(start.getX(), 0, start.getZ()).relative(facing, forward)
                        .relative(facing.getClockWise(), offset).above(y);
                BlockState originalState = world.getBlockState(pos);
                generateAsphaltAndSidewalk(forward, side, offset, pos, originalState);
                decorate(forward, side, offset, pos, originalState);
                cars(forward, side, offset, pos, originalState);
            }
        }
    }

    public void generateAsphaltAndSidewalk(int forward, int side, int offset, BlockPos pos, BlockState originalState) {
        BlockState asphaltState = ModBlocks.ASPHALT.get().defaultBlockState().setValue(BlockAsphalt.CITY, true);

        //Sidewalk
        if ((side < city.type.getPavementWidth() || side >= city.type.getRoadWidth() + city.type.getPavementWidth())
                && originalState != city.type.roadBlock) {
            BlockState pavementState = city.type.pavementBlock;
            ;
            if (pavementState == null) {
                pavementState = Blocks.STONE_BRICKS.defaultBlockState();
                if (city.rand.nextInt(3) == 0) {
                    pavementState = Blocks.CRACKED_STONE_BRICKS.defaultBlockState();
                }
                if (city.rand.nextInt(3) == 0) {
                    pavementState = Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
                    if (city.rand.nextInt(6) == 0) {
                        pavementState = Blocks.MOSSY_STONE_BRICK_SLAB.defaultBlockState();
                    }
                }
                if (city.rand.nextInt(6) == 0) {
                    pavementState = Blocks.STONE_BRICK_SLAB.defaultBlockState();
                }

            }
            world.setBlock(pos, pavementState,0);
        } else {
            //Road proper
            world.setBlock(pos, asphaltState,0);
        }

    }

    public void decorate(int forward, int side, int offset, BlockPos pos, BlockState originalState) {
        if (!tunnel) {
            if (canBranch) {
                if ((endCrossing != null && endCrossing.getStreets().size() > 2)
                        || city.getStreetsAtCrossingCount(end, 4) > 2) {
                    if (forward == city.type.roadLength
                            - (city.type.getRoadWidth() - (2 - (city.type.getRoadWidth() - 7)))
                            && side == city.type.getRoadWidth() + (city.type.getPavementWidth() * 2) - 2) {
                        city.type.trafficLight.generate(world, pos.above(), this, facing, forward, side, offset, canBranch);
                    }
                }
                if (forward < city.type.roadLength - (city.type.halfWidth + city.type.getPavementWidth())
                        && (forward + 5) % 8 == 0) {
                    if (/*(forward + 5) % 16 == 0 || */city.type == CityType.CITY) {
                        if (side == 0) {
                            city.type.streetLamp.generate(world, pos.above(), this, facing, forward, side, offset, false);
                        } else if (side == city.type.getRoadWidth() + (city.type.getPavementWidth() * 2) - 1) {
                            city.type.streetLamp.generate(world, pos.above(), this, facing, forward, side, offset, true);
                        }
                    }
                }
            } else {
                if ((startCrossing != null && startCrossing.getStreets().size() > 2)
                        || city.getStreetsAtCrossingCount(start, 4) > 2) {
                    if (forward == Math.ceil(city.type.getRoadWidth() / 2) + (1 - (city.type.getRoadWidth() - 7))
                            && side == city.type.getPavementWidth() - 1) {
                        city.type.trafficLight.generate(world, pos.above(), this, facing, forward, side, offset, !canBranch);
                    }
                }
                if (forward > city.type.halfWidth + city.type.getPavementWidth() && (forward - city.type.halfWidth) % 8 == 0) {
                    if (/*(forward - city.type.halfWidth) % 16 == 0 || */city.type == CityType.CITY) {
                        if (side == 0) {
                            city.type.streetLamp.generate(world, pos.above(), this, facing, forward, side, offset, false);
                        } else if (side == city.type.getRoadWidth() + (city.type.getPavementWidth() * 2) - 1) {
                            city.type.streetLamp.generate(world, pos.above(), this, facing, forward, side, offset, true);
                        }
                    }
                }
            }
        }
        if (connectedStreets.size() <= 1) {
            /*if(previousStreet == null && (startCrossing == null || startCrossing.getStreetsCont() <= 1) && (endCrossing == null || endCrossing.getStreetsCont() == 0)){
                if(forward == 2 && offset == 4){
                    city.type.limitSign.generate(world, pos.above(), this, facing.getOpposite(), forward, side, offset,true);
                }
            }
            else{*/
            if (previousStreet != null && forward == city.type.roadLength - 2 && offset == -4) {
                city.type.limitSign.generate(world, pos.above(), this, facing, forward, side, offset, true);
            }
            //}
        }
    }

    public void generateBuildings() {

        BlockPos pos = start.relative(facing,city.type.pavementWidth);
        Biome biome = world.getBiome(pos);
        int forward = 0;

        int tries = 0;

        int maxEnd = (canBranch ? city.type.roadLength - 10 : city.type.roadLength);

        while (forward < city.type.roadLength && tries < 128) {
            Building building = CityBuildings.getRandomBuilding(city.rand);
            if (building.allowedCityTypes != null && !building.allowedCityTypes.contains(city.type))
                continue;
            if (building.allowedBiomes != null && !building.allowedBiomes.isEmpty()
                    && !building.allowedBiomes.contains(biome))
                continue;
            Plot plot = new Plot(this, 0, building, false, pos);
            int newForward = forward;
            BlockPos newPos = pos;
            if (facing.getAxis() == Direction.Axis.X) {
                newForward += plot.xSize + 1;
                newPos = newPos.relative(facing, plot.xSize + 2);

            } else {
                newForward += plot.zSize + 1;
                newPos = newPos.relative(facing, plot.zSize + 2);
            }
            if(newForward > maxEnd || city.doesPlotIntersect(plot)){
                tries++;
                continue;
            }
            forward = newForward;
            pos = newPos;
            plots.add(plot);
            plot.generate();
        }

        pos = start.relative(facing,city.type.pavementWidth);
        forward = 0;
        tries = 0;

        while (forward < city.type.roadLength && tries < 128) {
            Building building = CityBuildings.getRandomBuilding(city.rand);
            if (building.allowedCityTypes != null && !building.allowedCityTypes.contains(city.type))
                continue;
            if (building.allowedBiomes != null && !building.allowedBiomes.isEmpty()
                    && !building.allowedBiomes.contains(biome))
                continue;
            if (!building.canBeMirrored) {
                continue;
            }

            Plot plot = new Plot(this, 0, building, true, pos);
            int newForward = forward;
            BlockPos newPos = pos;
            if (facing.getAxis() == Direction.Axis.X) {
                newForward += plot.xSize + 2;
                newPos = newPos.relative(facing, plot.xSize + 1);

            } else {
                newForward += plot.zSize + 2;
                newPos = newPos.relative(facing, plot.zSize + 1);
            }
            if(newForward > maxEnd || city.doesPlotIntersect(plot)){
                tries++;
                continue;
            }
            forward = newForward;
            pos = newPos;
            plots.add(plot);
            plot.generate();

        }
    }


    public void cars(int forward, int side, int offset, BlockPos pos, BlockState originalState) {
        BlockPos pos2 = pos.above();

        if (world.getBlockState(pos2).getBlock() == Blocks.AIR
                && world.getBlockState(pos).isFaceSturdy(world, pos, Direction.UP)) {
            // Cars generation
            if (Math.abs(side - (city.type.getRoadWidth() / 2 + city.type.getPavementWidth())) <= 2
                    && city.rand.nextInt(50) == 0) {

                Direction facing2 = facing;
                if (city.rand.nextInt(4) == 0) {
                    facing2 = Utils.HORIZONTALS[city.rand.nextInt(4)];
                }
                Cars.placeRandomCar(world, pos2, facing2, city.rand);
            }
        }
    }


}
