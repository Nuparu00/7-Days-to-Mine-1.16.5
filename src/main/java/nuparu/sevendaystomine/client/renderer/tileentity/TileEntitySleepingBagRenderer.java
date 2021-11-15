package nuparu.sevendaystomine.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.DyeColor;
import net.minecraft.state.properties.BedPart;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.BlockSleepingBag;
import nuparu.sevendaystomine.client.model.tileentity.SleepingBagBottomModel;
import nuparu.sevendaystomine.client.model.tileentity.SleepingBagHeadModel;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntitySleepingBag;

import java.util.Arrays;
import java.util.Comparator;

public class TileEntitySleepingBagRenderer extends TileEntityRenderer<TileEntitySleepingBag> {

    private final  RenderMaterial[] BED_TEXTURES = Arrays.stream(DyeColor.values()).sorted(Comparator.comparingInt(DyeColor::getId)).map((p_228770_0_) -> {
        System.out.println("KEX " + p_228770_0_.getName());
        return new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, new ResourceLocation(SevenDaysToMine.MODID,"entity/sleepingbag/" + p_228770_0_.getName()));
    }).toArray(RenderMaterial[]::new);

    private final SleepingBagHeadModel HEAD_MODEL = new SleepingBagHeadModel();
    private final SleepingBagBottomModel BOTTOM_MODEL = new SleepingBagBottomModel();

    public TileEntitySleepingBagRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);

    }

    @Override
    public void render(TileEntitySleepingBag te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {

        World world = te.getLevel();
        boolean flag = world != null;
        BlockState blockstate = flag ? te.getBlockState() : ModBlocks.SLEEPING_BAG_BLACK.get().defaultBlockState();
        Direction direction = blockstate.hasProperty(BlockSleepingBag.FACING) ? blockstate.getValue(BlockSleepingBag.FACING) : Direction.SOUTH;
        BedPart part = blockstate.hasProperty(BlockSleepingBag.PART) ? blockstate.getValue(BlockSleepingBag.PART) : BedPart.HEAD;
        DyeColor color = te.getColor();

        Block block = blockstate.getBlock();

        if (block instanceof BlockSleepingBag) {
            matrixStack.pushPose();
            float f = direction.toYRot();
            matrixStack.translate(0.5D, 1.5D, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(180-f));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));

            IVertexBuilder builder = BED_TEXTURES[color.getId()].buffer(buffer, RenderType::entityCutout);
            if(part==BedPart.HEAD){
                HEAD_MODEL.render(matrixStack,builder,combinedLight,combinedOverlay);
            }
            else{
                BOTTOM_MODEL.render(matrixStack,builder,combinedLight,combinedOverlay);
            }

            matrixStack.popPose();
        }

    }
}
