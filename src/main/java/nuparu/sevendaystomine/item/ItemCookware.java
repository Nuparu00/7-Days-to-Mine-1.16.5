package nuparu.sevendaystomine.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class ItemCookware extends BlockItem{

	public ItemCookware(Block block) {
		super(block, new Item.Properties().stacksTo(1));
	}


}
