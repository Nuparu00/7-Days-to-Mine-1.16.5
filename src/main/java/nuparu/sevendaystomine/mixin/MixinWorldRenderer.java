package nuparu.sevendaystomine.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.MatrixApplyingVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientChunkProvider;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import nuparu.sevendaystomine.block.repair.BreakData;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IChunkData;
import nuparu.sevendaystomine.client.util.ClientHookz;
import nuparu.sevendaystomine.config.ClientConfig;
import nuparu.sevendaystomine.util.ModConstants;
import nuparu.sevendaystomine.util.Utils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReferenceArray;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

    @Shadow
    @Final
    private static ResourceLocation SUN_LOCATION;
    private static Field f_storage;
    private static Field f_chunks;
    private static Field f_renderBuffers;
    @Shadow
    @Final
    private TextureManager textureManager;
    @Shadow
    private ClientWorld level;

    @Inject(at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/WorldRenderer.checkPoseStack(Lcom/mojang/blaze3d/matrix/MatrixStack;)V", ordinal = 0), method = "renderLevel(Lcom/mojang/blaze3d/matrix/MatrixStack;FJZLnet/minecraft/client/renderer/ActiveRenderInfo;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/util/math/vector/Matrix4f;)V", remap = ModConstants.REMAP)
    private void renderLevel(MatrixStack matrixStack, float partialTicks, long nano, boolean outline, ActiveRenderInfo camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        //if(minecraft.options.graphicsMode== GraphicsFanciness.FABULOUS){
        World world = minecraft.level;


        ClientChunkProvider provider = (ClientChunkProvider) world.getChunkSource();

        if (f_storage == null) {
            f_storage = ObfuscationReflectionHelper.findField(ClientChunkProvider.class, "field_217256_d");
        }
        try {
            ClientChunkProvider.ChunkArray storage = (ClientChunkProvider.ChunkArray) f_storage.get(provider);

            if (f_chunks == null) {
                f_chunks = ObfuscationReflectionHelper.findField(ClientChunkProvider.ChunkArray.class, "field_217195_b");
            }
            AtomicReferenceArray<Chunk> chunks = (AtomicReferenceArray<Chunk>) f_chunks.get(storage);
            if (f_renderBuffers == null) {
                f_renderBuffers = ObfuscationReflectionHelper.findField(Minecraft.class, "field_228006_P_");
            }
            RenderTypeBuffers renderBuffers = (RenderTypeBuffers) f_renderBuffers.get(minecraft);


            Vector3d vec = minecraft.gameRenderer.getMainCamera().getPosition();

            double d0 = vec.x();
            double d1 = vec.y();
            double d2 = vec.z();

            for (int i = 0; i < chunks.length(); i++) {
                Chunk chunk = chunks.get(i);
                if (chunk == null)
                    continue;
                IChunkData data = CapabilityHelper.getChunkData(chunk);
                if (data == null)
                    continue;
                Iterator<Map.Entry<BlockPos, BreakData>> it = data.getData().entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<BlockPos, BreakData> pair = it.next();
                    BlockPos blockpos1 = pair.getKey();
                    double d3 = (double) blockpos1.getX() - d0;
                    double d4 = (double) blockpos1.getY() - d1;
                    double d5 = (double) blockpos1.getZ() - d2;
                    if (!(d3 * d3 + d4 * d4 + d5 * d5 > 1024.0D)) {
                        BreakData breakData = pair.getValue();
                        if (breakData != null) {
                            int k3 = Math.min(Math.round(breakData.getState() * 9), 9);

                            matrixStack.pushPose();
                            matrixStack.translate((double) blockpos1.getX() - d0, (double) blockpos1.getY() - d1,
                                    (double) blockpos1.getZ() - d2);
                            RenderSystem.disableDepthTest();
                            if (k3 >= 0) {
                                minecraft.getMainRenderTarget().bindWrite(true);
                                MatrixStack.Entry matrixstack$entry1 = matrixStack.last();

                                IVertexBuilder ivertexbuilder1 = new MatrixApplyingVertexBuilder(
                                        renderBuffers.crumblingBufferSource().getBuffer(ModelBakery.DESTROY_TYPES.get(k3)),
                                        matrixstack$entry1.pose(), matrixstack$entry1.normal());
                                minecraft.getBlockRenderer().renderBreakingTexture(world.getBlockState(blockpos1),
                                        blockpos1, world, matrixStack, ivertexbuilder1);
                            }
                            matrixStack.popPose();
                            renderBuffers.crumblingBufferSource().endBatch();
                            RenderSystem.enableDepthTest();

                        }
                    }
                }

            }

        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        //}
    }

    @Inject(at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/vertex/VertexBuffer.bind()V", ordinal = 0), method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V", remap = ModConstants.REMAP)
    private void renderSkySky(MatrixStack matrixStack, float partialTicks, CallbackInfo ci) {
        //this.textureManager.bind(this.SUN_LOCATION);
        //System.out.println("Not anymore there is a blanket");

        if (ClientConfig.bloodmoonSky.get()) {
            ClientWorld world = this.level;
            if (Utils.isBloodmoon(world)) {
                ClientHookz.skyColorHook(matrixStack, partialTicks, world);
            }
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BufferBuilder;begin(ILnet/minecraft/client/renderer/vertex/VertexFormat;)V", ordinal = 2), method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V", remap = ModConstants.REMAP)
    private void renderSkyMoon(MatrixStack matrixStack, float p_228424_2_, CallbackInfo ci) {
        //this.textureManager.bind(this.SUN_LOCATION);
        if (ClientConfig.bloodmoonSky.get()) {
            ClientWorld world = this.level;
            if (Utils.isBloodmoon(world)) {
                RenderSystem.color4f(ClientConfig.blodmoonColorR.get().floatValue(), ClientConfig.blodmoonColorG.get().floatValue(), ClientConfig.blodmoonColorB.get().floatValue(), 1);
            }
        }
        //System.out.println("The sun is a deadly laser");
    }
}
