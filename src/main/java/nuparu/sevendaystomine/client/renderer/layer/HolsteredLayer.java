package nuparu.sevendaystomine.client.renderer.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.model.entity.HolsterModel;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.util.PlayerInventorySyncHelper;

public class HolsteredLayer extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {
    private final ResourceLocation TEX = new ResourceLocation(SevenDaysToMine.MODID, "entity/backpack");
    private final RenderMaterial MAT = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, TEX);
    HolsterModel holsterModel = new HolsterModel(0);

    public HolsteredLayer(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> p_i50950_1_) {
        super(p_i50950_1_);
    }

    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int p_225628_3_, AbstractClientPlayerEntity player, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {

        if (ServerConfig.renderPlayerInventory.get()) {
            if (PlayerInventorySyncHelper.itemsCache != null) {

                IVertexBuilder builder = MAT.buffer(buffer, RenderType::entityCutout);
                holsterModel.setPlayerModel(getParentModel());
                holsterModel.setBuffer(buffer);
                holsterModel.setupAnim(player,0,0,0,0,0);
                holsterModel.renderToBuffer(matrixStack, builder, p_225628_3_, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

            }
        }
    }

    protected HandSide getAttackArm(PlayerEntity p_217147_1_) {
        HandSide handside = p_217147_1_.getMainArm();
        return p_217147_1_.swingingArm == Hand.MAIN_HAND ? handside : handside.getOpposite();
    }

}