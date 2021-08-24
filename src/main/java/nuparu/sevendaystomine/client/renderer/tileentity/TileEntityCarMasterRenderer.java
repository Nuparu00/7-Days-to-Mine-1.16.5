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
import nuparu.sevendaystomine.block.*;
import nuparu.sevendaystomine.client.model.tileentity.AmbulanceModel;
import nuparu.sevendaystomine.client.model.tileentity.PoliceCarModel;
import nuparu.sevendaystomine.client.model.tileentity.SedanModel;
import nuparu.sevendaystomine.client.model.tileentity.TileEntityModel;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntityCarMaster;
import nuparu.sevendaystomine.tileentity.TileEntityWindTurbine;

public class TileEntityCarMasterRenderer extends TileEntityRenderer<TileEntityCarMaster> {

    public static final ResourceLocation TEXTURE_RED = new ResourceLocation(SevenDaysToMine.MODID,
            "entity/sedan_red");
    public static final ResourceLocation TEXTURE_BLUE = new ResourceLocation(SevenDaysToMine.MODID,
            "entity/sedan_blue");
    public static final ResourceLocation TEXTURE_YELLOW = new ResourceLocation(SevenDaysToMine.MODID,
            "entity/sedan_yellow");
    public static final ResourceLocation TEXTURE_GREEN = new ResourceLocation(SevenDaysToMine.MODID,
            "entity/sedan_green");
    public static final ResourceLocation TEXTURE_BLACK = new ResourceLocation(SevenDaysToMine.MODID,
            "entity/sedan_black");
    public static final ResourceLocation TEXTURE_WHITE = new ResourceLocation(SevenDaysToMine.MODID,
            "entity/sedan_white");
    public static final ResourceLocation TEXTURE_POLICE = new ResourceLocation(SevenDaysToMine.MODID,
            "entity/police_car");
    public static final ResourceLocation TEXTURE_AMBULANCE = new ResourceLocation(SevenDaysToMine.MODID,
            "entity/ambulance");

    public static final SedanModel SEDAN_MODEL = new SedanModel();
    public static final PoliceCarModel POLICE_CAR_MODEL = new PoliceCarModel();
    public static final AmbulanceModel AMBULANCE_MODEL = new AmbulanceModel();

    public TileEntityCarMasterRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);


    }



    @Override
    public void render(TileEntityCarMaster te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {

        World world = te.getLevel();
        boolean flag = world != null;
        BlockState blockstate = flag ? te.getBlockState() : ModBlocks.SEDAN_RED.get().defaultBlockState();
        Direction direction = blockstate.hasProperty(BlockSedan.FACING) ? blockstate.getValue(BlockSedan.FACING) : Direction.SOUTH;
        Block block = blockstate.getBlock();

        if (block instanceof BlockCar) {
            matrixStack.pushPose();
            float f = direction.toYRot();
            matrixStack.translate(0.5D, 1.375, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-f));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
            matrixStack.translate(0, 0, 0);

            IVertexBuilder builder = getRenderMaterial(block).buffer(buffer, RenderType::entityCutout);

           /* Shape1.render(matrixStack,builder,combinedLight,combinedOverlay);
            Shape2.render(matrixStack,builder,combinedLight,combinedOverlay);
            Shape3.render(matrixStack,builder,combinedLight,combinedOverlay);*/

            TileEntityModel model = getModel(block);

            if(model != null){
                model.render(matrixStack,builder,combinedLight,combinedOverlay);
            }

            matrixStack.popPose();
        }

    }

    public RenderMaterial getRenderMaterial(Block block){
        ResourceLocation res = TEXTURE_RED;
        if(block == ModBlocks.SEDAN_RED.get()){
            res = TEXTURE_RED;
        }
        else if(block == ModBlocks.SEDAN_BLACK.get()){
            res = TEXTURE_BLACK;
        }
        else if(block == ModBlocks.SEDAN_BLUE.get()){
            res = TEXTURE_BLUE;
        }
        else if(block == ModBlocks.SEDAN_GREEN.get()){
            res = TEXTURE_GREEN;
        }
        else if(block == ModBlocks.SEDAN_WHITE.get()){
            res = TEXTURE_WHITE;
        }
        else if(block == ModBlocks.SEDAN_YELLOW.get()){
            res = TEXTURE_YELLOW;
        }
        else if(block == ModBlocks.POLICE_CAR.get()){
            res = TEXTURE_POLICE;
        }
        else if(block == ModBlocks.AMBULANCE.get()){
            res = TEXTURE_AMBULANCE;
        }


        return new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, res);
    }

    public TileEntityModel getModel(Block block){
        if(block instanceof BlockSedan){
            return SEDAN_MODEL;
        }
        if(block instanceof BlockPoliceCar){
            return POLICE_CAR_MODEL;
        }
        if(block instanceof BlockAmbulance){
            return AMBULANCE_MODEL;
        }
        return null;
    }

}
