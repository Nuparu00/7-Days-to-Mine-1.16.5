package nuparu.sevendaystomine.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import nuparu.sevendaystomine.block.BlockPhoto;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.client.util.ResourcesHelper;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.PhotoRequestMessage;
import nuparu.sevendaystomine.tileentity.TileEntityPhoto;

public class TileEntityPhotoRenderer extends TileEntityRenderer<TileEntityPhoto> {


    public TileEntityPhotoRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);

    }


    @Override
    public void render(TileEntityPhoto te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {

        World world = te.getLevel();
        boolean flag = world != null;
        BlockState blockstate = flag ? te.getBlockState() : ModBlocks.CALENDAR.get().defaultBlockState();
        Direction direction = blockstate.hasProperty(BlockPhoto.FACING) ? blockstate.getValue(BlockPhoto.FACING) : Direction.SOUTH;
        Block block = blockstate.getBlock();

        if (block instanceof BlockPhoto) {

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
            }
        }
    }
}
