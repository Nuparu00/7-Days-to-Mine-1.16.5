package nuparu.sevendaystomine.client.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import nuparu.sevendaystomine.SevenDaysToMine;
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
