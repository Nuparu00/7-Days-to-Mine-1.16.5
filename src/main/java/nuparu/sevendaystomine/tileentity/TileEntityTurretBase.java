package nuparu.sevendaystomine.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.block.ContainerSmall;

public class TileEntityTurretBase extends TileEntityTurret {

	public TileEntityTurretBase() {
		super(ModTileEntities.TURRET_BASE.get());
	}

	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity player) {
		return ContainerSmall.createContainerServerSide(windowID, playerInventory, this);
	}

}
