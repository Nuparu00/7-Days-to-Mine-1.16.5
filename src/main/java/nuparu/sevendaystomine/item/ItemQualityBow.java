package nuparu.sevendaystomine.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
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
import nuparu.sevendaystomine.util.MathUtils;

public class ItemQualityBow extends BowItem implements IQuality {

	public double damageMultiplier = 1d;
	public float powerMultiplier = 1f;

	public ItemQualityBow(Item.Properties properties, double damageMultiplier, float powerMultiplier) {
		super(properties);
		this.damageMultiplier = damageMultiplier;
		this.powerMultiplier = powerMultiplier;
	}

	@Override
	public void releaseUsing(ItemStack stack, World world, LivingEntity entity, int timeLeft) {
	      if (entity instanceof PlayerEntity) {
	          PlayerEntity playerentity = (PlayerEntity)entity;
	          boolean flag = playerentity.abilities.instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
	          ItemStack itemstack = playerentity.getProjectile(stack);

	          int i = this.getUseDuration(stack) - timeLeft;
	          i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, world, playerentity, i, !itemstack.isEmpty() || flag);
	          if (i < 0) return;

	          if (!itemstack.isEmpty() || flag) {
	             if (itemstack.isEmpty()) {
	                itemstack = new ItemStack(Items.ARROW);
	             }

	             float f = getPowerForTime(i)*powerMultiplier;
	             if (!((double)f < 0.1D)) {
	                boolean flag1 = playerentity.abilities.instabuild || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, stack, playerentity));
	                if (!world.isClientSide) {
	                   ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
	                   AbstractArrowEntity abstractarrowentity = arrowitem.createArrow(world, itemstack, playerentity);
	                   abstractarrowentity = customArrow(abstractarrowentity);
	                   abstractarrowentity.shootFromRotation(playerentity, playerentity.xRot, playerentity.yRot, 0.0F, (float) (f * 3.0F * (1f + MathUtils.bias(EnumQualityState.isQualitySystemOn() ? ((float) getQuality(stack) / (float) CommonConfig.maxQuality.get()) : 1, 0.4))), 1.0F);
	                   if (f == 1.0F) {
	                      abstractarrowentity.setCritArrow(true);
	                   }

	                   int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
	                   if (j > 0) {
	                      abstractarrowentity.setBaseDamage((abstractarrowentity.getBaseDamage() + (double)j * 0.5D + 0.5D)*damageMultiplier);
	                   }

	                   int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
	                   if (k > 0) {
	                      abstractarrowentity.setKnockback(k);
	                   }

	                   if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
	                      abstractarrowentity.setSecondsOnFire(100);
	                   }

	                   stack.hurtAndBreak(1, playerentity, (p_220009_1_) -> p_220009_1_.broadcastBreakEvent(playerentity.getUsedItemHand()));
	                   if (flag1 || playerentity.abilities.instabuild && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
	                      abstractarrowentity.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
	                   }

	                   world.addFreshEntity(abstractarrowentity);
	                }

	                world.playSound(null, playerentity.getX(), playerentity.getY(), playerentity.getZ(), SoundEvents.ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
	                if (!flag1 && !playerentity.abilities.instabuild) {
	                   itemstack.shrink(1);
	                   if (itemstack.isEmpty()) {
	                      playerentity.inventory.removeItem(itemstack);
	                   }
	                }

	                playerentity.awardStat(Stats.ITEM_USED.get(this));
	             }
	          }
	       }
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
		if (stack.getOrCreateTag() == null) {
			stack.setTag(new CompoundNBT());
		}
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putInt("Quality", quality);
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
				ItemQuality.setQualityForPlayer(stack, player);
			}
			items.add(stack);
		}
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		if(!EnumQualityState.isQualitySystemOn()) return super.getRGBDurabilityForDisplay(stack);
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
/*
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		Item thisItem = toRepair.getItem();
		Item repairWith = repair.getItem();
		return (thisItem == ModItems.CRUDE_BOW.get() && repairWith == Items.STICK)
				|| (thisItem == ModItems.COMPOUND_BOW && repairWith == ModItems.IRON_PIPE);
	}*/

}
