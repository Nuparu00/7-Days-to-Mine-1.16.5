package nuparu.sevendaystomine.init;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.util.Utils;

public class ModDataSerializers {

	public static final DeferredRegister<DataSerializerEntry> SERIALIZERS = DeferredRegister.create(ForgeRegistries.DATA_SERIALIZERS,
			SevenDaysToMine.MODID);

	public static final RegistryObject<DataSerializerEntry> DIALOGUES = SERIALIZERS.register("dialogues",
			() -> new DataSerializerEntry(Utils.DIALOGUES));

}
