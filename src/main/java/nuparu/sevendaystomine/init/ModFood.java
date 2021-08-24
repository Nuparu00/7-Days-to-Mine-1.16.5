package nuparu.sevendaystomine.init;

import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import nuparu.sevendaystomine.potions.Potions;

public class ModFood {

	public static final Food ANTIBIOTICS = new Food.Builder().alwaysEat().fast().nutrition(0).saturationMod(0).build();
	public static final Food BANEBERRY = new Food.Builder().fast().nutrition(1).saturationMod(0.3f).effect(() -> new EffectInstance(Effects.WEAKNESS,3000) ,0.75f).effect(() -> new EffectInstance(Effects.MOVEMENT_SLOWDOWN,3000) ,0.65f).effect(() -> new EffectInstance(Effects.CONFUSION,1200) ,0.45f).effect(() -> new EffectInstance(Effects.BLINDNESS,1200) ,0.45f).build();
	public static final Food BLUEBERRY = new Food.Builder().fast().nutrition(1).saturationMod(0.3f).build();
	public static final Food CORN = new Food.Builder().fast().nutrition(1).saturationMod(1f).build();
	public static final Food COFFEE_BERRY = new Food.Builder().fast().nutrition(1).saturationMod(0.3f).build();
	public static final Food MURKY_WATER = new Food.Builder().alwaysEat().nutrition(0).saturationMod(0f).effect(() -> new EffectInstance(Potions.DYSENTERY.get(),6000) ,0.5f).build();
	public static final Food WATER = new Food.Builder().alwaysEat().nutrition(0).saturationMod(0f).build();
	public static final Food COFFEE_DRINK = new Food.Builder().alwaysEat().nutrition(0).saturationMod(0f).effect(() -> new EffectInstance(Potions.CAFFEINE_BUZZ.get(),2400) ,1f).build();
	public static final Food CANNED_ANIMAL_FOOD = new Food.Builder().nutrition(3).saturationMod(1f).meat().build();
	public static final Food CANNED_HAM = new Food.Builder().nutrition(6).saturationMod(2.5f).meat().build();
	public static final Food CANNED_CHICKEN = new Food.Builder().nutrition(5).saturationMod(2f).meat().build();
	public static final Food CANNED_CHILI = new Food.Builder().nutrition(4).saturationMod(2f).build();
	public static final Food CANNED_MISO = new Food.Builder().nutrition(4).saturationMod(1.5f).build();
	public static final Food CANNED_PASTA = new Food.Builder().nutrition(5).saturationMod(2f).build();
	public static final Food CANNED_FRUIT_AND_VEGETABLES = new Food.Builder().nutrition(3).saturationMod(1.5f).build();
	public static final Food CANNED_SOUP = new Food.Builder().nutrition(5).saturationMod(1.75f).build();
	public static final Food CANNED_STOCK = new Food.Builder().nutrition(4).saturationMod(1.6f).build();
	public static final Food CANNED_HUGE_MEAT = new Food.Builder().nutrition(7).saturationMod(3).meat().build();
	public static final Food MRE = new Food.Builder().nutrition(5).saturationMod(5).build();
	public static final Food MOLDY_BREAD = new Food.Builder().fast().nutrition(2).saturationMod(0.3f).effect(() -> new EffectInstance(Effects.POISON,120) ,0.75f).build();
	public static final Food SODA = new Food.Builder().alwaysEat().nutrition(0).saturationMod(0f).effect(() -> new EffectInstance(Potions.CAFFEINE_BUZZ.get(),3200) ,1f).build();

	}
