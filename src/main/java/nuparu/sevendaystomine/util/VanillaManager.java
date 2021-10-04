package nuparu.sevendaystomine.util;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.crafting.RecipeManager;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.EnumMaterial;

public class VanillaManager {

	public static ArrayList<VanillaBlockUpgrade> vanillaUpgrades = new ArrayList<VanillaBlockUpgrade>();


	public static void modifyVanilla() {
		editVanillaBlockProperties();
		//removeVanillaRecipes();
		addVanillaBlockUpgrades();

	}

	public static void editVanillaBlockProperties() {
		/*
		 * Blocks.DIRT.setHardness(22.5F); Blocks.GRASS_PATH.setHardness(23F);
		 * Blocks.STONE.setHardness(35F); Blocks.QUARTZ_BLOCK.setHardness(35F);
		 * Blocks.PLANKS.setHardness(27.5F); Blocks.COBBLESTONE.setHardness(32.5F);
		 * Blocks.LOG.setHardness(30F); Blocks.LOG2.setHardness(30F);
		 * Blocks.PLANKS.setHardness(33F); Blocks.OAK_DOOR.setHardness(30F);
		 * Blocks.SPRUCE_DOOR.setHardness(30F); Blocks.BIRCH_DOOR.setHardness(30F);
		 * Blocks.JUNGLE_DOOR.setHardness(30F); Blocks.ACACIA_DOOR.setHardness(30F);
		 * Blocks.DARK_OAK_DOOR.setHardness(30F); Blocks.IRON_DOOR.setHardness(40F);
		 * Blocks.SAND.setHardness(18F); Blocks.GRAVEL.setHardness(18F);
		 * Blocks.GRASS.setHardness(23F); Blocks.BRICK_BLOCK.setHardness(35F);
		 * Blocks.STONEBRICK.setHardness(35F); Blocks.STONE_SLAB.setHardness(35F);
		 * Blocks.STONE_SLAB2.setHardness(35F); Blocks.COAL_ORE.setHardness(35F);
		 * Blocks.IRON_ORE.setHardness(35F); Blocks.GOLD_ORE.setHardness(35F);
		 * Blocks.LAPIS_ORE.setHardness(32F); Blocks.REDSTONE_ORE.setHardness(30F);
		 * Blocks.LIT_REDSTONE_ORE.setHardness(30F);
		 * Blocks.DIAMOND_ORE.setHardness(38F); Blocks.EMERALD_ORE.setHardness(38F);
		 * Blocks.QUARTZ_ORE.setHardness(35F); Blocks.IRON_BLOCK.setHardness(40F);
		 * Blocks.IRON_BARS.setHardness(35F); Blocks.CLAY.setHardness(18F);
		 * Blocks.CONCRETE.setHardness(39F);
		 * 
		 * Blocks.DIRT.setResistance(1F); Blocks.STONE.setResistance(10F);
		 * Blocks.QUARTZ_BLOCK.setResistance(10F); Blocks.PLANKS.setResistance(5F);
		 * Blocks.COBBLESTONE.setResistance(10F); Blocks.LOG.setResistance(5F);
		 * Blocks.LOG2.setResistance(5F); Blocks.PLANKS.setResistance(4.5F);
		 * Blocks.OAK_DOOR.setResistance(5F); Blocks.SPRUCE_DOOR.setResistance(5F);
		 * Blocks.BIRCH_DOOR.setResistance(5F); Blocks.JUNGLE_DOOR.setResistance(5F);
		 * Blocks.ACACIA_DOOR.setResistance(5F); Blocks.DARK_OAK_DOOR.setResistance(5F);
		 * Blocks.IRON_DOOR.setResistance(10F); Blocks.SAND.setResistance(1F);
		 * Blocks.GRAVEL.setResistance(1F); Blocks.GRASS.setResistance(1F);
		 * Blocks.GRASS_PATH.setResistance(1F); Blocks.BRICK_BLOCK.setResistance(10F);
		 * Blocks.STONEBRICK.setResistance(10F); Blocks.STONE_SLAB.setResistance(10F);
		 * Blocks.STONE_SLAB2.setResistance(10F); Blocks.COAL_ORE.setResistance(5F);
		 * Blocks.IRON_ORE.setResistance(5F); Blocks.IRON_ORE.setResistance(5F);
		 * Blocks.GOLD_ORE.setResistance(5F); Blocks.LAPIS_ORE.setResistance(5F);
		 * Blocks.REDSTONE_ORE.setResistance(5F);
		 * Blocks.LIT_REDSTONE_ORE.setResistance(5F);
		 * Blocks.DIAMOND_ORE.setResistance(5F); Blocks.EMERALD_ORE.setResistance(5F);
		 * Blocks.QUARTZ_ORE.setResistance(5F); Blocks.IRON_BLOCK.setResistance(10F);
		 * Blocks.IRON_BARS.setResistance(10F); Blocks.CLAY.setResistance(1F);
		 * Blocks.CONCRETE.setResistance(20F);
		 */


		/*
		 * Items.APPLE.setMaxDamage(2); Items.PORKCHOP.setMaxDamage(4);
		 * Items.COOKED_PORKCHOP.setMaxDamage(4); Items.BEEF.setMaxDamage(4);
		 * Items.COOKED_BEEF.setMaxDamage(4); Items.BREAD.setMaxDamage(3);
		 * Items.CHICKEN.setMaxDamage(3); Items.COOKED_CHICKEN.setMaxDamage(3);
		 * Items.RABBIT.setMaxDamage(3); Items.COOKED_RABBIT.setMaxDamage(3);
		 * Items.MUTTON.setMaxDamage(4); Items.COOKED_MUTTON.setMaxDamage(4);
		 */
	}

