package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.item.IScrapable;

public class BlockCatwalkStairs extends BlockStairsBase implements IScrapable {

	private EnumMaterial material = EnumMaterial.IRON;
	private int weight = 3;

	public BlockCatwalkStairs() {
		super(() -> ModBlocks.CATWALK.get().defaultBlockState(), AbstractBlock.Properties.of(Material.METAL).sound(SoundType.METAL)
				.strength(2f, 1f).harvestTool(ToolType.PICKAXE).harvestLevel(0));
	}

	@Override
	public void setMaterial(EnumMaterial mat) {
		material = mat;
	}

	@Override
	public EnumMaterial getItemMaterial() {
		return material;
	}

	@Override
	public void setWeight(int newWeight) {
		weight = newWeight;
	}

	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public boolean canBeScraped() {
		return true;
	}

}
