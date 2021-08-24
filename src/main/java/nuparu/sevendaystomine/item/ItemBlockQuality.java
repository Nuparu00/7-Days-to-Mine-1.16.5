package nuparu.sevendaystomine.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
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
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.config.EnumQualityState;

public class ItemBlockQuality extends BlockItem implements IQuality {

	public ItemBlockQuality(Block block, Item.Properties properties) {
		super(block,properties);
		// TODO Auto-generated constructor stub
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
		if (stack.getOrCreateTag() == null) {
			stack.setTag(new CompoundNBT());
		}
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
				ItemQuality.setQualityForPlayer(stack,player);
			}
			items.add(stack);
		}
	}

}
