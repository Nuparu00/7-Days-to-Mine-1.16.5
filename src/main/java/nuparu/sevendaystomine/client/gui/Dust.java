package nuparu.sevendaystomine.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.util.MathUtils;

public class Dust {
    float x;
    float y;
    float motionX;
    float motionY;
    float scale;
    float opacity;
    int lifeSpan;
    float[] RGB;
    final Minecraft mc = Minecraft.getInstance();
    final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
            "textures/gui/title/background/dust.png");
    GuiMainMenuEnhanced gui;

    public Dust(float x, float y, float motionX, float motionY, float size, float opacity, float[] RGB,
                GuiMainMenuEnhanced gui) {
        this.x = x;
        this.y = y;
        this.motionX = motionX;
        this.motionY = motionY;
        this.scale = size;
        this.opacity = opacity;
        this.lifeSpan = 1200;
        this.RGB = RGB;
        this.gui = gui;
    }

    public void update(int mouseX, int mouseY) {

        this.x += +this.motionX;
        this.y += this.motionY;
        this.lifeSpan--;
        if (this.lifeSpan <= 0) {
            this.opacity -= 0.05f;
            this.lifeSpan = 0;
        }/* else if (opacity < 1 && RANDOM.nextInt(20) == 0) {
            opacity = (float) MathHelper.clamp(opacity+RANDOM.nextFloat()*0.1, 0, 1);
        }*/
        if (mouseX - x < -500) {
            motionX += MathUtils.getFloatInRange(-0.0071258f, 0.0005f);
        } else if (mouseX - x > 500) {
            motionX += MathUtils.getFloatInRange(-0.0005f, 0.0071258f);
        }

        if (this.opacity <= 0) {
            gui.dustsToRemove.add(this);
        }

    }

    public void draw(MatrixStack stack) {
        stack.pushPose();
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.enableAlphaTest();
        RenderSystem.color4f(RGB[0], RGB[1], RGB[2], this.opacity);
        stack.translate(x, y, 0.0F);
        stack.scale(scale, scale, scale);
        mc.getTextureManager().bind(TEXTURE);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuilder();
        Matrix4f pose = stack.last().pose();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.vertex(pose,0, 16, -90).uv(0.0f, 1.0f).endVertex();
        worldrenderer.vertex(pose,16, 16, -90).uv(1.0f, 1.0f).endVertex();
        worldrenderer.vertex(pose,16, 0, -90).uv(1.0f, 0.0f).endVertex();
        worldrenderer.vertex(pose,0, 0, -90).uv(0.0f, 0.0f).endVertex();
        tessellator.end();
        RenderSystem.enableDepthTest();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1);
        RenderSystem.disableBlend();
        stack.popPose();
    }

}