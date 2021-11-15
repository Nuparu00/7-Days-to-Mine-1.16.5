package nuparu.sevendaystomine.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.extensions.IForgeItem;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.config.EnumQualityState;
import nuparu.sevendaystomine.item.ItemArmorBase;
import nuparu.sevendaystomine.item.ItemQuality;
import nuparu.sevendaystomine.util.ItemUtils;
import nuparu.sevendaystomine.util.PlayerUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(IForgeItem.class)
public interface MixinIForgeItem {

    /**
     * @author Nuparu00
     * @reason Can not inject into interface
     */
    @Overwrite(remap = false)
    default int getRGBDurabilityForDisplay(ItemStack stack){
        if(CommonConfig.qualitySystem.get() == EnumQualityState.ALL && PlayerUtils.isVanillaQualityItem(stack)){
            switch (PlayerUtils.getQualityTierFromStack(stack)) {
                case FLAWLESS:
                   return 0xA300A3;
                case GREAT:
                    return 0x4545CC;
                case FINE:
                    return 0x37A337;
                case GOOD:
                    return 0xB2B23C;
                case POOR:
                    return 0xF09900;
                case FAULTY:
                    return 0x89713C;
            }
        }
        return MathHelper.hsvToRgb(Math.max(0.0F, (float) (1.0F - ((IForgeItem) this).getDurabilityForDisplay(stack))) / 3.0F, 1.0F, 1.0F);
    }

    /**
     * @author Nuparu00
     * @reason Can not inject into interface
     */
    @Overwrite(remap = false)
    default Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot, ItemStack stack)
    {
        IForgeItem thys = ((IForgeItem) this);
        if(stack != null && CommonConfig.qualitySystem.get() == EnumQualityState.ALL && PlayerUtils.isVanillaQualityItem(stack)){
            Multimap<Attribute, AttributeModifier> multimap = HashMultimap.<Attribute, AttributeModifier>create();
            if (thys instanceof ArmorItem){
                ArmorItem armor = (ArmorItem)thys;
                if (equipmentSlot == armor.getSlot()) {
                    if (!EnumQualityState.isQualitySystemOn()) {
                        multimap.put(Attributes.ARMOR, new AttributeModifier(
                                ItemArmorBase.ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor modifier", armor.getDefense(), AttributeModifier.Operation.ADDITION));
                        multimap.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(
                                ItemArmorBase.ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor toughness", armor.getToughness(), AttributeModifier.Operation.ADDITION));

                    } else {
                        multimap.put(Attributes.ARMOR, new AttributeModifier(
                                ItemArmorBase.ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor modifier", ItemUtils.getDamageReduction(stack), AttributeModifier.Operation.ADDITION));
                        multimap.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(
                                ItemArmorBase.ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor toughness", ItemUtils.getToughness(stack), AttributeModifier.Operation.ADDITION));
                    }
                }
                return multimap;
            }
            else if (equipmentSlot == EquipmentSlotType.MAINHAND) {
                if (thys instanceof ToolItem) {
                    ToolItem tool =(ToolItem)thys;

                    multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_UUID,
                            "Weapon modifier", ItemUtils.getAttackDamageModifiedTool(stack), AttributeModifier.Operation.ADDITION));
                    multimap.put(Attributes.ATTACK_SPEED,
                            new AttributeModifier(Item.BASE_ATTACK_SPEED_UUID, "Weapon modifier", ItemUtils.getAttackSpeedModifiedTool(stack), AttributeModifier.Operation.ADDITION));

                    return multimap;
                }
                else if (thys instanceof SwordItem) {
                    SwordItem sword =(SwordItem)thys;

                    multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_UUID,
                            "Weapon modifier",ItemUtils.getAttackDamageModifiedSword(stack), AttributeModifier.Operation.ADDITION));
                    multimap.put(Attributes.ATTACK_SPEED,
                            new AttributeModifier(Item.BASE_ATTACK_SPEED_UUID, "Weapon modifier", ItemUtils.getAttackSpeedModifiedSword(stack), AttributeModifier.Operation.ADDITION));

                    return multimap;
                }
            }
        }
        return thys.getItem().getDefaultAttributeModifiers(equipmentSlot);
    }
    /**
     * @author Nuparu00
     * @reason Can not inject into interface
     */
    @Overwrite(remap = false)
    default int getMaxDamage(ItemStack stack)
    {
        Item item = ((IForgeItem) this).getItem();
        int damage = item.getMaxDamage();
        if(PlayerUtils.isVanillaItemSuitableForQuality(this) && CommonConfig.qualitySystem.get() == EnumQualityState.ALL && PlayerUtils.isVanillaQualityItem(stack)){
            if (stack.getTag() != null) {
                damage += ItemQuality.getQualityFromStack(stack);
            }
        }
        return damage;
    }

}
