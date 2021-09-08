package nuparu.sevendaystomine.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.*;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.items.IItemHandler;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.item.EnumQuality;
import nuparu.sevendaystomine.item.IQuality;

public class ItemUtils {

	public static ItemUtils INSTANCE = new ItemUtils();
	public HashMap<EnumMaterial, Item> scrapResults = new HashMap<EnumMaterial, Item>();
	public HashMap<EnumMaterial, Item> smallestBit = new HashMap<EnumMaterial, Item>();

	public static HashMap<SwordItem, Float> swordSpeedCached = new HashMap<SwordItem, Float>();

	public void addScrapResult(EnumMaterial mat, Item item) {
		scrapResults.put(mat, item);
		addSmallestBit(mat,item);
	}
	
	public void addSmallestBit(EnumMaterial mat, Block block) {
		addSmallestBit(mat,Item.byBlock(block));
	}
	
	public void addSmallestBit(EnumMaterial mat, Item item) {
		smallestBit.put(mat, item);
	}

	public Item getScrapResult(EnumMaterial mat) {
		for (Map.Entry<EnumMaterial, Item> entry : scrapResults.entrySet()) {
			if (mat == entry.getKey()) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	public Item getSmallestBit(EnumMaterial mat) {
		for (Map.Entry<EnumMaterial, Item> entry : smallestBit.entrySet()) {
			if (mat == entry.getKey()) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	public static int getQuality(ItemStack stack) {
		CompoundNBT nbt = stack.getOrCreateTag();
		if (nbt != null) {
			if (nbt.contains("Quality")) {
				return nbt.getInt("Quality");
			}
		}
		return 0;
	}

	public static EnumQuality getQualityTierFromStack(ItemStack stack) {
		return getQualityTierFromInt(getQuality(stack));
	}

	public static EnumQuality getQualityTierFromInt(int quality) {
		return EnumQuality.getFromQuality(quality);
	}

	public static void fill(LootTable lootTable, IItemHandler inventory, LootContext lootContext) {
		List<ItemStack> list = lootTable.getRandomItems(lootContext);
		Random random = lootContext.getRandom();
		List<Integer> list1 = getAvailableSlots(inventory, random);
		lootTable.shuffleAndSplitItems(list, list1.size(), random);

		for(ItemStack itemstack : list) {
			if (list1.isEmpty()) {
				SevenDaysToMine.LOGGER.warn("Tried to over-fill a container");
				return;
			}

			if (itemstack.isEmpty()) {
				inventory.insertItem(list1.remove(list1.size() - 1), ItemStack.EMPTY,false);
			} else {
				inventory.insertItem(list1.remove(list1.size() - 1), itemstack,false);
			}
		}

	}

	private static List<Integer> getAvailableSlots(IItemHandler inventory, Random random) {
		List<Integer> list = Lists.newArrayList();

		for(int i = 0; i < inventory.getSlots(); ++i) {
			if (inventory.getStackInSlot(i).isEmpty()) {
				list.add(i);
			}
		}

		Collections.shuffle(list, random);
		return list;
	}

	public static List<Integer> getRandomEmptySlots(IItemHandler inv, Random random){
		final List<Integer> slots = new ArrayList<Integer>();
		for(int i = 0; i < inv.getSlots();i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.isEmpty()) {
				slots.add(i);
			}
		}
		return slots;
	}

	public static List<Integer> getRandomEmptySlots(IInventory inv, Random random){
		final List<Integer> slots = new ArrayList<Integer>();
		for(int i = 0; i < inv.getContainerSize() ;i++) {
			ItemStack stack = inv.getItem(i);
			if(stack.isEmpty()) {
				slots.add(i);
			}
		}
		return slots;
	}

	public static double getAttackDamageModifiedTool(ItemStack stack) {
		if(!(stack.getItem() instanceof ToolItem)) return 0;
		ToolItem tool = (ToolItem)stack.getItem();
		return tool.getAttackDamage() * (1 + ((float) getQuality(stack) / (float) CommonConfig.maxQuality.get()));
	}

	public static double getAttackSpeedModifiedTool(ItemStack stack) {
		if(!(stack.getItem() instanceof ToolItem)) return 0;
		ToolItem tool = (ToolItem)stack.getItem();

		float speed = 4;
		Iterator<AttributeModifier> it = tool.defaultModifiers.get(Attributes.ATTACK_SPEED).iterator();
		while(it.hasNext()){
			AttributeModifier modifier = it.next();
			if(modifier.getOperation() == AttributeModifier.Operation.ADDITION){
				speed+=modifier.getAmount();
			}
		}

		return speed * (1 + ((float) getQuality(stack) / (float) CommonConfig.maxQuality.get()));
	}

	public static double getAttackDamageModifiedSword(ItemStack stack) {
		if(!(stack.getItem() instanceof SwordItem)) return 0;
		SwordItem tool = (SwordItem)stack.getItem();
		return tool.getDamage() * (1f + ((float) getQuality(stack) / (float) CommonConfig.maxQuality.get()));
	}

	public static double getAttackSpeedModifiedSword(ItemStack stack) {
		if(!(stack.getItem() instanceof SwordItem)) return 0;
		SwordItem sword = (SwordItem)stack.getItem();
		float speed = 4;
		Iterator<AttributeModifier> it = sword.defaultModifiers.get(Attributes.ATTACK_SPEED).iterator();
		while(it.hasNext()){
			AttributeModifier modifier = it.next();
			if(modifier.getOperation() == AttributeModifier.Operation.ADDITION){
				speed+=modifier.getAmount();
			}
		}
		return speed * (1 + ((float) getQuality(stack) / (float) CommonConfig.maxQuality.get()));
	}

	public static double getDamageReduction(ItemStack stack) {
		if(!(stack.getItem() instanceof ArmorItem)) return 0;
		ArmorItem armor = (ArmorItem)stack.getItem();
		return armor.getDefense() * (((float) getQuality(stack) / (float) CommonConfig.maxQuality.get()));
	}

	public static double getToughness(ItemStack stack) {
		if(!(stack.getItem() instanceof ArmorItem)) return 0;
		ArmorItem armor = (ArmorItem)stack.getItem();
		return armor.getToughness() * (((float) getQuality(stack) / (float) CommonConfig.maxQuality.get()));
	}

	//Returns whether given item is SUITABLE for quality, not whether it actually has a value
	public static boolean isQualityItem(ItemStack stack){
		Item item = stack.getItem();
		return item instanceof IQuality || PlayerUtils.isVanillaItemSuitableForQuality(item);
	}


}