package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.model.entity.ZombieWolfModel;
import nuparu.sevendaystomine.client.renderer.layer.RedEyesLayer;
import nuparu.sevendaystomine.entity.ZombieWolfEntity;

public class ZombieWolfRenderer<T extends ZombieWolfEntity, M extends ZombieWolfModel<T>>
		extends MobRenderer<T, M> {
	private static final ResourceLocation ZOMBIE_LOCATION = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/zombie_wolf.png");
	private static final ResourceLocation EYES = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/zombie_wolf_eyes.png");

	public RedEyesLayer redEyesLayer;

	public ZombieWolfRenderer(EntityRendererManager manager) {
		super(manager, (M) new ZombieWolfModel<T>(), 0.5F);
		this.addLayer(redEyesLayer = new RedEyesLayer<T, M, M>(this,EYES));
	}

	public ResourceLocation getTextureLocation(T p_110775_1_) {
		return ZOMBIE_LOCATION;
	}

	protected boolean isShaking(T p_230495_1_) {
		return false;
	}

	public static class RenderFactory implements IRenderFactory<ZombieWolfEntity> {

		public RenderFactory(EntityRendererManager manager) {

		}

		@Override
		public EntityRenderer<? super ZombieWolfEntity> createRenderFor(EntityRendererManager manager) {
			return new ZombieWolfRenderer(manager);
		}
	}

}
