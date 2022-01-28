package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModItemGroups;
import nuparu.sevendaystomine.item.ItemLinkTool;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;
import nuparu.sevendaystomine.computer.EnumSystem;
import nuparu.sevendaystomine.tileentity.TileEntityItemHandler;

public class BlockComputer extends BlockHorizontalBase {
	
	private static final VoxelShape NORTH = Block.box(0.3125F*16, 0.0F, 0.0625F*16, 0.6875F*16, 16F, 0.875F*16);
	private static final VoxelShape SOUTH = Block.box(0.3125F*16, 0.0F, 0.125F*16, 0.6875F*16, 16F, 0.9375*16);
	private static final VoxelShape WEST = Block.box(0.0625F*16, 0.0F, 0.3125F*16, 0.875F*16, 16F, 0.6875F*16);
	private static final VoxelShape EAST = Block.box(0.125F*16, 0.0F, 0.3125F*16, 0.9375*16, 16F, 0.6875F*16);

	public BlockComputer(AbstractBlock.Properties properties) {
		super(properties);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_,
			ISelectionContext p_220053_4_) {
		switch(state.getValue(HorizontalBlock.FACING)) {
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
		return new TileEntityComputer();
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand,
			BlockRayTraceResult rayTraceResult) {
		ItemStack stack = playerIn.getItemInHand(hand);
		if (stack.getItem() instanceof ItemLinkTool)
			return ActionResultType.FAIL;
		if (!worldIn.isClientSide()) {
			if (!playerIn.isCrouching()) {
				TileEntity TE = worldIn.getBlockEntity(pos);
				if (TE != null && TE instanceof TileEntityComputer) {
					TileEntityComputer computerTE = (TileEntityComputer) TE;
					if (computerTE.getSystem() != EnumSystem.NONE && computerTE.isCompleted()) {
						if (computerTE.on) {
							computerTE.turnOff();
							TranslationTextComponent text = new TranslationTextComponent("computer.turn.off");
							text.getStyle().withColor(TextFormatting.GREEN);
							playerIn.sendMessage(text,Util.NIL_UUID);
							return ActionResultType.SUCCESS;
						} else {
							computerTE.turnOn();
							TranslationTextComponent text = new TranslationTextComponent("computer.turn.on");
							text.getStyle().withColor(TextFormatting.GREEN);
							playerIn.sendMessage(text,Util.NIL_UUID);
							return ActionResultType.SUCCESS;
						}
					}
				}
			}
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
			if (tileentity instanceof TileEntityComputer) {
				TileEntityComputer te = (TileEntityComputer) tileentity;
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
	public ItemGroup getItemGroup() {
		return ModItemGroups.TAB_ELECTRICITY;
	}
}
