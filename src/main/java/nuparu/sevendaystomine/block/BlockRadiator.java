package nuparu.sevendaystomine.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.init.ModLootTables;

import java.util.ArrayList;
import java.util.List;

public class BlockRadiator extends BlockHorizontalBase implements IWaterLoggable, ISalvageable {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public ResourceLocation salvageLootTable = ModLootTables.RADIATOR_SALVAGE;

	public VoxelShape NORTH = Block.box(0.0F, 0.0F, 11.2F, 16.0F, 12F, 16.0F);
	public VoxelShape SOUTH = Block.box(0.0F, 0.0F, 0.0F, 16.0F, 12F, 4.8F);
	public VoxelShape WEST = Block.box(11.2F, 0.0F, 0F, 16.0F, 12F, 16.0F);
	public VoxelShape EAST = Block.box(0.0F, 0.0F, 0.0F, 4.8F, 12F, 16.0F);

	public BlockRadiator() {
		super(AbstractBlock.Properties.of(Material.METAL).sound(SoundType.METAL).strength(1.5f, 1).noOcclusion());
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.SOUTH).setValue(WATERLOGGED, Boolean.FALSE));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_,
			ISelectionContext p_220053_4_) {
		switch (state.getValue(FACING)) {
		default:
		case NORTH:
			return NORTH;
		case SOUTH:
			return SOUTH;
		case WEST:
			return WEST;
		case EAST:
			return EAST;
		}

	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		Direction dir = context.getClickedFace();
		if(dir.getAxis() == Direction.Axis.Y){
			dir = context.getHorizontalDirection().getOpposite();
		}
		return this.defaultBlockState().setValue(FACING, dir).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
	}

	@Override
	public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, IWorld p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
		if (p_196271_1_.getValue(WATERLOGGED)) {
			p_196271_4_.getLiquidTicks().scheduleTick(p_196271_5_, Fluids.WATER, Fluids.WATER.getTickDelay(p_196271_4_));
		}

		return super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
	}

	@Override
	public ResourceLocation getSalvageLootTable(){
		return salvageLootTable;
	}

	@Override
	public void setSalvageLootTable(ResourceLocation resourceLocation){
		salvageLootTable = resourceLocation;
	}

	@Override
	public SoundEvent getSound() {
		return SoundEvents.ANVIL_LAND;
	}

	@Override
	public float getUpgradeRate(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		return 4;
	}

	@Override
	public void onSalvage(World world, BlockPos pos, BlockState oldState) {
		world.destroyBlock(pos, false);
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState p_204507_1_) {
		return p_204507_1_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_204507_1_);
	}
}
