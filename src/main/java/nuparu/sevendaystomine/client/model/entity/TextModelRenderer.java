package nuparu.sevendaystomine.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.vector.Vector3f;

public class TextModelRenderer extends ModelRenderer {

    String text;

    double xPos;
    double yPos;
    double zPos;

    public TextModelRenderer(int p_i225949_1_, int p_i225949_2_, int p_i225949_3_, int p_i225949_4_) {
        super(p_i225949_1_, p_i225949_2_, p_i225949_3_, p_i225949_4_);
        this.setTexSize(0, 0);
        this.texOffs(0, 0);
    }

    public TextModelRenderer(double xPos, double yPos, double zPos) {
        super(0,0,0,0);
        this.setTexSize(0, 0);
        this.texOffs(0, 0);
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder p_228309_2_, int p_228309_3_, int p_228309_4_, float p_228309_5_, float p_228309_6_, float p_228309_7_, float p_228309_8_) {
        if (this.visible) {
            matrixStack.pushPose();
            this.translateAndRotate(matrixStack);
                /*this.compile(p_228309_1_.last(), p_228309_2_, p_228309_3_, p_228309_4_, p_228309_5_, p_228309_6_, p_228309_7_, p_228309_8_);

                for(ModelRenderer modelrenderer : this.children) {
                    modelrenderer.render(p_228309_1_, p_228309_2_, p_228309_3_, p_228309_4_, p_228309_5_, p_228309_6_, p_228309_7_, p_228309_8_);
                }*/
            matrixStack.translate(0.35,0,0);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(-45));
            matrixStack.translate(xPos,yPos,zPos);
            matrixStack.scale(0.005f,0.005f,0.005f);
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.font.draw(matrixStack, text, 0, 0, 0x000000);

            matrixStack.popPose();

        }
    }

    public void setText(String text){
        this.text = text;
    }
}
