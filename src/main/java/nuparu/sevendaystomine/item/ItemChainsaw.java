package nuparu.sevendaystomine.item;

import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.ToolType;
import nuparu.sevendaystomine.config.EnumQualityState;

public class ItemChainsaw extends ItemFuelTool {
	private static final Set<Material> DIGGABLE_MATERIALS = Sets.newHashSet(Material.WOOD, Material.NETHER_WOOD,
			Material.PLANT, Material.REPLACEABLE_PLANT, Material.BAMBOO, Material.VEGETABLE);
	private static final Set<Block> OTHER_DIGGABLE_BLOCKS = Sets.newHashSet(Blocks.LADDER, Blocks.SCAFFOLDING,
			Blocks.OAK_BUTTON, Blocks.SPRUCE_BUTTON, Blocks.BIRCH_BUTTON, Blocks.JUNGLE_BUTTON, Blocks.DARK_OAK_BUTTON,
			Blocks.ACACIA_BUTTON, Blocks.CRIMSON_BUTTON, Blocks.WARPED_BUTTON);

	public ItemChainsaw(float attackDamageIn, float attackSpeedIn, IItemTier materialIn) {
		super(attackDamageIn, attackSpeedIn, materialIn, OTHER_DIGGABLE_BLOCKS, new Item.Properties().stacksTo(1)
				.addToolType(ToolType.AXE, materialIn.getLevel()).tab(ItemGroup.TAB_TOOLS).setNoRepair());
	}

	public float getDestroySpeed(ItemStack p_150893_1_, BlockState p_150893_2_) {
		Material material = p_150893_2_.getMaterial();
		return (float) (DIGGABLE_MATERIALS.contains(material) ? this.speed
				: super.getDestroySpeed(p_150893_1_, p_150893_2_));
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot,
			ItemStack stack) {
		Multimap<Attribute, AttributeModifier> multimap = HashMultimap.<Attribute, AttributeModifier>create();
		double damage = this.getAttackDamage();
		double speed = this.speed;
		if (EnumQualityState.isQualitySystemOn()) {
			damage = getAttackDamageModified(stack);
			speed = getAttackSpeedModified(stack);
		}
		CompoundNBT nbt = stack.getOrCreateTag();
		if (nbt == null || !nbt.contains("FuelCurrent") || nbt.getFloat("FuelCurrent") <= 0) {
			damage = 1f;
			speed = -2.4000000953674316D;
		}
		if (equipmentSlot == EquipmentSlotType.MAINHAND) {
			multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier",
					damage, AttributeModifier.Operation.ADDITION));
			multimap.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier",
					speed, AttributeModifier.Operation.ADDITION));
		}

		return multimap;
	}

}