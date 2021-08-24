package nuparu.sevendaystomine.client.gui.monitor.elements;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.client.gui.monitor.IScreenElement;
import nuparu.sevendaystomine.client.gui.monitor.MonitorScreen;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.computer.process.TickingProcess;
import nuparu.sevendaystomine.util.ColorRGBA;
import net.minecraft.util.SharedConstants;
import com.mojang.blaze3d.platform.GlStateManager;

@OnlyIn(Dist.CLIENT)
public class TextField implements IScreenElement {

	private double x;
	private double y;
	private int zLevel = 1;
	private double width;
	private double height;
	private int maxStringLength = 32;
	public int numberOfLines;

	private String contentText = "";
	private String displayText = "";
	private String defaultText = "";

	private int lineScrollOffset;
	private int maxLength = 32;
	private int cursorCounter;
	private boolean canLoseFocus = true;
	private boolean isFocused = false;
	private boolean isEnabled = true;
	private boolean isDisabled = false;
	private int cursorPosition;
	private int selectionEnd;
	public int enabledColor = 0x000000;
	public int disabledColor = 0x808080;
	private boolean enableBackgroundDrawing = true;

	private MonitorScreen screen;
	protected TickingProcess process;
	private FontRenderer font;

	public ColorRGBA backgroundColor = new ColorRGBA(1d, 1d, 1d);
	public ColorRGBA cursorColor = new ColorRGBA(0d, 0d, 1d);

	public TextField(double x, double y, double width, double height, MonitorScreen screen) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.screen = screen;

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

	public void setMaxStringLength(int length) {
		this.maxStringLength = length;

		if (this.contentText.length() > length) {
			this.contentText = this.contentText.substring(0, length);
		}
	}

	public int getMaxStringLength() {
		return this.maxStringLength;
	}

	@Override
	public void update() {
		if (this.isFocused && !isDisabled()) {
			updateCursorCounter();
		}
	}

	@Override
	public void render(MatrixStack matrix,float partialTicks) {
		if (isVisible() && isDisabled() == false) {
			matrix.pushPose();
			RenderUtils.drawColoredRect(matrix,backgroundColor, x, y, width, height, zLevel);

			int textColor = this.isFocused && isEnabled ? enabledColor : disabledColor;
			String textToDisplay = "";
			if (getContentText() != null && getContentText().isEmpty()) {
				if (!this.isFocused) {

					textToDisplay = defaultText;
				}

			} else {
				textToDisplay = getContentText();
			}
			int j = this.cursorPosition - this.lineScrollOffset;
			int k = this.selectionEnd - this.lineScrollOffset;
			String s = this.font.plainSubstrByWidth(textToDisplay.substring(Math.max(0, this.lineScrollOffset)),
					(int) Math.ceil(Math.max(0, this.getWidth())));
			if (k > s.length()) {
				k = s.length();
			}

			int textYSpace = (int) ((this.height-this.font.lineHeight)/2d)+1;
			if (!s.isEmpty()) {
				matrix.translate(x, y+textYSpace, zLevel + 1);
				RenderUtils.drawString(matrix,this.font.width(s) > getWidth() ? s.substring(j) : s, 0, 0,
						textColor);
				matrix.translate(-x, -y-textYSpace, -(zLevel + 1));
			}

			matrix.popPose();
			if (this.isFocused && this.cursorCounter / 6 % 2 == 0) {
				if (cursorPosition >= lineScrollOffset && lineScrollOffset >= 0
						&& (cursorPosition) <= textToDisplay.length()) {
					String ss = textToDisplay.substring(this.lineScrollOffset, cursorPosition);
					renderCursor(matrix,x + this.font.width(ss), y+textYSpace, 1, font.lineHeight, zLevel + 2);
				}

			}
		}
	}

	public void renderCursor(MatrixStack matrixStack, double x, double y, double width, double height, double zLevel) {
		/*matrixStack.pushPose();

		IRenderTypeBuffer.Impl impl = Minecraft.getInstance().renderBuffers().bufferSource();

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuilder();
		RenderSystem.disableTexture();
		RenderSystem.enableColorLogicOp();
		RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.vertex((double) x, (double) y + height, zLevel)
				.color((float) cursorColor.R, (float) cursorColor.G, (float) cursorColor.B, (float) cursorColor.A)
				.endVertex();
		bufferbuilder.vertex((double) x + width, (double) y + height, zLevel)
				.color((float) cursorColor.R, (float) cursorColor.G, (float) cursorColor.B, (float) cursorColor.A)
				.endVertex();
		bufferbuilder.vertex((double) x + width, (double) y, zLevel)
				.color((float) cursorColor.R, (float) cursorColor.G, (float) cursorColor.B, (float) cursorColor.A)
				.endVertex();
		bufferbuilder.vertex((double) x, (double) y, zLevel)
				.color((float) cursorColor.R, (float) cursorColor.G, (float) cursorColor.B, (float) cursorColor.A)
				.endVertex();
		bufferbuilder.end();
		impl.endBatch(RenderType.translucent());
		RenderSystem.disableColorLogicOp();
		RenderSystem.enableTexture();
		matrixStack.popPose();*/

		matrixStack.translate(0, 0, zLevel);
		AbstractGui.fill(matrixStack, (int)x, (int)y, (int)(x+width), (int)(y+height), this.cursorColor.toHex());
		matrixStack.translate(0, 0, -zLevel);
	}

