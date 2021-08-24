package nuparu.sevendaystomine.item;

import net.minecraft.item.Item;
import nuparu.sevendaystomine.init.ModItemGroups;

public class ItemGunPart extends ItemQualityScrapable {

	public ItemGunPart(EnumMaterial mat) {
		super(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_FORGING), mat);
	}

	public ItemGunPart(EnumMaterial mat, int weight) {
		super(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_FORGING),mat, weight);
	}
}
