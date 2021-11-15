package nuparu.sevendaystomine.loot.function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import nuparu.sevendaystomine.item.ItemFuelTool;

public class RandomFuelFunction extends LootFunction {

    protected RandomFuelFunction(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        Item item = stack.getItem();
        if(!(item instanceof ItemFuelTool)) return stack;
        ItemFuelTool fuelTool = (ItemFuelTool)item;
        fuelTool.initNBT(stack);
        fuelTool.setAmmo(stack,null,context.getRandom().nextInt(fuelTool.getCapacity(stack,null)));
        return stack;
    }

    @Override
    public LootFunctionType getType() {
        return ModLootFunctionManager.RANDOM_FUEL_TYPE;
    }

    public static class Serializer extends LootFunction.Serializer<RandomFuelFunction> {
        public void serialize(JsonObject jsonObject, RandomFuelFunction function, JsonSerializationContext context) {
            super.serialize(jsonObject, function, context);
        }

        public RandomFuelFunction deserialize(JsonObject jsonObject, JsonDeserializationContext context, ILootCondition[] lootConditions) {
            return new RandomFuelFunction(lootConditions);
        }
    }
}
