package nuparu.sevendaystomine.computer.process;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.MainWindow;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.gui.monitor.DesktopShortcut;
import nuparu.sevendaystomine.client.gui.monitor.IScreenElement;
import nuparu.sevendaystomine.client.gui.monitor.MonitorScreen;
import nuparu.sevendaystomine.client.gui.monitor.TaskbarButton;
import nuparu.sevendaystomine.client.gui.monitor.elements.Button;
import nuparu.sevendaystomine.client.gui.monitor.elements.TexturedButton;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.computer.HardDrive;
import nuparu.sevendaystomine.computer.application.Application;
import nuparu.sevendaystomine.computer.application.ApplicationRegistry;
import nuparu.sevendaystomine.util.ColorRGBA;
import nuparu.sevendaystomine.util.Utils;

public class WindowsDesktopProcess extends DesktopProcess {

	public final ResourceLocation WIN_10_BGR_DEF = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/backgrounds/bgr_win10.png");
	public final ResourceLocation WIN_8_BGR_DEF = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/backgrounds/bgr_win8.png");
	public final ResourceLocation WIN_7_BGR_DEF = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/backgrounds/bgr_win7.png");
	public final ResourceLocation WIN_XP_BGR_DEF = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/backgrounds/bgr_winxp.png");
	public final ResourceLocation WIN_98_BGR_DEF = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/backgrounds/bgr_win98.png");
	
	
	public final ResourceLocation WIN_10_START = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/apps/win10_start.png");
	public final ResourceLocation WIN_7_START = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/apps/win7_start.png");
	public final ResourceLocation WIN_10_SHUTDOWN = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/apps/win10_shutdown.png");

	public final ColorRGBA WIN_10_COL_DEF = new ColorRGBA(0.16, 0.17, 0.2, 0.8);
	public final ColorRGBA WIN_10_COL_START = new ColorRGBA(0.1, 0.11, 0.14, 0.76);

	private static final NumberFormat HOUR_FORMATTER = new DecimalFormat("00");

