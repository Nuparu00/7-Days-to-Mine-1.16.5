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
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntityCamera;

public class TileEntityCameraRenderer extends TileEntityRenderer<TileEntityCamera> {

    private final ResourceLocation TEX = new ResourceLocation(SevenDaysToMine.MODID, "entity/camera");
    private final RenderMaterial MAT = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, TEX);


    private final ModelRenderer spine;
    private final ModelRenderer arm_joint;
    private final ModelRenderer head_joint;

    public TileEntityCameraRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
        spine = new ModelRenderer(32,32,8, 9);
        spine.setPos(0.0F, 24.0F, 0.0F);
        spine.addBox(-2.0F, -8.0F, 7.0F, 4, 3, 1);

        arm_joint = new ModelRenderer(32,32,0,9);
        arm_joint.setPos(0.0F, 0.0F, 0.0F);
        spine.addChild(arm_joint);
        arm_joint.addBox(-1.0F, -7.0F, 3.0F, 2, 1, 4);
        arm_joint.texOffs(0, 0).addBox(-1.0F, -8.0F, 3.0F, 2, 1, 1);

        head_joint = new ModelRenderer(32,32,0, 0);
        head_joint.setPos(0.0F, -8.0F, 3.5F);
        arm_joint.addChild(head_joint);
        head_joint.addBox(-1.5F, -3.0F, -3.5F, 3, 3, 6);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    @Override
    public void render(TileEntityCamera te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {

        World world = te.getLevel();
        boolean flag = world != null;
        BlockState blockstate = flag ? te.getBlockState() : ModBlocks.CAMERA.get().defaultBlockState();
        Direction direction = blockstate.hasProperty(BlockCamera.FACING) ? blockstate.getValue(BlockCamera.FACING) : Direction.SOUTH;
        Block block = blockstate.getBlock();

        if (block instanceof BlockCamera) {
            matrixStack.pushPose();
            float f = direction.toYRot();
            matrixStack.translate(0.5D, 1.5, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-f));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));

            IVertexBuilder builder = MAT.buffer(buffer, RenderType::entityCutout);

           /* Shape1.render(matrixStack,builder,combinedLight,combinedOverlay);
            Shape2.render(matrixStack,builder,combinedLight,combinedOverlay);
            Shape3.render(matrixStack,builder,combinedLight,combinedOverlay);*/

            float rotation = 0f;
            if (te != null) {
                if (partialTicks == 1.0F) {
                    rotation = te.getHeadRotation();
                } else {
                    rotation = te.getHeadRotationPrev() + (te.getHeadRotation() - te.getHeadRotationPrev()) * partialTicks;
                }
            }

            head_joint.yRot = rotation/90f;
            spine.render(matrixStack,builder,combinedLight,combinedOverlay);

            matrixStack.popPose();
        }

    }
}
