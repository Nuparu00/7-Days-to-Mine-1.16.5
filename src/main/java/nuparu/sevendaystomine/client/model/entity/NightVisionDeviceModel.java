package nuparu.sevendaystomine.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.player.PlayerEntity;

public class NightVisionDeviceModel<T extends PlayerEntity> extends BipedModel<T> {
    public static final NightVisionDeviceModel INSTANCE = new NightVisionDeviceModel(0.3f);

    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
    ModelRenderer Shape6;
    ModelRenderer Shape7;
    ModelRenderer Shape8;
    ModelRenderer Shape9;
    ModelRenderer Shape10;
    ModelRenderer Shape11;
    ModelRenderer Shape12;
    ModelRenderer Shape13;
    ModelRenderer Shape14;

    BipedModel playerModel;

    public NightVisionDeviceModel(float p_i1148_1_) {
        super(p_i1148_1_);

        Shape1 = new ModelRenderer(this, 0, 0);
        Shape1.addBox(-4F, -5F, -6F, 8, 2, 2);
        Shape1.setPos(0F, 0F, 0F);
        Shape1.setTexSize(64, 32);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(this, 0, 10);
        Shape2.addBox(-3F, -5F, -7F, 2, 2, 1);
        Shape2.setPos(0F, 0F, 0F);
        Shape2.setTexSize(64, 32);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new ModelRenderer(this, 5, 10);
        Shape3.addBox(1F, -5F, -7F, 2, 2, 1);
        Shape3.setPos(0F, 0F, 0F);
        Shape3.setTexSize(64, 32);
        Shape3.mirror = true;
        setRotation(Shape3, 0F, 0F, 0F);
        Shape4 = new ModelRenderer(this, 0, 20);
        Shape4.addBox(-2F, -6F, -6F, 4, 1, 2);
        Shape4.setPos(0F, 0F, 0F);
        Shape4.setTexSize(64, 32);
        Shape4.mirror = true;
        setRotation(Shape4, 0F, 0F, 0F);
        Shape5 = new ModelRenderer(this, 0, 15);
        Shape5.addBox(-0.5F, -6F, -7F, 1, 1, 1);
        Shape5.setPos(0F, 0F, 0F);
        Shape5.setTexSize(64, 32);
        Shape5.mirror = true;
        setRotation(Shape5, 0F, 0F, 0F);
        Shape6 = new ModelRenderer(this, 30, 0);
        Shape6.addBox(-5F, -4.5F, -5F, 1, 1, 10);
        Shape6.setPos(0F, 0F, 0F);
        Shape6.setTexSize(64, 32);
        Shape6.mirror = true;
        setRotation(Shape6, 0F, 0F, 0F);
        Shape7 = new ModelRenderer(this, 30, 0);
        Shape7.addBox(4F, -4.5F, -5F, 1, 1, 10);
        Shape7.setPos(0F, 0F, 0F);
        Shape7.setTexSize(64, 32);
        Shape7.mirror = true;
        setRotation(Shape7, 0F, 0F, 0F);
        Shape8 = new ModelRenderer(this, 30, 15);
        Shape8.addBox(-4F, -4.5F, 4F, 8, 1, 1);
        Shape8.setPos(0F, 0F, 0F);
        Shape8.setTexSize(64, 32);
        Shape8.mirror = true;
        setRotation(Shape8, 0F, 0F, 0F);
        Shape9 = new ModelRenderer(this, 0, 24);
        Shape9.addBox(-0.5F, -8F, -5F, 1, 2, 1);
        Shape9.setPos(0F, 0F, 0F);
        Shape9.setTexSize(64, 32);
        Shape9.mirror = true;
        setRotation(Shape9, 0F, 0F, 0F);
        Shape10 = new ModelRenderer(this, 30, 20);
        Shape10.addBox(-0.5F, -8F, -4F, 1, 1, 8);
        Shape10.setPos(0F, 0F, 0F);
        Shape10.setTexSize(64, 32);
        Shape10.mirror = true;
        setRotation(Shape10, 0F, 0F, 0F);
        Shape11 = new ModelRenderer(this, 15, 15);
        Shape11.addBox(-0.5F, -8F, 4F, 1, 4, 1);
        Shape11.setPos(0F, 0F, 0F);
        Shape11.setTexSize(64, 32);
        Shape11.mirror = true;
        setRotation(Shape11, 0F, 0F, 0F);
        Shape12 = new ModelRenderer(this, 15, 15);
        Shape12.addBox(-5F, -8F, -0.5F, 1, 4, 1);
        Shape12.setPos(0F, 0F, 0F);
        Shape12.setTexSize(64, 32);
        Shape12.mirror = true;
        setRotation(Shape12, 0F, 0F, 0F);
        Shape13 = new ModelRenderer(this, 15, 15);
        Shape13.addBox(4F, -8F, -0.5F, 1, 4, 1);
        Shape13.setPos(0F, 0F, 0F);
        Shape13.setTexSize(64, 32);
        Shape13.mirror = true;
        setRotation(Shape13, 0F, 0F, 0F);
        Shape14 = new ModelRenderer(this, 30, 15);
        Shape14.addBox(-4F, -8F, -0.5F, 8, 1, 1);
        Shape14.setPos(0F, 0F, 0F);
        Shape14.setTexSize(64, 32);
        Shape14.mirror = true;
        setRotation(Shape14, 0F, 0F, 0F);

        this.head.visible = false;
        this.hat.visible = false;
        this.body.visible = false;
        this.rightArm.visible = false;
        this.leftArm.visible = false;
        this.rightLeg.visible = false;
        this.leftLeg.visible = false;

        this.Shape1.addChild(Shape2);
        this.Shape1.addChild(Shape3);
        this.Shape1.addChild(Shape4);
        this.Shape1.addChild(Shape5);
        this.Shape1.addChild(Shape6);
        this.Shape1.addChild(Shape7);
        this.Shape1.addChild(Shape8);
        this.Shape1.addChild(Shape9);
        this.Shape1.addChild(Shape10);
        this.Shape1.addChild(Shape11);
        this.Shape1.addChild(Shape12);
        this.Shape1.addChild(Shape13);
        this.Shape1.addChild(Shape14);
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
        Shape1.x = head.x;
        Shape1.y = head.y;
        Shape1.z = head.z;
        Shape1.xRot = head.xRot;
        Shape1.yRot = head.yRot;
        Shape1.zRot = head.zRot;
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        Shape1.x = head.x;
        Shape1.y = head.y;
        Shape1.z = head.z;
        Shape1.xRot = head.xRot;
        Shape1.yRot = head.yRot;
        Shape1.zRot = head.zRot;
        Shape1.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setPlayerModel(BipedModel playerModel){
        this.playerModel = playerModel;
    }

}
