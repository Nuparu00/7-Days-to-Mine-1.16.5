package nuparu.sevendaystomine.item;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import nuparu.sevendaystomine.advancements.ModTriggers;
import nuparu.sevendaystomine.block.IUpgradeable;
import nuparu.sevendaystomine.block.repair.BreakData;
import nuparu.sevendaystomine.block.repair.RepairDataManager;
import nuparu.sevendaystomine.block.repair.RepairEntry;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IChunkData;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.util.VanillaManager;

public class ItemStoneAxe extends ItemQualityAxe implements IUpgrader {

	public float effect = 1f;

	public ItemStoneAxe(IItemTier tier, float p_i48530_2_, float p_i48530_3_, Item.Properties properties) {
		super(tier, p_i48530_2_, p_i48530_3_, properties.addToolType(ToolType.PICKAXE,0));
	}

	@Override
	public IUpgrader setEffectiveness(float effect) {
		this.effect = effect;
		return this;
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		PlayerEntity playerIn = context.getPlayer();
		Hand hand = context.getHand();
		World worldIn = context.getLevel();
		ItemStack itemstack = playerIn.getItemInHand(hand);

		if (playerIn.isCrouching()) {
			return super.useOn(context);
		}

		BlockPos pos = new BlockPos(itemstack.getOrCreateTag().getInt("X"),
				itemstack.getOrCreateTag().getInt("Y"), itemstack.getOrCreateTag().getInt("Z"));

		BlockState state = worldIn.getBlockState(pos);
		Block block = state.getBlock();

		/*BreakSavedData data = BreakSavedData.get(worldIn);
		BreakData breakData = data.getBreakData(pos, worldIn.dimension().location());
		if (breakData != null && breakData.getState() != 0f) {
			float damage = breakData.getState();
			Repair repair = RepairManager.INSTANCE.getRepair(block, playerIn.inventory);
			if (repair != null) {
				for (int slot = 0; slot < playerIn.inventory.getContainerSize(); slot++) {
					ItemStack stack = playerIn.inventory.getItem(slot);
					if (ItemStack.isSame(stack, repair.getItemStack())) {
						float size = (repair.getPercentage() * 10) * (effect);
						if (damage < (effect)) {
							size = damage * (effect / 10);
						}
						if (stack.getCount() < (int) Math.floor(size * 10f)) {
							continue;
						}
						stack.shrink((int) Math.ceil(size * 10f));
						worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), ModSounds.UPGRADE_WOOD,
								SoundCategory.BLOCKS, MathUtils.getFloatInRange(0.5f, 0.75f),
								MathUtils.getFloatInRange(0.9f, 1f));
						playerIn.swing(hand);
						if (playerIn instanceof ServerPlayerEntity) {
							itemstack.hurt(1, random, (ServerPlayerEntity) playerIn);
						}
						if (damage - (effect / 1f) <= 0f) {
							data.removeBreakData(pos, worldIn);
							break;
						} else {
							data.setBreakData(pos, worldIn, damage - (effect / 1f));
							break;
						}

					} else {
						if (slot == playerIn.inventory.getContainerSize() - 1) {
							if (!worldIn.isClientSide() && !playerIn.isCreative() && !playerIn.isSpectator()) {
								playerIn.sendMessage(new TranslationTextComponent("repair.missing",
												(repair.getItemStack().getItem().getName(repair.getItemStack()).toString()),
												worldIn.getBlockState(pos).getBlock().getName().toString()),Util.NIL_UUID);
							}
						}
					}
				}
			}
		} else */

		IChunkData iChunkData = CapabilityHelper.getChunkData(worldIn.getChunkAt(pos));
		if (iChunkData != null && iChunkData.hasBreakData(pos)) {
			BreakData breakData = iChunkData.getBreakData(pos);
			float damage = breakData.getState();
			NonNullList<RepairEntry> repairs = RepairDataManager.instance.getEntries(block);

			if (!worldIn.isClientSide()) {
				if (repairs.isEmpty()) {
					playerIn.sendMessage(new TranslationTextComponent("repair.none", block.getName()), Util.NIL_UUID);
				}
				for (RepairEntry entry : repairs) {
					if (entry.getRepairLimit() >= 1 || ((1 - damage) + (entry.getRepairAmount() * effect) <= entry.getRepairLimit())) {
						if (!playerIn.isCreative()) {
							boolean flag = true;
							for (ItemStack stack : entry.getItems()) {
								if (!Utils.hasItemStack(playerIn, stack)) {
									flag = !flag;
									playerIn.sendMessage(new TranslationTextComponent("repair.missing.count", stack.getDisplayName(), block.getName(), stack.getCount()), Util.NIL_UUID);
									break;
								}
							}

							if (!flag) continue;


							for (ItemStack stack : entry.getItems()) {
								Utils.removeItemStack(playerIn.inventory, stack);
							}
						}

						playerIn.swing(hand);
						if (playerIn instanceof ServerPlayerEntity) {
							itemstack.hurt(1, random, (ServerPlayerEntity) playerIn);
						}

						iChunkData.addBreakData(pos, (float) -(entry.getRepairAmount() * effect));

						SoundType soundType = block.getSoundType(state, worldIn, pos, null);
						if (soundType != null) {
							worldIn.playSound(null, pos, soundType.getFallSound(), SoundCategory.NEUTRAL,
									(soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
						}
						break;
					}
				}
			}

		} else if (block instanceof IUpgradeable && ((IUpgradeable) block).getResult(worldIn, pos) != null) {
			IUpgradeable upgradeable = ((IUpgradeable) block);
			ItemStack[] itemStacks = upgradeable.getItems();

			if (hasItemStacks(playerIn, block, itemStacks) || playerIn.isCreative()) {
				worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), ModSounds.UPGRADE_WOOD.get(),
						SoundCategory.BLOCKS, MathUtils.getFloatInRange(0.5f, 0.75f),
						MathUtils.getFloatInRange(0.9f, 1f));
				itemstack.getOrCreateTag().putFloat("Percent", itemstack.getOrCreateTag().getFloat("Percent") + effect);
				playerIn.swing(hand);
				if (itemstack.getOrCreateTag().getFloat("Percent") >= 1F) {
					if (playerIn instanceof ServerPlayerEntity) {
						itemstack.hurt(1, random, (ServerPlayerEntity) playerIn);
					}
					upgradeable.onUpgrade(worldIn, pos, state);
					worldIn.setBlock(pos, upgradeable.getResult(worldIn, pos), 3);
					if (!worldIn.isClientSide()) {
						ModTriggers.BLOCK_UPGRADE.trigger((ServerPlayerEntity) playerIn, o -> true, state);
					}
					itemstack.getOrCreateTag().putFloat("Percent", 0F);
					if (!playerIn.isCreative()) {
						removeItemStacks(playerIn.inventory, itemStacks);
					}
				}
			}
		} else if (VanillaManager.getVanillaUpgrade(state) != null) {
			VanillaManager.VanillaBlockUpgrade upgrade = VanillaManager.getVanillaUpgrade(state);
			ItemStack[] itemStacks = upgrade.getItems();
			if (hasItemStacks(playerIn, block, itemStacks) || playerIn.isCreative()) {
				worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), ModSounds.UPGRADE_WOOD.get(),
						SoundCategory.BLOCKS, MathUtils.getFloatInRange(0.5f, 0.75f),
						MathUtils.getFloatInRange(0.9f, 1f));
				itemstack.getOrCreateTag().putFloat("Percent", itemstack.getOrCreateTag().getFloat("Percent") + effect);
				playerIn.swing(hand);
				if (itemstack.getOrCreateTag().getFloat("Percent") >= 1F) {
					if (playerIn instanceof ServerPlayerEntity) {
						itemstack.hurt(1, random, (ServerPlayerEntity) playerIn);
					}
					worldIn.setBlock(pos, upgrade.getResult(), 3);
					if (!worldIn.isClientSide()) {
						ModTriggers.BLOCK_UPGRADE.trigger((ServerPlayerEntity) playerIn, o -> true, state);
					}
					itemstack.getOrCreateTag().putFloat("Percent", 0F);
					if (!playerIn.isCreative()) {
						removeItemStacks(playerIn.inventory, itemStacks);
					}
				}
			}
		}

		return ActionResultType.PASS;
	}

	public boolean hasItemStack(PlayerEntity player, Block block, ItemStack itemStack) {
		int count = 0;
		for (int slot = 0; slot < player.inventory.getContainerSize(); slot++) {
			ItemStack stack = player.inventory.getItem(slot);
			if (stack != null && stack.getItem() == itemStack.getItem()) {
				count += stack.getCount();
			}
		}
		return (count >= Math.ceil(itemStack.getCount() * (1f - (effect - 0.25f))));
	}

	public boolean hasItemStacks(PlayerEntity player, Block block, ItemStack[] itemStacks) {
		for (ItemStack itemStack : itemStacks) {
			if (!hasItemStack(player, block, itemStack)) {
				if (!player.level.isClientSide() && !player.isCreative() && !player.isSpectator()) {
					player.sendMessage(new TranslationTextComponent("upgrade.missing",itemStack.getItem().getName(itemStack),block.getName()), Util.NIL_UUID);
				}
				return false;
			}
		}
		return true;
	}

	public void removeItemStacks(PlayerInventory inv, ItemStack[] itemStacks) {
		for (ItemStack itemStack : itemStacks) {
			int count = (int) Math.ceil(itemStack.getCount() * (1f - (effect - 0.25f)));
			for (int slot = 0; slot < inv.getContainerSize(); slot++) {
				ItemStack stack = inv.getItem(slot);
				if (stack != null && stack.getItem() == itemStack.getItem()) {
					int decrease = Math.min(count, stack.getCount());
					stack.shrink(decrease);
					count -= decrease;
					if (count <= 0) {
						break;
					}
				}
			}
		}
	}

	@Override
	public void inventoryTick(ItemStack itemstack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (itemstack.getOrCreateTag() == null) {

			Calendar calendar = Calendar.getInstance();
			Date date = calendar.getTime();
			String pattern = "yyyy/MM/dd/HH/mm/ss/SSS";
			SimpleDateFormat f = new SimpleDateFormat(pattern);

			itemstack.setTag(new CompoundNBT());
			itemstack.getOrCreateTag().putInt("X", 0);
			itemstack.getOrCreateTag().putInt("Y", 0);
			itemstack.getOrCreateTag().putInt("Z", 0);
			itemstack.getOrCreateTag().putFloat("Percent", 0F);
			itemstack.getOrCreateTag().putString("ID", entityIn.getUUID().toString() + "" + f.format(date));
		}
		if (isSelected) {

			BlockRayTraceResult ray = Utils.rayTraceServer(entityIn, 5, 1);
			if (ray != null) {
				BlockPos blockName = ray.getBlockPos();
				if (blockName != null) {
					if (blockName.getX() != itemstack.getOrCreateTag().getInt("X")
							|| blockName.getY() != itemstack.getOrCreateTag().getInt("Y")
							|| blockName.getZ() != itemstack.getOrCreateTag().getInt("Z")) {
						itemstack.getOrCreateTag().putInt("X", blockName.getX());
						itemstack.getOrCreateTag().putInt("Y", blockName.getY());
						itemstack.getOrCreateTag().putInt("Z", blockName.getZ());
						itemstack.getOrCreateTag().putFloat("Percent", 0F);

					}
				} else {
					if (0 != itemstack.getOrCreateTag().getInt("X")
							|| 0 != itemstack.getOrCreateTag().getInt("Y")
							|| 0 != itemstack.getOrCreateTag().getInt("Z")
							|| itemstack.getOrCreateTag().getFloat("Percent") != 0f) {

						itemstack.getOrCreateTag().putInt("X", 0);
						itemstack.getOrCreateTag().putInt("Y", 0);
						itemstack.getOrCreateTag().putInt("Z", 0);
						itemstack.getOrCreateTag().putFloat("Percent", 0F);
					}
				}

			} else {
				if (0 != itemstack.getOrCreateTag().getInt("X") || 0 != itemstack.getOrCreateTag().getInt("Y")
						|| 0 != itemstack.getOrCreateTag().getInt("Z")
						|| itemstack.getOrCreateTag().getFloat("Percent") != 0f) {

					itemstack.getOrCreateTag().putInt("X", 0);
					itemstack.getOrCreateTag().putInt("Y", 0);
					itemstack.getOrCreateTag().putInt("Z", 0);
					itemstack.getOrCreateTag().putFloat("Percent", 0F);
				}
			}

		} else {
			if (0 != itemstack.getOrCreateTag().getInt("X") || 0 != itemstack.getOrCreateTag().getInt("Y")
					|| 0 != itemstack.getOrCreateTag().getInt("Z")
					|| itemstack.getOrCreateTag().getFloat("Percent") != 0f) {

				itemstack.getOrCreateTag().putInt("X", 0);
				itemstack.getOrCreateTag().putInt("Y", 0);
				itemstack.getOrCreateTag().putInt("Z", 0);
				itemstack.getOrCreateTag().putFloat("Percent", 0F);
			}
		}
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		if (!slotChanged) {
			if (oldStack.getItem() == newStack.getItem()
					&& (oldStack.getOrCreateTag().getString("ID")).equals(newStack.getOrCreateTag().getString("ID"))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void onCraftedBy(ItemStack itemstack, World world, PlayerEntity player) {

		if (itemstack.getOrCreateTag() == null) {
			itemstack.setTag(new CompoundNBT());
		}
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		String pattern = "yyyy/MM/dd/HH/mm/ss/SSS";
		SimpleDateFormat f = new SimpleDateFormat(pattern);
		itemstack.getOrCreateTag().putInt("X", 0);
		itemstack.getOrCreateTag().putInt("Y", 0);
		itemstack.getOrCreateTag().putInt("Z", 0);
		itemstack.getOrCreateTag().putFloat("Percent", 0F);
		itemstack.getOrCreateTag().putString("ID", player.getUUID() + "" + f.format(date));
	}
}
