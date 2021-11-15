package nuparu.sevendaystomine.client.gui.monitor.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.gui.monitor.IScreenElement;
import nuparu.sevendaystomine.client.gui.monitor.MonitorScreen;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.computer.process.TickingProcess;
import nuparu.sevendaystomine.util.ColorRGBA;

@OnlyIn(Dist.CLIENT)
public class Button implements IScreenElement {

	protected double x;
	protected double y;
	protected int zLevel;
	protected double width;
	protected double height;

	protected MonitorScreen screen;
	protected TickingProcess tickingProcess;
	protected FontRenderer font;

	private String text = "";

	protected boolean isHovered = false;
	protected boolean isDisabled = false;

	public ColorRGBA normal = new ColorRGBA(0.8, 0.8, 0.8);
	public ColorRGBA hovered = new ColorRGBA(0.9, 0.9, 0.9);
	public ColorRGBA pressed = new ColorRGBA(0.6, 0.6, 0.6);

	public ColorRGBA borderColor = new ColorRGBA(0d, 0d, 0d);

	public boolean border = true;
	public boolean background = true;

	public int textNormal = 0x333333;
	public int textHovered = 0xffff99;

	protected double fontSize = 1;
	
	public final int ID;

	public Button(double x, double y, double width, double height, MonitorScreen screen, String text, int id) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.screen = screen;
		this.setText(text);
		this.ID = id;

		this.font = Minecraft.getInstance().font;
	}

	@Override
	public int getZLevel() {
		return this.zLevel;
	}

	@Override
	public double getX() {
		return this.x;
	}

	@Override
	public double getY() {
		return this.y;
	}

	@Override
	public double getWidth() {
		return this.width;
	}

	@Override
	public double getHeight() {
		return this.height;
	}

	@Override
	public void setZLevel(int zLevel) {
		this.zLevel = zLevel;
	}

	@Override
	public void setScreen(MonitorScreen screen) {
		this.screen = screen;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public void setWidth(double width) {
		this.width = width;
	}

	@Override
	public void setHeight(double height) {
		this.height = height;
	}
	
	public int getTextColor(int mouseX, int mouseY) {
		return isHovered(mouseX, mouseY) ? textHovered : textNormal;
	}

	@Override
	public void render(MatrixStack matrix,float partialTicks) {
		if (!isDisabled() && isVisible()) {
			boolean isHovered = isHovered(tickingProcess.getScreen().mouseX,tickingProcess.getScreen().mouseY);
			ColorRGBA color = isHovered ? hovered : normal;

			matrix.pushPose();
			if (border) {
				RenderUtils.drawColoredRect(matrix,borderColor, x - 1, y - 1, width + 2, height + 2, zLevel);
			}
			if (background) {
				RenderUtils.drawColoredRect(matrix,color, x, y, width, height, zLevel + 1);
			}
			int textColor = getTextColor(tickingProcess.getScreen().mouseX,tickingProcess.getScreen().mouseY);
			matrix.translate(x + (width / 2), y, zLevel + 2);

			String localized = SevenDaysToMine.proxy.localize(getText());
			if (fontSize != 1) {
				matrix.scale((float)fontSize, (float)fontSize, 1);
				matrix.translate(0, (height * fontSize) / 2d, 0);
			}
			RenderUtils.drawCenteredString(matrix,SevenDaysToMine.proxy.localize(localized), 0, 0, textColor);
			matrix.popPose();

		}
	}

	@Override
	public void update() {
		
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public boolean isFocused() {
		return false;
	}

	@Override
	public boolean isDisabled() {
		return this.isDisabled;
	}

	@Override
	public boolean isHovered(double mouseX, double mouseY) {
		return (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height);
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {

	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {

	}

	@Override
	public void mouseClickMove(double mouseX, double mouseY, int clickedMouseButton, double deltaX, double deltaY) {

	}

	@Override
	public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (isHovered(mouseX, mouseY) && !isDisabled()) {
			tickingProcess.onButtonPressed(this, mouseButton);
		}
	}

	public void setFontSize(double fontSize) {
		this.fontSize = fontSize;
	}
	
	@Override
	public void setProcess(TickingProcess process) {
		this.tickingProcess = process;
	}
	
	public void setFocus(boolean focus) {
		
	}

	@Override
	public boolean keyPressed(int keyCode, int p_231046_2_, int p_231046_3_) {
		return false;
	}

	@Override
	public boolean keyReleased(int keyCode, int p_223281_2_, int p_223281_3_) {
		return false;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
