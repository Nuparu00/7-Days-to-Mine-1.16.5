package nuparu.sevendaystomine.client.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import nuparu.sevendaystomine.util.ColorRGBA;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class RenderUtils {

    public static Map<EntityType<? extends Entity>, EntityRenderer<? extends Entity>> entityRenderers = Maps.newHashMap();

    public static void drawTexturedRect(MatrixStack matrix, ResourceLocation texture, double x, double y, int u, int v, double width,
                                        double height, double imageWidth, double imageHeight, double scale, double zLevel) {
        drawTexturedRect(matrix, texture, (float) x, (float) y, u, v, (float) width, (float) height, (float) imageWidth, (float) imageHeight, (float) scale, (float) zLevel);
    }

    public static void drawTexturedRect(MatrixStack matrix, ResourceLocation texture, float x, float y, int u, int v, float width,
                                        float height, float imageWidth, float imageHeight, float scale, float zLevel) {
        Minecraft.getInstance().getTextureManager().bind(texture);

        RenderSystem.color4f(1, 1, 1, 1);
        float minU = (float) u / (float) imageWidth;
        float maxU = (float) (u + width) / (float) imageWidth;
        float minV = (float) v / (float) imageHeight;
        float maxV = (float) (v + height) / (float) imageHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuilder();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        Matrix4f pose = matrix.last().pose();
        ;
        worldrenderer.vertex(pose, x + scale * (float) width, y + scale * (float) height, zLevel).uv(maxU, maxV).endVertex();
        worldrenderer.vertex(pose, x + scale * (float) width, y, zLevel).uv(maxU, minV).endVertex();
        worldrenderer.vertex(pose, x, y, zLevel).uv(minU, minV).endVertex();
        worldrenderer.vertex(pose, x, y + scale * (float) height, zLevel).uv(minU, maxV).endVertex();
        worldrenderer.end();
        WorldVertexBufferUploader.end(worldrenderer);
    }

    public static void drawTexturedRect(MatrixStack matrix, ResourceLocation texture, ColorRGBA color, double x, double y, int u, int v, double width,
                                        double height, double imageWidth, double imageHeight, double scale, double zLevel) {
        drawTexturedRect(matrix, texture, color, (float) x, (float) y, u, v, (float) width, (float) height, (float) imageWidth, (float) imageHeight, (float) scale, (float) zLevel);
    }

    public static void drawTexturedRect(MatrixStack matrix, ResourceLocation texture, ColorRGBA color, float x, float y, int u, int v,
                                        double width, float height, float imageWidth, float imageHeight, float scale, float zLevel) {
        ;
        Minecraft.getInstance().getTextureManager().bind(texture);
        RenderSystem.enableAlphaTest();
        RenderSystem.color4f(1, 1, 1, 1);
        float minU = (float) u / (float) imageWidth;
        float maxU = (float) (u + width) / (float) imageWidth;
        float minV = (float) v / (float) imageHeight;
        float maxV = (float) (v + height) / (float) imageHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuilder();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        Matrix4f pose = matrix.last().pose();
        ;
        worldrenderer.vertex(pose, x + scale * (float) width, y + scale * (float) height, zLevel).uv(maxU, maxV)
                .color((float) color.R, (float) color.G, (float) color.B, (float) color.A).endVertex();
        worldrenderer.vertex(pose, x + scale * (float) width, y, zLevel).uv(maxU, minV)
                .color((float) color.R, (float) color.G, (float) color.B, (float) color.A).endVertex();
        worldrenderer.vertex(pose, x, y, zLevel).uv(minU, minV)
                .color((float) color.R, (float) color.G, (float) color.B, (float) color.A).endVertex();
        worldrenderer.vertex(pose, x, y + scale * (float) height, zLevel).uv(minU, maxV)
                .color((float) color.R, (float) color.G, (float) color.B, (float) color.A).endVertex();
        worldrenderer.end();
        WorldVertexBufferUploader.end(worldrenderer);
        RenderSystem.disableAlphaTest();
    }

    public static void drawColoredRect(MatrixStack matrix, ColorRGBA color, double x, double y, double width,
                                       double height, double zLevel) {
        float r = (float) color.R;
        float g = (float) color.G;
        float b = (float) color.B;

        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        Matrix4f pose = matrix.last().pose();
        ;
        bufferbuilder.vertex(pose, (float) (x + 0), (float) (y + height), (float) zLevel).color(r, g, b, 1)
                .endVertex();
        bufferbuilder.vertex(pose, (float) (x + width), (float) (y + height), (float) zLevel).color(r, g, b, 1)
                .endVertex();
        bufferbuilder.vertex(pose, (float) (x + width), (float) (y + 0), (float) zLevel).color(r, g, b, 1)
                .endVertex();
        bufferbuilder.vertex(pose, (float) (x + 0), (float) (y + 0), (float) zLevel).color(r, g, b, 1).endVertex();
        bufferbuilder.end();
        WorldVertexBufferUploader.end(bufferbuilder);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void fill(double x, double y, int u, int v, double width, double height, double imageWidth,
                            double imageHeight, double scale, double zLevel) {
        GL11.glPushMatrix();
        RenderSystem.color4f(1, 1, 1, 255.0F);
        float minU = (float) u / (float) imageWidth;
        float maxU = (float) (u + width) / (float) imageWidth;
        float minV = (float) v / (float) imageHeight;
        float maxV = (float) (v + height) / (float) imageHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuilder();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        worldrenderer.vertex(x + scale * (double) width, y + scale * (double) height, zLevel).uv(maxU, maxV).endVertex();
        worldrenderer.vertex(x + scale * (double) width, y, zLevel).uv(maxU, minV).endVertex();
        worldrenderer.vertex(x, y, zLevel).uv(minU, minV).endVertex();
        worldrenderer.vertex(x, y + scale * (double) height, zLevel).uv(minU, maxV).endVertex();
        worldrenderer.end();
        GL11.glPopMatrix();
    }

    public static void drawCenteredString(MatrixStack matrix, String s, double x, double y, int color) {
        FontRenderer font = Minecraft.getInstance().font;
        matrix.pushPose();
        font.draw(matrix, s, (float) (x - font.width(s) / 2), (float) y, color);
        matrix.popPose();
    }

    public static void drawCenteredString(MatrixStack matrix, String s, float x, float y, int color) {
        FontRenderer font = Minecraft.getInstance().font;
        matrix.pushPose();
        font.draw(matrix, s, (float) (x - font.width(s) / 2), (float) y, color);
        matrix.popPose();
    }

    public static void drawCenteredString(MatrixStack matrix, String s, double x, double y, int color, boolean shadow) {
        FontRenderer font = Minecraft.getInstance().font;
        matrix.pushPose();
        font.draw(matrix, s, (float) (x - font.width(s) / 2), (float) y, color);
        matrix.popPose();
    }

    public static void drawString(MatrixStack matrix, String s, double x, double y, int color) {
        drawString(matrix, s, x, y, color, false);
    }

    public static void drawString(MatrixStack matrix, IReorderingProcessor s, double x, double y, int color) {
        drawString(matrix, s, x, y, color, false);
    }

    public static void drawString(MatrixStack matrix, String s, double x, double y, int color, boolean shadow) {
        FontRenderer font = Minecraft.getInstance().font;
        matrix.pushPose();
        font.draw(matrix, s, (float) x, (float) y, color);
        matrix.popPose();
    }

    public static void drawString(MatrixStack matrix, IReorderingProcessor s, double x, double y, int color, boolean shadow) {
        FontRenderer font = Minecraft.getInstance().font;
        matrix.pushPose();
        font.draw(matrix, s, (float) x, (float) y, color);
        matrix.popPose();
    }

    public static void drawString(MatrixStack matrix, String s, float x, float y, int color) {
        FontRenderer font = Minecraft.getInstance().font;
        matrix.pushPose();
        font.draw(matrix, s, x, y, color);
        matrix.popPose();
    }

    public static void glScissor(Minecraft mc, double x, double y, double width, double height) {
        glScissor(mc, (int) Math.round(x), (int) Math.round(y), (int) Math.round(width), (int) Math.round(height));
    }

    public static void glScissor(Minecraft mc, int x, int y, int width, int height) {
        MainWindow r = mc.getWindow();
        double scale = r.getGuiScale();
        GL11.glScissor((int) (x * scale), (int) ((r.getGuiScaledHeight() - y - height) * scale), (int) (width * scale),
                (int) (height * scale));
    }

    public static void renderView(Minecraft mc, Entity entityView, int width, int height, int resWidth, int resHeight,
                                  float x, float y, float z, float partialTicks) {
        Screen gui = mc.screen;
        if (mc.noRender)
            return;
        WorldRenderer levelRenderer = mc.levelRenderer;
        RenderType pass = net.minecraftforge.client.MinecraftForgeClient.getRenderLayer();

        Entity renderViewEntity = mc.getCameraEntity();
        boolean renderHand = ObfuscationReflectionHelper.getPrivateValue(WorldRenderer.class, levelRenderer,
                "field_175074_C");
        boolean hideGui = mc.options.hideGui;
        mc.options.hideGui = true;
        // mc.options.fboEnable = true;
        mc.screen = null;
        /*
         * ObfuscationReflectionHelper.setPrivateValue(WorldRenderer.class,
         * levelRenderer, false, "field_175074_C");
         * ObfuscationReflectionHelper.setPrivateValue(WorldRenderer.class,
         * levelRenderer, Minecraft.getSystemTime(), "field_78508_Y");
         * mc.setCameraEntity(entityView);
         *
         * ForgeHooksClient.setRenderLayer(RenderType.solid());
         *
         * matrix.pushPose(); Framebuffer frameBuffer = null; try {
         * GL11.glPushAttrib(GL11.GL_ENABLE_BIT); GL11.glMatrixMode(GL11.GL_MODELVIEW);
         * matrix.pushPose(); GL11.glMatrixMode(GL11.GL_PROJECTION);
         * matrix.pushPose();
         *
         * final float ALPHA_TEST_THRESHOLD = 0.1F; GL11.glAlphaFunc(GL11.GL_GREATER,
         * ALPHA_TEST_THRESHOLD);
         *
         * final boolean USE_DEPTH = true; frameBuffer = new Framebuffer(resWidth,
         * resHeight, USE_DEPTH);
         *
         * frameBuffer.bindFramebuffer(true); if
         * (ModConfig.client.useVanillaCameraRendering) { if (erv == null) { erv = new
         * EntityRendererVanilla(mc, mc.getResourceManager()); }
         * erv.updateShaderGroupSize(resWidth, resHeight); erv.updateCameraAndRender(1,
         * System.nanoTime(),frameBuffer,resWidth,resHeight); } else {
         * mc.levelRenderer.setupRender(1, System.nanoTime()); }
         *
         * renderFrameBuffer(frameBuffer, width, height, true, x, y, z);
         *
         * } catch (Exception e) { e.printStackTrace(); } finally { if (frameBuffer !=
         * null) { frameBuffer.destroyBuffers(); } }
         */
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        GL11.glPopAttrib();

        GL11.glPopMatrix();
        ForgeHooksClient.setRenderLayer(pass);
        mc.setCameraEntity(renderViewEntity);
        mc.options.hideGui = hideGui;
        mc.screen = gui;
        ObfuscationReflectionHelper.setPrivateValue(WorldRenderer.class, levelRenderer, renderHand, "field_175074_C");
    }

    public static void renderFrameBuffer(Framebuffer fbo, int width, int height, boolean p_178038_3_, float x, float y,
                                         float z) {


    }

    public static Color getColorAt(ResourceLocation res, int x, int y) {
        InputStream is = null;
        BufferedImage image;

        try {
            is = Minecraft.getInstance().getResourceManager().getResource(res).getInputStream();
            image = ImageIO.read(is);

            return new Color(image.getRGB(x, y), true);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    public static Color getColorAt(ResourceLocation res, double relativeX, double relativeY) {
        InputStream is = null;
        BufferedImage image;

        try {
            is = Minecraft.getInstance().getResourceManager().getResource(res).getInputStream();
            image = ImageIO.read(is);

            int x = (int) Math.round(relativeX * image.getWidth());
            int y = (int) Math.round(relativeY * image.getHeight());
            int rgb = image.getRGB(x, y);
            return new Color(rgb, true);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    public static void drawQuad(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, ResourceLocation res,
                                double angle) {
        if (angle % 360 != 0) {
            v1 = rotatePoint(v1, Vector3d.ZERO, angle);
            v2 = rotatePoint(v2, Vector3d.ZERO, angle);
            v3 = rotatePoint(v3, Vector3d.ZERO, angle);
            v4 = rotatePoint(v4, Vector3d.ZERO, angle);
        }

        RenderSystem.pushMatrix();
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        Minecraft.getInstance().getTextureManager().bind(res);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuilder();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.vertex(v1.x, v1.y, v1.z).uv(0.0f, 1.0f).endVertex();
        worldrenderer.vertex(v2.x, v2.y, v2.z).uv(1.0f, 1.0f).endVertex();
        worldrenderer.vertex(v3.x, v3.y, v3.z).uv(1.0f, 0.0f).endVertex();
        worldrenderer.vertex(v4.x, v4.y, v4.z).uv(0.0f, 0.0f).endVertex();
        worldrenderer.end();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();
    }

    public static Vector3d rotatePoint(Vector3d point, Vector3d origin, double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);

        point.subtract(origin);

        double x = point.x * c - point.y * s;
        double y = point.x * s + point.y * c;

        return new Vector3d(x + origin.x, y + origin.y, point.z);
    }

    public static List<TextComponent> splitText(TextComponent textComponent, int maxTextLenght,
                                                FontRenderer fontRendererIn, boolean p_178908_3_, boolean forceTextColor) {
        int i = 0;
        TextComponent itextcomponent = new StringTextComponent("");
        List<TextComponent> list = Lists.<TextComponent>newArrayList();
        List<TextComponent> list1 = Lists.newArrayList(textComponent);

        for (int j = 0; j < list1.size(); ++j) {
            TextComponent itextcomponent1 = list1.get(j);
            String s = itextcomponent1.getString();
            boolean flag = false;

            if (s.contains("\n")) {
                int k = s.indexOf(10);
                String s1 = s.substring(k + 1);
                s = s.substring(0, k + 1);
                TextComponent itextcomponent2 = new StringTextComponent(s1);
                itextcomponent2.setStyle(itextcomponent1.getStyle());
                list1.add(j + 1, itextcomponent2);
                flag = true;
            }

            String s4 = itextcomponent1.getContents();
            String s5 = s4.endsWith("\n") ? s4.substring(0, s4.length() - 1) : s4;
            int i1 = fontRendererIn.width(s5);
            StringTextComponent textcomponentstring = new StringTextComponent(s5);
            textcomponentstring.setStyle(itextcomponent1.getStyle());

            if (i + i1 > maxTextLenght) {
                String s2 = fontRendererIn.plainSubstrByWidth(s4, maxTextLenght - i, false);
                String s3 = s2.length() < s4.length() ? s4.substring(s2.length()) : null;

                if (s3 != null && !s3.isEmpty()) {
                    int l = s2.lastIndexOf(32);

                    if (l >= 0 && fontRendererIn.width(s4.substring(0, l)) > 0) {
                        s2 = s4.substring(0, l);

                        if (p_178908_3_) {
                            ++l;
                        }

                        s3 = s4.substring(l);
                    } else if (i > 0 && !s4.contains(" ")) {
                        s2 = "";
                        s3 = s4;
                    }

                    StringTextComponent textcomponentstring1 = new StringTextComponent(s3);
                    textcomponentstring1.setStyle(itextcomponent1.getStyle());
                    list1.add(j + 1, textcomponentstring1);
                }

                i1 = fontRendererIn.width(s2);
                textcomponentstring = new StringTextComponent(s2);
                textcomponentstring.setStyle(itextcomponent1.getStyle());
                flag = true;
            }

            if (i + i1 <= maxTextLenght) {
                i += i1;
                itextcomponent.append(textcomponentstring);
            } else {
                flag = true;
            }

            if (flag) {
                list.add(itextcomponent);
                i = 0;
                itextcomponent = new StringTextComponent("");
            }
        }

        list.add(itextcomponent);
        return list;
    }

    public static void renderNameTag(Vector3d pos, ITextComponent p_225629_2_, MatrixStack p_225629_3_, IRenderTypeBuffer p_225629_4_, int p_225629_5_) {
        Minecraft minecraft = Minecraft.getInstance();
        PlayerEntity player = minecraft.player;
        double d0 = minecraft.getEntityRenderDispatcher().distanceToSqr(pos.x, pos.y, pos.z);
        double maxDst = player != null && player.getMainHandItem().getItem() == Items.STRUCTURE_BLOCK || player.getOffhandItem().getItem() == Items.STRUCTURE_BLOCK ? 144 : 16;
        if (d0 <= maxDst) {
            float f = 1 + 0.5F;
            p_225629_3_.pushPose();
            p_225629_3_.translate(0.5D, (double) f, 0.5D);
            p_225629_3_.mulPose(minecraft.getEntityRenderDispatcher().cameraOrientation());
            p_225629_3_.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = p_225629_3_.last().pose();
            float f1 = Minecraft.getInstance().options.getBackgroundOpacity(1);
            int j = (int) (f1 * 255.0F) << 24;
            FontRenderer fontrenderer = minecraft.font;
            float f2 = (float) (-fontrenderer.width(p_225629_2_) / 2);
            fontrenderer.drawInBatch(p_225629_2_, f2, 0, 0xffffff, false, matrix4f, p_225629_4_, true, j, p_225629_5_);
            fontrenderer.drawInBatch(p_225629_2_, f2, (float) 0, -1, false, matrix4f, p_225629_4_, false, 0, p_225629_5_);

            p_225629_3_.popPose();
        }
    }
}
