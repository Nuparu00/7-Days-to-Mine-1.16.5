package nuparu.sevendaystomine.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

public class ItemModelRenderer extends ModelRenderer {
    ItemStack stack = ItemStack.EMPTY;

    double xPos;
    double yPos;
    double zPos;

    float xRot;
    float yRot;
    float zRot;

    float scale = 1;
    IRenderTypeBuffer buffer;


    public ItemModelRenderer(int p_i225949_1_, int p_i225949_2_, int p_i225949_3_, int p_i225949_4_) {
        super(p_i225949_1_, p_i225949_2_, p_i225949_3_, p_i225949_4_);
        this.setTexSize(0, 0);
        this.texOffs(0, 0);
    }

    public ItemModelRenderer(double xPos, double yPos, double zPos, float xRot, float yRot, float zRot) {
        super(0,0,0,0);
        this.setTexSize(0, 0);
        this.texOffs(0, 0);
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        this.xRot = xRot;
        this.yRot = yRot;
        this.zRot = zRot;
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder builder, int packedLight, int packedOverlay, float p_228309_5_, float p_228309_6_, float p_228309_7_, float p_228309_8_) {

        if (this.visible && !stack.isEmpty()) {

            matrixStack.pushPose();
            this.translateAndRotate(matrixStack);
                /*this.compile(p_228309_1_.last(), p_228309_2_, p_228309_3_, p_228309_4_, p_228309_5_, p_228309_6_, p_228309_7_, p_228309_8_);

                for(ModelRenderer modelrenderer : this.children) {
                    modelrenderer.render(p_228309_1_, p_228309_2_, p_228309_3_, p_228309_4_, p_228309_5_, p_228309_6_, p_228309_7_, p_228309_8_);
                }*/
            //matrixStack.translate(0.35,0,0);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(xRot));
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(yRot));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(zRot));
            matrixStack.translate(xPos,yPos,zPos);
            matrixStack.scale(scale,scale,scale);
            Minecraft minecraft = Minecraft.getInstance();
            World world = minecraft.level;

            ItemRenderer itemRenderer = minecraft.getItemRenderer();
            IBakedModel model = itemRenderer.getModel(stack, world, null);
            itemRenderer.render(stack, ItemCameraTransforms.TransformType.GUI, true, matrixStack, buffer, packedLight,  OverlayTexture.NO_OVERLAY, model);
            matrixStack.popPose();

        }
    }

    public void setStack(ItemStack stack){
        this.stack = stack;
    }

    public void setBuffer(IRenderTypeBuffer buffer) {
        this.buffer = buffer;
    }
}
