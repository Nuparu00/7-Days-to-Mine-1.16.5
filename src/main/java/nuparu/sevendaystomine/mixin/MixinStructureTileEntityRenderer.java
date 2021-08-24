package nuparu.sevendaystomine.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.StructureTileEntityRenderer;
import net.minecraft.state.properties.StructureMode;
import net.minecraft.tileentity.StructureBlockTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.ForgeRenderTypes;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.util.ModConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructureTileEntityRenderer.class)
public class MixinStructureTileEntityRenderer {
    @Inject(method = "Lnet/minecraft/client/renderer/tileentity/StructureTileEntityRenderer;render(Lnet/minecraft/tileentity/StructureBlockTileEntity;FLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;II)V", at = @At("HEAD"), remap = ModConstants.REMAP)
    public void render(StructureBlockTileEntity te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay, CallbackInfo ci) {
        if (te.getMode() == StructureMode.DATA) {
            String text = te.getMetaData();
            StringTextComponent component = new StringTextComponent(text);
            component.setStyle(component.getStyle().withColor(TextFormatting.GREEN));
            RenderUtils.renderNameTag(new Vector3d(te.getBlockPos().getX(), te.getBlockPos().getY(), te.getBlockPos().getZ()), component, matrixStack, buffer, 0xffffff);
        }
    }
}
