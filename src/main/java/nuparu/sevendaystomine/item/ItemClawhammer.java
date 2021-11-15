package nuparu.sevendaystomine.item;

import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;

public class ItemClawhammer extends ItemUpgrader {

	public ItemClawhammer(IItemTier p_i48530_1_, float p_i48530_2_, float p_i48530_3_, Item.Properties p_i48530_4_) {
		super(p_i48530_2_, p_i48530_3_, p_i48530_1_, new HashSet<Block>(), p_i48530_4_);
		effect = 0.5f;
		length = EnumLength.SHORT;
	}
}
