package nuparu.sevendaystomine.block;

import net.minecraft.block.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import nuparu.sevendaystomine.tileentity.TileEntityStreetSign;

public class BlockStreetSign extends StandingSignBlock {

	public BlockStreetSign(Properties propertiesIn, WoodType woodTypeIn) {
		super(propertiesIn, woodTypeIn);
	}

	@Override
	public boolean hasTileEntity(BlockState stateIn) {
		return true;
	}

	@Override
	public TileEntity newBlockEntity(IBlockReader worldIn) {
		return new TileEntityStreetSign();
	}
}