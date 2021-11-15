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
import nuparu.sevendaystomine.block.BlockAirplaneRotor;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntityAirplaneRotor;

public class TileEntityAirplaneEngineRenderer extends TileEntityRenderer<TileEntityAirplaneRotor> {

    private final ResourceLocation TEX = new ResourceLocation(SevenDaysToMine.MODID, "entity/airplanerotor_turbine");
    private final RenderMaterial MAT = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, TEX);

    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
    ModelRenderer Shape6;
    ModelRenderer Shape7;
    ModelRenderer Shape8;
    ModelRenderer Shape9;
    ModelRenderer Shape10;
    ModelRenderer Shape11;
    ModelRenderer Shape12;
    ModelRenderer Shape13;
    ModelRenderer Shape14;
    ModelRenderer Shape15;

    public TileEntityAirplaneEngineRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);

        Shape1 = new ModelRenderer(64,64, 0, 15);
        Shape1.addBox(-1F, -1F, -4F, 2, 2, 3);
        Shape1.setPos(0F, 0F, 0F);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(64,64, 0, 0);
        Shape2.addBox(-1.5F, -1.5F, -1F, 3, 3, 1);
        Shape2.setPos(0F, 0F, 0F);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new ModelRenderer(64,64, 0, 8);
        Shape3.addBox(-0.5F, -0.5F, -6F, 1, 1, 2);
        Shape3.setPos(0F, 0F, 0F);
        Shape3.mirror = true;
        setRotation(Shape3, 0F, 0F, 0F);
        Shape4 = new ModelRenderer(64,64, 30, 0);
        Shape4.addBox(-1.5F, -14F, -2F, 3, 11, 1);
        Shape4.setPos(0F, 0F, 0F);
        Shape4.mirror = true;
        setRotation(Shape4, 0F, 0F, 0F);
        Shape5 = new ModelRenderer(64,64, 30, 20);
        Shape5.addBox(-1F, -3F, -2F, 2, 2, 1);
        Shape5.setPos(0F, 0F, 0F);
        Shape5.mirror = true;
        setRotation(Shape5, 0F, 0F, 0F);
        Shape6 = new ModelRenderer(64,64, 30, 15);
        Shape6.addBox(-1F, -15F, -2F, 2, 1, 1);
        Shape6.setPos(0F, 0F, 0F);
        Shape6.mirror = true;
        setRotation(Shape6, 0F, 0F, 0F);
        Shape7 = new ModelRenderer(64,64, 20, 20);
        Shape7.addBox(-1F, 1F, -2F, 2, 2, 1);
        Shape7.setPos(0F, 0F, 0F);
        Shape7.mirror = true;
        setRotation(Shape7, 0F, 0F, 0F);
        Shape8 = new ModelRenderer(64,64, 20, 0);
        Shape8.addBox(-1.5F, 3F, -2F, 3, 11, 1);
        Shape8.setPos(0F, 0F, 0F);
        Shape8.mirror = true;
        setRotation(Shape8, 0F, 0F, 0F);
        Shape9 = new ModelRenderer(64,64, 20, 15);
        Shape9.addBox(-1F, 14F, -2F, 2, 1, 1);
        Shape9.setPos(0F, 0F, 0F);
        Shape9.mirror = true;
        setRotation(Shape9, 0F, 0F, 0F);
        Shape10 = new ModelRenderer(64,64, 58, 20);
        Shape10.addBox(1F, -1F, -2F, 2, 2, 1);
        Shape10.setPos(0F, 0F, 0F);
        Shape10.mirror = true;
        setRotation(Shape10, 0F, 0F, 0F);
        Shape11 = new ModelRenderer(64,64, 40, 28);
        Shape11.addBox(3F, -1.5F, -2F, 11, 3, 1);
        Shape11.setPos(0F, 0F, 0F);
        Shape11.mirror = true;
        setRotation(Shape11, 0F, 0F, 0F);
        Shape12 = new ModelRenderer(64,64, 60, 0);
        Shape12.addBox(14F, -1F, -2F, 1, 2, 1);
        Shape12.setPos(0F, 0F, 0F);
        Shape12.mirror = true;
        setRotation(Shape12, 0F, 0F, 0F);
        Shape13 = new ModelRenderer(64,64, 0, 22);
        Shape13.addBox(-3F, -1F, -2F, 2, 2, 1);
        Shape13.setPos(0F, 0F, 0F);
        Shape13.mirror = true;
        setRotation(Shape13, 0F, 0F, 0F);
        Shape14 = new ModelRenderer(64,64, 0, 28);
        Shape14.addBox(-14F, -1.5F, -2F, 11, 3, 1);
        Shape14.setPos(0F, 0F, 0F);
        Shape14.mirror = true;
        setRotation(Shape14, 0F, 0F, 0F);
        Shape15 = new ModelRenderer(64,64, 10, 22);
        Shape15.addBox(-15F, -1F, -2F, 1, 2, 1);
        Shape15.setPos(0F, 0F, 0F);
        Shape15.mirror = true;
        setRotation(Shape15, 0F, 0F, 0F);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    @Override
    public void render(TileEntityAirplaneRotor te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {

        World world = te.getLevel();
        boolean flag = world != null;
        BlockState blockstate = flag ? te.getBlockState() : ModBlocks.AIRPLANE_ROTOR.get().defaultBlockState();
        Direction direction = blockstate.hasProperty(BlockAirplaneRotor.FACING) ? blockstate.getValue(BlockAirplaneRotor.FACING) : Direction.SOUTH;
        Block block = blockstate.getBlock();

        if (block instanceof BlockAirplaneRotor) {
            matrixStack.pushPose();
            float f = direction.toYRot();
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-f));
            matrixStack.translate(0, 0, 0);

            matrixStack.translate(0, 0.125f, -0.6875);

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
            Shape5.render(matrixStack,builder,combinedLight,combinedOverlay);
            Shape6.render(matrixStack,builder,combinedLight,combinedOverlay);
            Shape7.render(matrixStack,builder,combinedLight,combinedOverlay);
            Shape8.render(matrixStack,builder,combinedLight,combinedOverlay);
            Shape9.render(matrixStack,builder,combinedLight,combinedOverlay);
            Shape10.render(matrixStack,builder,combinedLight,combinedOverlay);
            Shape11.render(matrixStack,builder,combinedLight,combinedOverlay);
            Shape12.render(matrixStack,builder,combinedLight,combinedOverlay);
            Shape13.render(matrixStack,builder,combinedLight,combinedOverlay);
            Shape14.render(matrixStack,builder,combinedLight,combinedOverlay);
            Shape15.render(matrixStack,builder,combinedLight,combinedOverlay);

            matrixStack.popPose();
        }

    }
}
