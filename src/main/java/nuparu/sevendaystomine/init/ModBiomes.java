package nuparu.sevendaystomine.init;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeMaker;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;

import java.util.Objects;

public class ModBiomes {

    public static DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, SevenDaysToMine.MODID);
    public static RegistryObject<Biome> WASTELAND_PLAINS = BIOMES.register("wasteland_plains", BiomeMaker::theVoidBiome);
    public static RegistryObject<Biome> WASTELAND_FOREST = BIOMES.register("wasteland_forest", BiomeMaker::theVoidBiome);


    public static RegistryKey<Biome> createKey(RegistryObject<Biome> object) {
        return RegistryKey.create(ForgeRegistries.Keys.BIOMES,
                Objects.requireNonNull(object.getId(),
                        "Non existing biome detected!"));
    }
}
