package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.item.ItemWire;
import nuparu.sevendaystomine.tileentity.TileEntityEnergySwitch;

public class BlockEnergySwitch extends BlockHorizontalBase {

	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;


	private static final VoxelShape UP = Block.box(0.375F*16, 0.0F, 0.375F*16, 0.625F*16, 1F*16, 0.625F*16);

	private EnumMaterial material = EnumMaterial.WOOD;
	private int weight = 2;

	public BlockEnergySwitch(AbstractBlock.Properties properties) {
		super(properties);
		this.registerDefaultState(
				this.defaultBlockState().setValue(FACING, Direction.SOUTH).setValue(POWERED, Boolean.valueOf(false)));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_,
			ISelectionContext p_220053_4_) {
		return UP;
	}

	public BlockState rotate(BlockState p_185499_1_, Rotation p_185499_2_) {
		return p_185499_1_.setValue(FACING, p_185499_2_.rotate(p_185499_1_.getValue(FACING)));
	}

	public BlockState mirror(BlockState p_185471_1_, Mirror p_185471_2_) {
		return p_185471_1_.rotate(p_185471_2_.getRotation(p_185471_1_.getValue(FACING)));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult result) {
		if (player.getItemInHand(hand).getItem() instanceof ItemWire) {
			return ActionResultType.PASS;
		}

		if (world.isClientSide) {
			BlockState blockstate1 = state.cycle(POWERED);
			if (blockstate1.getValue(POWERED)) {
				makeParticle(blockstate1, world, pos, 1.0F);
			}

			return ActionResultType.SUCCESS;
		} else {
			BlockState blockstate = this.pull(state, world, pos);
			float f = blockstate.getValue(POWERED) ? 0.6F : 0.5F;
			world.playSound((PlayerEntity) null, pos, SoundEvents.LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, f);
			return ActionResultType.CONSUME;
		}
	}

	public BlockState pull(BlockState state, World world, BlockPos pos) {
		CompoundNBT nbt = world.getBlockEntity(pos).save(new CompoundNBT());
		state = state.cycle(POWERED);
		world.setBlock(pos, state, 3);
		this.updateNeighbours(state, world, pos);
		world.getBlockEntity(pos).load(state, nbt);
		return state;
	}

	private static void makeParticle(BlockState p_196379_0_, IWorld p_196379_1_, BlockPos p_196379_2_,
			float p_196379_3_) {
		Direction direction = p_196379_0_.getValue(FACING).getOpposite();
		double d0 = (double) p_196379_2_.getX() + 0.5D + 0.1D * (double) direction.getStepX();
		double d1 = (double) p_196379_2_.getY() + 0.5D + 0.1D * (double) direction.getStepY();
		double d2 = (double) p_196379_2_.getZ() + 0.5D + 0.1D * (double) direction.getStepZ();
		p_196379_1_.addParticle(new RedstoneParticleData(1.0F, 0.0F, 0.0F, p_196379_3_), d0, d1, d2, 0.0D, 0.0D, 0.0D);
	}

	@Override
	public void neighborChanged(BlockState blockstate, World world, BlockPos pos, Block block, BlockPos frompos,
			boolean p_220069_6_) {
		BlockState state = world.getBlockState(pos.below());
		if (state.getBlock() == Blocks.AIR) {
			dropResources(blockstate, world, pos);
			world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		} else if (world.hasNeighborSignal(pos)) {
			CompoundNBT nbt = world.getBlockEntity(pos).save(new CompoundNBT());
			world.setBlockAndUpdate(pos, blockstate.setValue(POWERED, !blockstate.getValue(POWERED)));
			float f = ((Boolean) blockstate.getValue(POWERED)).booleanValue() ? 0.6F : 0.5F;
			world.playSound((PlayerEntity) null, pos, SoundEvents.LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, f);
			world.getBlockEntity(pos).load(state, nbt);
		}
	}

	private void updateNeighbours(BlockState p_196378_1_, World p_196378_2_, BlockPos p_196378_3_) {
		p_196378_2_.updateNeighborsAt(p_196378_3_, this);
	}

	@Override
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityEnergySwitch();
	}

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED);
	}

}