package nuparu.sevendaystomine.loot.condition;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.reflect.TypeToken;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;
import java.util.List;

public class QualityItemCondition implements ILootCondition
{
    public static final LootConditionType TYPE = new LootConditionType(new QualityItemCondition.Serializer());

    private QualityItemCondition()
    {

    }

    @Override
    public LootConditionType getType()
    {
        return TYPE;
    }

    @Override
    public boolean test(LootContext lootContext)
    {
        return true;
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static class Builder implements ILootCondition.IBuilder
    {

        public Builder()
        {
        }

        @Override
        public ILootCondition build()
        {
            return new QualityItemCondition();
        }
    }

    public static class Serializer implements ILootSerializer<QualityItemCondition>
    {

        @Override
        public void serialize(JsonObject object, QualityItemCondition instance, JsonSerializationContext ctx)
        {

        }

        @Override
        public QualityItemCondition deserialize(JsonObject object, JsonDeserializationContext ctx)
        {
            return new QualityItemCondition();
        }
    }
}