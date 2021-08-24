package nuparu.sevendaystomine.util.dialogue;

import net.minecraft.nbt.CompoundNBT;

public interface IDialogue {

	public String getUnloclaizedName();
	public void setUnlocalizedName(String unlocalizedName);
	public String getLocalizedText();
	
	public void readFromNBT(CompoundNBT nbt);
	public CompoundNBT save(CompoundNBT nbt);
}
