package nuparu.sevendaystomine.potions;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;

public class PotionBase extends Effect {

	public static final ResourceLocation textureAtlas = new ResourceLocation(SevenDaysToMine.MODID,"textures/gui/status-icons.png");

	public PotionBase(EffectType type, int color) {
		super(type, color);

	}
/*
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean hasStatusIcon() {
		Minecraft.getInstance().getTextureManager().bind(textureAtlas);
		return true;
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;

	}*/
}