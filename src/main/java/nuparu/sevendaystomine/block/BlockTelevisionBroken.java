package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockTelevisionBroken extends BlockHorizontalBase {

	public BlockTelevisionBroken() {
		super(AbstractBlock.Properties.of(Material.METAL).sound(SoundType.METAL).strength(1.5f, 2).noCollission());
	}
}
