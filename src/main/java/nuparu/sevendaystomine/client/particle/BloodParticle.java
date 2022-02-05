package nuparu.sevendaystomine.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import nuparu.sevendaystomine.util.MathUtils;

public class BloodParticle extends SpriteTexturedParticle {
    private final IAnimatedSprite spriteSet;

    protected BloodParticle(ClientWorld world, double x, double y, double z, double vx, double vy, double vz, IAnimatedSprite spriteSet) {
        super(world, x, y, z);
        this.spriteSet = spriteSet;
        this.setSize((float) 0.2, (float) 0.2);
        this.scale(MathUtils.getFloatInRange(0.15f,0.4f));
        this.setLifetime(MathUtils.getIntInRange(120,240));
        this.gravity = 0.6f;
        this.hasPhysics = true;
        this.xd = vx * 1;
        this.yd = vy * 1;
        this.zd = vz * 1;
        this.pickSprite(spriteSet);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        /*if(stoppedByCollision && !onGround){
            xd = 0;
            zd = 0;
            gravity = 0.05f;
        }
        else{
            gravity = 0.6f;
        }*/
    }
}
