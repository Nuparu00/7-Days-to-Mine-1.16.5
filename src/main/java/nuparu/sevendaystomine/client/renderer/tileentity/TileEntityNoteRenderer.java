package nuparu.sevendaystomine.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.BlockNote;
import nuparu.sevendaystomine.client.gui.GuiNote;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntityNote;
import nuparu.sevendaystomine.util.book.BookData;

import java.util.List;

public class TileEntityNoteRenderer extends TileEntityRenderer<TileEntityNote> {


    public TileEntityNoteRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);

    }

    public static void drawTexturedRect(MatrixStack matrix, ResourceLocation texture, IRenderTypeBuffer buffer, float x, float y, int u, int v, float width,
                                        float height, float imageWidth, float imageHeight, float scale, float zLevel, int packedLight, int overlay) {
        //Minecraft.getInstance().getTextureManager().bind(texture);

        RenderSystem.color4f(1, 1, 1, 1);
        float minU = (float) u / imageWidth;
        float maxU = (u + width) / imageWidth;
        float minV = (float) v / imageHeight;
        float maxV = (v + height) / imageHeight;
        /*Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuilder();
        worldrenderer.begin(7, DefaultVertexFormats.BLOCK);*/
        matrix.pushPose();
        Matrix4f pose = matrix.last().pose();

        IVertexBuilder vertexBuilder = buffer.getBuffer((RenderType.entityTranslucent(texture)));
        //vertexBuilder.vertex(pose, x + scale * width, y + scale * height, zLevel).color(1f,1f,1f,1f).uv(maxU, maxV).uv2(packedLight).normal(1,0,0).endVertex();
        //vertexBuilder.vertex(pose, x + scale * width, y, zLevel).color(1f,1f,1f,1f).uv(maxU, minV).uv2(packedLight).normal(1,0,0).endVertex();
        //vertexBuilder.vertex(pose, x, y, zLevel).color(1f,1f,1f,1f).uv(minU, minV).uv2(packedLight).normal(1,0,1).endVertex();
        //vertexBuilder.vertex(pose, x, y + scale * height, zLevel).color(1f,1f,1f,1f).uv(minU, maxV).uv2(packedLight).normal(1,0,0).endVertex();

        vertexBuilder.vertex(pose, x + scale * width, y + scale * height, zLevel).color(1f, 1f, 1f, 1f).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0, 0, 1).endVertex();
        vertexBuilder.vertex(pose, x + scale * width, y, zLevel).color(1f, 1f, 1f, 1f).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0, 0, 1).endVertex();
        vertexBuilder.vertex(pose, x, y, zLevel).color(1f, 1f, 1f, 1f).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0, 0, 1).endVertex();
        vertexBuilder.vertex(pose, x, y + scale * height, zLevel).color(1f, 1f, 1f, 1f).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0, 0, 1).endVertex();
        ////

        matrix.popPose();
        //WorldVertexBufferUploader.end(worldrenderer);
    }

    @Override
    public void render(TileEntityNote te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {

        World world = te.getLevel();
        boolean flag = world != null;
        BlockState blockstate = flag ? te.getBlockState() : ModBlocks.NOTE.get().defaultBlockState();
        Direction direction = blockstate.hasProperty(BlockNote.FACING) ? blockstate.getValue(BlockNote.FACING) : Direction.SOUTH;
        Block block = blockstate.getBlock();
        Minecraft minecraft = Minecraft.getInstance();

        if (block instanceof BlockNote) {
            BookData data = te.getBookData();

            matrixStack.pushPose();
            RenderSystem.enableDepthTest();

            float f = direction.toYRot();
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-f));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
            matrixStack.translate((direction == Direction.SOUTH || direction == Direction.WEST) ? 0.5 : -0.5, -1, 0);
            if(direction == Direction.NORTH || direction == Direction.WEST){
                matrixStack.translate(0, 0, 1);
            }

            matrixStack.scale(0.00625f, 0.00625f, 0.00625f);
            if (data != null) {
                BookData.Page page = data.getPages().get(0);
                if (page == null || page.res == null)
                    return;

                int marginHorizontal = (GuiNote.xSize - 256) / 2;
                int marginVertical = 0;

                RenderSystem.enableAlphaTest();
                RenderSystem.enableLighting();
                drawTexturedRect(matrixStack, page.res, buffer, 0 + marginHorizontal, 0 + marginVertical, 0, 0,
                        GuiNote.xSize, GuiNote.ySize, 256, 256, 1, -1, combinedLight, combinedOverlay);
                RenderSystem.disableLighting();
                RenderSystem.disableAlphaTest();

                for (BookData.TextBlock tb : page.textBlocks) {
                    matrixStack.pushPose();
                    matrixStack.translate(tb.x + marginHorizontal, tb.y + marginVertical, -tb.z - 2);
                    matrixStack.scale((float) tb.scale, (float) tb.scale, (float) tb.scale);
                    List<TextComponent> l = RenderUtils.splitText(
                            new StringTextComponent(tb.unlocalized ? SevenDaysToMine.proxy.localize(tb.text) : tb.text),
                            (int) Math.floor(tb.width / tb.scale), minecraft.font, true, true);
                    for (int i = 0; i < l.size(); i++) {
                        TextComponent component = l.get(i);
                        String s = "";
                        if (tb.formatting != null) {
                            for (TextFormatting tf : tb.formatting) {
                                if (tf == null)
                                    continue;
                                s += tf;
                            }
                        }
                        s += component.getString();

                        int color = tb.color;
                        FontRenderer font = Minecraft.getInstance().font;

                        if (tb.centered) {
                            RenderUtils.drawCenteredString(matrixStack, s, 0, i * (minecraft.font.lineHeight + 1) * tb.scale, color,
                                    tb.shadow);
                        } else {
                            RenderUtils.drawString(matrixStack, s, 0, i * (minecraft.font.lineHeight + 1) * tb.scale, color,
                                    tb.shadow);
                        }
                    }
                    matrixStack.popPose();
                }

                for (BookData.Image img : page.images) {
                    matrixStack.pushPose();
                    RenderUtils.drawTexturedRect(matrixStack, img.res, img.x + marginHorizontal, img.y + marginVertical, 0, 0,
                            img.width, img.height, img.width, img.height, 1, -img.z - 2);
                    matrixStack.popPose();
                }
                for (BookData.CraftingMatrix crafting : page.crafting) {

                    crafting.render(matrixStack, minecraft, marginHorizontal + crafting.x, marginVertical + crafting.y, true,
                            partialTicks);

                }
                for (BookData.Stack stack : page.stacks) {

                    stack.render(minecraft, marginHorizontal + stack.x, marginVertical + stack.y, partialTicks);
                }

            }
            RenderSystem.disableDepthTest();
            matrixStack.popPose();
            /*

            String path = te.path;
            if (path == null || path.isEmpty())
                return;
            if (te.image == null) {
                if (System.currentTimeMillis() >= te.nextUpdate) {
                    te.image = ResourcesHelper.INSTANCE.getImage(path);
                    if (te.image == null) {
                        PacketManager.photoRequest.sendToServer(new PhotoRequestMessage(path));
                        te.image = ResourcesHelper.INSTANCE.tryToGetImage(path);
                    }
                    te.nextUpdate = System.currentTimeMillis() + 2000;
                }
            }

            if (te.image != null && te.image.res != null) {
                int shape = Integer.compare(te.image.height, te.image.width);

                double w = 16;
                double h = 16;

                //Controls the shape of the photo - portrait vs landscape
                if (shape == -1) {
                    w = w * 0.75f;
                    h = ((float) te.image.height / (float) te.image.width) * w;
                } else if (shape == 0) {
                    h = h * 0.75f;
                    w = h;
                } else if (shape == 1) {
                    h = h * 0.75f;
                    w = ((float) te.image.width / (float) te.image.height) * h;
                }

                float f = direction.toYRot();

                matrixStack.pushPose();

                matrixStack.translate(0.5D, 0.5+h/32d, 0.5D);
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(-f));
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
                matrixStack.translate(-(w/32d), 0, 0.49);
                matrixStack.scale(0.0625f, 0.0625f, 0.0625f);
                RenderSystem.enableDepthTest();
                RenderUtils.drawTexturedRect(matrixStack,te.image.res,0,0,0,0,w,h,w,h,1,0);

                RenderSystem.disableDepthTest();
                matrixStack.popPose();
            }*/
        }
    }
}
