package nuparu.sevendaystomine.client.renderer.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.util.RenderStates;
import nuparu.sevendaystomine.entity.ZombieBaseEntity;

public class SoulBurnsLayer<T extends ZombieBaseEntity, M extends BipedModel<T>, A extends BipedModel<T>>
		extends LayerRenderer<T, M> {

	private static final RenderType SPIDER_EYES = RenderStates
			.eyes(new ResourceLocation(SevenDaysToMine.MODID, "textures/entity/soul_burnt_zombie_glow.png"));

	public SoulBurnsLayer(IEntityRenderer<T, M> p_i50926_1_) {
		super(p_i50926_1_);
	}

	@Override
	public void render(MatrixStack p_225628_1_, IRenderTypeBuffer p_225628_2_, int p_225628_3_, T p_225628_4_,
			float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_,
			float p_225628_10_) {
		IVertexBuilder ivertexbuilder = p_225628_2_.getBuffer(this.renderType());
		this.getParentModel().renderToBuffer(p_225628_1_, ivertexbuilder, 15728640, OverlayTexture.NO_OVERLAY, 1.0F,
				1.0F, 1.0F, 1.0F);
	}

	public RenderType renderType() {
		return SPIDER_EYES;
	}
}
