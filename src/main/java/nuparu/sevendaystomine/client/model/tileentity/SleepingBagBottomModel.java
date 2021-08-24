package nuparu.sevendaystomine.client.model.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;

public class SleepingBagBottomModel extends TileEntityModel {


    ModelRenderer LowerA;
    ModelRenderer LowerB;

    public SleepingBagBottomModel() {
        LowerA = new ModelRenderer(64, 64, 0, 16);
        LowerA.addBox(0F, 0F, 0F, 14, 1, 15);
        LowerA.setPos(-7F, 23F, -7F);
        LowerA.mirror = true;
        setRotation(LowerA, 0F, 0F, 0F);
        LowerB = new ModelRenderer(64, 64, 0, 40);
        LowerB.addBox(0F, 0F, 0F, 12, 1, 14);
        LowerB.setPos(-6F, 22F, -6F);
        LowerB.mirror = true;
        setRotation(LowerB, 0F, 0F, 0F);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    public void render(MatrixStack matrixStack, IVertexBuilder builder, int combinedLight, int combinedOverlay) {
        LowerA.render(matrixStack, builder, combinedLight, combinedOverlay);
        LowerB.render(matrixStack, builder, combinedLight, combinedOverlay);
    }
}
