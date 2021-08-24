package nuparu.sevendaystomine.block;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import nuparu.sevendaystomine.tileentity.TileEntityStreetSign;

import javax.annotation.Nullable;

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