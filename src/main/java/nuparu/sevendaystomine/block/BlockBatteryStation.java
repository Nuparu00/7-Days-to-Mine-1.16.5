package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import nuparu.sevendaystomine.init.ModItemGroups;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.tileentity.TileEntityBatteryStation;
import nuparu.sevendaystomine.tileentity.TileEntityItemHandler;

public class BlockBatteryStation extends BlockHorizontalBase{

	private static final VoxelShape POLE = Block.box(6, 0.0F, 6, 10, 16, 10);
	private static final VoxelShape BASE = Block.box(0, 0.0F, 0, 16, 4, 16);
	private static final VoxelShape SHAPE = VoxelShapes.or(POLE, BASE);

	private EnumMaterial material = EnumMaterial.LEAD;
	private int weight = 2;

	public BlockBatteryStation(AbstractBlock.Properties properties) {
		super(properties);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityBatteryStation();
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult rayTraceResult) {

		if(player.getItemInHand(hand).getItem() == ModItems.WIRE.get()){
			return ActionResultType.FAIL;
		}

		if (worldIn.isClientSide())
			return ActionResultType.SUCCESS;

		INamedContainerProvider namedContainerProvider = this.getMenuProvider(state, worldIn, pos);
		if (namedContainerProvider != null) {
			TileEntityBatteryStation tileEntity = (TileEntityBatteryStation)namedContainerProvider;
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
	public void onRemove(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = world.getBlockEntity(blockPos);
			if (tileentity instanceof TileEntityBatteryStation) {
				TileEntityBatteryStation te = (TileEntityBatteryStation) tileentity;
				InventoryHelper.dropContents(world, blockPos, te.getDrops());
			}
			super.onRemove(state, world, blockPos, newState, isMoving);
		}
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

	@Override
	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
		return SHAPE;
	}

	@Override
	public ItemGroup getItemGroup() {
		return ModItemGroups.TAB_ELECTRICITY;
	}
}
