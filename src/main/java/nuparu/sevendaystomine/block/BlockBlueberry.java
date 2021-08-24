package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.IItemProvider;
import nuparu.sevendaystomine.init.ModItems;

public class BlockBlueberry extends BlockFruitBush {

	public BlockBlueberry() {
		super(AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().sound(SoundType.CROP));
	}

	@Override
	protected IItemProvider getBaseSeedId() {
		return ModItems.BLUEBERRY.get();
	}

}
