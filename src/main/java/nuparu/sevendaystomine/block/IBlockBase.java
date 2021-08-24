package nuparu.sevendaystomine.block;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import nuparu.sevendaystomine.init.ModItemGroups;

public interface IBlockBase {
	
	public BlockItem createBlockitem();
	
	public default ItemGroup getItemGroup() {
		return ModItemGroups.TAB_BUILDING;
	}
}