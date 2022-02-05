package nuparu.sevendaystomine.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ModParticleType extends BasicParticleType {

    public ModParticleType(boolean p_i50791_1_) {
        super(p_i50791_1_);
    }


    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSe;
        public Factory(IAnimatedSprite spriteSet) {
            this.spriteSe = spriteSet;
        }



        @Nullable
        @Override
        public Particle createParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new BloodParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, spriteSe);
        }
    }
}