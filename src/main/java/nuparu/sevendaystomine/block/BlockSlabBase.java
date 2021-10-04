package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public abstract class BlockSlabBase extends SlabBlock implements IBlockBase {
	public BlockSlabBase(AbstractBlock.Properties properties) {
		super(properties);
		
	}
	
	@Override
	public BlockItem createBlockItem() {
		 final Item.Properties properties = new Item.Properties().tab(getItemGroup());
		 return new BlockItem(this, properties);
	}
}
