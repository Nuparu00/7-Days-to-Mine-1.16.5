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
import nuparu.sevendaystomine.block.BlockWindTurbine;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntityWindTurbine;

public class TileEntityWindTurbineRenderer extends TileEntityRenderer<TileEntityWindTurbine> {

    private final ResourceLocation TEX = new ResourceLocation(SevenDaysToMine.MODID, "entity/wind_turbine_propeller");
    private final RenderMaterial MAT = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, TEX);

    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;

    public TileEntityWindTurbineRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);

        Shape1 = new ModelRenderer(64, 32, 0, 0);
        Shape1.addBox(-2F, -2F, -1F, 4, 4, 2);
        Shape1.setPos(0F, 0F, -10F);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(64, 32, 20, 0);
        Shape2.addBox(-2F, -22F, -1F, 4, 20, 1);
        Shape2.setPos(0F, 0F, -9.5F);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, -0.1745329F, 0F);
        Shape3 = new ModelRenderer(64, 32, 20, 0);
        Shape3.addBox(-2F, 2F, -1F, 4, 20, 1);
        Shape3.setPos(0F, 0F, -9.5F);
        Shape3.mirror = true;
        setRotation(Shape3, 0F, 0.1745329F, 0F);
        Shape4 = new ModelRenderer(64, 32, 0, 27);
        Shape4.addBox(2F, -2F, -1F, 20, 4, 1);
        Shape4.setPos(0F, 0F, -9.5F);
        Shape4.mirror = true;
        setRotation(Shape4, 0.1745329F, 0F, 0F);
        Shape5 = new ModelRenderer(64, 32, 0, 27);
        Shape5.addBox(-22F, -2F, -1F, 20, 4, 1);
        Shape5.setPos(0F, 0F, -9.5F);
        Shape5.mirror = true;
        setRotation(Shape5, -0.1745329F, 0F, 0F);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    @Override
    public void render(TileEntityWindTurbine te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {

        World world = te.getLevel();
        boolean flag = world != null;
        BlockState blockstate = flag ? te.getBlockState() : ModBlocks.WIND_TURBINE.get().defaultBlockState();
        Direction direction = blockstate.hasProperty(BlockWindTurbine.FACING) ? blockstate.getValue(BlockWindTurbine.FACING) : Direction.SOUTH;
        Block block = blockstate.getBlock();

        if (block instanceof BlockWindTurbine) {
            matrixStack.pushPose();
            float f = direction.toYRot();
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-f));
            matrixStack.translate(0, 0, 0);

            float rot = 0;
            if (te != null) {
                float angle = te.angle;
                float prev = te.anglePrev;
                if (prev > angle) {
                    prev -= 360;
                }
                rot = prev + (angle - prev) * partialTicks;
            }

            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(rot));

            IVertexBuilder builder = MAT.buffer(buffer, RenderType::entityCutout);

           /* Shape1.render(matrixStack,builder,combinedLight,combinedOverlay);
            Shape2.render(matrixStack,builder,combinedLight,combinedOverlay);
            Shape3.render(matrixStack,builder,combinedLight,combinedOverlay);*/
            Shape1.render(matrixStack,builder,combinedLight,combinedOverlay);
            Shape2.render(matrixStack,builder,combinedLight,combinedOverlay);
            Shape3.render(matrixStack,builder,combinedLight,combinedOverlay);
            Shape4.render(matrixStack,builder,combinedLight,combinedOverlay);
            Shape5.render(matrixStack,builder,combinedLight,combinedOverlay);

            matrixStack.popPose();
        }

    }
}
