package nuparu.sevendaystomine.electricity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class ElectricConnection {

	private BlockPos from;
	private BlockPos to;

	public ElectricConnection() {
		this.from = BlockPos.ZERO;
		this.to = BlockPos.ZERO;
	}

	public ElectricConnection(BlockPos from, BlockPos to) {
		this.from = from;
		this.to = to;
	}

	public CompoundNBT writeNBT(CompoundNBT nbt) {
		nbt.putLong("from", from.asLong());
		nbt.putLong("to", to.asLong());
		return nbt;
	}

	public void readNBT(CompoundNBT nbt) {
		if (nbt.contains("from", Constants.NBT.TAG_LONG)) {
			from = BlockPos.of(nbt.getLong("from"));
		}
		if (nbt.contains("to", Constants.NBT.TAG_LONG)) {
			to = BlockPos.of(nbt.getLong("to"));
		}
	}

	public BlockPos getFrom() {
		return this.from;
	}

	public BlockPos getTo() {
		return this.to;
	}
	
	public double getDistance() {
		return from.distSqr(to.getX(),to.getY(),to.getZ(),false);
	}

	@Override
	public String toString() {
		return "ElectricConnection [ " + from.getX() + ";" + from.getY() + ";" + from.getZ() + " //" + to.getX() + ";"
				+ to.getY() + ";" + to.getZ() + "]";
	}

	public IVoltage getFrom(World world) {
		TileEntity te = world.getBlockEntity(getFrom());
		if (te != null && te instanceof IVoltage) {
			return (IVoltage) te;
		}
		return null;
	}

	public IVoltage getTo(World world) {
		TileEntity te = world.getBlockEntity(getTo());
		if (te != null && te instanceof IVoltage) {
			return (IVoltage) te;
		}
		return null;
	}

	public Vector3d getRenderFrom(World world) {
		if (world == null)
			return new Vector3d(from.getX(), from.getY(), from.getZ());

		IVoltage voltage = getFrom(world);
		if (voltage == null) {
			return new Vector3d(from.getX(), from.getY(), from.getZ());
		}

		return voltage.getWireOffset().add(from.getX(), from.getY(), from.getZ());
	}

	public Vector3d getRenderTo(World world) {
		if (world == null)
			return new Vector3d(to.getX(), to.getY(), to.getZ());

		IVoltage voltage = getTo(world);
		if (voltage == null) {
			return new Vector3d(to.getX(), to.getY(), to.getZ());
		}


		return voltage.getWireOffset().add(to.getX(), to.getY(), to.getZ());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElectricConnection other = (ElectricConnection) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}
}
