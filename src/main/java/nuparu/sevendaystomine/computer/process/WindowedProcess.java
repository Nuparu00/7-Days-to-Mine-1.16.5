package nuparu.sevendaystomine.computer.process;

import java.util.ArrayList;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.client.gui.monitor.IDraggable;
import nuparu.sevendaystomine.client.gui.monitor.IScreenElement;
import nuparu.sevendaystomine.client.gui.monitor.MonitorScreen;
import nuparu.sevendaystomine.client.gui.monitor.elements.Button;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.computer.application.Application;
import nuparu.sevendaystomine.computer.application.ApplicationRegistry;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.KillProcessMessage;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;
import nuparu.sevendaystomine.tileentity.TileEntityComputer.EnumSystem;
import nuparu.sevendaystomine.util.ColorRGBA;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Utils;

public abstract class WindowedProcess extends TickingProcess implements IDraggable {

	protected double x;
	protected double y;

	protected double old_x;
	protected double old_y;

	protected double offsetX;
	protected double offsetY;

	protected double width;
	protected double height;

	protected int zLevel;
	// 0=first;1=second;...
	private int windowOrder = -1;
	protected Application application;

	protected boolean isFocused = false;
	protected boolean isDragged = false;

	protected boolean maximized;
	protected boolean minimized;
	protected double old_height;
	protected double old_width;

	public static double title_bar_height = 0.05;

	public Button close;
	public Button maximize;
	public Button minimize;

	private int maxRelativeZ = 0;

	public WindowedProcess() {
		this(0, 0, 0, 0);
	}

