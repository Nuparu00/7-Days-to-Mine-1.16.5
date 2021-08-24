package nuparu.sevendaystomine.client.model.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;

public class SleepingBagHeadModel extends TileEntityModel {

    ModelRenderer UpperA;
    ModelRenderer UpperB;

    public SleepingBagHeadModel() {
        UpperA = new ModelRenderer(64,64, 0, 0);
        UpperA.addBox(0F, 0F, 0F, 14, 1, 15);
        UpperA.setPos(-7F, 23F, -8F);
        UpperA.mirror = true;
        setRotation(UpperA, 0F, 0F, 0F);
        UpperB = new ModelRenderer(64,64, 0, 32);
        UpperB.addBox(0F, 0F, 0F, 12, 1, 7);
        UpperB.setPos(-6F, 22F, -8F);
        UpperB.mirror = true;
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    public void render(MatrixStack matrixStack, IVertexBuilder builder, int combinedLight, int combinedOverlay) {
        UpperA.render(matrixStack, builder, combinedLight, combinedOverlay);
        UpperB.render(matrixStack, builder, combinedLight, combinedOverlay);
    }
}
