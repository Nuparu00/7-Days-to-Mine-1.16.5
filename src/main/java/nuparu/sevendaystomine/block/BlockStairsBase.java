package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class BlockStairsBase extends StairsBlock implements IBlockBase {

	public BlockStairsBase(java.util.function.Supplier<BlockState> state, AbstractBlock.Properties properties) {
		super(state, properties.noOcclusion());
	}
	
	@Override
	public BlockItem createBlockitem() {
		 final Item.Properties properties = new Item.Properties().tab(getItemGroup());
		 return new BlockItem(this, properties);
	}
}
