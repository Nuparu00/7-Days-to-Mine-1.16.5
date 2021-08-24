package nuparu.sevendaystomine.block;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;

public abstract class BlockTileProvider<TE extends TileEntity> extends BlockBase{

	public BlockTileProvider(Properties properties) {
		super(properties);
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
    {
        return true;
    }
/*
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
       
    }*/

}
