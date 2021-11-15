package nuparu.sevendaystomine.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import nuparu.sevendaystomine.SevenDaysToMine;

public class MiscSavedData extends WorldSavedData {
	public static final String DATA_NAME = SevenDaysToMine.MODID + ":misc_data";

	protected int dim = Integer.MIN_VALUE;

	private int lastAirdrop = 0;

	public MiscSavedData() {
		super(DATA_NAME);
	}

	public MiscSavedData(String s) {
		super(s);
	}

	@Override
	public void load( CompoundNBT compound) {
		dim = compound.getInt("dim");
		setLastAirdrop(compound.getInt("lastAirdrop"));

	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		ListNBT list = new ListNBT();

		compound.putInt("dim", dim);
		compound.putInt("lastAirdrop", getLastAirdrop());
		return compound;
	}

    public static MiscSavedData getOrCreate(ServerWorld world) {
    	MiscSavedData data = world.getDataStorage().get(MiscSavedData::new, DATA_NAME);
        if (data == null) {
            data = new MiscSavedData();
            world.getDataStorage().set(data);
        }
        return data;

    }

	public int getLastAirdrop() {
		return lastAirdrop;
	}

	public void setLastAirdrop(int lastAirdrop) {
		this.lastAirdrop = lastAirdrop;
		this.setDirty(true);
	}

}
