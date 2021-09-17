package nuparu.sevendaystomine.client.renderer.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.fml.config.ModConfig;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.util.PlayerInventorySyncHelper;
import nuparu.sevendaystomine.util.item.ItemCache;

public class HolsteredLayer extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {
    public HolsteredLayer(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> p_i50950_1_) {
        super(p_i50950_1_);
    }

    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int p_225628_3_, AbstractClientPlayerEntity player, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {

        if (CommonConfig.renderPlayerInventory.get()) {
            if (PlayerInventorySyncHelper.itemsCache != null
                    && PlayerInventorySyncHelper.itemsCache.containsKey(player.getName().getString())) {
                ItemCache cache = PlayerInventorySyncHelper.itemsCache.get(player.getName().getString());
                if (cache == null || cache.isEmpty())
                    return;
                Minecraft mc = Minecraft.getInstance();
                World world = player.level;
                ItemRenderer itemRenderer = mc.getItemRenderer();
                float swing = getParentModel().attackTime;
                //The item on player's back
                if (!cache.longItem.isEmpty()) {
                    ItemStack longStack = cache.longItem;
                    matrixStack.pushPose();

                    matrixStack.translate(0,0.375,0.155);
                    if (player.isCrouching()){
                        matrixStack.mulPose(Vector3f.XP.rotationDegrees(28.6479f));
                        matrixStack.translate(0,0.147, 0.104);
                    }
                    if(swing != 0){
                        float angle =  MathHelper.sin(MathHelper.sqrt(swing) * ((float)Math.PI * 2F)) * 0.2F;
                        if(getAttackArm(player) == HandSide.LEFT){
                            angle *=-1;
                        }
                        angle = (float) Math.toDegrees(angle);
                        matrixStack.mulPose(Vector3f.YP.rotationDegrees(angle));
                    }
                    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180));
                    IBakedModel upperModel = itemRenderer.getModel(longStack, world, null);
                    itemRenderer.render(longStack, ItemCameraTransforms.TransformType.GUI, true, matrixStack, buffer, p_225628_3_,  OverlayTexture.NO_OVERLAY, upperModel);

                    matrixStack.popPose();
                }
                //The item on the player's left hip
                if (!cache.shortItem_L.isEmpty()) {
                    ItemStack leftStack = cache.shortItem_L;
                    matrixStack.pushPose();

                    matrixStack.translate(0.23,0.85,0);
                    if (player.isCrouching()){
                        matrixStack.translate(0, 0.05, 0.25);
                    }

                    matrixStack.mulPose(Vector3f.XP.rotationDegrees((float)Math.toDegrees(getParentModel().leftLeg.xRot)));
                    matrixStack.mulPose(Vector3f.YP.rotationDegrees((float)Math.toDegrees(getParentModel().leftLeg.zRot)));
                    matrixStack.mulPose(Vector3f.ZP.rotationDegrees((float)Math.toDegrees(getParentModel().leftLeg.zRot)));

                    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(90));
                    matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
                    matrixStack.mulPose(Vector3f.XP.rotationDegrees(90));


                    matrixStack.scale(0.475f,0.475f,0.475f);
                    IBakedModel upperModel = itemRenderer.getModel(leftStack, world, null);
                    itemRenderer.render(leftStack, ItemCameraTransforms.TransformType.GUI, true, matrixStack, buffer, p_225628_3_,  OverlayTexture.NO_OVERLAY, upperModel);

                    matrixStack.popPose();
                }

            //The item on the player's right hip
            if (!cache.shortItem_R.isEmpty()) {
                ItemStack leftStack = cache.shortItem_R;
                matrixStack.pushPose();

                matrixStack.translate(-0.27,0.85,0);
                if (player.isCrouching()){
                    matrixStack.translate(0, 0.05, 0.25);
                }
                matrixStack.mulPose(Vector3f.XP.rotationDegrees((float)Math.toDegrees(getParentModel().rightLeg.xRot)));
                matrixStack.mulPose(Vector3f.YP.rotationDegrees((float)Math.toDegrees(getParentModel().rightLeg.zRot)));
                matrixStack.mulPose(Vector3f.ZP.rotationDegrees((float)Math.toDegrees(getParentModel().rightLeg.zRot)));

                matrixStack.mulPose(Vector3f.ZP.rotationDegrees(90));
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(90));


                matrixStack.scale(0.475f,0.475f,0.475f);
                IBakedModel upperModel = itemRenderer.getModel(leftStack, world, null);
                itemRenderer.render(leftStack, ItemCameraTransforms.TransformType.GUI, true, matrixStack, buffer,  p_225628_3_,  OverlayTexture.NO_OVERLAY, upperModel);

                matrixStack.popPose();
            }
        }
        }
    }
    protected HandSide getAttackArm(PlayerEntity p_217147_1_) {
        HandSide handside = p_217147_1_.getMainArm();
        return p_217147_1_.swingingArm == Hand.MAIN_HAND ? handside : handside.getOpposite();
    }

}