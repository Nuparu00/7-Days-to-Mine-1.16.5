package nuparu.sevendaystomine.client.model.itemstack;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class RiotShieldModel extends Model {
    private final ModelRenderer plate;
    private final ModelRenderer handle;

    public RiotShieldModel() {
        super(RenderType::entityCutout);
        texWidth = 64;
        texHeight = 64;

        plate = new ModelRenderer(this);
        plate.setPos(0.0F, 0.0F, 0.0F);
        plate.texOffs(16, 24).addBox(-6.0F, -11.0F, -2.0F, 2.0F, 8.0F, 1.0F, 0.0F, false);
        plate.texOffs(0, 0).addBox(-6.0F, -3.0F, -2.0F, 12.0F, 14.0F, 1.0F, 0.0F, false);
        plate.texOffs(22, 24).addBox(4.0F, -11.0F, -2.0F, 2.0F, 8.0F, 1.0F, 0.0F, false);
        plate.texOffs(10, 15).addBox(-4.0F, -11.0F, -2.0F, 8.0F, 4.0F, 1.0F, 0.0F, false);
        plate.texOffs(16, 20).addBox(-4.0F, -7.0F, -1.5F, 8.0F, 4.0F, 0.0F, 0.0F, false);

        handle = new ModelRenderer(this);
        handle.setPos(0.0F, 24.0F, 0.0F);
        handle.texOffs(0, 15).addBox(-1.0F, -27.0F, -1.0F, 2.0F, 6.0F, 6.0F, 0.0F, false);
    }

    public ModelRenderer plate() {
        return this.plate;
    }

    public ModelRenderer handle() {
        return this.handle;
    }

    public void renderToBuffer(MatrixStack p_225598_1_, IVertexBuilder p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
        this.plate.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
        this.handle.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
    }
}
