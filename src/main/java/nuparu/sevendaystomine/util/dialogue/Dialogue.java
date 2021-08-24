package nuparu.sevendaystomine.util.dialogue;

import net.minecraft.nbt.CompoundNBT;
import nuparu.sevendaystomine.SevenDaysToMine;

public class Dialogue implements IDialogue {

	private String unlocalizedName;

	public Dialogue(String unlocalizedName) {
		this.unlocalizedName = unlocalizedName;
	}

	@Override
	public String getUnloclaizedName() {
		return this.unlocalizedName;
	}

	@Override
	public void setUnlocalizedName(String unlocalizedName) {
		this.unlocalizedName = unlocalizedName;
	}

	@Override
	public String getLocalizedText() {
		return SevenDaysToMine.proxy.localize(unlocalizedName + ".text");
	}

	@Override
	public void readFromNBT(CompoundNBT nbt) {
		this.unlocalizedName = nbt.getString("name");
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		nbt.putString("name", this.unlocalizedName);
		return nbt;
	}

}
