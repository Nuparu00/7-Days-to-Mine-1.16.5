package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.entity.FlareEntity;
import nuparu.sevendaystomine.entity.MolotovEntity;

public class MolotovRenderer<T extends MolotovEntity> extends SpriteRenderer<T> {
    public MolotovRenderer(EntityRendererManager p_i50957_1_) {
        super(p_i50957_1_, Minecraft.getInstance().getItemRenderer());
    }

    public static class RenderFactory implements IRenderFactory<MolotovEntity> {

        public RenderFactory(EntityRendererManager manager) {

        }

        @Override
        public EntityRenderer<? super MolotovEntity> createRenderFor(EntityRendererManager manager) {
            return new MolotovRenderer(manager);
        }
    }
}
