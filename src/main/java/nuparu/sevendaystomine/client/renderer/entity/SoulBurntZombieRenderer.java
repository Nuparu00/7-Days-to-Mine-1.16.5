package nuparu.sevendaystomine.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.renderer.layer.SoulBurnsLayer;
import nuparu.sevendaystomine.entity.SoulBurntZombieEntity;

public class SoulBurntZombieRenderer<T extends SoulBurntZombieEntity, M extends BipedModel<T>>
		extends ZombieBipedRenderer<T, M> {

	public static final RenderMaterial FIRE_0 = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS,
			new ResourceLocation("block/soul_fire_0"));
	public static final RenderMaterial FIRE_1 = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS,
			new ResourceLocation("block/soul_fire_1"));

	private static final ResourceLocation ZOMBIE_LOCATION = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/soul_burnt_zombie.png");

	public SoulBurntZombieRenderer(EntityRendererManager manager) {
		super(manager);
		this.addLayer(new SoulBurnsLayer<T, M, M>(this));
	}

	public ResourceLocation getTextureLocation(T p_110775_1_) {
		return ZOMBIE_LOCATION;
	}

	@Override
	protected boolean isShaking(T p_230495_1_) {
		return false;
	}

	@Override
	public void render(T entity, float p_225623_2_, float p_225623_3_, MatrixStack matrix, IRenderTypeBuffer buffer,
			int p_225623_6_) {
		super.render(entity, p_225623_2_, p_225623_3_, matrix, buffer, p_225623_6_);
		if(!entity.isAlive()) return;
		renderFlame(matrix, buffer, entity);
	}

	private void renderFlame(MatrixStack p_229095_1_, IRenderTypeBuffer p_229095_2_, Entity p_229095_3_) {
		ActiveRenderInfo camera = Minecraft.getInstance().gameRenderer.getMainCamera();

		TextureAtlasSprite textureatlassprite = FIRE_0.sprite();
		TextureAtlasSprite textureatlassprite1 = FIRE_1.sprite();
		p_229095_1_.pushPose();
		float f = p_229095_3_.getBbWidth() * 1.4F;
		p_229095_1_.scale(f, f, f);
		float f1 = 0.5F;
		float f2 = 0.0F;
		float f3 = p_229095_3_.getBbHeight() / f;
		float f4 = 0.0F;
		p_229095_1_.mulPose(Vector3f.YP.rotationDegrees(-camera.getYRot()));
		p_229095_1_.translate(0.0D, 0.0D, -0.3F + (float) ((int) f3) * 0.02F);
		float f5 = 0.0F;
		int i = 0;
		IVertexBuilder ivertexbuilder = p_229095_2_.getBuffer(Atlases.cutoutBlockSheet());

		for (MatrixStack.Entry matrixstack$entry = p_229095_1_.last(); f3 > 0.0F; ++i) {
			TextureAtlasSprite textureatlassprite2 = i % 2 == 0 ? textureatlassprite : textureatlassprite1;
			float f6 = textureatlassprite2.getU0();
			float f7 = textureatlassprite2.getV0();
			float f8 = textureatlassprite2.getU1();
			float f9 = textureatlassprite2.getV1();
			if (i / 2 % 2 == 0) {
				float f10 = f8;
				f8 = f6;
				f6 = f10;
			}

			fireVertex(matrixstack$entry, ivertexbuilder, f1 - 0.0F, 0.0F - f4, f5, f8, f9);
			fireVertex(matrixstack$entry, ivertexbuilder, -f1 - 0.0F, 0.0F - f4, f5, f6, f9);
			fireVertex(matrixstack$entry, ivertexbuilder, -f1 - 0.0F, 1.4F - f4, f5, f6, f7);
			fireVertex(matrixstack$entry, ivertexbuilder, f1 - 0.0F, 1.4F - f4, f5, f8, f7);
			f3 -= 0.45F;
			f4 -= 0.45F;
			f1 *= 0.9F;
			f5 += 0.03F;
		}

		p_229095_1_.popPose();
	}

	private static void fireVertex(MatrixStack.Entry p_229090_0_, IVertexBuilder p_229090_1_, float p_229090_2_,
			float p_229090_3_, float p_229090_4_, float p_229090_5_, float p_229090_6_) {
		p_229090_1_.vertex(p_229090_0_.pose(), p_229090_2_, p_229090_3_, p_229090_4_).color(255, 255, 255, 255)
				.uv(p_229090_5_, p_229090_6_).overlayCoords(0, 10).uv2(240)
				.normal(p_229090_0_.normal(), 0.0F, 1.0F, 0.0F).endVertex();
	}

	public static class RenderFactory implements IRenderFactory<SoulBurntZombieEntity> {

		public RenderFactory(EntityRendererManager manager) {

		}

		@Override
		public EntityRenderer<? super SoulBurntZombieEntity> createRenderFor(EntityRendererManager manager) {
			return new SoulBurntZombieRenderer(manager);
		}
	}
}
