package nuparu.sevendaystomine.item;

import net.minecraft.item.Item;
import nuparu.sevendaystomine.init.ModItemGroups;

public class ItemGunPart extends ItemQuality {

	public ItemGunPart(Properties properties) {
		super(properties);
	}

	public ItemGunPart(){
		this(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_FORGING));
	}
}
