package nuparu.sevendaystomine.init;

import net.minecraft.entity.item.PaintingType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ModPaintingTypes {

    public static DeferredRegister<PaintingType> PAINTING_TYPES = DeferredRegister.create(ForgeRegistries.PAINTING_TYPES, SevenDaysToMine.MODID);
    public static RegistryObject<PaintingType> EARTH = PAINTING_TYPES.register("earth",()-> new PaintingType(32,16));

}
