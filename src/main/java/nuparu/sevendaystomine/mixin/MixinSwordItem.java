package nuparu.sevendaystomine.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.SwordItem;
import nuparu.sevendaystomine.config.EnumQualityState;
import nuparu.sevendaystomine.item.IQuality;
import nuparu.sevendaystomine.util.ModConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;

@Mixin(SwordItem.class)
public abstract class MixinSwordItem {


    @Shadow public abstract float getDamage();

    @Inject(method = "Lnet/minecraft/item/SwordItem;getDefaultAttributeModifiers(Lnet/minecraft/inventory/EquipmentSlotType;)Lcom/google/common/collect/Multimap;", at = @At("HEAD"), cancellable = true, remap = ModConstants.REMAP)
    public void getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
        SwordItem thys = ((SwordItem) (Object) this);
        if (!(thys instanceof IQuality) && EnumQualityState.isVanillaOn()) {
            if (equipmentSlot == EquipmentSlotType.MAINHAND) {

                Multimap<Attribute, AttributeModifier> map = HashMultimap.<Attribute, AttributeModifier>create();
                float speed = 4;
                Iterator<AttributeModifier> it = map.get(Attributes.ATTACK_SPEED).iterator();
                while(it.hasNext()){
                    AttributeModifier modifier = it.next();
                    if(modifier.getOperation() == AttributeModifier.Operation.ADDITION){
                        speed+=modifier.getAmount();
                    }
                }

                map.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(thys.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", thys.getDamage(), AttributeModifier.Operation.ADDITION));
                map.put(Attributes.ATTACK_SPEED, new AttributeModifier(thys.BASE_ATTACK_SPEED_UUID, "Weapon modifier", speed, AttributeModifier.Operation.ADDITION));

                cir.setReturnValue(map);
            }
        }
    }
/*
    @Inject(method = "Lnet/minecraft/item/SwordItem;<init>(Lnet/minecraft/item/IItemTier;IFLnet/minecraft/item/Item$Properties;)V", at = @At("RETURN"), remap = false)
    public void init(IItemTier p_i48460_1_, int p_i48460_2_, float p_i48460_3_, Item.Properties p_i48460_4_, CallbackInfo ci) {
        SwordItem thys = ((SwordItem) (Object) this);
        System.out.println("SWORD " + p_i48460_1_.toString() + " " + p_i48460_3_);
        ItemUtils.swordSpeedCached.put(thys,p_i48460_3_);
    }
*/


}