	public static void removeVanillaRecipes() {
		if (!CommonConfig.disableVanillaBlocksAndItems.get())
			return;
		RecipeManager.removeRecipe(Blocks.FURNACE);
		RecipeManager.removeRecipe(Blocks.OAK_PLANKS);
		RecipeManager.removeRecipe(Blocks.BIRCH_PLANKS);
		RecipeManager.removeRecipe(Blocks.SPRUCE_PLANKS);
		RecipeManager.removeRecipe(Blocks.JUNGLE_PLANKS);
		RecipeManager.removeRecipe(Blocks.DARK_OAK_PLANKS);
		RecipeManager.removeRecipe(Blocks.ACACIA_PLANKS);
		// RecipeManager.removeRecipe(Blocks.IRON_BARS);
		// RecipeManager.removeRecipe(Blocks.IRON_TRAPDOOR);
		// RecipeManager.removeRecipe(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
		RecipeManager.removeItem(Items.WOODEN_SWORD);
		RecipeManager.removeItem(Items.WOODEN_AXE);
		RecipeManager.removeItem(Items.WOODEN_PICKAXE);
		RecipeManager.removeItem(Items.WOODEN_SHOVEL);
		RecipeManager.removeItem(Items.WOODEN_HOE);
		RecipeManager.removeItem(Items.STONE_SWORD);
		RecipeManager.removeItem(Items.STONE_AXE);
		RecipeManager.removeItem(Items.STONE_PICKAXE);
		RecipeManager.removeItem(Items.STONE_SHOVEL);
		RecipeManager.removeItem(Items.STONE_HOE);

		RecipeManager.removeItem(Items.IRON_SWORD);
		RecipeManager.removeItem(Items.IRON_AXE);
		RecipeManager.removeItem(Items.IRON_PICKAXE);
		RecipeManager.removeItem(Items.IRON_SHOVEL);
		RecipeManager.removeItem(Items.IRON_HOE);

		// RecipeManager.removeRecipe(net.minecraft.init.Blocks.IRON_BLOCK);
		// RecipeManager.removeRecipe(net.minecraft.init.Blocks.GOLD_BLOCK);
		RecipeManager.removeRecipe(Items.IRON_CHESTPLATE);
		RecipeManager.removeRecipe(Items.IRON_HELMET);
		RecipeManager.removeRecipe(Items.IRON_BOOTS);
		RecipeManager.removeRecipe(Items.IRON_LEGGINGS);
		RecipeManager.removeRecipe(Items.LEATHER_CHESTPLATE);
		RecipeManager.removeRecipe(Items.LEATHER_HELMET);
		RecipeManager.removeRecipe(Items.LEATHER_BOOTS);
		RecipeManager.removeRecipe(Items.LEATHER_LEGGINGS);

		RecipeManager.removeRecipe(Items.GOLDEN_SWORD);
		RecipeManager.removeRecipe(Items.GOLDEN_AXE);
		RecipeManager.removeRecipe(Items.GOLDEN_PICKAXE);
		RecipeManager.removeRecipe(Items.GOLDEN_SHOVEL);
		RecipeManager.removeRecipe(Items.GOLDEN_HOE);
		RecipeManager.removeRecipe(Items.DIAMOND_SWORD);
		RecipeManager.removeRecipe(Items.DIAMOND_AXE);
		RecipeManager.removeRecipe(Items.DIAMOND_PICKAXE);
		RecipeManager.removeRecipe(Items.DIAMOND_SHOVEL);
		RecipeManager.removeRecipe(Items.DIAMOND_HOE);
		RecipeManager.removeRecipe(Blocks.TORCH);

		RecipeManager.removeRecipe(Items.GOLDEN_CHESTPLATE);
		RecipeManager.removeRecipe(Items.GOLDEN_HELMET);
		RecipeManager.removeRecipe(Items.GOLDEN_BOOTS);
		RecipeManager.removeRecipe(Items.GOLDEN_LEGGINGS);
		RecipeManager.removeRecipe(Items.DIAMOND_CHESTPLATE);
		RecipeManager.removeRecipe(Items.DIAMOND_HELMET);
		RecipeManager.removeRecipe(Items.DIAMOND_BOOTS);
		RecipeManager.removeRecipe(Items.DIAMOND_LEGGINGS);

		if (CommonConfig.disableBlockToIngot.get()) {
			RecipeManager.removeRecipe(Items.IRON_INGOT);
			RecipeManager.removeRecipe(Items.IRON_NUGGET);
			RecipeManager.removeRecipe(Items.GOLD_INGOT);
			RecipeManager.removeRecipe(Items.GOLD_INGOT);
		}

		RecipeManager.removeSmelting(new ItemStack(Items.GOLD_INGOT), "minecraft");
		RecipeManager.removeSmelting(new ItemStack(Items.IRON_INGOT), "minecraft");

	}

