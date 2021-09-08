package nuparu.sevendaystomine.client.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.events.RenderEventHandler;

import java.util.HashMap;

public class AnimationModelRenderers {
    private static HashMap<ResourceLocation,AnimationModelRenderer> modelRegistry = new HashMap<ResourceLocation,AnimationModelRenderer>();


    public static void init(){
        modelRegistry.clear();

        AnimationModelRenderer rightHand = new AnimationModelRenderer(new ResourceLocation(SevenDaysToMine.MODID,"right_hand")) {
            @Override
            void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light) {
                Minecraft minecraft = Minecraft.getInstance();
                AbstractClientPlayerEntity player = minecraft.player;;
                PlayerRenderer playerrenderer = (PlayerRenderer) minecraft.getEntityRenderDispatcher().getRenderer(player);
                playerrenderer.renderRightHand(matrixStack,buffer,light,player);

            }
        };

        register(rightHand);
        

        AnimationModelRenderer rightGun = new AnimationModelRenderer(new ResourceLocation(SevenDaysToMine.MODID,"right_gun")) {
            @Override
            void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light) {
                Minecraft minecraft = Minecraft.getInstance();
                AbstractClientPlayerEntity player = minecraft.player;
                RenderEventHandler.renderArmWithItem(minecraft, player, 0, 0, Hand.MAIN_HAND, 0, player.getMainHandItem(), 0, matrixStack, buffer, light);
            }
        };


        register(rightGun);

        AnimationModelRenderer leftHand = new AnimationModelRenderer(new ResourceLocation(SevenDaysToMine.MODID,"left_hand")) {
            @Override
            void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light) {
                Minecraft minecraft = Minecraft.getInstance();
                AbstractClientPlayerEntity player = minecraft.player;;
                PlayerRenderer playerrenderer = (PlayerRenderer) minecraft.getEntityRenderDispatcher().getRenderer(player);
                playerrenderer.renderLeftHand(matrixStack,buffer,light,player);

            }
        };

        register(leftHand);

        AnimationModelRenderer muzzleFlash = new AnimationModelRenderer(new ResourceLocation(SevenDaysToMine.MODID,"muzzle_flash")) {
            @Override
            void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light) {
                Minecraft minecraft = Minecraft.getInstance();

                double w = 1;
                double h = 1;

                Vector3d v4 = new Vector3d(-w / 2d, -h / 2d, 0);
                Vector3d v3 = new Vector3d(w / 2d, -h / 2d, 0);
                Vector3d v2 = new Vector3d(w / 2d, h / 2d, 0);
                Vector3d v1 = new Vector3d(-w / 2d, h / 2d, 0);

                double angle = RenderEventHandler.mainMuzzleFlashAngle;
                if (angle % 360 != 0) {
                    v1 = RenderUtils.rotatePoint(v1, Vector3d.ZERO, angle);
                    v2 = RenderUtils.rotatePoint(v2, Vector3d.ZERO, angle);
                    v3 = RenderUtils.rotatePoint(v3, Vector3d.ZERO, angle);
                    v4 = RenderUtils.rotatePoint(v4, Vector3d.ZERO, angle);
                }

                matrixStack.pushPose();
                RenderSystem.enableBlend();
                RenderSystem.enableDepthTest();
                matrixStack.translate(0,0.25,0);


                Minecraft.getInstance().getTextureManager().bind(new ResourceLocation(SevenDaysToMine.MODID, "textures/entity/particles/muzzle_flash.png"));

                RenderSystem.color4f(1, 1, 1, 1);

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder worldrenderer = tessellator.getBuilder();
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
                Matrix4f pose = matrixStack.last().pose();
                worldrenderer.vertex(pose,(float)v1.x,(float) v1.y, (float)v1.z).uv(0.0f, 1.0f).endVertex();
                worldrenderer.vertex(pose,(float)v2.x, (float)v2.y, (float)v2.z).uv(1.0f, 1.0f).endVertex();
                worldrenderer.vertex(pose,(float)v3.x, (float)v3.y, (float)v3.z).uv(1.0f, 0f).endVertex();
                worldrenderer.vertex(pose,(float)v4.x, (float)v4.y, (float)v4.z).uv(0.0f, 0.0f).endVertex();
                worldrenderer.end();
                WorldVertexBufferUploader.end(worldrenderer);
                RenderSystem.disableDepthTest();
                RenderSystem.disableBlend();
                matrixStack.popPose();

            }
        };

        register(muzzleFlash);

        AnimationModelRenderer flame = new AnimationModelRenderer(new ResourceLocation(SevenDaysToMine.MODID,"flame")) {
            @Override
            void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light) {
                Minecraft minecraft = Minecraft.getInstance();

                double w = 1;
                double h = 1;

                Vector3d v4 = new Vector3d(-w / 2d, -h / 2d, 0);
                Vector3d v3 = new Vector3d(w / 2d, -h / 2d, 0);
                Vector3d v2 = new Vector3d(w / 2d, h / 2d, 0);
                Vector3d v1 = new Vector3d(-w / 2d, h / 2d, 0);


                matrixStack.pushPose();
                RenderSystem.enableBlend();
                RenderSystem.enableDepthTest();
                matrixStack.translate(0,0.25,0);


                Minecraft.getInstance().getTextureManager().bind(new ResourceLocation("textures/particle/flame.png"));

                RenderSystem.color4f(1, 1, 1, 1);

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder worldrenderer = tessellator.getBuilder();
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
                Matrix4f pose = matrixStack.last().pose();
                worldrenderer.vertex(pose,(float)v1.x,(float) v1.y, (float)v1.z).uv(1.0f, 0.0f).endVertex();
                worldrenderer.vertex(pose,(float)v2.x, (float)v2.y, (float)v2.z).uv(0.0f, 0.0f).endVertex();
                worldrenderer.vertex(pose,(float)v3.x, (float)v3.y, (float)v3.z).uv(0.0f, 1f).endVertex();
                worldrenderer.vertex(pose,(float)v4.x, (float)v4.y, (float)v4.z).uv(1.0f, 1.0f).endVertex();
                worldrenderer.end();
                WorldVertexBufferUploader.end(worldrenderer);
                RenderSystem.disableDepthTest();
                RenderSystem.disableBlend();
                matrixStack.popPose();

            }
        };

        register(flame);

    }

    public static void register(AnimationModelRenderer renderer){
        modelRegistry.put(renderer.registryName,renderer);
    }

    public static AnimationModelRenderer get(ResourceLocation location){
        if(modelRegistry == null){
            return null;
        }
        if(!modelRegistry.containsKey(location)){
            return null;
        }
        return modelRegistry.get(location);
    }
}
