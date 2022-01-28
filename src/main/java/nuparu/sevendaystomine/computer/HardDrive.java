package nuparu.sevendaystomine.computer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.computer.application.Application;
import nuparu.sevendaystomine.computer.application.ApplicationRegistry;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;

public class HardDrive {

	public TileEntityComputer te;

	public ArrayList<Application> applications = new ArrayList<Application>();
	public ConcurrentHashMap<double[], Application> desktopIcons = new ConcurrentHashMap<double[], Application>();
	public ConcurrentHashMap<double[], File> files = new ConcurrentHashMap<double[], File>();

	private boolean bussy = false;

	public HardDrive(TileEntityComputer te) {
		this.te = te;
		if (te.getSystem() == EnumSystem.MAC) {
			desktopIcons.put(new double[] { 0.1, 32 }, ApplicationRegistry.INSTANCE.getByString("shell"));
			desktopIcons.put(new double[] { 0.1, 96 }, ApplicationRegistry.INSTANCE.getByString("notes"));
			desktopIcons.put(new double[] { 0.1, 64 }, ApplicationRegistry.INSTANCE.getByString("cctv"));
			desktopIcons.put(new double[] { 20, 32 }, ApplicationRegistry.INSTANCE.getByString("explorer"));
			desktopIcons.put(new double[] { 20, 64 }, ApplicationRegistry.INSTANCE.getByString("transit"));
			desktopIcons.put(new double[] { 20, 96 }, ApplicationRegistry.INSTANCE.getByString("epidemic"));
		} else {
			desktopIcons.put(new double[] { 0.1, 0.1 }, ApplicationRegistry.INSTANCE.getByString("shell"));
			desktopIcons.put(new double[] { 0.1, 64 }, ApplicationRegistry.INSTANCE.getByString("notes"));
			desktopIcons.put(new double[] { 0.1, 32 }, ApplicationRegistry.INSTANCE.getByString("cctv"));
			desktopIcons.put(new double[] { 20, 0.1 }, ApplicationRegistry.INSTANCE.getByString("explorer"));
			desktopIcons.put(new double[] { 20, 32 }, ApplicationRegistry.INSTANCE.getByString("transit"));
			desktopIcons.put(new double[] { 20, 64 }, ApplicationRegistry.INSTANCE.getByString("epidemic"));
		}
	}

	public CompoundNBT save(CompoundNBT nbt) {

		if (applications != null) {
			CompoundNBT apps = new CompoundNBT();
			apps.putInt("count", applications.size());
			for (int i = 0; i < applications.size(); i++) {
				Application app = applications.get(i);
				apps.putString("app" + i, ApplicationRegistry.INSTANCE.getResByApp(app).toString());
			}
			nbt.put("applications", apps);
		}
		if (desktopIcons != null) {
			ListNBT icons = new ListNBT();
			int i = 0;
			bussy = true;
			Iterator<Entry<double[], Application>> it = desktopIcons.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<double[], Application> entry = it.next();
				double[] coords = entry.getKey();
				Application app = entry.getValue();

				if (coords.length < 2) {
					continue;
				}

				CompoundNBT icon = new CompoundNBT();
				icon.putDouble("x", coords[0]);
				icon.putDouble("y", coords[1]);
				icon.putString("app", ApplicationRegistry.INSTANCE.getResByApp(app).toString());
				icons.add(icon);
				++i;
			}
			bussy = false;
			nbt.put("icons", icons);
		}

		return nbt;
	}

	public void readFromNBT(CompoundNBT nbt) {
		applications.clear();

		if (nbt.contains("applications")) {
			CompoundNBT apps = nbt.getCompound("applications");
			if (apps.contains("count")) {
				int count = apps.getInt("count");
				if (count > 0) {
					for (int i = 0; i < count; i++) {
						String s = apps.getString("app" + i);
						ResourceLocation res = new ResourceLocation(s);
						Application app = ApplicationRegistry.INSTANCE.getByRes(res);
						if (app != null) {
							applications.add(app);
						}
					}
				}
			}
		}
		desktopIcons.clear();
		if (nbt.contains("icons")) {
			ListNBT icons = nbt.getList("icons", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < icons.size(); i++) {
				CompoundNBT icon = icons.getCompound(i);
				double x = icon.getDouble("x");
				double y = icon.getDouble("y");
				String s = icon.getString("app");
				ResourceLocation res = new ResourceLocation(s);
				Application app = ApplicationRegistry.INSTANCE.getByRes(res);
				if (app != null) {
					desktopIcons.put(new double[] { x, y }, app);
				}
			}
		}
	}

	public void setIconPostion(double x, double y, Application app) {
		for (Map.Entry<double[], Application> entry : desktopIcons.entrySet()) {
			if (entry.getValue() == app) {
				desktopIcons.remove(entry.getKey());
				desktopIcons.put(new double[] { x, y }, app);
				te.setChanged();
			}
		}
	}

}
