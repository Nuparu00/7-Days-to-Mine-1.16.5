package nuparu.sevendaystomine.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.player.PlayerEntity;

public class BeretModel<T extends PlayerEntity> extends BipedModel<T> {
    public static final BeretModel INSTANCE = new BeretModel(0.3f);

    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
    ModelRenderer Shape6;
    ModelRenderer Shape7;
    ModelRenderer Shape8;
    ModelRenderer Shape9;

    BipedModel playerModel;

    public BeretModel(float p_i1148_1_) {
        super(p_i1148_1_);

        Shape1 = new ModelRenderer(this, 0, 18);
        Shape1.addBox(-4F, -9F, -4F, 8, 1, 7);
        Shape1.setPos(0F, 0F, 0F);
        Shape1.setTexSize(64, 32);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(this, 0, 0);
        Shape2.addBox(-4F, -11F, -6F, 9, 2, 9);
        Shape2.setPos(0F, 0F, 0F);
        Shape2.setTexSize(64, 32);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new ModelRenderer(this, 0, 0);
        Shape3.addBox(-3F, -10F, -4F, 8, 1, 9);
        Shape3.setPos(0F, 0F, 0F);
        Shape3.setTexSize(64, 32);
        Shape3.mirror = true;
        setRotation(Shape3, 0F, 0F, 0F);
        Shape4 = new ModelRenderer(this, 0, 0);
        Shape4.addBox(-5F, -13F, -5F, 7, 2, 7);
        Shape4.setPos(0F, 0F, 0F);
        Shape4.setTexSize(64, 32);
        Shape4.mirror = true;
        setRotation(Shape4, 0F, 0F, 0F);
        Shape5 = new ModelRenderer(this, 0, 0);
        Shape5.addBox(-6F, -12F, -6F, 5, 2, 7);
        Shape5.setPos(0F, 0F, 0F);
        Shape5.setTexSize(64, 32);
        Shape5.mirror = true;
        setRotation(Shape5, 0F, 0F, 0F);
        Shape6 = new ModelRenderer(this, 0, 0);
        Shape6.addBox(0F, -12F, -4F, 4, 3, 5);
        Shape6.setPos(0F, 0F, 0F);
        Shape6.setTexSize(64, 32);
        Shape6.mirror = true;
        setRotation(Shape6, 0F, 0F, 0F);
        Shape7 = new ModelRenderer(this, 0, 0);
        Shape7.addBox(4F, -10F, -4F, 3, 3, 7);
        Shape7.setPos(0F, 0F, 0F);
        Shape7.setTexSize(64, 32);
        Shape7.mirror = true;
        setRotation(Shape7, 0F, 0F, 0F);
        Shape8 = new ModelRenderer(this, 0, 0);
        Shape8.addBox(4F, -11F, -4F, 1, 1, 4);
        Shape8.setPos(0F, 0F, 0F);
        Shape8.setTexSize(64, 32);
        Shape8.mirror = true;
        setRotation(Shape8, 0F, 0F, 0F);
        Shape9 = new ModelRenderer(this, 0, 0);
        Shape9.addBox(-4F, -9F, 3F, 10, 2, 3);
        Shape9.setPos(0F, 0F, 0F);
        Shape9.setTexSize(64, 32);
        Shape9.mirror = true;
        setRotation(Shape9, 0F, 0F, 0F);

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
