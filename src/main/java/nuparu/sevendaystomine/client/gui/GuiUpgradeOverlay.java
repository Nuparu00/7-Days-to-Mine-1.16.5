package nuparu.sevendaystomine.client.gui;

import nuparu.sevendaystomine.item.IUpgrader;
import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.item.ItemUpgrader;
import nuparu.sevendaystomine.util.Utils;

@OnlyIn(Dist.CLIENT)
public class GuiUpgradeOverlay extends AbstractGui {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void eventHandler(RenderGameOverlayEvent event) {
		if (!event.isCancelable() && event.getType() == ElementType.EXPERIENCE) {

			MatrixStack matrix = event.getMatrixStack();
			Minecraft mc = Minecraft.getInstance();
			PlayerEntity entity = mc.player;
			ItemStack stack = entity.inventory.getSelected();
			if (stack != null && stack.getItem() instanceof IUpgrader) {
				BlockRayTraceResult objectPosition = Utils.rayTrace(entity.level, entity,
						entity.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue(),
						RayTraceContext.FluidMode.ANY);
				if (objectPosition != null) {
					BlockPos blockPos = objectPosition.getBlockPos();
					if (blockPos != null && stack.getTag() != null
							&& stack.getTag().contains("Percent", Constants.NBT.TAG_FLOAT)) {
						if (stack.getTag().getFloat("Percent") != 0) {

							MainWindow resolution = event.getWindow();
							int x = (resolution.getGuiScaledWidth() / 2);
							int y = (resolution.getGuiScaledHeight() / 2);
							float percent = stack.getOrCreateTag().getFloat("Percent");
							float percentAbs = Math.abs(percent);

							GL11.glPushMatrix();
							GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
							GL11.glDisable(GL11.GL_LIGHTING);
							GL11.glEnable(GL11.GL_BLEND);
							mc.getTextureManager().bind(GuiPlayerUI.UI_TEX);
							blit(matrix, x - 50, y + 2, 0, 178, (int) (percentAbs * 100f), 7);
							blit(matrix, x - 51, y - 32, percent < 0 ? 102 : 0, 136, 102, 42);
							RenderUtils.drawCenteredString(matrix, (int) (percentAbs * 100f) + "%", x, y + 10,
									0xffffff);
							GL11.glPopMatrix();

						}

					}

				}
			}

		}
	}
}
