package nuparu.sevendaystomine.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import nuparu.sevendaystomine.entity.AirdropEntity;

public class AirdropModel extends EntityModel<AirdropEntity> {
    private final ModelRenderer parachute;
    private final ModelRenderer ropes1;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer tarp;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer ropes2;
    private final ModelRenderer cube_r5;
    private final ModelRenderer cube_r6;
    private final ModelRenderer ropes3;
    private final ModelRenderer cube_r7;
    private final ModelRenderer cube_r8;
    private final ModelRenderer ropes4;
    private final ModelRenderer cube_r9;
    private final ModelRenderer cube_r10;
    private final ModelRenderer bb_main;

    public AirdropModel() {
        texWidth = 128;
        texHeight = 128;

        parachute = new ModelRenderer(this);
        parachute.setPos(0.0F, 24.0F, 0.0F);


        ropes1 = new ModelRenderer(this);
        ropes1.setPos(0.0F, 0.0F, 0.0F);
        parachute.addChild(ropes1);


        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(6.5F, -16.0F, -6.5F);
        ropes1.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0873F, 0.0F, 0.1571F);
        cube_r1.texOffs(0, 77).addBox(-0.5F, -33.0F, -0.5F, 1.0F, 33.0F, 1.0F, 0.0F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(6.5F, -16.0F, -6.5F);
        ropes1.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.082F, -0.0298F, 0.5049F);
        cube_r2.texOffs(20, 77).addBox(-0.5F, -31.0F, -0.5F, 1.0F, 31.0F, 1.0F, 0.0F, false);

        tarp = new ModelRenderer(this);
        tarp.setPos(0.0F, 0.0F, 0.0F);
        parachute.addChild(tarp);
        tarp.texOffs(0, 0).addBox(-12.0F, -49.0F, -10.0F, 24.0F, 1.0F, 20.0F, 0.0F, false);

        cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(12.0F, -49.0F, 0.0F);
        tarp.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, 0.0F, 0.5236F);
        cube_r3.texOffs(45, 35).addBox(0.0F, 0.0F, -10.0F, 12.0F, 1.0F, 20.0F, 0.0F, false);

        cube_r4 = new ModelRenderer(this);
        cube_r4.setPos(-12.0F, -49.0F, 0.0F);
        tarp.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 0.0F, -0.5236F);
        cube_r4.texOffs(0, 55).addBox(-12.0F, 0.0F, -10.0F, 12.0F, 1.0F, 20.0F, 0.0F, false);

        ropes2 = new ModelRenderer(this);
        ropes2.setPos(0.0F, 0.0F, 13.0F);
        parachute.addChild(ropes2);


        cube_r5 = new ModelRenderer(this);
        cube_r5.setPos(6.5F, -16.0F, -6.5F);
        ropes2.addChild(cube_r5);
        setRotationAngle(cube_r5, -0.0873F, 0.0F, 0.1571F);
        cube_r5.texOffs(75, 57).addBox(-0.5F, -33.0F, -0.5F, 1.0F, 33.0F, 1.0F, 0.0F, false);

        cube_r6 = new ModelRenderer(this);
        cube_r6.setPos(6.5F, -16.0F, -6.5F);
        ropes2.addChild(cube_r6);
        setRotationAngle(cube_r6, -0.082F, -0.0298F, 0.5049F);
        cube_r6.texOffs(15, 77).addBox(-0.5F, -31.0F, -0.5F, 1.0F, 31.0F, 1.0F, 0.0F, false);

        ropes3 = new ModelRenderer(this);
        ropes3.setPos(-13.0F, 0.0F, 0.0F);
        parachute.addChild(ropes3);


        cube_r7 = new ModelRenderer(this);
        cube_r7.setPos(6.5F, -16.0F, -6.5F);
        ropes3.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0873F, 0.0F, -0.1571F);
        cube_r7.texOffs(70, 57).addBox(-0.5F, -33.0F, -0.5F, 1.0F, 33.0F, 1.0F, 0.0F, false);

        cube_r8 = new ModelRenderer(this);
        cube_r8.setPos(6.5F, -16.0F, -6.5F);
        ropes3.addChild(cube_r8);
        setRotationAngle(cube_r8, 0.082F, -0.0298F, -0.5049F);
        cube_r8.texOffs(10, 77).addBox(-0.5F, -31.0F, -0.5F, 1.0F, 31.0F, 1.0F, 0.0F, false);

        ropes4 = new ModelRenderer(this);
        ropes4.setPos(0.0F, 0.0F, 13.0F);
        parachute.addChild(ropes4);


        cube_r9 = new ModelRenderer(this);
        cube_r9.setPos(-6.5F, -16.0F, -6.5F);
        ropes4.addChild(cube_r9);
        setRotationAngle(cube_r9, -0.0873F, 0.0F, -0.1571F);
        cube_r9.texOffs(65, 57).addBox(-0.5F, -33.0F, -0.5F, 1.0F, 33.0F, 1.0F, 0.0F, false);

        cube_r10 = new ModelRenderer(this);
        cube_r10.setPos(-6.5F, -16.0F, -6.5F);
        ropes4.addChild(cube_r10);
        setRotationAngle(cube_r10, -0.082F, -0.0298F, -0.5049F);
        cube_r10.texOffs(5, 77).addBox(-0.5F, -31.0F, -0.5F, 1.0F, 31.0F, 1.0F, 0.0F, false);

        bb_main = new ModelRenderer(this);
        bb_main.setPos(0.0F, 24.0F, 0.0F);
        bb_main.texOffs(0, 22).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(AirdropEntity entity, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {

        parachute.visible = (!entity.isOnGround() && !entity.getLanded());

    }

    private void setRotationAngle(ModelRenderer model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        parachute.render(matrixStack, buffer, packedLight, packedOverlay);
        bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
    }
}
