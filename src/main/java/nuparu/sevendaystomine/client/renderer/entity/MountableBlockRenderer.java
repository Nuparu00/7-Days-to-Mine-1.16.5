package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.entity.MountableBlockEntity;

public class MountableBlockRenderer<T extends MountableBlockEntity> extends EntityRenderer<T> {

	public MountableBlockRenderer(EntityRendererManager manager) {
		super(manager);
	}

	@Override
	public ResourceLocation getTextureLocation(T p_110775_1_) {
		return null;
	}

	public static class RenderFactory implements IRenderFactory<MountableBlockEntity> {

		public RenderFactory(EntityRendererManager manager) {

		}

		@Override
		public EntityRenderer<? super MountableBlockEntity> createRenderFor(EntityRendererManager manager) {
			return new MountableBlockRenderer(manager);
		}
	}

}
