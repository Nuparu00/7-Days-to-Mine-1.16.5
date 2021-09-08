package nuparu.sevendaystomine.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.player.PlayerEntity;

public class BackpackModel<T extends PlayerEntity> extends BipedModel<T> {
    ModelRenderer BackpackBase;
    ModelRenderer PocketLeft;
    ModelRenderer PocketRight;
    ModelRenderer PocketFront;
    ModelRenderer LeftA;
    ModelRenderer LeftB;
    ModelRenderer LeftC;
    ModelRenderer LeftD;
    ModelRenderer LeftE;
    ModelRenderer RightA;
    ModelRenderer RightB;
    ModelRenderer RightC;
    ModelRenderer RightD;
    ModelRenderer RightE;
    BipedModel playerModel;

    public BackpackModel(float p_i1148_1_) {
        super(p_i1148_1_);

        BackpackBase = new ModelRenderer(this, 46, 20);
        BackpackBase.addBox(-3F, 0F, 2F, 6, 9, 3);
        BackpackBase.setPos(0F, 0F, 0F);
        BackpackBase.setTexSize(64, 32);
        BackpackBase.mirror = true;
        setRotation(BackpackBase, 0F, 0F, 0F);
        PocketLeft = new ModelRenderer(this, 0, 18);
        PocketLeft.addBox(3F, 4F, 2F, 1, 4, 3);
        PocketLeft.setPos(0F, 0F, 0F);
        PocketLeft.setTexSize(64, 32);
        PocketLeft.mirror = true;
        setRotation(PocketLeft, 0F, 0F, 0F);
        PocketRight = new ModelRenderer(this, 0, 25);
        PocketRight.addBox(-4F, 4F, 2F, 1, 4, 3);
        PocketRight.setPos(0F, 0F, 0F);
        PocketRight.setTexSize(64, 32);
        PocketRight.mirror = true;
        setRotation(PocketRight, 0F, 0F, 0F);
        PocketFront = new ModelRenderer(this, 14, 19);
        PocketFront.addBox(-3F, 4F, 5F, 6, 5, 1);
        PocketFront.setPos(0F, 0F, 0F);
        PocketFront.setTexSize(64, 32);
        PocketFront.mirror = true;
        setRotation(PocketFront, 0F, 0F, 0F);
        LeftA = new ModelRenderer(this, 0, 4);
        LeftA.addBox(2.5F, -0.5F, 2F, 1, 3, 1);
        LeftA.setPos(0F, 0F, 0F);
        LeftA.setTexSize(64, 32);
        LeftA.mirror = true;
        setRotation(LeftA, 0F, 0F, 0F);
        LeftB = new ModelRenderer(this, 20, 0);
        LeftB.addBox(2.5F, -0.5F, -3.5F, 1, 1, 5);
        LeftB.setPos(0F, 0F, 1F);
        LeftB.setTexSize(64, 32);
        LeftB.mirror = true;
        setRotation(LeftB, 0F, 0F, 0F);
        LeftC = new ModelRenderer(this, 20, 6);
        LeftC.addBox(2.5F, 0.5F, -2.5F, 1, 3, 1);
        LeftC.setPos(0F, 0F, 0F);
        LeftC.setTexSize(64, 32);
        LeftC.mirror = true;
        setRotation(LeftC, 0F, 0F, 0F);
        LeftD = new ModelRenderer(this, 20, 10);
        LeftD.addBox(3F, 3.5F, -2.5F, 1, 2, 1);
        LeftD.setPos(0F, 0F, 0F);
        LeftD.setTexSize(64, 32);
        LeftD.mirror = true;
        setRotation(LeftD, 0F, 0F, 0F);
        LeftE = new ModelRenderer(this, 20, 13);
        LeftE.addBox(3.5F, 4.5F, -2F, 1, 1, 4);
        LeftE.setPos(0F, 0F, 0F);
        LeftE.setTexSize(64, 32);
        LeftE.mirror = true;
        setRotation(LeftE, 0F, 0F, 0F);
        RightA = new ModelRenderer(this, 0, 0);
        RightA.addBox(-3.5F, -0.5F, 2F, 1, 3, 1);
        RightA.setPos(0F, 0F, 0F);
        RightA.setTexSize(64, 32);
        RightA.mirror = true;
        setRotation(RightA, 0F, 0F, 0F);
        RightB = new ModelRenderer(this, 40, 0);
        RightB.addBox(-3.5F, -0.5F, -2.5F, 1, 1, 5);
        RightB.setPos(0F, 0F, 0F);
        RightB.setTexSize(64, 32);
        RightB.mirror = true;
        setRotation(RightB, 0F, 0F, 0F);
        RightC = new ModelRenderer(this, 40, 6);
        RightC.addBox(-3.5F, 0.5F, -2.5F, 1, 3, 1);
        RightC.setPos(0F, 0F, 0F);
        RightC.setTexSize(64, 32);
        RightC.mirror = true;
        setRotation(RightC, 0F, 0F, 0F);
        RightD = new ModelRenderer(this, 40, 10);
        RightD.addBox(-4F, 3.5F, -2.5F, 1, 2, 1);
        RightD.setPos(0F, 0F, 0F);
        RightD.setTexSize(64, 32);
        RightD.mirror = true;
        setRotation(RightD, 0F, 0F, 0F);
        RightE = new ModelRenderer(this, 40, 13);
        RightE.addBox(-4.5F, 4.5F, -2F, 1, 1, 4);
        RightE.setPos(0F, 0F, 0F);
        RightE.setTexSize(64, 32);
        RightE.mirror = true;
        setRotation(RightE, 0F, 0F, 0F);

        this.head.visible = false;
        this.hat.visible = false;
        this.body.visible = false;
        this.rightArm.visible = false;
        this.leftArm.visible = false;
        this.rightLeg.visible = false;
        this.leftLeg.visible = false;

        this.BackpackBase.addChild(PocketLeft);
        this.BackpackBase.addChild(PocketRight);
        this.BackpackBase.addChild(PocketFront);
        this.BackpackBase.addChild(LeftA);
        this.BackpackBase.addChild(LeftB);
        this.BackpackBase.addChild(LeftC);
        this.BackpackBase.addChild(LeftD);
        this.BackpackBase.addChild(LeftE);
        this.BackpackBase.addChild(RightA);
        this.BackpackBase.addChild(RightB);
        this.BackpackBase.addChild(RightC);
        this.BackpackBase.addChild(RightD);
        this.BackpackBase.addChild(RightE);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
                          float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        BackpackBase.x = body.x;
        BackpackBase.y = body.y;
        BackpackBase.z = body.z;
        BackpackBase.xRot = body.xRot;
        BackpackBase.yRot = body.yRot;
        BackpackBase.zRot = body.zRot;
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        BackpackBase.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setPlayerModel(BipedModel playerModel){
        this.playerModel = playerModel;
    }

}
