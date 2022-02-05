package nuparu.sevendaystomine.item;

import java.util.function.Supplier;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModItems;

public enum ModItemTier implements IItemTier {
	SCREWDRIVER(0, 150, 0.5f, 2.75f, 1, () -> {
		return Ingredient.of(Items.IRON_INGOT);
	}),
	SMALL_TOOLS(0, 250, 0.5f, 2.5f, 2, () -> {
		return Ingredient.of(Items.IRON_INGOT);
	}),STONE_TOOLS(1, 131, 3.5f, 2f, 2, () -> {
		return Ingredient.of(ModItems.SMALL_STONE.get());
	}), BONE_TOOLS(1, 120, 2f, 2.5f, 2, () -> {
		return Ingredient.of(Items.BONE);
	}), CRUDE_TOOLS(1, 60, 2f, 2.5f, 2, () -> {
		return Ingredient.of(ModItems.PLANK_WOOD.get());
	}), WOODEN_TOOLS(1, 60, 2f, 3f, 2, () -> {
		return Ingredient.of(ModItems.PLANK_WOOD.get());
	}), WOODEN_REINFORCED_TOOLS(100, 200, 4.5f, 2.5f, 2, () -> {
		return Ingredient.of(ModItems.PLANK_WOOD.get());
	}), BARBED_TOOLS(1, 180, 2f, 5f, 2, () -> {
		return Ingredient.of(ModBlocks.RAZOR_WIRE.get());
	}), SPIKED_TOOLS(1, 240, 2f, 7f, 2, () -> {
		return Ingredient.of(ModItems.IRON_PIPE.get());
	}), COPPER_TOOLS(0, 150, 5.2f, 3.75f, 2, () -> {
		return Ingredient.of(ModItems.INGOT_COPPER.get());
	}), SCRAP_TOOLS(0, 100, 4f, 4.6666f, 2, () -> {
		return Ingredient.of(ModItems.IRON_SCRAP.get());
	}), BRONZE_TOOLS(1, 180, 6f, 5.3333f, 2, () -> {
		return Ingredient.of(ModItems.INGOT_BRONZE.get());
	}), IRON_TOOLS(2, 250, 8.2f, 6f, 2, () -> {
		return Ingredient.of(Items.IRON_INGOT);
	}), STEEL_TOOLS(3, 500, 11f, 7f, 2, () -> {
		return Ingredient.of(ModItems.INGOT_STEEL.get());
	}), ARMY_TOOLS(1, 400, 2f, 7f, 2, () -> {
		return Ingredient.of(ModItems.INGOT_STEEL.get());
	}), MACHETE( 1, 250, 2f, 7.3f, 2, () -> {
		return Ingredient.of(ModItems.INGOT_STEEL.get());
	}), SLEDGEHAMMER(1, 200, 11f, 7f, 2, () -> {
		return Ingredient.of(ModItems.INGOT_STEEL.get());
	}), AUGER(4, 1000, 18F, 6f, 0, () -> {
		return Ingredient.of(ModItems.GEAR.get());
	}), NETHERITE_AUGER(5, 800, 32F, 8f, 0, () -> {
		return Ingredient.of(ModItems.GEAR.get());
	}), DIAMOND_KNIFE(1, 380, 2f, 10f, 2, () -> {
		return Ingredient.of(Items.DIAMOND);
	});

	private final int harvestLevel;
	private final int maxUses;
	private final float efficiency;
	private final float attackDamage;
	private final int enchantability;
	private final LazyValue<Ingredient> repairMaterial;

	ModItemTier(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn,
				int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
		this.harvestLevel = harvestLevelIn;
		this.maxUses = maxUsesIn;
		this.efficiency = efficiencyIn;
		this.attackDamage = attackDamageIn;
		this.enchantability = enchantabilityIn;
		this.repairMaterial = new LazyValue<>(repairMaterialIn);
	}

	public int getUses() {
		return this.maxUses;
	}

	public float getSpeed() {
		return this.efficiency;
	}

	public float getAttackDamageBonus() {
		return this.attackDamage;
	}

	public int getLevel() {
		return this.harvestLevel;
	}

	public int getEnchantmentValue() {
		return this.enchantability;
	}

	public Ingredient getRepairIngredient() {
		return this.repairMaterial.get();
	}
}
