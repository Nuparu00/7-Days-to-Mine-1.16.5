package nuparu.sevendaystomine.client.gui.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.inventory.block.ContainerSeparator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiSeparator extends ContainerScreen<ContainerSeparator> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/separator.png");
	ContainerSeparator container;

	public GuiSeparator(ContainerSeparator container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title);
		this.container = container;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
		int marginHorizontal = (this.width - this.imageWidth) / 2;
		int marginVertical = (this.height - this.imageHeight) / 2;
		if (container.separator != null && isInRect(marginHorizontal+81, marginVertical+59, 14, 14, mouseX, mouseY)) {
			List<ITextComponent> tooltip = new ArrayList<ITextComponent>();
			tooltip.add(new StringTextComponent(container.separator.getVoltageStored() + "J"));
			this.renderComponentTooltip(matrixStack,tooltip,mouseX,mouseY);
		}
	}

	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(TEXTURE);

		int marginHorizontal = (this.width - this.imageWidth) / 2;
		int marginVertical = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, marginHorizontal, marginVertical, 0, 0, imageWidth, imageHeight);

		// draw the cook progress bar
		//
		double cookProgress = container.fractionOfCookTimeComplete();

		int progressLevel = (int) (cookProgress * 24);

		//x,y,U-start,V-start
		this.blit(matrixStack, marginHorizontal + 107, marginVertical + 43, 176, 14, progressLevel + 1, 16);
		this.blit(matrixStack, marginHorizontal + 45+25-progressLevel-2, marginVertical + 43, 176+25-progressLevel-1, 31, progressLevel+1, 16);

		int k = this.getBurnLeftScaled(14);
		this.blit(matrixStack, 82, 73 - k, 176, 14 - k, 14, k);

	}

	@Override
	protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {

		this.font.draw(matrixStack, this.title, 8, 5, Color.darkGray.getRGB()); /// this.font.draw

		// draw the label for the player inventory slots
		this.font.draw(matrixStack, this.inventory.getDisplayName(), /// this.font.draw
				8, 70, Color.darkGray.getRGB());
	}

	// Returns true if the given x,y coordinates are within the given rectangle
	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY) {
		return ((mouseX >= x && mouseX <= x + xSize) && (mouseY >= y && mouseY <= y + ySize));
	}

	private int getBurnLeftScaled(int pixels) {
		if(container.separator == null) return 0;
		return (int) (container.separator.getVoltageStored() * pixels / container.separator.getCapacity());
	}

}
