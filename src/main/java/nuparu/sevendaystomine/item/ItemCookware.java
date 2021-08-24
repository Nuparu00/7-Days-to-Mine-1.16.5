package nuparu.sevendaystomine.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class ItemCookware extends BlockItem implements IScrapable{

	private EnumMaterial material;
	private int weight;
	
	public ItemCookware(Block block) {
		super(block, new Item.Properties().stacksTo(1));
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
