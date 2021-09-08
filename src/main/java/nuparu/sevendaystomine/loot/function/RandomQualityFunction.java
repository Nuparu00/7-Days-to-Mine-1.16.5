package nuparu.sevendaystomine.loot.function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.item.ItemClothing;
import nuparu.sevendaystomine.item.ItemQuality;
import nuparu.sevendaystomine.util.ItemUtils;
import nuparu.sevendaystomine.util.PlayerUtils;

public class RandomQualityFunction extends LootFunction {
    private final IRandomRange value;

    protected RandomQualityFunction(ILootCondition[] conditionsIn,IRandomRange randomRange) {
        super(conditionsIn);
        this.value = randomRange;
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        Item item = stack.getItem();
        if(!ItemUtils.isQualityItem(stack)) return stack;
        ItemQuality.setQualityForStack(stack, value.getInt(context.getRandom()));
        return stack;
    }

    @Override
    public LootFunctionType getType() {
        return ModLootFunctionManager.RANDOM_QUALITY_TYPE;
    }

    public static class Serializer extends LootFunction.Serializer<RandomQualityFunction> {
        public void serialize(JsonObject jsonObject, RandomQualityFunction function, JsonSerializationContext context) {
            super.serialize(jsonObject, function, context);
            jsonObject.add("quality", RandomRanges.serialize(function.value, context));
        }

        public RandomQualityFunction deserialize(JsonObject jsonObject, JsonDeserializationContext context, ILootCondition[] lootConditions) {
            IRandomRange irandomrange = RandomRanges.deserialize(jsonObject.get("quality"), context);
            return new RandomQualityFunction(lootConditions,irandomrange);
        }
    }
}
