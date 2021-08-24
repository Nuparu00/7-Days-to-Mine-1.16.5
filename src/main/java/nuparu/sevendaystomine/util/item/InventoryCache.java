package nuparu.sevendaystomine.util.item;

import net.minecraft.item.ItemStack;

public class InventoryCache {
	public ItemStack[] inventory;
	public ItemStack selected;
	public ItemStack backpack;
	public int index;

	public InventoryCache(ItemStack[] inventory, ItemStack selected, ItemStack backpack, int index) {
		this.inventory = inventory;
		this.selected = selected;
		this.index = index;
		this.backpack = backpack;
	}
}
