package nuparu.sevendaystomine.item;

import java.util.function.Supplier;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

public enum ModItemTier implements IItemTier {

	SMALL_TOOLS(0, 300, 0.5f, 20, 2, () -> {
		return Ingredient.of(Items.ACACIA_BOAT);
	}),STONE_TOOLS(1, 100, 3.5f, 5, 2, () -> {
		return Ingredient.of(Items.ACACIA_BOAT);
	}), BONE_TOOLS(1, 100, 2f, 6, 2, () -> {
		return Ingredient.of(Items.ACACIA_BOAT);
	}), CRUDE_TOOLS(1, 60, 2f, 8, 2, () -> {
		return Ingredient.of(Items.ACACIA_BOAT);
	}), WOODEN_TOOLS(1, 180, 2f, 14, 2, () -> {
		return Ingredient.of(Items.ACACIA_BOAT);
	}), WOODEN_REINFORCED_TOOLS(1, 200, 2f, 18, 2, () -> {
		return Ingredient.of(Items.ACACIA_BOAT);
	}), BARBED_TOOLS(1, 220, 2f, 22, 2, () -> {
		return Ingredient.of(Items.ACACIA_BOAT);
	}), SPIKED_TOOLS(1, 250, 2f, 26, 2, () -> {
		return Ingredient.of(Items.ACACIA_BOAT);
	}), COPPER_TOOLS(0, 100, 5.2f, 17, 2, () -> {
		return Ingredient.of(Items.ACACIA_BOAT);
	}), SCRAP_TOOLS(0, 125, 4f, 18, 2, () -> {
		return Ingredient.of(Items.ACACIA_BOAT);
	}), BRONZE_TOOLS(1, 250, 6f, 20, 2, () -> {
		return Ingredient.of(Items.ACACIA_BOAT);
	}), IRON_TOOLS(2, 300, 8.2f, 24, 2, () -> {
		return Ingredient.of(Items.ACACIA_BOAT);
	}), STEEL_TOOLS(3, 40, 11f, 28, 2, () -> {
		return Ingredient.of(Items.ACACIA_BOAT);
	}), ARMY_TOOLS(1, 350, 2f, 26, 2, () -> {
		return Ingredient.of(Items.ACACIA_BOAT);
	}), MACHETE( 1, 200, 2f, 28, 2, () -> {
		return Ingredient.of(Items.ACACIA_BOAT);
	}), SLEDGEHAMMER(1, 40, 11f, 30, 2, () -> {
		return Ingredient.of(Items.ACACIA_BOAT);
	}), AUGER(4, 1200, 26F, 24, 0, () -> {
		return Ingredient.of(Items.ACACIA_BOAT);
	});

	private final int harvestLevel;
	private final int maxUses;
	private final float efficiency;
	private final float attackDamage;
	private final int enchantability;
	private final LazyValue<Ingredient> repairMaterial;

	private ModItemTier(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn,
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
