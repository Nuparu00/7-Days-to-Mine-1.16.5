package nuparu.sevendaystomine.init;

import net.minecraft.entity.item.PaintingType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.loot.condition.QualityItemCondition;
import nuparu.sevendaystomine.loot.modifier.GrassModifier;
import nuparu.sevendaystomine.loot.modifier.QualityModifier;

public class ModLootModifiers {

    public static DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, SevenDaysToMine.MODID);
    public static RegistryObject<GrassModifier.Serializer> GRASS = LOOT_MODIFIERS.register("grass",()-> new GrassModifier.Serializer());
    public static RegistryObject<QualityModifier.Serializer> QUALITY = LOOT_MODIFIERS.register("quality",()-> new QualityModifier.Serializer());

    public static void registerConditions()
    {
        Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(SevenDaysToMine.MODID, "quality_item"), QualityItemCondition.TYPE);
    }

}
