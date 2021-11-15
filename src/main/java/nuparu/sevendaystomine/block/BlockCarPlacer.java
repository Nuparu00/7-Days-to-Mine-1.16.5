package nuparu.sevendaystomine.block;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import nuparu.sevendaystomine.tileentity.TileEntityCarPlacer;

import javax.annotation.Nullable;

public class BlockCarPlacer extends BlockHorizontalBase{
    public BlockCarPlacer(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityCarPlacer();
    }

    @Override
    public BlockItem createBlockItem() {
        final Item.Properties properties = new Item.Properties();
        return new BlockItem(this, properties);
    }
}
