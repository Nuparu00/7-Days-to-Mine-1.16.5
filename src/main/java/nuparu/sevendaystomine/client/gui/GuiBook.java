package nuparu.sevendaystomine.client.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MainWindow;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.item.guide.BookDataManager;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.util.book.BookData;
import nuparu.sevendaystomine.util.book.BookData.CraftingMatrix;
import nuparu.sevendaystomine.util.book.BookData.CraftingMatrix.GhostIngredient;
import nuparu.sevendaystomine.util.book.BookData.Image;
import nuparu.sevendaystomine.util.book.BookData.Page;
import nuparu.sevendaystomine.util.book.BookData.Stack;
import nuparu.sevendaystomine.util.book.BookData.TextBlock;

@OnlyIn(Dist.CLIENT)
public class GuiBook extends Screen implements IGuiEventListener {
	final int xSize = 256;
	final int ySize = 192;
	int pageIndex = 0;
	BookData data;

	GuiNextButton buttonNextPage;
	GuiNextButton buttonPreviousPage;

	public GuiBook(ResourceLocation res) {
		super(new StringTextComponent("gui.book"));
		this.data = BookDataManager.instance.get(res);
		System.out.println(data.getPages().size());
	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
		if(data == null || data.getPages() == null) return;
		Page page = data.getPages().get(pageIndex);
		if (page == null || page.res == null)
			return;
		minecraft.getTextureManager().bind(page.res);
		int marginHorizontal = (width - xSize) / 2;
		int marginVertical = (height - ySize) / 2;
		blit(matrix, marginHorizontal, marginVertical, 0, 0, xSize, ySize);

		super.render(matrix, mouseX, mouseY, partialTicks);

		for (TextBlock tb : page.textBlocks) {
			RenderSystem.pushMatrix();
			RenderSystem.translated(tb.x + marginHorizontal, tb.y + marginVertical, tb.z);
			RenderSystem.scaled(tb.scale, tb.scale, tb.scale);
			List<TextComponent> l = RenderUtils.splitText(
					new StringTextComponent(tb.unlocalized ? SevenDaysToMine.proxy.localize(tb.text) : tb.text),
					(int) Math.floor(tb.width / tb.scale), minecraft.font, true, true);
			for (int i = 0; i < l.size(); i++) {
				TextComponent component = l.get(i);

				StringBuilder s = new StringBuilder();
				if (tb.formatting != null) {
					for (TextFormatting tf : tb.formatting) {
						if (tf == null)
							continue;
						s.append(tf);
					}
				}
				s.append(component.getString());

				int color = tb.color;
				if (tb.hoverColor != -1
						&& Utils.isInArea(mouseX, mouseY, marginHorizontal + tb.x - (tb.centered ? tb.width / 2 : 0),
								marginVertical + tb.y, tb.width, tb.height)) {
					color = tb.hoverColor;
				}

				if (tb.centered) {
					RenderUtils.drawCenteredString(matrix, s.toString(), 0, i * (minecraft.font.lineHeight + 1) * tb.scale, color,
							tb.shadow);
				} else {
					RenderUtils.drawString(matrix, s.toString(), 0, i * (minecraft.font.lineHeight + 1) * tb.scale, color,
							tb.shadow);
				}
			}
			RenderSystem.popMatrix();
		}

		for (Image img : page.images) {
			RenderSystem.pushMatrix();
			RenderSystem.enableAlphaTest();
			RenderUtils.drawTexturedRect(matrix, img.res, img.x + marginHorizontal, img.y + marginVertical, 0, 0,
					img.width, img.height, img.width, img.height, 1, img.z);
			RenderSystem.disableAlphaTest();
			RenderSystem.popMatrix();
		}
		for (CraftingMatrix crafting : page.crafting) {

			crafting.render(matrix, minecraft, marginHorizontal + crafting.x, marginVertical + crafting.y, true,
					partialTicks);

		}
		for (Stack stack : page.stacks) {

			stack.render(minecraft, marginHorizontal + stack.x, marginVertical + stack.y, partialTicks);
		}
		for (CraftingMatrix crafting : page.crafting) {

			ItemStack itemstack = null;

			for (int i = 0; i < crafting.size(); ++i) {
				GhostIngredient ingredient = crafting.get(i);
				int j = ingredient.getX() + marginHorizontal + crafting.x;
				int k = ingredient.getY() + marginVertical + crafting.y;

				if (mouseX >= j && mouseY >= k && mouseX < j + 16 && mouseY < k + 16) {
					itemstack = ingredient.getItem();
				}
			}

			if (itemstack != null && this.minecraft.screen != null) {
					RenderSystem.pushMatrix();
				this.minecraft.screen.renderComponentTooltip(matrix,
						this.minecraft.screen.getTooltipFromItem(itemstack), mouseX, mouseY);

				RenderSystem.popMatrix();
			}

		}
		for (Stack stack : page.stacks) {

			int j = marginHorizontal + stack.x;
			int k = marginVertical + stack.y;

			if (mouseX >= j && mouseY >= k && mouseX < j + 16 && mouseY < k + 16 && this.minecraft.screen != null) {

				this.minecraft.screen.renderComponentTooltip(matrix,
						this.minecraft.screen.getTooltipFromItem(stack.stack), mouseX, mouseY);
			}
		}
	}

