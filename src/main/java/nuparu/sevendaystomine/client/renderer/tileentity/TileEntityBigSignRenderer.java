package nuparu.sevendaystomine.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallSignBlock;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.vector.Vector3f;
import nuparu.sevendaystomine.tileentity.TileEntityBigSignMaster;

import java.util.List;

public class TileEntityBigSignRenderer extends TileEntityRenderer<TileEntityBigSignMaster> {

    public TileEntityBigSignRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);

    }


    @Override
    public void render(TileEntityBigSignMaster te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        BlockState blockstate = te.getBlockState();
        matrixStack.pushPose();
        float f = 0.06666667F;
        matrixStack.translate(0.5D, 0.5D, 0.5D);
        float f4 = -blockstate.getValue(WallSignBlock.FACING).toYRot();
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(f4));
        matrixStack.translate(0.0D, -0.3125D, -0.4375D);

        FontRenderer fontrenderer = this.renderer.getFont();
        float f2 = 0.010416667F;
        matrixStack.translate(0.0D, 0.33333334F, 0.046666667F);
        matrixStack.scale(f, -f, f);
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
