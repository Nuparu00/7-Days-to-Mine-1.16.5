package nuparu.sevendaystomine.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.config.EnumQualityState;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.util.MathUtils;

public class ItemQuality extends Item implements IQuality {

	public ItemQuality(Item.Properties properties) {
		super(properties);
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
			ItemStack stack = new ItemStack(this, 1);
			if (player != null) {
				setQualityForPlayer(stack, player);
			}
			items.add(stack);
		}
	}

	public static ItemStack setQualityForPlayer(ItemStack stack, PlayerEntity player) {
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putInt("Quality", (int) MathUtils.clamp(player.totalExperience / ServerConfig.xpPerQuality.get(), 1,
				ServerConfig.maxQuality.get()));

		return stack;
	}

	public static int getQualityForPlayer(PlayerEntity player) {
		return (int) MathUtils.clamp(player.totalExperience / ServerConfig.xpPerQuality.get(), 1,
				ServerConfig.maxQuality.get());
	}

	public static ItemStack setQualityForStack(ItemStack stack, int quality) {
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putInt("Quality", quality);

		return stack;
	}

	/*
	 * Static version of ItemQuality#getQuality(), should replace the non-static one
	 * in the future
	 */
	public static int getQualityFromStack(ItemStack stack) {
		CompoundNBT nbt = stack.getOrCreateTag();
		if (nbt != null) {
			if (nbt.contains("Quality")) {
				return nbt.getInt("Quality");
			}
		}
		return 0;
	}

	public static boolean hasQualityTag(ItemStack stack) {
		return stack.getOrCreateTag() != null && stack.getOrCreateTag().contains("Quality", Constants.NBT.TAG_INT);
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
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
	public void onCraftedBy(ItemStack itemstack, World world, PlayerEntity player) {
		if (this.getQuality(itemstack) <= 0) {
			setQuality(itemstack,
					(int) Math.min(Math.floor(player.totalExperience / ServerConfig.xpPerQuality.get()),
							ServerConfig.maxQuality.get()));
		}
	}

}
