package nuparu.sevendaystomine.client.model.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;

public class AmbulanceModel extends TileEntityModel {

    private final ModelRenderer bone;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;
    private final ModelRenderer bone6;
    private final ModelRenderer bone7;
    private final ModelRenderer bone8;
    private final ModelRenderer bone9;
    private final ModelRenderer bone10;
    private final ModelRenderer bone11;
    private final ModelRenderer bone12;
    private final ModelRenderer bone13;

    public AmbulanceModel(){
        bone = new ModelRenderer(208,208,0,0);
        bone.setPos(0.0F, 16.0F, -31.0F);
        setRotationAngle(bone, 0.2618F, 0.0F, 0.0F);
        bone.texOffs(0, 106).addBox(-14.0F, -5.899F, -9.8284F, 28.0F, 2.0F, 11.0F, 0.0F, false);

        bone2 = new ModelRenderer(208,208,0,0);
        bone2.setPos(13.0F, 13.5F, -14.0F);
        setRotationAngle(bone2, -0.7854F, 0.0F, 0.0F);
        bone2.texOffs(8, 144).addBox(-1.01F, -11.9645F, -10.1924F, 2.0F, 18.0F, 2.0F, 0.0F, false);
        bone2.texOffs(0, 144).addBox(-26.99F, -11.9645F, -10.1924F, 2.0F, 18.0F, 2.0F, 0.0F, false);

        bone3 = new ModelRenderer(208,208,0,0);
        bone3.setPos(13.0F, 15.9F, 28.0F);
        setRotationAngle(bone3, 0.9599F, 0.0F, 0.0F);


        bone4 = new ModelRenderer(208,208,0,0);
        bone4.setPos(13.5F, 22.5F, -13.0F);
        bone4.texOffs(0, 67).addBox(-0.5F, -12.5F, -9.0F, 1.0F, 9.0F, 15.0F, 0.0F, false);
        bone4.texOffs(8, 36).addBox(-0.5F, -21.5F, 5.0F, 1.0F, 9.0F, 1.0F, 0.0F, false);
        bone4.texOffs(8, 0).addBox(-27.5F, -21.5F, 5.0F, 1.0F, 9.0F, 1.0F, 0.0F, false);
        bone4.texOffs(72, 119).addBox(-27.55F, -24.5F, 0.0F, 28.0F, 3.0F, 6.0F, 0.0F, false);
        bone4.texOffs(29, 30).addBox(-7.5F, -32.5F, 11.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone4.texOffs(140, 122).addBox(-24.5F, -34.5F, 10.0F, 22.0F, 2.0F, 4.0F, 0.0F, false);
        bone4.texOffs(0, 28).addBox(-20.5F, -32.5F, 11.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        bone5 = new ModelRenderer(208,208,0,0);
        bone5.setPos(0.0F, -16.5F, 8.5F);
        bone4.addChild(bone5);
        setRotationAngle(bone5, -0.7854F, 0.0F, 0.0F);
        bone5.texOffs(42, 30).addBox(-0.53F, 0.5355F, -9.6924F, 1.0F, 15.0F, 1.0F, 0.0F, false);

        bone6 = new ModelRenderer(208,208,0,0);
        bone6.setPos(-13.5F, 22.5F, -13.0F);
        bone6.texOffs(0, 0).addBox(-0.5F, -12.5F, -9.0F, 1.0F, 9.0F, 15.0F, 0.0F, false);

        bone7 = new ModelRenderer(208,208,0,0);
        bone7.setPos(0.0F, -16.5F, 8.5F);
        bone6.addChild(bone7);
        setRotationAngle(bone7, -0.7854F, 0.0F, 0.0F);
        bone7.texOffs(38, 30).addBox(-0.47F, 0.5355F, -9.6924F, 1.0F, 15.0F, 1.0F, 0.0F, false);

        bone8 = new ModelRenderer(208,208,0,0);
        bone8.setPos(13.5F, 22.5F, 2.0F);
        bone8.texOffs(93, 67).addBox(-27.5F, -11.5F, -9.0F, 28.0F, 8.0F, 21.0F, 0.0F, false);

        bone9 = new ModelRenderer(208,208,0,0);
        bone9.setPos(0.0F, -16.5F, 7.5F);
        bone8.addChild(bone9);
        setRotationAngle(bone9, 0.9599F, 0.0F, 0.0F);


        bone10 = new ModelRenderer(208,208,0,0);
        bone10.setPos(-13.5F, 22.5F, 2.0F);


        bone11 = new ModelRenderer(208,208,0,0);
        bone11.setPos(0.0F, -16.5F, 7.5F);
        bone10.addChild(bone11);
        setRotationAngle(bone11, 0.9599F, 0.0F, 0.0F);


        bone12 = new ModelRenderer(208,208,0,0);
        bone12.setPos(7.0F, 17.5F, -13.0F);
        setRotationAngle(bone12, 0.6109F, 0.0F, 0.0F);
        bone12.texOffs(12, 36).addBox(-0.5F, -8.9388F, -5.0781F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone12.texOffs(0, 24).addBox(-1.5F, -9.9388F, -3.0781F, 3.0F, 3.0F, 1.0F, 0.0F, false);

        bone13 = new ModelRenderer(208,208,0,0);
        bone13.setPos(0.0F, 24.0F, 0.0F);
        bone13.texOffs(124, 142).addBox(-12.0F, -8.0F, -18.0F, 10.0F, 3.0F, 9.0F, 0.0F, false);
        bone13.texOffs(0, 24).addBox(2.0F, -8.0F, -18.0F, 10.0F, 3.0F, 9.0F, 0.0F, false);
        bone13.texOffs(0, 91).addBox(-12.0F, -17.0F, -11.0F, 10.0F, 9.0F, 3.0F, 0.0F, false);
        bone13.texOffs(17, 0).addBox(2.0F, -17.0F, -11.0F, 10.0F, 9.0F, 3.0F, 0.0F, false);
        bone13.texOffs(17, 67).addBox(-14.0F, -8.0F, 15.0F, 3.0F, 6.0F, 6.0F, 0.0F, false);
        bone13.texOffs(93, 67).addBox(11.0F, -8.0F, 15.0F, 3.0F, 6.0F, 6.0F, 0.0F, false);
        bone13.texOffs(29, 18).addBox(-14.0F, -8.0F, -30.0F, 3.0F, 6.0F, 6.0F, 0.0F, false);
        bone13.texOffs(103, 0).addBox(11.0F, -8.0F, -30.0F, 3.0F, 6.0F, 6.0F, 0.0F, false);
        bone13.texOffs(0, 119).addBox(-14.0F, -13.0F, 14.0F, 28.0F, 3.0F, 8.0F, 0.0F, false);
        bone13.texOffs(78, 106).addBox(-14.0F, -14.0F, -32.0F, 28.0F, 4.0F, 9.0F, 0.0F, false);
        bone13.texOffs(64, 128).addBox(-11.0F, -10.0F, 14.0F, 22.0F, 6.0F, 8.0F, 0.0F, false);
        bone13.texOffs(124, 128).addBox(-11.0F, -10.0F, -31.0F, 22.0F, 6.0F, 8.0F, 0.0F, false);
        bone13.texOffs(0, 67).addBox(-14.0F, -5.0F, -23.0F, 28.0F, 2.0F, 37.0F, 0.0F, false);
        bone13.texOffs(0, 0).addBox(-14.0F, -33.0F, -7.0F, 28.0F, 20.0F, 47.0F, 0.0F, false);
        bone13.texOffs(103, 28).addBox(-14.0F, -9.0F, -42.0F, 28.0F, 6.0F, 11.0F, 0.0F, false);
        bone13.texOffs(17, 12).addBox(-6.0F, -9.0F, -43.0F, 12.0F, 1.0F, 1.0F, 0.0F, false);
        bone13.texOffs(0, 36).addBox(-7.0F, -12.0F, -44.0F, 1.0F, 7.0F, 3.0F, 0.0F, false);
        bone13.texOffs(32, 14).addBox(-13.0F, -10.0F, -43.0F, 5.0F, 3.0F, 1.0F, 0.0F, false);
        bone13.texOffs(0, 10).addBox(8.0F, -10.0F, -43.0F, 5.0F, 3.0F, 1.0F, 0.0F, false);
        bone13.texOffs(0, 0).addBox(6.0F, -12.0F, -44.0F, 1.0F, 7.0F, 3.0F, 0.0F, false);
        bone13.texOffs(93, 96).addBox(-14.0F, -12.0F, -36.0F, 28.0F, 3.0F, 5.0F, 0.0F, false);
        bone13.texOffs(0, 137).addBox(-14.0F, -11.0F, -41.0F, 28.0F, 2.0F, 5.0F, 0.0F, false);
        bone13.texOffs(134, 119).addBox(-14.0F, -11.0F, -42.0F, 28.0F, 2.0F, 1.0F, 0.0F, false);
        bone13.texOffs(103, 0).addBox(-14.0F, -13.0F, 22.0F, 28.0F, 10.0F, 18.0F, 0.0F, false);
        bone13.texOffs(0, 130).addBox(-14.0F, -5.0F, 40.0F, 28.0F, 2.0F, 3.0F, 0.0F, false);
        bone13.texOffs(66, 142).addBox(-14.0F, -14.0F, -23.0F, 28.0F, 9.0F, 1.0F, 0.0F, false);
    }

    private void setRotationAngle(ModelRenderer model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    public void render(MatrixStack matrixStack, IVertexBuilder builder, int combinedLight, int combinedOverlay) {
        bone.render(matrixStack, builder, combinedLight, combinedOverlay);
        bone2.render(matrixStack, builder, combinedLight, combinedOverlay);
        bone3.render(matrixStack, builder, combinedLight, combinedOverlay);
        bone4.render(matrixStack, builder, combinedLight, combinedOverlay);
        bone6.render(matrixStack, builder, combinedLight, combinedOverlay);
        bone8.render(matrixStack, builder, combinedLight, combinedOverlay);
        bone10.render(matrixStack, builder, combinedLight, combinedOverlay);
        bone12.render(matrixStack, builder, combinedLight, combinedOverlay);
        bone13.render(matrixStack, builder, combinedLight, combinedOverlay);
    }
}
