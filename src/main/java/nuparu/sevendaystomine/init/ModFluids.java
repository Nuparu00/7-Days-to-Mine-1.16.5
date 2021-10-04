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
	
	public static final RegistryObject<Fluid> GASOLINE_FLOWING = FLUIDS.register("flowing_gasoline",
			() -> new ForgeFlowingFluid.Flowing(gasoline()));

	public static final RegistryObject<Fluid> MERCURY = FLUIDS.register("mercury",
			() -> new ForgeFlowingFluid.Source(mercury()));

	public static final RegistryObject<Fluid> MERCURY_FLOWING = FLUIDS.register("flowing_mercury",
			() -> new ForgeFlowingFluid.Flowing(mercury()));

	private static ForgeFlowingFluid.Properties gasoline() {
		return new ForgeFlowingFluid.Properties(GASOLINE, GASOLINE_FLOWING,
				FluidAttributes.builder(new ResourceLocation(SevenDaysToMine.MODID, "blocks/gasoline_still"),
						new ResourceLocation(SevenDaysToMine.MODID, "blocks/gasoline_flow")))
								.block(() -> (FlowingFluidBlock)(ModBlocks.GASOLINE.get())).bucket(() -> ModItems.GASOLINE_BUCKET.get());
	}

	private static ForgeFlowingFluid.Properties mercury() {
		return new ForgeFlowingFluid.Properties(MERCURY, MERCURY_FLOWING,
				FluidAttributes.builder(new ResourceLocation(SevenDaysToMine.MODID, "blocks/mercury_still"),
						new ResourceLocation(SevenDaysToMine.MODID, "blocks/mercury_flow")))
				.block(() -> (FlowingFluidBlock)(ModBlocks.MERCURY.get())).bucket(() -> ModItems.MERCURY_BUCKET.get());
	}
}
