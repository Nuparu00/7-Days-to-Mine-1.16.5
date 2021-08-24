package nuparu.sevendaystomine.init;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.blockplacer.BlockPlacerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ModBlockPlacers {
    public static final DeferredRegister<BlockPlacerType<?>> BLOCK_PLACERS = DeferredRegister.create(ForgeRegistries.BLOCK_PLACER_TYPES,
            SevenDaysToMine.MODID);

   /* public static final RegistryObject<BlockPlacerType<BerryBushBlockPlacer>> BERRY_BUSH_BLOCK_PLACER = BLOCK_PLACERS.register("berry_bush_block_placer",
            () -> new BlockPlacerType(Codec.unit(() -> new BerryBushBlockPlacer())));*/
}
