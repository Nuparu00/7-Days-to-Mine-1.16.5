package nuparu.sevendaystomine.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.gen.processors.DataBlockProcessor;

public class ModStructureProcessors {

    public static IStructureProcessorType<DataBlockProcessor> DATA_BLOCK_PROCESSOR = () -> DataBlockProcessor.CODEC;
    public static IStructureProcessorType<DataBlockProcessor> BOOKSHELF_PROCESSOR = () -> DataBlockProcessor.CODEC;

    public static void register(){
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(SevenDaysToMine.MODID, "data_block_processor"), DATA_BLOCK_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(SevenDaysToMine.MODID, "bookshelf_processor"), BOOKSHELF_PROCESSOR);
    }
}
