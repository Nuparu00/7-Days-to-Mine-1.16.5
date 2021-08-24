package nuparu.sevendaystomine.computer.process;

import java.util.ArrayList;
import java.util.ListIterator;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.client.gui.monitor.DesktopShortcut;
import nuparu.sevendaystomine.client.gui.monitor.TaskbarButton;
import nuparu.sevendaystomine.computer.application.Application;
import nuparu.sevendaystomine.computer.application.ApplicationRegistry;
import nuparu.sevendaystomine.computer.process.WindowsDesktopProcess.IconPosUpdate;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.StartProcessMessage;
import nuparu.sevendaystomine.network.packets.SyncIconMessage;

public abstract class DesktopProcess extends TickingProcess {

	public ArrayList<ResourceLocation> processQueue = new ArrayList<ResourceLocation>();
	public boolean start = false;

	public ArrayList<DesktopShortcut> shortcuts = new ArrayList<DesktopShortcut>();
	public ArrayList<TaskbarButton> taskbarIcons = new ArrayList<TaskbarButton>();

	protected boolean shutdown = false;

	public DesktopProcess() {
		super();
	}

	@Override
	public void tick() {
		super.tick();
		if (this.shutdown) {
			if (computerTE != null) {
				computerTE.stopComputer();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		super.save(nbt);
		nbt.putBoolean("shutdown", shutdown);

		ListNBT list = new ListNBT();

		ListIterator<ResourceLocation> it = ((ArrayList<ResourceLocation>) (processQueue.clone())).listIterator();
		while (it.hasNext()) {
			ResourceLocation process = it.next();
			list.add(StringNBT.valueOf(process.toString()));
		}

		nbt.put("processQueue", list);
		return nbt;
	}

	@Override
	public void readFromNBT(CompoundNBT nbt) {
		super.readFromNBT(nbt);
		this.shutdown = nbt.getBoolean("shutdown");

		ListNBT list = nbt.getList("processQueue", Constants.NBT.TAG_STRING);
		if (computerTE != null && !computerTE.getLevel().isClientSide()) {
			for (int i = 0; i < list.size(); i++) {
				TickingProcess process = ProcessRegistry.INSTANCE
						.getByRes(new ResourceLocation(list.getString(i) + "_process"));
				if (process != null) {
					if (process instanceof WindowedProcess) {
						WindowedProcess wp = (WindowedProcess) process;
						wp.application = ApplicationRegistry.INSTANCE
								.getByRes(new ResourceLocation(list.getString(i)));
						wp.x = -1;
						wp.y = -1;
						wp.width = 100;
						wp.height = 100;
						wp.zLevel = 4;
					}
					computerTE.startProcess(process);
				}
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void onIconMove(Application app, double x, double y) {
		IconPosUpdate update = new IconPosUpdate();
		update.app = app;
		update.x = x;
		update.y = y;

		PacketManager.syncIcon
				.sendToServer(new SyncIconMessage(computerTE.getPos(), update.save(new CompoundNBT())));

	}

	public void tryToRunProcess(ResourceLocation res) {
		this.processQueue.add(res);

		CompoundNBT nbt = save(new CompoundNBT());
		PacketManager.startProcess.sendToServer(new StartProcessMessage(computerTE.getPos(), nbt));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void render(MatrixStack matrix,float partialTicks) {

	}
	
	public double getTaskbarHeight() {
		return screen.ySize * 0.1;	
	}
	
	public double iconSize() {
		return screen.ySize * 0.1;	
	}
}
