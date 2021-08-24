package nuparu.sevendaystomine.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.model.entity.ZombiePolicemanModel;
import nuparu.sevendaystomine.client.renderer.layer.RedEyesLayer;
import nuparu.sevendaystomine.client.renderer.layer.ZombiePolicemanGlow;
import nuparu.sevendaystomine.entity.ZombiePolicemanEntity;

public class ZombiePolicemanRenderer<T extends ZombiePolicemanEntity, M extends ZombiePolicemanModel<T>>
		extends MobRenderer<T, M> {
	private static final ResourceLocation ZOMBIE_LOCATION = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/zombie_policeman.png");
	private static final ResourceLocation EYES = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/zombie_policeman_eyes.png");

	public RedEyesLayer redEyesLayer;

	public ZombiePolicemanRenderer(EntityRendererManager manager) {
		super(manager, (M) new ZombiePolicemanModel<T>(), 0.5F);
		this.addLayer(redEyesLayer = new RedEyesLayer<T, M, M>(this, EYES));
		this.addLayer(new ZombiePolicemanGlow<T, M, M>(this));
	}

	public ResourceLocation getTextureLocation(T p_110775_1_) {
		return ZOMBIE_LOCATION;
	}

	protected boolean isShaking(T p_230495_1_) {
		return false;
	}

	@Override
	protected void scale(ZombiePolicemanEntity p_225620_1_, MatrixStack p_225620_2_, float p_225620_3_) {
		float f = p_225620_1_.getSwelling(p_225620_3_);
		float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
		f = MathHelper.clamp(f, 0.0F, 1.0F);
		f = f * f;
		f = f * f;
		float f2 = (1.0F + f * 0.4F) * f1;
		float f3 = (1.0F + f * 0.1F) / f1;
		p_225620_2_.scale(f2, f3, f2);
	}

	@Override
	protected float getWhiteOverlayProgress(ZombiePolicemanEntity p_225625_1_, float p_225625_2_) {
		float f = p_225625_1_.getSwelling(p_225625_2_);
		return (int) (f * 10.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(f, 0.5F, 1.0F);
	}

	public static class RenderFactory implements IRenderFactory<ZombiePolicemanEntity> {

		public RenderFactory(EntityRendererManager manager) {

		}

		@Override
		public EntityRenderer<? super ZombiePolicemanEntity> createRenderFor(EntityRendererManager manager) {
			return new ZombiePolicemanRenderer(manager);
		}
	}

}
