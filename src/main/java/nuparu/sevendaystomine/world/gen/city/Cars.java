package nuparu.sevendaystomine.world.gen.city;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
        return (BlockCar) cars.values().stream().toArray()[rand.nextInt(cars.values().size())];
    }

    public static void placeRandomCar(WorldGenRegion world, BlockPos pos, Direction facing, Random rand) {
        BlockCar car = getRandomCar(rand);
        if (car.canBePlaced(world, pos, facing)) {
            car.generate(world, pos, facing, true, null);
        }
        /*TileEntity te = world.getBlockEntity(pos);
        if (te instanceof TileEntityCarMaster) {
            TileEntityCarMaster master = (TileEntityCarMaster) te;
            master.setLootTable(car.lootTable, rand.nextLong());
            master.fillWithLoot(null);
        }*/
    }
}
