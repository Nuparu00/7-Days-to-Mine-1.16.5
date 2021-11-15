package nuparu.sevendaystomine.electricity;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IBattery {

	long getCapacity(ItemStack stack, @Nullable World world);
	long getVoltage(ItemStack stack, @Nullable World world);
	void setVoltage(ItemStack stack, @Nullable World world, long voltage);
	long tryToAddVoltage(ItemStack stack, @Nullable World world, long deltaVoltage);
	void drainVoltage(ItemStack stack, @Nullable World world, long deltaVoltage);
}
