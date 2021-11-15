package nuparu.sevendaystomine.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.*;
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
import nuparu.sevendaystomine.block.BlockSolarPanel;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntitySolarPanel;

public class TileEntitySolarPanelRenderer extends TileEntityRenderer<TileEntitySolarPanel> {

    private final ResourceLocation TEX = new ResourceLocation(SevenDaysToMine.MODID, "entity/solar_panel");
    private final RenderMaterial MAT = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, TEX);

    ModelRenderer panel;

    public TileEntitySolarPanelRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);

        panel = new ModelRenderer(128, 64, 72, 0);
        panel.addBox(-7F, -1F, -7F, 14, 2, 14);
        panel.setPos(0F, 0F, 0F);
        panel.mirror = true;
        setRotation(panel, 0F, 0F, 0F);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    @Override
    public void render(TileEntitySolarPanel te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {

        World world = te.getLevel();
        boolean flag = world != null;
        BlockState blockstate = flag ? te.getBlockState() : ModBlocks.SOLAR_PANEL.get().defaultBlockState();
        Direction direction = blockstate.hasProperty(BlockSolarPanel.FACING) ? blockstate.getValue(BlockSolarPanel.FACING) : Direction.SOUTH;
        Block block = blockstate.getBlock();

        if (block instanceof BlockSolarPanel) {
            matrixStack.pushPose();
            float f = direction.toYRot();
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-f));
            matrixStack.translate(0, 0.1875, 0);

            float rot = 180f;
            if (flag && direction.getAxis() == Direction.Axis.X) {
                rot += (float)(Math.toDegrees(world.getSunAngle(partialTicks)) * (direction == Direction.EAST ? -1 : 1));
            }
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(rot));

            IVertexBuilder builder = MAT.buffer(buffer, RenderType::entityCutout);

           /* Shape1.render(matrixStack,builder,combinedLight,combinedOverlay);
            Shape2.render(matrixStack,builder,combinedLight,combinedOverlay);
            Shape3.render(matrixStack,builder,combinedLight,combinedOverlay);*/
            panel.render(matrixStack,builder,combinedLight,combinedOverlay);

            matrixStack.popPose();
        }

    }
}
