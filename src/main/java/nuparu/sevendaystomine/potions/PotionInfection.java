package nuparu.sevendaystomine.potions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.EffectType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.util.RenderUtils;

public class PotionInfection extends PotionBase {

	public PotionInfection(EffectType type, int color) {
		super(type, color);
		this.addAttributeModifier(Attributes.MAX_HEALTH, "96a8df9e-bf44-11e7-abc4-cec278b6b50a",
				-0.1D,
				AttributeModifier.Operation.MULTIPLY_TOTAL);

	}

	/*@Override
	@OnlyIn(Dist.CLIENT)
	public void renderInventoryEffect(Effect effect, net.minecraft.client.gui.Gui gui, int x, int y, float z) {
		super.renderInventoryEffect(effect, gui, x, y, z);
		if (effect.getAmplifier() > 3) {
			String s1 = I18n.format(effect.getPotion().getName());
			RenderUtils.drawString(I18n.format("enchantment.level."+(effect.getAmplifier()+1)), x + 28 + Minecraft.getInstance().font.width(s1 + " "), y + 6,
					0xffffff, true);
		}
	}*/

}
