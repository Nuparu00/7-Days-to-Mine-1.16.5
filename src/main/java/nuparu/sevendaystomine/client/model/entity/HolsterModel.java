package nuparu.sevendaystomine.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.util.PlayerInventorySyncHelper;
import nuparu.sevendaystomine.util.item.ItemCache;

public class HolsterModel<T extends PlayerEntity> extends BipedModel<T> {
    private final ModelRenderer back;
    private final ModelRenderer right;
    private final ModelRenderer left;

    private final ItemModelRenderer backItem;
    private final ItemModelRenderer rightItem;
    private final ItemModelRenderer leftItem;

    BipedModel playerModel;
    IRenderTypeBuffer buffer;

    public HolsterModel(float p_i1148_1_) {
        super(p_i1148_1_);
        texWidth = 64;
        texHeight = 64;

        backItem = new ItemModelRenderer(0,0,0,0,0,0);
        rightItem = new ItemModelRenderer(0,0,0,0,0,0);
        leftItem = new ItemModelRenderer(0,0,0,0,0,0);

        back = new ModelRenderer(this);
        back.setPos(0.5F, 6.0F, 2.0F);
        back.texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, false);

        right = new ModelRenderer(this);
        right.setPos(-4.0F, 14.0F, -0.5F);
        right.texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, false);

        left = new ModelRenderer(this);
        left.setPos(4.0F, 14.0F, -0.5F);
        left.texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, false);

        back.addChild(backItem);
        right.addChild(rightItem);
        left.addChild(leftItem);
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

        backItem.visible = false;
        rightItem.visible = false;
        leftItem.visible = false;

        backItem.setBuffer(buffer);
        rightItem.setBuffer(buffer);
        leftItem.setBuffer(buffer);

        if (PlayerInventorySyncHelper.itemsCache.containsKey(entity.getName().getString())) {
            ItemCache cache = PlayerInventorySyncHelper.itemsCache.get(entity.getName().getString());
            if (cache == null || cache.isEmpty())
                return;

            if(!cache.longItem.isEmpty()){
                backItem.visible = true;

                back.x = playerModel.body.x;
                back.y = playerModel.body.y;
                back.z = playerModel.body.z;
                back.xRot = playerModel.body.xRot;
                back.yRot = playerModel.body.yRot;
                back.zRot = playerModel.body.zRot;

                backItem.setStack(cache.longItem);

                backItem.xRot = 0;
                backItem.yRot = 180;
                backItem.zRot = 0;
                backItem.scale = 1f;
                backItem.xPos = 0;
                backItem.yPos = -0.3f;
                backItem.zPos = 0.14;
            }


            if(!cache.shortItem_L.isEmpty()){
                leftItem.visible = true;

                left.x = playerModel.leftLeg.x;
                left.y = playerModel.leftLeg.y;
                left.z = playerModel.leftLeg.z;
                left.xRot = playerModel.leftLeg.xRot;
                left.yRot = playerModel.leftLeg.yRot;
                left.zRot = playerModel.leftLeg.zRot;

                leftItem.setStack(cache.shortItem_L);

                leftItem.xRot = 0;
                leftItem.yRot = -90;
                leftItem.zRot = -90;
                leftItem.scale = 0.5f;
                leftItem.xPos = 0;
                leftItem.yPos = 0f;
                leftItem.zPos = 0.13;
            }

            if(!cache.shortItem_R.isEmpty()){

                rightItem.visible = false;

                right.x = playerModel.rightLeg.x;
                right.y = playerModel.rightLeg.y;
                right.z = playerModel.rightLeg.z;
                right.xRot = playerModel.rightLeg.xRot;
                right.yRot = playerModel.rightLeg.yRot;
                right.zRot = playerModel.rightLeg.zRot;

                rightItem.setStack(cache.shortItem_R);

                rightItem.xRot = 0;
                rightItem.yRot = -90;
                rightItem.zRot = -90;
                rightItem.scale = 0.5f;
                rightItem.xPos = 0;
                rightItem.yPos = 0f;
                rightItem.zPos = -0.18;
            }


        }
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        back.render(matrixStack, buffer, packedLight, packedOverlay);
        right.render(matrixStack, buffer, packedLight, packedOverlay);
        left.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setPlayerModel(BipedModel playerModel){
        this.playerModel = playerModel;
    }

    public void setBuffer(IRenderTypeBuffer buffer) {
        this.buffer = buffer;
    }
}
