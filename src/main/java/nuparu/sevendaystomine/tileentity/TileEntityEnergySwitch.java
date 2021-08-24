package nuparu.sevendaystomine.tileentity;

import net.minecraft.util.math.vector.Vector3d;
import nuparu.sevendaystomine.block.BlockEnergySwitch;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.util.ModConstants;

public class TileEntityEnergySwitch extends TileEntityEnergyPole {

	public TileEntityEnergySwitch() {
		super(ModTileEntities.ENERGY_SWITCH.get());
	}

	@Override
	public long tryToSendPower(long power, ElectricConnection connection) {
		if (level.getBlockState(worldPosition).getValue(BlockEnergySwitch.POWERED) == false)
			return 0;
		long canBeAdded = capacity - voltage;
		long delta = Math.min(canBeAdded, power);
		long lost = 0;
		if (connection != null) {
			lost = (long) Math.round(delta * ModConstants.DROP_PER_BLOCK * connection.getDistance());
		}
		long realDelta = delta - lost;
		this.voltage += realDelta;

		return delta;
	}

	@Override
	public Vector3d getWireOffset() {
		return offsetDown;

	}
}
