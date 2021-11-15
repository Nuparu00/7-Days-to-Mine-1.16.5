package nuparu.sevendaystomine.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.Random;

public class FeatureCity<T extends IFeatureConfig> extends Feature<T> {

    public FeatureCity(Codec<T> p_i231922_1_) {
        super(p_i231922_1_);
    }

    boolean flag = false;
    public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, T config) {

        /*IChunk ichunk = world.getChunk(pos);
        if(ichunk instanceof Chunk) {
            Chunk chunk = (Chunk)ichunk;
            int chunkX = chunk.getPos().x;
            int chunkZ = chunk.getPos().z;
            if (Utils.canCityBeGeneratedHere(world, chunkX, chunkZ)) {
                ChunkPos chunkPos = chunk.getPos();
                long k = rand.nextLong() / 2L * 2L + 1L;
                long l = rand.nextLong() / 2L * 2L + 1L;

                Random rand2 = new Random((long) chunkX * k + (long) chunkZ * l ^ world.getSeed());
                System.out.println("FOUND CITY");
                City city = City.foundCity(world.getLevel(), chunkPos, rand2);
                city.startCityGen();
                return true;
            }
        }*/

        //System.out.println(pos.toString());
        if(pos.getX() % 64 == 0  && pos.getZ() % 64 == 0 && world instanceof WorldGenRegion){
            /*WorldGenRegion region = (WorldGenRegion)world;
            System.out.println("FOUND CITY +" + pos.toString() + " " + world.getClass() +  " " + world.getLevel());
           City city = City.foundCity(region, pos.above(128), rand);
           city.startCityGen();*/
           /*
            for(int i =  0; i < 16; i++){
                for(int j =  0; j < 16; j++){
                    for(int k =  0; k < 64; k++){
                        world.setBlock(pos.offset(i,64+k,j), Blocks.BEDROCK.defaultBlockState(),0);
                    }
                }
            }*/
            //city.startCityGen();
            flag = true;
            return true;
        }

        return false;
    }
}