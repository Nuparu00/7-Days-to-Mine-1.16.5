package nuparu.sevendaystomine.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.util.MathUtils;

public class ItemMagnum extends ItemGun {

	public ItemMagnum() {
		super();
		this.setMaxAmmo(6);
		this.setFullDamage(50F);
		this.setSpeed(15f);
		this.setRecoil(2.76f);
		this.setCounterDef(0);
		this.setCross(20);
		this.setReloadTime(2500);
		this.setDelay(15);
		setFOVFactor(1.25f);
		this.setType(EnumGun.PISTOL);
		this.setLength(EnumLength.SHORT);
		this.setWield(EnumWield.DUAL);
		this.setAimPosition(-0.229, 0.026,-0.25);
		this.setIdleAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"pistol_idle"));
		this.setShootAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"magnum_shoot"));
		this.setReloadAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"pistol_reload"));
	}

	public Item getReloadItem(ItemStack stack) {
		return ModItems.MAGNUM_BULLET.get();
	}
	
	@Override
	public SoundEvent getReloadSound() {
		return ModSounds.PISTOL_RELOAD.get();
	}

	@Override
	public SoundEvent getShotSound() {
		return ModSounds.MAGNUM_SHOT.get();
	}

	@Override
	public SoundEvent getDrySound() {
		return ModSounds.PISTOL_DRYSHOT.get();
	}
	
	@Override
	public Vector3d getMuzzleFlashPositionMain() {
		return new Vector3d(-0, 0.45, -0.6);
	}
	@Override
	public Vector3d getMuzzleFlashPositionSide() {
		return new Vector3d(-0.5, 0.45, -0.6);
	}
	@Override
	public Vector3d getMuzzleFlashAimPosition() {
		return new Vector3d(-0.05, 0.45, -0.6);
	}
	
	@Override
	public double getMuzzleFlashSize() {
		return 0.68;
	}
	
	@Override
	public float getShotSoundVolume() {
		return 1F;
	}
	
	@Override
	public float getShotSoundPitch() {
		return MathUtils.getFloatInRange(0.7f, 0.9f);
	}

}
