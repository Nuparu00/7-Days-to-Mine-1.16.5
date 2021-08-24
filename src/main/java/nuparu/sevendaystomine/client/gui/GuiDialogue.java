package nuparu.sevendaystomine.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.entity.human.EntityHuman;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.DialogueSelectionMessage;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.util.dialogue.Dialogue;
import nuparu.sevendaystomine.util.dialogue.DialogueTree;
import nuparu.sevendaystomine.util.dialogue.Dialogues;

@OnlyIn(Dist.CLIENT)
public class GuiDialogue extends Screen implements IGuiEventListener{

	public EntityHuman entity;

	public static int STYLING_COLOR = 0xff00ff00;

	private float currentScroll;

	public GuiDialogue(EntityHuman entity) {
		super(new StringTextComponent("screen.dialogue"));
		this.entity = entity;
	}

	@Override
	public void init() {
		this.buttons.clear();
		STYLING_COLOR = 0xffd43131;
		Dialogues dialogues = entity.getDialogues();
		String currentTree = entity.getCurrentDialogue();
		DialogueTree tree = dialogues.getTreeByName(currentTree);
		if (tree == null) {		
			return;
		}
		ArrayList<Dialogue> options = tree.getOptions();
		for (int i = 0; i < options.size(); i++) {
			Dialogue dialogue = options.get(i);
			addDialogueButton(dialogue.getUnloclaizedName());
		}
	}

	/*
	 * DIALOGUE BUTTONS HAVE TO BE ADDED BEFORE ANY OTHER BUTTONS!!
	 */
	public void addDialogueButton(String text) {
		int rectLeft = (this.width / 4);
		int rectRight = (this.width / 4) * 3;
		int rectTop = Math.round(this.height / 2f);

		int x = rectLeft + 5;
		int y = 0;
		if (buttons.size() != 0) {
			Widget button = this.buttons.get(buttons.size() - 1);
			y = button.y + button.getHeight();
		} else {
			y = rectTop + (5 * (buttons.size() + 1)) + 5;
		}

		addButton(new GuiDialogueOption(x, y, (rectRight - rectLeft) - 5, text, this, (button) -> {
			actionPerformed(button);
        }));
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {

		int rectLeft = Math.round(this.width / 4f);
		int rectRight = Math.round(this.width / 4f * 3f);
		int rectTop = Math.round(this.height / 2f);
		int rectBottom = Math.round(this.height - 40 - (this.height / 10f));

		RenderSystem.pushMatrix();
		RenderSystem.translated(rectLeft, rectTop - 15, 0);
		RenderSystem.scaled(1.5, 1.5, 1.5);
		AbstractGui.drawString(matrix,font, entity.getDisplayName().getString(), 0, 0, STYLING_COLOR);
		RenderSystem.popMatrix();

		this.fillGradient(matrix,rectLeft, rectTop, rectRight, rectBottom, -1072689136, -804253680);
		fill(matrix,rectLeft, rectTop - 2, rectRight, rectTop, STYLING_COLOR);
		fill(matrix,rectLeft, rectBottom, rectRight, rectBottom + 2, STYLING_COLOR);

		matrix.pushPose();

		double ratio = (double) minecraft.getWindow().getWidth() / (double) width;

		// CUTS ALL TEXT OUTSIDE OF THE RECT
		RenderSystem.enableScissor((int) Math.round(rectLeft * ratio), (int) Math.round((39 + (this.height / 10f)) * ratio),
				(int) Math.round((rectRight - rectLeft) * ratio), (int) Math.round((getContentRectHeight()) * ratio));
		// RenderUtils.drawColoredRect(new ColorRGBA(1,1,1), 0, 0, 2000, 2000, 0);
		super.render(matrix,mouseX, mouseY, partialTicks);
		RenderSystem.disableScissor();
		matrix.popPose();

	}

	@Override
	public boolean mouseScrolled(double p_231043_1_, double p_231043_3_, double i) {

		if (i != 0 && this.needsScrollBars()) {
			int j = Math.round((getContentRectHeight()) / 10);

			int h = getContentHeight();

			if (i > 0) {
				i = 1;
			}

			if (i < 0) {
				i = -1;
			}

			this.currentScroll = (float) ((double) this.currentScroll - (double) i / (double) j);
			this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);

			int deltaY = (int) Math.round(currentScroll / 2 * h);
			for (Widget button : buttons) {
				if (button instanceof GuiDialogueOption) {
					((GuiDialogueOption) button).setDeltaY(deltaY);
				}
			}
			return true;
		}
		return false;

	}