	public static void addVanillaBlockUpgrades() {
		/*addVanillaBlockUpgrade(
				Blocks.PLANKS.defaultBlockState().setValue(BlockPlanks.VARIANT, EnumWoodType.OAK),
				new ItemStack[] { new ItemStack(ModItems.PLANK_WOOD, 6) }, ModSounds.UPGRADE_WOOD,
				nuparu.sevendaystomine.init.ModBlocks.OAK_PLANKS_REINFORCED.defaultBlockState(),
				nuparu.sevendaystomine.init.ModBlocks.OAK_FRAME.defaultBlockState());
		addVanillaBlockUpgrade(
				Blocks.PLANKS.defaultBlockState().setValue(BlockPlanks.VARIANT, EnumWoodType.BIRCH),
				new ItemStack[] { new ItemStack(ModItems.PLANK_WOOD, 6) }, ModSounds.UPGRADE_WOOD,
				nuparu.sevendaystomine.init.ModBlocks.BIRCH_PLANKS_REINFORCED.defaultBlockState(),
				nuparu.sevendaystomine.init.ModBlocks.BIRCH_FRAME.defaultBlockState());
		addVanillaBlockUpgrade(
				Blocks.PLANKS.defaultBlockState().setValue(BlockPlanks.VARIANT, EnumWoodType.SPRUCE),
				new ItemStack[] { new ItemStack(ModItems.PLANK_WOOD.get(), 6) }, ModSounds.UPGRADE_WOOD,
				nuparu.sevendaystomine.init.ModBlocks.SPRUCE_PLANKS_REINFORCED.defaultBlockState(),
				nuparu.sevendaystomine.init.ModBlocks.SPRUCE_FRAME.defaultBlockState());
		addVanillaBlockUpgrade(
				Blocks.PLANKS.defaultBlockState().setValue(BlockPlanks.VARIANT, EnumWoodType.JUNGLE),
				new ItemStack[] { new ItemStack(ModItems.PLANK_WOOD, 6) }, ModSounds.UPGRADE_WOOD,
				nuparu.sevendaystomine.init.ModBlocks.JUNGLE_PLANKS_REINFORCED.defaultBlockState(),
				nuparu.sevendaystomine.init.ModBlocks.JUNGLE_FRAME.defaultBlockState());
		addVanillaBlockUpgrade(
				Blocks.PLANKS.defaultBlockState().setValue(BlockPlanks.VARIANT, EnumWoodType.ACACIA),
				new ItemStack[] { new ItemStack(ModItems.PLANK_WOOD, 6) }, ModSounds.UPGRADE_WOOD,
				nuparu.sevendaystomine.init.ModBlocks.ACACIA_PLANKS_REINFORCED.defaultBlockState(),
				nuparu.sevendaystomine.init.ModBlocks.ACACIA_FRAME.defaultBlockState());
		addVanillaBlockUpgrade(
				Blocks.PLANKS.defaultBlockState().setValue(BlockPlanks.VARIANT, EnumWoodType.DARK_OAK),
				new ItemStack[] { new ItemStack(ModItems.PLANK_WOOD, 6) }, ModSounds.UPGRADE_WOOD,
				nuparu.sevendaystomine.init.ModBlocks.DARKOAK_PLANKS_REINFORCED.defaultBlockState(),
				nuparu.sevendaystomine.init.ModBlocks.DARKOAK_FRAME.defaultBlockState());*/
	}

