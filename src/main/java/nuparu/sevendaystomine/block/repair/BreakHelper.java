package nuparu.sevendaystomine.block.repair;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

public class BreakHelper {
	public static final BreakHelper INSTANCE = new BreakHelper();

	public BreakHelper() {
	}

	public static CompoundNBT save(HashMap<BlockPos, BreakData> map) {

		CompoundNBT nbt = new CompoundNBT();
		ListNBT list = new ListNBT();
		Iterator<Entry<BlockPos, BreakData>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<BlockPos, BreakData> pair = it.next();
			CompoundNBT entryNBT = new CompoundNBT();
			entryNBT.putLong("pos", pair.getKey().asLong());
			pair.getValue().save(entryNBT);
			list.add(entryNBT);
		}
		nbt.put("map", list);

		return nbt;
	}

	public static HashMap<BlockPos, BreakData> of(CompoundNBT nbt) {
		HashMap<BlockPos, BreakData> map = new HashMap<BlockPos, BreakData>();
		if (nbt.contains("map")) {
			ListNBT list = nbt.getList("map", Constants.NBT.TAG_COMPOUND);
			Iterator<INBT> it = list.iterator();
				while(it.hasNext()) {
					INBT inbt = it.next();
					if(inbt instanceof CompoundNBT) {
						CompoundNBT entryNBT = (CompoundNBT)inbt;
						if(!entryNBT.contains("pos")) continue;
						if(!entryNBT.contains("lastChange")) continue;
						if(!entryNBT.contains("state")) continue;
						BlockPos pos = BlockPos.of(entryNBT.getLong("pos"));
						BreakData data = BreakData.of(entryNBT);

						map.put(pos, data);
					}
				}
			
		}

		return map;
	}

	public static CompoundNBT save(ArrayList<BreakData> list) {
		CompoundNBT nbt = new CompoundNBT();
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(list);
			byte[] bytes = bos.toByteArray();
			nbt.putByteArray("breakData", bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nbt;
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<BreakData> readFromNBT(CompoundNBT nbt) {
		if (nbt.contains("breakData")) {
			byte[] bytes = nbt.getByteArray("breakData");

			Object obj = null;
			ByteArrayInputStream bis = null;
			ObjectInputStream ois = null;
			try {
				bis = new ByteArrayInputStream(bytes);
				ois = new ObjectInputStream(bis);
				obj = ois.readObject();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (bis != null) {
					try {
						bis.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (ois != null) {
					try {
						ois.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			return (ArrayList<BreakData>) obj;
		}
		return null;
	}
}