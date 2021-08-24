package nuparu.sevendaystomine.client.gui.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.inventory.block.ContainerCombustionGenerator;
import nuparu.sevendaystomine.tileentity.TileEntityCombustionGenerator;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class GuiCombustionGenerator extends ContainerScreen<ContainerCombustionGenerator> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/generator_combustion.png");
	ContainerCombustionGenerator container;
	TileEntityCombustionGenerator tileEntityCombustionGenerator;

	public GuiCombustionGenerator(ContainerCombustionGenerator container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title);
		this.container = container;
		this.tileEntityCombustionGenerator = container.getTileEntity();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);

		int marginHorizontal = (this.width - this.imageWidth) / 2;
		int marginVertical = (this.height - this.imageHeight) / 2;

		this.font.draw(matrixStack,new TranslationTextComponent("gui.electricity.voltage",tileEntityCombustionGenerator.getPowerPerUpdate()),marginHorizontal+55,marginVertical+44,4210752);
		this.font.draw(matrixStack,new TranslationTextComponent("gui.electricity.stored",tileEntityCombustionGenerator.getVoltageStored(),tileEntityCombustionGenerator.getCapacity()),marginHorizontal+55,marginVertical+54,4210752);

		this.minecraft.getTextureManager().bind(TEXTURE);
		this.blit(matrixStack, marginHorizontal + 30 + (int)(tileEntityCombustionGenerator.getTemperature()*138), marginVertical+32, 190, 31, 6, 8);

	}

	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(TEXTURE);


		int marginHorizontal = (this.width - this.imageWidth) / 2;
		int marginVertical = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, marginHorizontal, marginVertical, 0, 0, imageWidth, imageHeight);

		// draw the fuel remaining bar for each fuel slot flame
		double burnRemaining = container.fractionOfFuelRemaining(0);
		int yOffset = (int) (Math.ceil(burnRemaining * 14));
		this.blit(matrixStack, this.getGuiLeft() + 30, this.getGuiTop() + 45 + 12 - yOffset, 176, 12 - yOffset, 14, yOffset);
	}

	@Override
	protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
		this.font.draw(matrixStack, this.title, 5, 5, Color.darkGray.getRGB());
	}

	// Returns true if the given x,y coordinates are within the given rectangle
	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY) {
		return ((mouseX >= x && mouseX <= x + xSize) && (mouseY >= y && mouseY <= y + ySize));
	}

}
