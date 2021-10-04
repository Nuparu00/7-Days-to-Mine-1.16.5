package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class BlockPillarBase extends RotatedPillarBlock implements IBlockBase {

	public BlockPillarBase(AbstractBlock.Properties properties) {
		super(properties);
	}

	@Override
	public BlockItem createBlockItem() {
		 final Item.Properties properties = new Item.Properties().tab(getItemGroup());
		 return new BlockItem(this, properties);
	}
}
