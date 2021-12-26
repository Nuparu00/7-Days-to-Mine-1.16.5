package nuparu.sevendaystomine.util;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import net.minecraft.util.*;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import nuparu.sevendaystomine.entity.MountableBlockEntity;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.items.IItemHandler;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.IUpgradeable;
import nuparu.sevendaystomine.block.repair.BreakData;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IChunkData;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.item.ItemGun;
import nuparu.sevendaystomine.item.ItemGun.EnumWield;
import nuparu.sevendaystomine.util.dialogue.Dialogues;
import nuparu.sevendaystomine.util.dialogue.DialoguesRegistry;

public class Utils {
	private static Logger logger;

	public static Direction[] HORIZONTALS = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH,
			Direction.WEST };

	public static Logger getLogger() {
		if (logger == null) {
			logger = LogManager.getLogger(SevenDaysToMine.MODID);
		}
		return logger;
	}

	public static boolean mountBlock(World world, BlockPos pos, PlayerEntity player) {
		return mountBlock(world, pos, player, 0);
	}

	public static boolean mountBlock(World world, BlockPos pos, PlayerEntity player, float deltaY) {
		double x = pos.getX()+0.5;
		double y = pos.getY();
		double z = pos.getZ()+0.5;
		List<MountableBlockEntity> list = world.getEntitiesOfClass(MountableBlockEntity.class,
				new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D).inflate(1D));
		for (MountableBlockEntity mount : list) {
			if (mount.getBlockPos() == pos) {
				if (!mount.isVehicle()) {
					player.startRiding(mount);
				}
				return true;
			}
		}
		if (list.size() == 0) {
			MountableBlockEntity mount = new MountableBlockEntity(world, x, y, z);
			mount.setDeltaY(deltaY);
			world.addFreshEntity(mount);
			player.startRiding(mount);
			return true;
		}
		return false;
	}

	public static void tryOpenWebsite(String URL) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI(URL));
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
				return;
			}
		} else {
			return;
		}
		return;
	}

	public static boolean isPlayerDualWielding(PlayerEntity player) {
		if (player == null) {
			return false;
		}
		ItemStack main = player.getMainHandItem();
		ItemStack sec = player.getOffhandItem();
		if ((main == null || main.isEmpty()) && (sec == null || sec.isEmpty())) {
			return false;
		}
		Item item_main = main.getItem();
		Item item_sec = sec.getItem();
		if (item_main == null && item_sec == null) {
			return false;
		}

		ItemGun gun_main = null;
		ItemGun gun_sec = null;

		if (item_main instanceof ItemGun) {
			gun_main = (ItemGun) item_main;
		}

		if (item_sec instanceof ItemGun) {
			gun_sec = (ItemGun) item_sec;
		}

		if (gun_main == null && gun_sec == null) {
			return false;
		}

		if (gun_main != null && gun_main.getWield() == EnumWield.DUAL) {
			if (gun_sec == null) {
				return false;
			}
			if (gun_sec.getWield() == EnumWield.DUAL) {
				return true;
			}
		}
		return false;
	}

	public static double getCrosshairSpread(PlayerEntity player) {
		if (player == null) {
			return -1;
		}
		ItemStack main = player.getMainHandItem();
		ItemStack sec = player.getOffhandItem();
		if ((main == null || main.isEmpty()) && (sec == null || sec.isEmpty())) {
			return -1;
		}
		Item item_main = main.getItem();
		Item item_sec = sec.getItem();
		if (item_main == null && item_sec == null) {
			return -1;
		}

		ItemGun gun_main = null;
		ItemGun gun_sec = null;

		if (item_main instanceof ItemGun) {
			gun_main = (ItemGun) item_main;
		}

		if (item_sec instanceof ItemGun) {
			gun_sec = (ItemGun) item_sec;
		}

		if (gun_main == null && gun_sec == null) {
			return -1;
		}

		if (gun_main != null && gun_sec == null) {
			return gun_main.getCross(player, Hand.MAIN_HAND);
		} else if (gun_main == null && gun_sec != null) {
			return gun_sec.getCross(player, Hand.OFF_HAND);
		} else {
			return Math.max(gun_sec.getCross(player, Hand.MAIN_HAND), gun_main.getCross(player, Hand.OFF_HAND));
		}

	}

	public static boolean hasGunInAnyHand(PlayerEntity player) {
		if (player == null) {
			return false;
		}
		ItemStack main = player.getMainHandItem();
		ItemStack sec = player.getOffhandItem();
		if ((main == null || main.isEmpty()) && (sec == null || sec.isEmpty())) {
			return false;
		}
		Item item_main = main.getItem();
		Item item_sec = sec.getItem();
		if (item_main == null && item_sec == null) {
			return false;
		}
		if (item_main instanceof ItemGun) {
			return true;
		}

		if (item_sec instanceof ItemGun) {
			return true;
		}
		return false;
	}

	public static float angularDistance(float alpha, float beta) {
		float phi = Math.abs(beta - alpha) % 360;
		float distance = phi > 180 ? 360 - phi : phi;
		return distance;
	}

	/**
	 * By Choonster
	 * 
	 * Returns <code>null</code>.
	 * <p>
	 * This is used in the initialisers of <code>static final</code> fields instead
	 * of using <code>null</code> directly to suppress the "Argument might be null"
	 * warnings from IntelliJ IDEA's "Constant conditions &amp; exceptions"
	 * inspection.
	 * <p>
	 * Based on diesieben07's solution <a href=
	 * "http://www.minecraftforge.net/forum/topic/60980-solved-disable-%E2%80%9Cconstant-conditions-exceptions%E2%80%9D-inspection-for-field-in-intellij-idea/?do=findCommentcomment=285024">here</a>.
	 *
	 * @param <T> The field's type.
	 * @return null
	 */
	public static <T> T Null() {
		return null;
	}

	/*
	 * By Choonster
	 */
	public static NonNullList<ItemStack> dropItemHandlerContents(IItemHandler itemHandler, Random random) {
		final NonNullList<ItemStack> drops = NonNullList.create();

		for (int slot = 0; slot < itemHandler.getSlots(); ++slot) {
			while (!itemHandler.getStackInSlot(slot).isEmpty()) {
				final int amount = random.nextInt(21) + 10;

				if (itemHandler.extractItem(slot, amount, true) != null) {
					final ItemStack itemStack = itemHandler.extractItem(slot, amount, false);
					drops.add(itemStack);
				}
			}
		}

		return drops;
	}

	public static BlockPos getTopGroundBlock(BlockPos pos, World world, boolean solid) {
		return getTopGroundBlock(pos, world, solid, true);
	}
	public static BlockPos getTopGroundBlock(BlockPos pos, WorldGenRegion world, boolean solid) {
		return getTopGroundBlock(pos, world, solid, true);
	}
	public static BlockPos getTopGroundBlock(BlockPos pos, World world, boolean solid, boolean ignoreFoliage) {
		Chunk chunk = world.getChunkAt(pos);
		BlockPos blockpos;
		BlockPos blockpos1;

		for (blockpos = new BlockPos(pos.getX(), chunk.getHighestSectionPosition() + 16, pos.getZ()); blockpos
				.getY() >= 0; blockpos = blockpos1) {
			blockpos1 = blockpos.below();
			BlockState state = chunk.getBlockState(blockpos1);
			Block block = state.getBlock();
			Material material = state.getMaterial();

			if (solid && material.isLiquid()) {
				blockpos = blockpos1;
				break;
			}
			if (solid && state.isFaceSturdy(world, pos, Direction.UP) && material != Material.SNOW) {
				blockpos = blockpos1;
				break;
			} else if (material != Material.AIR) {
				blockpos = blockpos1;
				break;
			}
		}

		return blockpos;
	}

	public static BlockPos getTopGroundBlock(BlockPos pos, WorldGenRegion world, boolean solid, boolean ignoreFoliage) {
		IChunk ichunk = world.getChunk(pos);
		if(ichunk instanceof Chunk) {
			Chunk chunk = (Chunk) ichunk;

			BlockPos blockpos;
			BlockPos blockpos1;

			for (blockpos = new BlockPos(pos.getX(), chunk.getHighestSectionPosition() + 16, pos.getZ()); blockpos
					.getY() >= 0; blockpos = blockpos1) {
				blockpos1 = blockpos.below();
				BlockState state = chunk.getBlockState(blockpos1);
				Block block = state.getBlock();
				Material material = state.getMaterial();

				if (solid && material.isLiquid()) {
					blockpos = blockpos1;
					break;
				}
				if (solid && state.isFaceSturdy(world, pos, Direction.UP) && material != Material.SNOW) {
					blockpos = blockpos1;
					break;
				} else if (material != Material.AIR) {
					blockpos = blockpos1;
					break;
				}
			}

			return blockpos;
		}
		return BlockPos.ZERO;
	}

	/*
	 * public static BlockPos getTopBiomeGroundBlock(BlockPos pos, World world) {
	 * Chunk chunk = world.getChunkAt(pos); BlockPos blockpos; BlockPos blockpos1;
	 * 
	 * Biome biome = world.getBiome(pos);
	 * 
	 * for (blockpos = new BlockPos(pos.getX(), chunk.getHighestSectionPosition() +
	 * 16, pos.getZ()); blockpos .getY() >= 0; blockpos = blockpos1) { blockpos1 =
	 * blockpos.below(); BlockState state = chunk.getBlockState(blockpos1); Block
	 * block = state.getBlock(); Material material = state.getMaterial();
	 * 
	 * if (state == biome.topBlock) { blockpos = blockpos1; break; } if
	 * (blockpos1.getY() <= 63) { return new BlockPos(0, -1, 0); } }
	 * 
	 * return blockpos; }
	 */

	public static int getTopSolidGroundHeight(BlockPos pos, World world) {
		Chunk chunk = world.getChunkAt(pos);
		BlockPos blockpos;
		BlockPos blockpos1;

		for (blockpos = new BlockPos(pos.getX(), chunk.getHighestSectionPosition() + 16, pos.getZ()); blockpos
				.getY() >= 0; blockpos = blockpos1) {
			blockpos1 = blockpos.below();
			BlockState state = chunk.getBlockState(blockpos1);
			Block block = state.getBlock();
			Material material = state.getMaterial();

			if (material.isLiquid()) {
				break;
			}
			if (state.isFaceSturdy(world, pos, Direction.UP)) {
				break;
			}
		}

		return blockpos != null ? blockpos.getY() : -1;
	}

	public static int getTopSolidGroundHeight(BlockPos pos, WorldGenRegion world) {
		IChunk ichunk = world.getChunk(pos);

		if(ichunk instanceof  Chunk) {
			Chunk chunk = (Chunk)ichunk;
			BlockPos blockpos;
			BlockPos blockpos1;

			for (blockpos = new BlockPos(pos.getX(), chunk.getHighestSectionPosition() + 16, pos.getZ()); blockpos
					.getY() >= 0; blockpos = blockpos1) {
				blockpos1 = blockpos.below();
				BlockState state = chunk.getBlockState(blockpos1);
				Block block = state.getBlock();
				Material material = state.getMaterial();

				if (material.isLiquid()) {
					break;
				}
				if (state.isFaceSturdy(world, pos, Direction.UP)) {
					break;
				}
			}

			return blockpos != null ? blockpos.getY() : -1;
		}
		return -1;
	}

	public static boolean isSolid(ISeedReader world, BlockPos pos, BlockState state) {
		Material material = state.getMaterial();
		Block block = state.getBlock();
		if (!material.isLiquid() && state.isFaceSturdy(world, pos, Direction.UP)) {
			return true;
		}
		return false;
	}

	public static float lerp(float a, float b, float f) {
		return (a * (1.0f - f)) + (b * f);
	}

	// returns true if the block has been destroyed
	public static boolean damageBlock(ServerWorld world, BlockPos pos, float damage, boolean breakBlock) {
		return damageBlock(world, pos, damage, breakBlock, true);
	}

	// returns true if the block has been destroyed
	public static boolean damageBlock(ServerWorld world, BlockPos pos, float damage, boolean breakBlock,
			boolean sound) {
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		float hardness = state.getDestroySpeed(world, pos);
		if (hardness <= 0) {
			return false;
		}
		if (block.isAir(state, world, pos)) {
			return false;
		}
		if (block instanceof IFluidBlock || block instanceof FlowingFluidBlock) {
			return false;
		}
		if (hardness == 0) {
			world.destroyBlock(pos, false);
			return true;
		}
		
		Chunk chunk = world.getChunkAt(pos);
		IChunkData chunkData = CapabilityHelper.getChunkData(chunk);
		
		if (chunkData == null) {
			return false;
		}
		SoundType soundType = block.getSoundType(state, world, pos, null);
		if (sound) {
			world.playSound(null, pos, soundType.getHitSound(), SoundCategory.NEUTRAL,
					(soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
		}
		chunkData.addBreakData(pos, damage / hardness);
		BreakData breakData = chunkData.getBreakData(pos);
		if (breakData != null && breakData.getState() >= 1f && breakBlock) {
			chunkData.removeBreakData(pos);
			if (block instanceof IUpgradeable) {
				BlockState prev = ((IUpgradeable) block).getPrev(world, pos, state);
				if (prev != null) {
					world.setBlockAndUpdate(pos, prev);
					((IUpgradeable) block).onDowngrade(world, pos, state);
				}
				return true;
			}
			world.destroyBlock(pos, false);
			return true;
		}

		return false;
	}

	/*
	 * public static boolean isOperator(PlayerEntity player) { return
	 * ServerLifecycleHooks.getCurrentServer().getPlayerList().getOppedPlayers()
	 * .getEntry(player.getGameProfile()) != null; }
	 */

	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static Entity getEntityByNBTAndResource(ResourceLocation resourceLocation, CompoundNBT nbt, World world) {

		if (nbt != null) {
			Entity entity = EntityType.create(nbt, world).orElse(null);
			return entity;
		}
		return null;
	}

	public static int getWorldMinutes(World world) {
		int time = (int) Math.abs((world.getDayTime() + 6000) % 24000);
		return (time % 1000) * 6 / 100;
	}

	public static int getWorldHours(World world) {
		int time = (int) Math.abs((world.getDayTime() + 6000) % 24000);
		return (int) ((float) time / 1000F);
	}

	public static boolean isInArea(double mouseX, double mouseY, double x, double y, double width, double height) {
		return (mouseX >= x && mouseY >= y) && (mouseX <= x + width && mouseY <= y + height);
	}

	public static boolean isInAreaAbs(double mouseX, double mouseY, double x1, double y1, double x2, double y2) {
		return (mouseX >= x1 && mouseY >= y1) && (mouseX <= x2 && mouseY <= y2);
	}

	public static boolean isInsideBlock(Entity entity, Block block) {
		double d0 = entity.getY() + (double) entity.getEyeHeight();
		BlockPos blockpos = new BlockPos(entity.getX(), d0, entity.getZ());
		BlockState BlockState = entity.level.getBlockState(blockpos);
		if (BlockState.getBlock() == block) {
			return true;
		}
		return false;
	}

	public static MinecraftServer getServer() {
		return ServerLifecycleHooks.getCurrentServer();
	}

	public static InputStream getInsideFileStream(String root ,String path) {
		return Utils.class.getResourceAsStream("/" + root + "/" + path);
	}

	public static File tempFileFromStream(InputStream in, String name, String extension) throws IOException {
		final File tempFile = File.createTempFile(name, extension);
		tempFile.deleteOnExit();
		try (FileOutputStream out = new FileOutputStream(tempFile)) {
			IOUtils.copy(in, out);
		}
		return tempFile;
	}

	public static byte[] getBytes(BufferedImage img) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(img, "png", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();
			return imageInByte;

		} catch (IOException e) {
			getLogger().debug(e.getMessage());
		}
		return null;
	}

	public static List<byte[]> divideArray(byte[] source, int chunksize) {

		List<byte[]> result = new ArrayList<byte[]>();
		int start = 0;
		while (start < source.length) {
			int end = Math.min(source.length, start + chunksize);
			result.add(Arrays.copyOfRange(source, start, end));
			start += chunksize;
		}

		return result;
	}

	public static boolean compareBlockPos(BlockPos a, BlockPos b) {
		return a != null && b != null && a.getX() == b.getX() && a.getY() == b.getY() && a.getZ() == b.getZ();
	}

	public static TextFormatting dyeColorToTextFormatting(DyeColor color) {
		switch (color) {
		default:
		case WHITE:
			return TextFormatting.WHITE;
		case ORANGE:
			return TextFormatting.GOLD;
		case MAGENTA:
			return TextFormatting.AQUA;
		case LIGHT_BLUE:
			return TextFormatting.BLUE;
		case YELLOW:
			return TextFormatting.YELLOW;
		case LIME:
			return TextFormatting.GREEN;
		case PINK:
			return TextFormatting.LIGHT_PURPLE;
		case GRAY:
			return TextFormatting.DARK_GRAY;
		case LIGHT_GRAY:
			return TextFormatting.GRAY;
		case CYAN:
			return TextFormatting.DARK_AQUA;
		case PURPLE:
			return TextFormatting.DARK_PURPLE;
		case BLUE:
			return TextFormatting.DARK_BLUE;
		case BROWN:
			return TextFormatting.GOLD;
		case GREEN:
			return TextFormatting.DARK_GREEN;
		case RED:
			return TextFormatting.DARK_RED;
		case BLACK:
			return TextFormatting.BLACK;

		}
	}

	public static final IDataSerializer<Dialogues> DIALOGUES = new IDataSerializer<Dialogues>() {
		public void write(PacketBuffer buf, Dialogues value) {
			if (buf == null || value == null || value.getKey() == null)
				return;
			buf.writeResourceLocation(value.getKey());
		}

		public Dialogues read(PacketBuffer buf) {
			return DialoguesRegistry.INSTANCE.getByRes(buf.readResourceLocation());
		}

		public DataParameter<Dialogues> createKey(int id) {
			return new DataParameter<Dialogues>(id, this);
		}

		public Dialogues copy(Dialogues value) {
			return value;
		}
	};

	public static byte[] convertToBytes(Object object) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream(bos);
		out.writeObject(object);
		return bos.toByteArray();

	}

	public static Object convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInput in = new ObjectInputStream(bis);
		return in.readObject();
	}

	@SuppressWarnings("rawtypes")
	public static boolean isClassFromPackageOrChild(Class clazz, String path) {
		return containsIgnoreCase(clazz.toString(), path);
	}

	public static boolean containsIgnoreCase(String str, String subString) {
		return str.toLowerCase().contains(subString.toLowerCase());
	}

	public static void stackTrace(int length) {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		for (int i = 0; i < Math.min(length, stackTraceElements.length); i++) {
			StackTraceElement ste = stackTraceElements[i];

		}
	}

	public static int facingToInt(Direction facing) {
		switch (facing) {
		case SOUTH:
			return 0;
		case EAST:
			return 90;
		case NORTH:
			return 180;
		case WEST:
			return 270;
		default:
			return 0;
		}
	}

	public static Rotation intToRotation(int angle) {
		int remainder = angle % 360;

		switch (remainder) {
		case 0:
			return Rotation.NONE;
		case 90:
			return Rotation.CLOCKWISE_90;
		case 180:
			return Rotation.CLOCKWISE_180;
		case 270:
			return Rotation.COUNTERCLOCKWISE_90;
		default:
			return Rotation.NONE;
		}
	}

	public static Rotation facingToRotation(Direction facing) {

		switch (facing) {
		default:
		case SOUTH:
			return Rotation.NONE;
		case WEST:
			return Rotation.CLOCKWISE_90;
		case NORTH:
			return Rotation.CLOCKWISE_180;
		case EAST:
			return Rotation.COUNTERCLOCKWISE_90;
		}
	}

	public static int getDay(World world) {
		return Math.round(world.getDayTime() / 24000) + 1;
	}

	public static boolean isBloodmoon(World world) {
		return CommonConfig.bloodmoonFrequency.get() > 0 && Utils.getDay(world) % CommonConfig.bloodmoonFrequency.get() == 0;
	}

	public static boolean isBloodmoonProper(World world) {
		return isBloodmoon(world) && !world.isDay();
	}

	public static boolean isBloodmoon(int day) {
		return CommonConfig.bloodmoonFrequency.get() > 0 && day % CommonConfig.bloodmoonFrequency.get() == 0;
	}

	public static boolean isWolfHorde(World world) {
		return CommonConfig.wolfHordeFrequency.get() > 0 && Utils.getDay(world) % CommonConfig.wolfHordeFrequency.get() == 0;
	}

	/*
	 * public static int getItemBurnTime(ItemStack stack) { if (stack.isEmpty()) {
	 * return 0; } else { int burnTime =
	 * net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack); if
	 * (burnTime >= 0) return burnTime; Item item = stack.getItem();
	 * 
	 * if (item == Item.byBlock(Blocks.WOODEN_SLAB)) { return 150; } else if (item
	 * == Item.byBlock(Blocks.WOOL)) { return 100; } else if (item ==
	 * Item.byBlock(Blocks.CARPET)) { return 67; } else if (item ==
	 * Item.byBlock(Blocks.LADDER)) { return 300; } else if (item ==
	 * Item.byBlock(Blocks.WOODEN_BUTTON)) { return 100; } else if
	 * (Block.getBlockFromItem(item).defaultBlockState().getItemMaterial() ==
	 * Material.WOOD) { return 300; } else if (item ==
	 * Item.byBlock(Blocks.COAL_BLOCK)) { return 16000; } else if (item instanceof
	 * ItemTool && "WOOD".equals(((ItemTool) item).getToolMaterialName())) { return
	 * 200; } else if (item instanceof ItemSword && "WOOD".equals(((ItemSword)
	 * item).getToolMaterialName())) { return 200; } else if (item instanceof
	 * ItemHoe && "WOOD".equals(((ItemHoe) item).getMaterialName())) { return 200; }
	 * else if (item == Items.STICK) { return 100; } else if (item != Items.BOW &&
	 * item != Items.FISHING_ROD) { if (item == Items.SIGN) { return 200; } else if
	 * (item == Items.COAL) { return 1600; } else if (item == Items.LAVA_BUCKET) {
	 * return 20000; } else if (item != Item.byBlock(Blocks.SAPLING) && item !=
	 * Items.BOWL) { if (item == Items.BLAZE_ROD) { return 2400; } else if (item
	 * instanceof ItemDoor && item != Items.IRON_DOOR) { return 200; } else { return
	 * item instanceof ItemBoat ? 400 : 0; } } else { return 100; } } else { return
	 * 300; } } }
	 */

	public static BlockPos getAirdropPos(World world) {
		List<? extends PlayerEntity> players = world.players();
		double xSum = 0;
		double zSum = 0;

		if (players.size() == 1) {
			PlayerEntity player = players.get(0);

			double angle = 2.0 * Math.PI * world.random.nextDouble();
			double dist = 256 + world.random.nextDouble() * 256;
			double x = player.getX() + dist * Math.cos(angle);
			double z = player.getZ() + dist * Math.sin(angle);

			return new BlockPos(x, 255, z);
		}

		for (PlayerEntity player : players) {
			xSum += player.getX();
			zSum += player.getZ();
		}

		double angle = 2.0 * Math.PI * world.random.nextDouble();
		double dist = 256 + world.random.nextDouble() * 256;
		double x = xSum / players.size() + dist * Math.cos(angle);
		double z = zSum / players.size() + dist * Math.sin(angle);

		return new BlockPos(x, 255, z);
	}

	public static BlockPos getAirDropStartPoint(World world, BlockPos centerPoint) {
		List<? extends PlayerEntity> players = world.players();
		if (players.size() == 0)
			return null;

		BlockPos theMostDistant = null;
		double lastDistance = Double.MAX_VALUE;
		if (players.size() == 1) {
			PlayerEntity player = players.get(0);
			theMostDistant = new BlockPos(player.getX() + (world.random.nextDouble() * 256) - 128, 255,
					player.getZ() + (world.random.nextDouble() * 256) - 128);
			lastDistance = centerPoint.distSqr(theMostDistant);
		}

		if (theMostDistant == null) {
			for (PlayerEntity player : players) {
				BlockPos bp = new BlockPos(player.getX(), 255, player.getZ());
				double dist = bp.distSqr(centerPoint);
				if (dist < lastDistance) {
					lastDistance = dist;
					theMostDistant = bp;
				}
			}
		}
		double x = 0;
		double z = 0;
		double d = (lastDistance + 32) / lastDistance;

		x = ((1 - d) * centerPoint.getX()) + (d * theMostDistant.getX());
		z = ((1 - d) * centerPoint.getZ()) + (d * theMostDistant.getY());
		return new BlockPos(x, 255, z);

	}

	@SafeVarargs
    public static <T> Set<T> combine(Set<T>... sets) {
		return Stream.of(sets).flatMap(Set::stream).collect(Collectors.toSet());
	}

	public static void infectPlayer(PlayerEntity player, int time) {
		IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
		if (!iep.isInfected()) {
			iep.setInfectionTime(time);
		}
	}

	public static int getItemCount(PlayerInventory inv, Item item) {
		int count = 0;
		for (int slot = 0; slot < inv.getContainerSize(); slot++) {
			ItemStack stack = inv.getItem(slot);
			if (stack != null && stack.getItem() == item) {
				count += stack.getCount();
			}
		}
		return count;
	}

	public static void renderBiomePreviewMap(World worldIn, ItemStack map) {
		/*
		 * if (map.getItem() instanceof MapItem) { MapData mapdata = ((MapItem)
		 * map.getItem()).getMapData(map, worldIn);
		 * 
		 * if (mapdata != null) { if (worldIn.provider.getDimension() ==
		 * mapdata.dimension) { int i = 1 << mapdata.scale; int j = mapdata.xCenter; int
		 * k = mapdata.zCenter;
		 * 
		 * int x = (j / i - 64) * i; int z = (k / i - 64) * i;
		 * 
		 * Biome[] abiome = worldIn.getBiomeProvider().getBiomes((Biome[]) null, x, z,
		 * 128 * i, 128 * i, false);
		 * 
		 * for (int l = 0; l < 128; ++l) { for (int i1 = 0; i1 < 128; ++i1) { int j1 = l
		 * * i; int k1 = i1 * i; Biome biome = abiome[j1 + k1 * 128 * i]; MapColor
		 * mapcolor = MapColor.AIR;
		 * 
		 * if (j1 % 64 == 0 || k1 % 64 == 0) { mapcolor = MapColor.BLACK; }
		 * 
		 * int l1 = 3; int i2 = 8;
		 * 
		 * if (l > 0 && i1 > 0 && l < 127 && i1 < 127) { if (abiome[(l - 1) * i + (i1 -
		 * 1) * i * 128 * i].getBaseHeight() >= 0.0F) { --i2; }
		 * 
		 * if (abiome[(l - 1) * i + (i1 + 1) * i * 128 * i].getBaseHeight() >= 0.0F) {
		 * --i2; }
		 * 
		 * if (abiome[(l - 1) * i + i1 * i * 128 * i].getBaseHeight() >= 0.0F) { --i2; }
		 * 
		 * if (abiome[(l + 1) * i + (i1 - 1) * i * 128 * i].getBaseHeight() >= 0.0F) {
		 * --i2; }
		 * 
		 * if (abiome[(l + 1) * i + (i1 + 1) * i * 128 * i].getBaseHeight() >= 0.0F) {
		 * --i2; }
		 * 
		 * if (abiome[(l + 1) * i + i1 * i * 128 * i].getBaseHeight() >= 0.0F) { --i2; }
		 * 
		 * if (abiome[l * i + (i1 - 1) * i * 128 * i].getBaseHeight() >= 0.0F) { --i2; }
		 * 
		 * if (abiome[l * i + (i1 + 1) * i * 128 * i].getBaseHeight() >= 0.0F) { --i2; }
		 * 
		 * if (biome.getBaseHeight() < 0.0F) { mapcolor = MapColor.ADOBE;
		 * 
		 * if (i2 > 7 && i1 % 2 == 0) { l1 = (l + (int) (MathHelper.sin((float) i1 +
		 * 0.0F) * 7.0F)) / 8 % 5;
		 * 
		 * if (l1 == 3) { l1 = 1; } else if (l1 == 4) { l1 = 0; } } else if (i2 > 7) {
		 * mapcolor = MapColor.AIR; } else if (i2 > 5) { l1 = 1; } else if (i2 > 3) { l1
		 * = 0; } else if (i2 > 1) { l1 = 0; } } else if (i2 > 0) { mapcolor =
		 * MapColor.BROWN;
		 * 
		 * if (i2 > 3) { l1 = 1; } else { l1 = 3; } } }
		 * 
		 * if (!BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN) &&
		 * Math.abs(RoadDecoratorWorldGen.getNoiseValue(x + j1, z + k1, 0)) < 0.005d) {
		 * mapcolor = MapColor.BROWN_STAINED_HARDENED_CLAY; }
		 * 
		 * if (mapcolor != MapColor.AIR) { mapdata.colors[l + i1 * 128] = (byte)
		 * (mapcolor.colorIndex * 4 + l1); mapdata.updateMapData(l, i1); } } } } } }
		 */
	}

	public static void consumeXp(PlayerEntity player, float amount) {
		if (player.totalExperience - amount <= 0) {
			player.experienceLevel = 0;
			player.experienceProgress = 0;
			player.totalExperience = 0;
			return;
		}

		player.totalExperience -= amount;
		/*
		 * if (player.experienceProgress * (float) player.max <= amount) { amount -=
		 * player.experienceProgress * (float) player.xpBarCap();
		 * player.experienceProgress = 1.0f; player.experienceLevel--; }
		 * 
		 * while (player.xpBarCap() < amount) { amount -= player.xpBarCap();
		 * player.experienceLevel--; }
		 * 
		 * player.experience -= amount / (float) player.xpBarCap();
		 */
	}

	public static boolean canCityBeGeneratedHere(ISeedReader world, int chunkX, int chunkZ) {
		return false;
	}

	public static List<ChunkPos> getClosestCities(ServerWorld world, int chunkX, int chunkZ, int dst) {
		List<ChunkPos> poses = new ArrayList<ChunkPos>();
		for (int i = -dst; i < dst; i++) {
			for (int j = -dst; j < dst; j++) {
				int cX = chunkX + i;
				int cZ = chunkZ + j;
				if (Utils.canCityBeGeneratedHere(world, cX, cZ)) {
					poses.add(new ChunkPos(cX, cZ));
				}
			}
		}
		return poses;
	}

	public static boolean isCityInArea(ServerWorld world, int chunkX, int chunkZ, int dst) {
		for (int i = -dst; i < dst; i++) {
			for (int j = -dst; j < dst; j++) {
				int cX = chunkX + i;
				int cZ = chunkZ + j;
				if (Utils.canCityBeGeneratedHere(world, cX, cZ)) {
					return true;
				}
			}
		}
		return false;
	}

	public static ChunkPos getClosestCity(ServerWorld world, int chunkX, int chunkZ) {

		ChunkPos nearest = null;
		double nearestDst = Double.MAX_VALUE;
		List<ChunkPos> cities = getClosestCities(world, chunkX, chunkZ, 128);
		for (ChunkPos pos : cities) {
			double dst = (pos.x - chunkX) + (pos.z - chunkZ);
			if (dst < nearestDst) {
				nearestDst = dst;
				nearest = pos;
			}
		}

		return nearest;
	}

	public static void generateDiagonal(World world, BlockPos start, BlockPos end) {
		int minX = Math.min(start.getX(), end.getX());
		int minZ = Math.min(start.getZ(), end.getZ());

		int maxX = Math.max(start.getX(), end.getX());
		int maxZ = Math.max(start.getZ(), end.getZ());

		Vector3d dif = new Vector3d(maxX - minX, 0, maxZ - minZ);
		Vector3d vec = dif.scale(1d / dif.length());

		for (double d = 0; d < dif.length(); d++) {
			Vector3d vec2 = vec.scale(d);
			world.setBlockAndUpdate(new BlockPos(minX + vec2.x, 200, minZ + vec2.z),
					Blocks.IRON_BLOCK.defaultBlockState());
		}
		world.setBlockAndUpdate(new BlockPos(minX, 50, minZ), Blocks.REDSTONE_BLOCK.defaultBlockState());
		world.setBlockAndUpdate(new BlockPos(maxX, 50, maxZ), Blocks.GOLD_BLOCK.defaultBlockState());

	}

	public static RayTraceResult raytraceEntities(Entity entity, double dst) {

		Entity pointedEntity = null;

		Vector3d vec3d = entity.getEyePosition(1);

		BlockRayTraceResult r = rayTraceServer(entity, dst, 1);
		double d1 = dst;
		if (r != null) {
			d1 = r.getLocation().distanceTo(vec3d);
		}

		Vector3d vec3d1 = entity.getLookAngle();
		Vector3d vec3d2 = vec3d.add(vec3d1.x * dst, vec3d1.y * dst, vec3d1.z * dst);
		List<Entity> list = entity.level.getEntities(entity,
				entity.getBoundingBox().inflate(vec3d1.x * dst, vec3d1.y * dst, vec3d1.z * dst),
				EntityPredicates.NO_SPECTATORS.and((Predicate<Entity>) Entity::canBeCollidedWith));

		Vector3d vec3d3 = null;
		double d2 = d1;
		for (int j = 0; j < list.size(); ++j) {
			Entity entity1 = list.get(j);
			AxisAlignedBB axisalignedbb = entity1.getBoundingBox().inflate(entity1.getPickRadius());
			Optional<Vector3d> optional = axisalignedbb.clip(vec3d, vec3d2);

			if (axisalignedbb.contains(vec3d)) {
				if (d2 >= 0.0D) {
					pointedEntity = entity1;
					vec3d3 = optional.orElse(vec3d);
					d2 = 0.0D;
				}
			} else if (optional.isPresent()) {
				Vector3d vector3d1 = optional.get();
				double d3 = vec3d.distanceToSqr(vector3d1);

				if (d3 < d2 || d2 == 0.0D) {
					if (entity1.getRootVehicle() == entity.getRootVehicle() && !entity1.canRiderInteract()) {
						if (d2 == 0.0D) {
							pointedEntity = entity1;
							vec3d3 = vector3d1;
						}
					} else {
						pointedEntity = entity1;
						vec3d3 = vector3d1;
						d2 = d3;
					}
				}
			}
		}

		if (pointedEntity != null) {
			return new EntityRayTraceResult(pointedEntity, vec3d3);
		}
		return null;
	}

	public static BlockRayTraceResult rayTraceServer(Entity entity, double blockReachDistance, float partialTicks) {
		Vector3d vec3 = getPositionEyesServer(entity, partialTicks);
		Vector3d vec31 = entity.getLookAngle();
		Vector3d vec32 = vec3.add(vec31.x * blockReachDistance, vec31.y * blockReachDistance,
				vec31.z * blockReachDistance);
		return entity.level.clip(new RayTraceContext(vec3, vec32, RayTraceContext.BlockMode.OUTLINE,
				RayTraceContext.FluidMode.NONE, entity));
	}

	public static Vector3d getPositionEyesServer(Entity entity, float partialTicks) {
		if (partialTicks == 1.0F) {
			return new Vector3d(entity.getX(), entity.getY() + (double) entity.getEyeHeight(), entity.getZ());
		} else {
			double d0 = entity.xOld + (entity.getX() - entity.xOld) * (double) partialTicks;
			double d1 = entity.yOld + (entity.getY() - entity.yOld) * (double) partialTicks
					+ (double) entity.getEyeHeight();
			double d2 = entity.zOld + (entity.getZ() - entity.zOld) * (double) partialTicks;
			return new Vector3d(d0, d1, d2);
		}
	}

	public static boolean hasItemStack(PlayerEntity player, ItemStack itemStack) {
		int count = 0;
		for (int slot = 0; slot < player.inventory.getContainerSize(); slot++) {
			ItemStack stack = player.inventory.getItem(slot);
			if (stack != null && stack.getItem() == itemStack.getItem()) {
				count += stack.getCount();
			}
		}
		return count >= itemStack.getCount();
	}

	public static void removeItemStack(PlayerInventory inv, ItemStack itemStack) {
		int count = itemStack.getCount();
		for (int slot = 0; slot < inv.getContainerSize(); slot++) {
			ItemStack stack = inv.getItem(slot);
			if (stack != null && stack.getItem() == itemStack.getItem()) {
				int decrease = Math.min(count, stack.getCount());
				inv.removeItem(slot, decrease);
				count -= decrease;
				if (count <= 0) {
					break;
				}
			}
		}
	}

	@Nullable
	public static IRecipe getRecipesForStack(ItemStack stack, MinecraftServer server) {
		for (IRecipe irecipe : server.getRecipeManager().getRecipes()) {
			if (ItemStack.isSameIgnoreDurability(stack, irecipe.getResultItem())) {
				return irecipe;
			}
		}

		return null;
	}

	public static PlayerEntity getPlayerFromUUID(UUID parUUID) {
		if (parUUID == null) {
			return null;
		}
		List<ServerPlayerEntity> allPlayers = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers();
		for (ServerPlayerEntity player : allPlayers) {
			if (player.getUUID().equals(parUUID)) {
				return player;
			}
		}
		return null;
	}

	public static void clearMatchingItems(IInventory inv, Item item, int count) {
		for (int i = 0; (i < inv.getMaxStackSize() && count > 0); i++) {
			ItemStack stack = inv.getItem(i);
			if (stack.getItem() == item) {
				int toRemove = Math.min(stack.getCount(), count);
				stack.shrink(toRemove);
				if (stack.getCount() <= 0) {
					inv.setItem(i, ItemStack.EMPTY);
				}
				count -= toRemove;
			}
		}
	}

	public static File getCurrentSaveRootDirectory(World world) {
		return DimensionType.getStorageFolder(world.dimension(),
				world.getServer().getWorldPath(FolderName.ROOT).toFile());
	}

	public static File getCurrentSaveRootDirectory() {
		MinecraftServer server = getServer();
		World world = server.overworld();
		return DimensionType.getStorageFolder(world.dimension(),
				world.getServer().getWorldPath(FolderName.ROOT).toFile());
	}

	public static <T> ArrayList<T> twoDimensionalArrayToList(T[][] array) {
		ArrayList<T> list = new ArrayList<T>();
		for (int i = 0; i < array.length; i++) {
			list.addAll(Arrays.asList(array[i]));
		}

		return list;
	}

	public static BlockRayTraceResult rayTrace(World worldIn, Entity playerIn, double distance,
			RayTraceContext.FluidMode fluidMode) {
		float f = playerIn.xRot;
		float f1 = playerIn.yRot;
		double d0 = playerIn.getX();
		double d1 = playerIn.getY() + (double) playerIn.getEyeHeight();
		double d2 = playerIn.getZ();
		Vector3d vec3 = new Vector3d(d0, d1, d2);
		float f2 = MathHelper.cos(-f1 * 0.017453292F - (float) Math.PI);
		float f3 = MathHelper.sin(-f1 * 0.017453292F - (float) Math.PI);
		float f4 = -MathHelper.cos(-f * 0.017453292F);
		float f5 = MathHelper.sin(-f * 0.017453292F);
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		Vector3d vector3d1 = vec3.add((double) f6 * distance, (double) f5 * distance, (double) f7 * distance);
		return worldIn
				.clip(new RayTraceContext(vec3, vector3d1, RayTraceContext.BlockMode.OUTLINE, fluidMode, playerIn));
	}


	public static boolean isNearStructure(Structure structure, ChunkGenerator p_242782_1_, long p_242782_2_, SharedSeedRandom p_242782_4_, int p_242782_5_, int p_242782_6_) {
		StructureSeparationSettings structureseparationsettings = p_242782_1_.getSettings().getConfig(structure);
		if (structureseparationsettings == null) {
			return false;
		} else {
			for(int i = p_242782_5_ - 10; i <= p_242782_5_ + 10; ++i) {
				for(int j = p_242782_6_ - 10; j <= p_242782_6_ + 10; ++j) {
					ChunkPos chunkpos = structure.getPotentialFeatureChunk(structureseparationsettings, p_242782_2_, p_242782_4_, i, j);
					if (i == chunkpos.x && j == chunkpos.z) {
						return true;
					}
				}
			}

			return false;
		}
	}
}
