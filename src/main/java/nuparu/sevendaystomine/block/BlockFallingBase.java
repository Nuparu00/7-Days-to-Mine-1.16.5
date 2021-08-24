package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FallingBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class BlockFallingBase extends FallingBlock implements IBlockBase {
	public BlockFallingBase(AbstractBlock.Properties properties) {
		super(properties);
	}

	@Override
	public BlockItem createBlockitem() {
		 final Item.Properties properties = new Item.Properties().tab(getItemGroup());
		 return new BlockItem(this, properties);
	}
	
}
