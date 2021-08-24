package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class BlockBurntPlanksSlab extends BlockSlabBase {

	public BlockBurntPlanksSlab() {
		super(AbstractBlock.Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(1.5f, 2f)
				.harvestTool(ToolType.AXE).harvestLevel(0));
	}
}
