package nuparu.sevendaystomine.item;

import nuparu.sevendaystomine.SevenDaysToMine;

public enum EnumMaterial {
	NONE("none"), CARBON("carbon"), IRON("iron"), BRASS("brass"),
	LEAD("lead"), STEEL("steel"), COPPER("copper"),
	BRONZE("bronze"), TIN("tin"), ZINC("zinc"), GOLD("gold"),
	WOLFRAM("wolfram"), URANIUM("uranium"), WOOD("wood"),
	STONE("stone"), GLASS("glass"), CLOTH("cloth"), PLANT_FIBER("plant_fiber"),
	PLASTIC("plastic"), CLAY("clay"), MERCURY("mercury"), POTASSIUM("potassium"),
	CONCRETE("concrete"), LEATHER("leather"), GASOLINE("gasoline"),
	SAND("sand"), PAPER("paper"), STRING("string"), BONE("bone");

	String registryName;

	EnumMaterial(String unlocalizedName) {
		this.registryName = unlocalizedName;
	}

	public String getRegistryName() {
		return this.registryName;
	}

	public String getUnlocalizedName() {
		return "material." + this.registryName;
	}

	public String getLocalizedName() {
		return SevenDaysToMine.proxy.localize(getUnlocalizedName());
	}

	public static EnumMaterial byName(String name){
		for(EnumMaterial material : values()){
			if(material.registryName.equals(name)){
				return material;
			}
		}
		return null;
	}

}