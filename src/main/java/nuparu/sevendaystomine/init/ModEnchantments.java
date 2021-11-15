package nuparu.sevendaystomine.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.*;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.enchantment.*;
import nuparu.sevendaystomine.item.ItemGun;

public class ModEnchantments {

	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS,
			SevenDaysToMine.MODID);

	public static final RegistryObject<Enchantment> FAST_RELOAD = ENCHANTMENTS.register("fast_reload",
            EnchantmentFastReload::new);
	public static final RegistryObject<Enchantment> MARKSMAN = ENCHANTMENTS.register("marksman",
            EnchantmentMarksman::new);
	public static final RegistryObject<Enchantment> EXPLOSIVE = ENCHANTMENTS.register("explosive",
            EnchantmentExplosive::new);
	public static final RegistryObject<Enchantment> SPARKING = ENCHANTMENTS.register("sparking",
            EnchantmentSparking::new);
	public static final RegistryObject<Enchantment> BIG_MAG = ENCHANTMENTS.register("big_mag",
            EnchantmentBigMag::new);
	public static final RegistryObject<Enchantment> MULTISHOT = ENCHANTMENTS.register("multishot",
            EnchantmentMultishot::new);
	public static final RegistryObject<Enchantment> SHAKING = ENCHANTMENTS.register("shaking",
            EnchantmentStrabismus::new);
	public static final RegistryObject<Enchantment> JAMMING = ENCHANTMENTS.register("jamming",
            EnchantmentJamming::new);
	public static final RegistryObject<Enchantment> SMALL_MAG = ENCHANTMENTS.register("small_mag",
            EnchantmentSmallMag::new);

	public static final EnchantmentType GUNS = EnchantmentType.create("guns", (item) -> item instanceof ItemGun);

}