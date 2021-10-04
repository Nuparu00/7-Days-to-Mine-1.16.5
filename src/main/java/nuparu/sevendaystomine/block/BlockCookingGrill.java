package nuparu.sevendaystomine.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntityBackpack;
import nuparu.sevendaystomine.tileentity.TileEntityGrill;
import nuparu.sevendaystomine.util.MathUtils;

import javax.annotation.Nullable;

public class BlockCookingGrill extends BlockCookware {


    public BlockCookingGrill(Properties properties, VoxelShape shape) {
        super(properties, shape);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand,
                                BlockRayTraceResult rayTraceResult) {
        ItemStack stack = player.getItemInHand(hand);

            if (stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() == ModBlocks.BEAKER.get()) {
                if (!worldIn.isClientSide()) {
                    worldIn.setBlockAndUpdate(pos, ModBlocks.COOKING_GRILL_BEAKER.get().defaultBlockState().setValue(FACING, state.getValue(FACING)).setValue(WATERLOGGED, state.getValue(WATERLOGGED)).setValue(CAMPFIRE, state.getValue(CAMPFIRE)));
                    worldIn.playSound((PlayerEntity) null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            SoundEvents.ITEM_FRAME_ADD_ITEM, SoundCategory.BLOCKS,
                            MathUtils.getFloatInRange(0.9f, 1.1f), MathUtils.getFloatInRange(0.9f, 1.1f));
                }
                return ActionResultType.SUCCESS;
            }
            if(player.isCrouching()) {
                return super.use(state, worldIn, pos, player, hand, rayTraceResult);
            }

        /*else {
            if (stack.isEmpty() && player.isCrouching()) {
                if (!worldIn.isClientSide()) {
                    worldIn.setBlockAndUpdate(pos, state.setValue(BEAKER, false));
                }
                return ActionResultType.SUCCESS;
            }
        }*/

        if(worldIn.isClientSide()) return ActionResultType.SUCCESS;;

        INamedContainerProvider namedContainerProvider = this.getMenuProvider(state, worldIn, pos);
        if (namedContainerProvider != null) {
            TileEntityGrill tileEntityGrill = (TileEntityGrill)namedContainerProvider;
            tileEntityGrill.unpackLootTable(player);
            if (!(player instanceof ServerPlayerEntity))
                return ActionResultType.FAIL;
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
            NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, (packetBuffer) -> {
                packetBuffer.writeBlockPos(pos);
            });
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityGrill();
    }

    @Nullable
    public INamedContainerProvider getMenuProvider(BlockState p_220052_1_, World p_220052_2_, BlockPos p_220052_3_) {
        TileEntity tileentity = p_220052_2_.getBlockEntity(p_220052_3_);
        return tileentity instanceof INamedContainerProvider ? (INamedContainerProvider) tileentity : null;
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = world.getBlockEntity(blockPos);
            if (tileentity instanceof TileEntityGrill) {
                TileEntityGrill te = (TileEntityGrill) tileentity;
                InventoryHelper.dropContents(world, blockPos, te.getDrops());
            }
            super.onRemove(state, world, blockPos, newState, isMoving);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, CAMPFIRE);
    }

}
