package nuparu.sevendaystomine.client.gui;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.item.IReloadable;
import nuparu.sevendaystomine.item.ItemFuelTool;
import nuparu.sevendaystomine.item.ItemGun;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Utils;

@OnlyIn(Dist.CLIENT)
public class GuiGun {

	public static final Minecraft mc = Minecraft.getInstance();

	private double posPrev = 0;
	private double pos = 0;

	@SubscribeEvent(priority = EventPriority.NORMAL)
	@OnlyIn(Dist.CLIENT)
	public void onRenderGameOverlayEvent(RenderGameOverlayEvent event) {
		if (!event.isCancelable() && event.getType() == ElementType.EXPERIENCE) {
			MatrixStack matrix = event.getMatrixStack();
			posPrev = pos;
			MainWindow res = event.getWindow();

			int x = (res.getGuiScaledWidth()) / 2;
			int y = (res.getGuiScaledHeight()) / 2;

			PlayerEntity player = mc.player;

			ItemStack main = player.getMainHandItem();
			ItemStack sec = player.getOffhandItem();
			if ((main == null || main.isEmpty()) && (sec == null || sec.isEmpty())) {
				pos = 0;
				return;
			}
			Item item_main = main.getItem();
			Item item_sec = sec.getItem();
			if (item_main == null && item_sec == null) {
				pos = 0;
				return;
			}

			IReloadable reloadableMain = null;
			IReloadable reloadableSec = null;

			if (item_main instanceof IReloadable) {
				reloadableMain = (IReloadable) item_main;
			}

			if (item_sec instanceof IReloadable) {
				reloadableSec = (IReloadable) item_sec;
			}

			if (reloadableMain == null && reloadableSec == null) {
				return;
			}

			if (reloadableMain != null) {
				int ammoMain = reloadableMain.getAmmo(main, player);
				int color_main = ammoMain <= 0 ? 0xff0000 : 0xffffff;
				boolean fuel = reloadableMain instanceof ItemFuelTool;
				String text = SevenDaysToMine.proxy.localize(fuel ? "stat.fuel" : "stat.ammo",ammoMain+"/"+reloadableMain.getCapacity(main, player));
				mc.font.draw(matrix,text,
						event.getWindow().getGuiScaledWidth() - mc.font.width(text), 0, color_main);
			}

			if (reloadableSec != null) {
				int ammoSec = reloadableSec.getAmmo(sec, player);
				int color_sec = ammoSec <= 0 ? 0xff0000 : 0xffffff;
				boolean fuel = reloadableSec instanceof ItemFuelTool;
				String text = SevenDaysToMine.proxy.localize(fuel ? "stat.fuel" : "stat.ammo",ammoSec+"/"+reloadableSec.getCapacity(main, player));
				mc.font.draw(matrix,text, 0, 0, color_sec);
			}

			ItemGun gunMain = null;
			if (reloadableMain instanceof ItemGun) {
				gunMain = (ItemGun) reloadableMain;
			}
			ItemGun gunSec = null;
			if (reloadableSec instanceof ItemGun) {
				gunSec = (ItemGun) reloadableSec;
			}

			if (gunMain != null || gunSec != null) {

				if(gunMain != null && (gunMain.getFOVFactor(main) != 1 && mc.options.keyAttack.isDown())) {
					return;
				}
				
				if(gunSec != null && (gunSec.getFOVFactor(sec) != 1 && mc.options.keyAttack.isDown())) {
					return;
				}

				double gunCross = Utils.getCrosshairSpread(player);
				float vel = (float) (Math.abs(player.getDeltaMovement().x) + Math.abs(player.getDeltaMovement().z)) * 0.5f;

				pos = gunCross * (float) (0.75 + 3.14 * vel);
				if (player.isCrouching()) {
					pos *= 0.75f;
				}

				mc.getTextureManager().bind(GuiPlayerUI.UI_TEX);

				double alpha = 1f - (vel) * 3.3f;

				double finalPos = MathUtils.lerp(posPrev, pos, event.getPartialTicks());

				RenderSystem.pushMatrix();
				GL11.glColor4d(1d, 1d, 1d, alpha);
				RenderSystem.enableBlend();
				RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
						GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE,
						GlStateManager.DestFactor.ZERO);
				RenderSystem.enableAlphaTest();
				// LEFT
				mc.gui.blit(matrix,(int) (x - 13 / 2 - (finalPos)), y - 2, 0, 118, 13, 2);
				// RIGHT
				mc.gui.blit(matrix,(int) (x - 13 / 2 + (finalPos)), y - 2, 19, 118, 13, 2);
				// TOP
				mc.gui.blit(matrix,x - 2, (int) (y - 13 / 2 - (finalPos)), 14, 104, 2, 13);
				// DOVVN
				mc.gui.blit(matrix,x - 2, (int) (y - 13 / 2 + (finalPos)), 14, 123, 2, 13);
				RenderSystem.disableAlphaTest();
				RenderSystem.disableBlend();
				RenderSystem.popMatrix();
			}
		}
	}

}
