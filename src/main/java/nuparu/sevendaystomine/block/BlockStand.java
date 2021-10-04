package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import nuparu.sevendaystomine.item.EnumMaterial;

public class BlockStand extends BlockBase {


	public BlockStand() {
		super(AbstractBlock.Properties.of(Material.METAL).sound(SoundType.METAL).strength(1.5f, 1).noOcclusion());
	}


}
