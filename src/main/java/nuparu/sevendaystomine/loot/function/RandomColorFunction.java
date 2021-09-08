package nuparu.sevendaystomine.loot.function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.SetContents;
import net.minecraft.util.JSONUtils;
import nuparu.sevendaystomine.item.ItemClothing;

import java.util.Arrays;

public class RandomColorFunction extends LootFunction {

    protected RandomColorFunction(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        Item item = stack.getItem();
        if (!(item instanceof ItemClothing))
            return stack;
        ItemClothing clothing = (ItemClothing)item;
        if(!clothing.isDyeable)
            return stack;
        clothing.setColor(stack, context.getRandom().nextInt(0xffffff + 1));

        return stack;
    }

    @Override
    public LootFunctionType getType() {
        return ModLootFunctionManager.RANDOM_COLOR_TYPE;
    }

    public static class Serializer extends LootFunction.Serializer<RandomColorFunction> {
        public void serialize(JsonObject jsonObject, RandomColorFunction function, JsonSerializationContext context) {
            super.serialize(jsonObject, function, context);
        }

        public RandomColorFunction deserialize(JsonObject jsonObject, JsonDeserializationContext context, ILootCondition[] lootConditions) {
            return new RandomColorFunction(lootConditions);
        }
    }
}
