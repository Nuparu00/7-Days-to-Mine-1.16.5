package nuparu.sevendaystomine.item;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

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
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
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
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import nuparu.sevendaystomine.config.EnumQualityState;
import nuparu.sevendaystomine.config.ServerConfig;

public class ItemQualitySword extends SwordItem implements IQuality, IToolLength {

	public EnumLength length = EnumLength.LONG;
	float speed;

	public ItemQualitySword(IItemTier tier, int p_i48460_2_, float p_i48460_3_, Item.Properties properties) {
		super(tier, p_i48460_2_, p_i48460_3_, properties.tab(ItemGroup.TAB_COMBAT));
		speed = p_i48460_3_;
	}

	public ItemQualitySword(IItemTier tier, int p_i48460_2_, float p_i48460_3_, Item.Properties properties,
			EnumLength length) {
		this(tier, p_i48460_2_, p_i48460_3_, properties);
		this.length = length;
	}

	public ItemQualitySword setAttackDamage(double speed) {
		Field f_attackDamage = ObfuscationReflectionHelper.findField(SwordItem.class, "attackDamage");
		f_attackDamage.setAccessible(true);

		try {
			f_attackDamage.setFloat(this, (float) speed);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return this;
	}

	public ItemQualitySword setLength(EnumLength length) {
		this.length = length;
		return this;
	}

	public int getQuality(ItemStack stack) {
		CompoundNBT nbt = stack.getOrCreateTag();
		if (nbt != null) {
			if (nbt.contains("Quality")) {
				return nbt.getInt("Quality");
			}
		}
		return 0;
	}

	public EnumQuality getQualityTierFromStack(ItemStack stack) {
		return getQualityTierFromInt(getQuality(stack));
	}

	public EnumQuality getQualityTierFromInt(int quality) {
		return EnumQuality.getFromQuality(quality);
	}

	public void setQuality(ItemStack stack, int quality) {
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putInt("Quality", quality);
	}

	@Override
	public void onCraftedBy(ItemStack itemstack, World world, PlayerEntity player) {
		setQuality(itemstack,
				(int) Math.min(Math.max(Math.floor(player.totalExperience / ServerConfig.xpPerQuality.get()), 1),
						ServerConfig.maxQuality.get()));
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		int i = 0;
		if (EnumQualityState.isQualitySystemOn() && stack.getOrCreateTag() != null) {
			i = getQuality(stack);
		}
		return super.getMaxDamage(stack) + i;
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
	public EnumLength getLength(ItemStack stack) {
		return length;
	}

	public double getAttackDamageModified(ItemStack stack) {
		return this.getDamage() * (0.5 + 1.5 * ((float) getQuality(stack) / (float) ServerConfig.maxQuality.get()));
	}

	public double getAttackSpeedModified(ItemStack stack) {
		float speed = 4;
		Iterator<AttributeModifier> it = this.defaultModifiers.get(Attributes.ATTACK_SPEED).iterator();
		while(it.hasNext()){
			AttributeModifier modifier = it.next();
			if(modifier.getOperation() == AttributeModifier.Operation.ADDITION){
				speed+=modifier.getAmount();
			}
		}
		return speed * (0.5 + 1.5 * ((float) getQuality(stack) / (float) ServerConfig.maxQuality.get()));
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
		Multimap<Attribute, AttributeModifier> map = HashMultimap.<Attribute, AttributeModifier>create();
		if ( EnumQualityState.isQualitySystemOn()) {
			if (equipmentSlot == EquipmentSlotType.MAINHAND) {


				float speed = 4;
				Iterator<AttributeModifier> it = map.get(Attributes.ATTACK_SPEED).iterator();
				while(it.hasNext()){
					AttributeModifier modifier = it.next();
					if(modifier.getOperation() == AttributeModifier.Operation.ADDITION){
						speed+=modifier.getAmount();
					}
				}

				map.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.getDamage(), AttributeModifier.Operation.ADDITION));
				map.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", speed, AttributeModifier.Operation.ADDITION));

				return map;
			}
		}
		return map;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot,
			ItemStack stack) {
		Multimap<Attribute, AttributeModifier> multimap = HashMultimap.<Attribute, AttributeModifier>create();
		if(stack != null && EnumQualityState.isQualitySystemOn()){

			if (equipmentSlot == EquipmentSlotType.MAINHAND) {

					multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID,
							"Weapon modifier",getAttackDamageModified(stack), AttributeModifier.Operation.ADDITION));
					multimap.put(Attributes.ATTACK_SPEED,
							new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", getAttackSpeedModified(stack), AttributeModifier.Operation.ADDITION));

					return multimap;

			}
		}
		return multimap;
	}

}
