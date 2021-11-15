package nuparu.sevendaystomine.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.StandingSignBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.tileentity.SignTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.vector.Vector3f;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.tileentity.TileEntityStreetSign;

import java.util.List;

public class TileEntityStreetSignRenderer extends TileEntityRenderer<TileEntityStreetSign> {
    private final SignTileEntityRenderer.SignModel signModel = new SignTileEntityRenderer.SignModel();

    public TileEntityStreetSignRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);

    }


    @Override
    public void render(TileEntityStreetSign te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        BlockState blockstate = te.getBlockState();
        matrixStack.pushPose();
        float f = 0.6666667F;
        if (blockstate.getBlock() instanceof StandingSignBlock) {
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            float f1 = -((float)(blockstate.getValue(StandingSignBlock.ROTATION) * 360) / 16.0F);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(f1));
            this.signModel.stick.visible = true;
        } else {
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            float f4 = -blockstate.getValue(WallSignBlock.FACING).toYRot();
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(f4));
            matrixStack.translate(0.0D, -0.3125D, -0.4375D);
            this.signModel.stick.visible = false;
        }

        matrixStack.pushPose();
        matrixStack.scale(0.6666667F, -0.6666667F, -0.6666667F);
        IVertexBuilder ivertexbuilder = Atlases.signTexture(SevenDaysToMine.STREET_WOOD_TYPE).buffer(buffer, this.signModel::renderType);
        this.signModel.sign.render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay);
        this.signModel.stick.render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay);
        matrixStack.popPose();
        FontRenderer fontrenderer = this.renderer.getFont();
        float f2 = 0.010416667F;
        matrixStack.translate(0.0D, 0.33333334F, 0.046666667F);
        matrixStack.scale(0.010416667F, -0.010416667F, 0.010416667F);
        int i = te.getColor().getTextColor();
        double d0 = 0.4D;
        int j = (int)((double) NativeImage.getR(i) * 0.4D);
        int k = (int)((double)NativeImage.getG(i) * 0.4D);
        int l = (int)((double)NativeImage.getB(i) * 0.4D);
        int i1 = NativeImage.combine(0, l, k, j);
        int j1 = 20;

        for(int k1 = 0; k1 < 4; ++k1) {
            IReorderingProcessor ireorderingprocessor = te.getRenderMessage(k1, (p_243502_1_) -> {
                List<IReorderingProcessor> list = fontrenderer.split(p_243502_1_, 90);
                return list.isEmpty() ? IReorderingProcessor.EMPTY : list.get(0);
            });
            if (ireorderingprocessor != null) {
                float f3 = (float)(-fontrenderer.width(ireorderingprocessor) / 2);
                fontrenderer.drawInBatch(ireorderingprocessor, f3, (float)(k1 * 10 - 20), i1, false, matrixStack.last().pose(), buffer, false, 0, combinedLight);
            }
        }

        matrixStack.popPose();
        }
}
