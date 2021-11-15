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
import nuparu.sevendaystomine.block.BlockTurretBase;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntityTurretBase;

public class TileEntityTurretBaseRenderer extends TileEntityRenderer<TileEntityTurretBase> {

    private final ResourceLocation TEX = new ResourceLocation(SevenDaysToMine.MODID, "entity/turret");
    private final RenderMaterial MAT = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, TEX);

    ModelRenderer BodyTop;
    ModelRenderer RotorBase;
    ModelRenderer RotorA;
    ModelRenderer RotorB;
    ModelRenderer RotorC;
    ModelRenderer RotorD;
    ModelRenderer BodyBottom;
    ModelRenderer LegTopA;
    ModelRenderer LegTopB;
    ModelRenderer LegTopC;
    ModelRenderer LegTopD;
    ModelRenderer LegBottomA;
    ModelRenderer LegBottomB;
    ModelRenderer LegBottomC;
    ModelRenderer LegBottomD;
    ModelRenderer HeadLidA;
    ModelRenderer HeadLidB;
    ModelRenderer HeadBase;
    ModelRenderer BarrelBase;
    ModelRenderer BarrelA;
    ModelRenderer BarrelB;
    ModelRenderer BarrelC;
    ModelRenderer BarrelD;
    ModelRenderer BarrelRing;
    ModelRenderer BarrelEnd;
    ModelRenderer AmmoBoxBaseA;
    ModelRenderer AmmoBoxBaseB;
    ModelRenderer AmmoBoxBaseC;
    ModelRenderer AmmoBox;
    ModelRenderer BarrelE;
    ModelRenderer BarrelF;
    ModelRenderer BarrelG;
    ModelRenderer BarrelH;

    public TileEntityTurretBaseRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);

        BodyTop = new ModelRenderer(64,64, 0, 45);
        BodyTop.addBox(-1.5F, 0F, -1.5F, 3, 2, 3);
        BodyTop.setPos(0F, 10F, 0F);
        BodyTop.mirror = true;
        setRotation(BodyTop, 0F, 0F, 0F);
        RotorBase = new ModelRenderer(64,64, 0, 37);
        RotorBase.addBox(-3F, 0F, -3F, 6, 1, 6);
        RotorBase.setPos(0F, 9F, 0F);
        RotorBase.mirror = true;
        setRotation(RotorBase, 0F, 0F, 0F);
        RotorA = new ModelRenderer(64,64, 0, 12);
        RotorA.addBox(-4F, 0F, -2F, 1, 1, 4);
        RotorA.setPos(0F, 9F, 0F);
        RotorA.mirror = true;
        setRotation(RotorA, 0F, 0F, 0F);
        RotorB = new ModelRenderer(64,64, 0, 12);
        RotorB.addBox(3F, 0F, -2F, 1, 1, 4);
        RotorB.setPos(0F, 9F, 0F);
        RotorB.mirror = true;
        setRotation(RotorB, 0F, 0F, 0F);
        RotorC = new ModelRenderer(64,64, 0, 5);
        RotorC.addBox(-2F, 0F, -4F, 4, 1, 1);
        RotorC.setPos(0F, 9F, 0F);
        RotorC.mirror = true;
        setRotation(RotorC, 0F, 0F, 0F);
        RotorD = new ModelRenderer(64,64, 0, 5);
        RotorD.addBox(-2F, 0F, 3F, 4, 1, 1);
        RotorD.setPos(0F, 9F, 0F);
        RotorD.mirror = true;
        setRotation(RotorD, 0F, 0F, 0F);
        BodyBottom = new ModelRenderer(64,64, 0, 24);
        BodyBottom.addBox(-2F, 0F, -2F, 4, 4, 4);
        BodyBottom.setPos(0F, 12F, 0F);
        BodyBottom.mirror = true;
        setRotation(BodyBottom, 0F, 0F, 0F);
        LegTopA = new ModelRenderer(64,64, 17, 25);
        LegTopA.addBox(0F, 0F, 0F, 2, 5, 2);
        LegTopA.setPos(1F, 15F, 1F);
        LegTopA.mirror = true;
        setRotation(LegTopA, 0.3926991F, 0F, -0.3926991F);
        LegTopB = new ModelRenderer(64,64, 26, 25);
        LegTopB.addBox(-2F, 0F, 0F, 2, 5, 2);
        LegTopB.setPos(-1F, 15F, 1F);
        LegTopB.mirror = true;
        setRotation(LegTopB, 0.3926991F, 0F, 0.3926991F);
        LegTopC = new ModelRenderer(64,64, 35, 25);
        LegTopC.addBox(0F, 0F, -2F, 2, 5, 2);
        LegTopC.setPos(1F, 15F, -1F);
        LegTopC.mirror = true;
        setRotation(LegTopC, -0.3926991F, 0F, -0.3926991F);
        LegTopD = new ModelRenderer(64,64, 44, 25);
        LegTopD.addBox(-2F, 0F, -2F, 2, 5, 2);
        LegTopD.setPos(-1F, 15F, -1F);
        LegTopD.mirror = true;
        setRotation(LegTopD, -0.3926991F, 0F, 0.3926991F);
        LegBottomA = new ModelRenderer(64,64, 17, 16);
        LegBottomA.addBox(0.5F, 5F, 0.5F, 1, 7, 1);
        LegBottomA.setPos(1F, 15F, 1F);
        LegBottomA.mirror = true;
        setRotation(LegBottomA, 0.3926991F, 0F, -0.3926991F);
        LegBottomB = new ModelRenderer(64,64, 22, 16);
        LegBottomB.addBox(-1.5F, 5F, 0.5F, 1, 7, 1);
        LegBottomB.setPos(-1F, 15F, 1F);
        LegBottomB.mirror = true;
        setRotation(LegBottomB, 0.3926991F, 0F, 0.3926991F);
        LegBottomC = new ModelRenderer(64,64, 27, 16);
        LegBottomC.addBox(0.5F, 5F, -1.5F, 1, 7, 1);
        LegBottomC.setPos(1F, 15F, -1F);
        LegBottomC.mirror = true;
        setRotation(LegBottomC, -0.3926991F, 0F, -0.3926991F);
        LegBottomD = new ModelRenderer(64,64, 32, 16);
        LegBottomD.addBox(-1.5F, 5F, -1.5F, 1, 7, 1);
        LegBottomD.setPos(-1F, 15F, -1F);
        LegBottomD.mirror = true;
        setRotation(LegBottomD, -0.3926991F, 0F, 0.3926991F);
        HeadLidA = new ModelRenderer(64,64, 50, 0);
        HeadLidA.addBox(2F, -5F, -3F, 1, 5, 6);
        HeadLidA.setPos(0F, 9F, 0F);
        HeadLidA.mirror = true;
        setRotation(HeadLidA, 0F, 0F, 0F);
        HeadLidB = new ModelRenderer(64,64, 50, 0);
        HeadLidB.addBox(-3F, -5F, -3F, 1, 5, 6);
        HeadLidB.setPos(0F, 9F, 0F);
        HeadLidB.mirror = true;
        setRotation(HeadLidB, 0F, 0F, 0F);
        HeadBase = new ModelRenderer(64,64, 0, 51);
        HeadBase.addBox(-2F, -6F, -4F, 4, 5, 8);
        HeadBase.setPos(0F, 9F, 0F);
        HeadBase.mirror = true;
        setRotation(HeadBase, 0F, 0F, 0F);
        BarrelBase = new ModelRenderer(64,64, 38, 11);
        BarrelBase.addBox(-0.5F, -4F, -16F, 1, 1, 12);
        BarrelBase.setPos(0F, 9F, 0F);
        BarrelBase.mirror = true;
        setRotation(BarrelBase, 0F, 0F, 0F);
        BarrelA = new ModelRenderer(64,64, 44, 17);
        BarrelA.addBox(-0.5F, -3F, -16F, 1, 1, 6);
        BarrelA.setPos(0F, 9F, 0F);
        BarrelA.mirror = true;
        setRotation(BarrelA, 0F, 0F, 0F);
        BarrelB = new ModelRenderer(64,64, 44, 17);
        BarrelB.addBox(-0.5F, -5F, -16F, 1, 1, 6);
        BarrelB.setPos(0F, 9F, 0F);
        BarrelB.mirror = true;
        setRotation(BarrelB, 0F, 0F, 0F);
        BarrelC = new ModelRenderer(64,64, 44, 17);
        BarrelC.addBox(-1.5F, -4F, -16F, 1, 1, 6);
        BarrelC.setPos(0F, 9F, 0F);
        BarrelC.mirror = true;
        setRotation(BarrelC, 0F, 0F, 0F);
        BarrelD = new ModelRenderer(64,64, 44, 17);
        BarrelD.addBox(0.5F, -4F, -16F, 1, 1, 6);
        BarrelD.setPos(0F, 9F, 0F);
        BarrelD.mirror = true;
        setRotation(BarrelD, 0F, 0F, 0F);
        BarrelRing = new ModelRenderer(64,64, 56, 28);
        BarrelRing.addBox(-1.5F, -5F, -10F, 3, 3, 1);
        BarrelRing.setPos(0F, 9F, 0F);
        BarrelRing.mirror = true;
        setRotation(BarrelRing, 0F, 0F, 0F);
        BarrelEnd = new ModelRenderer(64,64, 0, 18);
        BarrelEnd.addBox(-1.5F, -5F, -18F, 3, 3, 2);
        BarrelEnd.setPos(0F, 9F, 0F);
        BarrelEnd.mirror = true;
        setRotation(BarrelEnd, 0F, 0F, 0F);
        AmmoBoxBaseA = new ModelRenderer(64,64, 35, 0);
        AmmoBoxBaseA.addBox(-3F, -4F, 4F, 6, 4, 1);
        AmmoBoxBaseA.setPos(0F, 9F, 0F);
        AmmoBoxBaseA.mirror = true;
        setRotation(AmmoBoxBaseA, 0F, 0F, 0F);
        AmmoBoxBaseB = new ModelRenderer(64,64, 34, 58);
        AmmoBoxBaseB.addBox(-3F, -1F, 5F, 6, 1, 4);
        AmmoBoxBaseB.setPos(0F, 9F, 0F);
        AmmoBoxBaseB.mirror = true;
        setRotation(AmmoBoxBaseB, 0F, 0F, 0F);
        AmmoBoxBaseC = new ModelRenderer(64,64, 35, 0);
        AmmoBoxBaseC.addBox(-3F, -4F, 9F, 6, 4, 1);
        AmmoBoxBaseC.setPos(0F, 9F, 0F);
        AmmoBoxBaseC.mirror = true;
        setRotation(AmmoBoxBaseC, 0F, 0F, 0F);
        AmmoBox = new ModelRenderer(64,64, 44, 40);
        AmmoBox.addBox(-3F, -6F, 5F, 6, 5, 4);
        AmmoBox.setPos(0F, 9F, 0F);
        AmmoBox.mirror = true;
        setRotation(AmmoBox, 0F, 0F, 0F);
        BarrelE = new ModelRenderer(64,64, 37, 15);
        BarrelE.addBox(-0.5F, -3F, -9F, 1, 1, 5);
        BarrelE.setPos(0F, 9F, 0F);
        BarrelE.mirror = true;
        setRotation(BarrelE, 0F, 0F, 0F);
        BarrelF = new ModelRenderer(64,64, 37, 15);
        BarrelF.addBox(-0.5F, -5F, -9F, 1, 1, 5);
        BarrelF.setPos(0F, 9F, 0F);
        BarrelF.mirror = true;
        setRotation(BarrelF, 0F, 0F, 0F);
        BarrelG = new ModelRenderer(64,64, 37, 15);
        BarrelG.addBox(0.5F, -4F, -9F, 1, 1, 5);
        BarrelG.setPos(0F, 9F, 0F);
        BarrelG.mirror = true;
        setRotation(BarrelG, 0F, 0F, 0F);
        BarrelH = new ModelRenderer(64,64, 37, 15);
        BarrelH.addBox(-1.5F, -4F, -9F, 1, 1, 5);
        BarrelH.setPos(0F, 9F, 0F);
        BarrelH.mirror = true;
        setRotation(BarrelH, 0F, 0F, 0F);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    @Override
    public void render(TileEntityTurretBase te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {

        World world = te.getLevel();
        boolean flag = world != null;
        BlockState blockstate = flag ? te.getBlockState() : ModBlocks.TURRET_BASE.get().defaultBlockState();
        Direction direction = blockstate.hasProperty(BlockTurretBase.FACING) ? blockstate.getValue(BlockTurretBase.FACING) : Direction.SOUTH;
        Block block = blockstate.getBlock();

        if (block instanceof BlockTurretBase) {
            matrixStack.pushPose();
            float f = direction.toYRot();
            matrixStack.translate(0.5D, 1.5D, 0.5D);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-f));
            matrixStack.translate(0, 0, 0);

            float rotation = 0f;
            if (te != null) {
                if (partialTicks == 1.0F) {
                    rotation = te.headRotation;
                } else {
                    rotation = te.headRotationPrev + (te.headRotation - te.headRotationPrev) * partialTicks;
                }
            }

            if(direction.getAxis() == Direction.Axis.Z){
                rotation+=180;
            }

            IVertexBuilder builder = MAT.buffer(buffer, RenderType::entityCutout);

            rotateHead(rotation);

            BodyTop.render(matrixStack,builder,combinedLight,combinedOverlay);
            RotorBase.render(matrixStack,builder,combinedLight,combinedOverlay);
            RotorA.render(matrixStack,builder,combinedLight,combinedOverlay);
            RotorB.render(matrixStack,builder,combinedLight,combinedOverlay);
            RotorC.render(matrixStack,builder,combinedLight,combinedOverlay);
            RotorD.render(matrixStack,builder,combinedLight,combinedOverlay);
            BodyBottom.render(matrixStack,builder,combinedLight,combinedOverlay);
            LegTopA.render(matrixStack,builder,combinedLight,combinedOverlay);
            LegTopB.render(matrixStack,builder,combinedLight,combinedOverlay);
            LegTopC.render(matrixStack,builder,combinedLight,combinedOverlay);
            LegTopD.render(matrixStack,builder,combinedLight,combinedOverlay);
            LegBottomA.render(matrixStack,builder,combinedLight,combinedOverlay);
            LegBottomB.render(matrixStack,builder,combinedLight,combinedOverlay);
            LegBottomC.render(matrixStack,builder,combinedLight,combinedOverlay);
            LegBottomD.render(matrixStack,builder,combinedLight,combinedOverlay);
            HeadLidA.render(matrixStack,builder,combinedLight,combinedOverlay);
            HeadLidB.render(matrixStack,builder,combinedLight,combinedOverlay);
            HeadBase.render(matrixStack,builder,combinedLight,combinedOverlay);
            BarrelBase.render(matrixStack,builder,combinedLight,combinedOverlay);
            BarrelA.render(matrixStack,builder,combinedLight,combinedOverlay);
            BarrelB.render(matrixStack,builder,combinedLight,combinedOverlay);
            BarrelC.render(matrixStack,builder,combinedLight,combinedOverlay);
            BarrelD.render(matrixStack,builder,combinedLight,combinedOverlay);
            BarrelRing.render(matrixStack,builder,combinedLight,combinedOverlay);
            BarrelEnd.render(matrixStack,builder,combinedLight,combinedOverlay);
            AmmoBoxBaseA.render(matrixStack,builder,combinedLight,combinedOverlay);
            AmmoBoxBaseB.render(matrixStack,builder,combinedLight,combinedOverlay);
            AmmoBoxBaseC.render(matrixStack,builder,combinedLight,combinedOverlay);
            AmmoBox.render(matrixStack,builder,combinedLight,combinedOverlay);
            BarrelE.render(matrixStack,builder,combinedLight,combinedOverlay);
            BarrelF.render(matrixStack,builder,combinedLight,combinedOverlay);
            BarrelG.render(matrixStack,builder,combinedLight,combinedOverlay);
            BarrelH.render(matrixStack,builder,combinedLight,combinedOverlay);
            matrixStack.popPose();
        }

    }

    public void rotateHead(float rot) {

        this.HeadBase.yRot = rot / (180F / (float) Math.PI);
        this.HeadLidA.yRot = this.HeadBase.yRot;
        this.HeadLidB.yRot = this.HeadBase.yRot;
        this.BarrelBase.yRot = this.HeadBase.yRot;
        this.BarrelA.yRot = this.HeadBase.yRot;
        this.BarrelB.yRot = this.HeadBase.yRot;
        this.BarrelC.yRot = this.HeadBase.yRot;
        this.BarrelD.yRot = this.HeadBase.yRot;
        this.BarrelE.yRot = this.HeadBase.yRot;
        this.BarrelF.yRot = this.HeadBase.yRot;
        this.BarrelG.yRot = this.HeadBase.yRot;
        this.BarrelH.yRot = this.HeadBase.yRot;
        this.BarrelEnd.yRot = this.HeadBase.yRot;
        this.BarrelRing.yRot = this.HeadBase.yRot;
        this.AmmoBoxBaseA.yRot = this.HeadBase.yRot;
        this.AmmoBoxBaseB.yRot = this.HeadBase.yRot;
        this.AmmoBoxBaseC.yRot = this.HeadBase.yRot;
        this.AmmoBox.yRot = this.HeadBase.yRot;
        this.RotorBase.yRot = this.HeadBase.yRot;
        this.RotorA.yRot = this.HeadBase.yRot;
        this.RotorB.yRot = this.HeadBase.yRot;
        this.RotorC.yRot = this.HeadBase.yRot;
        this.RotorD.yRot = this.HeadBase.yRot;
    }
}
