package nuparu.sevendaystomine.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.init.ModItems;

public class ItemShotgun extends ItemGun {

	public ItemShotgun() {
		super();
		this.setMaxAmmo(4);
		this.setProjectiles(10);
		this.setFullDamage(35);
		this.setSpeed(1);
		this.setRecoil(4);
		this.setCounterDef(0);
		this.setCross(30);
		this.setReloadTime(1500);
		this.setDelay(10);
		setFOVFactor(1.2f);
		this.setType(EnumGun.SHOTGUN);
		this.setLength(EnumLength.LONG);
		this.setWield(EnumWield.TWO_HAND);
		this.setAimPosition(-0.41, 0.1, 0);
		this.setIdleAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"rifle_idle"));
		this.setShootAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"shotgun_shoot"));
		this.setReloadAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"pistol_reload"));
	}
	
	public Item getReloadItem(ItemStack stack) {
		return ModItems.SHOTGUN_SHELL.get();
	}
	
	@Override
	public SoundEvent getReloadSound() {
		return ModSounds.AK47_RELOAD.get();
	}

	@Override
	public SoundEvent getShotSound() {
		return ModSounds.SHOTGUN_SHOT.get();
	}

	@Override
	public SoundEvent getDrySound() {
		return ModSounds.PISTOL_DRYSHOT.get();
	}

	@Override
	public Vector3d getMuzzleFlashPositionMain() {
		return new Vector3d(-0.05, 0.35, -1.8);
	}
	@Override
	public Vector3d getMuzzleFlashPositionSide() {
		return new Vector3d(-0.05, 0.35, -1.8);
	}
	@Override
	public Vector3d getMuzzleFlashAimPosition() {
		return new Vector3d(0.15, 0.35, -1.4);
	}
	
	@Override
	public double getMuzzleFlashSize() {
		return 1;
	}
	
}
