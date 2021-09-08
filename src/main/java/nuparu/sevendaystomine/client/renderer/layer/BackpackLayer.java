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
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.model.entity.BackpackModel;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.util.PlayerInventorySyncHelper;
import nuparu.sevendaystomine.util.item.ItemCache;

public class BackpackLayer extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

    private final ResourceLocation TEX = new ResourceLocation(SevenDaysToMine.MODID, "entity/backpack");
    private final RenderMaterial MAT = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, TEX);
    BackpackModel backpackModel = new BackpackModel(0);

    public BackpackLayer(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> p_i50950_1_) {
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

                if (!cache.backpack.isEmpty() && cache.backpack.getItem() == ModItems.BACKPACK.get()) {
                    ItemStack backpack = cache.backpack;
                    backpackModel.setPlayerModel(getParentModel());
                    matrixStack.pushPose();
                    matrixStack.mulPose(Vector3f.XP.rotationDegrees((float) Math.toDegrees(getParentModel().body.xRot)));
                    if (player.isCrouching()){
                        matrixStack.translate(0,0.147, -0.09375);
                    }
                    IVertexBuilder builder = MAT.buffer(buffer, RenderType::entityCutout);
                    backpackModel.renderToBuffer(matrixStack,builder, p_225628_3_,  OverlayTexture.NO_OVERLAY,1,1,1,1);

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