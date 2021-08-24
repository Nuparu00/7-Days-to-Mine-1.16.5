package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class BlockBrickMossySlab extends BlockSlabBase {

	public BlockBrickMossySlab() {
		super(AbstractBlock.Properties.of(Material.STONE).sound(SoundType.STONE).strength(2f,4f).harvestTool(ToolType.PICKAXE).harvestLevel(0));
	}
}
