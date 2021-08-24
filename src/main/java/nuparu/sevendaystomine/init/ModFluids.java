package nuparu.sevendaystomine.init;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.BlockTable;
import nuparu.sevendaystomine.item.EnumMaterial;

public class ModFluids {

	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS,
			SevenDaysToMine.MODID);

	public static final RegistryObject<Fluid> GASOLINE = FLUIDS.register("gasoline",
			() -> new ForgeFlowingFluid.Source(gasoline()));
	
	public static final RegistryObject<Fluid> GASOLINE_FLOWING = FLUIDS.register("gasoline_flowing",
			() -> new ForgeFlowingFluid.Flowing(gasoline()));


	private static ForgeFlowingFluid.Properties gasoline() {
		return new ForgeFlowingFluid.Properties(GASOLINE, GASOLINE_FLOWING,
				FluidAttributes.builder(new ResourceLocation(SevenDaysToMine.MODID, "blocks/gasoline_still"),
						new ResourceLocation(SevenDaysToMine.MODID, "blocks/gasoline_flow")).color(0x3F1080FF))
								.block(() -> (FlowingFluidBlock)(ModBlocks.GASOLINE.get()));
	}
}
