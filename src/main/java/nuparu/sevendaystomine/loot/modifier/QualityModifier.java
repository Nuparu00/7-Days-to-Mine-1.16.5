package nuparu.sevendaystomine.loot.modifier;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.RandomRanges;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.item.ItemQuality;
import nuparu.sevendaystomine.util.ItemUtils;
import nuparu.sevendaystomine.util.PlayerUtils;

import javax.annotation.Nonnull;
import java.util.List;

public class QualityModifier extends LootModifier {

    public QualityModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }
    @Nonnull
    @Override
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        for(int i = 0; i < generatedLoot.size(); i++){
            ItemStack stack = generatedLoot.get(i);
            if(ItemUtils.isQualityItem(stack) && !stack.getOrCreateTag().contains("Quality", Constants.NBT.TAG_INT)){
                ItemQuality.setQualityForStack(stack, context.getRandom().nextInt(600)+1);
            }
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<QualityModifier> {

        @Override
        public QualityModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
            return new QualityModifier(conditionsIn);
        }

        @Override
        public JsonObject write(QualityModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            return json;
        }
    }
}
