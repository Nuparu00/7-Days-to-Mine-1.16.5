package nuparu.sevendaystomine.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.entity.FlameEntity;
import nuparu.sevendaystomine.entity.ShotEntity;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.ApplyRecoilMessage;

public class ItemFlamethrower extends ItemGun {

	public ItemFlamethrower() {
		super();
		setMaxAmmo(500);
		setFullDamage(20f);
		setSpeed(0.5f);
		setRecoil(3f);
		setCounterDef(0);
		setCross(22);
		setReloadTime(1500);
		setDelay(1);
		setProjectiles(10);
		setShotsPerAmmo(50);
		setType(EnumGun.RIFLE);
		setLength(EnumLength.LONG);
		setWield(EnumWield.TWO_HAND);
		this.setIdleAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"rifle_idle"));
		this.setShootAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"flamethrower_shoot"));
		this.setReloadAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"pistol_reload"));
	}

	@Override
	public Item getReloadItem(ItemStack stack) {
		return ModItems.GAS_CANISTER.get();
	}

	@Override
	public SoundEvent getReloadSound() {
		return ModSounds.AK47_RELOAD.get();
	}

	@Override
	public SoundEvent getShotSound() {
		return ModSounds.FLAMETHROWER_SHOT.get();
	}

	@Override
	public SoundEvent getDrySound() {
		return ModSounds.PISTOL_DRYSHOT.get();
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {

		ItemStack itemstack = playerIn.getItemInHand(handIn);

		/*
		 * if (handIn == Hand.OFF_HAND && (gunWield != EnumWield.DUAL &&
		 * playerIn.getMainHandItem() != ItemStack.EMPTY)) { return new
		 * ActionResult<ItemStack>(ActionResult.FAIL, itemstack); }
		 */

		if ((getWield() == EnumWield.TWO_HAND && !playerIn.getItemInHand(getOtherHand(handIn)).isEmpty())) {
			return ActionResult.fail(itemstack);
		}
		if (itemstack.isEmpty() || itemstack.getOrCreateTag() == null) {
			return ActionResult.fail(itemstack);
		}
		CompoundNBT nbt = itemstack.getOrCreateTag();
		if (!nbt.contains("Ammo") || !nbt.contains("Capacity") || !nbt.contains("NextFire")) {
			return ActionResult.fail(itemstack);
		}
		if (itemstack.getOrCreateTag().getLong("NextFire") > worldIn.getGameTime()
				|| itemstack.getOrCreateTag().getBoolean("Reloading")) {
			return ActionResult.fail(itemstack);
		}

		int ammo = nbt.getInt("Ammo");
		boolean flag = playerIn.isCreative();
		if (ammo > 0 || flag) {
			float velocity = getSpeed() * (1f + ((float) getQuality(itemstack) / (float) CommonConfig.maxQuality.get()));
			for (int i = 0; i < getProjectiles(); i++) {
				FlameEntity shot = new FlameEntity(playerIn,worldIn);
				float spread = ((float) getSpread(playerIn, handIn) / (playerIn.isCrouching() ? 1.5f : 1f));
				shot.shootFromRotation(playerIn, playerIn.xRot, playerIn.yRot, 0.0F, velocity,spread*23.5f);
				if (!worldIn.isClientSide()) {
					shot.setDamage(getFinalDamage(itemstack));
					worldIn.addFreshEntity(shot);
				}
			}
			worldIn.playSound(null, playerIn.blockPosition(), getShotSound(), SoundCategory.PLAYERS, getShotSoundVolume(),
					getShotSoundPitch());
			playerIn.swing(handIn);
			if (playerIn instanceof ServerPlayerEntity) {
				PacketManager.sendTo(PacketManager.applyRecoil, new ApplyRecoilMessage(getRecoil(),handIn==Hand.MAIN_HAND, false), (ServerPlayerEntity) playerIn);
				itemstack.hurt(1, random, (ServerPlayerEntity) playerIn);
			}

			if (!flag) {
				itemstack.getOrCreateTag().putInt("Ammo", ammo - 1);
			}

			itemstack.getOrCreateTag().putLong("NextFire", worldIn.getGameTime() + getDelay());

			return ActionResult.success(itemstack);
		} else {
			worldIn.playSound(null, playerIn.blockPosition(), getDrySound(), SoundCategory.PLAYERS, 0.3F,
					1.0F / (random.nextFloat() * 0.4F + 1.2F) + 1f * 0.5F);
			itemstack.getOrCreateTag().putLong("NextFire", worldIn.getGameTime() + (getDelay() / 2));
		}
		return ActionResult.fail(itemstack);
	}

}
