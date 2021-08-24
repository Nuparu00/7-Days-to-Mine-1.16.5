package nuparu.sevendaystomine.client.renderer.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.model.entity.ZombiePolicemanModel;
import nuparu.sevendaystomine.client.util.RenderStates;
import nuparu.sevendaystomine.entity.ZombieBaseEntity;
import nuparu.sevendaystomine.entity.ZombiePolicemanEntity;
import nuparu.sevendaystomine.util.Utils;

public class ZombiePolicemanGlow<T extends ZombiePolicemanEntity, M extends ZombiePolicemanModel<T>, A extends ZombiePolicemanModel<T>>
		extends LayerRenderer<T, M> {


	public ZombiePolicemanGlow(IEntityRenderer<T, M> p_i50926_1_) {
		super(p_i50926_1_);
	}

	@Override
	public void render(MatrixStack p_225628_1_, IRenderTypeBuffer p_225628_2_, int p_225628_3_, T entity,
			float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_,
			float p_225628_10_) {
		IVertexBuilder ivertexbuilder = p_225628_2_.getBuffer(RenderStates.zombiePolicemanGlow(
				new ResourceLocation(SevenDaysToMine.MODID, "textures/entity/zombie_policeman_glow.png"),
				1f - ((float) entity.getVomitTimer() / ZombiePolicemanEntity.RECHARGE_TIME)));
		this.getParentModel().renderToBuffer(p_225628_1_, ivertexbuilder, 15728640, OverlayTexture.NO_OVERLAY, 1.0F,
				1.0F, 1.0F, 1-((float)entity.getVomitTimer()/ZombiePolicemanEntity.RECHARGE_TIME));
	}

}
