package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class BlockDarkBricksMossySlab extends BlockSlabBase {

	public BlockDarkBricksMossySlab() {
		super(AbstractBlock.Properties.of(Material.STONE).sound(SoundType.STONE).strength(2f,4f).harvestTool(ToolType.PICKAXE).harvestLevel(0));
	}
}
