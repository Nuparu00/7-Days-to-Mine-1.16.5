package nuparu.sevendaystomine.item;

public interface IScrapable {

	public void setMaterial(EnumMaterial mat);

	public EnumMaterial getItemMaterial();

	public void setWeight(int newWeight);

	public int getWeight();

	public boolean canBeScraped();

}
