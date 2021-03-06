package nuparu.sevendaystomine.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import nuparu.sevendaystomine.tileentity.TileEntityCookingPot;

import javax.annotation.Nullable;

public class BlockCookingPot extends BlockCookware {


    public BlockCookingPot(Properties properties, VoxelShape shape) {
        super(properties, shape);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand,
                                BlockRayTraceResult rayTraceResult) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isCrouching()) {
            return super.use(state, worldIn, pos, player, hand, rayTraceResult);
        }


        if (worldIn.isClientSide()) return ActionResultType.SUCCESS;

        INamedContainerProvider namedContainerProvider = this.getMenuProvider(state, worldIn, pos);
        if (namedContainerProvider != null) {
            TileEntityCookingPot tileEntityGrill = (TileEntityCookingPot) namedContainerProvider;
            tileEntityGrill.unpackLootTable(player);
            if (!(player instanceof ServerPlayerEntity))
                return ActionResultType.FAIL;
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
            NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, (packetBuffer) -> packetBuffer.writeBlockPos(pos));
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
        return new TileEntityCookingPot();
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
            if (tileentity instanceof TileEntityCookingPot) {
                TileEntityCookingPot te = (TileEntityCookingPot) tileentity;
                InventoryHelper.dropContents(world, blockPos, te.getDrops());
            }
            super.onRemove(state, world, blockPos, newState, isMoving);
        }
    }

}
