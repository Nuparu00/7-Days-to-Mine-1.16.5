package nuparu.sevendaystomine.client.gui.monitor;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.client.gui.monitor.elements.Button;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.computer.process.WindowedProcess;
import nuparu.sevendaystomine.util.ColorRGBA;

@OnlyIn(Dist.CLIENT)
public class TaskbarButton extends Button {

	public WindowedProcess process;
	public boolean marked = false;

	public TaskbarButton(double x, double y, double width, double height, MonitorScreen screen, WindowedProcess process) {
		super(x, y, width, height, screen, "", 0);
		this.process = process;
		if (process.getApp() != null) {
			this.setText(process.getApp().getLocalizedName());
		}
		this.hovered = new ColorRGBA(1d, 1d, 1d);
		this.normal = new ColorRGBA(1d, 1d, 1d);
	}

	@Override
	public void render(MatrixStack matrix, float partialTicks) {
		if (isDisabled() == false && isVisible() && process.getApp() != null) {
			if (process.isFocused()) {
				RenderSystem.pushMatrix();
				RenderSystem.enableBlend();
				RenderSystem.enableAlphaTest();
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA_SATURATE, GL11.GL_ONE_MINUS_DST_COLOR);
				RenderUtils.drawColoredRect(matrix,hovered, x, y, width, height, zLevel);
				RenderSystem.disableAlphaTest();
				RenderSystem.disableBlend();
				RenderSystem.popMatrix();
			}
			ColorRGBA color = isHovered(screen.mouseX, screen.mouseY) ? hovered : normal;
			RenderSystem.pushMatrix();
			RenderSystem.enableBlend();
			RenderSystem.enableAlphaTest();
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			RenderUtils.drawTexturedRect(matrix,process.getApp().ICON, color, x + (width * 0.1), y + (height * 0.1), 0, 0,
					width * 0.8, height * 0.8, width * 0.8, height * 0.8, 1, zLevel + 1);
			RenderSystem.disableAlphaTest();
			RenderSystem.disableBlend();
			RenderSystem.popMatrix();

		}
	}

	@Override
	public void mouseClicked(double mouseX, double mouseY, int mouseButton) {

		if (isHovered(mouseX, mouseY) && !isDisabled()) {
			// marks the process to be focused the next tick, because in this tick all
			// processes become unfocused
			marked = true;
		}
	}

	@Override
	public void update() {
		super.update();
		if (marked) {
			process.setMinimized(false);
			process.tryToPutOnTop();
			process.setFocused(true);
			marked = false;
		}
	}

}
