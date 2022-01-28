package nuparu.sevendaystomine.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.init.ModItems;

public class ItemAK47 extends ItemGun {

	public ItemAK47() {
		super();
		this.setMaxAmmo(30);
		this.setFullDamage(80f);
		this.setSpeed(24f);
		this.setRecoil(3f);
		this.setCounterDef(0);
		this.setCross(24);
		this.setReloadTime(1500);
		this.setDelay(6);
		this.setFOVFactor(1.35f);
		this.setType(EnumGun.RIFLE);
		this.setLength(EnumLength.LONG);
		this.setWield(EnumWield.TWO_HAND);
		this.setAimPosition(-0.43, 0.12, 0);
		this.setIdleAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"rifle_idle"));
		this.setShootAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"rifle_shoot"));
		this.setReloadAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"pistol_reload"));
		this.setAimAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"ak47_aim_idle"));
		this.setAimShootAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"ak47_aim_shoot"));
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
		return ModSounds.AK47_SHOT.get();
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
		return new Vector3d(0.18, 0.25, -1.8);
	}
	
	@Override
	public double getMuzzleFlashSize() {
		return 1;
	}

}
