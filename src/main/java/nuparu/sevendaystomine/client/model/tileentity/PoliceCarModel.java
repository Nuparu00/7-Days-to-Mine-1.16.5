package nuparu.sevendaystomine.client.model.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;

public class PoliceCarModel extends TileEntityModel {

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

    public PoliceCarModel(){
        bone = new ModelRenderer(176,176,0,0);
        bone.setPos(0.0F, 16.0F, -31.0F);
        setRotationAngle(bone, 0.2618F, 0.0F, 0.0F);
        bone.texOffs(67, 67).addBox(-14.0F, -3.8978F, -6.2235F, 28.0F, 2.0F, 11.0F, 0.0F, false);

        bone2 = new ModelRenderer(176,176,0,0);
        bone2.setPos(13.0F, 13.5F, -14.0F);
        setRotationAngle(bone2, -0.7854F, 0.0F, 0.0F);
        bone2.texOffs(8, 43).addBox(-1.01F, -14.6213F, -3.1213F, 2.0F, 15.0F, 2.0F, 0.0F, false);
        bone2.texOffs(0, 43).addBox(-26.99F, -14.6213F, -3.1213F, 2.0F, 15.0F, 2.0F, 0.0F, false);

        bone3 = new ModelRenderer(176,176,0,0);
        bone3.setPos(13.0F, 15.9F, 28.0F);
        setRotationAngle(bone3, 0.9599F, 0.0F, 0.0F);
        bone3.texOffs(130, 130).addBox(-1.01F, -22.6207F, 0.4575F, 2.0F, 20.0F, 2.0F, 0.0F, false);
        bone3.texOffs(107, 117).addBox(-25.0F, -12.6207F, 0.4575F, 24.0F, 10.0F, 2.0F, 0.0F, false);
        bone3.texOffs(32, 0).addBox(-26.99F, -22.6207F, 0.4575F, 2.0F, 20.0F, 2.0F, 0.0F, false);

        bone4 = new ModelRenderer(176,176,0,0);
        bone4.setPos(13.5F, 22.5F, -13.0F);
        bone4.texOffs(49, 128).addBox(-0.5F, -11.5F, 0.0F, 1.0F, 9.0F, 15.0F, 0.0F, false);
        bone4.texOffs(67, 62).addBox(-0.5F, -19.5F, 14.0F, 1.0F, 8.0F, 1.0F, 0.0F, false);
        bone4.texOffs(0, 142).addBox(-0.52F, -19.5F, 7.0F, 1.0F, 1.0F, 7.0F, 0.0F, false);

        bone5 = new ModelRenderer(176,176,0,0);
        bone5.setPos(0.0F, -16.5F, 8.5F);
        bone4.addChild(bone5);
        setRotationAngle(bone5, -0.7854F, 0.0F, 0.0F);
        bone5.texOffs(25, 0).addBox(-0.53F, -2.1213F, -2.6213F, 1.0F, 12.0F, 1.0F, 0.0F, false);

        bone6 = new ModelRenderer(176,176,0,0);
        bone6.setPos(-13.5F, 22.5F, -13.0F);
        bone6.texOffs(32, 118).addBox(-0.5F, -11.5F, 0.0F, 1.0F, 9.0F, 15.0F, 0.0F, false);
        bone6.texOffs(38, 33).addBox(-0.5F, -19.5F, 14.0F, 1.0F, 8.0F, 1.0F, 0.0F, false);
        bone6.texOffs(99, 141).addBox(-0.48F, -19.5F, 7.0F, 1.0F, 1.0F, 7.0F, 0.0F, false);

        bone7 = new ModelRenderer(176,176,0,0);
        bone7.setPos(0.0F, -16.5F, 8.5F);
        bone6.addChild(bone7);
        setRotationAngle(bone7, -0.7854F, 0.0F, 0.0F);
        bone7.texOffs(8, 0).addBox(-0.47F, -2.1213F, -2.6213F, 1.0F, 12.0F, 1.0F, 0.0F, false);

        bone8 = new ModelRenderer(176,176,0,0);
        bone8.setPos(13.5F, 22.5F, 2.0F);
        bone8.texOffs(0, 118).addBox(-0.5F, -11.5F, 0.0F, 1.0F, 9.0F, 15.0F, 0.0F, false);
        bone8.texOffs(4, 24).addBox(-0.5F, -19.5F, 0.0F, 1.0F, 8.0F, 1.0F, 0.0F, false);
        bone8.texOffs(138, 129).addBox(-0.52F, -19.5F, 1.0F, 1.0F, 1.0F, 7.0F, 0.0F, false);

        bone9 = new ModelRenderer(176,176,0,0);
        bone9.setPos(0.0F, -16.5F, 7.5F);
        bone8.addChild(bone9);
        setRotationAngle(bone9, 0.9599F, 0.0F, 0.0F);
        bone9.texOffs(4, 62).addBox(-0.53F, -1.7207F, 1.9575F, 1.0F, 9.0F, 1.0F, 0.0F, false);

        bone10 = new ModelRenderer(176,176,0,0);
        bone10.setPos(-13.5F, 22.5F, 2.0F);
        bone10.texOffs(0, 0).addBox(-0.5F, -11.5F, 0.0F, 1.0F, 9.0F, 15.0F, 0.0F, false);
        bone10.texOffs(0, 24).addBox(-0.5F, -19.5F, 0.0F, 1.0F, 8.0F, 1.0F, 0.0F, false);
        bone10.texOffs(136, 41).addBox(-0.48F, -19.5F, 1.0F, 1.0F, 1.0F, 7.0F, 0.0F, false);

        bone11 = new ModelRenderer(176,176,0,0);
        bone11.setPos(0.0F, -16.5F, 7.5F);
        bone10.addChild(bone11);
        setRotationAngle(bone11, 0.9599F, 0.0F, 0.0F);
        bone11.texOffs(0, 62).addBox(-0.47F, -1.7207F, 1.9575F, 1.0F, 9.0F, 1.0F, 0.0F, false);

        bone12 = new ModelRenderer(176,176,0,0);
        bone12.setPos(7.0F, 17.5F, -13.0F);
        setRotationAngle(bone12, 0.6109F, 0.0F, 0.0F);
        bone12.texOffs(17, 10).addBox(-0.5F, -2.9575F, 1.7207F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone12.texOffs(0, 10).addBox(-1.5F, -3.9575F, 3.7207F, 3.0F, 3.0F, 1.0F, 0.0F, false);

        bone13 = new ModelRenderer(176,176,0,0);
        bone13.setPos(0.0F, 24.0F, 0.0F);
        bone13.texOffs(53, 117).addBox(-13.0F, -14.0F, 2.0F, 26.0F, 10.0F, 1.0F, 0.0F, false);
        bone13.texOffs(120, 109).addBox(-10.0F, -26.0F, 0.0F, 20.0F, 2.0F, 4.0F, 0.0F, false);
        bone13.texOffs(24, 36).addBox(-6.0F, -24.0F, 1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone13.texOffs(21, 11).addBox(5.0F, -24.0F, 1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone13.texOffs(66, 128).addBox(-12.0F, -7.0F, -9.0F, 10.0F, 3.0F, 9.0F, 0.0F, false);
        bone13.texOffs(0, 24).addBox(2.0F, -7.0F, -9.0F, 10.0F, 3.0F, 9.0F, 0.0F, false);
        bone13.texOffs(68, 91).addBox(-12.0F, -7.0F, 6.0F, 24.0F, 3.0F, 9.0F, 0.0F, false);
        bone13.texOffs(104, 129).addBox(-12.0F, -16.0F, -2.0F, 10.0F, 9.0F, 3.0F, 0.0F, false);
        bone13.texOffs(17, 118).addBox(2.0F, -16.0F, -2.0F, 10.0F, 9.0F, 3.0F, 0.0F, false);
        bone13.texOffs(0, 106).addBox(-12.0F, -16.0F, 13.0F, 24.0F, 9.0F, 3.0F, 0.0F, false);
        bone13.texOffs(0, 43).addBox(-14.0F, -23.0F, -6.0F, 28.0F, 2.0F, 17.0F, 0.0F, false);
        bone13.texOffs(138, 138).addBox(-14.0F, -8.0F, 24.0F, 3.0F, 6.0F, 6.0F, 0.0F, false);
        bone13.texOffs(139, 74).addBox(11.0F, -8.0F, 24.0F, 3.0F, 6.0F, 6.0F, 0.0F, false);
        bone13.texOffs(134, 91).addBox(-14.0F, -8.0F, -26.0F, 3.0F, 6.0F, 6.0F, 0.0F, false);
        bone13.texOffs(81, 140).addBox(11.0F, -8.0F, -26.0F, 3.0F, 6.0F, 6.0F, 0.0F, false);
        bone13.texOffs(74, 80).addBox(-14.0F, -12.0F, 23.0F, 28.0F, 3.0F, 8.0F, 0.0F, false);
        bone13.texOffs(0, 78).addBox(-14.0F, -13.0F, -28.0F, 28.0F, 4.0F, 9.0F, 0.0F, false);
        bone13.texOffs(98, 21).addBox(-11.0F, -9.0F, 23.0F, 22.0F, 6.0F, 8.0F, 0.0F, false);
        bone13.texOffs(60, 103).addBox(-11.0F, -9.0F, -27.0F, 22.0F, 6.0F, 8.0F, 0.0F, false);
        bone13.texOffs(112, 103).addBox(-14.0F, -15.0F, 19.0F, 28.0F, 1.0F, 2.0F, 0.0F, false);
        bone13.texOffs(98, 35).addBox(-14.0F, -17.0F, 16.0F, 28.0F, 3.0F, 3.0F, 0.0F, false);
        bone13.texOffs(0, 0).addBox(-14.0F, -4.0F, -19.0F, 28.0F, 1.0F, 42.0F, 0.0F, false);
        bone13.texOffs(0, 62).addBox(-14.0F, -8.0F, -38.0F, 28.0F, 5.0F, 11.0F, 0.0F, false);
        bone13.texOffs(0, 36).addBox(-6.0F, -8.0F, -39.0F, 12.0F, 1.0F, 1.0F, 0.0F, false);
        bone13.texOffs(17, 0).addBox(-7.0F, -11.0F, -40.0F, 1.0F, 7.0F, 3.0F, 0.0F, false);
        bone13.texOffs(29, 24).addBox(-13.0F, -9.0F, -39.0F, 5.0F, 3.0F, 1.0F, 0.0F, false);
        bone13.texOffs(29, 29).addBox(8.0F, -9.0F, -39.0F, 5.0F, 3.0F, 1.0F, 0.0F, false);
        bone13.texOffs(0, 0).addBox(6.0F, -11.0F, -40.0F, 1.0F, 7.0F, 3.0F, 0.0F, false);
        bone13.texOffs(98, 13).addBox(-14.0F, -11.0F, -32.0F, 28.0F, 3.0F, 5.0F, 0.0F, false);
        bone13.texOffs(85, 60).addBox(-14.0F, -10.0F, -37.0F, 28.0F, 2.0F, 5.0F, 0.0F, false);
        bone13.texOffs(112, 106).addBox(-14.0F, -10.0F, -38.0F, 28.0F, 2.0F, 1.0F, 0.0F, false);
        bone13.texOffs(98, 0).addBox(-14.0F, -12.0F, 31.0F, 28.0F, 9.0F, 4.0F, 0.0F, false);
        bone13.texOffs(0, 91).addBox(-14.0F, -13.0F, -19.0F, 28.0F, 9.0F, 6.0F, 0.0F, false);
        bone13.texOffs(73, 43).addBox(-14.0F, -14.0F, 16.0F, 28.0F, 10.0F, 7.0F, 0.0F, false);
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