	public static void addVanillaBlockUpgrade(Block parent, ItemStack[] items, SoundEvent sound, Block result) {
		vanillaUpgrades.add(new VanillaBlockUpgrade(parent.defaultBlockState(), items, sound, result.defaultBlockState(),
				(BlockState) null));
	}

	public static void addVanillaBlockUpgrade(BlockState parent, ItemStack[] items, SoundEvent sound,
			BlockState result) {
		vanillaUpgrades.add(new VanillaBlockUpgrade(parent, items, sound, result, (BlockState) null));
	}

	public static void addVanillaBlockUpgrade(Block parent, ItemStack[] items, SoundEvent sound, Block result,
			Block prev) {
		vanillaUpgrades.add(new VanillaBlockUpgrade(parent.defaultBlockState(), items, sound, result.defaultBlockState(),
				prev.defaultBlockState()));
	}

	public static void addVanillaBlockUpgrade(BlockState parent, ItemStack[] items, SoundEvent sound,
			BlockState result, BlockState prev) {
		vanillaUpgrades.add(new VanillaBlockUpgrade(parent, items, sound, result, prev));
	}

	public static VanillaBlockUpgrade getVanillaUpgrade(Block parent) {
		return getVanillaUpgrade(parent.defaultBlockState());
	}

	public static VanillaBlockUpgrade getVanillaUpgrade(BlockState parent) {
		for (VanillaBlockUpgrade upgrade : vanillaUpgrades) {
			if (upgrade.parent == parent)
				return upgrade;
		}
		return null;
	}


	public static class VanillaBlockUpgrade {

		private BlockState parent;
		private ItemStack[] items;
		private SoundEvent sound;
		private BlockState result;
		private BlockState prev;

		public VanillaBlockUpgrade(BlockState parent, ItemStack[] items, SoundEvent sound, BlockState result,
				BlockState prev) {
			this.parent = parent;
			this.items = items;
			this.sound = sound;
			this.result = result;
			this.prev = prev;
		}

		public BlockState getParent() {
			return parent;
		}

		public ItemStack[] getItems() {
			return items;
		}

		public SoundEvent getSound() {
			return sound;
		}

		public BlockState getResult() {
			return result;
		}

		public BlockState getPrev() {
			return prev;
		}
	}


}
