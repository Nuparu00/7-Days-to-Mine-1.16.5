package nuparu.sevendaystomine.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ToolItem;
import nuparu.sevendaystomine.config.EnumQualityState;
import nuparu.sevendaystomine.item.IQuality;
import nuparu.sevendaystomine.util.ModConstants;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ToolItem.class)
public class MixinToolItem {
    @Shadow
    @Final
    protected float speed;

    @Inject(method = "Lnet/minecraft/item/ToolItem;getDefaultAttributeModifiers(Lnet/minecraft/inventory/EquipmentSlotType;)Lcom/google/common/collect/Multimap;", at = @At("HEAD"), cancellable = true, remap = ModConstants.REMAP)
    public void getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
        ToolItem thys = ((ToolItem) (Object) this);
        if (!(thys instanceof IQuality) && EnumQualityState.isVanillaOn()) {
            if (equipmentSlot == EquipmentSlotType.MAINHAND) {

                Multimap<Attribute, AttributeModifier> map = HashMultimap.<Attribute, AttributeModifier>create();

                map.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", thys.getAttackDamage(), AttributeModifier.Operation.ADDITION));
                map.put(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_UUID, "Weapon modifier", this.speed, AttributeModifier.Operation.ADDITION));

                cir.setReturnValue(map);
            }
        }
    }

}
