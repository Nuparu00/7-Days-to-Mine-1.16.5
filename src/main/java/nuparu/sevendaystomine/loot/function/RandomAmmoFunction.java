package nuparu.sevendaystomine.loot.function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import nuparu.sevendaystomine.item.IReloadable;
import nuparu.sevendaystomine.item.ItemFuelTool;
import nuparu.sevendaystomine.item.ItemGun;

public class RandomAmmoFunction extends LootFunction {

    protected RandomAmmoFunction(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        Item item = stack.getItem();
        if(!(item instanceof ItemGun)) return stack;
        ItemGun reloadable = (ItemGun)item;
        reloadable.initNBT(stack);
        reloadable.setAmmo(stack,null,context.getRandom().nextInt(reloadable.getCapacity(stack,null)));
        return stack;
    }

    @Override
    public LootFunctionType getType() {
        return ModLootFunctionManager.RANDOM_AMMO_TYPE;
    }

    public static class Serializer extends LootFunction.Serializer<RandomAmmoFunction> {
        public void serialize(JsonObject jsonObject, RandomAmmoFunction function, JsonSerializationContext context) {
            super.serialize(jsonObject, function, context);
        }

        public RandomAmmoFunction deserialize(JsonObject jsonObject, JsonDeserializationContext context, ILootCondition[] lootConditions) {
            return new RandomAmmoFunction(lootConditions);
        }
    }
}
