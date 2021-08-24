package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.item.IScrapable;
import nuparu.sevendaystomine.tileentity.TileEntityWheels;

public class BlockWheels extends BlockHorizontalBase implements IScrapable{
	
	private static final VoxelShape AABB = Block.box(0.125d, 0, 0.125d, 0.875d, 0.4375d, 0.875d);

	private EnumMaterial material = EnumMaterial.CLOTH;
	private int weight = 2;
	
	
	public BlockWheels(AbstractBlock.Properties properties) {
		super(properties);
	}


	@Override
	public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_,
			ISelectionContext p_220053_4_) {
		return AABB;
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
	public boolean hasTileEntity(BlockState state)
    {
        return true;
    }
	
	@Override
    @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityWheels();
    }

}
