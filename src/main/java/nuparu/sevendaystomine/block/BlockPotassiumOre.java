package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class BlockPotassiumOre extends BlockOre {

	public BlockPotassiumOre() {
		super(AbstractBlock.Properties.of(Material.STONE).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(1).strength(1.8f,5));
	}
}
