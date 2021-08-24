package nuparu.sevendaystomine.client.gui.monitor;

import com.mojang.blaze3d.matrix.MatrixStack;

import nuparu.sevendaystomine.computer.process.TickingProcess;

public interface IScreenElement {
	public void setZLevel(int zLevel);
	
	public int getZLevel();

	public double getX();

	public double getY();

	public double getWidth();

	public double getHeight();

	public void render(MatrixStack stack, float partialTicks);
	
	public void update();
	
	public boolean isVisible();
	
	public boolean isFocused();
	
	public boolean isDisabled();
	
	public boolean isHovered(double mouseX, double mouseY);
	
	public void keyTyped(char typedChar, int keyCode);
	
	public void mouseReleased(int mouseX, int mouseY, int state);
	
	public void mouseClickMove(double mouseX, double mouseY, int clickedMouseButton, double deltaX, double deltaY);
	
	public void setScreen(MonitorScreen screen);

	public void mouseClicked(double mouseX, double mouseY, int mouseButton);
	
	public void setX(double x);
	
	public void setY(double y);
	
	public void setWidth(double width);
	
	public void setHeight(double height);
	
	public void setProcess(TickingProcess process);
	
	public void setFocus(boolean focus);

	public boolean keyPressed(int keyCode, int p_231046_2_, int p_231046_3_);

	public boolean keyReleased(int keyCode, int p_223281_2_, int p_223281_3_);
}
