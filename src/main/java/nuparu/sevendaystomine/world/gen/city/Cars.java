package nuparu.sevendaystomine.world.gen.city;

import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.WorldGenRegion;
import nuparu.sevendaystomine.block.BlockCar;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Cars {

    private static HashMap<BlockCar, Integer> cars = new HashMap<>();


    public static void init(){
        cars.clear();
       }

    public static void addCar(int weight, BlockCar car){
        cars.put(car,weight);
    }


    public static BlockCar getRandomCar(Random rand) {
        int total = 0;
        for (Map.Entry<BlockCar, Integer> entry : cars.entrySet()) {
            total += entry.getValue();
        }
        int i = rand.nextInt(total);
        for (Map.Entry<BlockCar, Integer> entry : cars.entrySet()) {
            i -= entry.getValue();
            if (i <= 0) {
                return entry.getKey();
            }
        }
        // Failsafe
        return (BlockCar) cars.values().toArray()[rand.nextInt(cars.values().size())];
    }

    public static void placeRandomCar(WorldGenRegion world, BlockPos pos, Direction facing, Random rand) {
        BlockCar car = getRandomCar(rand);
        if (car.canBePlaced(world, pos, facing)) {
            car.generate(world, pos, facing, true, null,rand);
        }
        /*TileEntity te = world.getBlockEntity(pos);
        if (te instanceof TileEntityCarMaster) {
            TileEntityCarMaster master = (TileEntityCarMaster) te;
            master.setLootTable(car.lootTable, rand.nextLong());
            master.fillWithLoot(null);
        }*/
    }

    public static void placeRandomCar(ISeedReader world, BlockPos pos, Direction facing, Random rand) {
        BlockCar car = getRandomCar(rand);
        if (car.canBePlaced(world, pos, facing)) {
            car.generate(world, pos, facing, true, null,rand);
        }
    }

    public static void placeCar(ISeedReader world, BlockPos pos, Direction facing, ResourceLocation res, Random rand) {
        BlockCar car = getRandomCar(rand);
        for (Map.Entry<BlockCar, Integer> entry : cars.entrySet()) {
            if(entry.getKey().getRegistryName().equals(res)){
                car = entry.getKey();
                break;
            }
        }
        world.setBlock(pos, Blocks.AIR.defaultBlockState(),0);
        if(car == null) return;

        if(!world.getBlockState(pos.below()).isFaceSturdy(world,pos.below(),Direction.UP)){
            pos = pos.below();
            if(!world.getBlockState(pos.below()).isFaceSturdy(world,pos.below(),Direction.UP)){
                return;
            }

        }

        if (car.canBePlaced(world, pos, facing)) {
            car.generate(world, pos, facing, true, null,rand);
        }
    }
}
