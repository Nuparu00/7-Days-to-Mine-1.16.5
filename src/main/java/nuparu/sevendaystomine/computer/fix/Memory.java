package nuparu.sevendaystomine.computer.fix;

import net.minecraft.nbt.CompoundNBT;
import nuparu.sevendaystomine.computer.EnumSystem;
import nuparu.sevendaystomine.computer.File;

import java.util.List;

public class Memory {

    public List<Process> runningProcesses;
    public EnumSystem system;

    public List<File> files;

    public Memory(){

    }


    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putString("test","0");
        nbt.putInt("integera",69);
        return nbt;
    }

    public void readFromNBT(CompoundNBT nbt) {

    }

}
