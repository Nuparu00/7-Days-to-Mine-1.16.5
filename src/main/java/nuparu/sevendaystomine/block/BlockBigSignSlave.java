package nuparu.sevendaystomine.block;

import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import nuparu.sevendaystomine.tileentity.TileEntityBigSignMaster;
import nuparu.sevendaystomine.tileentity.TileEntityBigSignSlave;

import javax.annotation.Nullable;

public class BlockBigSignSlave extends BlockHorizontalBase {

    protected static final VoxelShape EAST = Block.box(0.0D, 0D, 0, 0.125D*16, 16, 1D*16);
    protected static final VoxelShape WEST = Block.box(0.875D*16, 0.0, 0D, 1.0D*16, 1D*16, 1D*16);
    protected static final VoxelShape SOUTH = Block.box(0, 0.0, 0.0D, 1D*16, 1D*16, 0.125D*16);
    protected static final VoxelShape NORTH = Block.box(0, 0.0, 0.875D*16, 1D*16, 1D*16, 1.0D*16);

    public BlockBigSignSlave(Properties propertiesIn, WoodType woodTypeIn) {
        super(propertiesIn);
    }

    @Override
    public boolean hasTileEntity(BlockState stateIn) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_,
                               ISelectionContext p_220053_4_) {
        switch (state.getValue(HorizontalBlock.FACING)) {
            default:
            case NORTH:
                return NORTH;
            case SOUTH:
                return SOUTH;
            case WEST:
                return WEST;
            case EAST:
                return EAST;
        }
    }

    @Override
    @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityBigSignSlave();
    }


    @Override
    public void onRemove(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
        TileEntity tileentity = world.getBlockEntity(blockPos);
        if (tileentity != null && tileentity instanceof TileEntityBigSignSlave) {
            TileEntityBigSignSlave slave = (TileEntityBigSignSlave) tileentity;
            TileEntity te = world.getBlockEntity(slave.getParent());
            if (te != null && te instanceof TileEntityBigSignMaster) {
                world.destroyBlock(slave.getParent(), true);
            }

        }

        super.onRemove(state, world, blockPos, newState, isMoving);
    }

    @Deprecated
    @Override
    public VoxelShape getBlockSupportShape(BlockState state, IBlockReader p_230335_2_, BlockPos p_230335_3_) {
        return VoxelShapes.block();
    }

    @Override
    public BlockItem createBlockItem() {
        return null;
    }

}
