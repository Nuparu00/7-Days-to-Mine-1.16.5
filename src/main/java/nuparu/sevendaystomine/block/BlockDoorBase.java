package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public abstract class BlockDoorBase extends DoorBlock implements IBlockBase {

	public BlockDoorBase(AbstractBlock.Properties properties) {
		super(properties);
	}
	
	@Override
	public BlockItem createBlockItem() {
		 return null;
	}
	
}
