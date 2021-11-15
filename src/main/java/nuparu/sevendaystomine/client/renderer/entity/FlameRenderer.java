package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.entity.FlameEntity;

public class FlameRenderer<T extends FlameEntity> extends EntityRenderer<T> {

	public FlameRenderer(EntityRendererManager manager) {
		super(manager);
	}

	@Override
	public ResourceLocation getTextureLocation(T p_110775_1_) {
		return null;
	}

	public static class RenderFactory implements IRenderFactory<FlameEntity> {

		public RenderFactory(EntityRendererManager manager) {

		}

		@Override
		public EntityRenderer<? super FlameEntity> createRenderFor(EntityRendererManager manager) {
			return new FlameRenderer(manager);
		}
	}

}
