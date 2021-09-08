package nuparu.sevendaystomine.client.gui.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.inventory.block.ContainerPrinter;
import nuparu.sevendaystomine.inventory.block.ContainerScreenProjector;
import nuparu.sevendaystomine.tileentity.TileEntityPrinter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiPrinter extends ContainerScreen<ContainerPrinter> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/printer.png");
	ContainerPrinter container;
	TileEntityPrinter printer;

	public GuiPrinter(ContainerPrinter container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title);
		this.container = container;
		this.printer = container.getTileEntity();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);


		int marginHorizontal = (this.width - this.imageWidth) / 2;
		int marginVertical = (this.height - this.imageHeight) / 2;

		if (this.printer != null && isInRect(marginHorizontal+9, marginVertical+8, 16, 78, mouseX, mouseY)) {
			List<ITextComponent> tooltip = new ArrayList<ITextComponent>();
			tooltip.add(new StringTextComponent(printer.getInk() + " U"));
			this.renderComponentTooltip(matrixStack, tooltip, mouseX, mouseY);
		}
	}

	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(TEXTURE);
		int marginHorizontal = (this.width - this.imageWidth) / 2;
		int marginVertical = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, marginHorizontal, marginVertical, 0, 0, imageWidth, imageHeight);

		TextureAtlasSprite fluidTexture = minecraft.getTextureAtlas(PlayerContainer.BLOCK_ATLAS).apply(new ResourceLocation(SevenDaysToMine.MODID,"blocks/ink_still"));
		int fluidHeight = (int)Math.round(71*((double)printer.getInk()/TileEntityPrinter.CAPACITY));
		renderTiledTextureAtlas(9 + marginHorizontal, 8 + marginVertical + (71 - fluidHeight), 14, fluidHeight, 0, fluidTexture);

		this.minecraft.getTextureManager().bind(TEXTURE);

		this.blit(matrixStack,marginHorizontal+9, marginVertical+8, 176, 31, 14, 71);
	}


	@Override
	protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
		// draw the label for the top of the screen
		this.font.draw(matrixStack, this.title, this.imageWidth/2-this.font.width(this.title)/2, 5, Color.darkGray.getRGB()); /// this.font.draw

		// draw the label for the player inventory slots
		this.font.draw(matrixStack, this.inventory.getDisplayName(), /// this.font.draw
				this.imageWidth-8-this.font.width(this.inventory.getDisplayName()), 75, Color.darkGray.getRGB());
	}
	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY) {
		return ((mouseX >= x && mouseX <= x + xSize) && (mouseY >= y && mouseY <= y + ySize));
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
}
