package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nuparu.sevendaystomine.entity.CrawlerZombieEntity;
import nuparu.sevendaystomine.entity.FlameEntity;
import nuparu.sevendaystomine.entity.FragmentationGrenadeEntity;

public class FragmentationGrenadeRenderer<T extends FragmentationGrenadeEntity> extends SpriteRenderer<T> {
    public FragmentationGrenadeRenderer(EntityRendererManager p_i50957_1_) {
        super(p_i50957_1_, Minecraft.getInstance().getItemRenderer());
    }

    public static class RenderFactory implements IRenderFactory<FragmentationGrenadeEntity> {

        public RenderFactory(EntityRendererManager manager) {

        }

        @Override
        public EntityRenderer<? super FragmentationGrenadeEntity> createRenderFor(EntityRendererManager manager) {
            return new FragmentationGrenadeRenderer(manager);
        }
    }
}
