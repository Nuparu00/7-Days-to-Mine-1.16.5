package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.tileentity.TileEntityPhoto;

public class BlockPhoto extends BlockHorizontalBase {
	
	private static final VoxelShape NORTH = Block.box(0.0F, 0.0F, 15.4, 16.0F, 16, 16);
	private static final VoxelShape SOUTH = Block.box(0.0F, 0.0F, 0.0F, 16.0F, 16, 0.6);
	private static final VoxelShape WEST = Block.box(15.4, 0.0F, 0F, 16, 16, 16);
	private static final VoxelShape EAST = Block.box(0.0F, 0.0F, 0.0F, 0.6, 16, 16);

	private EnumMaterial material = EnumMaterial.IRON;
	private int weight = 2;

	public BlockPhoto(AbstractBlock.Properties properties) {
		super(properties.noOcclusion());
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_,
			ISelectionContext p_220053_4_) {
		switch(state.getValue(FACING)) {
		default:
		case NORTH: return NORTH;
		case SOUTH: return SOUTH;
		case WEST: return WEST;
		case EAST: return EAST;
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityPhoto();
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult rayTraceResult) {
		TileEntity te = worldIn.getBlockEntity(pos);
		if(te != null && te instanceof TileEntityPhoto) {
			TileEntityPhoto photo = (TileEntityPhoto)te;
			SevenDaysToMine.proxy.openPhoto(photo.getPath());
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	@Nullable
	public INamedContainerProvider getMenuProvider(BlockState p_220052_1_, World p_220052_2_, BlockPos p_220052_3_) {
		TileEntity tileentity = p_220052_2_.getBlockEntity(p_220052_3_);
		return tileentity instanceof INamedContainerProvider ? (INamedContainerProvider) tileentity : null;
	}
	
	@Override
	public BlockItem createBlockItem() {
		 return null;
	}


	@Override
	public BlockRenderType getRenderShape(BlockState p_149645_1_) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
	{
		TileEntity te = world.getBlockEntity(pos);
		if(te != null && te instanceof TileEntityPhoto) {
			TileEntityPhoto photo = (TileEntityPhoto)te;
			ItemStack stack = new ItemStack(ModItems.PHOTO.get());
			stack.getOrCreateTag().putString("path",photo.getPath());
			return stack;
		}
		return super.getPickBlock(state, target, world, pos, player);
	}

}