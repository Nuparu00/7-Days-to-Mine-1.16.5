package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class BlockFenceBase extends FenceBlock implements IBlockBase {
	
	public BlockFenceBase(AbstractBlock.Properties properties) {
		super(properties);
	}

	@Override
	public BlockItem createBlockItem() {
		 final Item.Properties properties = new Item.Properties().tab(getItemGroup());
		 return new BlockItem(this, properties);
	}
}