	@Override
	public void tick() {
		if (buttonPreviousPage != null) {
			buttonPreviousPage.active = pageIndex > 0;
		}
		if (buttonNextPage != null && data != null) {
			buttonNextPage.active = data.getPages().size() - 1 > pageIndex;
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void init() {
		super.init();
		buttons.clear();
		int offsetFromScreenLeft = (width - xSize) / 2;
		int marginVertical = (height - ySize) / 2;
		buttons.add(buttonNextPage = new GuiNextButton(offsetFromScreenLeft + xSize - 10 - 23, marginVertical + 160,
				true, this, this::actionPerformed));
		buttons.add(buttonPreviousPage = new GuiNextButton(offsetFromScreenLeft + 10, marginVertical + 160, false, this,
                this::actionPerformed));

		buttonNextPage.active = false;
		buttonPreviousPage.active = false;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		boolean def = super.mouseClicked(mouseX, mouseY, mouseButton);
		
		for(Widget button : buttons) {
			button.mouseClicked(mouseX, mouseY, mouseButton);
		}

		if(data == null || data.getPages() == null) return def;
		Page page = data.getPages().get(pageIndex);
		int marginHorizontal = (width - xSize) / 2;
		int marginVertical = (height - ySize) / 2;

		MainWindow sr = minecraft.getWindow();
		for (TextBlock tb : page.textBlocks) {
			if (tb.link >= 0 && tb.link < data.getPages().size()
					&& Utils.isInArea(mouseX, mouseY, marginHorizontal + tb.x - (tb.centered ? tb.width / 2 : 0),
							marginVertical + tb.y, tb.width, tb.height)) {
				pageIndex = tb.link;
			}
		}
		return true;
	}

	protected void actionPerformed(Button button) {

		if(data == null || data.getPages() == null) return;
		if (button == buttonNextPage) {
			if (pageIndex < data.getPages().size() - 1) {
				++pageIndex;
			}
		} else if (button == buttonPreviousPage) {
			if (pageIndex > 0) {
				--pageIndex;
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	class GuiNextButton extends Button {
		private final boolean isNextButton;
		private GuiBook gui;

		public GuiNextButton(int x, int y, boolean isNextButton, GuiBook gui, Button.IPressable press) {
			super(x, y, 23, 13, new StringTextComponent(""), press);
			this.isNextButton = isNextButton;
			this.gui = gui;
		}

		@Override
		public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {

			if(data == null || data.getPages() == null) return;
			if (visible && active) {
				boolean isButtonPressed = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				minecraft.getTextureManager().bind(gui.data.getPages().get(gui.pageIndex).res);
				int textureX = 0;
				int textureY = 192;

				if (isButtonPressed) {
					textureX += 23;
				}

				if (!isNextButton) {
					textureY += 13;
				}

				blit(stack, x, y, textureX, textureY, 23, 13);
			}
		}

	}
}
