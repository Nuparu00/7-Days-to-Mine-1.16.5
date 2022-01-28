package nuparu.sevendaystomine.client.renderer.entity;

import java.lang.reflect.Field;
import java.util.Map;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.entity.LootableCorpseEntity;
import nuparu.sevendaystomine.entity.ZombieBaseEntity;

public class LootableCorpseRenderer<T extends LootableCorpseEntity> extends EntityRenderer<T> {

	public LootableCorpseRenderer(EntityRendererManager manager) {
		super(manager);
	}

	@Override
	public void render(T entity, float entityYaw, float partialTicks, MatrixStack matrix,
			IRenderTypeBuffer buffer, int p_225623_6_) {
		super.render(entity, entityYaw, partialTicks, matrix, buffer, p_225623_6_);
		Entity original = entity.getOriginal();
		if (original == null)
			return;

		Field f = ObfuscationReflectionHelper.findField(RenderingRegistry.class, "INSTANCE");
		f.setAccessible(true);
		Object obj = null;
		try {
			obj = f.get(null);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		if (obj == null)
			return;

		RenderingRegistry registry = (RenderingRegistry) obj;

		Map<EntityType<? extends Entity>, IRenderFactory<? extends Entity>> entityRenderers = null;

		entityRenderers = ObfuscationReflectionHelper
				.getPrivateValue(RenderingRegistry.class, registry, "entityRenderers");

		EntityRenderer render = null;

		for (Map.Entry<EntityType<? extends Entity>, EntityRenderer<? extends Entity>> entry : RenderUtils.entityRenderers
				.entrySet()) {
			EntityType<? extends Entity> type = entry.getKey();
			if (type == original.getType()) {
				render = entry.getValue();
				break;
			}
		}
		if (render == null) {
			for (Map.Entry<EntityType<? extends Entity>, IRenderFactory<? extends Entity>> entry : entityRenderers
					.entrySet()) {
				EntityType<? extends Entity> type = entry.getKey();
				if (type == original.getType()) {
					render = entry.getValue().createRenderFor(entityRenderDispatcher);
					RenderUtils.entityRenderers.put(type, render);
					break;
				}
			}
		}

		if (render == null) {
			return;
		}
		
		float new_x = (float) ((original.getBbHeight() / 2f) * Math.cos((entityYaw + 90) * Math.PI / 180));
		float new_y = (original.getBbWidth() / 4f);
		float new_z = (float) ((original.getBbHeight() / 2f) * Math.sin((entityYaw + 90) * Math.PI / 180));

		float rotX = -90;
		float rotY = entityYaw;
		float rotZ = 0;
//entityYaw

		if (original instanceof ZombieBaseEntity) {
			ZombieBaseEntity zombie = ((ZombieBaseEntity) original);

			if (zombie.customCoprseTransform()) {
				Vector3d rot = zombie.corpseRotation();
				rotX+=rot.x;
				rotY+=rot.y;
				rotZ+=rot.z;

				new_y += zombie.corpseTranslation().y;
			}
			/*if (zombie.customCoprseTransform()) {
				Vector3d rot = zombie.corpseRotation();
				rotX += (float) rot.x;
				rotY += (float) rot.y;
				rotZ += (float) rot.z;
				Vector3d pos = zombie.corpseTranslation();
				new_x = (float) (pos.x * Math.cos((entityYaw + 90) * Math.PI / 180));
				new_y += pos.y;
				new_z = (float) (pos.z * Math.sin((entityYaw + 90) * Math.PI / 180));
			}*/
		}
		matrix.pushPose();
		//RenderSystem.rotatef(entityYaw, 0, -1, 0);
		//RenderSystem.rotatef(90, rotX, rotY, rotZ);

		//RenderSystem.rotatef(90, rotX, rotY, rotZ);
       // System.out.println(new_x + " " + new_y + " " + new_z);
		matrix.translate(-new_x, new_y, new_z);
		matrix.mulPose(Vector3f.YP.rotationDegrees(rotY));
		matrix.pushPose();
		matrix.mulPose(Vector3f.ZP.rotationDegrees(rotZ));
		matrix.mulPose(Vector3f.XP.rotationDegrees(rotX));
		render.render(original, entityYaw, partialTicks, matrix,buffer,p_225623_6_);
		matrix.popPose();
		//RenderSystem.rotatef(-90, rotX, rotY, rotZ);
		matrix.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(T p_110775_1_) {
		return null;
	}

	public static class RenderFactory implements IRenderFactory<LootableCorpseEntity> {

		public RenderFactory(EntityRendererManager manager) {

		}

		@Override
		public EntityRenderer<? super LootableCorpseEntity> createRenderFor(EntityRendererManager manager) {
			return new LootableCorpseRenderer(manager);
		}
	}

}
