package nuparu.sevendaystomine.loot.function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.conditions.ILootCondition;
import nuparu.sevendaystomine.item.ItemBattery;
import nuparu.sevendaystomine.item.ItemFuelTool;

public class RandomEnergyFunction extends LootFunction {

    protected RandomEnergyFunction(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        Item item = stack.getItem();
        if(!(item instanceof ItemBattery)) return stack;
        ItemBattery battery = (ItemBattery)item;
        battery.setVoltage(stack,context.getLevel(),battery.getCapacity(stack,context.getLevel()));
        return stack;
    }

    @Override
    public LootFunctionType getType() {
        return ModLootFunctionManager.RANDOM_ENERGY_TYPE;
    }

    public static class Serializer extends LootFunction.Serializer<RandomEnergyFunction> {
        public void serialize(JsonObject jsonObject, RandomEnergyFunction function, JsonSerializationContext context) {
            super.serialize(jsonObject, function, context);
        }

        public RandomEnergyFunction deserialize(JsonObject jsonObject, JsonDeserializationContext context, ILootCondition[] lootConditions) {
            return new RandomEnergyFunction(lootConditions);
        }
    }
}
