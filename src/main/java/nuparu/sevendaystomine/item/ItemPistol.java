package nuparu.sevendaystomine.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.init.ModItems;

public class ItemPistol extends ItemGun {

	public ItemPistol() {
		super();
		this.setMaxAmmo(15);
		this.setFullDamage(30F);
		this.setSpeed(10f);
		this.setRecoil(0.61f);
		this.setCounterDef(0);
		this.setCross(20);
		this.setReloadTime(1900);
		this.setDelay(6);
		this.setFOVFactor(1.25f);
		this.setType(EnumGun.PISTOL);
		this.setLength(EnumLength.SHORT);
		this.setWield(EnumWield.DUAL);
		this.setAimPosition(-0.195, 0.1,-0.25);
		this.setIdleAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"pistol_idle"));
		this.setShootAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"pistol_shoot"));
		this.setReloadAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"pistol_reload"));
	}

	public Item getReloadItem(ItemStack stack) {
		return ModItems.NINE_MM_BULLET.get();
	}
	
	@Override
	public SoundEvent getReloadSound() {
		return ModSounds.PISTOL_RELOAD.get();
	}

	@Override
	public SoundEvent getShotSound() {
		return ModSounds.PISTOL_SHOT.get();
	}

	@Override
	public SoundEvent getDrySound() {
		return ModSounds.PISTOL_DRYSHOT.get();
	}
	
	@Override
	public Vector3d getMuzzleFlashPositionMain() {
		return new Vector3d(0.03, 0.4, -0.2);
	}
	@Override
	public Vector3d getMuzzleFlashPositionSide() {
		return new Vector3d(-0.56, 0.4, -0.2);
	}
	@Override
	public Vector3d getMuzzleFlashAimPosition() {
		return new Vector3d(-0.05, 0.4, -0.15);
	}
	
	@Override
	public double getMuzzleFlashSize() {
		return 0.5;
	}

}
