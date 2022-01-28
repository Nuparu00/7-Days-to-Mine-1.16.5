package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.entity.ChlorineGrenadeEntity;
import nuparu.sevendaystomine.entity.FlareEntity;

public class ChlorineGrenadeRenderer<T extends ChlorineGrenadeEntity> extends SpriteRenderer<T> {
    public ChlorineGrenadeRenderer(EntityRendererManager p_i50957_1_) {
        super(p_i50957_1_, Minecraft.getInstance().getItemRenderer());
    }

    public static class RenderFactory implements IRenderFactory<ChlorineGrenadeEntity> {

        public RenderFactory(EntityRendererManager manager) {

        }

        @Override
        public EntityRenderer<? super ChlorineGrenadeEntity> createRenderFor(EntityRendererManager manager) {
            return new ChlorineGrenadeRenderer(manager);
        }
    }
}
