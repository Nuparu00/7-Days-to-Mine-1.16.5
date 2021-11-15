package nuparu.sevendaystomine.potions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.EffectType;
import net.minecraft.world.Difficulty;
import nuparu.sevendaystomine.util.DamageSources;

public class PotionAlcoholPoison extends PotionBase {

	public PotionAlcoholPoison(EffectType type, int color) {
		super(type, color);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
				"0d9a52e1-adc0-4c8b-bdad-3fe38a5d2657", -0.1D, AttributeModifier.Operation.MULTIPLY_BASE);
		this.addAttributeModifier(Attributes.MAX_HEALTH,
				"1acf3189-a3be-48d6-a8cb-9a3181c5ad36", -0.1, AttributeModifier.Operation.MULTIPLY_BASE);

	}

	@Override
	public void applyEffectTick(LivingEntity entity, int p_76394_2_) {
		if (entity.level.getDifficulty() == Difficulty.PEACEFUL)
			return;
		if (entity.level.random.nextInt(10) == 0) {
			entity.hurt(DamageSources.mercuryPoison, 1);

		}
	}
	
	@Override
	public boolean isDurationEffectTick(int p_76397_1_, int p_76397_2_) {
		return true;
	}

}
