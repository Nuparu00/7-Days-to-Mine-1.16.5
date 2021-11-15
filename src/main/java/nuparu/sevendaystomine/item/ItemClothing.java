package nuparu.sevendaystomine.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.config.EnumQualityState;
import nuparu.sevendaystomine.init.ModItemGroups;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.util.ColorDetector;
import nuparu.sevendaystomine.util.ItemUtils;

import javax.annotation.Nullable;
import java.util.List;

public abstract class ItemClothing extends DyeableArmorItem implements IQuality {

    public ResourceLocation texture;
    public ResourceLocation overlay;
    public float scale = 0f;
    public boolean isDyeable = false;
    public boolean hasOverlay = false;
    public int defaultColor = 16777215;
    @OnlyIn(Dist.CLIENT)
    protected PlayerModel<?> model;
    private EnumMaterial material = EnumMaterial.CLOTH;
    private int weight = 2;

    public ItemClothing(IArmorMaterial materialIn, EquipmentSlotType equipmentSlotIn, Item.Properties properties) {
        super(materialIn, equipmentSlotIn, properties.tab(ModItemGroups.TAB_CLOTHING));
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return SevenDaysToMine.MODID + ":textures/misc/empty.png";
    }

    @Override
    public boolean isValidRepairItem(ItemStack armor, ItemStack stack) {
        return stack.getItem() == ModItems.CLOTH.get();
    }

    @Override
    public int getColor(ItemStack p_200886_1_) {
        CompoundNBT compoundnbt = p_200886_1_.getTagElement("display");
        return compoundnbt != null && compoundnbt.contains("color", 99) ? compoundnbt.getInt("color") : 0xffffff;
    }

    @Override
    public ITextComponent getName(ItemStack itemstack) {
        ITextComponent textComponent = super.getName(itemstack);
        if (isDyeable) {
            int c = getColor(itemstack);
            float r = (float) (c >> 16 & 255);
            float g = (float) (c >> 8 & 255);
            float b = (float) (c & 255);
            String colorName = ColorDetector.INSTANCE.getColorMatch(r, g, b);
            if (colorName != null) {

                StringTextComponent itemText = new StringTextComponent(textComponent.getString());
                TranslationTextComponent colorText = new TranslationTextComponent(
                        SevenDaysToMine.proxy.localize(colorName));
                if (EnumQualityState.isQualitySystemOn()) {
                    EnumQuality quality = ItemUtils.getQualityTierFromStack(itemstack);
                    itemText.setStyle(itemText.getStyle().withColor(quality.getColor()));
                    colorText.setStyle(colorText.getStyle().withColor(quality.getColor()));
                }

                return colorText.append(new StringTextComponent(" ")).append(itemText);
            }

        }
        return textComponent;
    }

    @SuppressWarnings("unchecked")
    @OnlyIn(Dist.CLIENT)
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack,
                                                     EquipmentSlotType armorSlot, A _default) {
        //if (model == null) {

            model = new PlayerModel(0.35f, false);
        //}
        return (A) model;
    }

    public void setMaterial(EnumMaterial mat) {
        material = mat;
    }

    public EnumMaterial getItemMaterial() {
        return material;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int newWeight) {
        weight = newWeight;
    }

    public boolean canBeScraped() {
        return true;
    }

    @Override
    public int getQuality(ItemStack stack) {
        CompoundNBT nbt = stack.getOrCreateTag();
        if (nbt != null) {
            if (nbt.contains("Quality")) {
                return nbt.getInt("Quality");
            }
        }
        return 0;
    }

    @Override
    public EnumQuality getQualityTierFromStack(ItemStack stack) {
        return getQualityTierFromInt(getQuality(stack));
    }

    @Override
    public EnumQuality getQualityTierFromInt(int quality) {
        return EnumQuality.getFromQuality(quality);
    }

    @Override
    public void setQuality(ItemStack stack, int quality) {
        CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putInt("Quality", quality);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
                                ITooltipFlag flagIn) {
        if (!EnumQualityState.isQualitySystemOn())
            return;
        int quality = getQuality(stack);
        EnumQuality tier = getQualityTierFromInt(quality);
        TranslationTextComponent qualityTitle = new TranslationTextComponent(
                "stat.quality." + tier.name().toLowerCase());
        IFormattableTextComponent qualityValue = new TranslationTextComponent("stat.quality",quality);

        Style style = qualityTitle.getStyle().withColor(tier.color);
        qualityTitle.setStyle(style);
        qualityValue.setStyle(style);
        tooltip.add(qualityTitle);
        tooltip.add(qualityValue);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void fillItemCategory(ItemGroup tab, NonNullList<ItemStack> items) {
        if (this.allowdedIn(tab)) {
            PlayerEntity player = Minecraft.getInstance().player;
            ItemStack stack = new ItemStack(this);
            if (player != null) {
                ItemQuality.setQualityForPlayer(stack, player);
            }
            items.add(stack);
        }

    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        if (!EnumQualityState.isQualitySystemOn())
            return super.getRGBDurabilityForDisplay(stack);
        switch (getQualityTierFromStack(stack)) {
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
            case NONE:
            default:
                return super.getRGBDurabilityForDisplay(stack);
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
        Multimap<Attribute, AttributeModifier> multimap = super.getDefaultAttributeModifiers(equipmentSlot);

        if (equipmentSlot == this.slot) {
            multimap.put(Attributes.ARMOR, new AttributeModifier(
                    ARMOR_MODIFIER_UUID_PER_SLOT[equipmentSlot.getIndex()], "Armor modifier", this.getDefense(), AttributeModifier.Operation.ADDITION));
            multimap.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(
                    ARMOR_MODIFIER_UUID_PER_SLOT[equipmentSlot.getIndex()], "Armor toughness", this.getToughness(), AttributeModifier.Operation.ADDITION));
        }

        return multimap;
    }

    public double getDamageReduction(ItemStack stack) {
        return this.getDefense() * (1 + ((float) getQuality(stack) / (float) CommonConfig.maxQuality.get()));
    }

    public double getToughness(ItemStack stack) {
        return this.getToughness() / (1 + ((float) getQuality(stack) / (float) CommonConfig.maxQuality.get()));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack)
    {
        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.<Attribute, AttributeModifier>create();

        if (slot == this.slot) {
            if (!EnumQualityState.isQualitySystemOn()) {
                multimap.put(Attributes.ARMOR, new AttributeModifier(
                        ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()], "Armor modifier", this.getDefense(), AttributeModifier.Operation.ADDITION));
                multimap.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(
                        ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()], "Armor toughness", this.getToughness(), AttributeModifier.Operation.ADDITION));

            } else {
                multimap.put(Attributes.ARMOR, new AttributeModifier(
                        ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()], "Armor modifier", getDamageReduction(stack), AttributeModifier.Operation.ADDITION));
                multimap.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(
                        ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()], "Armor toughness", getToughness(stack), AttributeModifier.Operation.ADDITION));
            }
        }

        return multimap;
    }

    @Override
    public void onCraftedBy(ItemStack itemstack, World world, PlayerEntity player) {
        setQuality(itemstack,
                (int) Math.min(Math.max(Math.floor(player.totalExperience / CommonConfig.xpPerQuality.get()), 1),
                        CommonConfig.maxQuality.get()));
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        int i = 0;
        if (stack.getOrCreateTag() != null) {
            i = getQuality(stack);
        }
        return super.getMaxDamage(stack) + i;
    }
}
