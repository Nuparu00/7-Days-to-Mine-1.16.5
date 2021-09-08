package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.model.entity.BloatedZombieModel;
import nuparu.sevendaystomine.client.renderer.layer.RedEyesLayer;
import nuparu.sevendaystomine.entity.BloatedZombieEntity;
import nuparu.sevendaystomine.entity.RocketEntity;

public class RocketRenderer<T extends RocketEntity> extends EntityRenderer<T> {

	public RocketRenderer(EntityRendererManager manager) {
		super(manager);
	}

	@Override
	public ResourceLocation getTextureLocation(T p_110775_1_) {
		return null;
	}

	public static class RenderFactory implements IRenderFactory<RocketEntity> {

		public RenderFactory(EntityRendererManager manager) {

		}

		@Override
		public EntityRenderer<? super RocketEntity> createRenderFor(EntityRendererManager manager) {
			return new RocketRenderer(manager);
		}
	}

}
