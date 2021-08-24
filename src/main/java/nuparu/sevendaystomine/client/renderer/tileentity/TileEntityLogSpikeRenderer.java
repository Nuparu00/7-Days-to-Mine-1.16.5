package nuparu.sevendaystomine.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.BlockWindTurbine;
import nuparu.sevendaystomine.block.BlockWoodenLogSpike;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntityWindTurbine;
import nuparu.sevendaystomine.tileentity.TileEntityWoodenLogSpike;

public class TileEntityLogSpikeRenderer extends TileEntityRenderer<TileEntityWoodenLogSpike> {

    private final ResourceLocation TEX = new ResourceLocation(SevenDaysToMine.MODID, "entity/wind_turbine_propeller");
    private final RenderMaterial MAT = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, TEX);

    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;

    public TileEntityLogSpikeRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);

    }


    @Override
    public void render(TileEntityWoodenLogSpike te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {

        World world = te.getLevel();
        boolean flag = world != null;
        BlockState blockstate = flag ? te.getBlockState() : ModBlocks.OAK_LOG_SPIKE.get().defaultBlockState();
        Block block = blockstate.getBlock();

        if (block instanceof BlockWoodenLogSpike) {
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 0D, 0.5D);
            Minecraft minecraft = Minecraft.getInstance();

            float u0 = 0;
            float v0 = 0;
            float u1 = 1;
            float v1 = 1;
            minecraft.getTextureManager().bind(new ResourceLocation("textures/block/oak_log.png"));
            RenderSystem.enableDepthTest();
            RenderSystem.enableLighting();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder builder = tessellator.getBuilder();
            builder.begin(7, DefaultVertexFormats.POSITION_TEX_LIGHTMAP_COLOR);
            Matrix4f matrix = matrixStack.last().pose();

            builder.vertex(matrix,0,1,0).uv(u0+ (u1-u0)*0.5f,v0).uv2(0).color(1f,1f,1f,1f).endVertex();
            builder.vertex(matrix,-0.5f,0,0.5f).uv(u0,v1).uv2(0).color(1f,1f,1f,1f).endVertex();

            builder.vertex(matrix,0.5f,0,0.5f).uv(u1,v1).uv2(0).color(1f,1f,1f,1f).endVertex();
            builder.vertex(matrix,0.5f,0,-0.5f).uv(u0,v1).uv2(0).color(1f,1f,1f,1f).endVertex();
            tessellator.end();

            builder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP);
            builder.vertex(matrix,0,1,0).color(1f,1f,1f,1f).uv(u0+ (u1-u0)*0.5f,v0).overlayCoords(combinedOverlay).uv2(combinedLight).endVertex();
            builder.vertex(matrix,0.5f,0,-0.5f).color(1f,1f,1f,1f).uv(u0,v1).overlayCoords(combinedOverlay).uv2(combinedLight).endVertex();

            builder.vertex(matrix,-0.5f,0,-0.5f).color(1f,1f,1f,1f).uv(u1,v1).overlayCoords(combinedOverlay).uv2(combinedLight).endVertex();
            builder.vertex(matrix,-0.5f,0,0.5f).color(1f,1f,1f,1f).uv(u0,v1).overlayCoords(combinedOverlay).uv2(combinedLight).endVertex();
            tessellator.end();


            minecraft.getTextureManager().bind(new ResourceLocation("textures/block/oak_log_top.png"));

            builder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP);
            builder.vertex(matrix,0.5f,0,-0.5f).color(1f,1f,1f,1f).uv(u0,v0).overlayCoords(combinedOverlay).uv2(combinedLight).endVertex();
            builder.vertex(matrix,0.5f,0,0.5f).color(1f,1f,1f,1f).uv(u1,v0).overlayCoords(combinedOverlay).uv2(combinedLight).endVertex();
            builder.vertex(matrix,-0.5f,0,0.5f).color(1f,1f,1f,1f).uv(u1,v1).overlayCoords(combinedOverlay).uv2(combinedLight).endVertex();
            builder.vertex(matrix,-0.5f,0,-0.5f).color(1f,1f,1f,1f).uv(u0,v1).overlayCoords(combinedOverlay).uv2(combinedLight).endVertex();

            tessellator.end();

            RenderSystem.disableLighting();
            RenderSystem.disableDepthTest();

            matrixStack.popPose();
        }
    }

    public TextureAtlasSprite getTexture(Minecraft mc, Block block, boolean top){
        return mc.getTextureAtlas(PlayerContainer.BLOCK_ATLAS).apply(new ResourceLocation("textures/block/oak_log.png"));
    }
}
