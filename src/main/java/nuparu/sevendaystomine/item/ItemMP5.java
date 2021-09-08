package nuparu.sevendaystomine.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.init.ModItems;

public class ItemMP5 extends ItemGun {

	public ItemMP5() {
		super();
		this.setMaxAmmo(30);
		this.setFullDamage(40f);
		this.setSpeed(18f);
		this.setRecoil(2.3f);
		this.setCounterDef(0);
		this.setCross(24);
		this.setReloadTime(2000);
		this.setDelay(4);
		setFOVFactor(1.3f);
		this.setType(EnumGun.SUBMACHINE);
		this.setLength(EnumLength.LONG);
		this.setWield(EnumWield.TWO_HAND);
		this.setAimPosition(0.08, 0, -0.5);
		this.setIdleAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"hunting_rifle_idle"));
		this.setShootAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"mp5_shoot"));
		this.setReloadAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"pistol_reload"));
	}
	
	@Override
	public Item getReloadItem(ItemStack stack) {
		return ModItems.TEN_MM_BULLET.get();
	}
	
	@Override
	public SoundEvent getReloadSound() {
		return ModSounds.AK47_RELOAD.get();
	}

	@Override
	public SoundEvent getShotSound() {
		return ModSounds.MP5_SHOT.get();
	}

	@Override
	public SoundEvent getDrySound() {
		return ModSounds.PISTOL_DRYSHOT.get();
	}
	
	@Override
	public float getShotSoundVolume() {
		return 1F;
	}
	
	@Override
	public float getShotSoundPitch() {
		return 3.0F / (random.nextFloat() * 0.4F + 1.2F) * 0.5F;
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
		return new Vector3d(-0.35, 0.35, -1.8);
	}
	
	@Override
	public double getMuzzleFlashSize() {
		return 1;
	}

}
