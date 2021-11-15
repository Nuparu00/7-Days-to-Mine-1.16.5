package nuparu.sevendaystomine.client.gui.inventory;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.inventory.block.ContainerForge;

@OnlyIn(Dist.CLIENT)
public class GuiForge extends ContainerScreen<ContainerForge> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/forge.png");
	ContainerForge container;

	public GuiForge(ContainerForge container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title);
		this.container = container;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(TEXTURE);

		// width and height are the size provided to the window when initialised after
		// creation.
		// xSize, ySize are the expected size of the texture-? usually seems to be left
		// as a default.
		// The code below is typical for vanilla containers, so I've just copied that-
		// it appears to centre the texture within
		// the available window
		// draw the background for this window
		int marginHorizontal = (this.width - this.imageWidth) / 2;
		int marginVertical = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, marginHorizontal, marginVertical, 0, 0, imageWidth, imageHeight);

		// draw the cook progress bar
		double cookProgress = container.fractionOfCookTimeComplete();
		int progressLevel = (int) (cookProgress * 24);
		this.blit(matrixStack, marginHorizontal + 119, marginVertical + 43, 176, 14, progressLevel + 1, 16);

		// draw the fuel remaining bar for each fuel slot flame
		double burnRemaining = container.fractionOfFuelRemaining(0);
		int yOffset = (int) (Math.ceil(burnRemaining * 14));
		this.blit(matrixStack, this.getGuiLeft() + 89, this.getGuiTop() + 60 - yOffset, 176, 12 - yOffset, 14, yOffset);
	}

	@Override
	protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
		// draw the label for the top of the screen
		final int LABEL_XPOS = 5;
		final int LABEL_YPOS = 5;
		this.font.draw(matrixStack, this.title, LABEL_XPOS, LABEL_YPOS, Color.darkGray.getRGB()); /// this.font.draw

		// draw the label for the player inventory slots
		this.font.draw(matrixStack, this.inventory.getDisplayName(), /// this.font.draw
				8, 70, Color.darkGray.getRGB());
	}

	// Returns true if the given x,y coordinates are within the given rectangle
	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY) {
		return ((mouseX >= x && mouseX <= x + xSize) && (mouseY >= y && mouseY <= y + ySize));
	}

}
