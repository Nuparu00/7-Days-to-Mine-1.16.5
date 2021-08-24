package nuparu.sevendaystomine.init;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanValue;
import net.minecraft.world.GameRules.Category;
import net.minecraft.world.GameRules.IntegerValue;
import net.minecraft.world.GameRules.RuleType;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ModGameRules {
	
	static Method m_create_boolean = null;
	static Method m_create_int = null;

	public static GameRules.RuleKey<BooleanValue> handleThirst;
	public static GameRules.RuleKey<IntegerValue> damageDecayRate;
	public static GameRules.RuleKey<IntegerValue> hordeGlow;
	
	public static void register() {
		handleThirst = GameRules.register("handleThirst", Category.PLAYER, createBoolean(true));
		damageDecayRate = GameRules.register("damageDecayRate", Category.MISC, createInt(-1));
		hordeGlow = GameRules.register("hordeGlow", Category.MOBS, createInt(0));	
	}
	

	public static GameRules.RuleType<GameRules.BooleanValue> createBoolean(boolean defaultValue){
		if(m_create_boolean == null) {
			m_create_boolean = ObfuscationReflectionHelper.findMethod(GameRules.BooleanValue.class, "create", boolean.class);
		}
		try {
			return (RuleType<BooleanValue>) m_create_boolean.invoke(null, defaultValue);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static GameRules.RuleType<GameRules.IntegerValue> createInt(int defaultValue){
		if(m_create_int == null) {
			m_create_int = ObfuscationReflectionHelper.findMethod(GameRules.IntegerValue.class, "create", int.class);
		}
		try {
			return (RuleType<IntegerValue>) m_create_int.invoke(null, defaultValue);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
