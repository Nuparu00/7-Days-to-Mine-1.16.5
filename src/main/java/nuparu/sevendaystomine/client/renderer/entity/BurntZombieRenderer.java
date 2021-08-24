package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.renderer.layer.BurnsLayer;
import nuparu.sevendaystomine.client.renderer.layer.RedEyesLayer;
import nuparu.sevendaystomine.entity.BurntZombieEntity;
import nuparu.sevendaystomine.entity.ReanimatedCorpseEntity;

public class BurntZombieRenderer<T extends BurntZombieEntity, M extends BipedModel<T>>
		extends ZombieBipedRenderer<T, M> {
	private static final ResourceLocation ZOMBIE_LOCATION = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/burnt_zombie.png");

	public BurntZombieRenderer(EntityRendererManager manager) {
		super(manager);
		this.addLayer(new BurnsLayer<T, M, M>(this));
	}

	public ResourceLocation getTextureLocation(T p_110775_1_) {
		return ZOMBIE_LOCATION;
	}

	protected boolean isShaking(T entity) {
		return entity.isSoulFireConverting();
	}

	public static class RenderFactory implements IRenderFactory<BurntZombieEntity> {

		public RenderFactory(EntityRendererManager manager) {

		}

		@Override
		public EntityRenderer<? super BurntZombieEntity> createRenderFor(EntityRendererManager manager) {
			return new BurntZombieRenderer(manager);
		}
	}
}
