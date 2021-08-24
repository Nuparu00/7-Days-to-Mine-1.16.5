package nuparu.sevendaystomine.computer;

import net.minecraft.nbt.CompoundNBT;

public class File {

	String name;
	String path;
	String code;

	public File(String name, String path, String code) {
		this.name = name;
		this.path = path;
		this.code = code;
	}

	public void readFromNBT(CompoundNBT nbt) {
		this.name = nbt.getString("name");
		this.path = nbt.getString("path");
		this.code = nbt.getString("code");
	}

	public CompoundNBT save(CompoundNBT nbt) {
		nbt.putString("name", name);
		nbt.putString("path", path);
		nbt.putString("code", code);
		return nbt;

	}

}
