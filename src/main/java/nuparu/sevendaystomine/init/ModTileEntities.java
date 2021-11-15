package nuparu.sevendaystomine.init;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.tileentity.*;

@Mod.EventBusSubscriber
public class ModTileEntities {

	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES,
			SevenDaysToMine.MODID);

	public static final RegistryObject<TileEntityType<TileEntityForge>> FORGE = TILE_ENTITIES.register("forge",
			() -> TileEntityType.Builder.of(TileEntityForge::new, ModBlocks.FORGE.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTorch>> TORCH = TILE_ENTITIES.register("torch",
			() -> TileEntityType.Builder.of(TileEntityTorch::new, ModBlocks.TORCH_LIT.get(), ModBlocks.TORCH_LIT_WALL.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityCalendar>> CALENDAR = TILE_ENTITIES.register("calendar",
			() -> TileEntityType.Builder.of(TileEntityCalendar::new, ModBlocks.CALENDAR.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityFlag>> FLAG = TILE_ENTITIES.register("flag",
			() -> TileEntityType.Builder.of(TileEntityFlag::new, ModBlocks.FLAG.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityMetalSpikes>> METAL_SPIKES = TILE_ENTITIES.register("metal_spikes",
			() -> TileEntityType.Builder.of(TileEntityMetalSpikes::new, ModBlocks.METAL_SPIKES.get(), ModBlocks.METAL_SPIKES_EXTENDED.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityPhoto>> PHOTO = TILE_ENTITIES.register("photo",
			() -> TileEntityType.Builder.of(TileEntityPhoto::new, ModBlocks.PHOTO.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityRadio>> RADIO = TILE_ENTITIES.register("radio",
			() -> TileEntityType.Builder.of(TileEntityRadio::new, ModBlocks.RADIO.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityThermometer>> THERMOMETER = TILE_ENTITIES.register("thermometer",
			() -> TileEntityType.Builder.of(TileEntityThermometer::new, ModBlocks.THERMOMETER.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityWheels>> WHEELS = TILE_ENTITIES.register("wheels",
			() -> TileEntityType.Builder.of(TileEntityWheels::new, ModBlocks.WHEELS.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityCamera>> CAMERA = TILE_ENTITIES.register("camera",
			() -> TileEntityType.Builder.of(TileEntityCamera::new, ModBlocks.CAMERA.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityGlobe>> GLOBE = TILE_ENTITIES.register("globe",
			() -> TileEntityType.Builder.of(TileEntityGlobe::new, ModBlocks.GLOBE.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityWoodenSpikes>> WOODEN_SPIKES = TILE_ENTITIES.register("wooden_spikes",
			() -> TileEntityType.Builder.of(TileEntityWoodenSpikes::new, ModBlocks.WOODEN_SPIKES.get(), ModBlocks.WOODEN_SPIKES_BLOODED.get(),
					ModBlocks.WOODEN_SPIKES_BROKEN.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityWoodenLogSpike>> LOG_SPIKES = TILE_ENTITIES.register("log_spikes",
			() -> TileEntityType.Builder.of(TileEntityWoodenLogSpike::new,  ModBlocks.ACACIA_LOG_SPIKE.get(),
					ModBlocks.ACACIA_LOG_SPIKE.get(), ModBlocks.BIRCH_LOG_SPIKE.get(), ModBlocks.DARK_OAK_LOG_SPIKE.get(),
					ModBlocks.JUNGLE_LOG_SPIKE.get(), ModBlocks.OAK_LOG_SPIKE.get(), ModBlocks.SPRUCE_LOG_SPIKE.get(),
					ModBlocks.CRIMSON_LOG_SPIKE.get(), ModBlocks.WARPED_LOG_SPIKE.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityBackpack>> BACKPACK = TILE_ENTITIES.register("backpack",
			() -> TileEntityType.Builder.of(TileEntityBackpack::new, ModBlocks.BACKPACK_ARMY.get(), ModBlocks.BACKPACK_MEDICAL.get(),
					ModBlocks.BACKPACK_NORMAL.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityBirdNest>> BIRD_NEST = TILE_ENTITIES.register("bird_nest",
			() -> TileEntityType.Builder.of(TileEntityBirdNest::new, ModBlocks.BIRD_NEST.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityCardboard>> CARDBOARD = TILE_ENTITIES.register("cardboard",
			() -> TileEntityType.Builder.of(TileEntityCardboard::new, ModBlocks.CARDBOARD_BOX.get()).build(null));

	public static final RegistryObject<TileEntityType<TileEntityCashRegister>> CASH_REGISTER = TILE_ENTITIES.register("cash_register",
			() -> TileEntityType.Builder.of(TileEntityCashRegister::new, ModBlocks.CASH_REGISTER.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityDresser>> DRESSER = TILE_ENTITIES.register("dresser",
			() -> TileEntityType.Builder.of(TileEntityDresser::new, ModBlocks.DRESSER.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityCorpse>> CORPSE = TILE_ENTITIES.register("corpse",
			() -> TileEntityType.Builder.of(TileEntityCorpse::new, ModBlocks.CORPSE_00.get(),ModBlocks.CORPSE_01.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityCupboard>> CUPBOARD = TILE_ENTITIES.register("cupboard",
			() -> TileEntityType.Builder.of(TileEntityCupboard::new, ModBlocks.CUPBOARD.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityFileCabinet>> FILE_CABINET = TILE_ENTITIES.register("file_cabinet",
			() -> TileEntityType.Builder.of(TileEntityFileCabinet::new, ModBlocks.FILE_CABINET.get()).build(null));
	
	
	public static final RegistryObject<TileEntityType<TileEntityGarbage>> GARBAGE = TILE_ENTITIES.register("garbage",
			() -> TileEntityType.Builder.of(TileEntityGarbage::new, ModBlocks.GARBAGE.get()).build(null));
	
	public static final RegistryObject<TileEntityType<TileEntityMailBox>> MAIL_BOX = TILE_ENTITIES.register("mail_box",
			() -> TileEntityType.Builder.of(TileEntityMailBox::new, ModBlocks.MAIL_BOX.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityLamp>> LAMP = TILE_ENTITIES.register("lamp",
			() -> TileEntityType.Builder.of(TileEntityLamp::new, ModBlocks.LAMP.get()).build(null));

	public static final RegistryObject<TileEntityType<TileEntityMedicalCabinet>> MEDICAL_CABINET = TILE_ENTITIES.register("medical_cabinet",
			() -> TileEntityType.Builder.of(TileEntityMedicalCabinet::new, ModBlocks.MEDICAL_CABINET.get()).build(null));
	

	public static final RegistryObject<TileEntityType<TileEntityMonitor>> MONITOR = TILE_ENTITIES.register("monitor",
			() -> TileEntityType.Builder.of(TileEntityMonitor::new, ModBlocks.MONITOR_LINUX.get(), ModBlocks.MONITOR_MAC.get(),
					ModBlocks.MONITOR_WIN98.get(), ModBlocks.MONITOR_WINXP.get(), ModBlocks.MONITOR_WIN7.get(),
					ModBlocks.MONITOR_WIN8.get(), ModBlocks.MONITOR_WIN10.get(), ModBlocks.MONITOR_OFF.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityMicrowave>> MICROWAVE = TILE_ENTITIES.register("microwave",
			() -> TileEntityType.Builder.of(TileEntityMicrowave::new, ModBlocks.MICROWAVE.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityScreenProjector>> SCREEN_PROJECTOR = TILE_ENTITIES.register("screen_projector",
			() -> TileEntityType.Builder.of(TileEntityScreenProjector::new, ModBlocks.SCREEN_PROJECTOR.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntitySleepingBag>> SLEEPING_BAG = TILE_ENTITIES.register("sleeping_bag",
			() -> TileEntityType.Builder.of(TileEntitySleepingBag::new, ModBlocks.SLEEPING_BAG_BLACK.get(), ModBlocks.SLEEPING_BAG_WHITE.get(),
					ModBlocks.SLEEPING_BAG_RED.get(), ModBlocks.SLEEPING_BAG_PINK.get(),
					ModBlocks.SLEEPING_BAG_BLUE.get(), ModBlocks.SLEEPING_BAG_LIGHT_BLUE.get(),
					ModBlocks.SLEEPING_BAG_YELLOW.get(), ModBlocks.SLEEPING_BAG_ORANGE.get(),
					ModBlocks.SLEEPING_BAG_GREEN.get(), ModBlocks.SLEEPING_BAG_LIME.get(),
					ModBlocks.SLEEPING_BAG_BROWN.get(), ModBlocks.SLEEPING_BAG_GRAY.get(),
					ModBlocks.SLEEPING_BAG_PURPLE.get(), ModBlocks.SLEEPING_BAG_MAGENTA.get(),
					ModBlocks.SLEEPING_BAG_SILVER.get(), ModBlocks.SLEEPING_BAG_CYAN.get()).build(null));
	
	public static final RegistryObject<TileEntityType<TileEntityToilet>> TOILET = TILE_ENTITIES.register("toilet",
			() -> TileEntityType.Builder.of(TileEntityToilet::new, ModBlocks.TOILET.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTrashCan>> TRASH_CAN = TILE_ENTITIES.register("trash_can",
			() -> TileEntityType.Builder.of(TileEntityTrashCan::new, ModBlocks.TRASH_CAN.get()).build(null));
	

	public static final RegistryObject<TileEntityType<TileEntityTrashBin>> TRASH_BIN = TILE_ENTITIES.register("trash_bin",
			() -> TileEntityType.Builder.of(TileEntityTrashBin::new, ModBlocks.TRASH_BIN.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityEnergyPole>> ENERGY_POLE = TILE_ENTITIES.register("energy_pole",
			() -> TileEntityType.Builder.of(TileEntityEnergyPole::new, ModBlocks.ENERGY_POLE.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityEnergySwitch>> ENERGY_SWITCH = TILE_ENTITIES.register("energy_switch",
			() -> TileEntityType.Builder.of(TileEntityEnergySwitch::new, ModBlocks.ENERGY_SWITCH.get()).build(null));
	
	public static final RegistryObject<TileEntityType<TileEntityCodeSafe>> CODE_SAFE = TILE_ENTITIES.register("code_safe",
			() -> TileEntityType.Builder.of(TileEntityCodeSafe::new, ModBlocks.CODE_SAFE.get()).build(null));
	
	public static final RegistryObject<TileEntityType<TileEntityBookshelf>> BOOKSHELF = TILE_ENTITIES.register("bookshelf",
			() -> TileEntityType.Builder.of(TileEntityBookshelf::new,  ModBlocks.OAK_BOOKSHELF.get(), ModBlocks.SPRUCE_BOOKSHELF.get(),
					ModBlocks.BIRCH_BOOKSHELF.get(), ModBlocks.JUNGLE_BOOKSHELF.get(), ModBlocks.ACACIA_BOOKSHELF.get(),
					ModBlocks.DARK_OAK_BOOKSHELF.get(), ModBlocks.WARPED_BOOKSHELF.get(),
					ModBlocks.CRIMSON_BOOKSHELF.get()).build(null));
	
	public static final RegistryObject<TileEntityType<TileEntityBigSignMaster>> BIG_SIGN_MASTER = TILE_ENTITIES.register("big_sign_master",
			() -> TileEntityType.Builder.of(TileEntityBigSignMaster::new, ModBlocks.BIG_SIGN_MASTER.get()).build(null));
	
	public static final RegistryObject<TileEntityType<TileEntityBigSignSlave>> BIG_SIGN_SLAVE = TILE_ENTITIES.register("big_sign_slave",
			() -> TileEntityType.Builder.of(TileEntityBigSignSlave::new, ModBlocks.BIG_SIGN_SLAVE.get()).build(null));

	public static final RegistryObject<TileEntityType<TileEntityShield>> SHIELD = TILE_ENTITIES.register("shield",
			() -> TileEntityType.Builder.of(TileEntityShield::new).build(null));
	
	public static final RegistryObject<TileEntityType<TileEntitySolarPanel>> SOLAR_PANEL = TILE_ENTITIES.register("solar_panel",
			() -> TileEntityType.Builder.of(TileEntitySolarPanel::new, ModBlocks.SOLAR_PANEL.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityRefrigerator>> REFRIGERATOR = TILE_ENTITIES.register("refrigerator",
			() -> TileEntityType.Builder.of(TileEntityRefrigerator::new, ModBlocks.FRIDGE.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityCarMaster>> CAR_MASTER = TILE_ENTITIES.register("car_master",
			() -> TileEntityType.Builder.of(TileEntityCarMaster::new, ModBlocks.POLICE_CAR.get(), ModBlocks.AMBULANCE.get(), ModBlocks.SEDAN_BLACK.get(),
					ModBlocks.SEDAN_BLUE.get(), ModBlocks.SEDAN_GREEN.get(), ModBlocks.SEDAN_RED.get(),
					ModBlocks.SEDAN_WHITE.get(), ModBlocks.SEDAN_YELLOW.get() ).build(null));
	
	public static final RegistryObject<TileEntityType<TileEntityCarSlave>> CAR_SLAVE = TILE_ENTITIES.register("car_slave",
			() -> TileEntityType.Builder.of(TileEntityCarSlave::new, ModBlocks.POLICE_CAR.get(), ModBlocks.AMBULANCE.get(), ModBlocks.SEDAN_BLACK.get(),
					ModBlocks.SEDAN_BLUE.get(), ModBlocks.SEDAN_GREEN.get(), ModBlocks.SEDAN_RED.get(),
					ModBlocks.SEDAN_WHITE.get(), ModBlocks.SEDAN_YELLOW.get() ).build(null));
	
	public static final RegistryObject<TileEntityType<TileEntityComputer>> COMPUTER = TILE_ENTITIES.register("computer",
			() -> TileEntityType.Builder.of(TileEntityComputer::new, ModBlocks.COMPUTER.get()).build(null));
	
	public static final RegistryObject<TileEntityType<TileEntityGasGenerator>> GAS_GENERATOR = TILE_ENTITIES.register("gas_generator",
			() -> TileEntityType.Builder.of(TileEntityGasGenerator::new, ModBlocks.GENERATOR_GAS.get()).build(null));

	public static final RegistryObject<TileEntityType<TileEntityCombustionGenerator>> COMBUSTION_GENERATOR = TILE_ENTITIES.register("combustion_generator",
			() -> TileEntityType.Builder.of(TileEntityCombustionGenerator::new, ModBlocks.GENERATOR_COMBUSTION.get()).build(null));

	public static final RegistryObject<TileEntityType<TileEntityTurretBase>> TURRET_BASE = TILE_ENTITIES.register("tuuret_base",
			() -> TileEntityType.Builder.of(TileEntityTurretBase::new, ModBlocks.TURRET_BASE.get()).build(null));
	
	public static final RegistryObject<TileEntityType<TileEntityTurretAdvanced>> TURRET_ADVANCED = TILE_ENTITIES.register("turret_advanced",
			() -> TileEntityType.Builder.of(TileEntityTurretAdvanced::new, ModBlocks.TURRET_ADVANCED.get()).build(null));
	
	public static final RegistryObject<TileEntityType<TileEntityWindTurbine>> WIND_TURBINE = TILE_ENTITIES.register("wind_turbine",
			() -> TileEntityType.Builder.of(TileEntityWindTurbine::new, ModBlocks.WIND_TURBINE.get()).build(null));
	
	public static final RegistryObject<TileEntityType<TileEntityChemistryStation>> CHEMISTRY_STATION = TILE_ENTITIES.register("chemistry_station",
			() -> TileEntityType.Builder.of(TileEntityChemistryStation::new, ModBlocks.CHEMISTRY_STATION.get()).build(null));
	
	public static final RegistryObject<TileEntityType<TileEntityFlamethrower>> FLAMETHROWER_TRAP = TILE_ENTITIES.register("flamethrower_trap",
			() -> TileEntityType.Builder.of(TileEntityFlamethrower::new, ModBlocks.FLAMETHOWER.get()).build(null));
	
	public static final RegistryObject<TileEntityType<TileEntitySeparator>> SEPARATOR = TILE_ENTITIES.register("separator",
			() -> TileEntityType.Builder.of(TileEntitySeparator::new, ModBlocks.SEPARATOR.get()).build(null));
	
	public static final RegistryObject<TileEntityType<TileEntityBatteryStation>> BATTERY_STATION = TILE_ENTITIES.register("battery_station",
			() -> TileEntityType.Builder.of(TileEntityBatteryStation::new, ModBlocks.BATTERY_STATION.get()).build(null));
	

	public static final RegistryObject<TileEntityType<TileEntityAirplaneRotor>> AIRPLANE_ROTOR = TILE_ENTITIES.register("airplane_rotor",
			() -> TileEntityType.Builder.of(TileEntityAirplaneRotor::new, ModBlocks.AIRPLANE_ROTOR.get()).build(null));
	
	public static final RegistryObject<TileEntityType<TileEntityWorkbench>> WORKBENCH = TILE_ENTITIES.register("workbench",
			() -> TileEntityType.Builder.of(TileEntityWorkbench::new, ModBlocks.WORKBENCH.get()).build(null));
	
	public static final RegistryObject<TileEntityType<TileEntityTable>> TABLE = TILE_ENTITIES.register("table",
			() -> TileEntityType.Builder.of(TileEntityTable::new, ModBlocks.OAK_WRITING_TABLE.get(), ModBlocks.MODERN_WRITING_TABLE.get()).build(null));

	public static final RegistryObject<TileEntityType<TileEntityStreetSign>> STREET_SIGN = TILE_ENTITIES.register("street_sign",
			() -> TileEntityType.Builder.of(TileEntityStreetSign::new, ModBlocks.STREET_SIGN.get(),ModBlocks.STREET_SIGN_WALL.get()).build(null));

	public static final RegistryObject<TileEntityType<TileEntityPrinter>> PRINTER = TILE_ENTITIES.register("printer",
			() -> TileEntityType.Builder.of(TileEntityPrinter::new, ModBlocks.PRINTER.get()).build(null));

	public static final RegistryObject<TileEntityType<TileEntityGrill>> COOKING_GRILL = TILE_ENTITIES.register("cooking_grill",
			() -> TileEntityType.Builder.of(TileEntityGrill::new, ModBlocks.COOKING_GRILL.get()).build(null));

	public static final RegistryObject<TileEntityType<TileEntityCookingPot>> COOKING_POT = TILE_ENTITIES.register("cooking_pot",
			() -> TileEntityType.Builder.of(TileEntityCookingPot::new, ModBlocks.COOKING_POT.get()).build(null));

	public static final RegistryObject<TileEntityType<TileEntityBeaker>> BEAKER = TILE_ENTITIES.register("beaker",
			() -> TileEntityType.Builder.of(TileEntityBeaker::new, ModBlocks.COOKING_GRILL_BEAKER.get()).build(null));

	public static final RegistryObject<TileEntityType<TileEntityCarPlacer>> CAR_PLACER = TILE_ENTITIES.register("car_placer",
			() -> TileEntityType.Builder.of(TileEntityCarPlacer::new, ModBlocks.CAR_PLACER.get()).build(null));
}
