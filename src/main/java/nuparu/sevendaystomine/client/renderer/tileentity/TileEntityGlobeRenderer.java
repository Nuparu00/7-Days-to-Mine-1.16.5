package nuparu.sevendaystomine.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.BlockCamera;
import nuparu.sevendaystomine.block.BlockGlobe;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntityCamera;
import nuparu.sevendaystomine.tileentity.TileEntityGlobe;

public class TileEntityGlobeRenderer extends TileEntityRenderer<TileEntityGlobe> {

    private final ResourceLocation TEX = new ResourceLocation(SevenDaysToMine.MODID, "entity/globe");
    private final RenderMaterial MAT = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, TEX);


    private final ModelRenderer globe;

    public TileEntityGlobeRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
        globe = new ModelRenderer(64,64, 0, 0);
        globe.addBox(0F, 0F, 0F, 10, 10, 10);
        globe.setPos(-5F, 13F, -5F);
        globe.mirror = true;
        setRotation(globe, 0F, 0F, 0F);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    @Override
    public void render(TileEntityGlobe te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {

        World world = te.getLevel();
        boolean flag = world != null;
        BlockState blockstate = flag ? te.getBlockState() : ModBlocks.GLOBE.get().defaultBlockState();
        Direction direction = blockstate.hasProperty(BlockGlobe.FACING) ? blockstate.getValue(BlockGlobe.FACING) : Direction.SOUTH;
        Block block = blockstate.getBlock();

        if (block instanceof BlockGlobe) {
            matrixStack.pushPose();
            float f = direction.toYRot();
            double rot = 0;
            if (te != null) {
                double angle = te.angle;
                double prev = te.anglePrev;

                rot = prev + (angle - prev) * partialTicks;
            }
            matrixStack.translate(0.5D, 1.5D, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-f));
            matrixStack.translate(0D, 0, -0.605);
            //matrixStack.translate(0.5D, 1.4, 0.49725F);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(-23.5f));
            matrixStack.mulPose(Vector3f.YP.rotationDegrees((float) (rot)));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));

            IVertexBuilder builder = MAT.buffer(buffer, RenderType::entityCutout);

           /* Shape1.render(matrixStack,builder,combinedLight,combinedOverlay);
            Shape2.render(matrixStack,builder,combinedLight,combinedOverlay);
            Shape3.render(matrixStack,builder,combinedLight,combinedOverlay);*/



            globe.yRot = 0;
            globe.render(matrixStack,builder,combinedLight,combinedOverlay);

            matrixStack.popPose();
        }

    }
}
