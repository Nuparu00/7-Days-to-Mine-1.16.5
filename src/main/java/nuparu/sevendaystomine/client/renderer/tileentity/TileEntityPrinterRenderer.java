package nuparu.sevendaystomine.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import nuparu.sevendaystomine.block.BlockCalendar;
import nuparu.sevendaystomine.block.BlockPrinter;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntityCalendar;
import nuparu.sevendaystomine.tileentity.TileEntityPrinter;
import nuparu.sevendaystomine.util.Utils;

public class TileEntityPrinterRenderer extends TileEntityRenderer<TileEntityPrinter> {


    public TileEntityPrinterRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);

    }


    @Override
    public void render(TileEntityPrinter te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {

        World world = te.getLevel();
        boolean flag = world != null;
        BlockState blockstate = flag ? te.getBlockState() : ModBlocks.PRINTER.get().defaultBlockState();
        Direction direction = blockstate.hasProperty(BlockPrinter.FACING) ? blockstate.getValue(BlockPrinter.FACING) : Direction.SOUTH;
        Block block = blockstate.getBlock();

        if (block instanceof BlockPrinter) {

            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            float f = direction.toYRot();
            ItemStack upperStack = te.getInventory().getStackInSlot(1);
            if(!upperStack.isEmpty()) {
                matrixStack.pushPose();

                matrixStack.translate(0.5, 0.5, 0.5);
                matrixStack.scale(0.625f, 0.625f, 0.625f);
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(180 - f));
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(22.5f));
                matrixStack.translate(0, 0, 0.36);

                IBakedModel upperModel = itemRenderer.getModel(upperStack, te.getLevel(), null);
                itemRenderer.render(upperStack, ItemCameraTransforms.TransformType.FIXED, true, matrixStack, buffer, combinedLight, combinedOverlay, upperModel);

                matrixStack.popPose();
            }

            ItemStack bottomStack = te.getInventory().getStackInSlot(2);

            if(!bottomStack.isEmpty()) {
                matrixStack.pushPose();

                matrixStack.translate(0.5, 0.082, 0.5);
                matrixStack.scale(0.55f, 0.6f, 0.6f);

                matrixStack.mulPose(Vector3f.YP.rotationDegrees(180 - f));
                matrixStack.translate(0, 0, -0.36);
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(90f));
                IBakedModel bottomModel = itemRenderer.getModel(bottomStack, te.getLevel(), null);
                itemRenderer.render(bottomStack, ItemCameraTransforms.TransformType.FIXED, true, matrixStack, buffer, combinedLight, combinedOverlay, bottomModel);

                matrixStack.popPose();
            }
        }
    }
}
