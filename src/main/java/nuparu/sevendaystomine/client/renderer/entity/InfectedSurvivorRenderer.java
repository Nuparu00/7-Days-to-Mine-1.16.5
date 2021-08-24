package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.entity.InfectedSurvivorEntity;

public class InfectedSurvivorRenderer<T extends InfectedSurvivorEntity, M extends BipedModel<T>>
		extends ZombieBipedRenderer<T, M> {
	private static final ResourceLocation ZOMBIE_LOCATION = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/infected_survivor.png");

	public InfectedSurvivorRenderer(EntityRendererManager manager) {
		super(manager);
	}

	public ResourceLocation getTextureLocation(T p_110775_1_) {
		return ZOMBIE_LOCATION;
	}

	protected boolean isShaking(T p_230495_1_) {
		return false;
	}

	public static class RenderFactory implements IRenderFactory<InfectedSurvivorEntity> {

		public RenderFactory(EntityRendererManager manager) {

		}

		@Override
		public EntityRenderer<? super InfectedSurvivorEntity> createRenderFor(EntityRendererManager manager) {
			return new InfectedSurvivorRenderer(manager);
		}
	}
}
