package nuparu.sevendaystomine.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.model.entity.ModelCar;
import nuparu.sevendaystomine.client.model.entity.ModelMinibike;
import nuparu.sevendaystomine.entity.AirdropEntity;
import nuparu.sevendaystomine.entity.CarEntity;
import nuparu.sevendaystomine.util.Utils;

public class CarRenderer<T extends CarEntity, M extends ModelCar<T>> extends LivingRenderer<T, M> {

    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            new ResourceLocation(SevenDaysToMine.MODID,
                    "textures/entity/car/car_white.png"),
            new ResourceLocation(SevenDaysToMine.MODID,
                    "textures/entity/car/car_orange.png"),
            new ResourceLocation(SevenDaysToMine.MODID,
                    "textures/entity/car/car_magenta.png"),
            new ResourceLocation(SevenDaysToMine.MODID,
                    "textures/entity/car/car_light_blue.png"),
            new ResourceLocation(SevenDaysToMine.MODID,
                    "textures/entity/car/car_yellow.png"),
            new ResourceLocation(SevenDaysToMine.MODID,
                    "textures/entity/car/car_lime.png"),
            new ResourceLocation(SevenDaysToMine.MODID,
                    "textures/entity/car/car_pink.png"),
            new ResourceLocation(SevenDaysToMine.MODID,
                    "textures/entity/car/car_gray.png"),
            new ResourceLocation(SevenDaysToMine.MODID,
                    "textures/entity/car/car_light_gray.png"),
            new ResourceLocation(SevenDaysToMine.MODID,
                    "textures/entity/car/car_cyan.png"),
            new ResourceLocation(SevenDaysToMine.MODID,
                    "textures/entity/car/car_purple.png"),
            new ResourceLocation(SevenDaysToMine.MODID,
                    "textures/entity/car/car_blue.png"),
            new ResourceLocation(SevenDaysToMine.MODID,
                    "textures/entity/car/car_brown.png"),
            new ResourceLocation(SevenDaysToMine.MODID,
                    "textures/entity/car/car_green.png"),
            new ResourceLocation(SevenDaysToMine.MODID,
                    "textures/entity/car/car_red.png"),
            new ResourceLocation(SevenDaysToMine.MODID,
                    "textures/entity/car/car_black.png"),

    };


    public CarRenderer(EntityRendererManager manager) {
        super(manager, (M) new ModelCar<T>(), 0.6f);
    }


	@Override
    public void render(T minibike, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        super.render(minibike, entityYaw, partialTicks, matrixStack, p_225623_5_, p_225623_6_);
        /*Minecraft minecraft = Minecraft.getInstance();
        matrixStack.pushPose();
        matrixStack.translate(0,1.5,0);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(entityYaw+180));
        matrixStack.mulPose(Vector3f.ZN.rotationDegrees(Utils.lerp(minibike.getTurningPrev(), minibike.getTurning(), partialTicks)));

        float frontRotation = Utils.lerp(minibike.getFrontRotationPrev(), minibike.getFrontRotation(), partialTicks) * 0.0174533f * 90.5f;
        matrixStack.mulPose(Vector3f.XN.rotationDegrees(frontRotation));


        matrixStack.mulPose(Vector3f.XP.rotationDegrees(-40));
        matrixStack.translate(0.07,-0.05,0.543);
        matrixStack.scale(0.005f,0.005f,0.005f);
        minecraft.font.draw(matrixStack,"100%",0,0,0x000000);
        matrixStack.popPose();*/
    }

	/*@Override
	public void render(T entity, float entityYaw, float partialTicks, MatrixStack matrix,
			IRenderTypeBuffer buffer, int p_225623_6_) {
		super.render(entity, entityYaw, partialTicks, matrix, buffer, p_225623_6_);
		Minecraft minecraft = Minecraft.getInstance();

		boolean flag = this.isBodyVisible(entity);
		boolean flag1 = !flag && !entity.isInvisibleTo(minecraft.player);
		boolean flag2 = minecraft.shouldEntityAppearGlowing(entity);
		RenderType rendertype = this.getRenderType(entity, flag, flag1, flag2);
		matrix.pushPose();
		//matrix.scale(1,-1,1);
		matrix.translate(0,1.5,0);
		matrix.mulPose(Vector3f.XP.rotationDegrees(180));
		if (rendertype != null) {
			IVertexBuilder ivertexbuilder = buffer.getBuffer(rendertype);
			int i = getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, partialTicks));
			model.setupAnim(entity,0,0,0,0,0);
			model.renderToBuffer(matrix, ivertexbuilder, p_225623_6_, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
		}
		matrix.popPose();
	}
	@Nullable
	protected RenderType getRenderType(T p_230496_1_, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
		ResourceLocation resourcelocation = this.getTextureLocation(p_230496_1_);
		if (p_230496_3_) {
			return RenderType.itemEntityTranslucentCull(resourcelocation);
		} else if (p_230496_2_) {
			return model.renderType(resourcelocation);
		} else {
			return p_230496_4_ ? RenderType.outline(resourcelocation) : null;
		}
	}*/

	/*public static int getOverlayCoords(Entity p_229117_0_, float p_229117_1_) {
		return OverlayTexture.pack(OverlayTexture.u(p_229117_1_), OverlayTexture.v(false));
	}
	protected float getWhiteOverlayProgress(T p_225625_1_, float p_225625_2_) {
		return 0.0F;
	}
	protected boolean isBodyVisible(T p_225622_1_) {
		return !p_225622_1_.isInvisible();
	}*/

    @Override
    protected void setupRotations(T minibike, MatrixStack matrixStack, float p_225621_3_, float p_225621_4_, float partialTicks) {
        super.setupRotations(minibike, matrixStack, p_225621_3_, p_225621_4_, partialTicks);
//Utils.lerp(minibike.getTurningPrev(),minibike.getTurning(),partialTicks)
        //matrixStack.mulPose(Vector3f.ZP.rotationDegrees(Utils.lerp(minibike.getTurningPrev(), minibike.getTurning(), partialTicks)));
    }

    @Override
    public ResourceLocation getTextureLocation(T minibike) {
        DyeColor color = minibike.getColor();

        return TEXTURES[color.ordinal()];
        //return TEXTURES[(int) (15*Math.abs(Math.sin(minibike.level.getGameTime()/10)))];
    }

	@Override
	protected boolean shouldShowName(T minibike) {
    	return false;
	}

    public static class RenderFactory implements IRenderFactory<AirdropEntity> {

        public RenderFactory(EntityRendererManager manager) {

        }

        @Override
        public EntityRenderer<? super AirdropEntity> createRenderFor(EntityRendererManager manager) {
            return new CarRenderer(manager);
        }
    }

}
