package nuparu.sevendaystomine.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.jigsaw.IJigsawDeserializer;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.gen.deserializers.ModJigsawPiece;

public class ModStructurePoolElements {
    public static IJigsawDeserializer<ModJigsawPiece> MODDED = () -> ModJigsawPiece.CODEC;

    public static void register(){
        Registry.register(Registry.STRUCTURE_POOL_ELEMENT, new ResourceLocation(SevenDaysToMine.MODID, "mod_single_pool_element"), MODDED);
    }
}
