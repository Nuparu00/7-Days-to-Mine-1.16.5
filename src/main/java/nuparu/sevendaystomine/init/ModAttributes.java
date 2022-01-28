package nuparu.sevendaystomine.init;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ModAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, SevenDaysToMine.MODID);

    public static final RegistryObject<Attribute> THIRST = ATTRIBUTES.register("thirst",() -> new RangedAttribute("sevendaystomine.thirst",1000d,0d,1000d).setSyncable(true));

}
