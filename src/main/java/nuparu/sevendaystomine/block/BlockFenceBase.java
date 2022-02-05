package nuparu.sevendaystomine.block;

import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;

public class BlockFenceBase extends FenceBlock implements IBlockBase {
	
	public BlockFenceBase(AbstractBlock.Properties properties) {
		super(properties);
	}

	@Override
	public BlockItem createBlockItem() {
		 final Item.Properties properties = new Item.Properties().tab(getItemGroup());
		 return new BlockItem(this, properties);
	}

	@Override
	public boolean connectsTo(BlockState p_220111_1_, boolean p_220111_2_, Direction p_220111_3_) {
		return super.connectsTo(p_220111_1_, p_220111_2_, p_220111_3_) || p_220111_1_.getBlock() == this || p_220111_1_.getBlock() == this;
	}
}
