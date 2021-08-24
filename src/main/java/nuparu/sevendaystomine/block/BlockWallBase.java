package nuparu.sevendaystomine.block;

import net.minecraft.block.WallBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class BlockWallBase extends WallBlock implements IBlockBase {

	public BlockWallBase(Properties p_i48301_1_) {
		super(p_i48301_1_);
	}

	@Override
	public BlockItem createBlockitem() {
		 final Item.Properties properties = new Item.Properties().tab(getItemGroup());
		 return new BlockItem(this, properties);
	}
}
