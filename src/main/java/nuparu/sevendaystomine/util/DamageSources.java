package nuparu.sevendaystomine.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import nuparu.sevendaystomine.util.damagesource.EntityDamageShot;
import nuparu.sevendaystomine.util.damagesource.EntityDamageVehicle;

public class DamageSources {
	public static DamageSource thirst = new DamageSource("thirst").setScalesWithDifficulty().bypassMagic()
			.bypassArmor();
	public static DamageSource sharpGlass = new DamageSource("sharpGlass").setScalesWithDifficulty();
	public static DamageSource alcoholPoison = new DamageSource("alcoholPoison").setScalesWithDifficulty().bypassMagic()
			.bypassArmor();
	public static DamageSource mercuryPoison = new DamageSource("mercury_poisoning").setScalesWithDifficulty()
			.bypassMagic().bypassArmor();
	public static DamageSource bleeding = new DamageSource("bleeding").setScalesWithDifficulty().bypassMagic()
			.bypassArmor();
	public static DamageSource blade = new DamageSource("blade").setScalesWithDifficulty().bypassMagic();
	public static DamageSource infection = new DamageSource("infection").setScalesWithDifficulty().bypassMagic()
			.bypassArmor();
	public static DamageSource chlorinePoison = new DamageSource("chlorine_poisoning").setScalesWithDifficulty()
			.bypassMagic().bypassArmor();

	public static EntityDamageShot causeShotDamage(Entity source, Entity transmitter) {
		return new EntityDamageShot("shot.entity", transmitter, source);
	}

	public static EntityDamageVehicle causeVehicleDamage(Entity source, Entity transmitter) {
		return new EntityDamageVehicle("vehicle", transmitter, source);
	}
}
