package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
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
import net.minecraftforge.fml.network.NetworkHooks;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.tileentity.TileEntityMicrowave;
import nuparu.sevendaystomine.tileentity.TileEntityMonitor;

public class BlockMonitor extends BlockHorizontalBase {
	private static final VoxelShape NORTH = Block.box(0F, 0.0F, 0.25F*16, 16F, 12, 0.6875F*16);
	private static final VoxelShape SOUTH = Block.box(0.0F, 0.0F, 0.3125F*16, 16F, 12, 0.75*16);
	private static final VoxelShape WEST = Block.box(0.3125F*16, 0.0F, 0.0F, 0.75F*16, 12, 16F);
	private static final VoxelShape EAST = Block.box(0.25F*16, 0.0F, 0.0F, 0.6875F*16, 12, 16F);

	public BlockMonitor(AbstractBlock.Properties properties) {
		super(properties);
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
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult rayTraceResult) {
		TileEntity TE = worldIn.getBlockEntity(pos);
		if (TE != null && TE instanceof TileEntityMonitor) {
			TileEntityMonitor monitorTE = (TileEntityMonitor) TE;
			if (player.isCrouching()) {
				if (!worldIn.isClientSide()) {
					monitorTE.setState(!monitorTE.getState());
					if (monitorTE.getState() == true) {
						TranslationTextComponent text = new TranslationTextComponent("computer.turn.on");
						text.getStyle().withColor(TextFormatting.GREEN);
						player.sendMessage(text,Util.NIL_UUID);
					} else {
						TranslationTextComponent text = new TranslationTextComponent("computer.turn.off");
						text.getStyle().withColor(TextFormatting.GREEN);
						player.sendMessage(text,Util.NIL_UUID);
					}
				}
				return ActionResultType.SUCCESS;
			} else {
				//player.openGui(SevenDaysToMine.instance, 8, worldIn, pos.getX(), pos.getY(), pos.getZ());
				INamedContainerProvider namedContainerProvider = this.getMenuProvider(state, worldIn, pos);
				if (namedContainerProvider != null) {
					if (!(player instanceof ServerPlayerEntity))
						return ActionResultType.FAIL;
					ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
					NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, (packetBuffer) -> {
						packetBuffer.writeBlockPos(pos);
					});
				}
				if (!worldIn.isClientSide()) {
					monitorTE.addPlayer(player);
				}
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}

	@Nullable
	public INamedContainerProvider getMenuProvider(BlockState p_220052_1_, World p_220052_2_, BlockPos p_220052_3_) {
		TileEntity tileentity = p_220052_2_.getBlockEntity(p_220052_3_);
		return tileentity instanceof INamedContainerProvider ? (INamedContainerProvider) tileentity : null;
	}


	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityMonitor();
	}
}