package nuparu.sevendaystomine.block;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import nuparu.sevendaystomine.init.ModItemGroups;

import javax.annotation.Nullable;

public interface IBlockBase {

	@Nullable
	BlockItem createBlockItem();
	
	default ItemGroup getItemGroup() {
		return ModItemGroups.TAB_BUILDING;
	}
}