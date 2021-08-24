package nuparu.sevendaystomine.client.gui.monitor.elements;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.gui.monitor.MonitorScreen;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.util.ColorRGBA;

@OnlyIn(Dist.CLIENT)
public class TexturedButton extends Button {

	protected ResourceLocation res;

	public TexturedButton(double x, double y, double width, double height, MonitorScreen screen, String text,
			ResourceLocation res, int id) {
		super(x, y, width, height, screen, text, id);
		this.res = res;
	}

	@Override
	public void render(MatrixStack matrix,float partialTicks) {
		if (isDisabled() == false && isVisible()) {
			ColorRGBA color = isHovered(tickingProcess.getScreen().mouseX,tickingProcess.getScreen().mouseY) ? hovered : normal;
			matrix.pushPose();
			RenderUtils.drawTexturedRect(matrix,res, color, x, y, 0, 0, width, height, width,height, 1, zLevel + 1);

			int textColor = textNormal;
			if (isHovered(screen.mouseX, screen.mouseY)) {
				textColor = textHovered;
			}
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
}