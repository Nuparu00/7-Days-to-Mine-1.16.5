package nuparu.sevendaystomine.potions;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;

public class Potions {

	public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, SevenDaysToMine.MODID);
	public static final RegistryObject<Effect> THIRST = EFFECTS.register("thirst", () ->  new PotionThirst(EffectType.HARMFUL, 1930808));
	public static final RegistryObject<Effect> DYSENTERY = EFFECTS.register("dysentery", () ->  new PotionDysentery(EffectType.HARMFUL, 10092544));
	public static final RegistryObject<Effect> ALCOHOL_BUZZ = EFFECTS.register("alcohol_buzz", () ->  new PotionAlcoholBuzz(EffectType.BENEFICIAL, 20092544));
	public static final RegistryObject<Effect> DRUNK = EFFECTS.register("drunk", () ->  new PotionDrunk(EffectType.HARMFUL, 22092544));
	public static final RegistryObject<Effect> ALCOHOL_POISON = EFFECTS.register("alcohol_poison", () ->  new PotionAlcoholPoison(EffectType.HARMFUL, 1092544));
	public static final RegistryObject<Effect> CAFFEINE_BUZZ = EFFECTS.register("caffeine_buzz", () ->  new PotionCaffeineBuzz(EffectType.BENEFICIAL, 5092544));
	public static final RegistryObject<Effect> BLEEDING = EFFECTS.register("bleeding", () ->  new PotionBleeding(EffectType.HARMFUL, 5092544));
	public static final RegistryObject<Effect> INFECTION = EFFECTS.register("infection", () ->  new PotionInfection(EffectType.HARMFUL, 1930808));
	public static final RegistryObject<Effect> BROKEN_LEG = EFFECTS.register("broken_leg", () ->  new PotionBrokenLeg(EffectType.HARMFUL, 0xaaaaaa));
	public static final RegistryObject<Effect> MERCURY_POISON = EFFECTS.register("mercury_poison", () ->  new PotionMercuryPoisoning(EffectType.HARMFUL, 1930808));
	public static final RegistryObject<Effect> CHLORINE_POISON = EFFECTS.register("chlorine_poison", () ->  new PotionChlorinePoisoning(EffectType.HARMFUL, 1930808));
	public static final RegistryObject<Effect> SPLINTED_LEG = EFFECTS.register("splinted_leg", () ->  new PotionSplintedLeg(EffectType.HARMFUL, 0xaaaaaa));

}