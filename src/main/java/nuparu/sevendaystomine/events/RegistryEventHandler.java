package nuparu.sevendaystomine.events;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.IBlockBase;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModFeatures;
import nuparu.sevendaystomine.loot.modifier.GrassModifier;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = SevenDaysToMine.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEventHandler {
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> registry = event.getRegistry();
        ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(block -> {
            if (block instanceof IBlockBase) {
                BlockItem blockItem = ((IBlockBase) block).createBlockItem();
                if (blockItem != null) {
                    blockItem.setRegistryName(block.getRegistryName());
                    registry.register(blockItem);
                }
            }
        });
    }

    @SubscribeEvent
    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
        ModFeatures.finishRegistration(event);
    }

   /* @SubscribeEvent
    public static void onMissingBlockMapping(RegistryEvent.MissingMappings<Block> event) {
        System.out.println("onMissingBlockMapping()");
        for (RegistryEvent.MissingMappings.Mapping<Block> entry : event.getAllMappings()) {
            if(entry.key.toString().equals("sevendaystomine:Asphalt")){
                entry.remap(ModBlocks.ASPHALT.get());
            }
        }
    }*/

    /*@SubscribeEvent
    public static void registerModifierSerializers(@Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().register(
                    new GrassModifier.Serializer().setRegistryName(new ResourceLocation(SevenDaysToMine.MODID,"grass"))
            );
        }*/
    }
