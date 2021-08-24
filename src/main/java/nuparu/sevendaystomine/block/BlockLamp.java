package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import nuparu.sevendaystomine.tileentity.TileEntityLamp;

public class BlockLamp extends BlockTileProvider<TileEntityLamp> {

	public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

	public BlockLamp(AbstractBlock.Properties p_i48343_1_) {
		super(p_i48343_1_);
		this.registerDefaultState(this.defaultBlockState().setValue(LIT, Boolean.valueOf(false)));
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
		return this.defaultBlockState().setValue(LIT, Boolean.valueOf(false));
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityLamp();
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult rayTraceResult) {
		TileEntity TE = worldIn.getBlockEntity(pos);
		if (TE != null && TE instanceof TileEntityLamp) {
			TileEntityLamp lampTE = (TileEntityLamp) TE;
			if (player.isCrouching()) {
				if (!worldIn.isClientSide()) {
					lampTE.setState(!lampTE.getState());
				}
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.FAIL;
	}

	public static void setState(World world, BlockPos pos, boolean state) {
		CompoundNBT nbt = world.getBlockEntity(pos).save(new CompoundNBT());
		world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(LIT, state));
		world.getBlockEntity(pos).load(world.getBlockState(pos),nbt);
	}

	public static boolean isLit(World world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (!(state.getBlock() instanceof BlockLamp)) {
			return false;
		}
		return state.getValue(LIT);
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
		p_206840_1_.add(LIT);
	}

}
