package nuparu.sevendaystomine.init;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.entity.TwistedZombieEntity;
import nuparu.sevendaystomine.item.*;
import nuparu.sevendaystomine.computer.EnumSystem;

public class ModItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SevenDaysToMine.MODID);

	public static final RegistryObject<Item> IRON_SCRAP = ITEMS.register("scrap_iron", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> BRASS_SCRAP = ITEMS.register("scrap_brass", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> LEAD_SCRAP = ITEMS.register("scrap_lead", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> COPPER_SCRAP = ITEMS.register("scrap_copper", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> TIN_SCRAP = ITEMS.register("scrap_tin", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> GOLD_SCRAP = ITEMS.register("scrap_gold", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> STEEL_SCRAP = ITEMS.register("scrap_steel", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> ZINC_SCRAP = ITEMS.register("scrap_zinc", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> BRONZE_SCRAP = ITEMS.register("scrap_bronze", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> EMPTY_CAN = ITEMS.register("empty_can", () -> new ItemEmptyCan());
	public static final RegistryObject<Item> PLANK_WOOD = ITEMS.register("plank_wood", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> SMALL_STONE = ITEMS.register("small_stone", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> PLANT_FIBER = ITEMS.register("plant_fiber", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> EMPTY_JAR = ITEMS.register("empty_jar", () -> new ItemEmptyJar());
	public static final RegistryObject<Item> GLASS_SCRAP = ITEMS.register("scrap_glass", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> COFFEE_BERRY = ITEMS.register("coffee_berry", () -> new ItemCoffeeBerry());
	public static final RegistryObject<Item> COFFEE_BEANS = ITEMS.register("coffee_beans", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> CLOTH = ITEMS.register("cloth", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> STONE_AXE = ITEMS.register("stone_axe", () -> new ItemStoneAxe(ModItemTier.STONE_TOOLS, 0, -2, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> STONE_SHOVEL = ITEMS.register("stone_shovel", () -> new ItemQualitySpade(ModItemTier.STONE_TOOLS, -3f, 8f, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> BONE_SHIV = ITEMS.register("bone_shiv", () -> new ItemQualitySword(ModItemTier.BONE_TOOLS, 0, 8f, new Item.Properties().stacksTo(1), EnumLength.SHORT));
	public static final RegistryObject<Item> CRUDE_CLUB = ITEMS.register("crude_club", () -> new ItemQualitySword(ModItemTier.CRUDE_TOOLS, 0, -3.14f, new Item.Properties().stacksTo(1), EnumLength.LONG));
	public static final RegistryObject<Item> WOODEN_CLUB = ITEMS.register("wooden_club", () -> new ItemQualitySword(ModItemTier.WOODEN_TOOLS, 0, -3.14f, new Item.Properties().stacksTo(1), EnumLength.LONG));
	public static final RegistryObject<Item> IRON_REINFORCED_CLUB = ITEMS.register("iron_reinforced_club", () -> new ItemQualitySword(ModItemTier.WOODEN_REINFORCED_TOOLS, 0, -3.2f, new Item.Properties().stacksTo(1), EnumLength.LONG));
	public static final RegistryObject<Item> BARBED_CLUB = ITEMS.register("barbed_club", () -> new ItemQualitySword(ModItemTier.BARBED_TOOLS, 0, -3.22f, new Item.Properties().stacksTo(1), EnumLength.LONG));
	public static final RegistryObject<Item> SPIKED_CLUB = ITEMS.register("spiked_club", () -> new ItemQualitySword(ModItemTier.SPIKED_TOOLS, 0, -3.22f, new Item.Properties().stacksTo(1), EnumLength.LONG));
	public static final RegistryObject<Item> CLAWHAMMER = ITEMS.register("clawhammer", () -> new ItemClawhammer(ModItemTier.IRON_TOOLS, 0, -3.2f, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench", () -> new ItemWrench(ModItemTier.IRON_TOOLS));
	public static final RegistryObject<Item> KITCHEN_KNIFE = ITEMS.register("kitchen_knife", () -> new ItemQualitySword(ModItemTier.IRON_TOOLS, 0, -2.8f, new Item.Properties().stacksTo(1), EnumLength.SHORT));
	public static final RegistryObject<Item> ARMY_KNIFE = ITEMS.register("army_knife", () -> new ItemQualitySword(ModItemTier.ARMY_TOOLS, 0, -2.8f, new Item.Properties().stacksTo(1), EnumLength.SHORT));
	//public static final RegistryObject<Item> DIAMOND_KNIFE = ITEMS.register("diamond_knife", () -> new ItemQualitySword(ModItemTier.DIAMOND_KNIFE, 0, -2.8f, new Item.Properties().stacksTo(1), EnumLength.SHORT));
	public static final RegistryObject<Item> MACHETE = ITEMS.register("machete", () -> new ItemQualitySword(ModItemTier.MACHETE, 0, 8f, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> SCREWDRIVER = ITEMS.register("screwdriver", () -> new ItemScrewdriver());
	public static final RegistryObject<Item> PISTOL = ITEMS.register("pistol", () -> new ItemPistol());
	public static final RegistryObject<Item> AK47 = ITEMS.register("ak47", () -> new ItemAK47());
	public static final RegistryObject<Item> HUNTING_RIFLE = ITEMS.register("hunting_rifle", () -> new ItemHuntingRifle());
	public static final RegistryObject<Item> MAGNUM = ITEMS.register("magnum", () -> new ItemMagnum());
	public static final RegistryObject<Item> MAGNUM_SCOPED = ITEMS.register("magnum_scoped", () -> new ItemMagnum().setFOVFactor(2.5f).setScoped(true));
	public static final RegistryObject<Item> FLAMETHROWER = ITEMS.register("flamethrower", () -> new ItemFlamethrower());
	public static final RegistryObject<Item> MP5 = ITEMS.register("mp5", () -> new ItemMP5());
	public static final RegistryObject<Item> RPG = ITEMS.register("rpg", () -> new ItemRPG());
	public static final RegistryObject<Item> M4 = ITEMS.register("m4", () -> new ItemM4());
	public static final RegistryObject<Item> BOTTLED_MURKY_WATER = ITEMS.register("bottled_murky_water", () -> new ItemDrink(new Item.Properties().stacksTo(16).food(ModFood.MURKY_WATER).craftRemainder(EMPTY_JAR.get()), 250, 60));
	public static final RegistryObject<Item> BOTTLED_WATER = ITEMS.register("bottled_water", () -> new ItemDrink(new Item.Properties().stacksTo(16).food(ModFood.WATER).craftRemainder(EMPTY_JAR.get()), 250, 125));
	public static final RegistryObject<Item> BOTTLED_BEER = ITEMS.register("bottled_beer", () -> new ItemAlcoholDrink(new Item.Properties().stacksTo(16).food(ModFood.WATER).craftRemainder(EMPTY_JAR.get()), 250, 125));
	public static final RegistryObject<Item> GOLDENROD_TEA = ITEMS.register("goldenrod_tea", () -> new ItemTea(new Item.Properties().stacksTo(16).food(ModFood.WATER).craftRemainder(EMPTY_JAR.get()), 250, 125));
	public static final RegistryObject<Item> BOTTLED_COFFEE = ITEMS.register("bottled_coffee", () -> new ItemCoffeeDrink(new Item.Properties().stacksTo(16).food(ModFood.COFFEE_DRINK).craftRemainder(EMPTY_JAR.get()), 250, 250));
	public static final RegistryObject<Item> CANNED_MURKY_WATER = ITEMS.register("canned_murky_water", () -> new ItemDrink(new Item.Properties().stacksTo(16).food(ModFood.MURKY_WATER).craftRemainder(EMPTY_CAN.get()), 250, 60));
	public static final RegistryObject<Item> CANNED_WATER = ITEMS.register("canned_water", () -> new ItemDrink(new Item.Properties().stacksTo(16).food(ModFood.WATER).craftRemainder(EMPTY_CAN.get()), 250, 125));
	public static final RegistryObject<Item> CANNED_CAT_FOOD = ITEMS.register("canned_cat_food", () -> new ItemCannedFood(new Item.Properties().stacksTo(1).food(ModFood.CANNED_ANIMAL_FOOD).craftRemainder(EMPTY_CAN.get()).durability(2)));
	public static final RegistryObject<Item> CANNED_DOG_FOOD = ITEMS.register("canned_dog_food", () -> new ItemCannedFood(new Item.Properties().stacksTo(1).food(ModFood.CANNED_ANIMAL_FOOD).craftRemainder(EMPTY_CAN.get()).durability(2)));
	public static final RegistryObject<Item> CANNED_HAM = ITEMS.register("canned_ham", () -> new ItemCannedFood(new Item.Properties().stacksTo(1).food(ModFood.CANNED_HAM).craftRemainder(EMPTY_CAN.get()).durability(3)));
	public static final RegistryObject<Item> CANNED_CHICKEN = ITEMS.register("canned_chicken", () -> new ItemCannedFood(new Item.Properties().stacksTo(1).food(ModFood.CANNED_CHICKEN).craftRemainder(EMPTY_CAN.get()).durability(4)));
	public static final RegistryObject<Item> CANNED_CHILI = ITEMS.register("canned_chili", () -> new ItemCannedFood(new Item.Properties().stacksTo(1).food(ModFood.CANNED_CHILI).craftRemainder(EMPTY_CAN.get()).durability(2)));
	public static final RegistryObject<Item> CANNED_MISO = ITEMS.register("canned_miso", () -> new ItemCannedSoup(new Item.Properties().stacksTo(1).food(ModFood.CANNED_MISO).craftRemainder(EMPTY_CAN.get()).durability(2), 50, 30));
	public static final RegistryObject<Item> CANNED_PASTA = ITEMS.register("canned_pasta", () -> new ItemCannedFood(new Item.Properties().stacksTo(1).food(ModFood.CANNED_PASTA).craftRemainder(EMPTY_CAN.get()).durability(2)));
	public static final RegistryObject<Item> CANNED_PEARS = ITEMS.register("canned_pears", () -> new ItemCannedFood(new Item.Properties().stacksTo(1).food(ModFood.CANNED_FRUIT_AND_VEGETABLES).craftRemainder(EMPTY_CAN.get()).durability(2)));
	public static final RegistryObject<Item> CANNED_PEAS = ITEMS.register("canned_peas", () -> new ItemCannedFood(new Item.Properties().stacksTo(1).food(ModFood.CANNED_FRUIT_AND_VEGETABLES).craftRemainder(EMPTY_CAN.get()).durability(2)));
	public static final RegistryObject<Item> CANNED_SALMON = ITEMS.register("canned_salmon", () -> new ItemCannedFood(new Item.Properties().stacksTo(1).food(ModFood.CANNED_CHICKEN).craftRemainder(EMPTY_CAN.get()).durability(3)));
	public static final RegistryObject<Item> CANNED_SOUP = ITEMS.register("canned_soup", () -> new ItemCannedSoup(new Item.Properties().stacksTo(1).food(ModFood.CANNED_SOUP).craftRemainder(EMPTY_CAN.get()).durability(2), 50, 30));
	public static final RegistryObject<Item> CANNED_STOCK = ITEMS.register("canned_stock", () -> new ItemCannedSoup(new Item.Properties().stacksTo(1).food(ModFood.CANNED_STOCK).craftRemainder(EMPTY_CAN.get()).durability(2), 50, 30));
	public static final RegistryObject<Item> CANNED_TUNA = ITEMS.register("canned_tuna", () -> new ItemCannedFood(new Item.Properties().stacksTo(1).food(ModFood.CANNED_CHICKEN).craftRemainder(EMPTY_CAN.get()).durability(3)));
	public static final RegistryObject<Item> CANNED_BEEF = ITEMS.register("canned_beef", () -> new ItemCannedFood(new Item.Properties().stacksTo(1).food(ModFood.CANNED_HUGE_MEAT).craftRemainder(EMPTY_CAN.get()).durability(4)));
	public static final RegistryObject<Item> CANNED_LAMB = ITEMS.register("canned_lamb", () -> new ItemCannedFood(new Item.Properties().stacksTo(1).food(ModFood.CANNED_HUGE_MEAT).craftRemainder(EMPTY_CAN.get()).durability(4)));
	public static final RegistryObject<Item> MRE = ITEMS.register("mre", () -> new ItemFoodBitable(new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_FOOD).food(ModFood.MRE).durability(5)));
	public static final RegistryObject<Item> CORN = ITEMS.register("corn", () -> new ItemCorn());
	public static final RegistryObject<Item> BLUEBERRY = ITEMS.register("blueberry", () -> new ItemBlueberry());
	public static final RegistryObject<Item> BANEBERRY = ITEMS.register("baneberry", () -> new ItemBaneberry());
	public static final RegistryObject<Item> BANDAGE = ITEMS.register("bandage", () -> new ItemBandage());
	public static final RegistryObject<Item> BANDAGE_ADVANCED = ITEMS.register("bandage_advanced", () -> new ItemAdvancedBandage());
	public static final RegistryObject<Item> FIRST_AID_KIT = ITEMS.register("first_aid_kit", () -> new ItemFirstAidKit());
	public static final RegistryObject<Item> BLOOD_BAG = ITEMS.register("blood_bag", () -> new ItemBloodBag());
	public static final RegistryObject<Item> BLOOD_DRAW_KIT = ITEMS.register("blood_draw_kit", () -> new ItemBloodDrawKit());
	public static final RegistryObject<Item> ANTIBIOTICS = ITEMS.register("antibiotics", () -> new ItemAntibiotics());
	public static final RegistryObject<Item> INGOT_LEAD = ITEMS.register("lead_ingot", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> INGOT_BRASS = ITEMS.register("brass_ingot", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> INGOT_STEEL = ITEMS.register("steel_ingot", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> INGOT_COPPER = ITEMS.register("copper_ingot", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> INGOT_TIN = ITEMS.register("tin_ingot", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> INGOT_ZINC = ITEMS.register("zinc_ingot", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> INGOT_BRONZE = ITEMS.register("bronze_ingot", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> CENT = ITEMS.register("cent", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> BACKPACK = ITEMS.register("backpack", () -> new ItemBackpack());
	public static final RegistryObject<Item> MOLD_INGOT = ITEMS.register("ingot_mold", () -> new ItemMold());
	public static final RegistryObject<Item> BULLET_TIP_MOLD = ITEMS.register("bullet_tip_mold", () -> new ItemMold());
	public static final RegistryObject<Item> BULLET_CASING_MOLD = ITEMS.register("bullet_casing_mold", () -> new ItemMold());
	public static final RegistryObject<Item> CEMENT_MOLD = ITEMS.register("cement_mold", () -> new ItemMold());
	public static final RegistryObject<Item> CONCRETE_MIX = ITEMS.register("concrete_mix", () -> new Item(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_MATERIALS).craftRemainder(Items.BUCKET)));
	public static final RegistryObject<Item> CEMENT = ITEMS.register("cement", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> NINE_MM_BULLET = ITEMS.register("pistol_bullet", () -> new ItemBullet(new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> SEVEN_MM_BULLET = ITEMS.register("rifle_bullet", () -> new ItemBullet(new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> TEN_MM_BULLET = ITEMS.register("smg_bullet", () -> new ItemBullet(new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> MAGNUM_BULLET = ITEMS.register("magnum_bullet", () -> new ItemBullet(new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> SHOTGUN_SHELL = ITEMS.register("shotgun_shell", () -> new ItemBullet(new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> ROCKET = ITEMS.register("rocket", () -> new ItemBullet(new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	
	/*public static final RegistryObject<Item> FRIDGE = new ItemRefrigerator().setRegistryName("FridgeItem").setUnlocalizedName("Fridge")
			.setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	public static final RegistryObject<Item> SLEEPING_BAG = new ItemSleepingBag().setRegistryName("SleepingBagItem")
			.setUnlocalizedName("SleepingBagItem").setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	public static final RegistryObject<Item> WOODEN_DOOR_ITEM = new ItemDoorBase() {
		public Block getDoor() {
			return ModBlocks.WOODEN_DOOR;
		}
	}.setRegistryName("WoodenDoorItem").setUnlocalizedName("WoodenDoorItem")
			.setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	public static final RegistryObject<Item> WOODEN_DOOR_REINFORCED_ITEM = new ItemDoorBase() {
		public Block getDoor() {
			return ModBlocks.WOODEN_DOOR_REINFORCED;
		}
	}.setRegistryName("WoodenDoorReinforcedItem").setUnlocalizedName("WoodenDoorReinforcedItem")
			.setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	
	public static final RegistryObject<Item> WOODEN_DOOR_IRON_REINFORCED_ITEM = new ItemDoorBase() {
		public Block getDoor() {
			return ModBlocks.WOODEN_DOOR_IRON_REINFORCED;
		}
	}.setRegistryName("wooden_door_iron_reinforced_item").setUnlocalizedName("wooden_door_iron_reinforced_item")
			.setCreativeTab(SevenDaysToMine.TAB_BUILDING);


	public static final RegistryObject<Item> LOCKED_DOOR_ITEM = new ItemDoorBase() {
		public Block getDoor() {
			return ModBlocks.LOCKED_DOOR;
		}
	}.setRegistryName("locked_door_item").setUnlocalizedName("locked_door_item")
			.setCreativeTab(SevenDaysToMine.TAB_BUILDING);
			
			*/

	public static final RegistryObject<Item> AUGER = ITEMS.register("auger", () -> new ItemAuger(4, 12, ModItemTier.AUGER));
	public static final RegistryObject<Item> NETHERITE_AUGER = ITEMS.register("netherite_auger", () -> new ItemAuger(6, 20, ModItemTier.NETHERITE_AUGER).setReloadAmount(2));
	public static final RegistryObject<Item> CHAINSAW = ITEMS.register("chainsaw", () -> new ItemChainsaw(5, 12, ModItemTier.AUGER));
	public static final RegistryObject<Item> NETHERITE_CHAINSAW = ITEMS.register("netherite_chainsaw", () -> new ItemChainsaw(7, 20, ModItemTier.NETHERITE_AUGER).setReloadAmount(2));
	public static final RegistryObject<Item> COPPER_PICKAXE = ITEMS.register("copper_pickaxe", () -> new ItemQualityPickaxe(ModItemTier.COPPER_TOOLS, 0, -2.8f, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> BRONZE_PICKAXE = ITEMS.register("bronze_pickaxe", () -> new ItemQualityPickaxe(ModItemTier.BRONZE_TOOLS, 0, -2.8f, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> IRON_PICKAXE = ITEMS.register("iron_pickaxe", () -> new ItemQualityPickaxe(ModItemTier.IRON_TOOLS, 0, -2.8f, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> STEEL_PICKAXE = ITEMS.register("steel_pickaxe", () -> new ItemQualityPickaxe(ModItemTier.STEEL_TOOLS, 0, -2.8f, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> SCRAP_PICKAXE = ITEMS.register("scrap_pickaxe", () -> new ItemQualityPickaxe(ModItemTier.SCRAP_TOOLS, 0, -2.8f, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> MP3_PLAYER = ITEMS.register("mp3_player", () -> new ItemMP3());
	public static final RegistryObject<Item> RAM = ITEMS.register("ram", () -> new Item(new Item.Properties().stacksTo(16).tab(ModItemGroups.TAB_ELECTRICITY)));
	public static final RegistryObject<Item> MOTHERBOARD = ITEMS.register("motherboard", () -> new Item(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_ELECTRICITY)));
	public static final RegistryObject<Item> CPU = ITEMS.register("cpu", () -> new Item(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_ELECTRICITY)));
	public static final RegistryObject<Item> GPU = ITEMS.register("gpu", () -> new Item(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_ELECTRICITY)));
	public static final RegistryObject<Item> HDD = ITEMS.register("hdd", () -> new Item(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_ELECTRICITY)));
	public static final RegistryObject<Item> POWER_SUPPLY = ITEMS.register("power_supply", () -> new Item(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_ELECTRICITY)));
	public static final RegistryObject<Item> DISC = ITEMS.register("disc", () -> new Item(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_ELECTRICITY)));
	public static final RegistryObject<Item> LINUX_DISC = ITEMS.register("linux_disc", () -> new ItemInstallDisc(EnumSystem.LINUX));
	public static final RegistryObject<Item> MAC_DISC = ITEMS.register("mac_disc", () -> new ItemInstallDisc(EnumSystem.MAC));
	public static final RegistryObject<Item> WIN98_DISC = ITEMS.register("win98_disc", () -> new ItemInstallDisc(EnumSystem.WIN98));
	public static final RegistryObject<Item> WINXP_DISC = ITEMS.register("winxp_disc", () -> new ItemInstallDisc(EnumSystem.WINXP));
	public static final RegistryObject<Item> WIN7_DISC = ITEMS.register("win7_disc", () -> new ItemInstallDisc(EnumSystem.WIN7));
	public static final RegistryObject<Item> WIN8_DISC = ITEMS.register("win8_disc", () -> new ItemInstallDisc(EnumSystem.WIN8));
	public static final RegistryObject<Item> WIN10_DISC = ITEMS.register("win10_disc", () -> new ItemInstallDisc(EnumSystem.WIN10));
	public static final RegistryObject<Item> WIRE = ITEMS.register("wire", () -> new ItemWire());

	/*public static final RegistryObject<Item> STREET_SIGN = new ItemStreetSign().setRegistryName("street_sign")
			.setUnlocalizedName("street_sign");*/
	public static final RegistryObject<Item> PHOTO = ITEMS.register("photo", () -> new ItemPhoto());
	public static final RegistryObject<Item> ANALOG_CAMERA = ITEMS.register("analog_camera", () -> new ItemAnalogCamera());
	public static final RegistryObject<Item> SHORTS = ITEMS.register("shorts", () -> new ItemClothingLegs(true, false, "shorts"));
	public static final RegistryObject<Item> SKIRT = ITEMS.register("skirt", () -> new ItemClothingLegs(true, false, "skirt"));
	public static final RegistryObject<Item> JEANS = ITEMS.register("jeans", () -> new ItemClothingLegs(true, true, "jeans"));
	public static final RegistryObject<Item> SHORTS_LONG = ITEMS.register("shorts_long", () -> new ItemClothingLegs(true, false, "shorts_longer"));
	public static final RegistryObject<Item> JACKET = ITEMS.register("jacket", () -> new ItemClothingChest(true, false, "jacket"));
	public static final RegistryObject<Item> JUMPER = ITEMS.register("jumper", () -> new ItemClothingChest(true, false, "jumper"));
	public static final RegistryObject<Item> SHIRT = ITEMS.register("shirt", () -> new ItemClothingChest(true, false, "shirt"));
	public static final RegistryObject<Item> SHORT_SLEEVED_SHIRT = ITEMS.register("short_sleeved_shirt", () -> new ItemClothingChest(true, false, "short_sleeved_shirt"));
	public static final RegistryObject<Item> T_SHIRT_0 = ITEMS.register("t_shirt_0", () -> new ItemClothingChest(true, false, "t_shirt_0"));
	public static final RegistryObject<Item> T_SHIRT_1 = ITEMS.register("t_shirt_1", () -> new ItemClothingChest(true, false, "t_shirt_1"));
	public static final RegistryObject<Item> COAT = ITEMS.register("coat", () -> new ItemClothingChest(true, false, "coat"));
	public static final RegistryObject<Item> CAR_BATTERY = ITEMS.register("car_battery", () -> new ItemBattery());
	public static final RegistryObject<Item> SMALL_ENGINE = ITEMS.register("small_engine", () -> new ItemQuality(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_ELECTRICITY)));
	public static final RegistryObject<Item> MINIBIKE_SEAT = ITEMS.register("minibike_seat", () -> new ItemQuality(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_ELECTRICITY)));
	public static final RegistryObject<Item> MINIBIKE_HANDLES = ITEMS.register("minibike_handles", () -> new ItemQuality(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_ELECTRICITY)));
	public static final RegistryObject<Item> MINIBIKE_CHASSIS = ITEMS.register("minibike_chassis", () -> new ItemMinibikeChassis());
	public static final RegistryObject<Item> CAR_CHASSIS = ITEMS.register("car_chassis", () -> new ItemCarChassis());

	public static final RegistryObject<Item> FIBER_CHESTPLATE = ITEMS.register("fiber_chestplate", () -> new ItemArmorBase(ModArmorMaterial.FIBER, EquipmentSlotType.CHEST, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> FIBER_LEGGINGS = ITEMS.register("fiber_leggings", () -> new ItemArmorBase(ModArmorMaterial.FIBER, EquipmentSlotType.LEGS, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> FIBER_BOOTS = ITEMS.register("fiber_boots", () -> new ItemArmorBase(ModArmorMaterial.FIBER, EquipmentSlotType.FEET, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> FIBER_HAT = ITEMS.register("fiber_hat", () -> new ItemArmorBase(ModArmorMaterial.FIBER, EquipmentSlotType.HEAD, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));

	public static final RegistryObject<Item> STEEL_CHESTPLATE = ITEMS.register("steel_chestplate", () -> new ItemArmorBase(ModArmorMaterial.STEEL, EquipmentSlotType.CHEST, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> STEEL_LEGGINGS = ITEMS.register("steel_leggings", () -> new ItemArmorBase(ModArmorMaterial.STEEL, EquipmentSlotType.LEGS, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> STEEL_BOOTS = ITEMS.register("steel_boots", () -> new ItemArmorBase(ModArmorMaterial.STEEL, EquipmentSlotType.FEET, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> STEEL_HELMET = ITEMS.register("steel_helmet", () -> new ItemArmorBase(ModArmorMaterial.STEEL, EquipmentSlotType.HEAD, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));

	public static final RegistryObject<Item> LEATHER_IRON_CHESTPLATE = ITEMS.register("leather_iron_chestplate", () -> new ItemArmorBase(ModArmorMaterial.LEATHER_IRON, EquipmentSlotType.CHEST, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> LEATHER_IRON_LEGGINGS = ITEMS.register("leather_iron_leggings", () -> new ItemArmorBase(ModArmorMaterial.LEATHER_IRON, EquipmentSlotType.LEGS, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> LEATHER_IRON_BOOTS = ITEMS.register("leather_iron_boots", () -> new ItemArmorBase(ModArmorMaterial.LEATHER_IRON, EquipmentSlotType.FEET, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> LEATHER_IRON_HELMET = ITEMS.register("leather_iron_helmet", () -> new ItemArmorBase(ModArmorMaterial.LEATHER_IRON, EquipmentSlotType.HEAD, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	/*
		public static final RegistryObject<Item> IRON_CHESTPLATE = ITEMS.register("iron_chestplate", () -> new ItemArmorBase(ModArmorMaterial.IRON, EquipmentSlotType.CHEST,new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
		public static final RegistryObject<Item> IRON_LEGGINGS = ITEMS.register("iron_leggings", () -> new ItemArmorBase(ModArmorMaterial.IRON, EquipmentSlotType.LEGS,new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
		public static final RegistryObject<Item> IRON_BOOTS = ITEMS.register("iron_boots", () -> new ItemArmorBase(ModArmorMaterial.IRON, EquipmentSlotType.FEET,new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
		public static final RegistryObject<Item> IRON_HELMET = ITEMS.register("iron_helmet", () -> new ItemArmorBase(ModArmorMaterial.IRON, EquipmentSlotType.HEAD,new Item.Properties().tab(ItemGroup.TAB_COMBAT)));

		public static final RegistryObject<Item> LEATHER_CHESTPLATE = ITEMS.register("leather_chestplate", () -> new ItemLeatherArmor(ModArmorMaterial.LEATHER, EquipmentSlotType.CHEST,new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
		public static final RegistryObject<Item> LEATHER_LEGGINGS = ITEMS.register("leather_leggings", () -> new ItemLeatherArmor(ModArmorMaterial.LEATHER, EquipmentSlotType.LEGS,new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
		public static final RegistryObject<Item> LEATHER_BOOTS = ITEMS.register("leather_boots", () -> new ItemLeatherArmor(ModArmorMaterial.LEATHER, EquipmentSlotType.FEET,new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
		public static final RegistryObject<Item> LEATHER_HELMET = ITEMS.register("leather_helmet", () -> new ItemLeatherArmor(ModArmorMaterial.LEATHER, EquipmentSlotType.HEAD,new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	*/
	public static final RegistryObject<Item> MILITARY_CHESTPLATE = ITEMS.register("military_chestplate", () -> new ItemArmorBase(ModArmorMaterial.MILITARY, EquipmentSlotType.CHEST, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> MILITARY_LEGGINGS = ITEMS.register("military_leggings", () -> new ItemArmorBase(ModArmorMaterial.MILITARY, EquipmentSlotType.LEGS, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> MILITARY_BOOTS = ITEMS.register("military_boots", () -> new ItemArmorBase(ModArmorMaterial.MILITARY, EquipmentSlotType.FEET, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> MILITARY_HELMET = ITEMS.register("military_helmet", () -> new ItemArmorBase(ModArmorMaterial.MILITARY, EquipmentSlotType.HEAD, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));

	public static final RegistryObject<Item> COPPER_AXE = ITEMS.register("copper_axe", () -> new ItemQualityAxe(ModItemTier.COPPER_TOOLS, 0, -2.8f, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> BRONZE_AXE = ITEMS.register("bronze_axe", () -> new ItemQualityAxe(ModItemTier.BRONZE_TOOLS, 0, -2.8f, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> SCRAP_AXE = ITEMS.register("scrap_axe", () -> new ItemQualityAxe(ModItemTier.SCRAP_TOOLS, 0, -2.8f, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> IRON_AXE = ITEMS.register("iron_axe", () -> new ItemQualityAxe(ModItemTier.IRON_TOOLS, 0, -2.8f, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> STEEL_AXE = ITEMS.register("steel_axe", () -> new ItemQualityAxe(ModItemTier.STEEL_TOOLS, 0, -2.8f, new Item.Properties().stacksTo(1)));

	public static final RegistryObject<Item> COPPER_SHOVEL = ITEMS.register("copper_shovel", () -> new ItemQualitySpade(ModItemTier.COPPER_TOOLS, 0, -2.8f, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> BRONZE_SHOVEL = ITEMS.register("bronze_shovel", () -> new ItemQualitySpade(ModItemTier.BRONZE_TOOLS, 0, -2.8f, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> SCRAP_SHOVEL = ITEMS.register("scrap_shovel", () -> new ItemQualitySpade(ModItemTier.SCRAP_TOOLS, 0, -2.8f, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> IRON_SHOVEL = ITEMS.register("iron_shovel", () -> new ItemQualitySpade(ModItemTier.IRON_TOOLS, 0, -2.8f, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> STEEL_SHOVEL = ITEMS.register("steel_shovel", () -> new ItemQualitySpade(ModItemTier.STEEL_TOOLS, 0, -2.8f, new Item.Properties().stacksTo(1)));

	public static final RegistryObject<Item> VOLTMETER = ITEMS.register("voltmeter", () -> new ItemVoltmeter());
	public static final RegistryObject<Item> IRON_PIPE = ITEMS.register("iron_pipe", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> CIRCUIT = ITEMS.register("circuit", () -> new ItemCircuit());
	public static final RegistryObject<Item> LINK_TOOL = ITEMS.register("link_tool", () -> new ItemLinkTool());

	public static final RegistryObject<Item> SURVIVAL_GUIDE = ITEMS.register("survival_guide", () -> new ItemGuide(new ResourceLocation(SevenDaysToMine.MODID, "survival"), new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BOOKS)));
	public static final RegistryObject<Item> BOOK_FORGING = ITEMS.register("book_forging", () -> new ItemRecipeBook(new ResourceLocation(SevenDaysToMine.MODID, "forging"), "forging", new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BOOKS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BOOK_AMMO = ITEMS.register("book_ammo", () -> new ItemRecipeBook(new ResourceLocation(SevenDaysToMine.MODID, "ammo"), "ammo", new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BOOKS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BOOK_COMPUTERS = ITEMS.register("book_computers", () -> new ItemRecipeBook(new ResourceLocation(SevenDaysToMine.MODID, "computers"), "computers", new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BOOKS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BOOK_CONCRETE = ITEMS.register("book_concrete", () -> new ItemRecipeBook(new ResourceLocation(SevenDaysToMine.MODID, "concrete"), "concrete", new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BOOKS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BOOK_ELECTRICITY = ITEMS.register("book_electricity", () -> new ItemRecipeBook(new ResourceLocation(SevenDaysToMine.MODID, "electricity"), "electricity", new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BOOKS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BOOK_CHEMISTRY = ITEMS.register("book_chemistry", () -> new ItemRecipeBook(new ResourceLocation(SevenDaysToMine.MODID, "chemistry"), "chemistry", new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BOOKS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BOOK_METALWORKING = ITEMS.register("book_metalworking", () -> new ItemRecipeBook(new ResourceLocation(SevenDaysToMine.MODID, "metalworking"), "metalworking", new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BOOKS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BOOK_PISTOL = ITEMS.register("book_pistol", () -> new ItemRecipeBook(new ResourceLocation(SevenDaysToMine.MODID, "pistol"), "pistol", new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BOOKS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BOOK_MINIBIKE = ITEMS.register("book_minibike", () -> new ItemRecipeBook(new ResourceLocation(SevenDaysToMine.MODID, "minibike"), "minibike", new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BOOKS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SHOTGUN_SCHEMATICS = ITEMS.register("book_shotgun", () -> new ItemBlueprint(new ResourceLocation(SevenDaysToMine.MODID, "shotgun"), "shotgun", new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BOOKS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SNIPER_SCHEMATICS = ITEMS.register("book_sniper", () -> new ItemBlueprint(new ResourceLocation(SevenDaysToMine.MODID, "sniper"), "sniper", new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BOOKS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> MAGNUM_SCHEMATICS = ITEMS.register("book_magnum", () -> new ItemBlueprint(new ResourceLocation(SevenDaysToMine.MODID, "magnum"), "magnum", new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BOOKS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> MP5_SCHEMATICS = ITEMS.register("book_mp5", () -> new ItemBlueprint(new ResourceLocation(SevenDaysToMine.MODID, "mp5"), "mp5", new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BOOKS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> HUNTING_RIFLE_SCHEMATICS = ITEMS.register("book_hunting_rifle", () -> new ItemBlueprint(new ResourceLocation(SevenDaysToMine.MODID, "hunting_rifle"), "hunting_rifle", new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BOOKS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> AUGER_SCHEMATICS = ITEMS.register("book_auger", () -> new ItemBlueprint(new ResourceLocation(SevenDaysToMine.MODID, "auger"), "auger", new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BOOKS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ROCKET_SCHEMATICS = ITEMS.register("book_rocket", () -> new ItemBlueprint(new ResourceLocation(SevenDaysToMine.MODID, "rocket"), "rocket", new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BOOKS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BULLET_TIP = ITEMS.register("bullet_tip", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> BULLET_CASING = ITEMS.register("bullet_casing", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));

	public static final RegistryObject<Item> MOLDY_BREAD = ITEMS.register("moldy_bread", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_FOOD).food(ModFood.MOLDY_BREAD).stacksTo(1)));
	public static final RegistryObject<Item> SLEDGEHAMMER = ITEMS.register("sledgehammer", () -> new ItemQualitySword(ModItemTier.SLEDGEHAMMER, 0, -3.6f, new Item.Properties().stacksTo(1), EnumLength.LONG));


	public static final RegistryObject<Item> PISTOL_SLIDE = ITEMS.register("pistol_slide", () -> new ItemGunPart());
	public static final RegistryObject<Item> PISTOL_TRIGGER = ITEMS.register("pistol_trigger", () -> new ItemGunPart());
	public static final RegistryObject<Item> PISTOL_GRIP = ITEMS.register("pistol_grip", () -> new ItemGunPart());
	public static final RegistryObject<Item> SNIPER_RIFLE_BARREL = ITEMS.register("sniper_rifle_barrel", () -> new ItemGunPart());
	public static final RegistryObject<Item> SNIPER_RIFLE_TRIGGER = ITEMS.register("sniper_rifle_trigger", () -> new ItemGunPart());
	public static final RegistryObject<Item> SNIPER_RIFLE_SCOPE = ITEMS.register("sniper_rifle_scope", () -> new ItemGunPart());
	public static final RegistryObject<Item> SNIPER_RIFLE_STOCK = ITEMS.register("sniper_rifle_stock", () -> new ItemGunPart());

	public static final RegistryObject<Item> SHOTGUN_RECEIVER = ITEMS.register("shotgun_receiver", () -> new ItemGunPart());
	public static final RegistryObject<Item> SHOTGUN_STOCK = ITEMS.register("shotgun_stock", () -> new ItemGunPart());
	public static final RegistryObject<Item> SHOTGUN_STOCK_SHORT = ITEMS.register("shotgun_stock_short", () -> new ItemGunPart());
	public static final RegistryObject<Item> SHOTGUN_PARTS = ITEMS.register("shotgun_parts", () -> new ItemGunPart());
	public static final RegistryObject<Item> SHOTGUN_BARREL = ITEMS.register("shotgun_barrel", () -> new ItemGunPart());
	public static final RegistryObject<Item> SHOTGUN_BARREL_SHORT = ITEMS.register("shotgun_barrel_short", () -> new ItemGunPart());

	public static final RegistryObject<Item> MAGNUM_FRAME = ITEMS.register("magnum_frame", () -> new ItemGunPart());
	public static final RegistryObject<Item> MAGNUM_CYLINDER = ITEMS.register("magnum_cylinder", () -> new ItemGunPart());
	public static final RegistryObject<Item> MAGNUM_GRIP = ITEMS.register("magnum_grip", () -> new ItemGunPart());

	public static final RegistryObject<Item> MP5_BARREL = ITEMS.register("mp5_barrel", () -> new ItemGunPart());
	public static final RegistryObject<Item> MP5_TRIGGER = ITEMS.register("mp5_trigger", () -> new ItemGunPart());
	public static final RegistryObject<Item> MP5_STOCK = ITEMS.register("mp5_stock", () -> new ItemGunPart());

	public static final RegistryObject<Item> HUNTING_RIFLE_BARREL = ITEMS.register("hunting_rifle_barrel", () -> new ItemGunPart());
	public static final RegistryObject<Item> HUNTING_RIFLE_STOCK = ITEMS.register("hunting_rifle_stock", () -> new ItemGunPart());
	public static final RegistryObject<Item> RIFLE_PARTS = ITEMS.register("rifle_parts", () -> new ItemGunPart());
	public static final RegistryObject<Item> HUNTING_RIFLE_BOLT = ITEMS.register("hunting_rifle_bolt", () -> new ItemGunPart());

	public static final RegistryObject<Item> SCRAP_CHESTPLATE = ITEMS.register("scrap_chestplate", () -> new ItemArmorBase(ModArmorMaterial.SCRAP, EquipmentSlotType.CHEST, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> SCRAP_LEGGINGS = ITEMS.register("scrap_leggings", () -> new ItemArmorBase(ModArmorMaterial.SCRAP, EquipmentSlotType.LEGS, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> SCRAP_BOOTS = ITEMS.register("scrap_boots", () -> new ItemArmorBase(ModArmorMaterial.SCRAP, EquipmentSlotType.FEET, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> SCRAP_HELMET = ITEMS.register("scrap_helmet", () -> new ItemArmorBase(ModArmorMaterial.SCRAP, EquipmentSlotType.HEAD, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));

	public static final RegistryObject<Item> SNIPER_RIFLE = ITEMS.register("sniper_rifle", () -> new ItemSniperRifle());
	public static final RegistryObject<Item> SHOTGUN = ITEMS.register("shotgun", () -> new ItemShotgun());
	public static final RegistryObject<Item> SHOTGUN_SAWED_OFF = ITEMS.register("shotgun_short", () -> new ItemShotgunShort());

	public static final RegistryObject<Item> BELLOWS = ITEMS.register("bellows", () -> new Item(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_FORGING)));
	public static final RegistryObject<Item> MAGNET = ITEMS.register("magnet", () -> new Item(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_ELECTRICITY)));
	public static final RegistryObject<Item> PHOTO_CELL = ITEMS.register("photo_cell", () -> new Item(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_ELECTRICITY)));

	public static final RegistryObject<Item> POTASSIUM = ITEMS.register("potassium", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> GAS_CANISTER = ITEMS.register("gas_canister", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));

	public static final RegistryObject<Item> AUGER_BLADE = ITEMS.register("auger_blade", () -> new ItemQuality(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_FORGING)));
	public static final RegistryObject<Item> NIGHT_VISION_DEVICE = ITEMS.register("night_vision_device", () -> new ItemNightVisionDevice(ModArmorMaterial.SCRAP, EquipmentSlotType.HEAD, new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> CHRISTMAS_HAT = ITEMS.register("christmas_hat", () -> new ItemChristmasHat(ModArmorMaterial.FIBER, EquipmentSlotType.HEAD, new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_CLOTHING)));
	public static final RegistryObject<Item> BERET = ITEMS.register("beret", () -> new ItemBeret(ModArmorMaterial.FIBER, EquipmentSlotType.HEAD, new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_CLOTHING)));

	public static final RegistryObject<Item> STETHOSCOPE = ITEMS.register("stethoscope", () -> new ItemStethoscope());
	public static final RegistryObject<Item> BLOODMOON = ITEMS.register("bloodmoon", () -> new Item(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> COPPER_HOE = ITEMS.register("copper_hoe", () -> new ItemQualityHoe(ModItemTier.COPPER_TOOLS, 0, -2.8f, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> BRONZE_HOE = ITEMS.register("bronze_hoe", () -> new ItemQualityHoe(ModItemTier.BRONZE_TOOLS, 0, -2.8f, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> SCRAP_HOE = ITEMS.register("scrap_hoe", () -> new ItemQualityHoe(ModItemTier.SCRAP_TOOLS, 0, -2.8f, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> IRON_HOE = ITEMS.register("iron_hoe", () -> new ItemQualityHoe(ModItemTier.IRON_TOOLS, 0, -2.8f, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> STEEL_HOE = ITEMS.register("steel_hoe", () -> new ItemQualityHoe(ModItemTier.STEEL_TOOLS, 0, -2.8f, new Item.Properties().stacksTo(1)));


	public static final RegistryObject<Item> AUGER_HANDLES = ITEMS.register("auger_handles", () -> new ItemQuality(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_FORGING)));

	public static final RegistryObject<Item> EMPTY_JAR_MOLD = ITEMS.register("empty_jar_mold", () -> new ItemMold());
	public static final RegistryObject<Item> HUNTING_RIFLE_BARREL_MOLD = ITEMS.register("hunting_rifle_barrel_mold", () -> new ItemMold());
	public static final RegistryObject<Item> HUNTING_RIFLE_BOLT_MOLD = ITEMS.register("hunting_rifle_bolt_mold", () -> new ItemMold());
	public static final RegistryObject<Item> MP5_BARREL_MOLD = ITEMS.register("mp5_barrel_mold", () -> new ItemMold());
	public static final RegistryObject<Item> MP5_STOCK_MOLD = ITEMS.register("mp5_stock_mold", () -> new ItemMold());
	public static final RegistryObject<Item> MP5_TRIGGER_MOLD = ITEMS.register("mp5_trigger_mold", () -> new ItemMold());
	public static final RegistryObject<Item> PISTOL_BARREL_MOLD = ITEMS.register("pistol_barrel_mold", () -> new ItemMold());
	public static final RegistryObject<Item> PISTOL_TRIGGER_MOLD = ITEMS.register("pistol_trigger_mold", () -> new ItemMold());
	public static final RegistryObject<Item> SNIPER_RIFLE_STOCK_MOLD = ITEMS.register("sniper_rifle_stock_mold", () -> new ItemMold());
	public static final RegistryObject<Item> SNIPER_RIFLE_TRIGGER_MOLD = ITEMS.register("sniper_rifle_trigger_mold", () -> new ItemMold());
	public static final RegistryObject<Item> SHOTGUN_BARREL_MOLD = ITEMS.register("shotgun_barrel_mold", () -> new ItemMold());
	public static final RegistryObject<Item> SHOTGUN_RECEIVER_MOLD = ITEMS.register("shotgun_receiver_mold", () -> new ItemMold());
	public static final RegistryObject<Item> SHOTGUN_SHORT_BARREL_MOLD = ITEMS.register("shotgun_short_barrel_mold", () -> new ItemMold());

	public static final RegistryObject<Item> SALT = ITEMS.register("salt", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> CHLORINE_TANK = ITEMS.register("chlorine_tank", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> NATRIUM_TANK = ITEMS.register("natrium_tank", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));


	public static final RegistryObject<Item> CHLORINE_GRENADE = ITEMS.register("chlorine_grenade", () -> new ItemChlorineGrenade());
	public static final RegistryObject<Item> FRAGMENTATION_GRENADE = ITEMS.register("fragmentation_grenade", () -> new ItemFragmentationGrenade());
	public static final RegistryObject<Item> MOLOTOV = ITEMS.register("molotov", () -> new ItemMolotov());

	public static final RegistryObject<Item> RIOT_SHIELD = ITEMS.register("riot_shield", () -> new ItemRiotShield());
	public static final RegistryObject<Item> CRUDE_BOW = ITEMS.register("crude_bow", () -> new ItemQualityBow(new Item.Properties().durability(384).tab(ItemGroup.TAB_COMBAT), 1.45, 1f));
	public static final RegistryObject<Item> COMPOUND_BOW = ITEMS.register("compound_bow", () -> new ItemQualityBow(new Item.Properties().durability(384).tab(ItemGroup.TAB_COMBAT), 2.4, 1.33f));

	public static final RegistryObject<Item> SAND_DUST = ITEMS.register("sand_dust", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> VOMIT = ITEMS.register("vomit", () -> new Item(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> FLARE = ITEMS.register("flare", () -> new ItemFlare());


	public static final RegistryObject<Item> SODA = ITEMS.register("soda", () -> new ItemCoffeeDrink(new Item.Properties().stacksTo(16).food(ModFood.SODA).craftRemainder(EMPTY_CAN.get()), 200, 200));

	public static final RegistryObject<Item> REANIMATED_CORPSE_SPAWN_EGG = ITEMS.register("reanimated_corpse_spawn_egg", () -> new SpawnEggItem(ModEntities.REANIMATED_CORPSE_RAW, 0x403A34, 0x1D2637, (new Item.Properties()).tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<Item> PLAGUED_NURSE_SPAWN_EGG = ITEMS.register("plagued_nurse_spawn_egg", () -> new SpawnEggItem(ModEntities.PLAGUED_NURSE_RAW, 0x2C5B50, 0x1D2637, (new Item.Properties()).tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<Item> FROZEN_LUMBERJACK_SPAWN_EGG = ITEMS.register("frozen_lumberjack_spawn_egg", () -> new SpawnEggItem(ModEntities.FROZEN_LUMBERJACK_RAW, 0x151F36, 0x760504, (new Item.Properties()).tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<Item> FRIGID_HUNTER_SPAWN_EGG = ITEMS.register("frigid_hunter_spawn_egg", () -> new SpawnEggItem(ModEntities.FRIGID_HUNTER_RAW, 0x607A88, 0x593616, (new Item.Properties()).tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<Item> FROSTBITTEN_WORKER_SPAWN_EGG = ITEMS.register("frostbitten_worker_spawn_egg", () -> new SpawnEggItem(ModEntities.FROSTBITTEN_WORKER_RAW, 0x5B1C1C, 0x242A3C, (new Item.Properties()).tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<Item> MINER_ZOMBIE_SPAWN_EGG = ITEMS.register("miner_zombie_spawn_egg", () -> new SpawnEggItem(ModEntities.MINER_ZOMBIE_RAW, 0x1A1325, 0x211A17, (new Item.Properties()).tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<Item> SPIDER_ZOMBIE_SPAWN_EGG = ITEMS.register("spider_zombie_spawn_egg", () -> new SpawnEggItem(ModEntities.SPIDER_ZOMBIE_RAW, 0x2E2E34, 0x474239, (new Item.Properties()).tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<Item> CRAWLER_ZOMBIE_SPAWN_EGG = ITEMS.register("crawler_zombie_spawn_egg", () -> new SpawnEggItem(ModEntities.CRAWLER_ZOMBIE_RAW, 0x4D463C, 0x375268, (new Item.Properties()).tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<Item> BURNT_ZOMBIE_SPAWN_EGG = ITEMS.register("burnt_zombie_spawn_egg", () -> new SpawnEggItem(ModEntities.BURNT_ZOMBIE_RAW, 0x000000, 0xffc400, (new Item.Properties()).tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<Item> BLOATED_ZOMBIE_SPAWN_EGG = ITEMS.register("bloated_zombie_spawn_egg", () -> new SpawnEggItem(ModEntities.BLOATED_ZOMBIE_RAW, 0x4F4134, 0x211B17, (new Item.Properties()).tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<Item> SOUL_BURNT_ZOMBIE_SPAWN_EGG = ITEMS.register("soul_burnt_zombie_spawn_egg", () -> new SpawnEggItem(ModEntities.SOUL_BURNT_ZOMBIE_RAW, 0x000000, 0x00F4FF, (new Item.Properties()).tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<Item> ZOMBIE_SOLDIER_SPAWN_EGG = ITEMS.register("zombie_soldier_spawn_egg", () -> new SpawnEggItem(ModEntities.SOLDIER_ZOMBIE_RAW, 0x313B63, 0x544A38, (new Item.Properties()).tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<Item> INFECTED_SURVIVOR_SPAWN_EGG = ITEMS.register("infected_survivor_spawn_egg", () -> new SpawnEggItem(ModEntities.INFECTED_SURVIVOR_RAW, 0x6B5D65, 0x6C3B38, (new Item.Properties()).tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<Item> FERAL_ZOMBIE_SPAWN_EGG = ITEMS.register("feral_zombie_spawn_egg", () -> new SpawnEggItem(ModEntities.FERAL_ZOMBIE_RAW, 0x4B5435, 0x453A2D, (new Item.Properties()).tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<Item> ZOMBIE_POLICEMAN_SPAWN_EGG = ITEMS.register("zombie_policeman_spawn_egg", () -> new SpawnEggItem(ModEntities.ZOMBIE_POLICEMAN_RAW, 0x342A44, 0x4F593B, (new Item.Properties()).tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<Item> ZOMBIE_WOLF_SPAWN_EGG = ITEMS.register("zombie_wolf_spawn_egg", () -> new SpawnEggItem(ModEntities.ZOMBIE_WOLF_RAW, 0x827E7E, 0x851919, (new Item.Properties()).tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<Item> ZOMBIE_PIG_SPAWN_EGG = ITEMS.register("zombie_pig_spawn_egg", () -> new SpawnEggItem(ModEntities.ZOMBIE_PIG_RAW, 0xD39294, 0x852122, (new Item.Properties()).tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<Item> TWISTED_ZOMBIE_SPAWN_EGG = ITEMS.register("twisted_zombie_spawn_egg", () -> new SpawnEggItem(ModEntities.TWISTED_ZOMBIE_RAW, 0x11ccbb, 0x555555, (new Item.Properties()).tab(ItemGroup.TAB_MISC)));


	public static final RegistryObject<Item> SLEEPING_BAG_BLACK = ITEMS.register("sleeping_bag_black",
			() -> new BedItem(ModBlocks.SLEEPING_BAG_BLACK.get(), new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BUILDING)));
	public static final RegistryObject<Item> SLEEPING_BAG_WHITE = ITEMS.register("sleeping_bag_white",
			() -> new BedItem(ModBlocks.SLEEPING_BAG_WHITE.get(), new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BUILDING)));
	public static final RegistryObject<Item> SLEEPING_BAG_RED = ITEMS.register("sleeping_bag_red",
			() -> new BedItem(ModBlocks.SLEEPING_BAG_RED.get(), new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BUILDING)));
	public static final RegistryObject<Item> SLEEPING_BAG_BLUE = ITEMS.register("sleeping_bag_blue",
			() -> new BedItem(ModBlocks.SLEEPING_BAG_BLUE.get(), new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BUILDING)));
	public static final RegistryObject<Item> SLEEPING_BAG_GREEN = ITEMS.register("sleeping_bag_green",
			() -> new BedItem(ModBlocks.SLEEPING_BAG_GREEN.get(), new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BUILDING)));
	public static final RegistryObject<Item> SLEEPING_BAG_YELLOW = ITEMS.register("sleeping_bag_yellow",
			() -> new BedItem(ModBlocks.SLEEPING_BAG_YELLOW.get(), new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BUILDING)));
	public static final RegistryObject<Item> SLEEPING_BAG_ORANGE = ITEMS.register("sleeping_bag_orange",
			() -> new BedItem(ModBlocks.SLEEPING_BAG_ORANGE.get(), new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BUILDING)));
	public static final RegistryObject<Item> SLEEPING_BAG_CYAN = ITEMS.register("sleeping_bag_cyan",
			() -> new BedItem(ModBlocks.SLEEPING_BAG_CYAN.get(), new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BUILDING)));
	public static final RegistryObject<Item> SLEEPING_BAG_LIME = ITEMS.register("sleeping_bag_lime",
			() -> new BedItem(ModBlocks.SLEEPING_BAG_LIME.get(), new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BUILDING)));
	public static final RegistryObject<Item> SLEEPING_BAG_GRAY = ITEMS.register("sleeping_bag_gray",
			() -> new BedItem(ModBlocks.SLEEPING_BAG_GRAY.get(), new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BUILDING)));
	public static final RegistryObject<Item> SLEEPING_BAG_SILVER = ITEMS.register("sleeping_bag_silver",
			() -> new BedItem(ModBlocks.SLEEPING_BAG_SILVER.get(), new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BUILDING)));
	public static final RegistryObject<Item> SLEEPING_BAG_LIGHT_BLUE = ITEMS.register("sleeping_bag_light_blue",
			() -> new BedItem(ModBlocks.SLEEPING_BAG_LIGHT_BLUE.get(), new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BUILDING)));
	public static final RegistryObject<Item> SLEEPING_BAG_PINK = ITEMS.register("sleeping_bag_pink",
			() -> new BedItem(ModBlocks.SLEEPING_BAG_PINK.get(), new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BUILDING)));
	public static final RegistryObject<Item> SLEEPING_BAG_PURPLE = ITEMS.register("sleeping_bag_purple",
			() -> new BedItem(ModBlocks.SLEEPING_BAG_PURPLE.get(), new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BUILDING)));
	public static final RegistryObject<Item> SLEEPING_BAG_MAGENTA = ITEMS.register("sleeping_bag_magenta",
			() -> new BedItem(ModBlocks.SLEEPING_BAG_MAGENTA.get(), new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BUILDING)));
	public static final RegistryObject<Item> SLEEPING_BAG_BROWN = ITEMS.register("sleeping_bag_brown",
			() -> new BedItem(ModBlocks.SLEEPING_BAG_BROWN.get(), new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BUILDING)));

	public static final RegistryObject<Item> NOTE = ITEMS.register("note", () -> new ItemNote(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> STREET_SIGN = ITEMS.register("street_sign", () -> new SignItem((new Item.Properties()).stacksTo(16).tab(ItemGroup.TAB_DECORATIONS), ModBlocks.STREET_SIGN.get(), ModBlocks.STREET_SIGN_WALL.get()));
	public static final RegistryObject<Item> BIG_SIGN = ITEMS.register("big_sign", () -> new SignItem((new Item.Properties()).stacksTo(16).tab(ItemGroup.TAB_DECORATIONS), ModBlocks.BIG_SIGN_MASTER.get(), ModBlocks.BIG_SIGN_MASTER.get()));

	public static final RegistryObject<Item> TORCH = ITEMS.register("torch",
			() -> new WallOrFloorItem(ModBlocks.TORCH_LIT.get(), ModBlocks.TORCH_LIT_WALL.get(), (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));

	public static final RegistryObject<Item> TORCH_UNLIT = ITEMS.register("torch_unlit",
			() -> new WallOrFloorItem(ModBlocks.TORCH_UNLIT.get(), ModBlocks.TORCH_UNLIT_WALL.get(), (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));

	public static final RegistryObject<Item> FRIDGE = ITEMS.register("fridge", () -> new TallBlockItem(ModBlocks.FRIDGE.get(), new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BUILDING)));
	public static final RegistryObject<Item> LOCKED_DOOR = ITEMS.register("locked_door", () -> new TallBlockItem(ModBlocks.LOCKED_DOOR.get(), new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_BUILDING)));

	public static final RegistryObject<Item> GASOLINE_BUCKET = ITEMS.register("gasoline_bucket", () -> new BucketItem(ModFluids.GASOLINE, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1).tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<Item> MERCURY_BUCKET = ITEMS.register("mercury_bucket", () -> new BucketItem(ModFluids.MERCURY, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1).tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<Item> GEAR = ITEMS.register("gear", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));

	public static final RegistryObject<Item> REALITY_WAND = ITEMS.register("reality_wand", () -> new ItemRealityWand());
	public static final RegistryObject<Item> PLANK_FUNGI = ITEMS.register("plank_fungi", () -> new Item(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS)));
	public static final RegistryObject<Item> IRON_PIPE_MOLD = ITEMS.register("iron_pipe_mold", () -> new ItemMold());
}