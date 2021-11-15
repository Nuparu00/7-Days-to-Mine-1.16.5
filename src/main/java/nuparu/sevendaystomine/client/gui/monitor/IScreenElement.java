package nuparu.sevendaystomine.client.gui.monitor;

import com.mojang.blaze3d.matrix.MatrixStack;

import nuparu.sevendaystomine.computer.process.TickingProcess;

public interface IScreenElement {
	void setZLevel(int zLevel);
	
	int getZLevel();

	double getX();

	double getY();

	double getWidth();

	double getHeight();

	void render(MatrixStack stack, float partialTicks);
	
	void update();
	
	boolean isVisible();
	
	boolean isFocused();
	
	boolean isDisabled();
	
	boolean isHovered(double mouseX, double mouseY);
	
	void keyTyped(char typedChar, int keyCode);
	
	void mouseReleased(int mouseX, int mouseY, int state);
	
	void mouseClickMove(double mouseX, double mouseY, int clickedMouseButton, double deltaX, double deltaY);
	
	void setScreen(MonitorScreen screen);

	void mouseClicked(double mouseX, double mouseY, int mouseButton);
	
	void setX(double x);
	
	void setY(double y);
	
	void setWidth(double width);
	
	void setHeight(double height);
	
	void setProcess(TickingProcess process);
	
	void setFocus(boolean focus);

	boolean keyPressed(int keyCode, int p_231046_2_, int p_231046_3_);

	boolean keyReleased(int keyCode, int p_223281_2_, int p_223281_3_);
}
