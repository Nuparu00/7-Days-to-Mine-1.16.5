package nuparu.sevendaystomine.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.item.ItemGun;

public class EnchantmentJamming extends Enchantment {

	protected EnchantmentJamming() {
		super(Rarity.UNCOMMON, ModEnchantments.GUNS,
				new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND });
		setRegistryName(SevenDaysToMine.MODID, "jamming");
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		return stack.getItem() instanceof ItemGun && ((ItemGun)stack.getItem()).getMaxAmmo() > 1;
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
        return super.checkCompatibility(ench);
    }
	
	@Override
	public boolean isCurse() {
		return true;
	}
	
}
