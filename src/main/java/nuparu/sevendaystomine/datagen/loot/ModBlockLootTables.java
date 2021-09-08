package nuparu.sevendaystomine.datagen.loot;

import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.RandomValueRange;
import net.minecraftforge.fml.RegistryObject;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModItems;

public class ModBlockLootTables extends BlockLootTables {

    @Override
    protected void addTables() {
        //Generates generic loot table for all blocks
        for(RegistryObject<Block> entry : ModBlocks.BLOCKS.getEntries()){
            Block block = entry.get();
            System.out.println(entry.getId().toString());
            if(block.getLootTable() == LootTables.EMPTY){
                continue;
            }
            this.dropSelf(block);
        }
        //Generates specific case loot tables
        this.add(ModBlocks.ORE_POTASSIUM.get(),createSingleItemTableWithSilkTouch(ModBlocks.ORE_POTASSIUM.get(), ModItems.POTASSIUM.get(), new RandomValueRange(1,5)));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
