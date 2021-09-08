package nuparu.sevendaystomine.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.item.IScrapable;
import nuparu.sevendaystomine.tileentity.TileEntityItemHandler;
import nuparu.sevendaystomine.tileentity.TileEntityPrinter;
import nuparu.sevendaystomine.tileentity.TileEntityScreenProjector;

import javax.annotation.Nullable;

public class BlockPrinter extends BlockHorizontalBase implements IScrapable {

	private static final VoxelShape CENTER = Block.box(4,0,4,12,5,12);

	private static final VoxelShape SIDE_SHAPE_Z_LEFT_SHORT = Block.box(12,0,4,14,4.5,12);
	private static final VoxelShape SIDE_SHAPE_Z_RIGHT_SHORT= Block.box(2,0,4,4,4.5,12);

	private static final VoxelShape SIDE_SHAPE_Z_LEFT_TALL = Block.box(12,0,4,14,5,12);
	private static final VoxelShape SIDE_SHAPE_Z_RIGHT_TALL= Block.box(2,0,4,4,5,12);

	private static final VoxelShape SHAPE_NORTH  = VoxelShapes.or(CENTER,SIDE_SHAPE_Z_LEFT_SHORT,SIDE_SHAPE_Z_RIGHT_TALL);
	private static final VoxelShape SHAPE_SOUTH = VoxelShapes.or(CENTER,SIDE_SHAPE_Z_RIGHT_SHORT,SIDE_SHAPE_Z_LEFT_TALL);

	private static final VoxelShape SIDE_SHAPE_X_LEFT_SHORT = Block.box(4,0,12,12,4.5,14);
	private static final VoxelShape SIDE_SHAPE_X_RIGHT_SHORT = Block.box(4,0,2,12,4.5,4);

	private static final VoxelShape SIDE_SHAPE_X_LEFT_TALL = Block.box(4,0,12,12,5,14);
	private static final VoxelShape SIDE_SHAPE_X_RIGHT_TALL = Block.box(4,0,2,12,5,4);

	private static final VoxelShape SHAPE_WEST  = VoxelShapes.or(CENTER,SIDE_SHAPE_X_RIGHT_SHORT,SIDE_SHAPE_X_LEFT_TALL);
	private static final VoxelShape SHAPE_EAST = VoxelShapes.or(CENTER,SIDE_SHAPE_X_LEFT_SHORT,SIDE_SHAPE_X_RIGHT_TALL);

	private EnumMaterial material = EnumMaterial.IRON;
	private int weight = 2;

	public BlockPrinter(Properties properties) {
		super(properties);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_,
			ISelectionContext p_220053_4_) {
		Direction direction = state.getValue(FACING);
		switch(direction){
			default:
			case SOUTH: return SHAPE_SOUTH;
			case NORTH: return SHAPE_NORTH;
			case WEST: return SHAPE_WEST;
			case EAST: return SHAPE_EAST;
		}
	}


	@Override
	public void setMaterial(EnumMaterial mat) {
		material = mat;
	}

	@Override
	public EnumMaterial getItemMaterial() {
		return material;
	}

	@Override
	public void setWeight(int newWeight) {
		weight = newWeight;
	}

	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public boolean canBeScraped() {
		return true;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityPrinter();
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
			NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, (packetBuffer) -> {
				packetBuffer.writeBlockPos(pos);
			});
		}
		return ActionResultType.SUCCESS;
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
			if (tileentity instanceof TileEntityScreenProjector) {
				TileEntityScreenProjector te = (TileEntityScreenProjector) tileentity;
				InventoryHelper.dropContents(world, blockPos, te.getDrops());
			}
			super.onRemove(state, world, blockPos, newState, isMoving);
		}
	}
}