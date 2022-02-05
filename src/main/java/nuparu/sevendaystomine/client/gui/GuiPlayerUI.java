package nuparu.sevendaystomine.client.gui;

import nuparu.sevendaystomine.config.ServerConfig;
import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.item.ItemGun;
import nuparu.sevendaystomine.potions.Potions;
import nuparu.sevendaystomine.config.ClientConfig;

@OnlyIn(Dist.CLIENT)
public class GuiPlayerUI {

	public static final ResourceLocation UI_TEX = new ResourceLocation(SevenDaysToMine.MODID, "textures/gui/hud.png");
	public static final ResourceLocation SCOPE_TEX = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/misc/scope.png");

	float minibikeFuelPrev = 0;

	public GuiPlayerUI() {
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	@OnlyIn(Dist.CLIENT)
	public void onRenderGameOverlayEvent(RenderGameOverlayEvent event) {
		if (!event.isCancelable() && event.getType() == ElementType.EXPERIENCE) {

			Minecraft mc = Minecraft.getInstance();
			PlayerEntity player = mc.player;
			MatrixStack matrix = event.getMatrixStack();

			if (player == null)
				return;

			matrix.pushPose();
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.disableLighting();
			RenderSystem.disableBlend();
			RenderSystem.blendFuncSeparate(770, 771, 1, 0);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			mc.getTextureManager().bind(UI_TEX);

			EnumHudPosition pos = ClientConfig.hudPosition.get();

			MainWindow resolution = event.getWindow();

			double width = resolution.getGuiScaledWidth();
			double height = resolution.getGuiScaledHeight();
			boolean showFuel = false;
			// boolean showFuel = player.getVehicle() instanceof EntityMinibike;

			int x = (int) (width * pos.getX()) + pos.getXOffset();
			int y = (int) (height * pos.getY()) + pos.getYOffset() + (pos.isTop() && showFuel ? 10 : 0);

			if (!player.isCreative() && !player.isSpectator() && !mc.options.hideGui) {
				IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
				if (extendedPlayer != null) {
					if (ServerConfig.thirstSystem.get()) {
						mc.gui.blit(matrix, x + 1, y - 9, 0, player.hasEffect(Potions.DYSENTERY.get()) ? 29 : 8,
								(int) Math.floor(extendedPlayer.getThirst()
										/ (10f * (extendedPlayer.getMaximumThirst() / 780f))),
								6);
						mc.gui.blit(matrix, x, y - 10, 0, 0, 81, 8);
					}
					if (ServerConfig.staminaSystem.get()) {
						mc.gui.blit(matrix, x + 1, y, 0, 15, (int) Math.floor(
								extendedPlayer.getStamina() / (10f * (extendedPlayer.getMaximumStamina() / 780f))), 6);
						mc.gui.blit(matrix, x, y - 1, 0, 0, 81, 8);
					}
				}
			}

			/*
			 * if (showFuel) { EntityMinibike minibike = (EntityMinibike)
			 * player.getVehicle(); float fuel = minibike.getFuel(); mc.gui.blit(matrix,x +
			 * 1, y - 18, 0, 22, (int) Math .floor((fuel == 0 ? minibikeFuelPrev : fuel) /
			 * (10f * (EntityMinibike.MAX_FUEL / 780f))), 6); mc.gui.blit(matrix,x, y - 19,
			 * 0, 0, 81, 8); minibikeFuelPrev = minibike.getFuel(); }
			 */

			ItemStack stack = player.getMainHandItem();

			if (stack.isEmpty() || !(stack.getItem() instanceof ItemGun)) {
				stack = player.getOffhandItem();
				if (stack.isEmpty() || !(stack.getItem() instanceof ItemGun)) {
					matrix.popPose();
					return;
				}
			}
			ItemGun gun = (ItemGun) stack.getItem();
			float factor = gun.getFOVFactor(stack);
			if (factor == 1) {
				matrix.popPose();
				return;
			}
			if (mc.options.keyAttack.isDown() && gun.getScoped()) {
				int w = resolution.getGuiScaledWidth();
				int h = resolution.getGuiScaledHeight();

				RenderSystem.enableBlend();
				RenderSystem.disableDepthTest();
				RenderSystem.depthMask(false);
				RenderSystem.defaultBlendFunc();
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				RenderSystem.disableAlphaTest();
				mc.getTextureManager().bind(SCOPE_TEX);
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder bufferbuilder = tessellator.getBuilder();
				bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
				bufferbuilder.vertex(w / 2 - 2 * h, h, -90f).uv(0.0f, 1.0f).endVertex();
				bufferbuilder.vertex(w / 2 + 2 * h, h, -90f).uv(1.0f, 1.0f).endVertex();
				bufferbuilder.vertex(w / 2 + 2 * h, 0, -90).uv(1.0f, 0.0f).endVertex();
				bufferbuilder.vertex( w / 2 - 2 * h, 0, -90).uv(0.0f, 0.0f).endVertex();
				tessellator.end();
				RenderSystem.depthMask(true);
				RenderSystem.enableDepthTest();
				RenderSystem.enableAlphaTest();
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				RenderSystem.disableBlend();
			}
			matrix.popPose();
		}
	}
}
