package nuparu.sevendaystomine.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ItemQualityScrapable extends ItemQuality implements IScrapable {

	private EnumMaterial material;

	private int weight;

	public ItemQualityScrapable(Item.Properties properties, EnumMaterial mat) {
		this(properties, mat, 1);
	}

	public ItemQualityScrapable(Item.Properties properties, EnumMaterial mat, int weight) {
		super(properties);
		this.material = mat;
		this.weight = weight;
	}
	
	public ItemQualityScrapable(EnumMaterial mat) {
		this(new Item.Properties(), mat);
	}

	public ItemQualityScrapable(EnumMaterial mat, int weight) {
		this(new Item.Properties(),mat,weight);
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

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		switch (getQualityTierFromStack(stack)) {
		case FLAWLESS:
			return 0xA300A3;
		case GREAT:
			return 0x4545CC;
		case FINE:
			return 0x37A337;
		case GOOD:
			return 0xB2B23C;
		case POOR:
			return 0xF09900;
		case FAULTY:
			return 0x89713C;
		case NONE:
		default:
			return super.getRGBDurabilityForDisplay(stack);
		}
	}
}
