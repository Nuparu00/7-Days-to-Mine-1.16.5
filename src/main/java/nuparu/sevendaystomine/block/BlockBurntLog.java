package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class BlockBurntLog extends BlockPillarBase {

	public BlockBurntLog() {
		super(AbstractBlock.Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(2,3f).harvestTool(ToolType.AXE).harvestLevel(0));
	}

}