	public WindowsDesktopProcess() {
		super();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientInit() {
		super.clientInit();
		MainWindow sr = MonitorScreen.mc.getWindow();
		ResourceLocation start;
		ColorRGBA hover;
		switch (computerTE.getSystem()) {
		default:
		case NONE:
		case WIN10:
			start = WIN_10_START;
			hover = new ColorRGBA(0.3, 0.7, 0.95);
			break;
		case WIN7:
			start = WIN_7_START;
			hover = new ColorRGBA(0.8, 0.8, 0.8);
			break;
		}
		TexturedButton button = new TexturedButton((sr.getGuiScaledWidth() / 2) - (screen.xSize / 2),
				(sr.getGuiScaledHeight() / 2) + (screen.ySize / 2) - (getTaskbarHeight()), getTaskbarHeight(),
				getTaskbarHeight(), screen, "", start, 0);
		button.textNormal = 0xffffff;
		button.normal = new ColorRGBA(1d, 1d, 1d);
		button.hovered = hover;
		button.setFontSize(0.7);
		button.setProcess(this);
		button.setZLevel(1);
		elements.add(button);

		double i = screen.ySize * 0.05;
		TexturedButton button1 = new TexturedButton((sr.getGuiScaledWidth() / 2) - (screen.xSize / 2),
				(sr.getGuiScaledHeight() / 2) + (screen.ySize / 2) - (1.5 * getTaskbarHeight()) - 2, i, i, screen, "",
				WIN_10_SHUTDOWN, 1) {

			@Override
			public boolean isDisabled() {
				return ((WindowsDesktopProcess) tickingProcess).start == false;
			}

		};
		button1.textNormal = 0xffffff;
		button1.normal = new ColorRGBA(1d, 1d, 1d);
		button1.hovered = new ColorRGBA(0.3, 0.7, 0.95);
		button1.setFontSize(0.7);
		button1.setProcess(this);
		button1.setZLevel(3);
		elements.add(button1);

		refreshDesktop();

	}

	@OnlyIn(Dist.CLIENT)
	public void clientTick() {
		super.clientTick();
		Iterator<DesktopShortcut> it = shortcuts.iterator();
		while (it.hasNext()) {
			it.next().update();
		}
		Iterator<DesktopShortcut> it2 = shortcuts.iterator();
		while (it2.hasNext()) {
			it2.next().update();
		}

		Iterator<TaskbarButton> it3 = taskbarIcons.iterator();
		while (it3.hasNext()) {
			it3.next().update();
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void render(MatrixStack matrix,float partialTicks) {

		renderBackground(matrix,partialTicks);
		renderTaskbar(matrix,partialTicks);
		renderStart(matrix,partialTicks);

		for (IScreenElement element : elements) {
			element.render(matrix,partialTicks);
		}

		Iterator<DesktopShortcut> it = shortcuts.iterator();
		while (it.hasNext()) {
			it.next().render(matrix,partialTicks);
		}

		Iterator<TaskbarButton> it2 = taskbarIcons.iterator();
		while (it2.hasNext()) {
			it2.next().render(matrix,partialTicks);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void renderBackground(MatrixStack matrix,float partialTicks) {
		MainWindow sr = MonitorScreen.mc.getWindow();
		ResourceLocation bgr;
		switch (computerTE.getSystem()) {
		default:
		case NONE:
			bgr = WIN_10_BGR_DEF;
			break;
		case WIN10:
			bgr = WIN_10_BGR_DEF;
			break;
		case WIN8:
			bgr = WIN_8_BGR_DEF;
			break;
		case WIN7:
			bgr = WIN_7_BGR_DEF;
			break;
		case WINXP:
			bgr = WIN_XP_BGR_DEF;
			break;
		case WIN98:
			bgr = WIN_98_BGR_DEF;
			break;
		}
		matrix.pushPose();

		/*RenderUtils.drawColoredRect(matrix,new ColorRGBA(0.1,0.3,0.5), (sr.getGuiScaledWidth() / 2) - (screen.xSize / 2),
				(sr.getGuiScaledHeight() / 2) - (screen.ySize / 2), screen.xSize,
				screen.ySize, 0);*/
		/*RenderUtils.drawTexturedRect(matrix,bgr, (sr.getGuiScaledWidth() / 2) - (screen.xSize / 2),
				(sr.getGuiScaledHeight() / 2) - (screen.ySize / 2), 0, 0, screen.xSize, screen.ySize, screen.xSize,
				screen.ySize, 1, 0);*/
		Minecraft.getInstance().getTextureManager().bind(bgr);
		AbstractGui.blit(matrix,(int)(sr.getGuiScaledWidth() / 2) - (int)(screen.xSize / 2),
				(int)(sr.getGuiScaledHeight() / 2) - (int)(screen.ySize / 2),0, 0, (int)screen.xSize, (int)screen.ySize, (int)screen.xSize,
				(int)screen.ySize);

		matrix.popPose();
	}

	@OnlyIn(Dist.CLIENT)
	public void renderTaskbar(MatrixStack matrix,float partialTicks) {
		MainWindow sr = MonitorScreen.mc.getWindow();
		//RenderSystem.enableBlend();

		matrix.pushPose();

		matrix.translate(0,0,1);
		RenderUtils.drawColoredRect(matrix,this.WIN_10_COL_DEF, (sr.getGuiScaledWidth() / 2) - (screen.xSize / 2),
				(sr.getGuiScaledHeight() / 2) + (screen.ySize / 2) - (getTaskbarHeight()), screen.xSize,
				(getTaskbarHeight()), 2);
		//RenderSystem.disableBlend();
		double scale = this.getTaskbarHeight() / (0.1 * screen.ySize);
		matrix.translate(screen.getRelativeX(1) - (getTaskbarHeight()),
				screen.getRelativeY(1) - (screen.ySize * 0.05) - 0.025*screen.ySize*scale, 0);

		matrix.scale((float)scale, (float)scale, 1);
		matrix.translate(0, 0, 2);

		int hours = 0;
		int minutes = 0;
		if (computerTE != null && computerTE.hasLevel()) {
			hours = Utils.getWorldHours(computerTE.getLevel());
			minutes = Utils.getWorldMinutes(computerTE.getLevel());
		}

		RenderUtils.drawCenteredString(matrix,new StringBuilder("").append(HOUR_FORMATTER.format(hours)).append(":")
				.append(HOUR_FORMATTER.format(minutes)).toString(), 0, 0, 0xffffff);
		matrix.translate(0, 0, -3);
		matrix.popPose();
	}

	@OnlyIn(Dist.CLIENT)
	public void renderStart(MatrixStack matrix,float partialTicks) {
		if (start) {
			RenderUtils.drawColoredRect(matrix,WIN_10_COL_START, screen.getRelativeX(0), screen.getRelativeY(1 / 3d),
					(screen.xSize / 4d), (screen.ySize * (2 / 3d)) - (getTaskbarHeight()), 3);
			RenderSystem.pushMatrix();
			RenderSystem.translated(0, 0, 4);
			RenderUtils.drawString(matrix,computerTE.getUsername(), screen.getRelativeX(0) + 2,
					screen.getRelativeY(1 / 3d) + 2, 0xffffff);
			RenderSystem.translated(0, 0, -4);
			RenderSystem.popMatrix();
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void initWindow() {
		if (elements.size() == 0) {
			return;
		}
		MainWindow sr = MonitorScreen.mc.getWindow();
		IScreenElement element0 = elements.get(0);

		element0.setX((sr.getGuiScaledWidth() / 2) - (screen.xSize / 2));
		element0.setY((sr.getGuiScaledHeight() / 2) + (screen.ySize / 2) - (getTaskbarHeight()));
		element0.setWidth(getTaskbarHeight());
		element0.setHeight(getTaskbarHeight());

		IScreenElement element1 = elements.get(1);

		element1.setX((sr.getGuiScaledWidth() / 2) - (screen.xSize / 2));
		element1.setY((sr.getGuiScaledHeight() / 2) + (screen.ySize / 2) - (1.5 * getTaskbarHeight()) - 2);
		element1.setWidth(screen.ySize * 0.05);
		element1.setHeight(screen.ySize * 0.05);

		refreshDesktop();

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void onButtonPressed(Button button, int mouseButton) {
		int buttonId = button.ID;
		if (buttonId == 0) {
			start = !start;
			return;
		}
		if (buttonId == 1) {
			this.shutdown = true;
			sync("shutdown");
			return;
		}
	}

	@SuppressWarnings("unchecked")
	@OnlyIn(Dist.CLIENT)
	public void refreshDesktop() {

		shortcuts.clear();

		HardDrive drive = computerTE.getHardDrive();

		Iterator<Entry<double[], Application>> it = new HashMap<double[], Application>(drive.desktopIcons).entrySet()
				.iterator();
		while (it.hasNext()) {

			Map.Entry<double[], Application> entry = it.next();
			double[] coords = entry.getKey();
			Application app = entry.getValue();

			if (coords.length < 2 || app == null) {
				continue;
			}

			double x = coords[0];
			double y = coords[1];

			shortcuts.add(new DesktopShortcut(screen.getRelativeX(0) + x, screen.getRelativeY(0) + y, iconSize(),
					iconSize(), screen, this, app));

		}

		this.taskbarIcons.clear();

		MainWindow sr = MonitorScreen.mc.getWindow();

		ArrayList<TickingProcess> processes = (ArrayList<TickingProcess>) computerTE.getProcessesList().clone();
		for (int i = 0; i < processes.size(); i++) {
			TickingProcess process = processes.get(i);
			if (process instanceof WindowedProcess) {
				TaskbarButton tb = new TaskbarButton(
						(sr.getGuiScaledWidth() / 2) - (screen.xSize / 2) + (screen.ySize * 0.1 * (i)),
						(sr.getGuiScaledHeight() / 2) + (screen.ySize / 2) - (getTaskbarHeight()), screen.ySize * 0.1,
						getTaskbarHeight(), this.getScreen(), (WindowedProcess) process);
				tb.setProcess(this);
				tb.setZLevel(5);
				this.taskbarIcons.add(tb);
			}
		}

	}

	@OnlyIn(Dist.CLIENT)
	public void keyTyped(char typedChar, int keyCode) {
		super.keyTyped(typedChar, keyCode);

		Iterator<DesktopShortcut> it = shortcuts.iterator();
		while (it.hasNext()) {
			it.next().keyTyped(typedChar, keyCode);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);

		Iterator<DesktopShortcut> it = shortcuts.iterator();
		while (it.hasNext()) {
			it.next().mouseReleased(mouseX, mouseY, state);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void mouseClickMove(double mouseX, double mouseY, int clickedMouseButton, double deltaX,double deltaY) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, deltaX,deltaY);
		Iterator<DesktopShortcut> it = shortcuts.iterator();
		while (it.hasNext()) {
			it.next().mouseClickMove(mouseX, mouseY, clickedMouseButton, deltaX, deltaY);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
		MainWindow sr = MonitorScreen.mc.getWindow();
		if (start) {
			if (mouseX > (screen.getRelativeX(0) + (screen.xSize / 4d)) || mouseX < screen.getRelativeX(0)
					|| (mouseY < screen.getRelativeY(1 / 3d)
							|| mouseY > (screen.ySize * (2 / 3d)) - (getTaskbarHeight()) + screen.getRelativeY(1 / 3d))
							&& !Utils.isInArea(mouseX, mouseY, (sr.getGuiScaledWidth() / 2) - (screen.xSize / 2),
									(sr.getGuiScaledHeight() / 2) + (screen.ySize / 2) - (getTaskbarHeight()),
									getTaskbarHeight(), getTaskbarHeight())) {
				start = false;
			}
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);

		Iterator<DesktopShortcut> it = shortcuts.iterator();
		while (it.hasNext()) {
			it.next().mouseClicked(mouseX, mouseY, mouseButton);
		}

		Iterator<TaskbarButton> it2 = taskbarIcons.iterator();
		while (it2.hasNext()) {
			it2.next().mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	public static class IconPosUpdate {
		public Application app;
		public double x;
		public double y;

		public CompoundNBT save(CompoundNBT nbt) {
			nbt.putString("app", ApplicationRegistry.INSTANCE.getResByApp(app).toString());
			nbt.putDouble("x", x);
			nbt.putDouble("y", y);
			return nbt;
		}

		public void readFromNBT(CompoundNBT nbt) {
			String s = nbt.getString("app");
			ResourceLocation res = new ResourceLocation(s);
			app = ApplicationRegistry.INSTANCE.getByRes(res);
			x = nbt.getDouble("x");
			y = nbt.getDouble("y");
		}
	}

}