	public void updateCursorCounter() {
		++this.cursorCounter;
	}

	@Override
	public void mouseClicked(double mouseX, double mouseY, int mouseButton) {

		if (!isDisabled()) {
			if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
				this.isFocused = true;
				String s = this.font.plainSubstrByWidth(this.contentText.substring(this.lineScrollOffset),
						(int) Math.ceil(this.getWidth()));
				int i = (int) (mouseX - (int) Math.ceil(this.x));
				this.cursorPosition = (this.font.plainSubstrByWidth(s, i).length() + this.lineScrollOffset);
				this.cursorCounter = 0;
			} else {
				this.isFocused = false;
			}
		}
	}

	public void writeText(String t) {
		if (contentText.length() >= maxLength) {
			return;
		}
		String s = "";
		String s1 = SharedConstants.filterText(t);
		int i = this.cursorPosition;
		int k = this.getContentText().length() - i;
		int l;
		if (i < this.getContentText().length() && i >= 0) {
			String halfA = this.getContentText().substring(0, i);
			String halfB = this.getContentText().substring(i, this.getContentText().length());
			s = halfA + s1 + halfB;
		} else {
			s = getContentText() + s1;
		}
		l = s1.length();
		this.setContentText(s);
		setCursorPosition(i + l);
	}

	public void setCursorPosition(int pos) {
		this.cursorPosition = pos;
		int i = this.contentText.length();
		this.cursorPosition = MathHelper.clamp(this.cursorPosition, 0, i);
		this.setSelectionPos(this.cursorPosition);
	}

	public void setSelectionPos(int position) {
		int i = this.contentText.length();

		if (position > i) {
			position = i;
		}

		if (position < 0) {
			position = 0;
		}

		this.selectionEnd = position;

		if (this.font != null) {
			if (this.lineScrollOffset > i) {
				this.lineScrollOffset = i;
			}

			double j = this.getWidth();
			String s = this.font.plainSubstrByWidth(this.contentText.substring(this.lineScrollOffset),
					(int) Math.ceil(j));
			int k = s.length() + this.lineScrollOffset;

			if (position == this.lineScrollOffset) {
				this.lineScrollOffset -= this.font.plainSubstrByWidth(this.contentText, (int) Math.ceil(j), true)
						.length();
			}

			if (position > k) {
				this.lineScrollOffset += position - k;
			} else if (position <= this.lineScrollOffset) {
				this.lineScrollOffset -= this.lineScrollOffset - position;
			}

			this.lineScrollOffset = MathHelper.clamp(this.lineScrollOffset, 0, i);
		}
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {
		if (this.isFocused && !isDisabled()) {
			if (SharedConstants.isAllowedChatCharacter(typedChar)) {
				this.writeText(Character.toString(typedChar));
			}
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {

	}

	@Override
	public void mouseClickMove(double mouseX, double mouseY, int clickedMouseButton, double deltaX, double deltaY) {

	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public boolean isHovered(double mouseX, double mouseY) {
		return (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height);
	}

	@Override
	public boolean isFocused() {
		return this.isFocused;
	}

	@Override
	public boolean isDisabled() {
		return this.isDisabled;
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

	public void setDefaultText(String text) {
		this.defaultText = text;
	}

	@Override
	public void setProcess(TickingProcess process) {
		this.process = process;
	}

	public String getContentText() {
		return contentText;
	}

	public void setContentText(String contentText) {
		if (contentText == null) {
			contentText = "";
		}
		this.contentText = contentText;
	}

	public String getDefaultText() {
		return this.defaultText;
	}

	public void setFocus(boolean focus) {
		this.isFocused = focus;
	}

	@Override
	public boolean keyPressed(int keyCode, int p_231046_2_, int p_231046_3_) {
		if (this.isFocused && !isDisabled()) {
			if (keyCode == 259) {
				if (getContentText() == null || getContentText().length() == 0) {
					return false;
				}
				int i = this.cursorPosition;
				int l;
				String s = "";
				if (i < this.getContentText().length()) {
					String halfA = this.getContentText().substring(0, i);
					String halfB = this.getContentText().substring(i);
					if (halfA.length() > 0) {
						s = halfA.substring(0, halfA.length() - 1) + halfB;
						l = halfA.length() - 1;
					} else {
						s = halfA + halfB;
						l = halfA.length();
					}

				} else {
					if (getContentText().length() > 0) {
						s = getContentText().substring(0, getContentText().length() - 1);
						l = s.length();
					} else {
						s = getContentText();
						l = s.length();
					}
				}

				setContentText(s);
				setCursorPosition(l);
			}
			if (keyCode == 263) {
				if (cursorPosition > 0) {
					setCursorPosition(--cursorPosition);
				}
			}
			if (keyCode == 262) {
				if (cursorPosition < getContentText().length()) {
					setCursorPosition(++cursorPosition);
				}
			}
			if (Screen.isPaste(keyCode)) {
				if (this.isEnabled) {
					this.writeText(Minecraft.getInstance().keyboardHandler.getClipboard());
				}

			}
		}
		return false;
	}

	@Override
	public boolean keyReleased(int keyCode, int p_223281_2_, int p_223281_3_) {
		return false;
	}
}
