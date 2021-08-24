package nuparu.sevendaystomine.client.gui;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.dialogue.Subtitle;
import nuparu.sevendaystomine.util.dialogue.SubtitleHelper;

@OnlyIn(Dist.CLIENT)
public class GuiSubtitles {

	@SubscribeEvent(priority = EventPriority.NORMAL)
	@OnlyIn(Dist.CLIENT)
	public void onRenderGameOverlayEvent(RenderGameOverlayEvent event) {
		
		if (event.getType() == ElementType.EXPERIENCE && !event.isCancelable()) {
			MatrixStack matrix = event.getMatrixStack();
			SubtitleHelper helper = SubtitleHelper.INSTANCE;
			if (helper.getCurrentSubtitle() == null) {
				if (!helper.isAnythingInQueue()) {
					return;
				}
				helper.setCurrentSubtitle(getSubtitleFromQueue());
			} else if (helper.getCurrentSubtitle().showTime >= helper.getCurrentSubtitle().getDuration()) {
				ForgeIngameGui.renderArmor = true;
				if (!helper.isAnythingInQueue()) {
					return;
				}
				helper.setCurrentSubtitle(getSubtitleFromQueue());
			}

			Minecraft mc = Minecraft.getInstance();
			Subtitle subtitle = helper.getCurrentSubtitle();
			if (subtitle == null)
				return;
			MainWindow resolution = event.getWindow();

			int dialogueBottom = Math.round(resolution.getGuiScaledHeight() - 40 - (resolution.getGuiScaledHeight() / 10f));
			double scale = resolution.getGuiScale();
			int x = resolution.getGuiScaledWidth() / 2;
			int yBase = dialogueBottom;
			yBase += MathUtils.clamp((int) Math.round((resolution.getGuiScaledHeight() - dialogueBottom - 50) / 2d), 5,
					resolution.getGuiScaledHeight() - dialogueBottom);

			GL11.glPushMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_LIGHTING);
			if (subtitle.getSender() != null) {
				RenderUtils.drawCenteredString(matrix,subtitle.getSender().getDisplayName().getString(), x, yBase, GuiDialogue.STYLING_COLOR,
						true);
			}
			RenderUtils.drawCenteredString(matrix,
					TextFormatting.ITALIC + SevenDaysToMine.proxy.localize(subtitle.getDialogue() + ".response"), x,
					yBase + 10, 0xffffff, true);
			GL11.glPopMatrix();

			ObfuscationReflectionHelper.setPrivateValue(IngameGui.class, mc.gui, 0, "field_92017_k");

			subtitle.showTime+=event.getPartialTicks();
			ForgeIngameGui.renderArmor = false;
		}
	}

	public static Subtitle getSubtitleFromQueue() {
		try {
			return SubtitleHelper.INSTANCE.getSubtitleFromQueue();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
