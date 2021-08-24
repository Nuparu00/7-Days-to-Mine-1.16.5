package nuparu.sevendaystomine.client.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.ResourceLocation;

public abstract class AnimationModelRenderer {

    public ResourceLocation registryName;

    public AnimationModelRenderer(ResourceLocation registryName){
        this.registryName = registryName;
    }

    abstract void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light);

}
