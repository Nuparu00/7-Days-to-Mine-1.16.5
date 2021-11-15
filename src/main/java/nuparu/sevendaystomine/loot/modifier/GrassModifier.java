package nuparu.sevendaystomine.loot.modifier;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class GrassModifier extends LootModifier {
    private final Item item;

    public GrassModifier(ILootCondition[] conditionsIn, Item item) {
        super(conditionsIn);
        this.item = item;
    }
    @Nonnull
    @Override
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {

        generatedLoot.add(new ItemStack(item));
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<GrassModifier> {

        @Override
        public GrassModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation((JSONUtils.getAsString(object, "item"))));
            return new GrassModifier(conditionsIn, item);
        }

        @Override
        public JsonObject write(GrassModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.addProperty("item", ForgeRegistries.ITEMS.getKey(instance.item).toString());
            return json;
        }
    }
}
