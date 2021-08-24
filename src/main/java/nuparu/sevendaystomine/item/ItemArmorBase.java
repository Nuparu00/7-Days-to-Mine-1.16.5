package nuparu.sevendaystomine.item;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.config.EnumQualityState;

public class ItemArmorBase extends ArmorItem implements IQuality {
	public static final UUID[] ARMOR_MODIFIERS = new UUID[] { UUID.fromString("ed70b160-0ade-11eb-adc1-0242ac120002"),
			UUID.fromString("ed70b3c2-0ade-11eb-adc1-0242ac120002"),
			UUID.fromString("ed70b598-0ade-11eb-adc1-0242ac120002"),
			UUID.fromString("ed70b67e-0ade-11eb-adc1-0242ac120002") };

	public ItemArmorBase(IArmorMaterial materialIn, EquipmentSlotType equipmentSlotIn, Item.Properties properties) {
		super(materialIn, equipmentSlotIn, properties);
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
	public ITextComponent getName(ItemStack itemstack) {
		ITextComponent textComponent = super.getName(itemstack);
		if (!EnumQualityState.isQualitySystemOn()) {
			return textComponent;
		}
		EnumQuality quality = getQualityTierFromStack(itemstack);
		StringTextComponent stringTextComponent = new StringTextComponent(textComponent.getString());
		stringTextComponent.setStyle(textComponent.getStyle().withColor(quality.color));
		return stringTextComponent;
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
					ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor modifier", (double) this.getDefense(), AttributeModifier.Operation.ADDITION));
			multimap.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(
					ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor toughness", (double) this.getToughness(), AttributeModifier.Operation.ADDITION));
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
						ARMOR_MODIFIERS[slot.getIndex()], "Armor modifier", (double) this.getDefense(), AttributeModifier.Operation.ADDITION));
				multimap.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(
						ARMOR_MODIFIERS[slot.getIndex()], "Armor toughness", (double) this.getToughness(), AttributeModifier.Operation.ADDITION));

			} else {
				multimap.put(Attributes.ARMOR, new AttributeModifier(
						ARMOR_MODIFIERS[slot.getIndex()], "Armor modifier", getDamageReduction(stack), AttributeModifier.Operation.ADDITION));
				multimap.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(
						ARMOR_MODIFIERS[slot.getIndex()], "Armor toughness", getToughness(stack), AttributeModifier.Operation.ADDITION));
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