	public WindowedProcess(double x, double y, double width, double height) {
		super();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientInit() {
		super.clientInit();
		
		if (x == -1) {
			x = MonitorScreen.screen.getRelativeX(0.5) - 50;
		}
		if (y == -1) {
			y = MonitorScreen.screen.localYToGlobal(0);
		}
		
		elements.clear();
		close = new Button(x + width - MonitorScreen.screen.ySize * title_bar_height, y,
				MonitorScreen.screen.ySize * title_bar_height, MonitorScreen.screen.ySize * title_bar_height, MonitorScreen.screen, "X", 0);
		close.textNormal = 0xffffff;
		close.normal = new ColorRGBA(0.8, 0.2, 0.1);
		close.setFontSize(0.7);
		close.setProcess(this);
		close.setZLevel(zLevel);
		close.border = false;
		elements.add(close);

		maximize = new Button(x + width - 2 * (MonitorScreen.screen.ySize * title_bar_height), y,
				MonitorScreen.screen.ySize * title_bar_height, MonitorScreen.screen.ySize * title_bar_height, MonitorScreen.screen,
				Character.toString((char) 0x25A1), -1);
		maximize.textNormal = 0xffffff;
		maximize.normal = new ColorRGBA(0.8, 0.2, 0.1);
		maximize.setFontSize(0.7);
		maximize.setProcess(this);
		maximize.setZLevel(zLevel);
		maximize.border = false;
		elements.add(maximize);

		minimize = new Button(x + width - 3 * (MonitorScreen.screen.ySize * title_bar_height), y,
				MonitorScreen.screen.ySize * title_bar_height, MonitorScreen.screen.ySize * title_bar_height, MonitorScreen.screen, "-", -2);
		minimize.textNormal = 0xffffff;
		minimize.normal = new ColorRGBA(0.8, 0.2, 0.1);
		minimize.setFontSize(0.7);
		minimize.setProcess(this);
		minimize.setZLevel(zLevel);
		minimize.border = false;
		elements.add(minimize);
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		super.save(nbt);

		nbt.putDouble("x", x);
		nbt.putDouble("y", y);
		nbt.putDouble("old_x", old_x);
		nbt.putDouble("old_y", old_y);
		nbt.putDouble("offsetX", offsetX);
		nbt.putDouble("offsetY", offsetY);
		nbt.putDouble("width", width);
		nbt.putDouble("height", height);
		nbt.putInt("zLevel", zLevel);
		nbt.putInt("windowOrder", windowOrder);
		nbt.putString("application", ApplicationRegistry.INSTANCE.getResByApp(application).toString());
		nbt.putBoolean("isFocused", isFocused);
		nbt.putBoolean("isDragged", isDragged);
		nbt.putBoolean("maximized", maximized);
		nbt.putBoolean("minimized", minimized);
		nbt.putDouble("old_width", old_width);
		nbt.putDouble("old_height", old_height);

		return nbt;
	}

	@Override
	public void readFromNBT(CompoundNBT nbt) {
		super.readFromNBT(nbt);
		if (nbt.contains("x")) {
			this.x = nbt.getDouble("x");
		}
		if (nbt.contains("y")) {
			this.y = nbt.getDouble("y");
		}
		if (nbt.contains("width")) {
			this.width = nbt.getDouble("width");
		}
		if (nbt.contains("height")) {
			this.height = nbt.getDouble("height");
		}
		if (nbt.contains("zLevel")) {
			this.zLevel = nbt.getInt("zLevel");
		}
		if (nbt.contains("windowOrder")) {
			this.windowOrder = nbt.getInt("windowOrder");
		}
		if (nbt.contains("old_x")) {
			this.old_x = nbt.getDouble("old_x");
		}
		if (nbt.contains("old_y")) {
			this.old_y = nbt.getDouble("old_y");
		}
		if (nbt.contains("offsetX")) {
			this.offsetX = nbt.getDouble("offsetX");
		}
		if (nbt.contains("offsetY")) {
			this.offsetY = nbt.getDouble("offsetY");
		}
		if (nbt.contains("isDragged")) {
			this.isDragged = nbt.getBoolean("isDragged");
		}
		if (nbt.contains("isFocused")) {
			this.isFocused = nbt.getBoolean("isFocused");
		}
		if (nbt.contains("maximized")) {
			this.maximized = nbt.getBoolean("maximized");
		}
		if (nbt.contains("minimized")) {
			this.minimized = nbt.getBoolean("minimized");
		}
		if (nbt.contains("old_width")) {
			this.old_width = nbt.getDouble("old_width");
		}
		if (nbt.contains("old_height")) {
			this.old_height = nbt.getDouble("old_height");
		}

		if (nbt.contains("application")) {
			String s = nbt.getString("application");
			ResourceLocation res = new ResourceLocation(s);
			application = ApplicationRegistry.INSTANCE.getByRes(res);
		}

	}

	public String getTitle() {
		return this.application.getLocalizedName();
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public double getZLevel() {
		return zLevel;
	}

	public Application getApp() {
		return application;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void render(MatrixStack matrix,float partialTicks) {
		super.render(matrix, partialTicks);
		this.maxRelativeZ = 0;
	}

	@OnlyIn(Dist.CLIENT)
	public void drawWindow(MatrixStack matrix, String title, ColorRGBA bgrColor, ColorRGBA titleBarColor) {
		matrix.pushPose();
		matrix.translate(0, 0, zLevel);
		RenderUtils.drawColoredRect(matrix,titleBarColor, x, y, width, (MonitorScreen.screen.ySize * title_bar_height), 0);
		RenderUtils.drawColoredRect(matrix,bgrColor, x, y + (MonitorScreen.screen.ySize * title_bar_height), width,
				height - (MonitorScreen.screen.ySize * title_bar_height), 0);
		matrix.translate(x+1,y+1,0);
		matrix.scale(0.66f,0.66f,0.66f);
		RenderUtils.drawString(matrix,title, 0, 0, 0x000000);
		matrix.translate(0, 0, -zLevel);
		matrix.popPose();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void initWindow() {

		if (maximized && (this.width != MonitorScreen.screen.xSize || this.height != MonitorScreen.screen.ySize * 0.9
				|| this.x != MonitorScreen.screen.localXToGlobal(0) || this.y != MonitorScreen.screen.localYToGlobal(0))) {
			this.width = MonitorScreen.screen.xSize;
			this.height = MonitorScreen.screen.ySize * 0.9;
			this.x = MonitorScreen.screen.localXToGlobal(0);
			this.y = MonitorScreen.screen.localYToGlobal(0);
			
			if(this.computerTE.getSystem() == EnumSystem.MAC && y < MonitorScreen.screen.localYToGlobal(0)+screen.ySize*0.05) {
				y = MonitorScreen.screen.localYToGlobal(0)+screen.ySize*0.05;
			}
			
			sync("x", "y", "width", "height");
		}
		if (close == null)
			return;
		close.setX(x + width - MonitorScreen.screen.ySize * title_bar_height);
		close.setY(y);
		close.setWidth(MonitorScreen.screen.ySize * title_bar_height);
		close.setHeight(MonitorScreen.screen.ySize * title_bar_height);

		maximize.setX(x + width - 2 * (MonitorScreen.screen.ySize * title_bar_height));
		maximize.setY(y);
		maximize.setWidth(MonitorScreen.screen.ySize * title_bar_height);
		maximize.setHeight(MonitorScreen.screen.ySize * title_bar_height);

		minimize.setX(x + width - 3 * (MonitorScreen.screen.ySize * title_bar_height));
		minimize.setY(y);
		minimize.setWidth(MonitorScreen.screen.ySize * title_bar_height);
		minimize.setHeight(MonitorScreen.screen.ySize * title_bar_height);

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void onButtonPressed(Button button, int mouseButton) {

		if (isMinimized())
			return;
		int buttonId = button.ID;
		if (buttonId == 0) {
			PacketManager.killProcess.sendToServer(new KillProcessMessage(computerTE.getPos(), this.getId()));
		} else if (buttonId == -1) {
			if (!maximized) {
				this.old_height = height;
				this.old_width = width;
				this.width = MonitorScreen.screen.xSize;
				this.height = MonitorScreen.screen.ySize * 0.9;
			} else {
				this.width = old_width;
				this.height = old_height;
			}
			this.x = MonitorScreen.screen.localXToGlobal(0);
			this.y = MonitorScreen.screen.localYToGlobal(0);
			this.maximized = !this.maximized;
			initWindow();
			sync("x", "y", "width", "height", "old_height", "old_width", "maximized", "minimized");

		} else if (buttonId == -2) {
			setMinimized(true);
			sync("x", "y", "width", "height", "old_height", "old_width", "maximized", "minimized");

		}
	}

	public boolean isHovered(double mouseX, double mouseY) {
		return (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void mouseReleased(int mouseX, int mouseY, int state) {
		if (isMinimized())
			return;
		if (isDragged == true) {
			isDragged = false;
			if (mouseX < MonitorScreen.screen.getRelativeX(0) - 20 || mouseY < MonitorScreen.screen.getRelativeY(0) - 20
					|| mouseX > MonitorScreen.screen.getRelativeX(0) + MonitorScreen.screen.xSize + 20
					|| mouseY > MonitorScreen.screen.getRelativeY(0) + MonitorScreen.screen.ySize + 20) {
				x = old_x;
				y = old_y;
			}
			setOffsetX(0);
			setOffsetY(0);
			sync();
			onDragReleased();
			initWindow();
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void mouseClickMove(double mouseX, double mouseY, int clickedMouseButton, double deltaX, double deltaY) {
		if (isMinimized() || maximized)
			return;
		if (clickedMouseButton == 0 && isFocused() && Utils.isInArea(mouseX, mouseY, x, y,
				width - 3 * (MonitorScreen.screen.ySize * title_bar_height), MonitorScreen.screen.ySize * title_bar_height)) {
			if (isDragged == false) {
				isDragged = true;
			}
			x = MathUtils.clamp(mouseX - getOffsetX(), MonitorScreen.screen.getRelativeX(0),
					MonitorScreen.screen.getRelativeX(1) - width);
			y = MathUtils.clamp(mouseY - getOffsetY(), MonitorScreen.screen.getRelativeY(0),
					MonitorScreen.screen.getRelativeY(1) - height - (MonitorScreen.screen.ySize * 0.1));
			initWindow();
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
		boolean focusPrev = isFocused;
		
		if (isNotHidden(mouseX, mouseY)) {

			if (Utils.isInArea(mouseX, mouseY, x, y, width - 3 * (MonitorScreen.screen.ySize * title_bar_height),
					MonitorScreen.screen.ySize * title_bar_height)) {
				if (mouseButton == 0) {
					isFocused = true;
					old_x = x;
					old_y = y;
					setOffsetX(mouseX - x);
					setOffsetY(mouseY - y);

				}
			} else if (Utils.isInArea(mouseX, mouseY, x, y + MonitorScreen.screen.ySize * title_bar_height,
					MonitorScreen.screen.ySize * width, MonitorScreen.screen.ySize * height)) {
				isFocused = true;
				super.mouseClicked(mouseX, mouseY, mouseButton);
			} else {
				for (IScreenElement element : elements) {
					element.setFocus(false);
				}
				isFocused = false;
				super.mouseClicked(mouseX, mouseY, mouseButton);
			}
			
			tryToPutOnTop();
		} else {
			for (IScreenElement element : elements) {
				element.setFocus(false);
			}
			isFocused = false;
		}
		if (focusPrev != isFocused) {
			sync();
		}
	}

	@Override
	public boolean isDragged() {
		return isDragged;
	}

	@Override
	public void setOffsetX(double offset) {
		this.offsetX = offset;
	}

	@Override
	public void setOffsetY(double offset) {
		this.offsetY = offset;
	}

	@Override
	public double getOffsetX() {
		return this.offsetX;
	}

	@Override
	public double getOffsetY() {
		return this.offsetY;
	}

	public boolean isFocused() {
		return isFocused;
	}

	public void setFocused(boolean state) {
		boolean focusedPrev = isFocused;
		this.isFocused = state;
		if (focusedPrev != isFocused) {
			sync("isFocused");
		}
	}

	public boolean isMinimized() {
		return minimized;
	}

	// Checks if the window is really visible at given coords
	public boolean isNotHidden(double mouseX, double mouseY) {
		if (!isMinimized() && isHovered(mouseX, mouseY)) {
			@SuppressWarnings("unchecked")
			ArrayList<TickingProcess> processes = (ArrayList<TickingProcess>) computerTE.getProcessesList().clone();
			for (int i = 0; i < processes.size(); i++) {
				TickingProcess tp = processes.get(i);
				if (tp instanceof WindowedProcess) {
					WindowedProcess wp = (WindowedProcess) tp;
					if (wp.getWindowOrder() <= this.getWindowOrder()) {
						continue;
					} else {
						if (Utils.isInArea(mouseX, mouseY, wp.x, wp.y, wp.width, wp.height)) {
							return false;
						}
					}
				}
			}
			return true;
		}
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	public void tryToPutOnTop() {

		int newOrder = this.getWindowOrder();

		@SuppressWarnings("unchecked")
		ArrayList<TickingProcess> processes = (ArrayList<TickingProcess>) computerTE.getProcessesList().clone();
		for (int i = 0; i < processes.size(); i++) {
			TickingProcess tp = processes.get(i);
			if (tp instanceof WindowedProcess) {
				WindowedProcess wp = (WindowedProcess) tp;
				if (wp.getWindowOrder() > newOrder) {
					newOrder = wp.getWindowOrder();
					wp.setWindowOrder(wp.getWindowOrder() - 1);
					continue;
				}
			}
		}
		this.setWindowOrder(newOrder);
	}

	@OnlyIn(Dist.CLIENT)
	public void onDragReleased() {

	}

	public int getWindowOrder() {
		return windowOrder;
	}

	@OnlyIn(Dist.CLIENT)
	public void setWindowOrder(int newWindowOrder) {

		boolean flag = false;
		if (this.windowOrder != newWindowOrder) {
			flag = true;
		}
		this.windowOrder = newWindowOrder;
		this.zLevel = (1 + windowOrder) * 3;
		if (flag && application != null && computerTE != null) {
			sync("windowOrder", "zLevel");
		}

	}

	public static boolean isDesktopVisible(int x, int y, TileEntityComputer te) {
		@SuppressWarnings("unchecked")
		ArrayList<TickingProcess> processes = (ArrayList<TickingProcess>) te.getProcessesList().clone();
		for (int i = 0; i < processes.size(); i++) {
			TickingProcess tp = processes.get(i);
			if (tp instanceof WindowedProcess) {
				WindowedProcess wp = (WindowedProcess) tp;
				if (!wp.isMinimized() && Utils.isInArea(x, y, wp.x, wp.y, wp.width, wp.height)) {
					return false;
				}

			}
		}
		return true;
	}

	@OnlyIn(Dist.CLIENT)
	public void clientTick() {
		super.clientTick();
	}

	public void setMinimized(boolean state) {
		minimized = state;
		if (minimized) {
			for (IScreenElement element : elements) {
				element.setFocus(false);
			}
			isFocused = false;
		}
	}

	/*
	 * TO-DO: Have to solve issue with some elemnts from hidden windows being
	 * renderer on top of other windows
	 */
	public int offsetRelativeZ(int offset) {
		int i = zLevel + offset;
		if (i > maxRelativeZ) {
			maxRelativeZ = i;
		}
		return i;
	}
	
	public boolean isMouseInside() {
		return Utils.isInArea(screen.mouseX, screen.mouseY, x, y + (MonitorScreen.screen.ySize * title_bar_height), width, height);
	}

}