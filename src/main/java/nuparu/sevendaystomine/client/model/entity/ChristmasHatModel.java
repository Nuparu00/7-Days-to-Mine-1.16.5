package nuparu.sevendaystomine.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.player.PlayerEntity;

public class ChristmasHatModel<T extends PlayerEntity> extends BipedModel<T> {
    public static final ChristmasHatModel INSTANCE = new ChristmasHatModel(0.3f);

    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
    ModelRenderer Shape6;
    ModelRenderer Shape7;
    ModelRenderer Shape8;

    BipedModel playerModel;

    public ChristmasHatModel(float p_i1148_1_) {
        super(p_i1148_1_);

        Shape1 = new ModelRenderer(this, 0, 0);
        Shape1.addBox(-5F, -8F, -5F, 10, 2, 10);
        Shape1.setPos(0F, 0F, 0F);
        Shape1.setTexSize(64, 32);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(this, 0, 13);
        Shape2.addBox(-4F, -10F, -4F, 8, 2, 8);
        Shape2.setPos(0F, 0F, 0F);
        Shape2.setTexSize(64, 32);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new ModelRenderer(this, 0, 23);
        Shape3.addBox(-2.5F, -12F, -3F, 6, 3, 6);
        Shape3.setPos(0F, 0F, 0F);
        Shape3.setTexSize(64, 32);
        Shape3.mirror = true;
        setRotation(Shape3, 0F, 0F, -0.0981748F);
        Shape4 = new ModelRenderer(this, 42, 0);
        Shape4.addBox(-0.5F, -14F, -2F, 4, 3, 4);
        Shape4.setPos(0F, 0F, 0F);
        Shape4.setTexSize(64, 32);
        Shape4.mirror = true;
        setRotation(Shape4, 0F, 0F, -0.1963495F);
        Shape5 = new ModelRenderer(this, 43, 17);
        Shape5.addBox(4.5F, -14F, -1.5F, 3, 2, 3);
        Shape5.setPos(0F, 0F, 0F);
        Shape5.setTexSize(64, 32);
        Shape5.mirror = true;
        setRotation(Shape5, 0F, 0F, -0.5890486F);
        Shape6 = new ModelRenderer(this, 45, 23);
        Shape6.addBox(-14F, -9F, -1F, 2, 4, 2);
        Shape6.setPos(0F, 0F, 0F);
        Shape6.setTexSize(64, 32);
        Shape6.mirror = true;
        setRotation(Shape6, 0F, 0F, 0.7853982F);
        Shape7 = new ModelRenderer(this, 33, 18);
        Shape7.addBox(-5F, 3F, -0.5F, 1, 3, 1);
        Shape7.setPos(0F, -15F, 0F);
        Shape7.setTexSize(64, 32);
        Shape7.mirror = true;
        setRotation(Shape7, 0F, 0F, 0.3926991F);
        Shape8 = new ModelRenderer(this, 32, 26);
        Shape8.addBox(-11.25F, -8F, -1F, 2, 2, 2);
        Shape8.setPos(0F, 0F, 0F);
        Shape8.setTexSize(64, 32);
        Shape8.mirror = true;
        setRotation(Shape8, 0F, 0F, 0.3926991F);

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
