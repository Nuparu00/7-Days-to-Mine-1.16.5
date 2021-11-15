package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import nuparu.sevendaystomine.init.ModItemGroups;
import nuparu.sevendaystomine.tileentity.TileEntitySolarPanel;

public class BlockSolarPanel extends BlockHorizontalBase {

	private final VoxelShape SHAPE = Block.box(0.0F, 0F, 0F, 16F, 14, 16F);

	public BlockSolarPanel(AbstractBlock.Properties properties) {
		super(properties);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_,
			ISelectionContext p_220053_4_) {
		return SHAPE;
	}

	/*@Override
	public BlockRenderType getRenderShape(BlockState p_149645_1_) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}*/

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntitySolarPanel();
	}

	@Override
	public ItemGroup getItemGroup() {
		return ModItemGroups.TAB_ELECTRICITY;
	}

}