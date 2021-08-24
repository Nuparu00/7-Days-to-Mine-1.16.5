package nuparu.sevendaystomine.client.model.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

public abstract class TileEntityModel {
    public abstract void render(MatrixStack matrixStack, IVertexBuilder builder, int combinedLight, int combinedOverlay);
}
