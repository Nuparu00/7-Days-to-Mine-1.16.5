package nuparu.sevendaystomine.init;

import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ModParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES,
            SevenDaysToMine.MODID);

    public static final RegistryObject<BasicParticleType> BLOOD = PARTICLE_TYPES.register("blood",
            () -> new BasicParticleType(false));

}
