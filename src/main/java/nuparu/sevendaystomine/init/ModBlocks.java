package nuparu.sevendaystomine.init;

import java.util.function.ToIntFunction;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.potion.Effects;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.*;
import nuparu.sevendaystomine.item.EnumMaterial;
import net.minecraftforge.common.ToolType;

import net.minecraft.item.DyeColor;
import nuparu.sevendaystomine.proxy.StartupClient;

public class ModBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			SevenDaysToMine.MODID);

	public static final RegistryObject<BlockPlanksFrame> OAK_FRAME = BLOCKS.register("oak_planks_frame",
			() -> new BlockPlanksFrame(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(0.8F, 2.0F).sound(SoundType.WOOD), Blocks.OAK_PLANKS));
	public static final RegistryObject<BlockPlanksFrame> BIRCH_FRAME = BLOCKS.register("birch_planks_frame",
			() -> new BlockPlanksFrame(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(0.8F, 2.0F).sound(SoundType.WOOD), Blocks.BIRCH_PLANKS));
	public static final RegistryObject<BlockPlanksFrame> SPRUCE_FRAME = BLOCKS.register("spruce_planks_frame",
			() -> new BlockPlanksFrame(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(0.8F, 2.0F).sound(SoundType.WOOD), Blocks.SPRUCE_PLANKS));
	public static final RegistryObject<BlockPlanksFrame> JUNGLE_FRAME = BLOCKS.register("jungle_planks_frame",
			() -> new BlockPlanksFrame(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(0.8F, 2.0F).sound(SoundType.WOOD), Blocks.JUNGLE_PLANKS));
	public static final RegistryObject<BlockPlanksFrame> ACACIA_FRAME = BLOCKS.register("acacia_planks_frame",
			() -> new BlockPlanksFrame(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(0.8F, 2.0F).sound(SoundType.WOOD), Blocks.ACACIA_PLANKS));
	public static final RegistryObject<BlockPlanksFrame> DARK_OAK_FRAME = BLOCKS.register("dark_oak_planks_frame",
			() -> new BlockPlanksFrame(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(0.8F, 2.0F).sound(SoundType.WOOD), Blocks.DARK_OAK_PLANKS));
	public static final RegistryObject<BlockPlanksFrame> CRIMSON_FRAME = BLOCKS.register("crimson_planks_frame",
			() -> new BlockPlanksFrame(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(0.8F, 2.0F).sound(SoundType.WOOD), Blocks.CRIMSON_PLANKS));
	public static final RegistryObject<BlockPlanksFrame> WARPED_FRAME = BLOCKS.register("warped_planks_frame",
			() -> new BlockPlanksFrame(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(0.8F, 2.0F).sound(SoundType.WOOD), Blocks.WARPED_PLANKS));

	public static final RegistryObject<BlockPlanksReinforcedIron> OAK_PLANKS_REINFORCED_IRON = BLOCKS.register("oak_planks_reinforced_iron",
			() -> new BlockPlanksReinforcedIron(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(3.1F, 6.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockPlanksReinforcedIron> BIRCH_PLANKS_REINFORCED_IRON = BLOCKS
			.register("birch_planks_reinforced_iron", () -> new BlockPlanksReinforcedIron(AbstractBlock.Properties
					.of(Material.WOOD, MaterialColor.WOOD).strength(3.1F, 6.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockPlanksReinforcedIron> SPRUCE_PLANKS_REINFORCED_IRON = BLOCKS
			.register("spruce_planks_reinforced_iron", () -> new BlockPlanksReinforcedIron(AbstractBlock.Properties
					.of(Material.WOOD, MaterialColor.WOOD).strength(3.1F, 6.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockPlanksReinforcedIron> JUNGLE_PLANKS_REINFORCED_IRON = BLOCKS
			.register("jungle_planks_reinforced_iron", () -> new BlockPlanksReinforcedIron(AbstractBlock.Properties
					.of(Material.WOOD, MaterialColor.WOOD).strength(3.1F, 6.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockPlanksReinforcedIron> ACACIA_PLANKS_REINFORCED_IRON = BLOCKS
			.register("acacia_planks_reinforced_iron", () -> new BlockPlanksReinforcedIron(AbstractBlock.Properties
					.of(Material.WOOD, MaterialColor.WOOD).strength(3.1F, 6.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockPlanksReinforcedIron> DARK_OAK_PLANKS_REINFORCED_IRON = BLOCKS
			.register("dark_oak_planks_reinforced_iron", () -> new BlockPlanksReinforcedIron(AbstractBlock.Properties
					.of(Material.WOOD, MaterialColor.WOOD).strength(3.1F, 6.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockPlanksReinforcedIron> CRIMSON_PLANKS_REINFORCED_IRON = BLOCKS
			.register("crimson_planks_reinforced_iron", () -> new BlockPlanksReinforcedIron(AbstractBlock.Properties
					.of(Material.WOOD, MaterialColor.WOOD).strength(3.1F, 6.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockPlanksReinforcedIron> WARPED_PLANKS_REINFORCED_IRON = BLOCKS
			.register("warped_planks_reinforced_iron", () -> new BlockPlanksReinforcedIron(AbstractBlock.Properties
					.of(Material.WOOD, MaterialColor.WOOD).strength(3.1F, 6.0F).sound(SoundType.WOOD)));

	public static final RegistryObject<BlockPlanksReinforced> OAK_PLANKS_REINFORCED = BLOCKS.register("oak_planks_reinforced",
			() -> new BlockPlanksReinforced(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(2.5F, 4.0F).sound(SoundType.WOOD), OAK_PLANKS_REINFORCED_IRON.get()));
	public static final RegistryObject<BlockPlanksReinforced> BIRCH_PLANKS_REINFORCED = BLOCKS.register("birch_planks_reinforced",
			() -> new BlockPlanksReinforced(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(2.5F, 4.0F).sound(SoundType.WOOD), BIRCH_PLANKS_REINFORCED_IRON.get()));
	public static final RegistryObject<BlockPlanksReinforced> SPRUCE_PLANKS_REINFORCED = BLOCKS.register("spruce_planks_reinforced",
			() -> new BlockPlanksReinforced(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(2.5F, 4.0F).sound(SoundType.WOOD), SPRUCE_PLANKS_REINFORCED_IRON.get()));
	public static final RegistryObject<BlockPlanksReinforced> JUNGLE_PLANKS_REINFORCED = BLOCKS.register("jungle_planks_reinforced",
			() -> new BlockPlanksReinforced(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(2.5F, 4.0F).sound(SoundType.WOOD), JUNGLE_PLANKS_REINFORCED_IRON.get()));
	public static final RegistryObject<BlockPlanksReinforced> ACACIA_PLANKS_REINFORCED = BLOCKS.register("acacia_planks_reinforced",
			() -> new BlockPlanksReinforced(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(2.5F, 4.0F).sound(SoundType.WOOD), ACACIA_PLANKS_REINFORCED_IRON.get()));
	public static final RegistryObject<BlockPlanksReinforced> DARK_OAK_PLANKS_REINFORCED = BLOCKS.register("dark_oak_planks_reinforced",
			() -> new BlockPlanksReinforced(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(2.5F, 4.0F).sound(SoundType.WOOD), DARK_OAK_PLANKS_REINFORCED_IRON.get()));
	public static final RegistryObject<BlockPlanksReinforced> CRIMSON_PLANKS_REINFORCED = BLOCKS.register("crimson_planks_reinforced",
			() -> new BlockPlanksReinforced(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(2.5F, 4.0F).sound(SoundType.WOOD), CRIMSON_PLANKS_REINFORCED_IRON.get()));
	public static final RegistryObject<BlockPlanksReinforced> WARPED_PLANKS_REINFORCED = BLOCKS.register("warped_planks_reinforced",
			() -> new BlockPlanksReinforced(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(2.5F, 4.0F).sound(SoundType.WOOD), WARPED_PLANKS_REINFORCED_IRON.get()));

	public static final RegistryObject<Block> SMALL_ROCK_STONE = BLOCKS.register("small_rock_stone",
			() -> new BlockSmallRock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE)
					.strength(0.5F, 0.25F).instabreak().sound(SoundType.STONE)));
	public static final RegistryObject<Block> SMALL_ROCK_ANDESITE = BLOCKS.register("small_rock_andesite",
			() -> new BlockSmallRock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE)
					.strength(0.5F, 0.25F).instabreak().sound(SoundType.STONE)));
	public static final RegistryObject<Block> SMALL_ROCK_DIORITE = BLOCKS.register("small_rock_diorite",
			() -> new BlockSmallRock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE)
					.strength(0.5F, 0.25F).instabreak().sound(SoundType.STONE)));
	public static final RegistryObject<Block> SMALL_ROCK_GRANITE = BLOCKS.register("small_rock_granite",
			() -> new BlockSmallRock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE)
					.strength(0.5F, 0.25F).instabreak().sound(SoundType.STONE)));

	public static final RegistryObject<Block> STICK = BLOCKS.register("stick_block",
			() -> new BlockStick(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(0.5F, 0.25F)
					.instabreak().sound(SoundType.WOOD)));
	public static final RegistryObject<Block> BLUEBERRY_PLANT = BLOCKS.register("blueberry_plant",
			() -> new BlockBlueberry());
	public static final RegistryObject<Block> BANEBERRY_PLANT = BLOCKS.register("baneberry_plant",
			() -> new BlockBaneberry());
	public static final RegistryObject<Block> CORN_PLANT = BLOCKS.register("corn_plant", () -> new BlockCornPlant());
	public static final RegistryObject<Block> COFFEE_PLANT = BLOCKS.register("coffee_plant",
			() -> new BlockCoffeePlant());
	public static final RegistryObject<Block> GOLDENROD = BLOCKS.register("goldenrod",
			() -> new FlowerBlock(Effects.SATURATION, 7,
					AbstractBlock.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS)));
	public static final RegistryObject<Block> BUSH = BLOCKS.register("bush", () -> new BlockBush(
			AbstractBlock.Properties.of(Material.PLANT).noCollission().strength(0.75f, 0.75f).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> FORGE = BLOCKS.register("forge",
			() -> new BlockForge(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops()
					.strength(3.5F).lightLevel(litBlockEmission(13))));

	public static final RegistryObject<Block> COOKING_POT = BLOCKS.register("cooking_pot",
			() -> new BlockCookware(AbstractBlock.Properties.of(Material.METAL).noOcclusion(),
					Block.box(0.25D * 16, 0.0D, 0.25D * 16, 0.75D * 16, 0.5D * 16, 0.75D * 16), EnumMaterial.IRON, 12));
	public static final RegistryObject<Block> BEAKER = BLOCKS.register("beaker",
			() -> new BlockCookware(AbstractBlock.Properties.of(Material.GLASS).sound(SoundType.GLASS).noOcclusion(),
					Block.box(0.375D *16, 0.0D, 0.375D *16, 0.625D * 16, 0.5D * 16, 0.625D * 16), EnumMaterial.IRON, 12));

	public static final RegistryObject<Block> ORE_COPPER = BLOCKS.register("copper_ore", () -> new BlockOre(
			AbstractBlock.Properties.of(Material.STONE).strength(2, 5).harvestTool(ToolType.PICKAXE).harvestLevel(0),
			EnumMaterial.COPPER));

	public static final RegistryObject<Block> ORE_TIN = BLOCKS.register("tin_ore", () -> new BlockOre(
			AbstractBlock.Properties.of(Material.STONE).strength(1.8f, 5).harvestTool(ToolType.PICKAXE).harvestLevel(1),
			EnumMaterial.TIN));

	public static final RegistryObject<Block> ORE_ZINC = BLOCKS.register("zinc_ore", () -> new BlockOre(
			AbstractBlock.Properties.of(Material.STONE).strength(2.2f, 5).harvestTool(ToolType.PICKAXE).harvestLevel(1),
			EnumMaterial.ZINC));

	public static final RegistryObject<Block> ORE_LEAD = BLOCKS.register("lead_ore", () -> new BlockOre(
			AbstractBlock.Properties.of(Material.STONE).strength(3f, 5).harvestTool(ToolType.PICKAXE).harvestLevel(2),
			EnumMaterial.LEAD));

	public static final RegistryObject<Block> ORE_POTASSIUM = BLOCKS.register("potassium_ore",
			() -> new BlockPotassiumOre());
	public static final RegistryObject<Block> ORE_CINNABAR = BLOCKS.register("cinnabar_ore", () -> new BlockOre(
			AbstractBlock.Properties.of(Material.STONE).strength(2.5f, 5).harvestTool(ToolType.PICKAXE).harvestLevel(2),
			EnumMaterial.MERCURY));

	public static final RegistryObject<BlockRebarFrame> REBAR_FRAME = BLOCKS.register("rebar_frame", () -> new BlockRebarFrame());
	public static final RegistryObject<BlockRebarFrameWood> REBAR_FRAME_WOOD = BLOCKS.register("rebar_frame_wood",
			() -> new BlockRebarFrameWood());
	public static final RegistryObject<Block> REINFORCED_CONCRETE_WET = BLOCKS.register("reinforced_concrete_wet",
			() -> new BlockReinforcedConcreteWet());
	public static final RegistryObject<Block> REINFORCED_CONCRETE = BLOCKS.register("reinforced_concrete",
			() -> new BlockReinforcedConcrete());
	public static final RegistryObject<Block> ASPHALT = BLOCKS.register("asphalt", () -> new BlockAsphalt());

	public static final RegistryObject<Block> CODE_SAFE = BLOCKS.register("code_safe",
			() -> new BlockCodeSafe(AbstractBlock.Properties.of(Material.METAL).noOcclusion()));
	public static final RegistryObject<Block> CARDBOARD_BOX = BLOCKS.register("cardboard_box",
			() -> new BlockCardboardBox(AbstractBlock.Properties.of(Material.WOOL)));

	public static final RegistryObject<Block> CUPBOARD = BLOCKS.register("cupboard",
			() -> new BlockCupboard(AbstractBlock.Properties.of(Material.METAL).noOcclusion()));

	public static final RegistryObject<Block> GARBAGE = BLOCKS.register("garbage",
			() -> new BlockGarbage(AbstractBlock.Properties.of(Material.WOOL).sound(SoundType.WOOL).noOcclusion()));
	public static final RegistryObject<Block> OAK_BOOKSHELF = BLOCKS.register("oak_bookshelf",
			() -> new BlockBookshelfEnhanced(AbstractBlock.Properties.of(Material.WOOD)));
	public static final RegistryObject<Block> SPRUCE_BOOKSHELF = BLOCKS.register("spruce_bookshelf",
			() -> new BlockBookshelfEnhanced(AbstractBlock.Properties.of(Material.WOOD)));
	public static final RegistryObject<Block> BIRCH_BOOKSHELF = BLOCKS.register("birch_bookshelf",
			() -> new BlockBookshelfEnhanced(AbstractBlock.Properties.of(Material.WOOD)));
	public static final RegistryObject<Block> JUNGLE_BOOKSHELF = BLOCKS.register("jungle_bookshelf",
			() -> new BlockBookshelfEnhanced(AbstractBlock.Properties.of(Material.WOOD)));
	public static final RegistryObject<Block> ACACIA_BOOKSHELF = BLOCKS.register("acacia_bookshelf",
			() -> new BlockBookshelfEnhanced(AbstractBlock.Properties.of(Material.WOOD)));
	public static final RegistryObject<Block> DARK_OAK_BOOKSHELF = BLOCKS.register("dark_oak_bookshelf",
			() -> new BlockBookshelfEnhanced(AbstractBlock.Properties.of(Material.WOOD)));
	public static final RegistryObject<Block> WARPED_BOOKSHELF = BLOCKS.register("warped_bookshelf",
			() -> new BlockBookshelfEnhanced(AbstractBlock.Properties.of(Material.WOOD)));
	public static final RegistryObject<Block> CRIMSON_BOOKSHELF = BLOCKS.register("crimson_bookshelf",
			() -> new BlockBookshelfEnhanced(AbstractBlock.Properties.of(Material.WOOD)));

	public static final RegistryObject<Block> OAK_WRITING_TABLE = BLOCKS.register("oak_writing_table",
			() -> new BlockWritingTable(AbstractBlock.Properties.of(Material.WOOD)));
	
	public static final RegistryObject<Block> MODERN_WRITING_TABLE = BLOCKS.register("modern_writing_table",
			() -> new BlockWritingTable(AbstractBlock.Properties.of(Material.WOOD)));
	public static final RegistryObject<Block> MEDICAL_CABINET = BLOCKS.register("medical_cabinet",
			() -> new BlockMedicalCabinet(AbstractBlock.Properties.of(Material.METAL).noOcclusion()));

	public static final RegistryObject<Block> FRIDGE = BLOCKS.register("fridge",
			() -> new BlockRefrigerator(AbstractBlock.Properties.of(Material.METAL)));

	public static final RegistryObject<Block> MAIL_BOX = BLOCKS.register("mail_box",
			() -> new BlockMailBox(AbstractBlock.Properties.of(Material.METAL).noOcclusion()));

	public static final RegistryObject<Block> BIRD_NEST = BLOCKS.register("bird_nest",
			() -> new BlockBirdNest(AbstractBlock.Properties.of(Material.METAL).noOcclusion()));

	public static final RegistryObject<Block> TRASH_CAN = BLOCKS.register("trash_can",
			() -> new BlockTrashCan(AbstractBlock.Properties.of(Material.METAL)));

	public static final RegistryObject<Block> TORCH_UNLIT = BLOCKS.register("torch_unlit", () -> new BlockTorchUnlit());

	public static final RegistryObject<Block> TORCH_UNLIT_WALL = BLOCKS.register("torch_unlit_wall",
			() -> new BlockTorchUnlitWall());

	public static final RegistryObject<Block> TORCH_LIT = BLOCKS.register("torch_lit", () -> new BlockTorchEnhanced());

	public static final RegistryObject<Block> TORCH_LIT_WALL = BLOCKS.register("torch_lit_wall",
			() -> new BlockTorchEnhancedWall());

	public static final RegistryObject<Block> ARMCHAIR_BLACK = BLOCKS.register("armchair_black",
			() -> new BlockArmchair(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));

	public static final RegistryObject<Block> ARMCHAIR_WHITE = BLOCKS.register("armchair_white",
			() -> new BlockArmchair(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));
	public static final RegistryObject<Block> ARMCHAIR_RED = BLOCKS.register("armchair_red",
			() -> new BlockArmchair(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));
	public static final RegistryObject<Block> ARMCHAIR_GREEN = BLOCKS.register("armchair_green",
			() -> new BlockArmchair(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));
	public static final RegistryObject<Block> ARMCHAIR_BROWN = BLOCKS.register("armchair_brown",
			() -> new BlockArmchair(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));
	public static final RegistryObject<Block> ARMCHAIR_PINK = BLOCKS.register("armchair_pink",
			() -> new BlockArmchair(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));
	public static final RegistryObject<Block> ARMCHAIR_YELLOW = BLOCKS.register("armchair_yellow",
			() -> new BlockArmchair(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));
	public static final RegistryObject<Block> ARMCHAIR_BLUE = BLOCKS.register("armchair_blue",
			() -> new BlockArmchair(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));
	public static final RegistryObject<Block> ARMCHAIR_CYAN = BLOCKS.register("armchair_cyan",
			() -> new BlockArmchair(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));
	public static final RegistryObject<Block> ARMCHAIR_PURPLE = BLOCKS.register("armchair_purple",
			() -> new BlockArmchair(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));
	public static final RegistryObject<Block> ARMCHAIR_ORANGE = BLOCKS.register("armchair_orange",
			() -> new BlockArmchair(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));
	public static final RegistryObject<Block> ARMCHAIR_MAGENTA = BLOCKS.register("armchair_magenta",
			() -> new BlockArmchair(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));
	public static final RegistryObject<Block> ARMCHAIR_LIME = BLOCKS.register("armchair_lime",
			() -> new BlockArmchair(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));
	public static final RegistryObject<Block> ARMCHAIR_LIGHT_BLUE = BLOCKS.register("armchair_light_blue",
			() -> new BlockArmchair(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));
	public static final RegistryObject<Block> ARMCHAIR_SILVER = BLOCKS.register("armchair_silver",
			() -> new BlockArmchair(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));
	public static final RegistryObject<Block> ARMCHAIR_GRAY = BLOCKS.register("armchair_gray",
			() -> new BlockArmchair(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));

	public static final RegistryObject<Block> SLEEPING_BAG_BLACK = BLOCKS.register("sleeping_bag_black",
			() -> new BlockSleepingBag(DyeColor.BLACK, AbstractBlock.Properties.copy(Blocks.BLACK_BED)));
	public static final RegistryObject<Block> SLEEPING_BAG_WHITE = BLOCKS.register("sleeping_bag_white",
			() -> new BlockSleepingBag(DyeColor.WHITE, AbstractBlock.Properties.copy(Blocks.BLACK_BED)));
	public static final RegistryObject<Block> SLEEPING_BAG_RED = BLOCKS.register("sleeping_bag_red",
			() -> new BlockSleepingBag(DyeColor.RED, AbstractBlock.Properties.copy(Blocks.BLACK_BED)));
	public static final RegistryObject<Block> SLEEPING_BAG_BLUE = BLOCKS.register("sleeping_bag_blue",
			() -> new BlockSleepingBag(DyeColor.BLUE, AbstractBlock.Properties.copy(Blocks.BLACK_BED)));
	public static final RegistryObject<Block> SLEEPING_BAG_GREEN = BLOCKS.register("sleeping_bag_green",
			() -> new BlockSleepingBag(DyeColor.GREEN, AbstractBlock.Properties.copy(Blocks.BLACK_BED)));
	public static final RegistryObject<Block> SLEEPING_BAG_YELLOW = BLOCKS.register("sleeping_bag_yellow",
			() -> new BlockSleepingBag(DyeColor.YELLOW, AbstractBlock.Properties.copy(Blocks.BLACK_BED)));
	public static final RegistryObject<Block> SLEEPING_BAG_ORANGE = BLOCKS.register("sleeping_bag_orange",
			() -> new BlockSleepingBag(DyeColor.ORANGE, AbstractBlock.Properties.copy(Blocks.BLACK_BED)));
	public static final RegistryObject<Block> SLEEPING_BAG_CYAN = BLOCKS.register("sleeping_bag_cyan",
			() -> new BlockSleepingBag(DyeColor.CYAN, AbstractBlock.Properties.copy(Blocks.BLACK_BED)));
	public static final RegistryObject<Block> SLEEPING_BAG_LIME = BLOCKS.register("sleeping_bag_lime",
			() -> new BlockSleepingBag(DyeColor.LIME, AbstractBlock.Properties.copy(Blocks.BLACK_BED)));
	public static final RegistryObject<Block> SLEEPING_BAG_GRAY = BLOCKS.register("sleeping_bag_gray",
			() -> new BlockSleepingBag(DyeColor.GRAY, AbstractBlock.Properties.copy(Blocks.BLACK_BED)));
	public static final RegistryObject<Block> SLEEPING_BAG_SILVER = BLOCKS.register("sleeping_bag_silver",
			() -> new BlockSleepingBag(DyeColor.LIGHT_GRAY, AbstractBlock.Properties.copy(Blocks.BLACK_BED)));
	public static final RegistryObject<Block> SLEEPING_BAG_LIGHT_BLUE = BLOCKS.register("sleeping_bag_light_blue",
			() -> new BlockSleepingBag(DyeColor.LIGHT_BLUE, AbstractBlock.Properties.copy(Blocks.BLACK_BED)));
	public static final RegistryObject<Block> SLEEPING_BAG_PINK = BLOCKS.register("sleeping_bag_pink",
			() -> new BlockSleepingBag(DyeColor.PINK, AbstractBlock.Properties.copy(Blocks.BLACK_BED)));
	public static final RegistryObject<Block> SLEEPING_BAG_PURPLE = BLOCKS.register("sleeping_bag_purple",
			() -> new BlockSleepingBag(DyeColor.PURPLE, AbstractBlock.Properties.copy(Blocks.BLACK_BED)));
	public static final RegistryObject<Block> SLEEPING_BAG_MAGENTA = BLOCKS.register("sleeping_bag_magenta",
			() -> new BlockSleepingBag(DyeColor.MAGENTA, AbstractBlock.Properties.copy(Blocks.BLACK_BED)));
	public static final RegistryObject<Block> SLEEPING_BAG_BROWN = BLOCKS.register("sleeping_bag_brown",
			() -> new BlockSleepingBag(DyeColor.BROWN, AbstractBlock.Properties.copy(Blocks.BLACK_BED)));

	public static final RegistryObject<Block> THROTTLE = BLOCKS.register("throttle",
			() -> new BlockThrottle(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));

	public static final RegistryObject<Block> WOODEN_DOOR = BLOCKS.register("wooden_door",
			() -> new BlockWoodenDoor(AbstractBlock.Properties.copy(Blocks.OAK_DOOR)));

	public static final RegistryObject<Block> WOODEN_DOOR_REINFORCED = BLOCKS.register("wooden_door_reinforced",
			() -> new BlockWoodenDoorReinforced(AbstractBlock.Properties.copy(Blocks.OAK_DOOR)));

	public static final RegistryObject<Block> LOCKED_DOOR = BLOCKS.register("locked_door",
			() -> new BlockDoorLocked(AbstractBlock.Properties.copy(Blocks.OAK_DOOR)));

	public static final RegistryObject<Block> WOODEN_DOOR_IRON_REINFORCED = BLOCKS.register(
			"wooden_door_iron_reinforced",
			() -> new BlockWoodenDoorIronReinforced(AbstractBlock.Properties.copy(Blocks.OAK_DOOR)));

	public static final RegistryObject<Block> TRAFFIC_LIGHT = BLOCKS.register("traffic_light",
			() -> new BlockTrafficLight());

	public static final RegistryObject<Block> TRAFFIC_LIGHT_PEDESTRIAN = BLOCKS.register("traffic_light_pedestrian",
			() -> new BlockTrafficLightPedestrian());

	public static final RegistryObject<Block> SEDAN_RED = BLOCKS.register("sedan_red", () -> new BlockSedan());
	public static final RegistryObject<Block> SEDAN_BLUE = BLOCKS.register("sedan_blue", () -> new BlockSedan());
	public static final RegistryObject<Block> SEDAN_YELLOW = BLOCKS.register("sedan_yellow", () -> new BlockSedan());
	public static final RegistryObject<Block> SEDAN_GREEN = BLOCKS.register("sedan_green", () -> new BlockSedan());
	public static final RegistryObject<Block> SEDAN_WHITE = BLOCKS.register("sedan_white", () -> new BlockSedan());
	public static final RegistryObject<Block> SEDAN_BLACK = BLOCKS.register("sedan_black", () -> new BlockSedan());
	public static final RegistryObject<Block> POLICE_CAR = BLOCKS.register("police_car", () -> new BlockPoliceCar());
	public static final RegistryObject<Block> AMBULANCE = BLOCKS.register("ambulance", () -> new BlockAmbulance());

	public static final RegistryObject<Block> DEAD_MOSSY_STONE = BLOCKS.register("dead_mossy_stone",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.MOSSY_COBBLESTONE)));
	public static final RegistryObject<Block> DEAD_MOSSY_BRICK = BLOCKS.register("dead_mossy_brick",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.MOSSY_STONE_BRICKS)));

	public static final RegistryObject<Block> BASALT = BLOCKS.register("basalt",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE)));
	public static final RegistryObject<Block> MARBLE = BLOCKS.register("marble",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE)));
	public static final RegistryObject<Block> RHYOLITE = BLOCKS.register("rhyolite",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE)));

	public static final RegistryObject<Block> BASALT_COBBLESTONE = BLOCKS.register("basalt_cobblestone",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.COBBLESTONE)));
	public static final RegistryObject<Block> MARBLE_COBBLESTONE = BLOCKS.register("marble_cobblestone",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.COBBLESTONE)));
	public static final RegistryObject<Block> RHYOLITE_COBBLESTONE = BLOCKS.register("rhyolite_cobblestone",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.COBBLESTONE)));

	public static final RegistryObject<Block> BASALT_BRICKS = BLOCKS.register("basalt_bricks",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE_BRICKS)));
	public static final RegistryObject<Block> MARBLE_BRICKS = BLOCKS.register("marble_bricks",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE_BRICKS)));
	public static final RegistryObject<Block> RHYOLITE_BRICKS = BLOCKS.register("rhyolite_bricks",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE_BRICKS)));

	public static final RegistryObject<Block> BASALT_POLISHED = BLOCKS.register("basalt_polished",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE_BRICKS)));
	public static final RegistryObject<Block> MARBLE_POLISHED = BLOCKS.register("marble_polished",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE_BRICKS)));
	public static final RegistryObject<Block> RHYOLITE_POLISHED = BLOCKS.register("rhyolite_polished",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE_BRICKS)));

	public static final RegistryObject<Block> ANDESITE_BRICKS = BLOCKS.register("andesite_bricks",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE_BRICKS)));
	public static final RegistryObject<Block> DIORITE_BRICKS = BLOCKS.register("diorite_bricks",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE_BRICKS)));
	public static final RegistryObject<Block> GRANITE_BRICKS = BLOCKS.register("granite_bricks",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE_BRICKS)));

	public static final RegistryObject<Block> CATWALK = BLOCKS.register("catwalk",
			() -> new BlockCatwalkBase(AbstractBlock.Properties.of(Material.METAL).noOcclusion()));

	public static final RegistryObject<Block> CATWALK_STAIRS = BLOCKS.register("catwalk_stairs",
			() -> new BlockCatwalkStairs());

	public static final RegistryObject<Block> BASALT_BRICKS_CRACKED = BLOCKS.register("basalt_bricks_cracked",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE_BRICKS)));
	public static final RegistryObject<Block> MARBLE_BRICKS_CRACKED = BLOCKS.register("marble_bricks_cracked",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE_BRICKS)));
	public static final RegistryObject<Block> RHYOLITE_BRICKS_CRACKED = BLOCKS.register("rhyolite_bricks_cracked",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE_BRICKS)));

	public static final RegistryObject<Block> ANDESITE_BRICKS_CRACKED = BLOCKS.register("andesite_bricks_cracked",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE_BRICKS)));
	public static final RegistryObject<Block> DIORITE_BRICKS_CRACKED = BLOCKS.register("diorite_bricks_cracked",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE_BRICKS)));
	public static final RegistryObject<Block> GRANITE_BRICKS_CRACKED = BLOCKS.register("granite_bricks_cracked",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE_BRICKS)));

	public static final RegistryObject<Block> BASALT_BRICKS_MOSSY = BLOCKS.register("basalt_bricks_mossy",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE_BRICKS)));
	public static final RegistryObject<Block> MARBLE_BRICKS_MOSSY = BLOCKS.register("marble_bricks_mossy",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE_BRICKS)));
	public static final RegistryObject<Block> RHYOLITE_BRICKS_MOSSY = BLOCKS.register("rhyolite_bricks_mossy",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE_BRICKS)));

	public static final RegistryObject<Block> ANDESITE_BRICKS_MOSSY = BLOCKS.register("andesite_bricks_mossy",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE_BRICKS)));
	public static final RegistryObject<Block> DIORITE_BRICKS_MOSSY = BLOCKS.register("diorite_bricks_mossy",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE_BRICKS)));
	public static final RegistryObject<Block> GRANITE_BRICKS_MOSSY = BLOCKS.register("granite_bricks_mossy",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE_BRICKS)));

	public static final RegistryObject<Block> COMPUTER = BLOCKS.register("computer",
			() -> new BlockComputer(AbstractBlock.Properties.of(Material.METAL)));
	public static final RegistryObject<Block> MONITOR_OFF = BLOCKS.register("monitor_off",
			() -> new BlockMonitor(AbstractBlock.Properties.of(Material.METAL)));
	public static final RegistryObject<Block> MONITOR_MAC = BLOCKS.register("monitor_mac",
			() -> new BlockMonitor(AbstractBlock.Properties.of(Material.METAL)));
	public static final RegistryObject<Block> MONITOR_LINUX = BLOCKS.register("monitor_linux",
			() -> new BlockMonitor(AbstractBlock.Properties.of(Material.METAL)));
	public static final RegistryObject<Block> MONITOR_WIN98 = BLOCKS.register("monitor_win98",
			() -> new BlockMonitor(AbstractBlock.Properties.of(Material.METAL)));
	public static final RegistryObject<Block> MONITOR_WINXP = BLOCKS.register("monitor_winxp",
			() -> new BlockMonitor(AbstractBlock.Properties.of(Material.METAL)));
	public static final RegistryObject<Block> MONITOR_WIN7 = BLOCKS.register("monitor_win7",
			() -> new BlockMonitor(AbstractBlock.Properties.of(Material.METAL)));
	public static final RegistryObject<Block> MONITOR_WIN8 = BLOCKS.register("monitor_win8",
			() -> new BlockMonitor(AbstractBlock.Properties.of(Material.METAL)));
	public static final RegistryObject<Block> MONITOR_WIN10 = BLOCKS.register("monitor_win10",
			() -> new BlockMonitor(AbstractBlock.Properties.of(Material.METAL)));

	public static final RegistryObject<Block> BOARDS = BLOCKS.register("boards", () -> new BlockBoards());

	public static final RegistryObject<Block> TOILET = BLOCKS.register("toilet",
			() -> new BlockToilet(AbstractBlock.Properties.of(Material.STONE).noOcclusion()));

	public static final RegistryObject<Block> FLAG = BLOCKS.register("flag", () -> new BlockFlag());

	public static final RegistryObject<Block> MICROWAVE = BLOCKS.register("microwave",
			() -> new BlockMicrowave(AbstractBlock.Properties.of(Material.METAL).noOcclusion()));

	public static final RegistryObject<Block> SINK_FAUCET = BLOCKS.register("sink_faucet", () -> new BlockSinkFaucet());

	public static final RegistryObject<Block> METAL_LADDER = BLOCKS.register("metal_ladder",
			() -> new BlockLadderMetal());
	public static final RegistryObject<Block> RADIATOR = BLOCKS.register("radiator", () -> new BlockRadiator());
	public static final RegistryObject<Block> DARK_BRICKS = BLOCKS.register("dark_bricks",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE_BRICKS)));

	public static final RegistryObject<Block> CINDER = BLOCKS.register("cinder", () -> new BlockCinder());

	public static final RegistryObject<Block> BACKPACK_NORMAL = BLOCKS.register("backpack_normal",
			() -> new BlockBackpack(AbstractBlock.Properties.copy(Blocks.WHITE_WOOL)));
	public static final RegistryObject<Block> BACKPACK_ARMY = BLOCKS.register("backpack_army",
			() -> new BlockBackpack(AbstractBlock.Properties.copy(Blocks.WHITE_WOOL)));
	public static final RegistryObject<Block> BACKPACK_MEDICAL = BLOCKS.register("backpack_medical",
			() -> new BlockBackpack(AbstractBlock.Properties.copy(Blocks.WHITE_WOOL)));

	public static final RegistryObject<Block> TV_BROKEN = BLOCKS.register("television_broken",
			() -> new BlockTelevisionBroken());
	public static final RegistryObject<Block> SHOWER_HEAD = BLOCKS.register("shower_head", () -> new BlockShowerHead());

	public static final RegistryObject<Block> CORPSE_00 = BLOCKS.register("corpse_00",
			() -> new BlockCorpse(AbstractBlock.Properties.of(Material.WOOL)));
	public static final RegistryObject<Block> CORPSE_01 = BLOCKS.register("corpse_01",
			() -> new BlockCorpse(AbstractBlock.Properties.of(Material.WOOL)));
	public static final RegistryObject<Block> SKELETON = BLOCKS.register("skeleton", () -> new BlockSkeleton());
	public static final RegistryObject<Block> SKELETON_SITTING = BLOCKS.register("skeleton_sitting",
			() -> new BlockSkeleton());
	public static final RegistryObject<Block> SKELETON_TORSO = BLOCKS.register("skeleton_torso", () -> new BlockSkeleton());

	public static final RegistryObject<Block> WORKBENCH = BLOCKS.register("workbench",
			() -> new BlockWorkbench(AbstractBlock.Properties.of(Material.WOOD).noOcclusion()));
	public static final RegistryObject<Block> CHEMISTRY_STATION = BLOCKS.register("chemistry_station",
			() -> new BlockChemistryStation(AbstractBlock.Properties.of(Material.METAL).noOcclusion()));

	public static final RegistryObject<Block> CHAIR_OAK = BLOCKS.register("chair_oak",
			() -> new BlockChair(AbstractBlock.Properties.of(Material.WOOD)));
	public static final RegistryObject<Block> CHAIR_BIRCH = BLOCKS.register("chair_birch",
			() -> new BlockChair(AbstractBlock.Properties.of(Material.WOOD)));
	public static final RegistryObject<Block> CHAIR_SPRUCE = BLOCKS.register("chair_spruce",
			() -> new BlockChair(AbstractBlock.Properties.of(Material.WOOD)));
	public static final RegistryObject<Block> CHAIR_JUNGLE = BLOCKS.register("chair_jungle",
			() -> new BlockChair(AbstractBlock.Properties.of(Material.WOOD)));
	public static final RegistryObject<Block> CHAIR_ACACIA = BLOCKS.register("chair_acacia",
			() -> new BlockChair(AbstractBlock.Properties.of(Material.WOOD)));
	public static final RegistryObject<Block> CHAIR_BIG_OAK = BLOCKS.register("chair_big_oak",
			() -> new BlockChair(AbstractBlock.Properties.of(Material.WOOD)));
	public static final RegistryObject<Block> CHAIR_CRIMSON = BLOCKS.register("chair_crimson",
			() -> new BlockChair(AbstractBlock.Properties.of(Material.WOOD)));
	public static final RegistryObject<Block> CHAIR_WARPED = BLOCKS.register("chair_warped",
			() -> new BlockChair(AbstractBlock.Properties.of(Material.WOOD)));

	public static final RegistryObject<Block> TABLE_OAK = BLOCKS.register("table_oak",
			() -> new BlockTable(AbstractBlock.Properties.of(Material.WOOD), EnumMaterial.WOOD, 6));
	public static final RegistryObject<Block> TABLE_BIRCH = BLOCKS.register("table_birch",
			() -> new BlockTable(AbstractBlock.Properties.of(Material.WOOD), EnumMaterial.WOOD, 6));
	public static final RegistryObject<Block> TABLE_SPRUCE = BLOCKS.register("table_spruce",
			() -> new BlockTable(AbstractBlock.Properties.of(Material.WOOD), EnumMaterial.WOOD, 6));
	public static final RegistryObject<Block> TABLE_JUNGLE = BLOCKS.register("table_jungle",
			() -> new BlockTable(AbstractBlock.Properties.of(Material.WOOD), EnumMaterial.WOOD, 6));
	public static final RegistryObject<Block> TABLE_ACACIA = BLOCKS.register("table_acacia",
			() -> new BlockTable(AbstractBlock.Properties.of(Material.WOOD), EnumMaterial.WOOD, 6));
	public static final RegistryObject<Block> TABLE_BIG_OAK = BLOCKS.register("table_big_oak",
			() -> new BlockTable(AbstractBlock.Properties.of(Material.WOOD), EnumMaterial.WOOD, 6));
	public static final RegistryObject<Block> TABLE_CRIMSON = BLOCKS.register("table_crimson",
			() -> new BlockTable(AbstractBlock.Properties.of(Material.WOOD), EnumMaterial.WOOD, 6));
	public static final RegistryObject<Block> TABLE_WARPED = BLOCKS.register("table_warped",
			() -> new BlockTable(AbstractBlock.Properties.of(Material.WOOD), EnumMaterial.WOOD, 6));
	public static final RegistryObject<Block> TABLE_BURNT = BLOCKS.register("table_burnt",
			() -> new BlockTable(AbstractBlock.Properties.of(Material.WOOD), EnumMaterial.WOOD, 6));

	public static final RegistryObject<Block> GENERATOR_GAS = BLOCKS.register("generator_gas",
			() -> new BlockGenerator(AbstractBlock.Properties.of(Material.WOOD).noOcclusion()));
	public static final RegistryObject<Block> GENERATOR_COMBUSTION = BLOCKS.register("generator_combustion",
			() -> new BlockCombustionGenerator(AbstractBlock.Properties.of(Material.WOOD).noOcclusion()));

	public static final RegistryObject<Block> GASOLINE = BLOCKS.register("gasoline",
			() -> new FlowingFluidBlock(() -> (FlowingFluid) ModFluids.GASOLINE_FLOWING.get(),
					AbstractBlock.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));

	public static final RegistryObject<Block> ENERGY_POLE = BLOCKS.register("energy_pole",
			() -> new BlockEnergyPole(AbstractBlock.Properties.of(Material.WOOD)));

	public static final RegistryObject<Block> ENERGY_SWITCH = BLOCKS.register("energy_switch",
			() -> new BlockEnergySwitch(AbstractBlock.Properties.of(Material.WOOD)));

	public static final RegistryObject<Block> LAMP = BLOCKS.register("lamp",
			() -> new BlockLamp(AbstractBlock.Properties.of(Material.METAL).noOcclusion()));

	public static final RegistryObject<Block> THERMOMETER = BLOCKS.register("thermometer",
			() -> new BlockThermometer(AbstractBlock.Properties.of(Material.METAL)));

	public static final RegistryObject<Block> STREET_SIGN = BLOCKS.register("street_sign",
			() -> new BlockStreetSign(AbstractBlock.Properties.of(Material.METAL).noCollission(), SevenDaysToMine.STREET_WOOD_TYPE));

	public static final RegistryObject<Block> STREET_SIGN_WALL = BLOCKS.register("street_sign_wall",
			() -> new BlockWallStreetSign(AbstractBlock.Properties.of(Material.METAL).noCollission(), SevenDaysToMine.STREET_WOOD_TYPE));

	public static final RegistryObject<Block> PHOTO = BLOCKS.register("photo",
			() -> new BlockPhoto(AbstractBlock.Properties.of(Material.WOOL)));

	public static final RegistryObject<Block> SCREEN_PROJECTOR = BLOCKS.register("screen_projector",
			() -> new BlockScreenProjector(AbstractBlock.Properties.of(Material.METAL).noOcclusion()));

	public static final RegistryObject<Block> DRESSER = BLOCKS.register("dresser",
			() -> new BlockDresser(AbstractBlock.Properties.of(Material.WOOD).noOcclusion()));

	public static final RegistryObject<Block> BIG_SIGN_MASTER = BLOCKS.register("big_sign",
			() -> new BlockBigSignMaster(AbstractBlock.Properties.of(Material.METAL).noCollission(), SevenDaysToMine.STREET_WOOD_TYPE));

	public static final RegistryObject<Block> BIG_SIGN_SLAVE = BLOCKS.register("big_sign_slave",
			() -> new BlockBigSignSlave(AbstractBlock.Properties.of(Material.METAL).noCollission(), SevenDaysToMine.STREET_WOOD_TYPE));

	public static final RegistryObject<Block> SOFA_BLACK = BLOCKS.register("sofa_black",
			() -> new BlockSofa(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));

	public static final RegistryObject<Block> SOFA_WHITE = BLOCKS.register("sofa_white",
			() -> new BlockSofa(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));

	public static final RegistryObject<Block> SOFA_RED = BLOCKS.register("sofa_red",
			() -> new BlockSofa(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));

	public static final RegistryObject<Block> SOFA_GREEN = BLOCKS.register("sofa_green",
			() -> new BlockSofa(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));

	public static final RegistryObject<Block> SOFA_BLUE = BLOCKS.register("sofa_blue",
			() -> new BlockSofa(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));

	public static final RegistryObject<Block> SOFA_BROWN = BLOCKS.register("sofa_brown",
			() -> new BlockSofa(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));

	public static final RegistryObject<Block> SOFA_PINK = BLOCKS.register("sofa_pink",
			() -> new BlockSofa(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));

	public static final RegistryObject<Block> SOFA_YELLOW = BLOCKS.register("sofa_yellow",
			() -> new BlockSofa(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));

	public static final RegistryObject<Block> SOFA_CYAN = BLOCKS.register("sofa_cyan",
			() -> new BlockSofa(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));

	public static final RegistryObject<Block> SOFA_PURPLE = BLOCKS.register("sofa_purple",
			() -> new BlockSofa(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));

	public static final RegistryObject<Block> SOFA_ORANGE = BLOCKS.register("sofa_orange",
			() -> new BlockSofa(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));

	public static final RegistryObject<Block> SOFA_MAGENTA = BLOCKS.register("sofa_magenta",
			() -> new BlockSofa(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));

	public static final RegistryObject<Block> SOFA_LIME = BLOCKS.register("sofa_lime",
			() -> new BlockSofa(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));

	public static final RegistryObject<Block> SOFA_SILVER = BLOCKS.register("sofa_silver",
			() -> new BlockSofa(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));

	public static final RegistryObject<Block> SOFA_LIGHT_BLUE = BLOCKS.register("sofa_light_blue",
			() -> new BlockSofa(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));

	public static final RegistryObject<Block> SOFA_GRAY = BLOCKS.register("sofa_gray",
			() -> new BlockSofa(AbstractBlock.Properties.copy(Blocks.BLACK_WOOL)));

	public static final RegistryObject<Block> TRASH_BIN = BLOCKS.register("trash_bin",
			() -> new BlockTrashBin(AbstractBlock.Properties.of(Material.METAL)));

	public static final RegistryObject<Block> WHEELS = BLOCKS.register("wheels",
			() -> new BlockWheels(AbstractBlock.Properties.of(Material.WOOL)));

	public static final RegistryObject<Block> LARGE_ROCK = BLOCKS.register("large_rock", () -> new BlockRock());

	public static final RegistryObject<Block> WOODEN_SPIKES = BLOCKS.register("wooden_spikes",
			() -> new BlockWoodenSpikes(AbstractBlock.Properties.of(Material.WOOD)));

	public static final RegistryObject<Block> WOODEN_SPIKES_BLOODED = BLOCKS.register("wooden_spikes_blooded",
			() -> new BlockWoodenSpikes(AbstractBlock.Properties.of(Material.WOOD)));

	public static final RegistryObject<Block> WOODEN_SPIKES_BROKEN = BLOCKS.register("wooden_spikes_broken",
			() -> new BlockWoodenSpikes(AbstractBlock.Properties.of(Material.WOOD)));

	public static final RegistryObject<Block> AIRPLANE_ROTOR = BLOCKS.register("airplane_rotor",
			() -> new BlockAirplaneRotor(AbstractBlock.Properties.of(Material.METAL)));

	public static final RegistryObject<Block> SOLAR_PANEL = BLOCKS.register("solar_panel",
			() -> new BlockSolarPanel(AbstractBlock.Properties.of(Material.METAL)));

	public static final RegistryObject<Block> WIND_TURBINE = BLOCKS.register("wind_turbine",
			() -> new BlockWindTurbine(AbstractBlock.Properties.of(Material.METAL).noOcclusion()));

	public static final RegistryObject<Block> BATTERY_STATION = BLOCKS.register("battery_station",
			() -> new BlockBatteryStation(AbstractBlock.Properties.of(Material.METAL).noOcclusion()));

	public static final RegistryObject<Block> BRICK_MOSSY = BLOCKS.register("brick_block_mossy",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.MOSSY_STONE_BRICKS)));

	public static final RegistryObject<Block> DARK_BRICKS_MOSSY = BLOCKS.register("dark_bricks_mossy",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.MOSSY_STONE_BRICKS)));

	public static final RegistryObject<Block> TURRET_BASE = BLOCKS.register("turret_base",
			() -> new BlockTurretBase(AbstractBlock.Properties.of(Material.METAL)));

	public static final RegistryObject<Block> TURRET_ADVANCED = BLOCKS.register("turret_advanced",
			() -> new BlockTurretAdvanced(AbstractBlock.Properties.of(Material.METAL)));

	public static final RegistryObject<BlockWoodenLogSpike> OAK_LOG_SPIKE = BLOCKS.register("oak_log_spike",
			() -> new BlockWoodenLogSpike(AbstractBlock.Properties.of(Material.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockWoodenLogSpike> BIRCH_LOG_SPIKE = BLOCKS.register("birch_log_spike",
			() -> new BlockWoodenLogSpike(AbstractBlock.Properties.of(Material.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockWoodenLogSpike> SPRUCE_LOG_SPIKE = BLOCKS.register("spruce_log_spike",
			() -> new BlockWoodenLogSpike(AbstractBlock.Properties.of(Material.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockWoodenLogSpike> JUNGLE_LOG_SPIKE = BLOCKS.register("jungle_log_spike",
			() -> new BlockWoodenLogSpike(AbstractBlock.Properties.of(Material.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockWoodenLogSpike> ACACIA_LOG_SPIKE = BLOCKS.register("acacia_log_spike",
			() -> new BlockWoodenLogSpike(AbstractBlock.Properties.of(Material.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockWoodenLogSpike> DARK_OAK_LOG_SPIKE = BLOCKS.register("dark_oak_log_spike",
			() -> new BlockWoodenLogSpike(AbstractBlock.Properties.of(Material.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockWoodenLogSpike> WARPED_LOG_SPIKE = BLOCKS.register("warped_log_spike",
			() -> new BlockWoodenLogSpike(AbstractBlock.Properties.copy(Blocks.WARPED_STEM)));
	public static final RegistryObject<BlockWoodenLogSpike> CRIMSON_LOG_SPIKE = BLOCKS.register("crimson_log_spike",
			() -> new BlockWoodenLogSpike(AbstractBlock.Properties.copy(Blocks.CRIMSON_STEM)));
	
	
	public static final RegistryObject<Block> SANDBAGS = BLOCKS.register("sandbags", () -> new BlockSandbags());

	public static final RegistryObject<Block> FILE_CABINET = BLOCKS.register("file_cabinet",
			() -> new BlockFileCabinet(AbstractBlock.Properties.of(Material.METAL)));

	public static final RegistryObject<Block> CASH_REGISTER = BLOCKS.register("cash_register",
			() -> new BlockCashRegister(AbstractBlock.Properties.of(Material.METAL)));

	public static final RegistryObject<Block> CAMERA = BLOCKS.register("camera",
			() -> new BlockCamera(AbstractBlock.Properties.of(Material.METAL)));

	public static final RegistryObject<Block> BURNT_LOG = BLOCKS.register("burnt_log", () -> new BlockBurntLog());

	public static final RegistryObject<BlockBurntFrame> BURNT_FRAME = BLOCKS.register("burnt_frame", () -> new BlockBurntFrame());

	public static final RegistryObject<BlockBurntPlanks> BURNT_PLANKS = BLOCKS.register("burnt_planks",
			() -> new BlockBurntPlanks());

	public static final RegistryObject<Block> DRY_GROUND = BLOCKS.register("dry_ground",
			() -> new BlockDryGround(AbstractBlock.Properties.copy(Blocks.GRAVEL)));

	public static final RegistryObject<Block> BURNT_PLANKS_STAIRS = BLOCKS.register("burnt_planks_stairs",
			() -> new BlockStairsBase(() -> BURNT_PLANKS.get().defaultBlockState(),
					AbstractBlock.Properties.copy(BURNT_PLANKS.get())));

	public static final RegistryObject<Block> BURNT_PLANKS_SLAB = BLOCKS.register("burnt_planks_slab",
			() -> new BlockBurntPlanksSlab());

	public static final RegistryObject<Block> BURNT_PLANKS_FENCE = BLOCKS.register("burnt_planks_fence",
			() -> new BlockBurntPlanksFence());

	public static final RegistryObject<Block> BURNT_CHAIR = BLOCKS.register("burnt_chair",
			() -> new BlockChair(AbstractBlock.Properties.copy(BURNT_PLANKS.get())));

	public static final RegistryObject<Block> RADIO = BLOCKS.register("radio",
			() -> new BlockRadio(AbstractBlock.Properties.of(Material.METAL)));
	public static final RegistryObject<Block> GLOBE = BLOCKS.register("globe",
			() -> new BlockGlobe(AbstractBlock.Properties.of(Material.METAL)));

	public static final RegistryObject<Block> SEPARATOR = BLOCKS.register("separator",
			() -> new BlockSeparator(AbstractBlock.Properties.of(Material.METAL)));
	public static final RegistryObject<Block> RAZOR_WIRE = BLOCKS.register("razor_wire",
			() -> new BlockRazorWire(AbstractBlock.Properties.of(Material.METAL).noOcclusion()));

	public static final RegistryObject<Block> STAND = BLOCKS.register("stand", () -> new BlockStand());

	public static final RegistryObject<Block> LANDMINE = BLOCKS.register("landmine", () -> new BlockLandmine());

	public static final RegistryObject<Block> FAKE_ANVIL = BLOCKS.register("fake_anvil",
			() -> new BlockFakeAnvil(AbstractBlock.Properties.copy(Blocks.ANVIL)));

	public static final RegistryObject<Block> REDSTONE_LAMP_BROKEN = BLOCKS.register("redstone_lamp_broken",
			() -> new BlockRedstoneLightBroken());

	public static final RegistryObject<Block> CALENDAR = BLOCKS.register("calendar",
			() -> new BlockCalendar(AbstractBlock.Properties.of(Material.WOOL).noOcclusion()));

	public static final RegistryObject<Block> FLAMETHOWER = BLOCKS.register("flamethrower_trap",
			() -> new BlockFlamethrower(AbstractBlock.Properties.of(Material.METAL)));

	public static final RegistryObject<Block> METAL_SPIKES = BLOCKS.register("metal_spikes",
			() -> new BlockMetalSpikes(AbstractBlock.Properties.of(Material.METAL).noCollission()));
	public static final RegistryObject<Block> METAL_SPIKES_EXTENDED = BLOCKS.register("metal_spikes_extended",
			() -> new BlockMetalSpikes(AbstractBlock.Properties.of(Material.METAL).noCollission()));

	public static final RegistryObject<Block> STEEL_BLOCK = BLOCKS.register("steel_block",
			() -> new BlockMetal(AbstractBlock.Properties.of(Material.METAL).strength(6, 12), EnumMaterial.STEEL, 54));
	public static final RegistryObject<Block> BRONZE_BLOCK = BLOCKS.register("bronze_block",
			() -> new BlockMetal(AbstractBlock.Properties.of(Material.METAL).strength(2f, 5), EnumMaterial.BRASS, 54));
	public static final RegistryObject<Block> LEAD_BLOCK = BLOCKS.register("lead_block",
			() -> new BlockMetal(AbstractBlock.Properties.of(Material.METAL).strength(4, 11), EnumMaterial.LEAD, 54));
	public static final RegistryObject<Block> COPPER_BLOCK = BLOCKS.register("copper_block",
			() -> new BlockMetal(AbstractBlock.Properties.of(Material.METAL).strength(1.5f, 3), EnumMaterial.COPPER,
					54));
	public static final RegistryObject<Block> BRASS_BLOCK = BLOCKS.register("brass_block",
			() -> new BlockMetal(AbstractBlock.Properties.of(Material.METAL).strength(1.9f, 4), EnumMaterial.BRASS,
					54));

	public static final RegistryObject<Block> PAPER = BLOCKS.register("paper", () -> new BlockPaper());
	public static final RegistryObject<Block> SAND_LAYER = BLOCKS.register("sand_layer",
			() -> new BlockSandLayer(AbstractBlock.Properties.copy(Blocks.SAND)));
	public static final RegistryObject<Block> RED_SAND_LAYER = BLOCKS.register("red_sand_layer",
			() -> new BlockSandLayer(AbstractBlock.Properties.copy(Blocks.SAND)));

	public static final RegistryObject<Block> DARK_BRICKS_SLAB = BLOCKS.register("dark_bricks_slab",
			() -> new BlockDarkBricksSlab());
	public static final RegistryObject<Block> DARK_BRICKS_STAIRS = BLOCKS.register("dark_bricks_stairs",
			() -> new BlockStairsBase(() -> BURNT_PLANKS.get().defaultBlockState(),
					AbstractBlock.Properties.copy(DARK_BRICKS.get())));

	public static final RegistryObject<Block> DARK_BRICKS_MOSSY_SLAB = BLOCKS.register("dark_bricks_mossy_slab",
			() -> new BlockDarkBricksMossySlab());

	public static final RegistryObject<Block> DARK_BRICKS_MOSSY_STAIRS = BLOCKS.register("dark_bricks_mossy_stairs",
			() -> new BlockStairsBase(() -> BURNT_PLANKS.get().defaultBlockState(),
					AbstractBlock.Properties.copy(DARK_BRICKS_MOSSY.get())));

	public static final RegistryObject<Block> BRICKS_MOSSY_STAIRS = BLOCKS.register("brick_mossy_stairs",
			() -> new BlockStairsBase(() -> BURNT_PLANKS.get().defaultBlockState(),
					AbstractBlock.Properties.copy(BRICK_MOSSY.get())));

	public static final RegistryObject<Block> BRICK_MOSSY_SLAB = BLOCKS.register("brick_mossy_slab",
			() -> new BlockBrickMossySlab());

	public static final RegistryObject<Block> ASPHALT_SLAB = BLOCKS.register("asphalt_slab",
			() -> new BlockAsphaltSlab());

	public static final RegistryObject<Block> PRINTER = BLOCKS.register("printer",
			() -> new BlockPrinter(AbstractBlock.Properties.of(Material.METAL).noOcclusion()));

	public static final RegistryObject<Block> STRUCTURE_STONE = BLOCKS.register("structure_stone",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.STONE)));

	public static final RegistryObject<Block> STRUCTURE_GRANITE = BLOCKS.register("structure_granite",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.GRANITE)));

	public static final RegistryObject<Block> STRUCTURE_DIORITE = BLOCKS.register("structure_diorite",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.DIORITE)));

	public static final RegistryObject<Block> STRUCTURE_ANDESITE = BLOCKS.register("structure_andesite",
			() -> new BlockBase(AbstractBlock.Properties.copy(Blocks.ANDESITE)));

	public static ToIntFunction<BlockState> litBlockEmission(int p_235420_0_) {
		return (p_235421_1_) -> {
			return p_235421_1_.getValue(BlockStateProperties.LIT) ? p_235420_0_ : 0;
		};
	}
}
