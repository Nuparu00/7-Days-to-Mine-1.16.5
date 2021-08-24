package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.item.IScrapable;

public class BlockMetal extends BlockBase implements IScrapable {

	private EnumMaterial enumMat = null;
	private int weight = 12;

	public BlockMetal(AbstractBlock.Properties properties, EnumMaterial mat, int weight) {
		super(properties);
		this.enumMat = mat;
		this.weight = weight;
	}

	public BlockMetal(AbstractBlock.Properties properties, EnumMaterial mat) {
		this(properties, mat, 12);
	}

	public BlockMetal(EnumMaterial mat, int weight) {
		this(AbstractBlock.Properties.of(Material.METAL).sound(SoundType.METAL), mat, weight);
	}

	public BlockMetal(EnumMaterial mat) {
		this(AbstractBlock.Properties.of(Material.METAL).sound(SoundType.METAL), mat);
	}

	@Override
	public void setMaterial(EnumMaterial mat) {
		enumMat = mat;
	}

	@Override
	public EnumMaterial getItemMaterial() {
		return enumMat;
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
