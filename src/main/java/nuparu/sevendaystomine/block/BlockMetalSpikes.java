package nuparu.sevendaystomine.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.tileentity.TileEntityMetalSpikes;

import javax.annotation.Nullable;

public class BlockMetalSpikes extends HorizontalFaceBlock implements  IBlockBase, IWaterLoggable {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final VoxelShape AABB_DOWN = Block.box(0, 15, 0, 16, 16, 16);
    public static final VoxelShape AABB_UP = Block.box(0, 0, 0, 16, 1, 16);
    public static final VoxelShape AABB_NORTH = Block.box(0, 0, 15, 16, 16, 16);
    public static final VoxelShape AABB_SOUTH = Block.box(0, 0, 0.0, 16, 16, 1);
    public static final VoxelShape AABB_WEST = Block.box(15, 0, 0, 16, 16, 16);
    public static final VoxelShape AABB_EAST = Block.box(0.0, 0, 0, 1, 16, 16);

    public BlockMetalSpikes(AbstractBlock.Properties p_i48369_1_) {
        super(p_i48369_1_);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FACE, AttachFace.WALL).setValue(WATERLOGGED, Boolean.valueOf(false)));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos,
                               ISelectionContext context) {
        AttachFace face = state.getValue(FACE);
        Direction facing = state.getValue(FACING);

        switch (face) {
            case FLOOR:
                return AABB_UP;
            case CEILING:
                return AABB_DOWN;
            case WALL: {
                switch (facing) {
                    case WEST:
                        return AABB_WEST;
                    case NORTH:
                        return AABB_NORTH;
                    case SOUTH:
                        return AABB_SOUTH;
                    case EAST:
                        return AABB_EAST;
                }
            }
        }
        return super.getShape(state, reader, pos, context);
    }

    @Override
    public void entityInside(BlockState state, World world, BlockPos pos, Entity entityIn) {
        TileEntity te = world.getBlockEntity(pos);
        if (te != null && te instanceof TileEntityMetalSpikes) {
            TileEntityMetalSpikes spikes = (TileEntityMetalSpikes) te;
            if (spikes.isRetracted())
                return;
            if (!(entityIn instanceof LivingEntity)) {
                return;
            }
            entityIn.makeStuckInBlock(state, new Vector3d(0.25D, (double) 0.05F, 0.25D));
            if (entityIn instanceof PlayerEntity) {
                if (((PlayerEntity) entityIn).isCreative() || ((PlayerEntity) entityIn).isSpectator()) {
                    return;
                }
            }
            entityIn.hurt(DamageSource.GENERIC, 7);
            spikes.dealDamage(1);
        }
    }

    public boolean canSurvive(BlockState p_196260_1_, IWorldReader p_196260_2_, BlockPos p_196260_3_) {
        return canAttach(p_196260_2_, p_196260_3_, getConnectedDirection(p_196260_1_).getOpposite());
    }

    public static boolean canAttach(IWorldReader p_220185_0_, BlockPos p_220185_1_, Direction p_220185_2_) {
        BlockPos blockpos = p_220185_1_.relative(p_220185_2_);
        return p_220185_0_.getBlockState(blockpos).isFaceSturdy(p_220185_0_, blockpos, p_220185_2_.getOpposite());
    }


    protected static Direction getConnectedDirection(BlockState p_196365_0_) {
        switch ((AttachFace) p_196365_0_.getValue(FACE)) {
            case CEILING:
                return Direction.DOWN;
            case FLOOR:
                return Direction.UP;
            default:
                return p_196365_0_.getValue(FACING);
        }
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(FACE, FACING, WATERLOGGED);
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if (!(state.getBlock() instanceof BlockMetalSpikes)) {
            super.onRemove(state, world, blockPos, newState, isMoving);
        }
    }

    @Override
    public void setPlacedBy(World p_180633_1_, BlockPos p_180633_2_, BlockState p_180633_3_, LivingEntity p_180633_4_, ItemStack p_180633_5_) {
        if (!p_180633_1_.isClientSide) {
            this.checkIfExtend(p_180633_1_, p_180633_2_, p_180633_3_);
        }

    }

    @Override
    public void neighborChanged(BlockState p_220069_1_, World p_220069_2_, BlockPos p_220069_3_, Block p_220069_4_, BlockPos p_220069_5_, boolean p_220069_6_) {
        if (!p_220069_2_.isClientSide) {
            this.checkIfExtend(p_220069_2_, p_220069_3_, p_220069_1_);
        }

    }

    @Override
    public void onPlace(BlockState p_220082_1_, World p_220082_2_, BlockPos p_220082_3_, BlockState p_220082_4_, boolean p_220082_5_) {
        if (!p_220082_4_.is(p_220082_1_.getBlock()) && !p_220082_2_.isClientSide && p_220082_2_.getBlockEntity(p_220082_3_) == null) {
            this.checkIfExtend(p_220082_2_, p_220082_3_, p_220082_1_);
        }

    }

    private void checkIfExtend(World world, BlockPos pos, BlockState state) {
        boolean powered = world.hasNeighborSignal(pos);
        TileEntity te = world.getBlockEntity(pos);
        if (te != null && te instanceof TileEntityMetalSpikes) {
            TileEntityMetalSpikes tileEntityMetalSpikes = (TileEntityMetalSpikes) te;
            tileEntityMetalSpikes.setRetracted(!powered, state);
        }

    }

    @Override
    public BlockItem createBlockItem() {
        if (this == ModBlocks.METAL_SPIKES_EXTENDED.get()) {
            return null;
        }
        final Item.Properties properties = new Item.Properties().tab(getItemGroup());
        return new BlockItem(this, properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityMetalSpikes();
    }


    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        for (Direction direction : context.getNearestLookingDirections()) {
            BlockState blockstate;
            if (direction.getAxis() == Direction.Axis.Y) {
                blockstate = this.defaultBlockState()
                        .setValue(FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR)
                        .setValue(FACING, context.getHorizontalDirection()).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
            } else {
                blockstate = this.defaultBlockState().setValue(FACE, AttachFace.WALL).setValue(FACING,
                        direction.getOpposite()).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
            }

            if (blockstate.canSurvive(context.getLevel(), context.getClickedPos())) {
                return blockstate;
            }
        }

        return null;
    }

    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_,
                                  IWorld p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        if (p_196271_1_.getValue(WATERLOGGED)) {
            p_196271_4_.getLiquidTicks().scheduleTick(p_196271_5_, Fluids.WATER, Fluids.WATER.getTickDelay(p_196271_4_));
        }
        return getConnectedDirection(p_196271_1_).getOpposite() == p_196271_2_
                && !p_196271_1_.canSurvive(p_196271_4_, p_196271_5_) ? Blocks.AIR.defaultBlockState()
                : super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_,
                p_196271_6_);
    }

    @Override
    public FluidState getFluidState(BlockState p_204507_1_) {
        return p_204507_1_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_204507_1_);
    }

}
