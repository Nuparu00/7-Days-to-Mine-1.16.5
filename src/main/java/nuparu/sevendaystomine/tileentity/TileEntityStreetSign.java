package nuparu.sevendaystomine.tileentity;

import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntityType;
import nuparu.sevendaystomine.init.ModTileEntities;

public class TileEntityStreetSign extends SignTileEntity
{
	@Override
	public TileEntityType<TileEntityStreetSign> getType()
	{
		return ModTileEntities.STREET_SIGN.get();
	}
}