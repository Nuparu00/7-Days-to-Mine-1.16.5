package nuparu.sevendaystomine.client.particle;

import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;

@OnlyIn(Dist.CLIENT)
public class ParticleBlood extends SpriteTexturedParticle {

	private final ResourceLocation texture = new ResourceLocation(SevenDaysToMine.MODID, "entity/particles/blood");

	public ParticleBlood(ClientWorld p_i232447_1_, double x, double y, double z) {
		super(p_i232447_1_, x, y, z);
	}

	public ParticleBlood(ClientWorld p_i232448_1_, double x, double y, double z,
			double motionX, double motionY, double motionZ) {
		super(p_i232448_1_, x, y, z, motionX, motionY, motionZ);
	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

}
