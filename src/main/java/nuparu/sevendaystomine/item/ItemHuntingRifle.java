package nuparu.sevendaystomine.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.init.ModItems;

public class ItemHuntingRifle extends ItemGun {

	public ItemHuntingRifle() {
		super();
		this.setMaxAmmo(1);
		this.setFullDamage(60f);
		this.setSpeed(24f);
		this.setRecoil(3.2f);
		this.setCounterDef(0);
		this.setCross(16);
		this.setReloadTime(500);
		this.setDelay(2);
		setFOVFactor(1.38f);
		this.setType(EnumGun.RIFLE);
		this.setLength(EnumLength.LONG);
		this.setWield(EnumWield.TWO_HAND);
		this.setAimPosition(-0.43, 0.1, 0);
	}

	@Override
	public Item getReloadItem(ItemStack stack) {
		return ModItems.SEVEN_MM_BULLET.get();
	}

	@Override
	public float getShotSoundVolume() {
		return 1F;
	}

	@Override
	public float getShotSoundPitch() {
		return 1F / (random.nextFloat() * 0.4F + 1.2F) + 1f * 0.5F;
	}

	@Override
	public SoundEvent getReloadSound() {
		return ModSounds.HUNTING_RIFLE_RELOAD.get();
	}

	@Override
	public SoundEvent getShotSound() {
		return ModSounds.HUNTING_RIFLE_SHOT.get();
	}

	@Override
	public SoundEvent getDrySound() {
		return ModSounds.PISTOL_DRYSHOT.get();
	}
	
	@Override
	public Vector3d getMuzzleFlashPositionMain() {
		return new Vector3d(-0.04, 0.42, -1.8);
	}
	@Override
	public Vector3d getMuzzleFlashPositionSide() {
		return new Vector3d(-0.04, 0.42, -1.8);
	}
	@Override
	public Vector3d getMuzzleFlashAimPosition() {
		return new Vector3d(0.18, 0.35, -1.8);
	}
	
	@Override
	public double getMuzzleFlashSize() {
		return 1;
	}

}
