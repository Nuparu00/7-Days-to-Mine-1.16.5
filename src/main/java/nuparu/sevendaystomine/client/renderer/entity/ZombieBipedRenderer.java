package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import nuparu.sevendaystomine.client.renderer.layer.RedEyesLayer;
import nuparu.sevendaystomine.entity.ZombieBipedEntity;

public abstract class ZombieBipedRenderer<T extends ZombieBipedEntity, M extends BipedModel<T>>
		extends BipedRenderer<T, M> {

	public RedEyesLayer redEyesLayer;
	
	public ZombieBipedRenderer(EntityRendererManager manager) {
		super(manager, (M) new BipedModel<T>(0.0F), 0.5F);
		this.addLayer(redEyesLayer = new RedEyesLayer<T, M, M>(this));
	}

	protected boolean isShaking(T p_230495_1_) {
		return false;
	}

}
