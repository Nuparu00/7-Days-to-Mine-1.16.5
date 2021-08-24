package nuparu.sevendaystomine.item;

import net.minecraft.item.Item;
import nuparu.sevendaystomine.init.ModItemGroups;
import nuparu.sevendaystomine.util.ItemUtils;

public class ItemScrap extends Item implements IScrapable {

	private EnumMaterial material;

	private int weight;

	public ItemScrap(Item.Properties properties, EnumMaterial mat) {
		this(properties,mat, 1);
	}

	public ItemScrap(Item.Properties properties, EnumMaterial mat, int weight) {
		super(properties.tab(ModItemGroups.TAB_MATERIALS));
		this.material = mat;
		this.weight = weight;
		ItemUtils.INSTANCE.addScrapResult(mat, this);
	}
	
	public ItemScrap(EnumMaterial mat) {
		this(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS),mat, 1);
	}

	public ItemScrap(EnumMaterial mat, int weight) {
		this(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS),mat,weight);
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
		return false;
	}
}
