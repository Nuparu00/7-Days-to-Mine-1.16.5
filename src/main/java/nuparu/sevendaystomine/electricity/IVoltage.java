package nuparu.sevendaystomine.electricity;

import java.util.List;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.energy.IEnergyStorage;

public interface IVoltage extends IEnergyStorage {

	EnumDeviceType getDeviceType();
	
	int getMaximalInputs();
	
	int getMaximalOutputs();
	
	List<ElectricConnection> getInputs();
	
	List<ElectricConnection> getOutputs();
	
	long getOutput();
	
	long getMaxOutput();
	
	long getOutputForConnection(ElectricConnection connection);
	
	boolean tryToConnect(ElectricConnection connection);
	
	boolean canConnect(ElectricConnection connection);

	long getRequiredPower();
	
	long getCapacity();
	long getVoltageStored();
	void storePower(long power);
	
	/*
	 * Returns the power that could fit
	 */
    long tryToSendPower(long power, ElectricConnection connection);
	
	Vector3d getWireOffset();
	
	/*
	 * If is passive, the block does not require a direct connection to an energy pole to receive power (like lamps)
	 */
    boolean isPassive();
	
	boolean disconnect(IVoltage voltage);
	BlockPos getPos();
}
