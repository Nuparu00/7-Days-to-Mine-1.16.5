package nuparu.sevendaystomine.item;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.util.Utils;

public class ItemFuelTool extends ItemQualityTool implements IReloadable {

	public SoundEvent refillSound;

	public ItemFuelTool(float attackDamageIn, float attackSpeedIn, IItemTier materialIn, Set<Block> effectiveBlocksIn, Item.Properties properties) {
		super(attackDamageIn, attackSpeedIn, materialIn, effectiveBlocksIn,properties);
	}


	@Override
	public void onCraftedBy(ItemStack itemstack, World world, PlayerEntity player) {
		super.onCraftedBy(itemstack, world, player);
		initNBT(itemstack);

	}

	public void initNBT(ItemStack itemstack) {
		itemstack.getOrCreateTag().putInt("FuelMax", 1000);
		itemstack.getOrCreateTag().putInt("FuelCurrent", 0);
		itemstack.getOrCreateTag().putInt("ReloadTime", 0);
		itemstack.getOrCreateTag().putBoolean("Reloading", false);
	}

	@Override
	public void onReloadStart(World world, PlayerEntity player, ItemStack stack, int reloadTime) {
		stack.getOrCreateTag().putLong("NextFire",
				world.getGameTime() + (long) Math.ceil((reloadTime / 1000d) * 20));
	}

	@Override
	public void onReloadEnd(World world, PlayerEntity player, ItemStack stack, ItemStack bullet) {
		if (bullet != null && !bullet.isEmpty() && stack.getOrCreateTag().contains("FuelCurrent")
				&& stack.getOrCreateTag().contains("FuelMax")) {

			stack.getOrCreateTag().putBoolean("Reloading", false);
			int toReload = getCapacity(stack,player) - getAmmo(stack,player);
			int reload = Math.min((int)Math.floor(toReload/5), Utils.getItemCount(player.inventory, bullet.getItem()));

			setAmmo(stack, player, getAmmo(stack, player) + reload * 5);
			Utils.clearMatchingItems(player.inventory, bullet.getItem(), reload);
		}
	}

	@Override
	public int getAmmo(ItemStack stack, PlayerEntity player) {
		if (stack == null || stack.isEmpty() || stack.getOrCreateTag() == null
				|| !stack.getOrCreateTag().contains("FuelMax"))
			return -1;
		return stack.getOrCreateTag().getInt("FuelMax");
	}

	@Override
	public int getCapacity(ItemStack stack, PlayerEntity player) {
		return 1000;
	}

	@Override
	public void setAmmo(ItemStack stack, @Nullable PlayerEntity player, int ammo) {
		stack.getOrCreateTag().putInt("FuelMax", ammo);
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
		if (stack.getOrCreateTag() != null && stack.getOrCreateTag().contains("FuelCurrent")
				&& stack.getOrCreateTag().getInt("FuelCurrent") < 0F) {

			stack.getOrCreateTag().putInt("FuelCurrent", 0);
		}

	}

	@Override
	public boolean mineBlock(ItemStack stack, World worldIn, BlockState state, BlockPos pos,
			LivingEntity entityLiving) {
		CompoundNBT nbt = stack.getOrCreateTag();
		setAmmo(stack, null, getAmmo(stack, null) - 1);
		return !worldIn.isClientSide();
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
    {
        return 1-((double)getAmmo(stack,null) / (double)getCapacity(stack,null));
    }
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
    {
        return true;
    }
	

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);

		if (stack == null)
			return;
		tooltip.add(new StringTextComponent(getAmmo(stack,null) + "/" + getCapacity(stack,null)));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void fillItemCategory(ItemGroup tab, NonNullList<ItemStack> items) {
		if (this.allowdedIn(tab)) {
			PlayerEntity player = Minecraft.getInstance().player;
			ItemStack stack = new ItemStack(this, 1);
			if (player != null) {
				setQuality(stack,
						(int) Math.min(Math.max(Math.floor(player.totalExperience / CommonConfig.xpPerQuality.get()), 1),
								CommonConfig.maxQuality.get()));
				CompoundNBT nbt = stack.getOrCreateTag();
				nbt.putInt("FuelMax", 1000);
				nbt.putInt("FuelCurrent", 0);
				nbt.putInt("ReloadTime", 90000);
				nbt.putBoolean("Reloading", false);
			}
			items.add(stack);
		}
	}

	@Override
	public Item getReloadItem(ItemStack stack) {
		return ModItems.GAS_CANISTER.get();
	}

	@Override
	public int getReloadTime(ItemStack stack) {
		return 200;
	}

	@Override
	public SoundEvent getReloadSound() {
		return SoundEvents.BUCKET_FILL;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
		return this.getAmmo(stack, null) > 0 ? super.onLeftClickEntity(stack, player, entity) : true;
	}

}