	public boolean needsScrollBars() {
		return buttons.size() > 0 && getContentHeight() > getContentRectHeight();
	}

	public int getContentHeight() {
		Widget last = buttons.get(buttons.size() - 1);
		return last.y + last.getHeight() - buttons.get(0).y;
	}

	public int getContentRectHeight() {
		return Math.round((this.height - 40 - (this.height / 10f)) - (this.height / 2f));
	}

	protected void actionPerformed(Button button) {
		System.out.println("ACT");
		if (button instanceof GuiDialogueOption) {
			GuiDialogueOption option = (GuiDialogueOption) button;
			PacketManager.sendToServer(PacketManager.dialogueSelection,new DialogueSelectionMessage(option.dialogueName, entity));
		}
	}

	public class GuiDialogueOption extends Button {

		protected List<String> lines = new ArrayList<String>();
		private final GuiDialogue gui;

		private int rectLeft;
		private int rectRight;
		private int rectTop;
		private int rectBottom;

		private final int defaultX;
		private final int defaultY;

		protected int deltaX = 0;
		protected int deltaY = 0;

		public final String dialogueName;

		public GuiDialogueOption(int x, int y, int widthIn, String dialogueName, GuiDialogue gui, Button.IPressable press) {
			super(x, y, widthIn, 10, new StringTextComponent(dialogueName),press);
			this.gui = gui;
			this.defaultX = x;
			this.defaultY = y;

			this.dialogueName = dialogueName;

			String text = SevenDaysToMine.proxy.localize(dialogueName + ".text");

			// SPLITS THE TEXT TO FIT THE RECT WITHOUT BREAKING WORDS
			if (minecraft.font.width(text) <= widthIn) {
				lines.add(text);
			} else {
				while (minecraft.font.width(text) > widthIn) {
					for (int i = 2; i < text.length(); i++) {
						String sub = text.substring(0, i);
						if (minecraft.font.width(sub) > widthIn) {
							boolean isPreviousEmpty = text.charAt(i - 2) == ' ';
							boolean isThisEmpty = text.charAt(i - 1) == ' ';
							boolean isNextEmpty = text.length() - 1 >= i + 1 ? text.charAt(i) == ' ' : true;

							if (!isThisEmpty && !isPreviousEmpty) {
								for (int j = i - 3; j > 2; j--) {
									boolean b = text.charAt(j) == ' ';
									if (b) {
										i = j;
										break;
									}
								}
							}

							lines.add(text.substring(0, i).trim());
							text = text.substring(i);

							break;
						}
					}
				}
				if (minecraft.font.width(text) > 0) {
					lines.add(text.trim());
				}
			}

			this.height = 10 + lines.size() * minecraft.font.lineHeight;

			rectLeft = Math.round(gui.width / 4f);
			rectRight = Math.round(gui.width / 4f * 3f);
			rectTop = Math.round(gui.height / 2f);
			rectBottom = Math.round(gui.height - 40 - (gui.height / 10f));

		}

		@Override
		public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
			this.y = defaultY - deltaY;

			if (this.visible) {
				FontRenderer fontrenderer = minecraft.font;

				int rectLeft = Math.round(gui.width / 4f);
				int rectRight = Math.round(gui.width / 4f * 3f);
				int rectTop = Math.round(gui.height / 2f);

				this.isHovered = (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
						&& mouseY < this.y + this.height)
						&& Utils.isInArea(mouseX, mouseY, rectLeft, rectTop, rectRight - rectLeft,
								gui.getContentRectHeight());
				int j = 14737632;
                if (!this.active) {
					j = 10526880;
				} else if (this.isHovered) {
					j = 16777120;
				}

				for (int i = 0; i < lines.size(); i++) {
					String s = lines.get(i);
					AbstractGui.drawString(matrix, fontrenderer, s, this.x, this.y + i * 10, j);
				}

			}
		}

		/*@Override
		public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
			return super.mousePressed(mc, mouseX, mouseY)
					&& Utils.isInArea(mouseX, mouseY, rectLeft, rectTop, rectRight - rectLeft, rectBottom - rectTop);
		}*/

		public void setDeltaY(int delta) {
			this.deltaY = delta;
		}

		public int getDeltaY() {
			return this.deltaY;
		}
	}
}
