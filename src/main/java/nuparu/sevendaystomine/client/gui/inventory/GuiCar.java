package nuparu.sevendaystomine.client.gui.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.inventory.entity.ContainerCar;
import nuparu.sevendaystomine.item.EnumQuality;
import nuparu.sevendaystomine.util.PlayerUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiCar extends ContainerScreen<ContainerCar> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/car.png");
	ContainerCar container;
	//boolean chestPrev = false;

	public GuiCar(ContainerCar container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title);
		this.container = container;
		imageWidth = 246;
		//updateSize();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);

		int mid = (int) ((176/2f)-this.font.width(this.container.carEntity.getName())/2);
		int titleWidth = this.font.width(this.container.carEntity.getName());

		int marginHorizontal = (this.width - this.imageWidth) / 2;
		int marginVertical = (this.height - this.imageHeight) / 2;

		if (isInRect(marginHorizontal+(int) (mid), marginVertical+6, titleWidth, 10, mouseX, mouseY)) {
			List<ITextComponent> tooltip = new ArrayList<ITextComponent>();

			int quality = container.carEntity.getCalculatedQuality();
			EnumQuality tier = PlayerUtils.getQualityTierFromInt(quality);
			IFormattableTextComponent qualityValue = new TranslationTextComponent("stat.quality", quality);
			tooltip.add(qualityValue);

			IFormattableTextComponent health = new TranslationTextComponent("stat.health", this.container.carEntity.getHealth(),this.container.carEntity.getMaxHealth());
			tooltip.add(health);

			IFormattableTextComponent fuel = new TranslationTextComponent("stat.fuel", this.container.carEntity.getFuel());
			tooltip.add(fuel);

			IFormattableTextComponent acceleration = new TranslationTextComponent("stat.acceleration", this.container.carEntity.getAcceleration());
			tooltip.add(acceleration);
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

		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		InventoryScreen.renderEntityInInventory(i + ((this.imageWidth-66) / 2), j + 62, 20, (float) (i + 51) - x, (float) (j + 75 - 50) - y,
				this.container.carEntity);
	}

	@Override
	protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
		// draw the label for the top of the screen
		this.font.draw(matrixStack, this.container.carEntity.getName(), (176/2f)-this.font.width(this.container.carEntity.getName())/2, 6, Color.darkGray.getRGB()); /// this.font.draw

		// draw the label for the player inventory slots
		this.font.draw(matrixStack, this.inventory.getDisplayName(), /// this.font.draw
				8, 75, Color.darkGray.getRGB());
	}

	@Override
	public void tick() {
		super.tick();
		if (!container.carEntity.isAlive()) {
			Minecraft.getInstance().setScreen(null);
		}

		//updateSize();
	}

	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY) {
		return ((mouseX >= x && mouseX <= x + xSize) && (mouseY >= y && mouseY <= y + ySize));
	}

}
