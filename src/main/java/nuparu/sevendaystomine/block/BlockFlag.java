package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import nuparu.sevendaystomine.tileentity.TileEntityFlag;

public class BlockFlag extends BlockHorizontalBase {

	private static final VoxelShape NORTH = Block.box(0.375*16, 0.125F*16, 0.7F*16, 0.625F*16, 0.375F*16, 1F*16);
	private static final VoxelShape SOUTH = Block.box(0.375*16, 0.125F*16, 0.0F*16, 0.625F*16, 0.375F*16, 0.3F*16);
	private static final VoxelShape WEST = Block.box(0.7F*16, 0.125F*16, 0.375F*16, 1F*16, 0.375F*16, 0.625F*16);
	private static final VoxelShape EAST = Block.box(0.0F, 0.125F*16, 0.375F*16, 0.3F*16, 0.375F*16, 0.625F*16);

	public BlockFlag() {
		super(AbstractBlock.Properties.of(Material.WOOD).strength(0.7f, 1));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityFlag();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_,
			ISelectionContext p_220053_4_) {
		switch (state.getValue(HorizontalBlock.FACING)) {
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

}
