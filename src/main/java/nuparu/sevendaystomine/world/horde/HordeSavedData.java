package nuparu.sevendaystomine.world.horde;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.SevenDaysToMine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class HordeSavedData extends WorldSavedData {
    public static final String DATA_NAME = SevenDaysToMine.MODID + ":horde_data";
    protected CopyOnWriteArrayList<Horde> hordes = new CopyOnWriteArrayList<Horde>();

    public ServerWorld world;

    public HordeSavedData() {
        super(DATA_NAME);
    }

    public HordeSavedData(String s) {
        super(s);
    }


    @Override
    public void load(CompoundNBT compound) {
        if (compound.contains("hordes")) {
            hordes.clear();
            ListNBT list = compound.getList("hordes", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundNBT nbt = list.getCompound(i);
                if (!nbt.contains("class"))
                    continue;
                String className = nbt.getString("class");

                Class<?> clazz;
                try {
                    clazz = Class.forName(className);

                    if (clazz == null)
                        continue;
                    Constructor<?> constructor = clazz.getConstructor(World.class);
                    Horde horde = (Horde) constructor.newInstance(world);
                    horde.readFromNBT(nbt);
                    horde.data = this;
                    hordes.add(horde);
                } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
                        | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        if(hordes == null) {
            hordes = new CopyOnWriteArrayList<Horde>();
        }
        ListNBT list = new ListNBT();
        for (Horde horde : hordes) {
            list.add(horde.writeToNBT(new CompoundNBT()));
        }
        compound.put("hordes", list);
        return compound;
    }

    public void addHorde(Horde horde) {
        if(hordes == null) {
            hordes = new CopyOnWriteArrayList<Horde>();
        }
        if (!hordes.contains(horde)) {
            horde.data = this;
            hordes.add(horde);
        }
        setDirty();
    }

    public Horde getHordeByUUID(UUID uuid) {
        if(hordes == null) {
            hordes = new CopyOnWriteArrayList<Horde>();
        }
        for (Horde horde : hordes) {
            if (horde.uuid.equals(uuid)) {
                return horde;
            }
        }
        return null;
    }

    public void removeHorde(Horde horde) {
        if(hordes == null) {
            hordes = new CopyOnWriteArrayList<Horde>();
        }
        if (hordes.contains(horde)) {
            hordes.remove(horde);
            horde.onRemove();
            setDirty();
        }
    }

    public void clear() {
        if(hordes == null) {
            hordes = new CopyOnWriteArrayList<Horde>();
        }
        for (Horde horde : hordes) {
            horde.onRemove();
        }
        hordes.clear();
        setDirty();
    }

    public void update(World world) {
        if(hordes == null) {
            hordes = new CopyOnWriteArrayList<Horde>();
        }
        for (Horde horde : hordes) {
            horde.update();
        }
    }

    public static HordeSavedData getOrCreate(ServerWorld world) {
        HordeSavedData data = world.getDataStorage().get(HordeSavedData::new, DATA_NAME);
        if (data == null) {
            data = new HordeSavedData();
            world.getDataStorage().set(data);
        }
        return data;

    }
}
