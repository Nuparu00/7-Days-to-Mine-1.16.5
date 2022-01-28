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

public class ItemM4 extends ItemGun {

	public ItemM4() {
		super();
		this.setMaxAmmo(30);
		this.setFullDamage(65f);
		this.setSpeed(22f);
		this.setRecoil(2.5f);
		this.setCounterDef(0);
		this.setCross(22);
		this.setReloadTime(1500);
		this.setDelay(7);
		setFOVFactor(1.37f);
		this.setType(EnumGun.RIFLE);
		this.setLength(EnumLength.LONG);
		this.setWield(EnumWield.TWO_HAND);
		this.setAimPosition(-0.025, -0.025, -0.4);
		this.setIdleAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"rifle_idle"));
		this.setShootAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"rifle_shoot"));
		this.setReloadAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"pistol_reload"));
		this.setAimAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"m4_aim_idle"));
		this.setAimShootAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"m4_aim_shoot"));
	}
	
	@Override
	public Item getReloadItem(ItemStack stack) {
		return ModItems.SEVEN_MM_BULLET.get();
	}
	
	@Override
	public SoundEvent getReloadSound() {
		return ModSounds.AK47_RELOAD.get();
	}

	@Override
	public SoundEvent getShotSound() {
		return ModSounds.M4_SHOT.get();
	}

	@Override
	public SoundEvent getDrySound() {
		return ModSounds.PISTOL_DRYSHOT.get();
	}
	
	@Override
	public Vector3d getMuzzleFlashPositionMain() {
		return new Vector3d(0.08, 0.28, -1.8);
	}
	@Override
	public Vector3d getMuzzleFlashPositionSide() {
		return new Vector3d(-0.04, 0.42, -1.8);
	}
	@Override
	public Vector3d getMuzzleFlashAimPosition() {
		return new Vector3d(-0.25, 0.25, -1.8);
	}
	
	@Override
	public double getMuzzleFlashSize() {
		return 1;
	}
	
	@Override
	public float getShotSoundVolume() {
		return 1F;
	}
	
	@Override
	public float getShotSoundPitch() {
		return MathUtils.getFloatInRange(1f, 1.2f);
	}

}
