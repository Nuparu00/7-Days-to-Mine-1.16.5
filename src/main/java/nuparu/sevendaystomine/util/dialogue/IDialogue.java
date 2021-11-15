package nuparu.sevendaystomine.util.dialogue;

import net.minecraft.nbt.CompoundNBT;

public interface IDialogue {

	String getUnloclaizedName();
	void setUnlocalizedName(String unlocalizedName);
	String getLocalizedText();
	
	void readFromNBT(CompoundNBT nbt);
	CompoundNBT save(CompoundNBT nbt);
}
