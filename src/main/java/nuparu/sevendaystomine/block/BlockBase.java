package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import nuparu.sevendaystomine.SevenDaysToMine;

public class BlockBase extends Block implements IBlockBase {
	public BlockBase(AbstractBlock.Properties properties)
    {
        super(properties);
    }

	@Override
	public BlockItem createBlockItem() {
		 final Item.Properties properties = new Item.Properties().tab(getItemGroup());
		 return new BlockItem(this, properties);
	}
	
}
