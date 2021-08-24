package nuparu.sevendaystomine.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;

public abstract class TileEntityCar extends TileEntity {

	public TileEntityCar(TileEntityType<?> type) {
		super(type);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(this.getBlockPos().offset(-2.5, 0, -2.5), getBlockPos().offset(2.5, 2, 2.5));
	}

	public abstract TileEntityCarMaster getMaster();

}
