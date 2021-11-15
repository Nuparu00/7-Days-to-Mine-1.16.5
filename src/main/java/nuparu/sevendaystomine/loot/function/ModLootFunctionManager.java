package nuparu.sevendaystomine.loot.function;

import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ModLootFunctionManager {

    public static final LootFunctionType RANDOM_COLOR_TYPE = register(new ResourceLocation(SevenDaysToMine.MODID,"random_color"), new RandomColorFunction.Serializer());
    public static final LootFunctionType RANDOM_QUALITY_TYPE = register(new ResourceLocation(SevenDaysToMine.MODID,"random_quality"), new RandomQualityFunction.Serializer());
    public static final LootFunctionType RANDOM_FUEL_TYPE = register(new ResourceLocation(SevenDaysToMine.MODID,"random_fuel"), new RandomFuelFunction.Serializer());
    public static final LootFunctionType RANDOM_AMMO_TYPE = register(new ResourceLocation(SevenDaysToMine.MODID,"random_ammo"), new RandomAmmoFunction.Serializer());
    public static final LootFunctionType RANDOM_ENERGY_TYPE = register(new ResourceLocation(SevenDaysToMine.MODID,"random_energy"), new RandomEnergyFunction.Serializer());


    public static LootFunctionType register(ResourceLocation resourceLocation, ILootSerializer<? extends ILootFunction> lootSerializer) {
        return Registry.register(Registry.LOOT_FUNCTION_TYPE, resourceLocation, new LootFunctionType(lootSerializer));
    }
}
