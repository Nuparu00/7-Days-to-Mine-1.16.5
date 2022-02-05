package nuparu.sevendaystomine.client.renderer.itemstack;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.model.ShieldModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.BannerTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.model.itemstack.RiotShieldModel;

import java.util.List;

public class RiotShieldRenderer extends ItemStackTileEntityRenderer {

    private final RiotShieldModel shieldModel = new RiotShieldModel();
    private final ResourceLocation TEX = new ResourceLocation(SevenDaysToMine.MODID, "entity/riot_shield");
    private final RenderMaterial MAT = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, TEX);

    @Override
    public void renderByItem(ItemStack p_239207_1_, ItemCameraTransforms.TransformType p_239207_2_, MatrixStack matrixStack, IRenderTypeBuffer p_239207_4_, int p_239207_5_, int p_239207_6_) {
        matrixStack.pushPose();
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        matrixStack.translate(0,0,0.03125);
        IVertexBuilder ivertexbuilder = MAT.sprite().wrap(ItemRenderer.getFoilBufferDirect(p_239207_4_, this.shieldModel.renderType(MAT.atlasLocation()), true, p_239207_1_.hasFoil()));
        this.shieldModel.handle().render(matrixStack, ivertexbuilder, p_239207_5_, p_239207_6_, 1.0F, 1.0F, 1.0F, 1.0F);
        this.shieldModel.plate().render(matrixStack, ivertexbuilder, p_239207_5_, p_239207_6_, 1.0F, 1.0F, 1.0F, 1.0F);


        matrixStack.popPose();

    }
}
