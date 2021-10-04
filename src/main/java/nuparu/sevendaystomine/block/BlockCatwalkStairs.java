package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.item.EnumMaterial;

public class BlockCatwalkStairs extends BlockStairsBase {

	public BlockCatwalkStairs() {
		super(() -> ModBlocks.CATWALK.get().defaultBlockState(), AbstractBlock.Properties.of(Material.METAL).sound(SoundType.METAL)
				.strength(2f, 1f).harvestTool(ToolType.PICKAXE).harvestLevel(0));
	}

}
