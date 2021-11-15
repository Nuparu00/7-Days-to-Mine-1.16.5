package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import nuparu.sevendaystomine.tileentity.TileEntityItemHandler;
import nuparu.sevendaystomine.tileentity.TileEntityRefrigerator;

public class BlockRefrigerator extends BlockHorizontalBase {

	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

	public BlockRefrigerator(AbstractBlock.Properties properties) {
		super(properties);
	    this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.SOUTH).setValue(HALF, DoubleBlockHalf.LOWER));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityRefrigerator();
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult rayTraceResult) {
		if (worldIn.isClientSide())
			return ActionResultType.SUCCESS;

		INamedContainerProvider namedContainerProvider = this.getMenuProvider(state, worldIn, pos);
		if (namedContainerProvider != null) {
			TileEntityItemHandler tileEntity = (TileEntityItemHandler)namedContainerProvider;
			tileEntity.unpackLootTable(player);
			if (!(player instanceof ServerPlayerEntity))
				return ActionResultType.FAIL;
			ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
			NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, (packetBuffer) -> packetBuffer.writeBlockPos(pos));
		}
		return ActionResultType.SUCCESS;
	}

	@Nullable
	public INamedContainerProvider getMenuProvider(BlockState p_220052_1_, World p_220052_2_, BlockPos p_220052_3_) {
		TileEntity tileentity = p_220052_2_.getBlockEntity(p_220052_3_);
		return tileentity instanceof INamedContainerProvider ? (INamedContainerProvider) tileentity : null;
	}

	@Override
	public void setPlacedBy(World p_180633_1_, BlockPos p_180633_2_, BlockState p_180633_3_, LivingEntity p_180633_4_, ItemStack p_180633_5_) {
		p_180633_1_.setBlock(p_180633_2_.above(), p_180633_3_.setValue(HALF, DoubleBlockHalf.UPPER), 3);
		if (p_180633_5_.hasCustomHoverName()) {
			TileEntity tileentity = p_180633_1_.getBlockEntity(p_180633_2_);
			if (tileentity instanceof TileEntityItemHandler) {
				((TileEntityItemHandler)tileentity).setDisplayName(p_180633_5_.getHoverName());
			}
			tileentity = p_180633_1_.getBlockEntity(p_180633_2_.above());
			if (tileentity instanceof TileEntityItemHandler) {
				((TileEntityItemHandler)tileentity).setDisplayName(p_180633_5_.getHoverName());
			}
		}

	}

	@Override
	public void onRemove(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = world.getBlockEntity(blockPos);
			if (tileentity instanceof TileEntityItemHandler) {
				TileEntityItemHandler te = (TileEntityItemHandler) tileentity;
				InventoryHelper.dropContents(world, blockPos, te.getDrops());
			}
			super.onRemove(state, world, blockPos, newState, isMoving);
		}
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING,HALF);
	}

	@Override
	public BlockItem createBlockItem() {
		return null;
	}

	public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, IWorld p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
		DoubleBlockHalf doubleblockhalf = p_196271_1_.getValue(HALF);
		if (p_196271_2_.getAxis() == Direction.Axis.Y && doubleblockhalf == DoubleBlockHalf.LOWER == (p_196271_2_ == Direction.UP)) {
			return p_196271_3_.is(this) && p_196271_3_.getValue(HALF) != doubleblockhalf ? p_196271_1_.setValue(FACING, p_196271_3_.getValue(FACING)) : Blocks.AIR.defaultBlockState();
		} else {
			return doubleblockhalf == DoubleBlockHalf.LOWER && p_196271_2_ == Direction.DOWN && !p_196271_1_.canSurvive(p_196271_4_, p_196271_5_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
		}
	}

	@Override
	public boolean canSurvive(BlockState p_196260_1_, IWorldReader p_196260_2_, BlockPos p_196260_3_) {
		BlockPos blockpos = p_196260_3_.below();
		BlockState blockstate = p_196260_2_.getBlockState(blockpos);
		return p_196260_1_.getValue(HALF) == DoubleBlockHalf.LOWER ? blockstate.isFaceSturdy(p_196260_2_, blockpos, Direction.UP) : blockstate.is(this);
	}
}