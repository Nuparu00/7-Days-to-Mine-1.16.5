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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.electricity.IBattery;
import nuparu.sevendaystomine.init.ModItemGroups;

public class ItemBattery extends ItemQualityScrapable implements IBattery{

	public static final int BASE_VOLTAGE = 2500;

	public ItemBattery(EnumMaterial mat, int weight) {
		super(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_ELECTRICITY),mat, weight);
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
			CompoundNBT nbt = stack.getOrCreateTag();
			nbt.putLong("voltage", getCapacity(stack,null));
			items.add(stack);
		}
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		CompoundNBT nbt = stack.getOrCreateTag();
		return (nbt != null && nbt.contains("voltage", Constants.NBT.TAG_LONG));
	}

	/**
	 * Queries the percentage of the 'Durability' bar that should be drawn.
	 *
	 * @param stack The current ItemStack
	 * @return 0.0 for 100% (no damage / full bar), 1.0 for 0% (fully damaged /
	 *         empty bar)
	 */
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		CompoundNBT nbt = stack.getOrCreateTag();
		if (nbt != null && nbt.contains("voltage", Constants.NBT.TAG_LONG)) {
			return 1-((double) nbt.getLong("voltage") / getCapacity(stack,null));
		}
		return 0d;
	}

	@Override
	public long getCapacity(ItemStack stack, @Nullable World world) {
		return (long) (BASE_VOLTAGE*(1+(double)(ItemQuality.getQualityFromStack(stack)/CommonConfig.maxQuality.get())));
	}

	@Override
	public long getVoltage(ItemStack stack,@Nullable World world) {
		CompoundNBT nbt = stack.getOrCreateTag();
		if (nbt != null && nbt.contains("voltage", Constants.NBT.TAG_LONG)) {
			return nbt.getLong("voltage");
		}
		return 0;
	}

	@Override
	public void setVoltage(ItemStack stack,@Nullable World world, long voltage) {
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putLong("voltage", Math.max(0,voltage));
	}

	@Override
	public long tryToAddVoltage(ItemStack stack,@Nullable World world,long deltaVoltage) {
		if(deltaVoltage < 0) return deltaVoltage;
		CompoundNBT nbt = stack.getOrCreateTag();
		if(nbt == null || !nbt.contains("voltage",Constants.NBT.TAG_LONG)) {
			setVoltage(stack,world,deltaVoltage);
		}
		long voltage = nbt.getLong("voltage");
		long difference = getCapacity(stack,world)-voltage;
		long toAdd = Math.min(deltaVoltage, difference);
		nbt.putLong("voltage", voltage+toAdd);
		return deltaVoltage-toAdd;
	}
	
	@Override
	public void drainVoltage(ItemStack stack,@Nullable World world,long deltaVoltage) {
		if(deltaVoltage < 0) return;
		CompoundNBT nbt = stack.getOrCreateTag();
		if(nbt == null || !nbt.contains("voltage",Constants.NBT.TAG_LONG)) {
			return;
		}
		nbt.putLong("voltage", Math.max(0,nbt.getLong("voltage")-deltaVoltage));
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(new StringTextComponent(this.getVoltage(stack, worldIn) + "/" + this.getCapacity(stack, worldIn)+" J"));
	}
}
