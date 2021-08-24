package nuparu.sevendaystomine.client.gui.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.inventory.block.ContainerGasGenerator;
import nuparu.sevendaystomine.tileentity.TileEntityGasGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiGasGenerator extends ContainerScreen<ContainerGasGenerator> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/generator_gas.png");
	ContainerGasGenerator container;
	TileEntityGasGenerator tileEntityGasGenerator;


	int fluidHeight = 0;
	public GuiGasGenerator(ContainerGasGenerator container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title);
		this.container = container;
		this.tileEntityGasGenerator = container.getTileEntity();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);

		int marginHorizontal = (this.width - this.imageWidth) / 2;
		int marginVertical = (this.height - this.imageHeight) / 2;

		this.font.draw(matrixStack,new TranslationTextComponent("gui.electricity.voltage", tileEntityGasGenerator.getPowerPerUpdate()),marginHorizontal+55,marginVertical+44,4210752);
		this.font.draw(matrixStack,new TranslationTextComponent("gui.electricity.stored", tileEntityGasGenerator.getVoltageStored(), tileEntityGasGenerator.getCapacity()),marginHorizontal+55,marginVertical+54,4210752);

		this.minecraft.getTextureManager().bind(TEXTURE);
		this.blit(matrixStack, marginHorizontal + 30 + (int)(tileEntityGasGenerator.getTemperature()*138), marginVertical+32, 190, 31, 6, 8);

		if (this.tileEntityGasGenerator != null && isInRect(marginHorizontal+9, marginVertical+8, 16, 78, mouseX, mouseY)) {
			List<ITextComponent> tooltip = new ArrayList<ITextComponent>();
			tooltip.add(new StringTextComponent(tileEntityGasGenerator.getTank().getFluidAmount() + " mB"));
			this.renderComponentTooltip(matrixStack, tooltip, mouseX, mouseY);
		}
	}

	public void renderTiledTextureAtlas(int x, int y, int width, int height, float depth, TextureAtlasSprite sprite) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuilder();
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		this.minecraft.getTextureManager().bind(PlayerContainer.BLOCK_ATLAS);

		putTiledTextureQuads(buffer, x, y, width, height, depth, sprite);

		tessellator.end();
	}

	public void putTiledTextureQuads(BufferBuilder buffer, int x, int y, int width, int height, float depth,
									 TextureAtlasSprite sprite) {
		float u1 = sprite.getU0();
		float v1 = sprite.getV0();

		// tile vertically
		do {
			int renderHeight = Math.min(sprite.getHeight(), height);
			height -= renderHeight;

			float v2 = sprite.getV((16f * renderHeight) / (float) sprite.getHeight());

			// we need to draw the quads per width too
			int x2 = x;
			int width2 = width;
			// tile horizontally
			do {
				int renderWidth = Math.min(sprite.getWidth(), width2);
				width2 -= renderWidth;

				float u2 = sprite.getU((16f * renderWidth) / (float) sprite.getWidth());

				buffer.vertex(x2, y, depth).uv(u1, v1).endVertex();
				buffer.vertex(x2, y + renderHeight, depth).uv(u1, v2).endVertex();
				buffer.vertex(x2 + renderWidth, y + renderHeight, depth).uv(u2, v2).endVertex();
				buffer.vertex(x2 + renderWidth, y, depth).uv(u2, v1).endVertex();

				x2 += renderWidth;
			} while (width2 > 0);

			y += renderHeight;
		} while (height > 0);
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

		Fluid fluid = tileEntityGasGenerator.getTank().getFluid().getFluid();
		TextureAtlasSprite fluidTexture = minecraft.getTextureAtlas(PlayerContainer.BLOCK_ATLAS).apply(fluid.getAttributes().getStillTexture());
		fluidHeight = tileEntityGasGenerator.getFluidGuiHeight(71);
		renderTiledTextureAtlas(9 + marginHorizontal, 8 + marginVertical + (71 - fluidHeight), 14, fluidHeight, 0, fluidTexture);

		this.minecraft.getTextureManager().bind(TEXTURE);

		this.blit(matrixStack,marginHorizontal+9, marginVertical+8, 176, 31, 14, 71);
	}

	@Override
	protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
		this.font.draw(matrixStack, this.title, 30, 5, Color.darkGray.getRGB());
	}

	// Returns true if the given x,y coordinates are within the given rectangle
	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY) {
		return ((mouseX >= x && mouseX <= x + xSize) && (mouseY >= y && mouseY <= y + ySize));
	}

}
