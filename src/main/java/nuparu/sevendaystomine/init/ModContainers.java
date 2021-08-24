package nuparu.sevendaystomine.init;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.inventory.block.*;

public class ModContainers {
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS,
			SevenDaysToMine.MODID);

	public static final RegistryObject<ContainerType<ContainerForge>> FORGE = CONTAINERS.register("forge",
			() -> IForgeContainerType.create(ContainerForge::createContainerClientSide));

	public static final RegistryObject<ContainerType<ContainerSmall>> SMALL = CONTAINERS.register("small",
			() -> IForgeContainerType.create(ContainerSmall::createContainerClientSide));

	public static final RegistryObject<ContainerType<ContainerTiny>> TINY = CONTAINERS.register("tiny",
			() -> IForgeContainerType.create(ContainerTiny::createContainerClientSide));

	public static final RegistryObject<ContainerType<ContainerChemistryStation>> CHEMISTRY_STATION = CONTAINERS.register("chemistry_station",
			() -> IForgeContainerType.create(ContainerChemistryStation::createContainerClientSide));

	public static final RegistryObject<ContainerType<ContainerSeparator>> SEPARATOR = CONTAINERS.register("separator",
			() -> IForgeContainerType.create(ContainerSeparator::createContainerClientSide));

	public static final RegistryObject<ContainerType<ContainerBatteryStation>> BATTERY_STATION = CONTAINERS.register("battery_station",
			() -> IForgeContainerType.create(ContainerBatteryStation::createContainerClientSide));

	public static final RegistryObject<ContainerType<ContainerCombustionGenerator>> COMBUSTION_GENERATOR = CONTAINERS.register("combustion_generator",
			() -> IForgeContainerType.create(ContainerCombustionGenerator::createContainerClientSide));

	public static final RegistryObject<ContainerType<ContainerGasGenerator>> GAS_GENERATOR = CONTAINERS.register("gas_generator",
			() -> IForgeContainerType.create(ContainerGasGenerator::createContainerClientSide));

	public static final RegistryObject<ContainerType<ContainerFlamethrower>> FLAMETHROWER = CONTAINERS.register("flamethrower",
			() -> IForgeContainerType.create(ContainerFlamethrower::createContainerClientSide));

	public static final RegistryObject<ContainerType<ContainerScreenProjector>> SCREEN_PROJECTOR = CONTAINERS.register("screen_projector",
			() -> IForgeContainerType.create(ContainerScreenProjector::createContainerClientSide));

	public static final RegistryObject<ContainerType<ContainerWorkbench>> WORKBENCH = CONTAINERS.register("workbench",
			() -> IForgeContainerType.create(ContainerWorkbench::createContainerClientSide));

	public static final RegistryObject<ContainerType<ContainerComputer>> COMPUTER = CONTAINERS.register("computer",
			() -> IForgeContainerType.create(ContainerComputer::createContainerClientSide));

	public static final RegistryObject<ContainerType<ContainerMonitor>> MONITOR = CONTAINERS.register("monitor",
			() -> IForgeContainerType.create(ContainerMonitor::createContainerClientSide));

	public static final RegistryObject<ContainerType<ContainerTurretAdvanced>> TURRET_ADVANCED = CONTAINERS.register("turret_advanced",
			() -> IForgeContainerType.create(ContainerTurretAdvanced::createContainerClientSide));

}
