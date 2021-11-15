package nuparu.sevendaystomine.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import nuparu.sevendaystomine.block.BlockCalendar;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntityCalendar;
import nuparu.sevendaystomine.util.Utils;

public class TileEntityCalendarRenderer extends TileEntityRenderer<TileEntityCalendar> {


    public TileEntityCalendarRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);

    }


    @Override
    public void render(TileEntityCalendar te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {

        World world = te.getLevel();
        boolean flag = world != null;
        BlockState blockstate = flag ? te.getBlockState() : ModBlocks.CALENDAR.get().defaultBlockState();
        Direction direction = blockstate.hasProperty(BlockCalendar.FACING) ? blockstate.getValue(BlockCalendar.FACING) : Direction.SOUTH;
        Block block = blockstate.getBlock();

        if (block instanceof BlockCalendar) {

            String text = "?";

            if (CommonConfig.bloodmoonFrequency.get() > 0 && CommonConfig.bloodmoonFrequency.get() <= 999) {
                int mod = Utils.getDay(world) % CommonConfig.bloodmoonFrequency.get();
                int i = mod == 0 ? 0 : CommonConfig.bloodmoonFrequency.get() - (mod);
                text = i + "";
            }

            FontRenderer fontrenderer = this.renderer.getFont();

            matrixStack.pushPose();
            float f = direction.toYRot();
            matrixStack.translate(0.5D, 0.90625, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-f));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
            matrixStack.translate((1-fontrenderer.width(text))*0.03125, 0,0.4355);
            matrixStack.scale(0.0625f,0.0625f,0.0625f);

            fontrenderer.draw(matrixStack,text,0,0,0x000000);

            matrixStack.popPose();
        }
    }
}
