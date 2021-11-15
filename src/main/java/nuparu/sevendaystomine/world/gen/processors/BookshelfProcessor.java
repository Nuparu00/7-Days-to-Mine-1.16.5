package nuparu.sevendaystomine.world.gen.processors;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.*;
import nuparu.sevendaystomine.block.BlockBookshelfEnhanced;
import nuparu.sevendaystomine.init.ModStructureProcessors;

import java.util.Random;

public class BookshelfProcessor extends StructureProcessor {


    public static final Codec<BookshelfProcessor> CODEC = Codec.unit(BookshelfProcessor::new);
    public static final BookshelfProcessor INSTANCE = new BookshelfProcessor();
    private boolean[] oldConnections;

    @Override
    public Template.BlockInfo processBlock(IWorldReader worldView, BlockPos pos, BlockPos blockPos, Template.BlockInfo structureBlockInfoLocal, Template.BlockInfo structureBlockInfoWorld, PlacementSettings structurePlacementData) {
        BlockState blockState = structureBlockInfoWorld.state;
        BlockPos worldPos = structureBlockInfoWorld.pos;
        Random random = structurePlacementData.getRandom(worldPos);

        if (blockState.getBlock() instanceof BlockBookshelfEnhanced) {
            CompoundNBT nbt = new CompoundNBT();
            if(random.nextInt(4) == 0) {
                nbt.putString("LootTable", "sevendaystomine:containers/bookshelf/bookshelf_rare");
            }
            else {
                nbt.putString("LootTable", "sevendaystomine:containers/bookshelf/bookshelf_common");
            }
            return new Template.BlockInfo(worldPos, blockState, nbt);
        }
        return structureBlockInfoWorld;
    }

    @Override
    protected IStructureProcessorType<?> getType() {
        return ModStructureProcessors.BOOKSHELF_PROCESSOR;
    }

}
