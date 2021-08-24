package nuparu.sevendaystomine.client.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class RenderStates extends RenderState {

	protected static final RenderState.TransparencyState ZOMBIE_POLICEMAN_GLOW_TRANSPARENCY = new RenderState.TransparencyState(
			"zombie_policeman_glow_transparency", () -> {
				RenderSystem.enableBlend();
				RenderSystem.enableAlphaTest();
				RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
						GlStateManager.DestFactor.DST_ALPHA, GlStateManager.SourceFactor.ONE_MINUS_SRC_ALPHA,
						GlStateManager.DestFactor.DST_ALPHA);
			}, () -> {
				RenderSystem.disableBlend();
				RenderSystem.disableAlphaTest();
				RenderSystem.defaultBlendFunc();
			});

	public RenderStates(String p_i225973_1_, Runnable p_i225973_2_, Runnable p_i225973_3_) {
		super(p_i225973_1_, p_i225973_2_, p_i225973_3_);
	}

	public static RenderType eyes(ResourceLocation p_228652_0_) {
		RenderState.TextureState renderstate$texturestate = new RenderState.TextureState(p_228652_0_, false, false);
		return RenderType.create("eyes", DefaultVertexFormats.NEW_ENTITY, 7, 256, false, true,
				RenderType.State.builder().setTextureState(renderstate$texturestate)
						.setTransparencyState(TRANSLUCENT_TRANSPARENCY).setWriteMaskState(COLOR_WRITE)
						.setFogState(BLACK_FOG).createCompositeState(false));
	}

	public static RenderType zombiePolicemanGlow(ResourceLocation p_228652_0_, float f) {
		RenderState.TextureState renderstate$texturestate = new RenderState.TextureState(p_228652_0_, false, false);
		return RenderType.create("zombie_policeman_glow", DefaultVertexFormats.NEW_ENTITY, 7, 256, false, true,
				RenderType.State.builder().setTextureState(renderstate$texturestate)
						.setTransparencyState(ZOMBIE_POLICEMAN_GLOW_TRANSPARENCY).setWriteMaskState(COLOR_WRITE)
						.setFogState(BLACK_FOG).setOverlayState(OVERLAY).setLightmapState(NO_LIGHTMAP)
						.createCompositeState(false));

	}
}
