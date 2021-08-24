package nuparu.sevendaystomine.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import nuparu.sevendaystomine.config.EnumQualityState;
import nuparu.sevendaystomine.item.IQuality;
import nuparu.sevendaystomine.item.ItemArmorBase;
import nuparu.sevendaystomine.util.ModConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorItem.class)
public class MixinArmorItem {


    @Inject(method = "Lnet/minecraft/item/ArmorItem;getDefaultAttributeModifiers(Lnet/minecraft/inventory/EquipmentSlotType;)Lcom/google/common/collect/Multimap;", at = @At("HEAD"), cancellable = true, remap = ModConstants.REMAP)
    public void getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
        ArmorItem thys = ((ArmorItem) (Object) this);
        if (!(thys instanceof IQuality) && EnumQualityState.isVanillaOn()) {
            Multimap<Attribute, AttributeModifier> multimap = HashMultimap.<Attribute, AttributeModifier>create();

            if (equipmentSlot == thys.getSlot()) {
                multimap.put(Attributes.ARMOR, new AttributeModifier(
                        ItemArmorBase.ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor modifier", (double) thys.getDefense(), AttributeModifier.Operation.ADDITION));
                multimap.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(
                        ItemArmorBase.ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor toughness", (double) thys.getToughness(), AttributeModifier.Operation.ADDITION));
            }
            cir.setReturnValue(multimap);
        }
    }

}
