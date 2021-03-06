package nuparu.sevendaystomine.block;

import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;

public class BlockOre extends OreBlock implements IBlockBase {
	
	public BlockOre(Properties properties) {
		super(properties);
	}
	
	@Override
	   protected int xpOnDrop(Random p_220281_1_) {
	      if (this == Blocks.COAL_ORE) {
	         return MathHelper.nextInt(p_220281_1_, 0, 2);
	      } else if (this == Blocks.DIAMOND_ORE) {
	         return MathHelper.nextInt(p_220281_1_, 3, 7);
	      } else if (this == Blocks.EMERALD_ORE) {
	         return MathHelper.nextInt(p_220281_1_, 3, 7);
	      } else if (this == Blocks.LAPIS_ORE) {
	         return MathHelper.nextInt(p_220281_1_, 2, 5);
	      } else if (this == Blocks.NETHER_QUARTZ_ORE) {
	         return MathHelper.nextInt(p_220281_1_, 2, 5);
	      } else {
	         return this == Blocks.NETHER_GOLD_ORE ? MathHelper.nextInt(p_220281_1_, 0, 1) : 0;
	      }
	   }
	
	@Override
	public BlockItem createBlockItem() {
		 final Item.Properties properties = new Item.Properties().tab(getItemGroup());
		 return new BlockItem(this, properties);
	}
}
