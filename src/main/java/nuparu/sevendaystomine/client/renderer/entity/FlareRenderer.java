package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.entity.FlareEntity;

public class FlareRenderer<T extends FlareEntity> extends SpriteRenderer<T> {
    public FlareRenderer(EntityRendererManager p_i50957_1_) {
        super(p_i50957_1_, Minecraft.getInstance().getItemRenderer());
    }

    public static class RenderFactory implements IRenderFactory<FlareEntity> {

        public RenderFactory(EntityRendererManager manager) {

        }

        @Override
        public EntityRenderer<? super FlareEntity> createRenderFor(EntityRendererManager manager) {
            return new FlareRenderer(manager);
        }
    }
}
