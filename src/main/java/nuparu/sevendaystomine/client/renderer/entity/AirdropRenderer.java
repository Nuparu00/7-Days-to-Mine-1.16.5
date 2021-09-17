package nuparu.sevendaystomine.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.model.entity.AirdropModel;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.entity.AirdropEntity;
import nuparu.sevendaystomine.entity.LootableCorpseEntity;
import nuparu.sevendaystomine.entity.ZombieBaseEntity;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Map;

public class AirdropRenderer<T extends AirdropEntity> extends EntityRenderer<T> {
	private static final ResourceLocation TEX = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/airdrop.png");
	public static final AirdropModel model = new AirdropModel();

	public AirdropRenderer(EntityRendererManager manager) {
		super(manager);
	}

	@Override
	public void render(T entity, float entityYaw, float partialTicks, MatrixStack matrix,
			IRenderTypeBuffer buffer, int p_225623_6_) {
		super.render(entity, entityYaw, partialTicks, matrix, buffer, p_225623_6_);
		Minecraft minecraft = Minecraft.getInstance();

		boolean flag = this.isBodyVisible(entity);
		boolean flag1 = !flag && !entity.isInvisibleTo(minecraft.player);
		boolean flag2 = minecraft.shouldEntityAppearGlowing(entity);
		RenderType rendertype = this.getRenderType(entity, flag, flag1, flag2);
		matrix.pushPose();
		//matrix.scale(1,-1,1);
		matrix.translate(0,1.5,0);
		matrix.mulPose(Vector3f.XP.rotationDegrees(180));
		if (rendertype != null) {
			IVertexBuilder ivertexbuilder = buffer.getBuffer(rendertype);
			int i = getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, partialTicks));
			this.model.setupAnim(entity,0,0,0,0,0);
			this.model.renderToBuffer(matrix, ivertexbuilder, p_225623_6_, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
		}
		matrix.popPose();
	}
	@Nullable
	protected RenderType getRenderType(T p_230496_1_, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
		ResourceLocation resourcelocation = this.getTextureLocation(p_230496_1_);
		if (p_230496_3_) {
			return RenderType.itemEntityTranslucentCull(resourcelocation);
		} else if (p_230496_2_) {
			return this.model.renderType(resourcelocation);
		} else {
			return p_230496_4_ ? RenderType.outline(resourcelocation) : null;
		}
	}

	public static int getOverlayCoords(Entity p_229117_0_, float p_229117_1_) {
		return OverlayTexture.pack(OverlayTexture.u(p_229117_1_), OverlayTexture.v(false));
	}
	protected float getWhiteOverlayProgress(T p_225625_1_, float p_225625_2_) {
		return 0.0F;
	}
	protected boolean isBodyVisible(T p_225622_1_) {
		return !p_225622_1_.isInvisible();
	}
	@Override
	public ResourceLocation getTextureLocation(T p_110775_1_) {
		return TEX;
	}

	public static class RenderFactory implements IRenderFactory<AirdropEntity> {

		public RenderFactory(EntityRendererManager manager) {

		}

		@Override
		public EntityRenderer<? super AirdropEntity> createRenderFor(EntityRendererManager manager) {
			return new AirdropRenderer(manager);
		}
	}

}
