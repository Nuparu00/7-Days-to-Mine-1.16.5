package nuparu.sevendaystomine.block;

import net.minecraft.block.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import nuparu.sevendaystomine.tileentity.TileEntityStreetSign;

public class BlockWallStreetSign extends WallSignBlock
{

    public BlockWallStreetSign(Properties propertiesIn, WoodType woodTypeIn)
    {
        super(propertiesIn, woodTypeIn);
    }

    @Override
    public boolean hasTileEntity(BlockState stateIn)
    {
        return true;
    }

    @Override
    public TileEntity newBlockEntity(IBlockReader worldIn)
    {
        return new TileEntityStreetSign();
    }

    @Deprecated
    @Override
    public VoxelShape getBlockSupportShape(BlockState state, IBlockReader p_230335_2_, BlockPos p_230335_3_) {
        return  VoxelShapes.block();
    }
}