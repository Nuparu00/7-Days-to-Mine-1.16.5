package nuparu.sevendaystomine.item;

import net.minecraft.item.Item;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.util.ItemUtils;

public class ItemScrapable extends Item implements IScrapable {
	private EnumMaterial material;
	private int weight;

	public ItemScrapable(Item.Properties properties, EnumMaterial mat) {
		this(properties, mat, 1);
	}
	public ItemScrapable(Item.Properties properties, EnumMaterial mat, int weight) {
		super(properties);
		this.material = mat;
		this.weight = weight;
	}
	
	public ItemScrapable(EnumMaterial mat) {
		this(new Item.Properties(), mat, 1);
	}
	public ItemScrapable(EnumMaterial mat, int weight) {
		this(new Item.Properties(),mat,weight);
	}
	
	public ItemScrapable setSmallestBit() {
		ItemUtils.INSTANCE.addSmallestBit(material, this);
		return this;
	}

	public void setMaterial(EnumMaterial mat) {
		material = mat;
	}

	public EnumMaterial getItemMaterial() {
		return material;
	}

	public void setWeight(int newWeight) {
		weight = newWeight;
	}

	public int getWeight() {
		return weight;
	}

	public boolean canBeScraped() {
		return true;
	}
	
}