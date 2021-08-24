package nuparu.sevendaystomine.item;

import net.minecraft.item.Item;
import nuparu.sevendaystomine.init.ModItemGroups;

public class ItemMold extends Item{

	public ItemMold() {
		super(new Item.Properties().stacksTo(1).durability(64).tab(ModItemGroups.TAB_FORGING));
	}
}
