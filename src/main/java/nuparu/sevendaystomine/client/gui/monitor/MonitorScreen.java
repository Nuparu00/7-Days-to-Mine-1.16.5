package nuparu.sevendaystomine.client.gui.monitor;

import java.util.ArrayList;
import java.util.Comparator;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.client.gui.inventory.GuiMonitor;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.computer.process.TickingProcess;
import nuparu.sevendaystomine.computer.process.WindowedProcess;
import nuparu.sevendaystomine.util.ColorRGBA;

@OnlyIn(Dist.CLIENT)
public class MonitorScreen {
	private ArrayList<IScreenElement> elements = new ArrayList<IScreenElement>();

	public int mouseX = 0;
	public int mouseY = 0;

	public double xSize = 1920;
	public double ySize = 1080;

	public static final Minecraft mc = Minecraft.getInstance();
	public final GuiMonitor gui;

	public static MonitorScreen screen;

	public MonitorScreen(GuiMonitor gui) {
		this.gui = gui;
		screen = this;
		//gui.setFocused(true);
	}

	public void setScale(MainWindow sr) {
		xSize = sr.getGuiScaledWidth() / 2;
		ySize = xSize * 0.5625d;

		if (ySize > (sr.getGuiScaledHeight() / 2)) {
			ySize = sr.getGuiScaledHeight() / 2;
			xSize = ySize * (1920d / 1080d);
		}
		if (gui.shouldDisplayAnything()) {
			for (TickingProcess process : gui.getComputer().getProcessesList()) {
				process.initWindow();
			}

		}
	}

	public void render(MatrixStack matrix,float partialTicks) {
		drawFrame(matrix);
		drawDefaultScreen(matrix);
		if (gui.shouldDisplayAnything()) {
			ArrayList<WindowedProcess> wps = new ArrayList<WindowedProcess>();
			if (gui.getComputer() == null)
				return;
			for (TickingProcess process : (ArrayList<TickingProcess>) gui.getComputer().getProcessesList().clone()) {
				if (process == null)
					continue;
				if (process.getScreen() == null || process.getScreen() != this) {
					process.setScreen(this);
				}
				if (!process.clientInit) {
					process.clientInit();
				}

				if (process instanceof WindowedProcess) {
					wps.add((WindowedProcess) process);
				} else {
					process.render(matrix,partialTicks);
				}
			}

			wps.sort(Comparator.comparing(WindowedProcess::getWindowOrder));
			for (WindowedProcess wp : wps) {
				if (!wp.isMinimized()) {
					wp.render(matrix,partialTicks);
				}
			}

		}
	}

	public void update() {
		if (gui.shouldDisplayAnything() && gui.getComputer() != null) {
			ArrayList<TickingProcess> processesClone = new ArrayList<TickingProcess>(gui.getComputer().getProcessesList());
			for (TickingProcess process : processesClone) {
				if (process != null) {
					process.clientTick();
				}
			}
		}
	}

	public void drawFrame(MatrixStack matrix) {
		MainWindow sr = mc.getWindow();

		double width = (xSize * 1.05625d);
		double height = (ySize * 1.1d);

		double x = (sr.getGuiScaledWidth() / 2) - (width / 2);
		double y = (sr.getGuiScaledHeight() / 2) - (height / 2);

		RenderUtils.drawColoredRect(matrix,new ColorRGBA(0.056, 0.056, 0.056), x, y, width, height,-1);
	}

	public void drawDefaultScreen(MatrixStack matrix) {
		MainWindow sr = mc.getWindow();
		RenderUtils.drawColoredRect(matrix,new ColorRGBA(0d, 0d, 0d), (sr.getGuiScaledWidth() / 2) - (xSize / 2),
				(sr.getGuiScaledHeight() / 2) - (ySize / 2), xSize, ySize,0);
	}

	public double getOffsetLeft(int w) {
		return (xSize - (w)) / 2;
	}

	public double getOffsetTop(int h) {
		return (ySize - (h)) / 2;
	}

	public double localXToGlobal(double x) {
		MainWindow sr = mc.getWindow();
		return (sr.getGuiScaledWidth() / 2) - (xSize / 2) + x;
	}

	public double localYToGlobal(double y) {
		MainWindow sr = mc.getWindow();
		return (sr.getGuiScaledHeight() / 2) - (ySize / 2) + y;
	}

	/*
	 * Takes in range 0.0-1.0 (both inclusive)
	 */
	public double getRelativeX(double x) {
		return localXToGlobal(x * xSize);
	}

	/*
	 * Takes in range 0.0-1.0 (both inclusive)
	 */
	public double getRelativeY(double y) {
		return localYToGlobal(y * ySize);
	}

	public void keyTyped(char typedChar, int keyCode) {
		if (gui.shouldDisplayAnything()) {
			for (TickingProcess process : gui.getComputer().getProcessesList()) {
				process.keyTyped(typedChar, keyCode);
			}
		}
	}

	public void mouseReleased(int mouseX, int mouseY, int state) {
		if (gui.shouldDisplayAnything()) {
			for (TickingProcess process : gui.getComputer().getProcessesList()) {
				process.mouseReleased(mouseX, mouseY, state);
			}
		}
	}

	public void mouseClickMove(double mouseX, double mouseY, int clickedMouseButton, double deltaX, double deltaY) {
		if (gui.shouldDisplayAnything()) {
			for (TickingProcess process : gui.getComputer().getProcessesList()) {
				process.mouseClickMove(mouseX, mouseY, clickedMouseButton, deltaX,deltaY);
			}
		}
	}

	public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
		MainWindow sr = mc.getWindow();

		double width = (xSize * 1.05625d);
		double height = (ySize * 1.1d);

		double x = (sr.getGuiScaledWidth() / 2) - (width / 2);
		double y = (sr.getGuiScaledHeight() / 2) - (height / 2);

		//gui.setFocused(Utils.isInArea(mouseX, mouseY, x, y, width, height));

		if (gui.shouldDisplayAnything()) {
			for (TickingProcess process : gui.getComputer().getProcessesList()) {
				process.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}

	}

	public boolean keyPressed(int keyCode, int p_231046_2_, int p_231046_3_) {
		if (gui.shouldDisplayAnything()) {
			for (TickingProcess process : gui.getComputer().getProcessesList()) {
				process.keyPressed(keyCode,p_231046_2_,p_231046_3_);
			}
		}
		return false;
	}

	public boolean keyReleased(int keyCode, int p_223281_2_, int p_223281_3_){
		if (gui.shouldDisplayAnything()) {
			for (TickingProcess process : gui.getComputer().getProcessesList()) {
				process.keyReleased(keyCode,p_223281_2_,p_223281_3_);
			}
		}

		return false;
	}
}