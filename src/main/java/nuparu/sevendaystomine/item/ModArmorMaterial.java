package nuparu.sevendaystomine.item;

import java.util.function.Supplier;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum ModArmorMaterial implements IArmorMaterial {
	LEATHER("sevendaystomine:leather", 5, new int[] { 1, 2, 3, 1 }, 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> {
		return Ingredient.of(Items.LEATHER);
	}), CLOTHING("sevendaystomine:clothing", 6, new int[] { 1, 2, 2, 1 }, 0, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> {
		return Ingredient.of(Items.IRON_INGOT);
	}), IRON("sevendaystomine:iron", 15, new int[] { 2, 5, 6, 2 }, 9, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> {
		return Ingredient.of(Items.IRON_INGOT);
	}), FIBER("sevendaystomine:fiber", 2, new int[] { 1, 1, 1, 1 }, 0, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> {
		return Ingredient.of(Items.IRON_INGOT);
	}), STEEL("sevendaystomine:steel", 25, new int[] { 3, 4, 3, 3 }, 0, SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, 0.0F, () -> {
		return Ingredient.of(Items.GOLD_INGOT);
	}), LEATHER_IRON("sevendaystomine:leather_iron", 12, new int[] { 2, 3, 2, 1 }, 0, SoundEvents.ARMOR_EQUIP_LEATHER, 2.0F, 0.0F, () -> {
		return Ingredient.of(Items.DIAMOND);
	}), SCRAP("sevendaystomine:scrap", 10, new int[] { 2, 2, 2, 1 }, 0, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> {
		return Ingredient.of(Items.SCUTE);
	}), MILITARY("sevendaystomine:military", 30, new int[] { 5, 6, 4, 4 }, 2, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, () -> {
		return Ingredient.of(Items.NETHERITE_INGOT);
	});
	
	private static final int[] HEALTH_PER_SLOT = new int[] { 13, 15, 16, 11 };
	private final String name;
	private final int durabilityMultiplier;
	private final int[] slotProtections;
	private final int enchantmentValue;
	private final SoundEvent sound;
	private final float toughness;
	private final float knockbackResistance;
	private final LazyValue<Ingredient> repairIngredient;

	private ModArmorMaterial(String name, int durabilityMultiplier, int[] slotProtections, int enchantmentValue,
			SoundEvent sound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
		this.name = name;
		this.durabilityMultiplier = durabilityMultiplier;
		this.slotProtections = slotProtections;
		this.enchantmentValue = enchantmentValue;
		this.sound = sound;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.repairIngredient = new LazyValue<>(repairIngredient);
	}

	public int getDurabilityForSlot(EquipmentSlotType p_200896_1_) {
		return HEALTH_PER_SLOT[p_200896_1_.getIndex()] * this.durabilityMultiplier;
	}

	public int getDefenseForSlot(EquipmentSlotType p_200902_1_) {
		return this.slotProtections[p_200902_1_.getIndex()];
	}

	public int getEnchantmentValue() {
		return this.enchantmentValue;
	}

	public SoundEvent getEquipSound() {
		return this.sound;
	}

	public Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}

	@OnlyIn(Dist.CLIENT)
	public String getName() {
		return this.name;
	}

	public float getToughness() {
		return this.toughness;
	}

	public float getKnockbackResistance() {
		return this.knockbackResistance;
	}
}