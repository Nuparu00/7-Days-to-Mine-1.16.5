package nuparu.sevendaystomine.init;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.enchantment.*;
import nuparu.sevendaystomine.item.ItemGun;
import nuparu.sevendaystomine.util.Utils;

public class ModEnchantments {

	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS,
			SevenDaysToMine.MODID);

	public static final RegistryObject<Enchantment> FAST_RELOAD = ENCHANTMENTS.register("fast_reload",
			() -> new EnchantmentFastReload());
	public static final RegistryObject<Enchantment> MARKSMAN = ENCHANTMENTS.register("marksman",
			() -> new EnchantmentMarksman());
	public static final RegistryObject<Enchantment> EXPLOSIVE = ENCHANTMENTS.register("explosive",
			() -> new EnchantmentExplosive());
	public static final RegistryObject<Enchantment> SPARKING = ENCHANTMENTS.register("sparking",
			() -> new EnchantmentSparking());
	public static final RegistryObject<Enchantment> BIG_MAG = ENCHANTMENTS.register("big_mag",
			() -> new EnchantmentBigMag());
	public static final RegistryObject<Enchantment> MULTISHOT = ENCHANTMENTS.register("multishot",
			() -> new EnchantmentMultishot());
	public static final RegistryObject<Enchantment> SHAKING = ENCHANTMENTS.register("shaking",
			() -> new EnchantmentStrabismus());
	public static final RegistryObject<Enchantment> JAMMING = ENCHANTMENTS.register("jamming",
			() -> new EnchantmentJamming());
	public static final RegistryObject<Enchantment> SMALL_MAG = ENCHANTMENTS.register("small_mag",
			() -> new EnchantmentSmallMag());

	public static final EnchantmentType GUNS = EnchantmentType.create("guns", (item) -> item instanceof ItemGun);

}