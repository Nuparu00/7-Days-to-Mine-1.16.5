package nuparu.sevendaystomine.crafting.forge;

import net.minecraft.network.PacketBuffer;
import nuparu.sevendaystomine.item.EnumMaterial;

public class MaterialStack {

    public static final MaterialStack EMPTY = new MaterialStack(EnumMaterial.NONE,0);

    EnumMaterial material;
    int weight;


    public MaterialStack(EnumMaterial material) {
        this(material,1);
    }

    public MaterialStack(EnumMaterial material, int weight) {
        this.material = material;
        this.weight = weight;
    }


    public static MaterialStack fromNetwork(PacketBuffer buffer) {
        String matName = buffer.readUtf();
        EnumMaterial material = EnumMaterial.byName(matName);
        if(material == EnumMaterial.NONE) return EMPTY;
        int weight = buffer.readInt();
        return new MaterialStack(material,weight);
    }

    public final void toNetwork(PacketBuffer buffer) {
        buffer.writeUtf(material.getRegistryName());
        buffer.writeInt(weight);
    }

    public EnumMaterial getMaterial(){
        return material;
    }

    public int getWeight(){
        return weight;
    }
}
