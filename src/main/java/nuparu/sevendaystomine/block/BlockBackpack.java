package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import nuparu.sevendaystomine.tileentity.TileEntityBackpack;
import nuparu.sevendaystomine.tileentity.TileEntityItemHandler;

public class BlockBackpack extends BlockHorizontalBase implements IWaterLoggable {

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	private static final VoxelShape NORTH = Block.box(4, 0.0F, 16, 12, 13, 10);
	private static final VoxelShape SOUTH = Block.box(4, 0.0F, 0F, 12, 13, 6);
	private static final VoxelShape WEST = Block.box(16, 0.0F, 4, 10, 13, 12);
	private static final VoxelShape EAST = Block.box(0F, 0.0F, 4, 6, 13, 12);

	public BlockBackpack(AbstractBlock.Properties properties) {
		super(properties.noOcclusion());
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.SOUTH).setValue(WATERLOGGED, Boolean.FALSE));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_,
			ISelectionContext p_220053_4_) {
		switch(state.getValue(FACING)) {
		default:
		case NORTH : return NORTH;
		case SOUTH : return SOUTH;
		case WEST : return WEST;
		case EAST : return EAST;
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityBackpack();
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult rayTraceResult) {
		if (worldIn.isClientSide())
			return ActionResultType.SUCCESS;

		INamedContainerProvider namedContainerProvider = this.getMenuProvider(state, worldIn, pos);
		if (namedContainerProvider != null) {
			TileEntityBackpack tileEntityBackpack = (TileEntityBackpack)namedContainerProvider;
			tileEntityBackpack.unpackLootTable(player);
			if (!(player instanceof ServerPlayerEntity))
				return ActionResultType.FAIL;
			ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
			NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, (packetBuffer) -> packetBuffer.writeBlockPos(pos));
			}
		return ActionResultType.SUCCESS;
	}

	@Override
	public void setPlacedBy(World p_180633_1_, BlockPos p_180633_2_, BlockState p_180633_3_, LivingEntity p_180633_4_, ItemStack p_180633_5_) {
		if (p_180633_5_.hasCustomHoverName()) {
			TileEntity tileentity = p_180633_1_.getBlockEntity(p_180633_2_);
			if (tileentity instanceof TileEntityItemHandler) {
				((TileEntityItemHandler)tileentity).setDisplayName(p_180633_5_.getHoverName());
			}
		}

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
			if (tileentity instanceof TileEntityBackpack) {
				TileEntityBackpack te = (TileEntityBackpack) tileentity;
				InventoryHelper.dropContents(world, blockPos, te.getDrops());
			}
			super.onRemove(state, world, blockPos, newState, isMoving);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
		p_206840_1_.add(FACING, WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState p_204507_1_) {
		return p_204507_1_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_204507_1_);
	}
}
