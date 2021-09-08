package nuparu.sevendaystomine.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModEnchantments;
import nuparu.sevendaystomine.item.ItemGun;

public class EnchantmentFastReload extends Enchantment {

	public EnchantmentFastReload() {
		super(Rarity.COMMON, ModEnchantments.GUNS,
				new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND });
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		return stack.getItem() instanceof ItemGun;
	}
	
	@Override
	public int getMinCost(int enchantmentLevel)
    {
        return 1 + 10 * (enchantmentLevel - 1);
    }
	
	@Override
	public int getMaxCost(int enchantmentLevel)
    {
        return super.getMinCost(enchantmentLevel) + 50;
    }
	
	@Override
	protected boolean checkCompatibility(Enchantment ench)
    {
        return super.checkCompatibility(ench) && ench != ModEnchantments.BIG_MAG.get();
    }
}
