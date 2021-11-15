package nuparu.sevendaystomine.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.block.BlockCarPlacer;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.world.gen.city.Cars;

public class TileEntityCarPlacer extends TileEntity implements ITickableTileEntity {


    public ResourceLocation carToPlace;

    public TileEntityCarPlacer() {
        super(ModTileEntities.CAR_PLACER.get());
    }
    public TileEntityCarPlacer(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    @Override
    public void tick(){

        if(level instanceof ServerWorld) {
            System.out.println(carToPlace == null ? "NULL " : carToPlace.toString());
            if(((ServerWorld)level).getChunkSource().getGenerator() instanceof FlatChunkGenerator &&
                    level.dimension().equals(World.OVERWORLD)){
                return;
            }
            if (carToPlace != null) {
                BlockState state = this.getBlockState();
                if (state.getBlock() != ModBlocks.CAR_PLACER.get()) return;
                Cars.placeCar((ServerWorld) level, worldPosition, state.getValue(BlockCarPlacer.FACING), carToPlace, level.random);
            }
            else{

            }
        }
        if(carToPlace == null){
            level.setBlockAndUpdate(worldPosition,Blocks.AIR.defaultBlockState());
        }
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);
        if(compound.contains("car", Constants.NBT.TAG_STRING)) {
            this.carToPlace = new ResourceLocation(compound.getString("car"));
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        if(carToPlace != null) {
            compound.putString("car", carToPlace.toString());
        }
        return compound;
    }
}
